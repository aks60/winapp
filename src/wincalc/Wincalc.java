package wincalc;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dataset.Record;
import enums.LayoutArea;
import enums.TypeElem;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;

public class Wincalc {

    protected static boolean production = false;
    //protected final Constructive constr;
    //protected static final HashMap<Short, Constructive> constrMap = new HashMap<>();
    protected Integer nuni = 0;
    protected Record articlesRec = null;  //главный артикл системы профилей
    protected String prj = "empty";
    protected float percentMarkup = 0;  //процентная надбавка
    protected final int colorNone = 1005;  //без цвета (возвращаемое значение по умолчанию)
    protected float width = 0.f;  //ширина окна
    protected float height = 0.f;  //высота окна
    protected float heightAdd = 0.f; //арка, трапеция, треугольник
    private float scale = .3f; //масштаб рисунка
    protected int color1 = -1; //базовый цвет
    protected int color2 = -1; //внутренний цвет
    protected int color3 = -1; //внещний цвет
    private byte[] bufferSmallImg = null; //рисунок без линий
    private byte[] bufferFullImg = null; //полный рисунок
    protected String labelSketch = "empty"; //надпись на эскизе
    protected AreaBase rootArea = null;
    private HashMap<Integer, String> mapPro4Params = new HashMap();
    //protected Syssize syssizeRec = null; //константы
    protected BufferedImage img = null;  //образ рисунка
    protected HashMap<Integer, Object[]> mapParamDef = new HashMap(); //параметры по умолчанию
    protected HashMap<String, ElemJoinig> mapJoin = new HashMap(); //список соединений рам и створок
    protected HashMap<String, LinkedList<Object[]>> drawMapLineList = new HashMap(); //список линий окон 
    protected Gson gson = new Gson(); //библиотека json

    public Wincalc() {
    }

    public AreaBase create(String productJson) {

        mapParamDef.clear();
        mapJoin.clear();
        drawMapLineList.clear();

        //Парсинг входного скрипта
        AreaBase mainArea = parsingScript(productJson);

        //Создание root окна
        if (mainArea instanceof AreaSquare) {
            rootArea = (AreaSquare) mainArea;  //калькуляция простого окна
        } else if (mainArea instanceof AreaArch) {
            rootArea = (AreaArch) mainArea;    //калькуляция арки
        } else if (mainArea instanceof AreaTrapeze) {
            rootArea = (AreaTrapeze) mainArea; //калькуляция трапеции
        }
        //Инициализация объектов калькуляции
        LinkedList<AreaBase> areaList = rootArea.elemList(TypeElem.AREA); //список контейнеров
        LinkedList<AreaStvorka> stvorkaList = rootArea.elemList(TypeElem.FULLSTVORKA); //список створок
        EnumMap<LayoutArea, ElemFrame> hmElemRama = rootArea.mapFrame; //список рам

        //CalcConstructiv constructiv = new CalcConstructiv(mainArea); //конструктив
        //CalcTariffication tariffic = new CalcTariffication(mainArea); //класс тарификации
        
        //Соединения рамы
        rootArea.joinRama();  //обход соединений и кальк. углов рамы
        areaList.stream().forEach(area -> area.joinArea(mapJoin)); //обход(схлопывание) соединений рамы

        return rootArea;
    }

    /**
     * Парсим входное json окно и строим объектную модель окна
     */
    private AreaBase parsingScript(String json) {
        AreaBase rootArea = null;
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

//            Sysproa sysproaRec = Sysproa.find(constr, nuni, TypeProfile.FRAME, ProfileSide.Left);
//            articlesRec = Artikls.get(constr, sysproaRec.anumb, true); //главный артикл системы профилей
//            syssizeRec = Syssize.find(constr, articlesRec.sunic); //системные константы
            //Цвета
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

                if (TypeElem.FRAME.name().equals(jsonFrame.get("elemType").getAsString())) {

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
                    AreaBase areaSimple1 = parsingAddArea(rootArea, rootArea, elemL1);

                    for (Object objL2 : elemL1.get("elements").getAsJsonArray()) { //второй уровень
                        JsonObject elemL2 = (JsonObject) objL2;
                        if (TypeElem.AREA.name().equals(elemL2.get("elemType").getAsString())) {
                            AreaBase areaSimple2 = parsingAddArea(rootArea, areaSimple1, elemL2);

                            for (Object objL3 : elemL2.get("elements").getAsJsonArray()) {  //третий уровень
                                JsonObject elemL3 = (JsonObject) objL3;
                                if (TypeElem.AREA.name().equals(elemL3.get("elemType").getAsString())) {
                                    AreaBase areaSimple3 = parsingAddArea(rootArea, areaSimple2, elemL3);

                                    for (Object objL4 : elemL3.get("elements").getAsJsonArray()) {  //четвёртый уровень
                                        JsonObject elemL4 = (JsonObject) objL4;
                                        if (TypeElem.AREA.name().equals(elemL4.get("elemType").getAsString())) {
                                            AreaBase areaSinple4 = parsingAddArea(rootArea, areaSimple3, elemL4);
                                        } else {
                                            parsingAddElem(rootArea, areaSimple3, elemL4);
                                        }
                                    }

                                } else {
                                    parsingAddElem(rootArea, areaSimple2, elemL3);
                                }
                            }
                        } else {
                            parsingAddElem(rootArea, areaSimple1, elemL2);
                        }
                    }
                } else {
                    parsingAddElem(rootArea, rootArea, elemL1);
                }
            }
        } catch (Exception e2) {
            System.out.println("Ошибка Iwindows.parsingScript() " + e2);
        }
        return rootArea;
    }

    private AreaBase parsingAddArea(AreaBase rootArea, AreaBase ownerArea, JsonObject objArea) {

        float width = (ownerArea.layout() == LayoutArea.VERTICAL) ? ownerArea.width : objArea.get("width").getAsFloat();
        float height = (ownerArea.layout() == LayoutArea.VERTICAL) ? objArea.get("height").getAsFloat() : ownerArea.height;

        String layoutObj = objArea.get("layoutArea").getAsString();
        LayoutArea layoutArea = ("VERTICAL".equals(layoutObj)) ? LayoutArea.VERTICAL : LayoutArea.HORIZONTAL;
        String id = objArea.get("id").getAsString();
        AreaScene sceneArea = new AreaScene(this, ownerArea, id, layoutArea, width, height);
        ownerArea.addElem(sceneArea);
        return sceneArea;
    }

    private void parsingAddElem(AreaBase root, AreaBase owner, JsonObject elem) {

        if (TypeElem.IMPOST.name().equals(elem.get("elemType").getAsString())) {
            owner.addElem(new ElemImpost(this, owner, elem.get("id").getAsString()));

        } else if (TypeElem.GLASS.name().equals(elem.get("elemType").getAsString())) {
            if (elem.get("paramJson") != null) {
                owner.addElem(new ElemGlass(this, owner, elem.get("id").getAsString(), elem.get("paramJson").getAsString()));
            } else {
                owner.addElem(new ElemGlass(this, owner, elem.get("id").getAsString()));
            }

        } else if (TypeElem.FULLSTVORKA.name().equals(elem.get("elemType").getAsString())) {

            AreaStvorka elemStvorka = new AreaStvorka(this, owner, elem.get("id").getAsString(), elem.get("paramJson").getAsString());
            owner.addElem(elemStvorka);
            //Уровень ниже
            for (Object obj : elem.get("elements").getAsJsonArray()) { //т.к. может быть и глухарь
                JsonObject elem2 = (JsonObject) obj;
                if (TypeElem.GLASS.name().equals(elem2.get("elemType").getAsString())) {
                    if (elem2.get("paramJson") != null) {
                        elemStvorka.addElem(new ElemGlass(this, elemStvorka, elem2.get("id").getAsString(), elem2.get("paramJson").getAsString()));
                    } else {
                        elemStvorka.addElem(new ElemGlass(this, elemStvorka, elem2.get("id").getAsString()));
                    }
                }
            }
        }
    }
}
