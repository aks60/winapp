package estimate;

import estimate.model.ElemGlass;
import estimate.model.AreaStvorka;
import estimate.model.AreaArch;
import estimate.model.AreaTriangl;
import estimate.model.ElemJoining;
import estimate.model.AreaSimple;
import estimate.model.AreaRectangl;
import estimate.model.AreaTrapeze;
import estimate.model.ElemImpost;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dataset.Query;
import dataset.Record;
import domain.eArtikl;
import domain.eSyssize;
import domain.eSyspar1;
import domain.eSysprof;
import enums.LayoutArea;
import enums.TypeElem;
import enums.UseArtiklTo;
import estimate.constr.Cal5e;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import estimate.constr.Specification;
import estimate.constr.Tariffication;
import estimate.constr.Joining;
import estimate.constr.Elements;
import estimate.constr.Filling;
import estimate.constr.Furniture;
import estimate.model.Com5t;
import estimate.model.ElemSimple;
import estimate.script.Intermediate;
import frames.swing.Draw;

public class Wincalc {

    public Connection conn;
    public Integer nuni = 0;
    public int prj = -1;
    public Record artiklRec = null; //главный артикл системы профилей
    public Record syssizeRec = null; //константы    
    public float genId = 100; //генерация ключа спецификации

    public float width = 0.f;     //ширина окна
    public float height = 0.f;    //высота окна
    public float heightAdd = 0.f; //арка, трапеция, треугольник
    public final int colorNone = 1005;  //без цвета (возвращаемое значение по умолчанию)
    public int colorID1 = -1; //базовый цвет
    public int colorID2 = -1; //внутренний цвет
    public int colorID3 = -1; //внещний цвет

    public byte[] bufferByte = null; //буффер рисунка
    public BufferedImage bufferImg = null;  //образ рисунка
    public Graphics2D gc2d = null; //графический котекст рисунка  
    public float scale1 = 1, scale2 = 20; //коэффициент сжатия
    public String labelSketch = "empty"; //надпись на эскизе

    public AreaSimple rootArea = null;
    public HashMap<Integer, Record> mapParamDef = new HashMap(); //параметры по умолчанию           
    public LinkedList<Com5t> listCom5t; //список всех Component
    public LinkedList<ElemSimple> listElem; //список ElemSimple
    public HashMap<String, ElemJoining> mapJoin = new HashMap(); //список соединений рам и створок 
    public ArrayList<Specification> listSpec = new ArrayList(); //спецификация
    public Cal5e calcElements, calcJoining, calcFilling, calcFurniture, tariffication; //объекты калькуляции конструктива
    
    public AreaSimple build(String productJson) {
        //System.out.println(productJson);
        mapParamDef.clear();
        mapJoin.clear();
        listSpec.clear();
        genId = 100;

        //Парсинг входного скрипта
        parsingScript(productJson);

        //Загрузим параметры по умолчанию
        eSyspar1.find(nuni).stream().forEach(rec -> mapParamDef.put(rec.getInt(eSyspar1.grup), rec));

        //Соединения 
        rootArea.joinFrame(); //соединения рамы
        rootArea.joinElem(); //T-соединения рамы 
        LinkedList<AreaStvorka> listAreaStv = rootArea.listElem(TypeElem.STVORKA); //список створок
        listAreaStv.stream().forEach(area5e -> area5e.joinFrame());  //соединения створок

        //Список элементов, (важно! получаем после построения створки)
        listElem = rootArea.listElem(TypeElem.FRAME_SIDE, TypeElem.STVORKA_SIDE, TypeElem.IMPOST, TypeElem.GLASS);
        Collections.sort(listElem, Collections.reverseOrder((a, b) -> Float.compare(a.id(), b.id())));
        Draw.iwin = this;   
        return rootArea;
    }

    //Конструктив и тарификация 
    public void constructiv() {
        try {

            Query.conf = "calc";           
            calcElements = new Elements(this); //составы
            calcElements.calc();
            calcJoining = new Joining(this); //соединения
            calcJoining.calc();            
            calcFilling = new Filling(this); //заполнения
            calcFilling.calc();            
            calcFurniture = new Furniture(this); //фурнитура 
            calcFurniture.calc();
            tariffication = new Tariffication(this); //тарификация
            tariffication.calc();
            for (ElemSimple elemRec : listElem) {
                listSpec.add(elemRec.specificationRec);
                listSpec.addAll(elemRec.specificationRec.specificationList);
            }
            Collections.sort(listSpec, (o1, o2) -> (o1.place.subSequence(0, 3) + o1.name).compareTo(o2.place.subSequence(0, 3) + o2.name));

        } catch (Exception e) {
            System.out.println("Ошибка калькуляции конструктива Wincalc.constructiv(" + e);
        } finally {
            Query.conf = "app";
        }
    }

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

            Record sysprofRec = eSysprof.find2(nuni, UseArtiklTo.FRAME);
            artiklRec = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), true);
            syssizeRec = eSyssize.find(artiklRec.getInt(eArtikl.syssize_id));

            colorID1 = jsonObj.get("color1").getAsInt();
            colorID2 = jsonObj.get("color2").getAsInt();
            colorID3 = jsonObj.get("color3").getAsInt();

            //Главное окно
            String layoutObj = jsonObj.get("layoutArea").getAsString();
            String elemType = jsonObj.get("elemType").getAsString();
            Intermediate intermediateRoot = new Intermediate(null, id, elemType, layoutObj, width, height, paramJson);
            listIntermediate.add(intermediateRoot);
            
            //Добавим все остальные Intermediate
            intermBuild(jsonObj, intermediateRoot, listIntermediate); 
            Collections.sort(listIntermediate, (o1, o2) -> Float.compare(o1.id, o2.id)); //упорядочим порядок построения окна
            windowsBuild(listIntermediate); //строим конструкцию из промежуточного списка

        } catch (Exception e) {
            System.err.println("Ошибка Wincalc.parsingScript() " + e);
        }
    }

    //Промежуточный список окна (для последовательности построения)
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
                rootArea = new AreaRectangl(this, null, imdRoot.id, TypeElem.RECTANGL, imdRoot.layout, imdRoot.width, imdRoot.height, colorID1, colorID2, colorID3, imdRoot.param); //простое
            } else if (TypeElem.TRAPEZE == imdRoot.type) {
                rootArea = new AreaTrapeze(this, null, imdRoot.id, TypeElem.TRAPEZE, imdRoot.layout, imdRoot.width, imdRoot.height, colorID1, colorID2, colorID3, imdRoot.param); //трапеция
            } else if (TypeElem.TRIANGL == imdRoot.type) {
                rootArea = new AreaTriangl(this, null, imdRoot.id, TypeElem.TRIANGL, imdRoot.layout, imdRoot.width, imdRoot.height, colorID1, colorID2, colorID3, imdRoot.param); //треугольник
            } else if (TypeElem.ARCH == imdRoot.type) {
                rootArea = new AreaArch(this, null, imdRoot.id, TypeElem.ARCH, imdRoot.layout, imdRoot.width, imdRoot.height, colorID1, colorID2, colorID3, imdRoot.param); //арка
            }
            imdRoot.area5e = rootArea;

            //Цикл по элементам конструкции ранж. по ключам.
            for (int index = 1; index < listIntermediate.size(); index++) {
                Intermediate imd = listIntermediate.get(index);
                if (TypeElem.STVORKA == imd.type) {
                    imd.addArea(new AreaStvorka(this, imd.owner.area5e, imd.id, imd.param));
                } else if (TypeElem.AREA == imd.type) {
                    imd.addArea(new AreaSimple(this, imd.owner.area5e, imd.id, imd.type, imd.layout, imd.width, imd.height, -1, -1, -1, null)); //простое
                } else if (TypeElem.IMPOST == imd.type) {
                    imd.addElem(new ElemImpost(imd.owner.area5e, imd.id));
                } else if (TypeElem.GLASS == imd.type) {
                    imd.addElem(new ElemGlass(imd.owner.area5e, imd.id, imd.param));
                }
            }
        } catch (Exception e) {
            System.err.println("wincalc.Wincalc.windowsBuild() " + e);
        }
    }
}
