package builder.model;

import builder.IStvorka;
import builder.IArea5e;
import builder.ICom5t;
import builder.IElem5e;
import dataset.Record;
import domain.eArtikl;
import domain.eSyssize;
import enums.Layout;
import enums.LayoutJoin;
import enums.TypeOpen1;
import enums.TypeJoin;
import java.awt.Color;
import builder.Wincalc;
import builder.making.Cal5e;
import builder.making.Joining;
import builder.script.GsonElem;
import com.google.gson.JsonObject;
import common.LinkedList2;
import common.eProp;
import domain.eArtdet;
import domain.eColor;
import domain.eElement;
import domain.eSysfurn;
import enums.LayoutHandle;
import enums.PKjson;
import enums.Type;
import enums.TypeOpen2;
import frames.swing.DrawStroke;

public class AreaStvorka extends AreaSimple implements IStvorka {

    private Record sysfurnRec = eSysfurn.up.newRecord(); //фурнитура
    private Record handleRec = eArtikl.virtualRec(); //ручка
    private Record loopRec = eArtikl.virtualRec(); //подвес(петли)
    private Record lockRec = eArtikl.virtualRec(); //замок
    private Record mosqRec = eArtikl.virtualRec(); //москитка
    private Record elementRec = eElement.up.newRecord(); //состав москидки  

    private int handleColor = -3; //цвет ручки
    private int loopColor = -3; //цвет подвеса
    private int lockColor = -3; //цвет замка
    private int mosqColor = -3; //цвет москитки

    private float handleHeight = 0; //высота ручки
    private TypeOpen1 typeOpen = TypeOpen1.INVALID; //направление открывания
    private LayoutHandle handleLayout = LayoutHandle.VARIAT; //положение ручки на створке      
    private boolean paramCheck[] = {true, true, true, true, true, true, true, true};
    private float offset[] = {0, 0, 0, 0};

    public AreaStvorka(Wincalc winc, IArea5e owner, GsonElem gson) {
        super(winc, owner, gson, (owner.x2() - owner.x1()), (owner.y2() - owner.y1()));

        //Добавим рамы створки    Ujson.getAsJsonObject(param, stvKey)  
        IElem5e stvBot = (eProp.old.read().equals("0")) ? new builder.model.ElemFrame(this, gson.id() + .1f, Layout.BOTT, gson.param().getAsJsonObject(PKjson.stvorkaBottom), gson)
                : new builder.model.ElemFrame(this, gson.id() + .1f, Layout.BOTT, gson.param().getAsJsonObject(PKjson.stvorkaBottom), gson);
        frames.put(stvBot.layout(), stvBot);
        IElem5e stvRigh = (eProp.old.read().equals("0")) ? new builder.model.ElemFrame(this, gson.id() + .2f, Layout.RIGHT, gson.param().getAsJsonObject(PKjson.stvorkaRight), gson)
                : new builder.model.ElemFrame(this, gson.id() + .2f, Layout.RIGHT, gson.param().getAsJsonObject(PKjson.stvorkaRight), gson);
        frames.put(stvRigh.layout(), stvRigh);
        IElem5e stvTop = (eProp.old.read().equals("0")) ? new builder.model.ElemFrame(this, gson.id() + .3f, Layout.TOP, gson.param().getAsJsonObject(PKjson.stvorkaTop), gson)
                : new builder.model.ElemFrame(this, gson.id() + .3f, Layout.TOP, gson.param().getAsJsonObject(PKjson.stvorkaTop), gson);
        frames.put(stvTop.layout(), stvTop);
        IElem5e stvLeft = (eProp.old.read().equals("0")) ? new builder.model.ElemFrame(this, gson.id() + .4f, Layout.LEFT, gson.param().getAsJsonObject(PKjson.stvorkaLeft), gson)
                : new builder.model.ElemFrame(this, gson.id() + .4f, Layout.LEFT, gson.param().getAsJsonObject(PKjson.stvorkaLeft), gson);
        frames.put(stvLeft.layout(), stvLeft);

        //Положение элементов створки с учётом нахлёста
        setNaxlest(stvLeft, stvBot, stvRigh, stvTop);
        //Коррекция фреймов створок с учётом нахлёста
        stvBot.setLocation();
        stvRigh.setLocation();
        stvTop.setLocation();
        stvLeft.setLocation();

        initFurniture(gson.param());

        stvBot.spcRec().width = width();
        stvTop.spcRec().width = width();
        stvRigh.spcRec().height = height();
        stvLeft.spcRec().height = height();
    }

    /**
     * Коррекция координат area створки с учётом нахлёста
     */
    private void setNaxlest(IElem5e stvLef, IElem5e stvBot, IElem5e stvRig, IElem5e stvTop) {
        Object o1 = stvLef.joinFlat(Layout.LEFT);
        IElem5e joinLef = stvLef.joinFlat(Layout.LEFT), 
                joinTop = stvTop.joinFlat(Layout.TOP),
                joinBot = stvBot.joinFlat(Layout.BOTT), 
                joinRig = stvRig.joinFlat(Layout.RIGHT);

        if (winc.syssizeRec().getInt(eSyssize.id) != -1) {
            float naxl = winc.syssizeRec().getFloat(eSyssize.naxl);
            x1 = x1 + joinLef.artiklRec().getFloat(eArtikl.height) - joinLef.artiklRec().getFloat(eArtikl.size_centr) - joinLef.artiklRec().getFloat(eArtikl.size_falz) - naxl;
            y1 = y1 + joinTop.artiklRec().getFloat(eArtikl.height) - joinTop.artiklRec().getFloat(eArtikl.size_centr) - joinTop.artiklRec().getFloat(eArtikl.size_falz) - naxl;
            x2 = x2 - joinRig.artiklRec().getFloat(eArtikl.height) + joinRig.artiklRec().getFloat(eArtikl.size_centr) + joinRig.artiklRec().getFloat(eArtikl.size_falz) + naxl;
            y2 = y2 - joinBot.artiklRec().getFloat(eArtikl.height) + joinBot.artiklRec().getFloat(eArtikl.size_centr) + joinBot.artiklRec().getFloat(eArtikl.size_falz) + naxl;

        } else { //Вычисление смещения створки через параметр
            try {
                winc.mapJoin.clear();
                winc.mapJoin.put(stvBot.joinPoint(2), new ElemJoining(winc, TypeJoin.VAR10, LayoutJoin.CBOT, stvBot, joinBot, 0));
                winc.mapJoin.put(stvRig.joinPoint(2), new ElemJoining(winc, TypeJoin.VAR10, LayoutJoin.CRIGH, stvRig, joinRig, 0));
                winc.mapJoin.put(stvTop.joinPoint(2), new ElemJoining(winc, TypeJoin.VAR10, LayoutJoin.CTOP, stvTop, joinTop, 0));
                winc.mapJoin.put(stvLef.joinPoint(2), new ElemJoining(winc, TypeJoin.VAR10, LayoutJoin.CLEFT, stvLef, joinLef, 0));
                Cal5e joining = new Joining(winc, true);
                joining.calc();

                x1 = (joinLef.x1() + joinLef.artiklRec().getFloat(eArtikl.size_centr)) + offset[3];
                y1 = (joinTop.y1() + joinTop.artiklRec().getFloat(eArtikl.size_centr)) + offset[2];
                x2 = (joinRig.x2() - joinRig.artiklRec().getFloat(eArtikl.size_centr)) - offset[1];
                y2 = (joinBot.y2() - joinBot.artiklRec().getFloat(eArtikl.size_centr)) - offset[0];
                
            } catch (Exception e) {
                System.err.println("Ошибка:model.AreaStvorka.setNaxlest() " + e);
            } finally {
                winc.mapJoin.clear();
            }
        }

    }

    /**
     * Фурнитура через параметр или первая в сстеме см. табл. sysfurn
     */
    @Override
    public void initFurniture(JsonObject param) {

        IElem5e stvLeft = frames.get(Layout.LEFT);

        //Фурнитура створки, ручка, подвес
        if (isJson(param, PKjson.sysfurnID)) {
            sysfurnRec = eSysfurn.find2(param.get(PKjson.sysfurnID).getAsInt());
            paramCheck[0] = false;
        } else {
            sysfurnRec = eSysfurn.find3(winc.nuni()); //ищем первую в системе
        }
        //Ручка
        if (isJson(param, PKjson.artiklHandl)) {
            handleRec = eArtikl.find(param.get(PKjson.artiklHandl).getAsInt(), false);
            paramCheck[1] = false;
        } else {
            handleRec = eArtikl.find(sysfurnRec.getInt(eSysfurn.artikl_id1), false);
            paramCheck[1] = true;
        }
        //Текстура ручки
        if (isJson(param, PKjson.colorHandl)) {
            handleColor = param.get(PKjson.colorHandl).getAsInt();
            paramCheck[2] = false;
        } else {
            int colorFK = eArtdet.find(handleRec.getInt(eArtikl.id)).getInt(eArtdet.color_fk);
            handleColor = eColor.find3(colorFK).getInt(eColor.id);
            paramCheck[2] = true;
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
            paramCheck[7] = false;
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

    /**
     * Угловые и прилегающие соединения
     */
    @Override
    public void joining() {
        IElem5e stvBott = frames.get(Layout.BOTT), stvRight = frames.get(Layout.RIGHT),
                stvTop = frames.get(Layout.TOP), stvLeft = frames.get(Layout.LEFT);
        stvBott.anglHoriz(0);
        stvRight.anglHoriz(90);
        stvTop.anglHoriz(180);
        stvLeft.anglHoriz(270);
        frames.entrySet().forEach(elem -> {
            elem.getValue().anglCut()[0] = 45;
            elem.getValue().anglCut()[1] = 45;
        });

        //Угловое соединение правое нижнее
        winc.mapJoin.put(stvBott.joinPoint(1), ElemJoining.create(winc, TypeJoin.VAR20, LayoutJoin.RBOT, stvBott, stvRight, 90));
        //Угловое соединение правое верхнее
        winc.mapJoin.put(stvRight.joinPoint(1), ElemJoining.create(winc, TypeJoin.VAR20, LayoutJoin.RTOP, stvRight, stvTop, 90));
        //Угловое соединение левое верхнее
        winc.mapJoin.put(stvTop.joinPoint(1), ElemJoining.create(winc, TypeJoin.VAR20, LayoutJoin.LTOP, stvTop, stvLeft, 90));
        //Угловое соединение левое нижнее
        winc.mapJoin.put(stvLeft.joinPoint(1), ElemJoining.create(winc, TypeJoin.VAR20, LayoutJoin.LBOT, stvLeft, stvBott, 90));

        //Прилегающее нижнее
        IElem5e frmBott = (stvBott.joinFlat(Layout.BOTT) != null) ? stvBott.joinFlat(Layout.BOTT) : root.frames().get(Layout.BOTT);
        winc.mapJoin.put(stvBott.joinPoint(2), ElemJoining.create(winc, TypeJoin.VAR10, LayoutJoin.CBOT, stvBott, frmBott, 0));
        //Прилегающее левое
        IElem5e frmLeft = (stvLeft.joinFlat(Layout.LEFT) != null) ? stvLeft.joinFlat(Layout.LEFT) : root.frames().get(Layout.LEFT);
        winc.mapJoin.put(stvLeft.joinPoint(2), ElemJoining.create(winc, TypeJoin.VAR10, LayoutJoin.CLEFT, stvLeft, frmLeft, 0));
        //Прилегающее верхнее 
        IElem5e frmTop = (stvTop.joinFlat(Layout.TOP) != null) ? stvTop.joinFlat(Layout.TOP) : root.frames().get(Layout.TOP);
        winc.mapJoin.put(stvTop.joinPoint(2), ElemJoining.create(winc, TypeJoin.VAR10, LayoutJoin.CTOP, stvTop, frmTop, 0));
        //Прилегающее правое
        IElem5e frmRight = (stvRight.joinFlat(Layout.RIGHT) != null) ? stvRight.joinFlat(Layout.RIGHT) : root.frames().get(Layout.RIGHT);
        winc.mapJoin.put(stvRight.joinPoint(2), ElemJoining.create(winc, TypeJoin.VAR10, LayoutJoin.CRIGH, stvRight, frmRight, 0));
    }

    /**
     * Прорисовка створки
     */
    @Override
    public void paint() {

        frames.get(Layout.TOP).paint();
        frames.get(Layout.BOTT).paint();
        frames.get(Layout.LEFT).paint();
        frames.get(Layout.RIGHT).paint();

        if (typeOpen != TypeOpen1.INVALID) {
            float DX = 20, DY = 60, X1 = 0, Y1 = 0;
            IElem5e elemL = frames.get(Layout.LEFT);
            IElem5e elemR = frames.get(Layout.RIGHT);
            IElem5e elemT = frames.get(Layout.TOP);
            IElem5e elemB = frames.get(Layout.BOTT);

            if (typeOpen.id == 1 || typeOpen.id == 3) {
                X1 = elemR.x1() + (elemR.x2() - elemR.x1()) / 2;
                Y1 = elemR.y1() + (elemR.y2() - elemR.y1()) / 2;
                DrawStroke.drawLine(winc, elemL.x1(), elemL.y1(), elemR.x2(), elemR.y1() + (elemR.y2() - elemR.y1()) / 2);
                DrawStroke.drawLine(winc, elemL.x1(), elemL.y2(), elemR.x2(), elemR.y1() + (elemR.y2() - elemR.y1()) / 2);

            } else if (typeOpen.id == 2 || typeOpen.id == 4) {
                X1 = elemL.x1() + (elemL.x2() - elemL.x1()) / 2;
                Y1 = elemL.y1() + (elemL.y2() - elemL.y1()) / 2;
                DrawStroke.drawLine(winc, elemR.x2(), elemR.y1(), elemL.x1(), elemL.y1() + (elemL.y2() - elemL.y1()) / 2);
                DrawStroke.drawLine(winc, elemR.x2(), elemR.y2(), elemL.x1(), elemL.y1() + (elemL.y2() - elemL.y1()) / 2);
            }
            if (typeOpen.id == 3 || typeOpen.id == 4) {
                DrawStroke.drawLine(winc, elemT.x1() + (elemT.x2() - elemT.x1()) / 2, elemT.y1(), elemB.x1(), elemB.y2());
                DrawStroke.drawLine(winc, elemT.x1() + (elemT.x2() - elemT.x1()) / 2, elemT.y1(), elemB.x2(), elemB.y2());
            }

            if (root.type() == Type.DOOR) {
                DY = 20;
                winc.gc2d.rotate(Math.toRadians(-90), X1 - DX, Y1 - DY);
                DrawStroke.strokePolygon(winc, X1 - DX, X1 + DX, X1 + DX, X1 - DX, Y1 - DY, Y1 - DY, Y1 + DY, Y1 + DY, 0xFFFFFFFF, Color.BLACK);
                DX = DX - 12;
                Y1 = Y1 + 20;
                DY = 60;
                DrawStroke.strokePolygon(winc, X1 - DX, X1 + DX, X1 + DX, X1 - DX, Y1 - DY, Y1 - DY, Y1 + DY, Y1 + DY, 0xFFFFFFFF, Color.BLACK);

            } else {
                int handlRGB = eColor.find(this.handleColor).getInt(eColor.rgb);
                DrawStroke.strokePolygon(winc, X1 - DX, X1 + DX, X1 + DX, X1 - DX, Y1 - DY, Y1 - DY, Y1 + DY, Y1 + DY, handlRGB, Color.BLACK);
                DX = DX - 12;
                Y1 = Y1 + 20;
            }
        }
    }

    //Фурнитура
    @Override
    public Record sysfurnRec() {
        return sysfurnRec;
    }

    //Ручка
    @Override
    public Record handleRec() {
        return this.handleRec;
    }

    //Ручка
    @Override
    public void handleRec(Record handleRec
    ) {
        this.handleRec = handleRec;
    }

    //Подвес(петли)
    @Override
    public Record loopRec() {
        return loopRec;
    }

    //Подвес(петли)
    @Override
    public void loopRec(Record loopRec) {
        this.loopRec = loopRec;
    }

    //Замок
    @Override
    public Record lockRec() {
        return lockRec;
    }

    //Замок
    @Override
    public void lockRec(Record lockRec
    ) {
        this.lockRec = lockRec;
    }

    //Цвет ручки
    @Override
    public int handleColor() {
        return handleColor;
    }

    //Цвет ручки
    @Override
    public void handleColor(int handleColor
    ) {
        this.handleColor = handleColor;
    }

    //Цвет подвеса
    @Override
    public int loopColor() {
        return loopColor;
    }

    //Цвет подвеса
    @Override
    public void loopColor(int loopColor
    ) {
        this.loopColor = loopColor;
    }

    //Цвет замка
    @Override
    public int lockColor() {
        return lockColor;
    }

    //Цвет замка
    @Override
    public void lockColor(int lockColor
    ) {
        this.lockColor = lockColor;
    }

    //Высота ручки
    @Override
    public float handleHeight() {
        return handleHeight;
    }

    //Высота ручки
    @Override
    public void handleHeight(float handleHeight
    ) {
        this.handleHeight = handleHeight;
    }

    //Направление открывания
    @Override
    public TypeOpen1 typeOpen() {
        return typeOpen;
    }

    //Направление открывания
    @Override
    public void typeOpen(TypeOpen1 typeOpen
    ) {
        this.typeOpen = typeOpen;
    }

    //Положение ручки на створке
    @Override
    public LayoutHandle handleLayout() {
        return handleLayout;
    }

    //Положение ручки на створке
    @Override
    public void handleLayout(LayoutHandle handleLayout
    ) {
        this.handleLayout = handleLayout;
    }

    //Москитная сетка
    @Override
    public Record mosqRec() {
        return mosqRec;
    }

    //Сосав москитки
    @Override
    public Record elementRec() {
        return elementRec;
    }

    @Override
    public boolean[] paramCheck() {
        return paramCheck;
    }

    @Override
    public void paramCheck(boolean[] paramCheck
    ) {
        this.paramCheck = paramCheck;
    }

    @Override
    public float[] offset() {
        return offset;
    }
}
