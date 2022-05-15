package builder.model;

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
        float height = (winc.rootGson.height() < winc.rootGson.height2()) ? winc.rootGson.height2() : winc.rootGson.height();
        setDimension(0, 0, winc.rootGson.width(), height);
    }

    @Override
    protected void addFilling(ElemGlass glass, Specific spcAdd) {
        if (winc.form == Form.RIGHT) {

            if (glass.anglHoriz == glass.sideHoriz[0]) {
                ElemJoining ej = winc.mapJoin.get(root().frames.get(Layout.RIGHT).joinPoint(1));
                spcAdd.width += glass.width() + 2 * glass.gzazo;
                spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
                spcAdd.anglCut1 = 45;
                spcAdd.anglCut2 = 45;
                spcAdd.anglHoriz = 0;

            } else if (glass.anglHoriz == glass.sideHoriz[1]) {
                ElemSimple insideTop = root().frames.get(Layout.TOP), insideBott = glass.joinFlat(Layout.BOTT), insideRight = root().frames.get(Layout.RIGHT);
                ElemJoining ej = winc.mapJoin.get(insideRight.joinPoint(1));
                float dy1 = (insideTop.artiklRec.getFloat(eArtikl.height) - insideTop.artiklRec.getFloat(eArtikl.size_falz)) / UCom.sin(ej.angl);
                float dy2 = (insideRight.artiklRec.getFloat(eArtikl.height) - insideRight.artiklRec.getFloat(eArtikl.size_falz)) * UCom.tan(90 - ej.angl);
                float Y1 = insideRight.y1 + dy1 + dy2;
                float Y2 = insideBott.y1 + insideBott.artiklRec.getFloat(eArtikl.size_falz);
                spcAdd.width += Y2 - Y1;
                spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
                spcAdd.anglCut1 = 45;
                spcAdd.anglCut2 = insideRight.anglCut[1];
                spcAdd.anglHoriz = insideRight.anglHoriz;

            } else if (glass.anglHoriz == glass.sideHoriz[2]) {
                ElemSimple insideLeft = root().frames.get(Layout.LEFT), insideTop = root().frames.get(Layout.TOP), insideRight = root().frames.get(Layout.RIGHT);
                ElemJoining ej = winc.mapJoin.get(insideTop.joinPoint(1));
                float dx1 = insideLeft.x2 - insideLeft.artiklRec.getFloat(eArtikl.size_falz);
                float dx2 = insideRight.x1 + insideRight.artiklRec.getFloat(eArtikl.size_falz);
                spcAdd.width += (dx2 - dx1) / UCom.sin(ej.angl);
                spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
                spcAdd.anglCut1 = root().frames.get(Layout.TOP).anglCut[0];
                spcAdd.anglCut2 = root().frames.get(Layout.TOP).anglCut[1];
                spcAdd.anglHoriz = root().frames.get(Layout.LEFT).anglHoriz;

            } else if (glass.anglHoriz == glass.sideHoriz[3]) {
                ElemSimple insideLeft = root().frames.get(Layout.LEFT), insideTop = root().frames.get(Layout.TOP), insideBott = glass.joinFlat(Layout.BOTT);
                ElemJoining ej = winc.mapJoin.get(insideLeft.joinPoint(0));
                float dy1 = (insideTop.artiklRec.getFloat(eArtikl.height) - insideTop.artiklRec.getFloat(eArtikl.size_falz)) / UCom.sin(ej.angl);
                float dy2 = (insideLeft.artiklRec.getFloat(eArtikl.height) - insideLeft.artiklRec.getFloat(eArtikl.size_falz)) * UCom.tan(90 - ej.angl);
                float Y1 = insideLeft.y1 + dy1 + dy2;
                float Y2 = insideBott.y1 + insideBott.artiklRec.getFloat(eArtikl.size_falz);
                spcAdd.width += Y2 - Y1;
                spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
                spcAdd.anglCut1 = insideLeft.anglCut[0];
                spcAdd.anglCut2 = 45;
                spcAdd.anglHoriz = insideLeft.anglHoriz;
            }
        } else if (winc.form == Form.LEFT) {

            if (glass.anglHoriz == glass.sideHoriz[0]) {
                ElemJoining ej = winc.mapJoin.get(root().frames.get(Layout.RIGHT).joinPoint(1));
                spcAdd.width += glass.width() + 2 * glass.gzazo;
                spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
                spcAdd.anglCut1 = 45;
                spcAdd.anglCut2 = 45;
                spcAdd.anglHoriz = 0;

            } else if (glass.anglHoriz == glass.sideHoriz[1]) {
                ElemSimple insideRirht = root().frames.get(Layout.LEFT), insideTop = root().frames.get(Layout.TOP), insideBott = glass.joinFlat(Layout.BOTT);
                ElemJoining ej = winc.mapJoin.get(insideRirht.joinPoint(1));
                float dy1 = (insideTop.artiklRec.getFloat(eArtikl.height) - insideTop.artiklRec.getFloat(eArtikl.size_falz)) / UCom.sin(ej.angl);
                float dy2 = (insideRirht.artiklRec.getFloat(eArtikl.height) - insideRirht.artiklRec.getFloat(eArtikl.size_falz)) * UCom.tan(90 - ej.angl);
                float Y1 = insideRirht.y1 + dy1 - dy2;
                float Y2 = insideBott.y1 + insideBott.artiklRec.getFloat(eArtikl.size_falz);
                spcAdd.width += Y2 - Y1;
                spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
                spcAdd.anglCut2 = insideRirht.anglCut[0];
                spcAdd.anglCut1 = 45;
                spcAdd.anglHoriz = insideRirht.anglHoriz;

            } else if (glass.anglHoriz == glass.sideHoriz[2]) {
                ElemSimple insideLeft = root().frames.get(Layout.LEFT), insideTop = root().frames.get(Layout.TOP), insideRight = root().frames.get(Layout.RIGHT);
                ElemJoining ej = winc.mapJoin.get(insideTop.joinPoint(1));
                float dx1 = insideLeft.x2 - insideLeft.artiklRec.getFloat(eArtikl.size_falz);
                float dx2 = insideRight.x1 + insideRight.artiklRec.getFloat(eArtikl.size_falz);
                spcAdd.width += (dx2 - dx1) / UCom.sin(ej.angl);
                spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
                spcAdd.anglCut1 = root().frames.get(Layout.TOP).anglCut[0];
                spcAdd.anglCut2 = root().frames.get(Layout.TOP).anglCut[1];
                spcAdd.anglHoriz = root().frames.get(Layout.LEFT).anglHoriz;

            } else if (glass.anglHoriz == glass.sideHoriz[3]) {
                ElemSimple insideTop = root().frames.get(Layout.TOP), insideBott = glass.joinFlat(Layout.BOTT), insideLeft = root().frames.get(Layout.RIGHT);
                ElemJoining ej = winc.mapJoin.get(insideLeft.joinPoint(0));
                float dy1 = (insideTop.artiklRec.getFloat(eArtikl.height) - insideTop.artiklRec.getFloat(eArtikl.size_falz)) / UCom.cos(90 - ej.angl);
                float dy2 = (insideLeft.artiklRec.getFloat(eArtikl.height) - insideLeft.artiklRec.getFloat(eArtikl.size_falz)) * UCom.tan(90 - ej.angl);
                float Y1 = insideLeft.y1 + dy1 + dy2;
                float Y2 = insideBott.y1 + insideBott.artiklRec.getFloat(eArtikl.size_falz);
                spcAdd.width += Y2 - Y1;
                spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
                spcAdd.anglCut2 = 45;
                spcAdd.anglCut1 = insideLeft.anglCut[1];
                spcAdd.anglHoriz = insideLeft.anglHoriz;
            }
        }
        glass.spcRec.spcList.add(spcAdd); //добавим спецификацию
    }

    @Override
    //Угловые соединения
    public void joining() {

        super.joining();

        ElemSimple elemBott = frames.get(Layout.BOTT), elemRight = frames.get(Layout.RIGHT), elemTop = frames.get(Layout.TOP), elemLeft = frames.get(Layout.LEFT);

        if (winc.form == Form.RIGHT) {
            float angl = (float) Math.toDegrees(Math.atan(Math.abs(winc.height1() - winc.height2()) / width()));
            ElemJoining.create(elemLeft.joinPoint(1), winc, TypeJoin.VAR20, LayoutJoin.LBOT, elemLeft, elemRight, 90); //угловое соединение левое нижнее
            ElemJoining.create(elemBott.joinPoint(1), winc, TypeJoin.VAR20, LayoutJoin.RBOT, elemBott, elemRight, 90); //угловое соединение правое нижнее 
            ElemJoining.create(elemRight.joinPoint(1), winc, TypeJoin.VAR20, LayoutJoin.RTOP, elemRight, elemTop, 90 + angl); //угловое соединение правое верхнее
            ElemJoining.create(elemTop.joinPoint(1), winc, TypeJoin.VAR20, LayoutJoin.LTOP, elemTop, elemLeft, 90 - angl);    //угловое соединение левое верхнее 

        } else if (winc.form == Form.LEFT) {
            float angl = (float) Math.toDegrees(Math.atan(Math.abs(winc.height1() - winc.height2()) / width()));
            ElemJoining.create(elemLeft.joinPoint(1), winc, TypeJoin.VAR20, LayoutJoin.LBOT, elemLeft, elemRight, 90); //угловое соединение левое нижнее 
            ElemJoining.create(elemBott.joinPoint(1), winc, TypeJoin.VAR20, LayoutJoin.RBOT, elemBott, elemRight, 90); //угловое соединение правое нижнее
            ElemJoining.create(elemRight.joinPoint(1), winc, TypeJoin.VAR20, LayoutJoin.RTOP, elemRight, elemTop, 90 - angl); //угловое соединение правое верхнее
            ElemJoining.create(elemTop.joinPoint(1), winc, TypeJoin.VAR20, LayoutJoin.LTOP, elemTop, elemLeft, 90 + angl); //угловое соединение левое верхнее  
        }
    }
}
