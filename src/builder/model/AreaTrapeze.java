package builder.model;

import builder.IElem5e;
import enums.Layout;
import builder.Wincalc;
import builder.making.Specific;
import builder.script.GsonRoot;
import common.UCom;
import domain.eArtikl;
import domain.eSyssize;
import enums.Form;
import enums.LayoutJoin;
import enums.PKjson;
import enums.Type;
import enums.TypeJoin;

public class AreaTrapeze extends AreaSimple {

    public AreaTrapeze(Wincalc winc) {
        super(winc);
        setDimension(0, 0, winc.rootGson.width(), winc.rootGson.height());
    }

    //Угловые соединения    
    @Override
    public void joining() {

        super.joining();

        IElem5e elemBott = frames.get(Layout.BOTT), 
                elemRight = frames.get(Layout.RIGHT), 
                elemTop = frames.get(Layout.TOP), 
                elemLeft = frames.get(Layout.LEFT);

        if (winc.form == Form.RIGHT) {
            winc.listJoin.add(new ElemJoining(winc, TypeJoin.VAR20, LayoutJoin.RBOT, elemBott, elemRight)); //угловое соединение правое нижнее 
            winc.listJoin.add(new ElemJoining(winc, TypeJoin.VAR20, LayoutJoin.RTOP, elemRight, elemTop)); //угловое соединение правое верхнее
            winc.listJoin.add(new ElemJoining(winc, TypeJoin.VAR20, LayoutJoin.LTOP, elemTop, elemLeft));    //угловое соединение левое верхнее
            winc.listJoin.add(new ElemJoining(winc, TypeJoin.VAR20, LayoutJoin.LBOT, elemLeft, elemBott)); //угловое соединение левое нижнее 


        } else if (winc.form == Form.LEFT) {
            double angl = Math.toDegrees(Math.atan(Math.abs(winc.height1() - winc.height2()) / width()));
            winc.listJoin.add(new ElemJoining(winc, TypeJoin.VAR20, LayoutJoin.RBOT, elemBott, elemRight)); //угловое соединение правое нижнее
            winc.listJoin.add(new ElemJoining(winc, TypeJoin.VAR20, LayoutJoin.RTOP, elemRight, elemTop)); //угловое соединение правое верхнее
            winc.listJoin.add(new ElemJoining(winc, TypeJoin.VAR20, LayoutJoin.LTOP, elemTop, elemLeft)); //угловое соединение левое верхнее  
            winc.listJoin.add(new ElemJoining(winc, TypeJoin.VAR20, LayoutJoin.LBOT, elemLeft, elemBott)); //угловое соединение левое нижнее 

        } else if (winc.form == Form.SYMM) {

        }
    }
}