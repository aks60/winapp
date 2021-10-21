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
import java.util.LinkedList;
import builder.Wincalc;
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
    public float naxlest = 0;

    public AreaStvorka(Wincalc iwin, AreaSimple owner, float id, String param) {
        super(iwin, owner, id, Type.STVORKA, Layout.VERT, (owner.x2 - owner.x1), (owner.y2 - owner.y1), iwin.colorID1, iwin.colorID2, iwin.colorID3, param);

        Gson gson = new GsonBuilder().create();
        JsonObject paramObj = new GsonBuilder().create().fromJson(param, JsonObject.class);

        //Добавим рамы створки    Ujson.getAsJsonObject(paramObj, stvKey)  
        ElemFrame stvBot = new ElemFrame(this, id + .1f, Layout.BOTT, gson.toJson(UJson.getAsJsonObject(paramObj, PKjson.stvorkaBottom)));
        mapFrame.put(stvBot.layout(), stvBot);
        ElemFrame stvRigh = new ElemFrame(this, id + .2f, Layout.RIGHT, gson.toJson(UJson.getAsJsonObject(paramObj, PKjson.stvorkaRight)));
        mapFrame.put(stvRigh.layout(), stvRigh);
        ElemFrame stvTop = new ElemFrame(this, id + .3f, Layout.TOP, gson.toJson(UJson.getAsJsonObject(paramObj, PKjson.stvorkaTop)));
        mapFrame.put(stvTop.layout(), stvTop);
        ElemFrame stvLeft = new ElemFrame(this, id + .4f, Layout.LEFT, gson.toJson(UJson.getAsJsonObject(paramObj, PKjson.stvorkaLeft)));
        mapFrame.put(stvLeft.layout(), stvLeft);

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
            
            System.out.println(joinBot.y1 + "  " + joinBot.artiklRec.getFloat(eArtikl.size_falz) + "  " + iwin.syssizeRec.getFloat(eSyssize.naxl));

        } else {
            float X1 = (joinLef.type() == Type.IMPOST || joinLef.type() == Type.SHTULP || joinLef.type() == Type.STOIKA) ? joinLef.x1 + joinLef.width() / 2 : joinLef.x1;
            float Y2 = (joinBot.type() == Type.IMPOST || joinBot.type() == Type.SHTULP || joinBot.type() == Type.STOIKA) ? joinBot.y2 - joinBot.height() / 2 : joinBot.y2;
            float X2 = (joinRig.type() == Type.IMPOST || joinRig.type() == Type.SHTULP || joinBot.type() == Type.STOIKA) ? joinRig.x2 - joinRig.width() / 2 : joinRig.x2;
            float Y1 = (joinTop.type() == Type.IMPOST || joinTop.type() == Type.SHTULP || joinBot.type() == Type.STOIKA) ? joinTop.y1 + joinTop.height() / 2 : joinTop.y1;
            x1 = X1 + offset(stvLef, joinLef);
            y2 = Y2 - offset(stvBot, joinBot);
            x2 = X2 - offset(stvRig, joinRig);
            y1 = Y1 + offset(stvTop, joinTop);
        }
    }

    public void initFurniture(String param) {

        ElemFrame stvLeft = mapFrame.get(Layout.LEFT);

        //Фурнитура створки, ручка, подвес
        if (param(param, PKjson.sysfurnID) != -1) {
            sysfurnRec = eSysfurn.find2(param(param, PKjson.sysfurnID));
            paramCheck[0] = false;
        } else {
            sysfurnRec = eSysfurn.find3(iwin.nuni); //ищем первую в системе
        }
        //Ручка
        if (param(param, PKjson.artiklHandl) != -1) {
            handleRec = eArtikl.find(param(param, PKjson.artiklHandl), false);
            paramCheck[1] = false;
        }
        //Текстура ручки
        if (param(param, PKjson.colorHandl) != -1) {
            handleColor = param(param, PKjson.colorHandl);
            paramCheck[2] = false;
        }
        //Подвес (петли)
        if (param(param, PKjson.artiklLoop) != -1) {
            loopRec = eArtikl.find(param(param, PKjson.artiklLoop), false);
            paramCheck[3] = false;
        }
        //Текстура подвеса
        if (param(param, PKjson.colorLoop) != -1) {
            loopColor = param(param, PKjson.colorLoop);
            paramCheck[4] = false;
        }
        //Замок
        if (param(param, PKjson.artiklLock) != -1) {
            lockRec = eArtikl.find(param(param, PKjson.artiklLock), false);
            paramCheck[5] = false;
        }
        //Текстура замка
        if (param(param, PKjson.colorLock) != -1) {
            lockColor = param(param, PKjson.colorLock);
            paramCheck[6] = false;
        }
        //Сторона открывания
        if (param(param, PKjson.typeOpen) != -1) {
            typeOpen = TypeOpen1.get(param(param, PKjson.typeOpen));
        } else {
            typeOpen = (sysfurnRec.getInt(eSysfurn.side_open) == TypeOpen2.LEF.id) ? TypeOpen1.LEFT : TypeOpen1.RIGHT;
        }
        //Положение или высота ручки на створке
        if (param(param, PKjson.positionHandl) != -1) {
            int position = param(param, PKjson.positionHandl);
            if (position == LayoutHandle.VARIAT.id) {
                handleLayout = LayoutHandle.VARIAT;
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
    public void joinFrame() {
        ElemSimple stvBott = mapFrame.get(Layout.BOTT), stvRight = mapFrame.get(Layout.RIGHT),
                stvTop = mapFrame.get(Layout.TOP), stvLeft = mapFrame.get(Layout.LEFT);
        stvBott.anglHoriz = 0;
        stvRight.anglHoriz = 90;
        stvTop.anglHoriz = 180;
        stvLeft.anglHoriz = 270;
        mapFrame.entrySet().forEach(elem -> {
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
        ElemJoining.create(stvBott.joinPoint(2), iwin, TypeJoin.VAR10, LayoutJoin.CBOT, stvBott, stvBott.joinFlat(Layout.BOTT), 0);
        //Прилегающее верхнее 
        ElemJoining.create(stvTop.joinPoint(2), iwin, TypeJoin.VAR10, LayoutJoin.CTOP, stvTop, stvTop.joinFlat(Layout.TOP), 0);
        //Прилегающее левое
        ElemJoining.create(stvLeft.joinPoint(2), iwin, TypeJoin.VAR10, LayoutJoin.CLEFT, stvLeft, stvLeft.joinFlat(Layout.LEFT), 0);
        //Прилегающее правое
        ElemJoining.create(stvRight.joinPoint(2), iwin, TypeJoin.VAR10, LayoutJoin.CRIGH, stvRight, stvRight.joinFlat(Layout.RIGHT), 0);
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
                return UCom.getFloat(joinpar1Rec.getStr(eJoinpar1.text));
            }
        }
        return 0;
    }

    @Override
    public void paint() {

        mapFrame.get(Layout.TOP).paint();
        mapFrame.get(Layout.BOTT).paint();
        mapFrame.get(Layout.LEFT).paint();
        mapFrame.get(Layout.RIGHT).paint();

        if (typeOpen != TypeOpen1.INVALID) {
            float DX = 20, DY = 60, X1 = 0, Y1 = 0;
            ElemSimple elemL = mapFrame.get(Layout.LEFT);
            ElemSimple elemR = mapFrame.get(Layout.RIGHT);
            ElemSimple elemT = mapFrame.get(Layout.TOP);
            ElemSimple elemB = mapFrame.get(Layout.BOTT);

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
            if (typeOpen.id == 3 || typeOpen.id == 4) {
                Draw.drawLine(iwin, elemB.x1, elemB.y2, elemT.x1 + (elemT.x2 - elemT.x1) / 2, elemT.y1);
                Draw.drawLine(iwin, elemB.x2, elemB.y2, elemT.x1 + (elemT.x2 - elemT.x1) / 2, elemT.y1);
            }
            Draw.strokePolygon(iwin, X1 - DX, X1 + DX, X1 + DX, X1 - DX, Y1 - DY, Y1 - DY, Y1 + DY, Y1 + DY, 0xFFFFFFFF, Color.BLACK);
            DX = DX - 12;
            Y1 = Y1 + 20;
            Draw.strokePolygon(iwin, X1 - DX, X1 + DX, X1 + DX, X1 - DX, Y1 - DY, Y1 - DY, Y1 + DY, Y1 + DY, 0xFFFFFFFF, Color.BLACK);
        }
    }
}
