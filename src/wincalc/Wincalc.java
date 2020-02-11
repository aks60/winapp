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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import main.Main;
import wincalc.constr.Constructive;
import wincalc.model.ElemSimple;

public class Wincalc {

    protected final Constructive constr = null;
    protected static final HashMap<Short, Constructive> constrMap = new HashMap<>();
    public Integer nuni = 0;
    public Record artiklRec = null;  //главный артикл системы профилей
    protected float percentMarkup = 0;  //процентная надбавка

    protected final int colorNone = 1005;  //без цвета (возвращаемое значение по умолчанию)
    public float width = 0.f;  //ширина окна
    public float height = 0.f;  //высота окна
    public float heightAdd = 0.f; //арка, трапеция, треугольник
    public int color1 = -1; //базовый цвет
    public int color2 = -1; //внутренний цвет
    public int color3 = -1; //внещний цвет

    public byte[] bufferByte = null; //буффер рисунка
    public BufferedImage bufferImg = null;  //образ рисунка
    public Graphics2D gc2d = null; //графический котекст рисунка  
    public float scaleDxy = 1; //коэффициент сжатия
    protected String labelSketch = "empty"; //надпись на эскизе

    public AreaSimple rootArea = null;
    private HashMap<Integer, String> mapPro4Params = new HashMap();
    public Record sysconsRec = null; //константы
    protected HashMap<Integer, Record> mapParamDef = new HashMap(); //параметры по умолчанию
    public HashMap<String, ElemJoining> mapJoin = new HashMap(); //список соединений рам и створок
    public LinkedList<ElemSimple> listElem; //список элементов
    LinkedList<AreaSimple> listArea; //список area
    protected HashMap<String, LinkedList<Object[]>> drawMapLineList = new HashMap(); //список линий окон 
    protected Gson gson = new Gson(); //библиотека json

    public Wincalc() {
    }

    public AreaSimple create(String productJson) {

        mapParamDef.clear();
        mapJoin.clear();
        drawMapLineList.clear();

        //Парсинг входного скрипта
        parsingScript(productJson);
        //System.out.println(productJson); //вывод на консоль json

        //Загрузим параметры по умолчанию
        ArrayList<Record> syspar1List = eSyspar1.up.find(nuni);
        syspar1List.stream().forEach(record -> mapParamDef.put(record.getInt(eSyspar1.pnumb), record));

        //Инициализация объектов калькуляции
        LinkedList<AreaSimple> listArea = rootArea.listElem(TypeElem.AREA); //список контейнеров
        LinkedList<AreaStvorka> listStvorka = rootArea.listElem(TypeElem.FULLSTVORKA); //список створок
        EnumMap<LayoutArea, ElemFrame> mapElemRama = rootArea.mapFrame; //список рам
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

        //Тестирование
        if (Main.dev == true) {
            //System.out.println(productJson); //вывод на консоль json
            //mapJoin.entrySet().forEach(it -> System.out.println(it.getKey() + ":  id=" + it.getValue().id + "  " + it.getValue()));
            //listElem.stream().forEach(el -> System.out.println(el));
        }
        return rootArea;
    }

    // Парсим входное json окно и строим объектную модель окна
    private void parsingScript(String json) {

        try {
            JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
            JsonObject mainObj = jsonElement.getAsJsonObject();

            String id = mainObj.get("id").getAsString();
            String paramJson = mainObj.get("paramJson").getAsString();
            nuni = mainObj.get("nuni").getAsInt();

            width = mainObj.get("width").getAsFloat();
            height = mainObj.get("height").getAsFloat();
            heightAdd = mainObj.get("heightAdd").getAsFloat();

            Record sysprofRec = eSysprof.up.find3(nuni, TypeProfile.FRAME, ProfileSide.LEFT);
            artiklRec = eArtikl.up.find(sysprofRec.getInt(eSysprof.artikl_id), true);
            sysconsRec = eSyscons.find(artiklRec.getInt(eArtikl.syscons_id));

            color1 = mainObj.get("color1").getAsInt();
            color2 = mainObj.get("color2").getAsInt();
            color3 = mainObj.get("color3").getAsInt();

            //Определим напрвление построения окна
            String layoutObj = mainObj.get("layoutArea").getAsString();
            String elemType = mainObj.get("elemType").getAsString();
            LayoutArea layoutRoot = ("VERT".equals(layoutObj)) ? LayoutArea.VERT : LayoutArea.HORIZ;

            if ("SQUARE".equals(mainObj.get("elemType").getAsString())) {
                rootArea = new AreaSquare(this, null, id, TypeElem.SQUARE, layoutRoot, width, height, color1, color2, color3, paramJson); //простое

            } else if ("TRAPEZE".equals(mainObj.get("elemType").getAsString())) {
                rootArea = new AreaTrapeze(this, null, id, TypeElem.TRAPEZE, layoutRoot, width, height, color1, color2, color3, paramJson); //трапеция

            } else if ("TRIANGL".equals(mainObj.get("elemType").getAsString())) {
                rootArea = new AreaTriangl(this, null, id, TypeElem.TRIANGL, layoutRoot, width, height, color1, color2, color3, paramJson); //треугольник

            } else if ("ARCH".equals(mainObj.get("elemType").getAsString())) {
                rootArea = new AreaArch(this, null, id, TypeElem.ARCH, layoutRoot, width, height, color1, color2, color3, paramJson); //арка
            }

            //Добавим рамы
            for (Object elemFrame : mainObj.get("elements").getAsJsonArray()) {
                JsonObject jsonFrame = (JsonObject) elemFrame;

                if (TypeElem.FRAME_BOX.name().equals(jsonFrame.get("elemType").getAsString())) {

                    if (LayoutArea.LEFT.name().equals(jsonFrame.get("layoutFrame").getAsString())) {
                        ElemFrame frameLeft = rootArea.addFrame(new ElemFrame(rootArea, jsonFrame.get("id").getAsString(), LayoutArea.LEFT));

                    } else if (LayoutArea.RIGHT.name().equals(jsonFrame.get("layoutFrame").getAsString())) {
                        ElemFrame frameRight = rootArea.addFrame(new ElemFrame(rootArea, jsonFrame.get("id").getAsString(), LayoutArea.RIGHT));

                    } else if (LayoutArea.TOP.name().equals(jsonFrame.get("layoutFrame").getAsString())) {
                        ElemFrame frameTop = rootArea.addFrame(new ElemFrame(rootArea, jsonFrame.get("id").getAsString(), LayoutArea.TOP));

                    } else if (LayoutArea.BOTTOM.name().equals(jsonFrame.get("layoutFrame").getAsString())) {
                        ElemFrame frameBottom = rootArea.addFrame(new ElemFrame(rootArea, jsonFrame.get("id").getAsString(), LayoutArea.BOTTOM));

                    } else if (LayoutArea.ARCH.name().equals(jsonFrame.get("layoutFrame").getAsString())) {
                        ElemFrame frameArch = rootArea.addFrame(new ElemFrame(rootArea, jsonFrame.get("id").getAsString(), LayoutArea.ARCH));
                    }
                }
            }

            //Элементы окна
            for (Object obj1 : mainObj.get("elements").getAsJsonArray()) { //первый уровень
                JsonObject elem1 = (JsonObject) obj1;
                String type1 = elem1.get("elemType").getAsString();
                if (TypeElem.AREA.name().equals(type1) || TypeElem.FULLSTVORKA.name().equals(type1)) {
                    AreaSimple areaSimple1 = addArea(rootArea, elem1);

                    for (Object obj2 : elem1.get("elements").getAsJsonArray()) { //второй уровень
                        JsonObject elem2 = (JsonObject) obj2;
                        String type2 = elem2.get("elemType").getAsString();
                        if (TypeElem.AREA.name().equals(type2) || TypeElem.FULLSTVORKA.name().equals(type2)) {
                            AreaSimple areaSimple2 = addArea(areaSimple1, elem2);

                            for (Object obj3 : elem2.get("elements").getAsJsonArray()) {  //третий уровень
                                JsonObject elem3 = (JsonObject) obj3;
                                String type3 = elem3.get("elemType").getAsString();
                                if (TypeElem.AREA.name().equals(type3) || TypeElem.FULLSTVORKA.name().equals(type3)) {
                                    AreaSimple areaSimple3 = addArea(areaSimple2, elem3);

                                    for (Object obj4 : elem3.get("elements").getAsJsonArray()) {  //четвёртый уровень
                                        JsonObject elem4 = (JsonObject) obj4;
                                        String type4 = elem4.get("elemType").getAsString();
                                        if (TypeElem.AREA.name().equals(type4) || TypeElem.FULLSTVORKA.name().equals(type4)) {
                                            AreaSimple areaSimple4 = addArea(areaSimple3, elem4);

                                            for (Object obj5 : elem4.get("elements").getAsJsonArray()) {  //пятый уровень
                                                JsonObject elem5 = (JsonObject) obj5;
                                                String type5 = elem5.get("elemType").getAsString();
                                                if (TypeElem.AREA.name().equals(type5) || TypeElem.FULLSTVORKA.name().equals(type5)) {
                                                    AreaSimple areaSinple5 = addArea(areaSimple4, elem5);

                                                } else {
                                                    addElem(areaSimple4, elem5);
                                                }
                                            }
                                        } else {
                                            addElem(areaSimple3, elem4);
                                        }
                                    }
                                } else {
                                    addElem(areaSimple2, elem3);
                                }
                            }
                        } else {
                            addElem(areaSimple1, elem2);
                        }
                    }
                } else {
                    addElem(rootArea, elem1);
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка Wincalc.parsingScript() " + e);
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
            System.out.println(simpleArea.getId());
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
                System.out.println(elemSimple.getId());
                ownerArea.listChild().add(elemSimple);

            } else if (TypeElem.GLASS.name().equals(elemType)) {
                String paramElem = (objElem.get("paramJson") != null) ? objElem.get("paramJson").getAsString() : null;
                ElemSimple elemSimple = new ElemGlass(ownerArea, id, paramElem);
                System.out.println(elemSimple.getId());
                ownerArea.listChild().add(elemSimple);
            }
        } catch (Exception e) {
            System.out.println("Ошибка Wincalc.addElem() " + e);
        }
    }
}
