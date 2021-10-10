package builder.model;

import enums.Layout;
import enums.LayoutJoin;
import enums.TypeJoin;
import builder.Wincalc;
import builder.script.GsonRoot;
import domain.eArtikl;
import domain.eSyssize;
import enums.Type;

public class AreaDoor extends AreaSimple {

    public AreaDoor(Wincalc iwin, GsonRoot gson, int color1, int color2, int color3) {
        super(iwin, null, gson.id(), Type.DOOR, gson.layout(), gson.width(), gson.height(), color1, color2, color3, gson.param());
        setDimension(0, 0, gson.width(), gson.height());
    }
    
    //@Override
    public void joinFrame() {
        ElemSimple elemBott = mapFrame.get(Layout.BOTT), elemRight = mapFrame.get(Layout.RIGHT),
                elemTop = mapFrame.get(Layout.TOP), elemLeft = mapFrame.get(Layout.LEFT);
        //Угловое соединение правое нижнее
        ElemJoining joinBot = new ElemJoining(iwin, TypeJoin.VAR20, LayoutJoin.RBOT, elemBott, elemRight, 90);
        iwin.mapJoin.put(elemBott.joinPoint(1), joinBot);

        //Угловое соединение правое верхнее
        ElemJoining joinRight = new ElemJoining(iwin, TypeJoin.VAR20, LayoutJoin.RTOP, elemRight, elemTop, 90);
        iwin.mapJoin.put(elemRight.joinPoint(1), joinRight);

        //Угловое соединение левое верхнее    
        ElemJoining joinTop = new ElemJoining(iwin, TypeJoin.VAR20, LayoutJoin.LTOP, elemTop, elemLeft, 90);
        iwin.mapJoin.put(elemTop.joinPoint(1), joinTop);

        //Угловое соединение левое нижнее
        ElemJoining joinLeft = new ElemJoining(iwin, TypeJoin.VAR20, LayoutJoin.LBOT, elemLeft, elemRight, 90);
        iwin.mapJoin.put(elemLeft.joinPoint(1), joinBot);
    }
}