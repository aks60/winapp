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
            for (Object objL1 : mainObj.get("elements").getAsJsonArray()) { //первый уровень
                JsonObject elemL1 = (JsonObject) objL1;
                if (TypeElem.AREA.name().equals(elemL1.get("elemType").getAsString())) {
                    AreaSimple areaSimple1 = addArea(rootArea, rootArea, elemL1);

                    for (Object objL2 : elemL1.get("elements").getAsJsonArray()) { //второй уровень
                        JsonObject elemL2 = (JsonObject) objL2;
                        if (TypeElem.AREA.name().equals(elemL2.get("elemType").getAsString())) {
                            AreaSimple areaSimple2 = addArea(rootArea, areaSimple1, elemL2);

                            for (Object objL3 : elemL2.get("elements").getAsJsonArray()) {  //третий уровень
                                JsonObject elemL3 = (JsonObject) objL3;
                                if (TypeElem.AREA.name().equals(elemL3.get("elemType").getAsString())) {
                                    AreaSimple areaSimple3 = addArea(rootArea, areaSimple2, elemL3);

                                    for (Object objL4 : elemL3.get("elements").getAsJsonArray()) {  //четвёртый уровень
                                        JsonObject elemL4 = (JsonObject) objL4;
                                        if (TypeElem.AREA.name().equals(elemL4.get("elemType").getAsString())) {
                                            AreaSimple areaSinple4 = addArea(rootArea, areaSimple3, elemL4);
                                        } else {
                                            addElem(rootArea, areaSimple3, elemL4);
                                        }
                                    }

                                } else {
                                    addElem(rootArea, areaSimple2, elemL3);
                                }
                            }
                        } else {
                            addElem(rootArea, areaSimple1, elemL2);
                        }
                    }
                } else {
                    addElem(rootArea, rootArea, elemL1);
                }
            }           
        } catch (Exception e2) {
            System.out.println("Ошибка Wincalc.parsingScript() " + e2);
        }
    }

    //Добавление AREA в конструцию
    private AreaSimple addArea(AreaSimple rootArea, AreaSimple ownerArea, JsonObject objArea) {

        float width = (ownerArea.layout() == LayoutArea.VERT) ? ownerArea.width() : objArea.get("width").getAsFloat();
        float height = (ownerArea.layout() == LayoutArea.VERT) ? objArea.get("height").getAsFloat() : ownerArea.height();

        String layoutObj = objArea.get("layoutArea").getAsString();
        LayoutArea layoutArea = ("VERT".equals(layoutObj)) ? LayoutArea.VERT : LayoutArea.HORIZ;
        AreaSimple simpleArea = AreaSimple.getInstanc(this, ownerArea, objArea.get("id").getAsString(), TypeElem.AREA, layoutArea, width, height);
        ownerArea.listChild().add(simpleArea);
        return simpleArea;
    }

    //Добавление Element в конструцию
    private void addElem(AreaSimple root, AreaSimple owner, JsonObject elem) {

        if (TypeElem.IMPOST.name().equals(elem.get("elemType").getAsString())) {
            owner.listChild().add(new ElemImpost(owner, elem.get("id").getAsString()));

        } else if (TypeElem.GLASS.name().equals(elem.get("elemType").getAsString())) {
            if (elem.get("paramJson") != null) {
                owner.listChild().add(new ElemGlass(owner, elem.get("id").getAsString(), elem.get("paramJson").getAsString()));
            } else {
                owner.listChild().add(new ElemGlass(owner, elem.get("id").getAsString(), null));
            }

        } else if (TypeElem.FULLSTVORKA.name().equals(elem.get("elemType").getAsString())) {

            AreaStvorka elemStvorka = new AreaStvorka(this, owner, elem.get("id").getAsString(), elem.get("paramJson").getAsString());
            owner.listChild().add(elemStvorka);
            //Уровень ниже
            for (Object obj : elem.get("elements").getAsJsonArray()) { //т.к. может быть и глухарь
                JsonObject elem2 = (JsonObject) obj;
                if (TypeElem.GLASS.name().equals(elem2.get("elemType").getAsString())) {
                    if (elem2.get("paramJson") != null) {
                        elemStvorka.listChild().add(new ElemGlass(elemStvorka, elem2.get("id").getAsString(), elem2.get("paramJson").getAsString()));
                    } else {
                        elemStvorka.listChild().add(new ElemGlass(elemStvorka, elem2.get("id").getAsString(), null));
                    }
                }
            }
        }
    }
}
