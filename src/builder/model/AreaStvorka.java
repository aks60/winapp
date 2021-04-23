package builder.model;

import dataset.Record;
import domain.eArtikl;
import domain.eJoining;
import domain.eJoinpar1;
import domain.eJoinvar;
import domain.eSyssize;
import enums.LayoutArea;
import enums.TypeElem;
import enums.LayoutJoin;
import enums.TypeOpen1;
import enums.TypeJoin;
import java.awt.Color;
import java.util.LinkedList;
import builder.Wincalc;
import builder.making.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import domain.eSysfurn;
import enums.LayoutHandle;
import enums.PKjson;
import enums.TypeOpen2;
import frames.Ujson;
import java.util.List;

public class AreaStvorka extends AreaSimple {

    public Record sysfurnRec = eSysfurn.up.newRecord(); //фурнитура
    public TypeOpen1 typeOpen = TypeOpen1.INVALID; //направление открывания
    public Record handleRec = eArtikl.virtualRec(); //ручка
    public int handleColor = -3; //цвет ручки
    public float handleHeight = 0; //высота ручки
    public LayoutHandle handleLayout = LayoutHandle.SET; //положение ручки на створке       

    public AreaStvorka(Wincalc iwin, AreaSimple owner, float id, String param) {
        super(iwin, owner, id, TypeElem.STVORKA, LayoutArea.VERT, (owner.x2 - owner.x1), (owner.y2 - owner.y1), iwin.colorID1, iwin.colorID2, iwin.colorID3, param);

        Gson gson = new GsonBuilder().create();
        JsonObject paramObj = new GsonBuilder().create().fromJson(param, JsonObject.class);

        //Добавим рамы створки    Ujson.getAsJsonObject(paramObj, stvKey)  
        ElemFrame stvBot = new ElemFrame(this, id + .1f, LayoutArea.BOTTOM, gson.toJson(Ujson.getAsJsonObject(paramObj, PKjson.stvorkaBottom)));
        mapFrame.put(stvBot.layout(), stvBot);
        ElemFrame stvRigh = new ElemFrame(this, id + .2f, LayoutArea.RIGHT, gson.toJson(Ujson.getAsJsonObject(paramObj, PKjson.stvorkaRight)));
        mapFrame.put(stvRigh.layout(), stvRigh);
        ElemFrame stvTop = new ElemFrame(this, id + .3f, LayoutArea.TOP, gson.toJson(Ujson.getAsJsonObject(paramObj, PKjson.stvorkaTop)));
        mapFrame.put(stvTop.layout(), stvTop);
        ElemFrame stvLeft = new ElemFrame(this, id + .4f, LayoutArea.LEFT, gson.toJson(Ujson.getAsJsonObject(paramObj, PKjson.stvorkaLeft)));
        mapFrame.put(stvLeft.layout(), stvLeft);

        //Положение элементов створки с учётом нахлёста
        setLocation(stvLeft, stvBot, stvRigh, stvTop);
        stvBot.setLocation();
        stvRigh.setLocation();
        stvTop.setLocation();
        stvLeft.setLocation();

        initFurniture(param);

        stvBot.spcRec.width = width();
        stvTop.spcRec.width = width();
        stvRigh.spcRec.height = height();
        stvLeft.spcRec.height = height();
    }

    //Коррекция координат створки с учётом нахлёста
    private void setLocation(ElemFrame stvLef, ElemFrame stvBot, ElemFrame stvRig, ElemFrame stvTop) {

        ElemSimple adjacentLef = join(LayoutArea.LEFT), adjacentTop = join(LayoutArea.TOP),
                adjacentBot = join(LayoutArea.BOTTOM), adjacentRig = join(LayoutArea.RIGHT);

        if (iwin().syssizeRec.getInt(eSyssize.id) != -1) {
            x1 = adjacentLef.x2 - adjacentLef.artiklRec.getFloat(eArtikl.size_falz) - iwin().syssizeRec.getFloat(eSyssize.naxl);
            y1 = adjacentTop.y2 - adjacentTop.artiklRec.getFloat(eArtikl.size_falz) - iwin().syssizeRec.getFloat(eSyssize.naxl);
            x2 = adjacentRig.x1 + adjacentRig.artiklRec.getFloat(eArtikl.size_falz) + iwin().syssizeRec.getFloat(eSyssize.naxl);
            y2 = adjacentBot.y1 + adjacentBot.artiklRec.getFloat(eArtikl.size_falz) + iwin().syssizeRec.getFloat(eSyssize.naxl);

        } else {
            float X1 = (adjacentLef.type() == TypeElem.IMPOST) ? adjacentLef.x1 + adjacentLef.width() / 2 : adjacentLef.x1;
            float Y2 = (adjacentBot.type() == TypeElem.IMPOST) ? adjacentBot.y2 - adjacentBot.height() / 2 : adjacentBot.y2;
            float X2 = (adjacentRig.type() == TypeElem.IMPOST) ? adjacentRig.x2 - adjacentRig.width() / 2 : adjacentRig.x2;
            float Y1 = (adjacentTop.type() == TypeElem.IMPOST) ? adjacentTop.y1 + adjacentTop.height() / 2 : adjacentTop.y1;
            x1 = X1 + offset(stvLef, adjacentLef);
            y2 = Y2 - offset(stvBot, adjacentBot);
            x2 = X2 - offset(stvRig, adjacentRig);
            y1 = Y1 + offset(stvTop, adjacentTop);
        }
    }


    public void initFurniture(String param) {

        ElemFrame stvLeft = mapFrame.get(LayoutArea.LEFT);
        
        //Фурнитура створки, ручка, подвес
        if (param(param, PKjson.sysfurnID) != -1) {
            sysfurnRec = eSysfurn.find2(param(param, PKjson.sysfurnID));
        } else {
            sysfurnRec = eSysfurn.find3(iwin().nuni);
        }
        //Ручка
        if (param(param, PKjson.artiklHandl) != -1) {
            handleRec = eArtikl.find(param(param, PKjson.artiklHandl), false);
        }
        //Сторона открывания
        if (param(param, PKjson.typeOpen) != -1) {
            this.typeOpen = TypeOpen1.get(param(param, PKjson.typeOpen));
        } else {
            this.typeOpen = (sysfurnRec.getInt(eSysfurn.side_open) == TypeOpen2.LEF.id) ? TypeOpen1.LEFT : TypeOpen1.RIGHT;
        }
        //Подбор текстуры ручки
        if (param(param, PKjson.colorHandl) != -1) {
            handleColor = param(param, PKjson.colorHandl);
        } else {
            handleColor = -3; //iwin().colorID1; //если цвет не установлен подбираю по основной текстуре
        }
        //Положение или высота ручки на створке
        if (param(param, PKjson.positionHandl) != -1) {
            int position = param(param, PKjson.positionHandl);
            if (position == LayoutHandle.SET.id) {
                handleLayout = LayoutHandle.SET;
                handleHeight = param(param, PKjson.heightHandl);
            } else {
                handleLayout = (position == LayoutHandle.MIDL.id) ? LayoutHandle.MIDL : LayoutHandle.CONST;
                handleHeight = stvLeft.height()/ 2;
            }
        } else if (sysfurnRec.getInt(eSysfurn.hand_pos) == LayoutHandle.MIDL.id) {
            handleLayout = LayoutHandle.MIDL;
            handleHeight = stvLeft.height() / 2;
        } else if (sysfurnRec.getInt(eSysfurn.hand_pos) == LayoutHandle.CONST.id) {
            handleLayout = LayoutHandle.CONST;
            handleHeight = stvLeft.height() / 2;
        } else if (sysfurnRec.getInt(eSysfurn.hand_pos) == LayoutHandle.SET.id) {
            handleLayout = LayoutHandle.SET;
            handleHeight = stvLeft.height() / 2;
        } else {
            handleLayout = LayoutHandle.MIDL; //по умолчанию
            handleHeight = stvLeft.height() / 2;
        }
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
                el.joinElement2 = listElem.stream().filter(el2 -> el2 != el.joinElement1 && el2.inside(x1 + width() / 2, y1) == true).findFirst().orElse(null);
                iwin().mapJoin.put(String.valueOf(x1 + width() / 2) + ":" + String.valueOf(y1), el);

            } else if (index == 1) { //Прилигающее нижнее
                el.layoutJoin = LayoutJoin.CBOT;
                el.joinElement1 = mapFrame.get(LayoutArea.BOTTOM);
                el.joinElement2 = listElem.stream().filter(el2 -> el2 != el.joinElement1 && el2.inside(x1 + width() / 2, y2) == true).findFirst().orElse(null);
                iwin().mapJoin.put(String.valueOf(x1 + width() / 2) + ":" + String.valueOf(y2), el);

            } else if (index == 2) { //Прилигающее левое
                el.layoutJoin = LayoutJoin.CLEFT;
                el.joinElement1 = mapFrame.get(LayoutArea.LEFT);
                el.joinElement2 = listElem.stream().filter(el2 -> el2 != el.joinElement1 && el2.inside(x1, y1 + height() / 2) == true).findFirst().orElse(null);
                iwin().mapJoin.put(String.valueOf(x1) + ":" + String.valueOf(y1 + height() / 2), el);

            } else if (index == 3) { //Прилигающее правое
                el.layoutJoin = LayoutJoin.CRIGH;
                el.joinElement1 = mapFrame.get(LayoutArea.RIGHT);
                el.joinElement2 = listElem.stream().filter(el2 -> el2 != el.joinElement1 && el2.inside(x2, y1 + height() / 2) == true).findFirst().orElse(null);
                iwin().mapJoin.put(String.valueOf(x2) + ":" + String.valueOf(y1 + height() / 2), el);
            }
        }
    }

    //Вычисление смещения створки через параметр
    private float offset(ElemSimple profStv, ElemSimple profFrm) {
        Record joiningRec = eJoining.find(profStv.artiklRec, profFrm.artiklRec);
        List<Record> joinvarList = eJoinvar.find(joiningRec.getInt(eJoining.id));
        Record joinvarRec = joinvarList.stream().filter(rec -> rec.getInt(eJoinvar.types) == TypeJoin.VAR10.id).findFirst().orElse(null);
        if (joinvarRec != null) {
            List<Record> joinpar1List = eJoinpar1.find(joinvarRec.getInt(eJoinvar.id));
            Record joinpar1Rec = joinpar1List.stream().filter(rec -> rec.getInt(eJoinpar1.params_id) == 1040).findFirst().orElse(null);
            if (joinpar1Rec != null) {
                return Util.getFloat(joinpar1Rec.getStr(eJoinpar1.text));
            }
        }
        return 0;
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

            if (typeOpen.id == 1 || typeOpen.id == 3) {
                X1 = elemR.x1 + (elemR.x2 - elemR.x1) / 2;
                Y1 = elemR.y1 + (elemR.y2 - elemR.y1) / 2;
                iwin().draw.drawLine(elemL.x1, elemL.y1, elemR.x2, elemR.y1 + (elemR.y2 - elemR.y1) / 2);
                iwin().draw.drawLine(elemL.x1, elemL.y2, elemR.x2, elemR.y1 + (elemR.y2 - elemR.y1) / 2);

            } else if (typeOpen.id == 2 || typeOpen.id == 4) {
                X1 = elemL.x1 + (elemL.x2 - elemL.x1) / 2;
                Y1 = elemL.y1 + (elemL.y2 - elemL.y1) / 2;
                iwin().draw.drawLine(elemR.x2, elemR.y1, elemL.x1, elemL.y1 + (elemL.y2 - elemL.y1) / 2);
                iwin().draw.drawLine(elemR.x2, elemR.y2, elemL.x1, elemL.y1 + (elemL.y2 - elemL.y1) / 2);
            }
            if (typeOpen.id == 3 || typeOpen.id == 4) {
                iwin().draw.drawLine(elemB.x1, elemB.y2, elemT.x1 + (elemT.x2 - elemT.x1) / 2, elemT.y1);
                iwin().draw.drawLine(elemB.x2, elemB.y2, elemT.x1 + (elemT.x2 - elemT.x1) / 2, elemT.y1);
            }
            iwin().draw.strokePolygon(X1 - DX, X1 + DX, X1 + DX, X1 - DX, Y1 - DY, Y1 - DY, Y1 + DY, Y1 + DY, 0xFFFFFFFF, Color.BLACK);
            DX = DX - 12;
            Y1 = Y1 + 20;
            iwin().draw.strokePolygon(X1 - DX, X1 + DX, X1 + DX, X1 - DX, Y1 - DY, Y1 - DY, Y1 + DY, Y1 + DY, 0xFFFFFFFF, Color.BLACK);
        }
    }
}
