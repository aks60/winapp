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

        IElem5e elemBott = frames.get(Layout.BOTT), elemRight = frames.get(Layout.RIGHT),
                elemTopR = frames.get(Layout.TOPR), elemTop = frames.get(Layout.TOP), 
                elemTopL = frames.get(Layout.TOPL), elemLeft = frames.get(Layout.LEFT);

        if (winc.form == Form.SYMM) {
            winc.listJoin.add(new ElemJoining(winc, TypeJoin.VAR20, LayoutJoin.ANGL, elemBott, elemRight)); //угловое соединение правое нижнее
            winc.listJoin.add(new ElemJoining(winc, TypeJoin.VAR20, LayoutJoin.ANGL, elemLeft, elemBott)); //угловое соединение левое нижнее
            if (frames.get(Layout.TOPR) != null) {
                winc.listJoin.add(new ElemJoining(winc, TypeJoin.VAR20, LayoutJoin.ANGL, elemRight, elemTopR)); //угловое соединение правое среднее
                winc.listJoin.add(new ElemJoining(winc, TypeJoin.VAR20, LayoutJoin.ANGL, elemTopR, elemTop)); //угловое соединение правое верхнее
            }
            if (frames.get(Layout.TOPL) != null) {
                winc.listJoin.add(new ElemJoining(winc, TypeJoin.VAR20, LayoutJoin.ANGL, elemTopL, elemLeft)); //угловое соединение левое среднее
                winc.listJoin.add(new ElemJoining(winc, TypeJoin.VAR20, LayoutJoin.ANGL, elemTop, elemTopL)); //угловое соединение левое верхнее
            }           
        } else {
            winc.listJoin.add(new ElemJoining(winc, TypeJoin.VAR20, LayoutJoin.ANGL, elemBott, elemRight)); //угловое соединение правое нижнее 
            winc.listJoin.add(new ElemJoining(winc, TypeJoin.VAR20, LayoutJoin.ANGL, elemRight, elemTop)); //угловое соединение правое верхнее
            winc.listJoin.add(new ElemJoining(winc, TypeJoin.VAR20, LayoutJoin.ANGL, elemTop, elemLeft));    //угловое соединение левое верхнее
            winc.listJoin.add(new ElemJoining(winc, TypeJoin.VAR20, LayoutJoin.ANGL, elemLeft, elemBott)); //угловое соединение левое нижнее 
        }
    }
}
