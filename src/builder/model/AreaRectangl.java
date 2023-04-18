package builder.model;

import builder.Wincalc;
import builder.IElem5e;
import enums.Layout;
import enums.LayoutJoin;
import enums.TypeJoin;
import builder.script.GsonRoot;
import domain.eArtikl;
import domain.eSyssize;
import enums.Type;

public class AreaRectangl extends AreaSimple {

    public AreaRectangl(Wincalc winc) {
        super(winc);
        setDimension(0, 0, winc.rootGson.width(), winc.rootGson.height());
    }

    //Угловые соединения
    @Override
    public void joining() {

        super.joining(); //T - соединения

        IElem5e elemBott = frames.get(Layout.BOTT), elemRight = frames.get(Layout.RIGHT),
                elemTop = frames.get(Layout.TOP), elemLeft = frames.get(Layout.LEFT);

        winc.listJoin.add(new ElemJoining(winc, TypeJoin.VAR20, LayoutJoin.ANGL, elemBott, elemRight)); //угловое соединение правое нижнее 
        winc.listJoin.add(new ElemJoining(winc, TypeJoin.VAR20, LayoutJoin.ANGL, elemRight, elemTop)); //угловое соединение правое верхнее
        winc.listJoin.add(new ElemJoining(winc, TypeJoin.VAR20, LayoutJoin.ANGL, elemTop, elemLeft));    //угловое соединение левое верхнее
        winc.listJoin.add(new ElemJoining(winc, TypeJoin.VAR20, LayoutJoin.ANGL, elemLeft, elemBott)); //угловое соединение левое нижнее 
    }
}
