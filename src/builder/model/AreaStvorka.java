package builder.model;

import dataset.Record;
import domain.eArtikl;
import domain.eJoining;
import domain.eJoinpar1;
import domain.eJoinvar;
import domain.eSyssize;
import enums.Layout;
import enums.LayoutJoin;
import enums.TypeOpen1;
import enums.TypeJoin;
import java.awt.Color;
import builder.Wincalc;
import builder.making.Joining;
import builder.making.Specific;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import common.UCom;
import domain.eSysfurn;
import enums.LayoutHandle;
import enums.PKjson;
import enums.Type;
import enums.TypeOpen2;
import frames.UJson;
import frames.swing.Draw;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AreaStvorka extends AreaSimple {

    public Record sysfurnRec = eSysfurn.up.newRecord(); //фурнитура
    public Record handleRec = eArtikl.virtualRec(); //ручка
    public Record loopRec = eArtikl.virtualRec(); //подвес(петли)
    public Record lockRec = eArtikl.virtualRec(); //замок

    public int handleColor = -3; //цвет ручки
    public int loopColor = -3; //цвет подвеса
    public int lockColor = -3; //цвет замка

    public float handleHeight = 0; //высота ручки
    public TypeOpen1 typeOpen = TypeOpen1.INVALID; //направление открывания
    public LayoutHandle handleLayout = LayoutHandle.VARIAT; //положение ручки на створке   
    public boolean paramCheck[] = {true, true, true, true, true, true, true};
    public float offset[] = {0, 0, 0, 0};

    public AreaStvorka(Wincalc iwin, AreaSimple owner, float id, JsonObject param) {
        super(iwin, owner, id, Type.STVORKA, Layout.VERT, (owner.x2 - owner.x1), (owner.y2 - owner.y1), iwin.colorID1, iwin.colorID2, iwin.colorID3, param);
        
        //Добавим рамы створки    Ujson.getAsJsonObject(param, stvKey)  
        ElemFrame stvBot = new ElemFrame(this, id + .1f, Layout.BOTT, param.getAsJsonObject(PKjson.stvorkaBottom));
        frames.put(stvBot.layout, stvBot);
        ElemFrame stvRigh = new ElemFrame(this, id + .2f, Layout.RIGHT, param.getAsJsonObject(PKjson.stvorkaRight));
        frames.put(stvRigh.layout, stvRigh);
        ElemFrame stvTop = new ElemFrame(this, id + .3f, Layout.TOP, param.getAsJsonObject(PKjson.stvorkaTop));
        frames.put(stvTop.layout, stvTop);
        ElemFrame stvLeft = new ElemFrame(this, id + .4f, Layout.LEFT, param.getAsJsonObject(PKjson.stvorkaLeft));
        frames.put(stvLeft.layout, stvLeft);

        //Положение элементов створки с учётом нахлёста
        setNaxlest(stvLeft, stvBot, stvRigh, stvTop);
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
    private void setNaxlest(ElemFrame stvLef, ElemFrame stvBot, ElemFrame stvRig, ElemFrame stvTop) {
        ElemSimple joinLef = stvLef.joinFlat(Layout.LEFT), joinTop = stvTop.joinFlat(Layout.TOP),
                joinBot = stvBot.joinFlat(Layout.BOTT), joinRig = stvRig.joinFlat(Layout.RIGHT);

        if (iwin.syssizeRec.getInt(eSyssize.id) != -1) {
            x1 = joinLef.x2 - joinLef.artiklRec.getFloat(eArtikl.size_falz) - iwin.syssizeRec.getFloat(eSyssize.naxl);
            y1 = joinTop.y2 - joinTop.artiklRec.getFloat(eArtikl.size_falz) - iwin.syssizeRec.getFloat(eSyssize.naxl);
            x2 = joinRig.x1 + joinRig.artiklRec.getFloat(eArtikl.size_falz) + iwin.syssizeRec.getFloat(eSyssize.naxl);
            y2 = joinBot.y1 + joinBot.artiklRec.getFloat(eArtikl.size_falz) + iwin.syssizeRec.getFloat(eSyssize.naxl);

        } else { //Вычисление смещения створки через параметр
            try {
                iwin.mapJoin.clear();
                iwin.mapJoin.put(stvBot.joinPoint(2), new ElemJoining(iwin, TypeJoin.VAR10, LayoutJoin.CBOT, stvBot, joinBot, 0));
                iwin.mapJoin.put(stvRig.joinPoint(2), new ElemJoining(iwin, TypeJoin.VAR10, LayoutJoin.CRIGH, stvRig, joinRig, 0));
                iwin.mapJoin.put(stvTop.joinPoint(2), new ElemJoining(iwin, TypeJoin.VAR10, LayoutJoin.CTOP, stvTop, joinTop, 0));
                iwin.mapJoin.put(stvLef.joinPoint(2), new ElemJoining(iwin, TypeJoin.VAR10, LayoutJoin.CLEFT, stvLef, joinLef, 0));
                Joining joining = new Joining(iwin, true);
                joining.calc();
                                
                y2 = (joinBot.y2 - joinBot.artiklRec.getFloat(eArtikl.size_centr)) - offset[0];
                x2 = (joinRig.x2 - joinRig.artiklRec.getFloat(eArtikl.size_centr)) - offset[1];
                y1 = (joinTop.y1 + joinTop.artiklRec.getFloat(eArtikl.size_centr)) + offset[2];
                x1 = (joinLef.x1 + joinLef.artiklRec.getFloat(eArtikl.size_centr)) + offset[3];

            } catch (Exception e) {
                System.err.println("Ошибка:model.AreaStvorka.setNaxlest() " + e);
            } finally {
                iwin.mapJoin.clear();
            }
        }

    }

    public void initFurniture(JsonObject param) {

        ElemFrame stvLeft = frames.get(Layout.LEFT);

        //Фурнитура створки, ручка, подвес
        if (isJson(param, PKjson.sysfurnID)) {
            sysfurnRec = eSysfurn.find2(param.get(PKjson.sysfurnID).getAsInt());
            paramCheck[0] = false;
        } else {
            sysfurnRec = eSysfurn.find3(iwin.nuni); //ищем первую в системе
        }
        //Ручка
        if (isJson(param, PKjson.artiklHandl)) {
            handleRec = eArtikl.find(param.get(PKjson.artiklHandl).getAsInt(), false);
            paramCheck[1] = false;
        }
        //Текстура ручки
        if (isJson(param, PKjson.colorHandl)) {
            handleColor = param.get(PKjson.colorHandl).getAsInt();
            paramCheck[2] = false;
        }
        //Подвес (петли)
        if (isJson(param, PKjson.artiklLoop)) {
            loopRec = eArtikl.find(param.get(PKjson.artiklLoop).getAsInt(), false);
            paramCheck[3] = false;
        }
        //Текстура подвеса
        if (isJson(param, PKjson.colorLoop)) {
            loopColor = param.get(PKjson.colorLoop).getAsInt();
            paramCheck[4] = false;
        }
        //Замок
        if (isJson(param, PKjson.artiklLock)) {
            lockRec = eArtikl.find(param.get(PKjson.artiklLock).getAsInt(), false);
            paramCheck[5] = false;
        }
        //Текстура замка
        if (isJson(param, PKjson.colorLock)) {
            lockColor = param.get(PKjson.colorLock).getAsInt();
            paramCheck[6] = false;
        }
        //Сторона открывания
        if (isJson(param, PKjson.typeOpen)) {
            typeOpen = TypeOpen1.get(param.get(PKjson.typeOpen).getAsInt());
        } else {
            typeOpen = (sysfurnRec.getInt(eSysfurn.side_open) == TypeOpen2.LEF.id) ? TypeOpen1.LEFT : TypeOpen1.RIGHT;
        }
        //Положение или высота ручки на створке
        if (isJson(param, PKjson.positionHandl)) {
            int position = param.get(PKjson.positionHandl).getAsInt();
            if (position == LayoutHandle.VARIAT.id) {
                handleLayout = LayoutHandle.VARIAT;
                handleHeight = param.get(PKjson.heightHandl).getAsInt();
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
        } else if (sysfurnRec.getInt(eSysfurn.hand_pos) == LayoutHandle.VARIAT.id) {
            handleLayout = LayoutHandle.VARIAT;
            handleHeight = stvLeft.height() / 2;
        } else {
            handleLayout = LayoutHandle.MIDL; //по умолчанию
            handleHeight = stvLeft.height() / 2;
        }
    }

    protected void addFilling(ElemGlass glass, Specific spcAdd) {
        if (glass.anglHoriz == glass.sideHoriz[0] || glass.anglHoriz == glass.sideHoriz[2]) { //по горизонтали
            spcAdd.width += glass.width() + 2 * glass.gzazo;
            spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);

        } else if (glass.anglHoriz == glass.sideHoriz[1] || glass.anglHoriz == glass.sideHoriz[3]) { //по вертикали
            spcAdd.width += glass.height() + 2 * glass.gzazo;
            spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);

        } else {
            System.err.println("Промах:builder.model.AreaStvorka.addFilling()");
        }
        spcAdd.anglCut1 = 45;
        spcAdd.anglCut2 = 45;
        glass.spcRec.spcList.add(spcAdd);
    }

    @Override
    public void joining() {
        ElemSimple stvBott = frames.get(Layout.BOTT), stvRight = frames.get(Layout.RIGHT),
                stvTop = frames.get(Layout.TOP), stvLeft = frames.get(Layout.LEFT);
        stvBott.anglHoriz = 0;
        stvRight.anglHoriz = 90;
        stvTop.anglHoriz = 180;
        stvLeft.anglHoriz = 270;
        frames.entrySet().forEach(elem -> {
            elem.getValue().anglCut[0] = 45;
            elem.getValue().anglCut[1] = 45;
        });

        //Угловое соединение правое нижнее
        ElemJoining.create(stvBott.joinPoint(1), iwin, TypeJoin.VAR20, LayoutJoin.RBOT, stvBott, stvRight, 90);
        //Угловое соединение правое верхнее
        ElemJoining.create(stvRight.joinPoint(1), iwin, TypeJoin.VAR20, LayoutJoin.RTOP, stvRight, stvTop, 90);
        //Угловое соединение левое верхнее
        ElemJoining.create(stvTop.joinPoint(1), iwin, TypeJoin.VAR20, LayoutJoin.LTOP, stvTop, stvLeft, 90);
        //Угловое соединение левое нижнее
        ElemJoining.create(stvLeft.joinPoint(1), iwin, TypeJoin.VAR20, LayoutJoin.LBOT, stvLeft, stvBott, 90);

        //Прилегающее нижнее
        ElemSimple frmBott = (stvBott.joinFlat(Layout.BOTT) != null) ? stvBott.joinFlat(Layout.BOTT) : root.frames.get(Layout.BOTT);
        ElemJoining.create(stvBott.joinPoint(2), iwin, TypeJoin.VAR10, LayoutJoin.CBOT, stvBott, frmBott, 0);
        //Прилегающее верхнее 
        ElemSimple frmTop = (stvTop.joinFlat(Layout.TOP) != null) ? stvTop.joinFlat(Layout.TOP) : root.frames.get(Layout.TOP);
        ElemJoining.create(stvTop.joinPoint(2), iwin, TypeJoin.VAR10, LayoutJoin.CTOP, stvTop, frmTop, 0);
        //Прилегающее левое
        ElemSimple frmLeft = (stvLeft.joinFlat(Layout.LEFT) != null) ? stvLeft.joinFlat(Layout.LEFT) : root.frames.get(Layout.LEFT);
        ElemJoining.create(stvLeft.joinPoint(2), iwin, TypeJoin.VAR10, LayoutJoin.CLEFT, stvLeft, frmLeft, 0);
        //Прилегающее правое
        ElemSimple frmRight = (stvRight.joinFlat(Layout.RIGHT) != null) ? stvRight.joinFlat(Layout.RIGHT) : root.frames.get(Layout.RIGHT);
        ElemJoining.create(stvRight.joinPoint(2), iwin, TypeJoin.VAR10, LayoutJoin.CRIGH, stvRight, frmRight, 0);
    }

    @Override
    public void paint() {

        frames.get(Layout.TOP).paint();
        frames.get(Layout.BOTT).paint();
        frames.get(Layout.LEFT).paint();
        frames.get(Layout.RIGHT).paint();

        if (typeOpen != TypeOpen1.INVALID) {
            float DX = 20, DY = 60, X1 = 0, Y1 = 0;
            ElemSimple elemL = frames.get(Layout.LEFT);
            ElemSimple elemR = frames.get(Layout.RIGHT);
            ElemSimple elemT = frames.get(Layout.TOP);
            ElemSimple elemB = frames.get(Layout.BOTT);

            if (typeOpen.id == 1 || typeOpen.id == 3) {
                X1 = elemR.x1 + (elemR.x2 - elemR.x1) / 2;
                Y1 = elemR.y1 + (elemR.y2 - elemR.y1) / 2;
                Draw.drawLine(iwin, elemL.x1, elemL.y1, elemR.x2, elemR.y1 + (elemR.y2 - elemR.y1) / 2);
                Draw.drawLine(iwin, elemL.x1, elemL.y2, elemR.x2, elemR.y1 + (elemR.y2 - elemR.y1) / 2);

            } else if (typeOpen.id == 2 || typeOpen.id == 4) {
                X1 = elemL.x1 + (elemL.x2 - elemL.x1) / 2;
                Y1 = elemL.y1 + (elemL.y2 - elemL.y1) / 2;
                Draw.drawLine(iwin, elemR.x2, elemR.y1, elemL.x1, elemL.y1 + (elemL.y2 - elemL.y1) / 2);
                Draw.drawLine(iwin, elemR.x2, elemR.y2, elemL.x1, elemL.y1 + (elemL.y2 - elemL.y1) / 2);
            }

            if (root.type == Type.DOOR) {
                DY = 20;
                iwin.gc2d.rotate(Math.toRadians(-90), X1 - DX, Y1 - DY);
                Draw.strokePolygon(iwin, X1 - DX, X1 + DX, X1 + DX, X1 - DX, Y1 - DY, Y1 - DY, Y1 + DY, Y1 + DY, 0xFFFFFFFF, Color.BLACK);
                DX = DX - 12;
                Y1 = Y1 + 20;
                DY = 60;
                Draw.strokePolygon(iwin, X1 - DX, X1 + DX, X1 + DX, X1 - DX, Y1 - DY, Y1 - DY, Y1 + DY, Y1 + DY, 0xFFFFFFFF, Color.BLACK);

            } else {
                Draw.strokePolygon(iwin, X1 - DX, X1 + DX, X1 + DX, X1 - DX, Y1 - DY, Y1 - DY, Y1 + DY, Y1 + DY, 0xFFFFFFFF, Color.BLACK);
                DX = DX - 12;
                Y1 = Y1 + 20;
            }
        }
    }
}
