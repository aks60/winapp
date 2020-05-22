package calculate;

import calculate.model.ElemGlass;
import calculate.model.ElemFrame;
import calculate.model.AreaStvorka;
import calculate.model.AreaArch;
import calculate.model.AreaTriangl;
import calculate.model.ElemJoining;
import calculate.model.AreaSimple;
import calculate.model.AreaRectangl;
import calculate.model.AreaTrapeze;
import calculate.model.ElemImpost;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dataset.Record;
import domain.eArtikl;
import domain.eSyssize;
import domain.eSyspar1;
import domain.eSysprof;
import enums.LayoutArea;
import enums.UseSide;
import enums.TypeElem;
import enums.UseArtiklTo;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import calculate.constr.Specification;
import calculate.constr.Tariffication;
import calculate.constr.Joining;
import calculate.constr.Elements;
import calculate.model.Com5t;
import calculate.model.ElemSimple;
import calculate.script.Intermediate;

public class Wincalc {

    public Connection conn;
    public Integer nuni = 0;
    public Record artiklRec = null;  //главный артикл системы профилей
    public Record sysconsRec = null; //константы
    public float genId = 100; //генерация ключа спецификации

    public float width = 0.f;     //ширина окна
    public float height = 0.f;    //высота окна
    public float heightAdd = 0.f; //арка, трапеция, треугольник
    public final int colorNone = 1005;  //без цвета (возвращаемое значение по умолчанию)
    public int color1 = -1; //базовый цвет
    public int color2 = -1; //внутренний цвет
    public int color3 = -1; //внещний цвет

    public byte[] bufferByte = null; //буффер рисунка
    public BufferedImage bufferImg = null;  //образ рисунка
    public Graphics2D gc2d = null; //графический котекст рисунка  
    public float scale1 = 1, scale2 = 20; //коэффициент сжатия
    public String labelSketch = "empty"; //надпись на эскизе

    public AreaSimple rootArea = null;
    public HashMap<Integer, Object[]> mapParamDef = new HashMap(); //параметры по умолчанию       
    public LinkedList<Com5t> listCom5t; //список всех Com5t
    public LinkedList<ElemSimple> listElem; //список ElemSimple
    public HashMap<String, ElemJoining> mapJoin = new HashMap(); //список соединений рам и створок 
    public ArrayList<Specification> listSpec = new ArrayList(); //спецификация
    protected Tariffication tariffication = new Tariffication(this); //тарификация
//==============================================================================    

    public AreaSimple create(String productJson) {

        mapParamDef.clear();
        mapJoin.clear();
        listSpec.clear();
        genId = 100;

        //Парсинг входного скрипта
        parsingScript(productJson);

        //Загрузим параметры по умолчанию
        ArrayList<Record> syspar1List = eSyspar1.find(nuni);
        syspar1List.stream().forEach(record -> mapParamDef.put(record.getInt(eSyspar1.grup),
                new Object[]{record.getStr(eSyspar1.text), record.getInt(eSyspar1.numb)}));

        //Соединения 
        rootArea.joinFrame();  //соединения рамы
        rootArea.joinElem(); //T-соединения рамы 
        LinkedList<AreaStvorka> listAreaStv = rootArea.listElem(TypeElem.STVORKA); //список створок
        listAreaStv.stream().forEach(area5e -> area5e.joinFrame());  //соединения створок

        //Список элементов, (важно! получаем после построения створки)
        listElem = rootArea.listElem(TypeElem.FRAME_SIDE, TypeElem.STVORKA_SIDE, TypeElem.IMPOST, TypeElem.GLASS);
        Collections.sort(listElem, Collections.reverseOrder((a, b) -> Float.compare(a.id(), b.id())));

        //Тестирование                
        //listSpec.stream().forEach(rec -> System.out.println(rec));
        //System.out.println(productJson); //вывод на консоль json
        //mapJoin.entrySet().forEach(it -> System.out.println(it.getKey() + ":  id=" + it.getValue().id + "  " + it.getValue()));            
        return rootArea;
    }

    //Конструктив и тарификация 
    public void constructiv() {
        try {
            Elements elements = new Elements(this); //составы
            elements.build();
            Joining joining = new Joining(this); //соединения
            //joining.build();
            /*  Filling filling = new Filling(iwin, this); //заполнения
            filling.build();
            Accessory accessory = new Accessory(iwin, this); //фурнитура        
            constructiv.kitsFirst();                       //комплекты */
            for (ElemSimple elemRec : listElem) {

                listSpec.add(elemRec.specificationRec);
                listSpec.addAll(elemRec.specificationRec.specificationList);
            }
        } catch (Exception e) {
            System.out.println("Ошибка калькуляции конструктива Wincalc.constructiv(" + e);
        }
    }
//==============================================================================

    // Парсим входное json окно и строим объектную модель окна
    private void parsingScript(String json) {
        try {
            Gson gson = new Gson(); //библиотека jso
            JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
            JsonObject jsonObj = jsonElement.getAsJsonObject();
            LinkedList<Intermediate> listIntermediate = new LinkedList();

            int id = jsonObj.get("id").getAsInt();
            String paramJson = jsonObj.get("paramJson").getAsString();
            nuni = jsonObj.get("nuni").getAsInt();

            width = jsonObj.get("width").getAsFloat();
            height = jsonObj.get("height").getAsFloat();
            heightAdd = jsonObj.get("heightAdd").getAsFloat();

//            Record sysprofRec = eSysprof.find3(nuni, UseArtiklTo.FRAME, UseSide.LEFT);
            Record sysprofRec = eSysprof.find2(nuni, UseArtiklTo.FRAME);
            artiklRec = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), true);
            sysconsRec = eSyssize.find(artiklRec.getInt(eArtikl.syssize_id));

            color1 = jsonObj.get("color1").getAsInt();
            color2 = jsonObj.get("color2").getAsInt();
            color3 = jsonObj.get("color3").getAsInt();

            //Главное окно
            String layoutObj = jsonObj.get("layoutArea").getAsString();
            String elemType = jsonObj.get("elemType").getAsString();
            Intermediate intermediateRoot = new Intermediate(null, id, elemType, layoutObj, width, height, paramJson);
            listIntermediate.add(intermediateRoot);

            //Добавим рамы в список
            for (Object elemFrame : jsonObj.get("elements").getAsJsonArray()) {
                JsonObject jsonFrame = (JsonObject) elemFrame;
                if (TypeElem.FRAME_SIDE.name().equals(jsonFrame.get("elemType").getAsString())) {

                    String layourFrame = jsonFrame.get("layoutFrame").getAsString();
                    listIntermediate.add(new Intermediate(intermediateRoot, jsonFrame.get("id").getAsInt(), TypeElem.FRAME_SIDE.name(), layourFrame, null));
                }
            }

            intermBuild(jsonObj, intermediateRoot, listIntermediate); //добавим все остальные Intermediate

            Collections.sort(listIntermediate, (o1, o2) -> Float.compare(o1.id, o2.id)); //упорядочим порядок построения окна
            windowsBuild(listIntermediate); //строим конструкцию из промежуточного списка

        } catch (Exception e) {
            System.err.println("Ошибка Wincalc.parsingScript() " + e);
        }
    }

    //Промежуточный список построения окна (для последовательности построения)
    private void intermBuild(JsonObject jso, Intermediate owner, LinkedList<Intermediate> listIntermediate) {
        try {
            for (Object obj : jso.get("elements").getAsJsonArray()) {
                JsonObject objArea = (JsonObject) obj;
                int id = objArea.get("id").getAsInt();
                String type = objArea.get("elemType").getAsString();
                String param = (objArea.get("paramJson") != null) ? objArea.get("paramJson").getAsString() : null;
                if (TypeElem.AREA.name().equals(type) || TypeElem.STVORKA.name().equals(type)) {

                    float width = (owner.layout == LayoutArea.VERT) ? owner.width : objArea.get("width").getAsFloat();
                    float height = (owner.layout == LayoutArea.VERT) ? objArea.get("height").getAsFloat() : owner.height;
                    String layout = objArea.get("layoutArea").getAsString();

                    Intermediate intermediateBox = new Intermediate(owner, id, type, layout, width, height, param);
                    listIntermediate.add(intermediateBox);

                    intermBuild(objArea, intermediateBox, listIntermediate); //рекурсия
                } else {

                    if (TypeElem.IMPOST.name().equals(type)) {
                        listIntermediate.add(new Intermediate(owner, id, type, LayoutArea.ANY.name(), param));
                    } else if (TypeElem.GLASS.name().equals(type)) {
                        listIntermediate.add(new Intermediate(owner, id, type, LayoutArea.ANY.name(), param));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("wincalc.Wincalc.intermBuild() " + e);
        }
    }

    //Строим конструкцию из промежуточного списка
    private void windowsBuild(LinkedList<Intermediate> listIntermediate) {
        try {
            //Главное окно        
            Intermediate imdRoot = listIntermediate.getFirst();
            if (TypeElem.RECTANGL == imdRoot.type) {
                rootArea = new AreaRectangl(this, null, imdRoot.id, TypeElem.RECTANGL, imdRoot.layout, imdRoot.width, imdRoot.height, color1, color2, color3, imdRoot.param); //простое
            } else if (TypeElem.TRAPEZE == imdRoot.type) {
                rootArea = new AreaTrapeze(this, null, imdRoot.id, TypeElem.TRAPEZE, imdRoot.layout, imdRoot.width, imdRoot.height, color1, color2, color3, imdRoot.param); //трапеция
            } else if (TypeElem.TRIANGL == imdRoot.type) {
                rootArea = new AreaTriangl(this, null, imdRoot.id, TypeElem.TRIANGL, imdRoot.layout, imdRoot.width, imdRoot.height, color1, color2, color3, imdRoot.param); //треугольник
            } else if (TypeElem.ARCH == imdRoot.type) {
                rootArea = new AreaArch(this, null, imdRoot.id, TypeElem.ARCH, imdRoot.layout, imdRoot.width, imdRoot.height, color1, color2, color3, imdRoot.param); //арка
            }
            imdRoot.area5e = rootArea;

            //Цыкл по элементам конструкции ранж. по ключам.
            for (int index = 1; index < listIntermediate.size(); index++) {
                Intermediate imd = listIntermediate.get(index);

                //Добавим рамы в гпавное окно
                if (TypeElem.FRAME_SIDE == imd.type) {
                    ElemFrame elemFrame = new ElemFrame(rootArea, imd.id, imd.layout);
                    rootArea.mapFrame.put(elemFrame.layout(), elemFrame);
                    continue;
                }
                //Остальные
                if (TypeElem.AREA == imd.type || TypeElem.STVORKA == imd.type) {

                    if (TypeElem.STVORKA == imd.type) {
                        imd.addArea(new AreaStvorka(this, imd.owner.area5e, imd.id, imd.param));

                    } else if (TypeElem.RECTANGL == rootArea.type()) {
                        imd.addArea(new AreaRectangl(this, imd.owner.area5e, imd.id, imd.type, imd.layout, imd.width, imd.height, -1, -1, -1, null)); //простое

                    } else if (TypeElem.TRAPEZE == rootArea.type()) {
                        imd.addArea(new AreaTrapeze(this, imd.owner.area5e, imd.id, imd.type, imd.layout, imd.width, imd.height, -1, -1, -1, null)); //трапеция

                    } else if (TypeElem.TRIANGL == rootArea.type()) {
                        imd.addArea(new AreaTriangl(this, imd.owner.area5e, imd.id, imd.type, imd.layout, imd.width, imd.height, -1, -1, -1, null)); //треугольник

                    } else if (TypeElem.ARCH == rootArea.type()) {
                        imd.addArea(new AreaArch(this, imd.owner.area5e, imd.id, imd.type, imd.layout, imd.width, imd.height, -1, -1, -1, null)); //арка
                    }
                } else {
                    if (TypeElem.IMPOST == imd.type) {
                        imd.addElem(new ElemImpost(imd.owner.area5e, imd.id));

                    } else if (TypeElem.GLASS == imd.type) {
                        imd.addElem(new ElemGlass(imd.owner.area5e, imd.id, imd.param));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("wincalc.Wincalc.windowsBuild() " + e);
        }
    }
}
