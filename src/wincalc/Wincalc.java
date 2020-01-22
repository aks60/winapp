package wincalc;

import wincalc.model.ElemGlass;
import wincalc.model.ElemFrame;
import wincalc.model.AreaStvorka;
import wincalc.model.AreaArch;
import wincalc.model.AreaTriangl;
import wincalc.model.ElemJoinig;
import wincalc.model.AreaContainer;
import wincalc.model.AreaSquare;
import wincalc.model.AreaTrapeze;
import wincalc.model.ElemImpost;
import wincalc.model.AreaScene;
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
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import wincalc.constr.Constructive;

public class Wincalc {

    public static boolean dev = false;
    protected final Constructive constr = null;
    protected static final HashMap<Short, Constructive> constrMap = new HashMap<>();
    public Integer nuni = 0;
    public Record articlesRec = null;  //главный артикл системы профилей
    protected String prj = "empty";
    protected float percentMarkup = 0;  //процентная надбавка
    protected final int colorNone = 1005;  //без цвета (возвращаемое значение по умолчанию)
    public float width = 0.f;  //ширина окна
    public float height = 0.f;  //высота окна
    public float heightAdd = 0.f; //арка, трапеция, треугольник
    public float scale = .3f; //масштаб рисунка
    public int color1 = -1; //базовый цвет
    public int color2 = -1; //внутренний цвет
    public int color3 = -1; //внещний цвет
    private byte[] bufferSmallImg = null; //рисунок без линий
    private byte[] bufferFullImg = null; //полный рисунок
    protected String labelSketch = "empty"; //надпись на эскизе
    public AreaContainer rootArea = null;
    private HashMap<Integer, String> mapPro4Params = new HashMap();
    public Record sysconsRec = null; //константы
    public BufferedImage img = null;  //образ рисунка
    protected HashMap<Integer, Record> mapParamDef = new HashMap(); //параметры по умолчанию
    public HashMap<String, ElemJoinig> mapJoin = new HashMap(); //список соединений рам и створок
    protected HashMap<String, LinkedList<Object[]>> drawMapLineList = new HashMap(); //список линий окон 
    protected Gson gson = new Gson(); //библиотека json

    public Wincalc() {
    }

    public AreaContainer create(String productJson) {

        mapParamDef.clear();
        mapJoin.clear();
        drawMapLineList.clear();

        //Парсинг входного скрипта
        AreaContainer mainArea = parsingScript(productJson);
        img = new BufferedImage((int) (width + 260),
                (int) (heightAdd + 260), BufferedImage.TYPE_INT_RGB); //инит. буфера рисунка
        
        //Загрузим параметры по умолчанию
        ArrayList<Record> syspar1List = eSyspar1.up.find(nuni);        
        syspar1List.stream().forEach(record -> mapParamDef.put(record.getInt(eSyspar1.pnumb), record));
        
        //Создание root окна
        if (mainArea instanceof AreaSquare) {
            rootArea = (AreaSquare) mainArea;  //калькуляция простого окна
        } else if (mainArea instanceof AreaArch) {
            rootArea = (AreaArch) mainArea;    //калькуляция арки
        } else if (mainArea instanceof AreaTrapeze) {
            rootArea = (AreaTrapeze) mainArea; //калькуляция трапеции
        }
        //Инициализация объектов калькуляции
        LinkedList<AreaContainer> areaList = rootArea.listElem(TypeElem.AREA); //список контейнеров
        LinkedList<AreaStvorka> stvorkaList = rootArea.listElem(TypeElem.FULLSTVORKA); //список створок
        EnumMap<LayoutArea, ElemFrame> mapElemRama = rootArea.mapFrame; //список рам

        //Калькуляция конструктива
        //CalcConstructiv constructiv = new CalcConstructiv(mainArea); //конструктив
        //CalcTariffication tariffic = new CalcTariffication(mainArea); //класс тарификации
        
        //Соединения рамы
        rootArea.joinRama();  //обход соединений и кальк. углов рамы
        areaList.stream().forEach(area -> area.passJoinArea(mapJoin)); //обход(схлопывание) соединений рамы
        mapJoin.entrySet().stream().forEach(elemJoin -> elemJoin.getValue().initJoin()); //инит. варианта соединения

        //Соединения створок
        stvorkaList.stream().forEach(stvorka -> stvorka.setCorrection()); //коррекция размера створки с учётом нахлёста и построение рамы створки
        stvorkaList.stream().forEach(stvorka -> stvorka.passJoinRama()); //обход соединений и кальк. углов створок

        //Список элементов
        LinkedList<AreaContainer> elemList = rootArea.listElem(TypeElem.FRAME_BOX,
                 TypeElem.FRAME_STV, TypeElem.IMPOST, TypeElem.GLASS);  //(важно! получаем после построения створки)        

        rootArea.drawWin(1f, bufferFullImg, true);     //full рис.
        
        //Тестирование
        if (Wincalc.dev == true) {
            System.out.println(productJson); //вывод на консоль json
            //Specification.write_txt(constr, rootArea.specificList()); //вывод на тестирование в DLL
            //Specification.write_txt2(constr, rootArea.specificList()); //вывод уникального индекса
            //CalcBase.test_param(ParamSpecific.paramSum); //тестирование парам. спецификации
            //Main.print_joining(hmJoinElem); //соединения на консоль
            //model.Main.compareIWin(rootArea.specificList(), prj, true); //сравнение спецификации с профстроем
        }
        return rootArea;
    }

    /**
     * Парсим входное json окно и строим объектную модель окна
     */
    private AreaContainer parsingScript(String json) {
        AreaContainer rootArea = null;
        try {
            JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
            JsonObject mainObj = jsonElement.getAsJsonObject();

            String id = mainObj.get("id").getAsString();
            String paramJson = mainObj.get("paramJson").getAsString();
            nuni = mainObj.get("nuni").getAsInt();
            if (mainObj.get("prj") != null) {
                prj = mainObj.get("prj").getAsString();
            }
            width = mainObj.get("width").getAsFloat();
            height = mainObj.get("heightLow").getAsFloat();
            heightAdd = mainObj.get("height").getAsFloat();

            Record sysprofRec = eSysprof.up.find3(nuni, TypeProfile.FRAME, ProfileSide.Left);
            articlesRec = eArtikl.up.find(sysprofRec.getInt(eSysprof.artikl_id), true);
            sysconsRec = eSyscons.find(articlesRec.getInt(eArtikl.syscons_id));

            color1 = mainObj.get("color1").getAsInt();
            color2 = mainObj.get("color2").getAsInt();
            color3 = mainObj.get("color3").getAsInt();

            //Определим напрвление построения окна
            String layoutObj = mainObj.get("layoutArea").getAsString();
            LayoutArea layoutRoot = ("VERTICAL".equals(layoutObj)) ? LayoutArea.VERTICAL : LayoutArea.HORIZONTAL;

            if ("SQUARE".equals(mainObj.get("elemType").getAsString())) {
                rootArea = new AreaSquare(this, id, layoutRoot, width, height, color1, color2, color3, paramJson); //простое

            } else if ("TRAPEZE".equals(mainObj.get("elemType").getAsString())) {
                rootArea = new AreaTrapeze(this, id, layoutRoot, width, height, color1, color2, color3, paramJson); //трапеция

            } else if ("TRIANGL".equals(mainObj.get("elemType").getAsString())) {
                rootArea = new AreaTriangl(this, id, layoutRoot, width, height, color1, color2, color3, paramJson); //треугольник

            } else if ("ARCH".equals(mainObj.get("elemType").getAsString())) {
                rootArea = new AreaArch(this, id, layoutRoot, width, height, color1, color2, color3, paramJson); //арка

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
                    AreaContainer areaContainer = parsingAddArea(rootArea, rootArea, elemL1);

                    for (Object objL2 : elemL1.get("elements").getAsJsonArray()) { //второй уровень
                        JsonObject elemL2 = (JsonObject) objL2;
                        if (TypeElem.AREA.name().equals(elemL2.get("elemType").getAsString())) {
                            AreaContainer areaSimple2 = parsingAddArea(rootArea, areaContainer, elemL2);

                            for (Object objL3 : elemL2.get("elements").getAsJsonArray()) {  //третий уровень
                                JsonObject elemL3 = (JsonObject) objL3;
                                if (TypeElem.AREA.name().equals(elemL3.get("elemType").getAsString())) {
                                    AreaContainer areaSimple3 = parsingAddArea(rootArea, areaSimple2, elemL3);

                                    for (Object objL4 : elemL3.get("elements").getAsJsonArray()) {  //четвёртый уровень
                                        JsonObject elemL4 = (JsonObject) objL4;
                                        if (TypeElem.AREA.name().equals(elemL4.get("elemType").getAsString())) {
                                            AreaContainer areaSinple4 = parsingAddArea(rootArea, areaSimple3, elemL4);
                                        } else {
                                            parsingAddElem(rootArea, areaSimple3, elemL4);
                                        }
                                    }

                                } else {
                                    parsingAddElem(rootArea, areaSimple2, elemL3);
                                }
                            }
                        } else {
                            parsingAddElem(rootArea, areaContainer, elemL2);
                        }
                    }
                } else {
                    parsingAddElem(rootArea, rootArea, elemL1);
                }
            }
        } catch (Exception e2) {
            System.out.println("Ошибка Wincala.parsingScript() " + e2);
        }
        return rootArea;
    }

    private AreaContainer parsingAddArea(AreaContainer rootArea, AreaContainer ownerArea, JsonObject objArea) {

        float width = (ownerArea.layout() == LayoutArea.VERTICAL) ? ownerArea.width() : objArea.get("width").getAsFloat();
        float height = (ownerArea.layout() == LayoutArea.VERTICAL) ? objArea.get("height").getAsFloat() : ownerArea.height();

        String layoutObj = objArea.get("layoutArea").getAsString();
        LayoutArea layoutArea = ("VERTICAL".equals(layoutObj)) ? LayoutArea.VERTICAL : LayoutArea.HORIZONTAL;
        String id = objArea.get("id").getAsString();
        AreaScene sceneArea = new AreaScene(this, ownerArea, id, layoutArea, width, height);
        ownerArea.addElem(sceneArea);
        return sceneArea;
    }

    private void parsingAddElem(AreaContainer root, AreaContainer owner, JsonObject elem) {

        if (TypeElem.IMPOST.name().equals(elem.get("elemType").getAsString())) {
            owner.addElem(new ElemImpost(owner, elem.get("id").getAsString()));

        } else if (TypeElem.GLASS.name().equals(elem.get("elemType").getAsString())) {
            if (elem.get("paramJson") != null) {
                owner.addElem(new ElemGlass(owner, elem.get("id").getAsString(), elem.get("paramJson").getAsString()));
            } else {
                owner.addElem(new ElemGlass(owner, elem.get("id").getAsString()));
            }

        } else if (TypeElem.FULLSTVORKA.name().equals(elem.get("elemType").getAsString())) {

            AreaStvorka elemStvorka = new AreaStvorka(this, owner, elem.get("id").getAsString(), elem.get("paramJson").getAsString());
            owner.addElem(elemStvorka);
            //Уровень ниже
            for (Object obj : elem.get("elements").getAsJsonArray()) { //т.к. может быть и глухарь
                JsonObject elem2 = (JsonObject) obj;
                if (TypeElem.GLASS.name().equals(elem2.get("elemType").getAsString())) {
                    if (elem2.get("paramJson") != null) {
                        elemStvorka.addElem(new ElemGlass(elemStvorka, elem2.get("id").getAsString(), elem2.get("paramJson").getAsString()));
                    } else {
                        elemStvorka.addElem(new ElemGlass(elemStvorka, elem2.get("id").getAsString()));
                    }
                }
            }
        }
    }
}
