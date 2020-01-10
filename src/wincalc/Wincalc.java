
package wincalc;

import enums.eLayoutArea;
import enums.eTypeElem;
import forms.Artikls;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class Wincalc {
    
    protected static boolean production = false;
    //protected final Constructive constr;
    //protected static final HashMap<Short, Constructive> constrMap = new HashMap<>();
    protected int nuni = 0;
    protected Artikls articlesRec = null;  //главный артикл системы профилей
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
    private AreaBase rootArea = null;
    private HashMap<Integer, String> hmPro4Params = new HashMap();
    //protected Syssize syssizeRec = null; //константы
    protected BufferedImage img = null;  //образ рисунка
    protected HashMap<Integer, Object[]> hmParamDef = new HashMap(); //параметры по умолчанию
    protected HashMap<String, ElemJoinig> hmJoinElem = new HashMap(); //список соединений рам и створок
    protected HashMap<String, LinkedList<Object[]>> drawMapLineList = new HashMap(); //список линий окон    
    
    
    public Wincalc() {
    }
    
   public AreaBase create(String productJson) {
      
        hmParamDef.clear();
        hmJoinElem.clear();
        drawMapLineList.clear(); 
        
        //Парсинг входного скрипта
        AreaBase mainArea = parsingScript(productJson);
       
        
        return rootArea;
   }
   
 /**
     * Парсим входное json окно и строим объектную модель окна
     */
    private AreaBase parsingScript(String json) {
        String layoutObj = null;
        AreaBase rootArea = null;
        try {
            JSONParser parser = new JSONParser();
            //java.io.Reader json2 = new  java.io.FileReader("src\\resource\\script.json");
            //java.io.Reader json2 = new  java.io.FileReader("X:\\_aks\\Razio58 арка 1ств (пустая areaId).json");
            JSONObject mainObj = (JSONObject) parser.parse(json);
            String id = mainObj.get("id").toString();
            String paramJson = mainObj.get("paramJson").toString();
            nuni = Integer.parseInt(mainObj.get("nuni").toString());
            if (mainObj.get("prj") != null) prj = mainObj.get("prj").toString();
            width = Float.parseFloat(mainObj.get("width").toString());
            height = Float.parseFloat(mainObj.get("heightLow").toString());
            heightAdd = Float.parseFloat(mainObj.get("height").toString());

//            Sysproa sysproaRec = Sysproa.find(constr, nuni, TypeProfile.FRAME, ProfileSide.Left);
//            articlesRec = Artikls.get(constr, sysproaRec.anumb, true); //главный артикл системы профилей
//            syssizeRec = Syssize.find(constr, articlesRec.sunic); //системные константы

            //Цвета
            color1 = Integer.parseInt(mainObj.get("colorBase").toString());
            color2 = Integer.parseInt(mainObj.get("colorInternal").toString());
            color3 = Integer.parseInt(mainObj.get("colorExternal").toString());

            //Определим напрвление построения окна
            layoutObj = mainObj.get("layoutArea").toString();
            eLayoutArea layoutRoot = ("VERTICAL".equals(layoutObj)) ? eLayoutArea.VERTICAL : eLayoutArea.HORIZONTAL;

            if ("SQUARE".equals(mainObj.get("elemType").toString())) {
                rootArea = new AreaSquare(this, id, layoutRoot, width, height, color1, color2, color3, paramJson); //простое

            } else if ("TRAPEZE".equals(mainObj.get("elemType").toString())) {
                rootArea = new AreaTrapeze(this, id, layoutRoot, width, height, color1, color2, color3, paramJson); //трапеция

            } else if ("TRIANGL".equals(mainObj.get("elemType").toString())) {
                rootArea = new AreaTriangl(this, id, layoutRoot, width, height, color1, color2, color3, paramJson); //треугольник

            } else if ("ARCH".equals(mainObj.get("elemType").toString())) {
                rootArea = new AreaArch(this, id, layoutRoot, width, height, color1, color2, color3, paramJson); //арка

            }

            //Добавим рамы
            for (Object elemFrame : (JSONArray) mainObj.get("elements")) {
                JSONObject jsonFrame = (JSONObject) elemFrame;

                if (eTypeElem.FRAME.name().equals(jsonFrame.get("elemType"))) {

                    if (eLayoutArea.LEFT.name().equals(jsonFrame.get("layoutFrame"))) {
                        ElemFrame frameLeft = rootArea.addFrame(new ElemFrame(rootArea, jsonFrame.get("id").toString(), eLayoutArea.LEFT));

                    } else if (eLayoutArea.RIGHT.name().equals(jsonFrame.get("layoutFrame"))) {
                        ElemFrame frameRight = rootArea.addFrame(new ElemFrame(rootArea, jsonFrame.get("id").toString(), eLayoutArea.RIGHT));

                    } else if (eLayoutArea.TOP.name().equals(jsonFrame.get("layoutFrame"))) {
                        ElemFrame frameTop = rootArea.addFrame(new ElemFrame(rootArea, jsonFrame.get("id").toString(), eLayoutArea.TOP));

                    } else if (eLayoutArea.BOTTOM.name().equals(jsonFrame.get("layoutFrame"))) {
                        ElemFrame frameBottom = rootArea.addFrame(new ElemFrame(rootArea, jsonFrame.get("id").toString(), eLayoutArea.BOTTOM));

                    } else if (eLayoutArea.ARCH.name().equals(jsonFrame.get("layoutFrame"))) {
                        ElemFrame frameArch = rootArea.addFrame(new ElemFrame(rootArea, jsonFrame.get("id").toString(), eLayoutArea.ARCH));
                    }
                }
            }

            //Элементы окна
            for (Object objL1 : (JSONArray) mainObj.get("elements")) { //первый уровень
                JSONObject elemL1 = (JSONObject) objL1;
                if (eTypeElem.AREA.name().equals(elemL1.get("elemType"))) {
                    AreaBase areaSimple1 = parsingAddArea(rootArea, rootArea, elemL1);

                    for (Object objL2 : (JSONArray) elemL1.get("elements")) { //второй уровень
                        JSONObject elemL2 = (JSONObject) objL2;
                        if (eTypeElem.AREA.name().equals(elemL2.get("elemType"))) {
                            AreaBase areaSimple2 = parsingAddArea(rootArea, areaSimple1, elemL2);

                            for (Object objL3 : (JSONArray) elemL2.get("elements")) {  //третий уровень
                                JSONObject elemL3 = (JSONObject) objL3;
                                if (eTypeElem.AREA.name().equals(elemL3.get("elemType"))) {
                                    AreaBase areaSimple3 = parsingAddArea(rootArea, areaSimple2, elemL3);

                                    for (Object objL4 : (JSONArray) elemL3.get("elements")) {  //четвёртый уровень
                                        JSONObject elemL4 = (JSONObject) objL4;
                                        if (eTypeElem.AREA.name().equals(elemL4.get("elemType"))) {
                                            AreaBase areaSinple4 = parsingAddArea(rootArea, areaSimple3, elemL4);
                                        } else parsingAddElem(rootArea, areaSimple3, elemL4);
                                    }

                                } else parsingAddElem(rootArea, areaSimple2, elemL3);
                            }
                        } else parsingAddElem(rootArea, areaSimple1, elemL2);
                    }
                } else parsingAddElem(rootArea, rootArea, elemL1);
            }
        } catch (ParseException e) {
            System.out.println("Ошибка Iwindows.parsingScript() " + e);
        } catch (Exception e2) {
            System.out.println("Ошибка Iwindows.parsingScript() " + e2);
        }
        return rootArea;
    }

    private AreaBase parsingAddArea(AreaBase rootArea, AreaBase ownerArea, JSONObject objArea) {

        float width = (ownerArea.layout() == eLayoutArea.VERTICAL) ? ownerArea.width : Float.valueOf(objArea.get("width").toString());
        float height = (ownerArea.layout() == eLayoutArea.VERTICAL) ? Float.valueOf(objArea.get("height").toString()) : ownerArea.height;

        String layoutObj = objArea.get("layoutArea").toString();
        eLayoutArea layoutArea = ("VERTICAL".equals(layoutObj)) ? eLayoutArea.VERTICAL : eLayoutArea.HORIZONTAL;
        String id = objArea.get("id").toString();
        AreaScene sceneArea = new AreaScene(this, rootArea, ownerArea, id, layoutArea, width, height);
        ownerArea.addElem(sceneArea);
        return sceneArea;
    }

    private void parsingAddElem(AreaBase root, AreaBase owner, JSONObject elem) throws ParseException {

        if (eTypeElem.IMPOST.name().equals(elem.get("elemType"))) {
            owner.addElem(new ElemImpost(root, owner, elem.get("id").toString()));

        } else if (eTypeElem.GLASS.name().equals(elem.get("elemType"))) {
            if (elem.get("paramJson") != null) {
                owner.addElem(new ElemGlass(root, owner, elem.get("id").toString(), elem.get("paramJson").toString()));
            } else {
                owner.addElem(new ElemGlass(root, owner, elem.get("id").toString()));
            }

        } else if (eTypeElem.FULLSTVORKA.name().equals(elem.get("elemType"))) {

            AreaStvorka elemStvorka = new AreaStvorka(this, owner, elem.get("id").toString(), elem.get("paramJson").toString());
            owner.addElem(elemStvorka);
            //Уровень ниже
            for (Object obj : (JSONArray) elem.get("elements")) { //т.к. может быть и глухарь
                JSONObject elem2 = (JSONObject) obj;
                if (eTypeElem.GLASS.name().equals(elem2.get("elemType"))) {
                    if (elem2.get("paramJson") != null) {
                        elemStvorka.addElem(new ElemGlass(root, elemStvorka, elem2.get("id").toString(), elem2.get("paramJson").toString()));
                    } else {
                        elemStvorka.addElem(new ElemGlass(root, elemStvorka, elem2.get("id").toString()));
                    }
                }
            }
        }
    }
   
}
