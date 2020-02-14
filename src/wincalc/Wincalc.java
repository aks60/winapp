package wincalc;

import wincalc.model.ElemGlass;
import wincalc.model.ElemFrame;
import wincalc.model.AreaStvorka;
import wincalc.model.AreaArch;
import wincalc.model.AreaTriangl;
import wincalc.model.ElemJoining;
import wincalc.model.AreaSimple;
import wincalc.model.AreaSquare;
import wincalc.model.AreaTrapeze;
import wincalc.model.ElemImpost;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dataset.Record;
import domain.eArtikl;
import domain.eSyscons;
import domain.eSyspar1;
import domain.eSysprof;
import enums.LayoutArea;
import enums.ProfileSide;
import enums.TypeElem;
import enums.TypeProfile;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import main.Main;
import wincalc.model.Com5t;
import wincalc.model.ElemSimple;
import wincalc.script.Intermediate;

public class Wincalc {

    public static boolean production = false;
    public Connection conn;

    public Integer nuni = 0;
    public Record artiklRec = null;  //главный артикл системы профилей
    public Record sysconsRec = null; //константы

    public float width = 0.f;     //ширина окна
    public float height = 0.f;    //высота окна
    public float heightAdd = 0.f; //арка, трапеция, треугольник
    protected final int colorNone = 1005;  //без цвета (возвращаемое значение по умолчанию)
    public int color1 = -1; //базовый цвет
    public int color2 = -1; //внутренний цвет
    public int color3 = -1; //внещний цвет

    public byte[] bufferByte = null; //буффер рисунка
    public BufferedImage bufferImg = null;  //образ рисунка
    public Graphics2D gc2d = null; //графический котекст рисунка  
    public float scaleDxy = 1; //коэффициент сжатия
    protected String labelSketch = "empty"; //надпись на эскизе
    protected HashMap<String, LinkedList<Object[]>> drawMapLineList = new HashMap(); //список линий окон 

    public AreaSimple rootArea = null;
    private HashMap<Integer, String> mapPro4Params = new HashMap();
    protected HashMap<Integer, Record> mapParamDef = new HashMap(); //параметры по умолчанию       

    public LinkedList<Com5t> listCom5t; //список всех Com5t
    public LinkedList<ElemSimple> listElem; //список ElemSimple
    public LinkedList<AreaSimple> listArea; //список AreaSimple
    public HashMap<String, ElemJoining> mapJoin = new HashMap(); //список соединений рам и створок 

    protected Intermediate intermediate = null;
    protected LinkedList<Intermediate> listIntermediate = new LinkedList();

    public AreaSimple create(String productJson) {

        mapParamDef.clear();
        mapJoin.clear();
        drawMapLineList.clear();

        //Парсинг входного скрипта
        parsingScript(productJson);

        //Загрузим параметры по умолчанию
        ArrayList<Record> syspar1List = eSyspar1.find(nuni);
        syspar1List.stream().forEach(record -> mapParamDef.put(record.getInt(eSyspar1.pnumb), record));

        //Инициализация объектов калькуляции
        listArea = rootArea.listElem(TypeElem.AREA); //список контейнеров
        LinkedList<AreaStvorka> listStvorka = rootArea.listElem(TypeElem.FULLSTVORKA); //список створок
        listElem = rootArea.listElem(TypeElem.FRAME_BOX, TypeElem.FRAME_STV, TypeElem.IMPOST); //список элементов
        HashMap<String, HashSet<ElemSimple>> mapClap = new HashMap(); //временно для схлопывания соединений

        //Соединения рамы  
        rootArea.joinFrame();  //обход соединений и кальк. углов 
        listArea.stream().forEach(area -> area.joinElem(mapClap, listElem)); //обход(схлопывание) соединений рамы

        //Соединения створок
        listStvorka.stream().forEach(stvorka -> stvorka.correction()); //коррекция размера створки с учётом нахлёста и построение рамы створки
        listStvorka.stream().forEach(area -> area.joinFrame());
        listStvorka.stream().forEach(area -> area.joinElem(mapClap, listElem)); //обход(схлопывание) соединений створки

        //Список элементов, (важно! получаем после построения створки)
        listElem = rootArea.listElem(TypeElem.FRAME_BOX, TypeElem.FRAME_STV, TypeElem.IMPOST, TypeElem.GLASS);
        Collections.sort(listElem, Collections.reverseOrder((a, b) -> {
            return a.getId().compareTo(b.getId());
        }));

        //Конструктив и тарификация
        //Constructiv constructiv = new Constructiv(this); //конструктив
        //constructiv.calculate();
        //Tariffication tariffic = new Tariffication(this); //тарификации
        //tariffic.calculate(listCom5t);
        //Тестирование
        if (Main.dev == true) {
            //System.out.println(productJson); //вывод на консоль json
            //mapJoin.entrySet().forEach(it -> System.out.println(it.getKey() + ":  id=" + it.getValue().id + "  " + it.getValue()));
            listIntermediate.stream().forEach(el -> System.out.println(el));
        }
        return rootArea;
    }

    // Парсим входное json окно и строим объектную модель окна
    private void parsingScript(String json) {

        try {
            Gson gson = new Gson(); //библиотека jso
            JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
            JsonObject mainObj = jsonElement.getAsJsonObject();

            String id = mainObj.get("id").getAsString();
            String paramJson = mainObj.get("paramJson").getAsString();
            nuni = mainObj.get("nuni").getAsInt();

            width = mainObj.get("width").getAsFloat();
            height = mainObj.get("height").getAsFloat();
            heightAdd = mainObj.get("heightAdd").getAsFloat();

            Record sysprofRec = eSysprof.find3(nuni, TypeProfile.FRAME, ProfileSide.LEFT);
            artiklRec = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), true);
            sysconsRec = eSyscons.find(artiklRec.getInt(eArtikl.syscons_id));

            color1 = mainObj.get("color1").getAsInt();
            color2 = mainObj.get("color2").getAsInt();
            color3 = mainObj.get("color3").getAsInt();

            //Определим напрвление построения окна
            String layoutObj = mainObj.get("layoutArea").getAsString();
            String elemType = mainObj.get("elemType").getAsString();
            LayoutArea layoutRoot = ("VERT".equals(layoutObj)) ? LayoutArea.VERT : LayoutArea.HORIZ;

            //Главное окно
            if ("SQUARE".equals(elemType)) {
                rootArea = new AreaSquare(this, null, id, TypeElem.SQUARE, layoutRoot, width, height, color1, color2, color3, paramJson); //простое

            } else if ("TRAPEZE".equals(elemType)) {
                rootArea = new AreaTrapeze(this, null, id, TypeElem.TRAPEZE, layoutRoot, width, height, color1, color2, color3, paramJson); //трапеция

            } else if ("TRIANGL".equals(elemType)) {
                rootArea = new AreaTriangl(this, null, id, TypeElem.TRIANGL, layoutRoot, width, height, color1, color2, color3, paramJson); //треугольник

            } else if ("ARCH".equals(elemType)) {
                rootArea = new AreaArch(this, null, id, TypeElem.ARCH, layoutRoot, width, height, color1, color2, color3, paramJson); //арка
            }

            intermediate = new Intermediate(null, id, elemType, layoutObj, width, height, paramJson);

            //Добавим рамы в гпавное окно
            for (Object elemFrame : mainObj.get("elements").getAsJsonArray()) {
                JsonObject jsonFrame = (JsonObject) elemFrame;

                if (TypeElem.FRAME_BOX.name().equals(jsonFrame.get("elemType").getAsString())) {
                    String layourFrame = jsonFrame.get("layoutFrame").getAsString();

                    if (LayoutArea.LEFT.name().equals(layourFrame)) {
                        ElemFrame frameLeft = rootArea.addFrame(new ElemFrame(rootArea, jsonFrame.get("id").getAsString(), LayoutArea.LEFT));

                    } else if (LayoutArea.RIGHT.name().equals(layourFrame)) {
                        ElemFrame frameRight = rootArea.addFrame(new ElemFrame(rootArea, jsonFrame.get("id").getAsString(), LayoutArea.RIGHT));

                    } else if (LayoutArea.TOP.name().equals(layourFrame)) {
                        ElemFrame frameTop = rootArea.addFrame(new ElemFrame(rootArea, jsonFrame.get("id").getAsString(), LayoutArea.TOP));

                    } else if (LayoutArea.BOTTOM.name().equals(layourFrame)) {
                        ElemFrame frameBottom = rootArea.addFrame(new ElemFrame(rootArea, jsonFrame.get("id").getAsString(), LayoutArea.BOTTOM));

                    } else if (LayoutArea.ARCH.name().equals(layourFrame)) {
                        ElemFrame frameArch = rootArea.addFrame(new ElemFrame(rootArea, jsonFrame.get("id").getAsString(), LayoutArea.ARCH));
                    }

                    listIntermediate.add(new Intermediate(intermediate, jsonFrame.get("id").getAsString(), TypeElem.FRAME_BOX.name, layourFrame, ""));
                }
            }
            //Добавим все остальные элементы
            buildWin(mainObj, rootArea);
            buildWin2(mainObj, intermediate);

        } catch (Exception e) {
            System.out.println("Ошибка Wincalc.parsingScript() " + e);
        }
    }

    //Рекурсия дерева скрипта
    private void buildWin(JsonObject jso, AreaSimple ownerArea) {

        for (Object obj : jso.get("elements").getAsJsonArray()) {
            JsonObject jsonObject = (JsonObject) obj;
            String type = jsonObject.get("elemType").getAsString();

            if (TypeElem.AREA.name().equals(type) || TypeElem.FULLSTVORKA.name().equals(type)) {
                AreaSimple ownerArea2 = addArea(ownerArea, jsonObject);
                buildWin(jsonObject, ownerArea2);
            } else {
                addElem(ownerArea, jsonObject);
            }
        }
    }

    private void buildWin2(JsonObject jso, Intermediate owner) {

        for (Object obj : jso.get("elements").getAsJsonArray()) {
            JsonObject objArea = (JsonObject) obj;
            String id = objArea.get("id").getAsString();
            String type = objArea.get("elemType").getAsString();
            String param = (objArea.get("paramJson") != null) ? objArea.get("paramJson").getAsString() : null;           
            if (TypeElem.AREA.name().equals(type) || TypeElem.FULLSTVORKA.name().equals(type)) {

                float width = (owner.layout == LayoutArea.VERT) ? owner.width : objArea.get("width").getAsFloat();
                float height = (owner.layout == LayoutArea.VERT) ? objArea.get("height").getAsFloat() : owner.height;                
                String layout = objArea.get("layoutArea").getAsString();
                Intermediate intermediate = new Intermediate(owner, id, type, layout, width, height, param);
                listIntermediate.add(intermediate);
                buildWin2(objArea, intermediate);
            } else {

                Intermediate intermediate = new Intermediate(owner, id, type, LayoutArea.NONE.name, param);
                listIntermediate.add(intermediate);
            }
        }
    }

    //Добавление AREA в конструцию
    private AreaSimple addArea(AreaSimple ownerArea, JsonObject objArea) {
        try {
            float width = (ownerArea.layout() == LayoutArea.VERT) ? ownerArea.width() : objArea.get("width").getAsFloat();
            float height = (ownerArea.layout() == LayoutArea.VERT) ? objArea.get("height").getAsFloat() : ownerArea.height();
            String id = objArea.get("id").getAsString();
            String paramArea = (objArea.get("paramJson") != null) ? objArea.get("paramJson").getAsString() : null;
            TypeElem typeArea = (TypeElem.AREA.name().equals(objArea.get("elemType").getAsString()) == true) ? TypeElem.AREA : TypeElem.FULLSTVORKA;
            LayoutArea layoutArea = ("VERT".equals(objArea.get("layoutArea").getAsString())) ? LayoutArea.VERT : LayoutArea.HORIZ;

            AreaSimple simpleArea = null;
            if (TypeElem.FULLSTVORKA == typeArea) {
                simpleArea = new AreaStvorka(this, ownerArea, id, paramArea);
            } else {
                if (TypeElem.SQUARE == this.rootArea.typeElem()) {
                    simpleArea = new AreaSquare(this, ownerArea, id, typeArea, layoutArea, width, height, -1, -1, -1, null); //простое

                } else if (TypeElem.TRAPEZE == this.rootArea.typeElem()) {
                    simpleArea = new AreaTrapeze(this, ownerArea, id, typeArea, layoutArea, width, height, -1, -1, -1, null); //трапеция

                } else if (TypeElem.TRIANGL == this.rootArea.typeElem()) {
                    simpleArea = new AreaTriangl(this, ownerArea, id, typeArea, layoutArea, width, height, -1, -1, -1, null); //треугольник

                } else if (TypeElem.ARCH == this.rootArea.typeElem()) {
                    simpleArea = new AreaArch(this, ownerArea, id, typeArea, layoutArea, width, height, -1, -1, -1, null); //арка
                }
            }
            //System.out.println(simpleArea.getId());
            ownerArea.listChild().add(simpleArea);
            return simpleArea;

        } catch (Exception e) {
            System.out.println("Ошибка Wincalc.addArea() " + e);
            return null;
        }
    }

    //Добавление Element в конструцию
    private void addElem(AreaSimple ownerArea, JsonObject objElem) {
        try {
            String id = objElem.get("id").getAsString();
            String elemType = objElem.get("elemType").getAsString();

            if (TypeElem.IMPOST.name().equals(elemType)) {
                ElemSimple elemSimple = new ElemImpost(ownerArea, id);
                //System.out.println(elemSimple.getId());
                ownerArea.listChild().add(elemSimple);

            } else if (TypeElem.GLASS.name().equals(elemType)) {
                String paramElem = (objElem.get("paramJson") != null) ? objElem.get("paramJson").getAsString() : null;
                ElemSimple elemSimple = new ElemGlass(ownerArea, id, paramElem);
                //System.out.println(elemSimple.getId());                
                ownerArea.listChild().add(elemSimple);
            }
        } catch (Exception e) {
            System.out.println("Ошибка Wincalc.addElem() " + e);
        }
    }
}
