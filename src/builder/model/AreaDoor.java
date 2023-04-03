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

public class AreaDoor extends AreaSimple {

    public AreaDoor(Wincalc winc) {
        super(winc);
        setDimension(0, 0, winc.rootGson.width(), winc.rootGson.height());
    }

    //Угловые соединения
    @Override
    public void joining() {

        super.joining(); //T - соединения

        IElem5e elemBott = frames.get(Layout.BOTT), elemRight = frames.get(Layout.RIGHT),
                elemTop = frames.get(Layout.TOP), elemLeft = frames.get(Layout.LEFT);
        //Угловое соединение правое нижнее
        winc.listJoin.add(ElemJoining.create(winc, TypeJoin.VAR20, LayoutJoin.RBOT, elemBott, elemRight));
        //Угловое соединение правое верхнее
        winc.listJoin.add(ElemJoining.create(winc, TypeJoin.VAR20, LayoutJoin.RTOP, elemRight, elemTop));
        //Угловое соединение левое верхнее    
        winc.listJoin.add(ElemJoining.create(winc, TypeJoin.VAR20, LayoutJoin.LTOP, elemTop, elemLeft));
        //Угловое соединение левое нижнее
        winc.listJoin.add(ElemJoining.create(winc, TypeJoin.VAR20, LayoutJoin.LBOT, elemLeft, elemBott));
    }
}
