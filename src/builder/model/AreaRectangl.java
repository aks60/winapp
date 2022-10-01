package builder.model;

import builder.IElem5e;
import enums.Layout;
import enums.LayoutJoin;
import enums.TypeJoin;
import builder.Wincalc;
import builder.script.GsonRoot;
import domain.eArtikl;
import domain.eSyssize;
import enums.Type;

public class AreaRectangl extends AreaSimple {

    public AreaRectangl(Wincalc winc) {
        super(winc);
        setDimension(0, 0, winc.rootGson.width(), winc.rootGson.height());
    }

    //@Override
    //Угловые соединения
    public void joining() {

        super.joining(); //T - соединения

        IElem5e elemBott = frames.get(Layout.BOTT), elemRight = frames.get(Layout.RIGHT),
                elemTop = frames.get(Layout.TOP), elemLeft = frames.get(Layout.LEFT);

        //Угловое соединение правое нижнее
        winc.mapJoin.put(elemBott.joinPoint(1), ElemJoining.create(winc, TypeJoin.VAR20, LayoutJoin.RBOT, elemBott, elemRight, 90));
        //Угловое соединение правое верхнее
        winc.mapJoin.put(elemRight.joinPoint(1), ElemJoining.create(winc, TypeJoin.VAR20, LayoutJoin.RTOP, elemRight, elemTop, 90));
        //Угловое соединение левое верхнее    
        winc.mapJoin.put(elemTop.joinPoint(1), ElemJoining.create(winc, TypeJoin.VAR20, LayoutJoin.LTOP, elemTop, elemLeft, 90));
        //Угловое соединение левое нижнее
        winc.mapJoin.put(elemLeft.joinPoint(1), ElemJoining.create(winc, TypeJoin.VAR20, LayoutJoin.LBOT, elemLeft, elemBott, 90));
    }
}
