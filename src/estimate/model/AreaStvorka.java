package estimate.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import frames.swing.Draw;
import dataset.Record;
import domain.eArtikl;
import domain.eSetting;
import domain.eSysfurn;
import domain.eSyssize;
import domain.eSysprof;
import enums.LayoutArea;
import enums.LayoutFurn1;
import enums.ParamJson;
import enums.TypeElem;
import enums.LayoutJoin;
import enums.TypeOpen1;
import enums.UseArtiklTo;
import enums.TypeJoin;
import java.awt.Color;
import java.util.LinkedList;
import estimate.Wincalc;

public class AreaStvorka extends AreaSimple {

    public String handleHeight = ""; //высота ручки
    public LayoutFurn1 handleSide = null; //сторона ручки
    public int handleColor = -1; //цвет ручки
    public TypeOpen1 typeOpen = TypeOpen1.LEFT; //тип открывания
    public Integer sysfurnID = null; //то, что выбрал клиент

    public AreaStvorka(Wincalc iwin, AreaSimple owner, float id, String param) {
        super(iwin, owner, id, TypeElem.STVORKA, LayoutArea.VERT, (owner.x2 - owner.x1), (owner.y2 - owner.y1), iwin.colorID1, iwin.colorID2, iwin.colorID3, param);

        if (param != null && param.isEmpty() == false) {
            JsonObject jsonObj = new Gson().fromJson(param.replace("'", "\""), JsonObject.class);
            if (jsonObj.get(ParamJson.typeOpen.name()) != null) {
                this.typeOpen = TypeOpen1.get(jsonObj.get(ParamJson.typeOpen.name()).getAsInt());
            }
            if (jsonObj.get(ParamJson.sysfurnID.name()) != null) {
                this.sysfurnID = jsonObj.get(ParamJson.sysfurnID.name()).getAsInt();
            } else {
                this.sysfurnID = eSysfurn.find2(typeOpen.id).getInt(eSysfurn.id);
            }
        }

        //Коррекция створки с учётом нахлёста
        ElemSimple adjacentLeft = join(LayoutArea.LEFT), adjacentTop = join(LayoutArea.TOP),
                adjacentBott = join(LayoutArea.BOTTOM), adjacentRight = join(LayoutArea.RIGHT);
        if ("ps3".equals(eSetting.find(2).getStr(eSetting.val))) {

            x1 = adjacentLeft.x2 - adjacentLeft.owner().offset;
            y1 = adjacentTop.y2 - adjacentTop.owner().offset;
            x2 = adjacentRight.x1 + adjacentRight.owner().offset;
            y2 = adjacentBott.y1 + adjacentBott.owner().offset;
        } else {
            x1 = adjacentLeft.x2 - adjacentLeft.artiklRec.getFloat(eArtikl.size_falz) - iwin.syssizeRec.getFloat(eSyssize.naxl);
            y1 = adjacentTop.y2 - adjacentTop.artiklRec.getFloat(eArtikl.size_falz) - iwin.syssizeRec.getFloat(eSyssize.naxl);
            x2 = adjacentRight.x1 + adjacentRight.artiklRec.getFloat(eArtikl.size_falz) + iwin.syssizeRec.getFloat(eSyssize.naxl);
            y2 = adjacentBott.y1 + adjacentBott.artiklRec.getFloat(eArtikl.size_falz) + iwin.syssizeRec.getFloat(eSyssize.naxl);
        }

        //Добавим рамы створки        
        ElemFrame stvBot = new ElemFrame(this, id + .1f, LayoutArea.BOTTOM);
        mapFrame.put(stvBot.layout(), stvBot);
        ElemFrame stvRigh = new ElemFrame(this, id + .2f, LayoutArea.RIGHT);
        mapFrame.put(stvRigh.layout(), stvRigh);
        ElemFrame stvTop = new ElemFrame(this, id + .3f, LayoutArea.TOP);
        mapFrame.put(stvTop.layout(), stvTop);
        ElemFrame stvLeft = new ElemFrame(this, id + .4f, LayoutArea.LEFT);
        mapFrame.put(stvLeft.layout(), stvLeft);

        stvBot.specificationRec.width = width();
        stvTop.specificationRec.width = width();
        stvRigh.specificationRec.height = height();
        stvLeft.specificationRec.height = height();

    }

    @Override
    public void joinFrame() {
        //Цикл по сторонам створки
        for (int index = 0; index < 4; index++) {
            ElemJoining el = new ElemJoining(iwin());
            el.id = id() + (float) (index + 1) / 100;
            mapFrame.get(LayoutArea.BOTTOM).anglHoriz = 0;
            mapFrame.get(LayoutArea.RIGHT).anglHoriz = 90;
            mapFrame.get(LayoutArea.TOP).anglHoriz = 180;
            mapFrame.get(LayoutArea.LEFT).anglHoriz = 270;
            mapFrame.entrySet().forEach(elem -> {
                elem.getValue().anglCut1 = 45;
                elem.getValue().anglCut2 = 45;
            });
            el.anglProf = 90;
            if (index == 0) { //Угловое соединение левое верхнее
                el.init(TypeJoin.VAR20, LayoutJoin.LTOP, mapFrame.get(LayoutArea.LEFT), mapFrame.get(LayoutArea.TOP));
                iwin().mapJoin.put(String.valueOf(x1) + ":" + String.valueOf(y1), el);

            } else if (index == 1) { //Угловое соединение левое нижнее
                el.init(TypeJoin.VAR20, LayoutJoin.LBOT, mapFrame.get(LayoutArea.LEFT), mapFrame.get(LayoutArea.BOTTOM));
                iwin().mapJoin.put(String.valueOf(x1) + ":" + String.valueOf(y2), el);

            } else if (index == 2) { //Угловое соединение правое нижнее
                el.init(TypeJoin.VAR20, LayoutJoin.RBOT, mapFrame.get(LayoutArea.RIGHT), mapFrame.get(LayoutArea.BOTTOM));
                iwin().mapJoin.put(String.valueOf(x2) + ":" + String.valueOf(y2), el);

            } else if (index == 3) { //Угловое соединение правое верхнее
                el.init(TypeJoin.VAR20, LayoutJoin.RTOP, mapFrame.get(LayoutArea.RIGHT), mapFrame.get(LayoutArea.TOP));
                iwin().mapJoin.put(String.valueOf(x2) + ":" + String.valueOf(y1), el);
            }
        }

        LinkedList<ElemSimple> listElem = iwin().rootArea.listElem(TypeElem.FRAME_SIDE, TypeElem.STVORKA_SIDE, TypeElem.IMPOST);
        for (int index = 0; index < 4; index++) {
            ElemJoining el = new ElemJoining(iwin());
            el.id = id() + (float) (index + 5) / 100;
            el.typeJoin = TypeJoin.VAR10;
            el.anglProf = 0;

            if (index == 0) { //Прилигающее верхнее 
                el.layoutJoin = LayoutJoin.CTOP;
                el.joinElement1 = mapFrame.get(LayoutArea.TOP);
                el.joinElement2 = listElem.stream().filter(el2 -> el2.inside(x1 + width() / 2, y1 - 1) == true).findFirst().orElse(null);
                iwin().mapJoin.put(String.valueOf(x1 + width() / 2) + ":" + String.valueOf(y1), el);

            } else if (index == 1) { //Прилигающее нижнее
                el.layoutJoin = LayoutJoin.CBOT;
                el.joinElement1 = mapFrame.get(LayoutArea.BOTTOM);
                el.joinElement2 = listElem.stream().filter(el2 -> el2.inside(x1 + width() / 2, y2 + 1) == true).findFirst().orElse(null);
                iwin().mapJoin.put(String.valueOf(x1 + width() / 2) + ":" + String.valueOf(y2), el);

            } else if (index == 2) { //Прилигающее левое
                el.layoutJoin = LayoutJoin.CLEFT;
                el.joinElement1 = mapFrame.get(LayoutArea.LEFT);
                el.joinElement2 = listElem.stream().filter(el2 -> el2.inside(x1 - 1, y1 + height() / 2) == true).findFirst().orElse(null);
                iwin().mapJoin.put(String.valueOf(x1) + ":" + String.valueOf(y1 + height() / 2), el);

            } else if (index == 3) { //Прилигающее правое
                el.layoutJoin = LayoutJoin.CRIGH;
                el.joinElement1 = mapFrame.get(LayoutArea.RIGHT);
                el.joinElement2 = listElem.stream().filter(el2 -> el2.inside(x2 + 1, y1 + height() / 2) == true).findFirst().orElse(null);
                iwin().mapJoin.put(String.valueOf(x2) + ":" + String.valueOf(y1 + height() / 2), el);
            }
        }
    }

    @Override
    public void paint() {

        mapFrame.get(LayoutArea.TOP).paint();
        mapFrame.get(LayoutArea.BOTTOM).paint();
        mapFrame.get(LayoutArea.LEFT).paint();
        mapFrame.get(LayoutArea.RIGHT).paint();

        if (typeOpen != TypeOpen1.INVALID) {
            float DX = 20, DY = 60, X1 = 0, Y1 = 0;
            ElemSimple elemL = mapFrame.get(LayoutArea.LEFT);
            ElemSimple elemR = mapFrame.get(LayoutArea.RIGHT);
            ElemSimple elemT = mapFrame.get(LayoutArea.TOP);
            ElemSimple elemB = mapFrame.get(LayoutArea.BOTTOM);

            float dy = iwin().heightAdd - iwin().height;

            if (typeOpen.id == 1 || typeOpen.id == 3) {
                X1 = elemR.x1 + (elemR.x2 - elemR.x1) / 2;
                Y1 = elemR.y1 + (elemR.y2 - elemR.y1) / 2;
                iwin().draw.drawLine(elemL.x1, elemL.y1 + dy, elemR.x2, elemR.y1 + dy + (elemR.y2 - elemR.y1) / 2);
                iwin().draw.drawLine(elemL.x1, elemL.y2 + dy, elemR.x2, elemR.y1 + dy + (elemR.y2 - elemR.y1) / 2);

            } else if (typeOpen.id == 2 || typeOpen.id == 4) {
                X1 = elemL.x1 + (elemL.x2 - elemL.x1) / 2;
                Y1 = elemL.y1 + (elemL.y2 - elemL.y1) / 2;
                iwin().draw.drawLine(elemR.x2, elemR.y1 + dy, elemL.x1, elemL.y1 + dy + (elemL.y2 - elemL.y1) / 2);
                iwin().draw.drawLine(elemR.x2, elemR.y2 + dy, elemL.x1, elemL.y1 + dy + (elemL.y2 - elemL.y1) / 2);
            }
            if (typeOpen.id == 3 || typeOpen.id == 4) {
                iwin().draw.drawLine(elemB.x1, elemB.y2 + dy, elemT.x1 + (elemT.x2 - elemT.x1) / 2, elemT.y1 + dy);
                iwin().draw.drawLine(elemB.x2, elemB.y2 + dy, elemT.x1 + (elemT.x2 - elemT.x1) / 2, elemT.y1 + dy);
            }
            iwin().draw.strokePolygon(X1 - DX, X1 + DX, X1 + DX, X1 - DX, Y1 - DY, Y1 - DY, Y1 + DY, Y1 + DY, 0xFFFFFFFF, Color.BLACK);
            DX = DX - 12;
            Y1 = Y1 + 20;
            iwin().draw.strokePolygon(X1 - DX, X1 + DX, X1 + DX, X1 - DX, Y1 - DY, Y1 - DY, Y1 + DY, Y1 + DY, 0xFFFFFFFF, Color.BLACK);
        }
    }
}
