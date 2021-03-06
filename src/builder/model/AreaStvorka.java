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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import common.Util;
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
        ElemFrame stvBot = new ElemFrame(this, id + .1f, LayoutArea.BOTT, gson.toJson(Ujson.getAsJsonObject(paramObj, PKjson.stvorkaBottom)));
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

        ElemSimple joinLef = stvLef.joinFlat(LayoutArea.LEFT), joinTop = stvTop.joinFlat(LayoutArea.TOP),
                joinBot = stvBot.joinFlat(LayoutArea.BOTT), joinRig = stvRig.joinFlat(LayoutArea.RIGHT);
        
        if (iwin().syssizeRec.getInt(eSyssize.id) != -1) {
            x1 = joinLef.x2 - joinLef.artiklRec.getFloat(eArtikl.size_falz) - iwin().syssizeRec.getFloat(eSyssize.naxl);
            y1 = joinTop.y2 - joinTop.artiklRec.getFloat(eArtikl.size_falz) - iwin().syssizeRec.getFloat(eSyssize.naxl);
            x2 = joinRig.x1 + joinRig.artiklRec.getFloat(eArtikl.size_falz) + iwin().syssizeRec.getFloat(eSyssize.naxl);
            y2 = joinBot.y1 + joinBot.artiklRec.getFloat(eArtikl.size_falz) + iwin().syssizeRec.getFloat(eSyssize.naxl);

        } else {
            float X1 = (joinLef.type() == TypeElem.IMPOST || joinLef.type() == TypeElem.SHTULP) ? joinLef.x1 + joinLef.width() / 2 : joinLef.x1;
            float Y2 = (joinBot.type() == TypeElem.IMPOST || joinBot.type() == TypeElem.SHTULP) ? joinBot.y2 - joinBot.height() / 2 : joinBot.y2;
            float X2 = (joinRig.type() == TypeElem.IMPOST || joinRig.type() == TypeElem.SHTULP) ? joinRig.x2 - joinRig.width() / 2 : joinRig.x2;
            float Y1 = (joinTop.type() == TypeElem.IMPOST || joinTop.type() == TypeElem.SHTULP) ? joinTop.y1 + joinTop.height() / 2 : joinTop.y1;
            x1 = X1 + offset(stvLef, joinLef);
            y2 = Y2 - offset(stvBot, joinBot);
            x2 = X2 - offset(stvRig, joinRig);
            y1 = Y1 + offset(stvTop, joinTop);
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
                handleHeight = stvLeft.height() / 2;
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
        ElemSimple elemBott = mapFrame.get(LayoutArea.BOTT), elemRight = mapFrame.get(LayoutArea.RIGHT),
                elemTop = mapFrame.get(LayoutArea.TOP), elemLeft = mapFrame.get(LayoutArea.LEFT);        
        //Цикл по сторонам створки
        for (int index = 0; index < 4; index++) {
            elemBott.anglHoriz = 0;
            elemRight.anglHoriz = 90;
            elemTop.anglHoriz = 180;
            elemLeft.anglHoriz = 270;
            mapFrame.entrySet().forEach(elem -> {
                elem.getValue().anglCut[0] = 45;
                elem.getValue().anglCut[1] = 45;
            });

            if (index == 0) { //Угловое соединение правое нижнее
                ElemJoining el = new ElemJoining(id() + (float) (index + 1) / 100, TypeJoin.VAR20, LayoutJoin.RBOT, elemBott, elemRight, 90);
                iwin().mapJoin.put(elemBott.joinPoint(1), el);

            } else if (index == 1) { //Угловое соединение правое верхнее
                ElemJoining el = new ElemJoining(id() + (float) (index + 1) / 100, TypeJoin.VAR20, LayoutJoin.RTOP, elemRight, elemTop, 90);
                iwin().mapJoin.put(elemRight.joinPoint(1), el);

            } else if (index == 2) { //Угловое соединение левое верхнее
                ElemJoining el = new ElemJoining(id() + (float) (index + 1) / 100, TypeJoin.VAR20, LayoutJoin.LTOP, elemTop, elemLeft, 90);
                iwin().mapJoin.put(elemTop.joinPoint(1), el);

            } else if (index == 3) { //Угловое соединение левое нижнее
                ElemJoining el = new ElemJoining(id() + (float) (index + 1) / 100, TypeJoin.VAR20, LayoutJoin.LBOT, elemLeft, elemBott, 90);
                iwin().mapJoin.put(elemLeft.joinPoint(1), el);
            }
        }

        LinkedList<ElemSimple> listElem = iwin().rootArea.listElem(TypeElem.FRAME_SIDE, TypeElem.STVORKA_SIDE, TypeElem.IMPOST, TypeElem.SHTULP);
        for (int index = 0; index < 4; index++) {
             if (index == 0) { //Прилегающее нижнее
                ElemJoining el = new ElemJoining(id() + (float) (index + 5) / 100, TypeJoin.VAR10, LayoutJoin.CBOT, elemBott, elemBott.joinFlat(LayoutArea.BOTT), 0);                               
                iwin().mapJoin.put(elemBott.joinPoint(2), el);
                
            } else if (index == 1) { //Прилегающее верхнее 
                ElemJoining el = new ElemJoining(id() + (float) (index + 5) / 100, TypeJoin.VAR10, LayoutJoin.CTOP, elemTop, elemTop.joinFlat(LayoutArea.TOP), 0);
                iwin().mapJoin.put(elemTop.joinPoint(2), el);

            } else if (index == 2) { //Прилегающее левое
                ElemJoining el = new ElemJoining(id() + (float) (index + 5) / 100, TypeJoin.VAR10, LayoutJoin.CLEFT, elemLeft, elemLeft.joinFlat(LayoutArea.LEFT), 0);
                iwin().mapJoin.put(elemLeft.joinPoint(2), el);

            } else if (index == 3) { //Прилегающее правое
                ElemJoining el = new ElemJoining(id() + (float) (index + 5) / 100, TypeJoin.VAR10, LayoutJoin.CRIGH, elemRight, elemRight.joinFlat(LayoutArea.RIGHT), 0);
                iwin().mapJoin.put(elemRight.joinPoint(2), el);
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
        mapFrame.get(LayoutArea.BOTT).paint();
        mapFrame.get(LayoutArea.LEFT).paint();
        mapFrame.get(LayoutArea.RIGHT).paint();

        if (typeOpen != TypeOpen1.INVALID) {
            float DX = 20, DY = 60, X1 = 0, Y1 = 0;
            ElemSimple elemL = mapFrame.get(LayoutArea.LEFT);
            ElemSimple elemR = mapFrame.get(LayoutArea.RIGHT);
            ElemSimple elemT = mapFrame.get(LayoutArea.TOP);
            ElemSimple elemB = mapFrame.get(LayoutArea.BOTT);

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
