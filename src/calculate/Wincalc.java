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
    public LinkedList<AreaSimple> listArea; //список AreaSimple
    public HashMap<String, ElemJoining> mapJoin = new HashMap(); //список соединений рам и створок 
    public ArrayList<Specification> listSpec = new ArrayList(); //спецификация

    //спецификация конструкции
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

        //Инициализация объектов калькуляции
        listArea = rootArea.listElem(TypeElem.AREA); //список контейнеров
        LinkedList<AreaStvorka> listAreaStv = rootArea.listElem(TypeElem.STVORKA); //список створок
        listElem = rootArea.listElem(TypeElem.FRAME_BOX, TypeElem.FRAME_STV, TypeElem.IMPOST); //список элементов
        HashMap<String, HashSet<ElemSimple>> mapClap = new HashMap(); //временно для схлопывания соединений

        //Соединения рамы  
        rootArea.joinFrame();  //обход соединений и кальк. углов главного окна 
        listArea = rootArea.listElem(TypeElem.RECTANGL, TypeElem.ARCH, TypeElem.TRAPEZE, TypeElem.TRIANGL);
        listArea.stream().forEach(area -> area.joinElem()); //обход(схлопывание) соединений окон

        //Соединения створок
//        listAreaStv.stream().forEach(area -> area.joinFrame());
//        listAreaStv.stream().forEach(area -> area.joinElem(mapClap, listElem)); //обход(схлопывание) соединений створok

        //Список элементов, (важно! получаем после построения створки)
        listElem = rootArea.listElem(TypeElem.FRAME_BOX, TypeElem.FRAME_STV, TypeElem.IMPOST, TypeElem.GLASS);
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
            JsonObject mainObj = jsonElement.getAsJsonObject();
            LinkedList<Intermediate> listIntermediate = new LinkedList();

            int id = mainObj.get("id").getAsInt();
            String paramJson = mainObj.get("paramJson").getAsString();
            nuni = mainObj.get("nuni").getAsInt();

            width = mainObj.get("width").getAsFloat();
            height = mainObj.get("height").getAsFloat();
            heightAdd = mainObj.get("heightAdd").getAsFloat();

//            Record sysprofRec = eSysprof.find3(nuni, UseArtiklTo.FRAME, UseSide.LEFT);
            Record sysprofRec = eSysprof.find2(nuni, UseArtiklTo.FRAME);
            artiklRec = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), true);
            sysconsRec = eSyssize.find(artiklRec.getInt(eArtikl.syssize_id));

            color1 = mainObj.get("color1").getAsInt();
            color2 = mainObj.get("color2").getAsInt();
            color3 = mainObj.get("color3").getAsInt();

            //Главное окно
            String layoutObj = mainObj.get("layoutArea").getAsString();
            String elemType = mainObj.get("elemType").getAsString();
            Intermediate intermediate = new Intermediate(null, id, elemType, layoutObj, width, height, paramJson);
            listIntermediate.add(intermediate);

            //Добавим рамы в список
            for (Object elemFrame : mainObj.get("elements").getAsJsonArray()) {
                JsonObject jsonFrame = (JsonObject) elemFrame;
                if (TypeElem.FRAME_BOX.name().equals(jsonFrame.get("elemType").getAsString())) {

                    String layourFrame = jsonFrame.get("layoutFrame").getAsString();
                    listIntermediate.add(new Intermediate(intermediate, jsonFrame.get("id").getAsInt(), TypeElem.FRAME_BOX.name(), layourFrame, null));
                }
            }

            intermBuild(mainObj, intermediate, listIntermediate); //добавим все остальные Intermediate
            Collections.sort(listIntermediate, (o1, o2) -> Float.compare(o1.id, o2.id));
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

                    Intermediate intermediate = new Intermediate(owner, id, type, layout, width, height, param);
                    listIntermediate.add(intermediate);

                    intermBuild(objArea, intermediate, listIntermediate); //рекурсия
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
            Intermediate imf = listIntermediate.getFirst();
            if (TypeElem.RECTANGL == imf.type) {
                rootArea = new AreaRectangl(this, null, imf.id, TypeElem.RECTANGL, imf.layout, imf.width, imf.height, color1, color2, color3, imf.param); //простое
            } else if (TypeElem.TRAPEZE == imf.type) {
                rootArea = new AreaTrapeze(this, null, imf.id, TypeElem.TRAPEZE, imf.layout, imf.width, imf.height, color1, color2, color3, imf.param); //трапеция
            } else if (TypeElem.TRIANGL == imf.type) {
                rootArea = new AreaTriangl(this, null, imf.id, TypeElem.TRIANGL, imf.layout, imf.width, imf.height, color1, color2, color3, imf.param); //треугольник
            } else if (TypeElem.ARCH == imf.type) {
                rootArea = new AreaArch(this, null, imf.id, TypeElem.ARCH, imf.layout, imf.width, imf.height, color1, color2, color3, imf.param); //арка
            }
            imf.area = rootArea;

            //Цыкл по элементам конструкции ранж. по ключам.
            for (int index = 1; index < listIntermediate.size(); index++) {
                Intermediate imd = listIntermediate.get(index);

                //Добавим рамы в гпавное окно
                if (TypeElem.FRAME_BOX == imd.type) {
                    ElemFrame elemFrame = new ElemFrame(rootArea, imd.id, imd.layout);
                    rootArea.mapFrame.put(elemFrame.layout(), elemFrame);
                    continue;
                }
                //Остальные
                if (TypeElem.AREA == imd.type || TypeElem.STVORKA == imd.type) {

                    if (TypeElem.STVORKA == imd.type) {
                        imd.addArea(new AreaStvorka(this, imd.owner.area, imd.id, imd.param));
                    } else {
                        if (TypeElem.RECTANGL == rootArea.type()) {
                            imd.addArea(new AreaRectangl(this, imd.owner.area, imd.id, imd.type, imd.layout, imd.width, imd.height, -1, -1, -1, null)); //простое

                        } else if (TypeElem.TRAPEZE == rootArea.type()) {
                            imd.addArea(new AreaTrapeze(this, imd.owner.area, imd.id, imd.type, imd.layout, imd.width, imd.height, -1, -1, -1, null)); //трапеция

                        } else if (TypeElem.TRIANGL == rootArea.type()) {
                            imd.addArea(new AreaTriangl(this, imd.owner.area, imd.id, imd.type, imd.layout, imd.width, imd.height, -1, -1, -1, null)); //треугольник

                        } else if (TypeElem.ARCH == rootArea.type()) {
                            imd.addArea(new AreaArch(this, imd.owner.area, imd.id, imd.type, imd.layout, imd.width, imd.height, -1, -1, -1, null)); //арка
                        }
                    }
                } else {
                    if (TypeElem.IMPOST == imd.type) {
                        imd.addElem(new ElemImpost(imd.owner.area, imd.id));

                    } else if (TypeElem.GLASS == imd.type) {
                        imd.addElem(new ElemGlass(imd.owner.area, imd.id, imd.param));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("wincalc.Wincalc.windowsBuild() " + e);
        }
    }
}
