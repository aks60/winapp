package builder.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dataset.Record;
import domain.eParams;
import domain.eSysprof;
import enums.Layout;
import enums.LayoutJoin;
import enums.TypeJoin;
import java.io.File;
import java.util.EnumMap;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import startup.Main;
import builder.Wincalc;
import builder.script.GsonElem;
import builder.script.GsonRoot;
import common.UCom;
import dataset.Query;
import enums.PKjson;
import enums.Type;
import java.util.HashMap;

public class AreaSimple extends Com5t {

    public EnumMap<Layout, ElemFrame> frames = new EnumMap<>(Layout.class); //список рам в окне 
    public LinkedList<Com5t> childs = new LinkedList(); //дети

    public AreaSimple(Wincalc winc, AreaSimple owner, Type type) {
        super(winc.rootGson.id(), winc, owner, winc.rootGson);
        this.type = type;
        this.layout = winc.rootGson.layout();
        this.colorID1 = winc.rootGson.color1;
        this.colorID2 = winc.rootGson.color2;
        this.colorID3 = winc.rootGson.color3;

        initСonstructiv(winc.rootGson.param());
        setLocation(winc.rootGson.width(), winc.rootGson.height());
        initParametr(winc.rootGson.param());
    }
    
    public AreaSimple(Wincalc winc, AreaSimple owner, Type type, GsonElem gson, float width, float height) {
        super(gson.id(), winc, owner, gson);
        this.type = type;
        this.layout = gson.layout();
        this.colorID1 = winc.rootGson.color1;
        this.colorID2 = winc.rootGson.color2;
        this.colorID3 = winc.rootGson.color3;

        initСonstructiv(gson.param());
        setLocation(width, height);
        initParametr(gson.param());
    }

    public void initСonstructiv(JsonObject param) {
        if (isJson(param, PKjson.sysprofID)) {//профили через параметр
            sysprofRec = eSysprof.find3(param.get(PKjson.sysprofID).getAsInt());
        }
        winc.listArea.add(this);
        winc.listAll.add(this);
    }

    protected void setLocation(float width, float height) {
        //Происходит при подкдадке дополнительной ареа в арке
        //или сужении area створки при нахлёсте профилей
        if (owner != null) {            
            if (owner.childs.isEmpty() == true) { //если childs.isEmpty то prevArea искать нет смысла

                if (Layout.VERT.equals(owner.layout)) { //сверху вниз
                    float Y2 = (owner.y1 + height > owner.y2) ? owner.y2 : owner.y1 + height;
                    setDimension(owner.x1, owner.y1, owner.x2, Y2);

                } else if (Layout.HORIZ.equals(owner.layout)) { //слева направо
                    float X2 = (owner.x1 + width > owner.x2) ? owner.x2 : owner.x1 + width;
                    setDimension(owner.x1, owner.y1, X2, owner.y2);
                }

            } else { 
                for (int index = owner.childs.size() - 1; index >= 0; --index) { //т.к. this area ёщё не создана начнём с конца
                    if (owner.childs.get(index) instanceof AreaSimple) {
                        AreaSimple prevArea = (AreaSimple) owner.childs.get(index);

                        if (Layout.VERT.equals(owner.layout)) { //сверху вниз                            
                            float Y2 = (prevArea.y2 + height > owner.y2) ? owner.y2 : prevArea.y2 + height;
                            setDimension(owner.x1, prevArea.y2, owner.x2, Y2);

                        } else if (Layout.HORIZ.equals(owner.layout)) { //слева направо
                            float X2 = (prevArea.x2 + width > owner.x2) ? owner.x2 : prevArea.x2 + width;
                            setDimension(prevArea.x2, owner.y1, X2, owner.y2);
                        }
                        break;
                    }
                }
            }
        }
    }

    protected void initParametr(JsonObject param) {
        try {
            if (isJson(param)) {
                HashMap defMap = new HashMap();
                //Добавим к параметрам системы конструкции параметры конкретной конструкции
                JsonArray ioknaParamArr = param.getAsJsonArray(PKjson.ioknaParam);
                if (ioknaParamArr != null && !ioknaParamArr.isJsonNull() && ioknaParamArr.isJsonArray()) {
                    ioknaParamArr.forEach(grup -> {
                        Record paramsRec = eParams.find(grup.getAsInt()); //параметр менеджера  
                        Record syspar1Rec = winc.mapPardef.get(paramsRec.getInt(eParams.params_id));
                        if (syspar1Rec != null) { //ситуация если конструкция с nuni = -3, т.е. модели
                            syspar1Rec.setNo(eParams.text, paramsRec.getStr(eParams.text));
                        }
                    });
                }
            }
        } catch (Exception e) {
            System.out.println(winc.rootGson.prj);
            System.err.println("Ошибка:Com5t.parsingParam() " + e);
        }
    }

    public void joining() {

        LinkedList<ElemSimple> impList = UCom.listSortObj(winc.listElem, Type.IMPOST, Type.SHTULP, Type.STOIKA);
        LinkedList<ElemSimple> elemList = UCom.listSortObj(winc.listElem, Type.FRAME_SIDE, Type.STVORKA_SIDE, Type.IMPOST, Type.SHTULP, Type.STOIKA);

        //T - соединения
        for (ElemSimple elemImp : impList) {
            //Цикл по сторонам рамы и импостам (т.к. в створке Т-обр. соединений нет)
            for (ElemSimple elem5e : elemList) {
                if ((elem5e.owner.type == Type.ARCH && elem5e.layout == Layout.TOP) == false) { //для арки inside() не работает
                    elemImp.anglCut[0] = 90;
                    elemImp.anglCut[1] = 90;

                    if (elemImp.owner.layout == Layout.HORIZ) { //Импосты(штульпы...)  расположены по горизонтали слева на право                     
                        if (elem5e.inside(elemImp.x2, elemImp.y2) == true && elem5e != elemImp) { //T - соединение нижнее                              
                            ElemJoining.create(elemImp.joinPoint(0), winc, TypeJoin.VAR40, LayoutJoin.TBOT, elemImp, elem5e, 90);
                        } else if (elem5e.inside(elemImp.x1, elemImp.y1) == true && elem5e != elemImp) { //T - соединение верхнее                            
                            ElemJoining.create(elemImp.joinPoint(1), winc, TypeJoin.VAR40, LayoutJoin.TTOP, elemImp, elem5e, 90);
                        }

                    } else {//Импосты(штульпы...) расположены по вертикали снизу вверх
                        if (elem5e.inside(elemImp.x1, elemImp.y1) == true && elem5e != elemImp) { //T - соединение левое                             
                            ElemJoining.create(elemImp.joinPoint(0), winc, TypeJoin.VAR40, LayoutJoin.TLEFT, elemImp, elem5e, 90);
                        } else if (elem5e.inside(elemImp.x2, elemImp.y2) == true && elem5e != elemImp) { //T - соединение правое                              
                            ElemJoining.create(elemImp.joinPoint(1), winc, TypeJoin.VAR40, LayoutJoin.TRIGH, elemImp, elem5e, 90);
                        }
                    }
                }
            }
        }
    }

    //Рисуем конструкцию
    public void draw() {
        try {
            //Прорисовка стеклопакетов
            LinkedList<ElemGlass> elemGlassList = UCom.listSortObj(winc.listElem, Type.GLASS);
            elemGlassList.stream().forEach(el -> el.paint());

            //Прорисовка импостов
            LinkedList<ElemCross> elemImpostList = UCom.listSortObj(winc.listElem, Type.IMPOST);
            elemImpostList.stream().forEach(el -> el.paint());

            //Прорисовка штульпов
            LinkedList<ElemCross> elemShtulpList = UCom.listSortObj(winc.listElem, Type.SHTULP);
            elemShtulpList.stream().forEach(el -> el.paint());

            //Прорисовка стоек
            LinkedList<ElemCross> elemStoikaList = UCom.listSortObj(winc.listElem, Type.STOIKA);
            elemStoikaList.stream().forEach(el -> el.paint());

            //Прорисовка рам
            frames.get(Layout.TOP).paint();
            frames.get(Layout.BOTT).paint();
            frames.get(Layout.LEFT).paint();
            frames.get(Layout.RIGHT).paint();

            //Прорисовка створок
            LinkedList<AreaStvorka> elemStvorkaList = UCom.listSortObj(winc.listArea, Type.STVORKA);
            elemStvorkaList.stream().forEach(el -> el.paint());

            //Рисунок в память
            if (winc.bufferImg != null) {
                ByteArrayOutputStream byteArrOutStream = new ByteArrayOutputStream();
                ImageIO.write(winc.bufferImg, "png", byteArrOutStream);
                if (Main.dev == true) {
                    File outputfile = new File("CanvasImage.png");
                    ImageIO.write(winc.bufferImg, "png", outputfile);
                }
            }
        } catch (Exception s) {
            System.err.println("Ошибка:AreaSimple.drawWin() " + s);
        }
    }
}
