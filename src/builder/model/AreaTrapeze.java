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

    public AreaTrapeze(Wincalc iwin, GsonRoot gson, int color1, int color2, int color3) {
        super(iwin, null, gson.id(), Type.TRAPEZE, gson.layout(), gson.width(), gson.height(), color1, color2, color3, gson.param());
        setDimension(0, 0, gson.width(), gson.height());
    }

    protected void addSpecificShtapik(ElemGlass glass, Specific spcAdd) {
        if (iwin().form == Form.NUM2) {

            if (glass.anglHoriz == glass.sideHoriz[0]) {
                ElemJoining ej = iwin().mapJoin.get(rootArea().mapFrame.get(Layout.RIGHT).joinPoint(1));
                spcAdd.width += width() + 2 * glass.gzazo;
                spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
                spcAdd.place = spcAdd.place + ".Н";
                spcAdd.anglCut1 = 45;
                spcAdd.anglCut2 = 45;
                spcAdd.anglHoriz = 0;

            } else if (glass.anglHoriz == glass.sideHoriz[1]) {
                ElemSimple insideTop = rootArea().mapFrame.get(Layout.TOP), insideBott = glass.joinFlat(Layout.BOTT), insideRight = rootArea().mapFrame.get(Layout.RIGHT);
                ElemJoining ej = iwin().mapJoin.get(rootArea().mapFrame.get(Layout.RIGHT).joinPoint(1));
                float dy1 = (insideTop.artiklRec.getFloat(eArtikl.height) - insideTop.artiklRec.getFloat(eArtikl.size_falz)) / UCom.sin(ej.angl);
                float dy2 = (insideRight.artiklRec.getFloat(eArtikl.height) - insideRight.artiklRec.getFloat(eArtikl.size_falz)) * UCom.tan(90 - ej.angl);
                float Y1 = insideRight.y1 + dy1 + dy2;
                float Y2 = insideBott.y1 + insideBott.artiklRec.getFloat(eArtikl.size_falz);
                spcAdd.width += Y2 - Y1;
                spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
                spcAdd.anglCut1 = 45;
                spcAdd.anglCut2 = insideRight.anglCut[1];
                spcAdd.anglHoriz = insideRight.anglHoriz;
                spcAdd.place = spcAdd.place + ".П";

            } else if (glass.anglHoriz == glass.sideHoriz[2]) {
                ElemSimple insideLeft = rootArea().mapFrame.get(Layout.LEFT), insideRight = rootArea().mapFrame.get(Layout.RIGHT);
                ElemJoining ej = iwin().mapJoin.get(rootArea().mapFrame.get(Layout.TOP).joinPoint(1));
                float dx1 = insideLeft.x2 - insideLeft.artiklRec.getFloat(eArtikl.size_falz);
                float dx2 = insideRight.x1 + insideRight.artiklRec.getFloat(eArtikl.size_falz);
                spcAdd.width += (dx2 - dx1) / UCom.sin(ej.angl);
                spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
                spcAdd.anglCut1 = rootArea().mapFrame.get(Layout.TOP).anglCut[0];
                spcAdd.anglCut2 = rootArea().mapFrame.get(Layout.TOP).anglCut[1];
                spcAdd.anglHoriz = rootArea().mapFrame.get(Layout.LEFT).anglHoriz;
                spcAdd.place = spcAdd.place + ".В";

            } else if (glass.anglHoriz == glass.sideHoriz[3]) {
                ElemSimple insideLeft = rootArea().mapFrame.get(Layout.LEFT), insideTop = rootArea().mapFrame.get(Layout.TOP), insideBott = glass.joinFlat(Layout.BOTT);
                ElemJoining ej = iwin().mapJoin.get(rootArea().mapFrame.get(Layout.LEFT).joinPoint(0));
                float dy1 = (insideTop.artiklRec.getFloat(eArtikl.height) - insideTop.artiklRec.getFloat(eArtikl.size_falz)) / UCom.sin(ej.angl);
                float dy2 = (insideLeft.artiklRec.getFloat(eArtikl.height) - insideLeft.artiklRec.getFloat(eArtikl.size_falz)) * UCom.tan(90 - ej.angl);
                float Y1 = insideLeft.y1 + dy1 + dy2;
                float Y2 = insideBott.y1 + insideBott.artiklRec.getFloat(eArtikl.size_falz);
                spcAdd.width += Y2 - Y1;
                spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
                spcAdd.anglCut1 = insideLeft.anglCut[0];
                spcAdd.anglCut2 = 45;
                spcAdd.anglHoriz = insideLeft.anglHoriz;
                spcAdd.place = spcAdd.place + ".Л";
            }
        } else if (iwin().form == Form.NUM4) {

            if (glass.anglHoriz == glass.sideHoriz[0]) {
                ElemJoining ej = iwin().mapJoin.get(rootArea().mapFrame.get(Layout.RIGHT).joinPoint(1));
                spcAdd.width += width() + 2 * glass.gzazo;
                spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
                spcAdd.place = spcAdd.place + ".Н";
                spcAdd.anglCut1 = 45;
                spcAdd.anglCut2 = 45;
                spcAdd.anglHoriz = 0;

            } else if (glass.anglHoriz == glass.sideHoriz[1]) {
                ElemSimple insideRirht = rootArea().mapFrame.get(Layout.LEFT), insideTop = rootArea().mapFrame.get(Layout.TOP), insideBott = glass.joinFlat(Layout.BOTT);
                ElemJoining ej = iwin().mapJoin.get(rootArea().mapFrame.get(Layout.RIGHT).joinPoint(1));
                float dy1 = (insideTop.artiklRec.getFloat(eArtikl.height) - insideTop.artiklRec.getFloat(eArtikl.size_falz)) / UCom.sin(ej.angl);
                float dy2 = (insideRirht.artiklRec.getFloat(eArtikl.height) - insideRirht.artiklRec.getFloat(eArtikl.size_falz)) * UCom.tan(90 - ej.angl);
                float Y1 = insideRirht.y1 + dy1 - dy2;
                float Y2 = insideBott.y1 + insideBott.artiklRec.getFloat(eArtikl.size_falz);
                spcAdd.width += Y2 - Y1;
                spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
                spcAdd.anglCut2 = insideRirht.anglCut[0];
                spcAdd.anglCut1 = 45;
                spcAdd.anglHoriz = insideRirht.anglHoriz;
                spcAdd.place = spcAdd.place + ".П*";

            } else if (glass.anglHoriz == glass.sideHoriz[2]) {
                ElemSimple insideLeft = rootArea().mapFrame.get(Layout.LEFT), insideRight = rootArea().mapFrame.get(Layout.RIGHT);
                ElemJoining ej = iwin().mapJoin.get(rootArea().mapFrame.get(Layout.TOP).joinPoint(1));
                float dx1 = insideLeft.x2 - insideLeft.artiklRec.getFloat(eArtikl.size_falz);
                float dx2 = insideRight.x1 + insideRight.artiklRec.getFloat(eArtikl.size_falz);
                spcAdd.width += (dx2 - dx1) / UCom.sin(ej.angl);
                spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
                spcAdd.anglCut1 = rootArea().mapFrame.get(Layout.TOP).anglCut[0];
                spcAdd.anglCut2 = rootArea().mapFrame.get(Layout.TOP).anglCut[1];
                spcAdd.anglHoriz = rootArea().mapFrame.get(Layout.LEFT).anglHoriz;
                spcAdd.place = spcAdd.place + ".В";

            } else if (glass.anglHoriz == glass.sideHoriz[3]) {
                ElemSimple insideTop = rootArea().mapFrame.get(Layout.TOP), insideBott = glass.joinFlat(Layout.BOTT), insideLeft = rootArea().mapFrame.get(Layout.RIGHT);
                ElemJoining ej = iwin().mapJoin.get(rootArea().mapFrame.get(Layout.LEFT).joinPoint(0));
                float dy1 = (insideTop.artiklRec.getFloat(eArtikl.height) - insideTop.artiklRec.getFloat(eArtikl.size_falz)) / UCom.cos(90 - ej.angl);
                float dy2 = (insideLeft.artiklRec.getFloat(eArtikl.height) - insideLeft.artiklRec.getFloat(eArtikl.size_falz)) * UCom.tan(90 - ej.angl);
                float Y1 = insideLeft.y1 + dy1 + dy2;
                float Y2 = insideBott.y1 + insideBott.artiklRec.getFloat(eArtikl.size_falz);
                spcAdd.width += Y2 - Y1;
                spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
                spcAdd.anglCut2 = 45;
                spcAdd.anglCut1 = insideLeft.anglCut[1];
                spcAdd.anglHoriz = insideLeft.anglHoriz;
                spcAdd.place = spcAdd.place + ".Л*";
            }
        }
        glass.spcRec.spcList.add(spcAdd); //добавим спецификацию
    }

    protected void addSpecificPadding(ElemGlass glass, Specific spcAdd) {
        if (iwin().form == Form.NUM2) {

            if (glass.anglHoriz == glass.sideHoriz[0]) {
                ElemJoining ej = iwin().mapJoin.get(rootArea().mapFrame.get(Layout.RIGHT).joinPoint(1));
                spcAdd.width += width() + 2 * glass.gzazo;
                spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
                spcAdd.place = spcAdd.place + ".Н";
                spcAdd.anglCut1 = 45;
                spcAdd.anglCut2 = 45;
                spcAdd.anglHoriz = 0;

            } else if (glass.anglHoriz == glass.sideHoriz[1]) {
                ElemSimple insideTop = rootArea().mapFrame.get(Layout.TOP), insideBott = glass.joinFlat(Layout.BOTT), insideRight = rootArea().mapFrame.get(Layout.RIGHT);
                ElemJoining ej = iwin().mapJoin.get(rootArea().mapFrame.get(Layout.RIGHT).joinPoint(1));
                float dy1 = (insideTop.artiklRec.getFloat(eArtikl.height) - insideTop.artiklRec.getFloat(eArtikl.size_falz)) / UCom.sin(ej.angl);
                float dy2 = (insideRight.artiklRec.getFloat(eArtikl.height) - insideRight.artiklRec.getFloat(eArtikl.size_falz)) * UCom.tan(90 - ej.angl);
                float Y1 = insideRight.y1 + dy1 + dy2;
                float Y2 = insideBott.y1 + insideBott.artiklRec.getFloat(eArtikl.size_falz);
                spcAdd.width += Y2 - Y1;
                spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
                spcAdd.anglCut1 = 45;
                spcAdd.anglCut2 = insideRight.anglCut[1];
                spcAdd.anglHoriz = insideRight.anglHoriz;
                spcAdd.place = spcAdd.place + ".П";

            } else if (glass.anglHoriz == glass.sideHoriz[2]) {
                ElemSimple insideLeft = rootArea().mapFrame.get(Layout.LEFT), insideRight = rootArea().mapFrame.get(Layout.RIGHT);
                ElemJoining ej = iwin().mapJoin.get(rootArea().mapFrame.get(Layout.TOP).joinPoint(1));
                float dx1 = insideLeft.x2 - insideLeft.artiklRec.getFloat(eArtikl.size_falz);
                float dx2 = insideRight.x1 + insideRight.artiklRec.getFloat(eArtikl.size_falz);
                spcAdd.width += (dx2 - dx1) / UCom.sin(ej.angl);
                spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
                spcAdd.anglCut1 = rootArea().mapFrame.get(Layout.TOP).anglCut[0];
                spcAdd.anglCut2 = rootArea().mapFrame.get(Layout.TOP).anglCut[1];
                spcAdd.anglHoriz = rootArea().mapFrame.get(Layout.LEFT).anglHoriz;
                spcAdd.place = spcAdd.place + ".В";

            } else if (glass.anglHoriz == glass.sideHoriz[3]) {
                ElemSimple insideLeft = rootArea().mapFrame.get(Layout.LEFT), insideTop = rootArea().mapFrame.get(Layout.TOP), insideBott = glass.joinFlat(Layout.BOTT);
                ElemJoining ej = iwin().mapJoin.get(rootArea().mapFrame.get(Layout.LEFT).joinPoint(0));
                float dy1 = (insideTop.artiklRec.getFloat(eArtikl.height) - insideTop.artiklRec.getFloat(eArtikl.size_falz)) / UCom.sin(ej.angl);
                float dy2 = (insideLeft.artiklRec.getFloat(eArtikl.height) - insideLeft.artiklRec.getFloat(eArtikl.size_falz)) * UCom.tan(90 - ej.angl);
                float Y1 = insideLeft.y1 + dy1 + dy2;
                float Y2 = insideBott.y1 + insideBott.artiklRec.getFloat(eArtikl.size_falz);
                spcAdd.width += Y2 - Y1;
                spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
                spcAdd.anglCut1 = insideLeft.anglCut[0];
                spcAdd.anglCut2 = 45;
                spcAdd.anglHoriz = insideLeft.anglHoriz;
                spcAdd.place = spcAdd.place + ".Л";
            }
        } else if (iwin().form == Form.NUM4) {

            if (glass.anglHoriz == glass.sideHoriz[0]) {
                ElemJoining ej = iwin().mapJoin.get(rootArea().mapFrame.get(Layout.RIGHT).joinPoint(1));
                spcAdd.width += width() + 2 * glass.gzazo;
                spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
                spcAdd.place = spcAdd.place + ".Н";
                spcAdd.anglCut1 = 45;
                spcAdd.anglCut2 = 45;
                spcAdd.anglHoriz = 0;

            } else if (glass.anglHoriz == glass.sideHoriz[1]) {
                ElemSimple insideRirht = rootArea().mapFrame.get(Layout.LEFT), insideTop = rootArea().mapFrame.get(Layout.TOP), insideBott = glass.joinFlat(Layout.BOTT);
                ElemJoining ej = iwin().mapJoin.get(rootArea().mapFrame.get(Layout.RIGHT).joinPoint(1));
                float dy1 = (insideTop.artiklRec.getFloat(eArtikl.height) - insideTop.artiklRec.getFloat(eArtikl.size_falz)) / UCom.sin(ej.angl);
                float dy2 = (insideRirht.artiklRec.getFloat(eArtikl.height) - insideRirht.artiklRec.getFloat(eArtikl.size_falz)) * UCom.tan(90 - ej.angl);
                float Y1 = insideRirht.y1 + dy1 - dy2;
                float Y2 = insideBott.y1 + insideBott.artiklRec.getFloat(eArtikl.size_falz);
                spcAdd.width += Y2 - Y1;
                spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
                spcAdd.anglCut2 = insideRirht.anglCut[0];
                spcAdd.anglCut1 = 45;
                spcAdd.anglHoriz = insideRirht.anglHoriz;
                spcAdd.place = spcAdd.place + ".П*";

            } else if (glass.anglHoriz == glass.sideHoriz[2]) {
                ElemSimple insideLeft = rootArea().mapFrame.get(Layout.LEFT), insideRight = rootArea().mapFrame.get(Layout.RIGHT);
                ElemJoining ej = iwin().mapJoin.get(rootArea().mapFrame.get(Layout.TOP).joinPoint(1));
                float dx1 = insideLeft.x2 - insideLeft.artiklRec.getFloat(eArtikl.size_falz);
                float dx2 = insideRight.x1 + insideRight.artiklRec.getFloat(eArtikl.size_falz);
                spcAdd.width += (dx2 - dx1) / UCom.sin(ej.angl);
                spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
                spcAdd.anglCut1 = rootArea().mapFrame.get(Layout.TOP).anglCut[0];
                spcAdd.anglCut2 = rootArea().mapFrame.get(Layout.TOP).anglCut[1];
                spcAdd.anglHoriz = rootArea().mapFrame.get(Layout.LEFT).anglHoriz;
                spcAdd.place = spcAdd.place + ".В";

            } else if (glass.anglHoriz == glass.sideHoriz[3]) {
                ElemSimple insideTop = rootArea().mapFrame.get(Layout.TOP), insideBott = glass.joinFlat(Layout.BOTT), insideLeft = rootArea().mapFrame.get(Layout.RIGHT);
                ElemJoining ej = iwin().mapJoin.get(rootArea().mapFrame.get(Layout.LEFT).joinPoint(0));
                float dy1 = (insideTop.artiklRec.getFloat(eArtikl.height) - insideTop.artiklRec.getFloat(eArtikl.size_falz)) / UCom.cos(90 - ej.angl);
                float dy2 = (insideLeft.artiklRec.getFloat(eArtikl.height) - insideLeft.artiklRec.getFloat(eArtikl.size_falz)) * UCom.tan(90 - ej.angl);
                float Y1 = insideLeft.y1 + dy1 + dy2;
                float Y2 = insideBott.y1 + insideBott.artiklRec.getFloat(eArtikl.size_falz);
                spcAdd.width += Y2 - Y1;
                spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
                spcAdd.anglCut2 = 45;
                spcAdd.anglCut1 = insideLeft.anglCut[1];
                spcAdd.anglHoriz = insideLeft.anglHoriz;
                spcAdd.place = spcAdd.place + ".Л*";
            }
        }
        glass.spcRec.spcList.add(spcAdd); //добавим спецификацию

    }

    @Override
    public void joinFrame() {
        ElemSimple elemBott = mapFrame.get(Layout.BOTT), elemRight = mapFrame.get(Layout.RIGHT), elemTop = mapFrame.get(Layout.TOP), elemLeft = mapFrame.get(Layout.LEFT);

        ElemJoining joinLeft = null, joinBott = null, joinRight = null, joinTop = null;

        if (iwin().form == Form.NUM2) {
            float angl = (float) Math.toDegrees(Math.atan((height() - iwin().heightAdd) / width()));
            joinLeft = new ElemJoining(iwin(), TypeJoin.VAR20, LayoutJoin.LBOT, elemLeft, elemRight, 90); //угловое соединение левое нижнее
            joinBott = new ElemJoining(iwin(), TypeJoin.VAR20, LayoutJoin.RBOT, elemBott, elemRight, 90); //угловое соединение правое нижнее
            joinRight = new ElemJoining(iwin(), TypeJoin.VAR20, LayoutJoin.RTOP, elemRight, elemTop, 90 + angl); //угловое соединение правое верхнее
            joinTop = new ElemJoining(iwin(), TypeJoin.VAR20, LayoutJoin.LTOP, elemTop, elemLeft, 90 - angl);    //угловое соединение левое верхнее    

        } else if (iwin().form == Form.NUM4) {
            float angl = (float) Math.toDegrees(Math.atan((height() - iwin().heightAdd) / width()));
            joinLeft = new ElemJoining(iwin(), TypeJoin.VAR20, LayoutJoin.LBOT, elemLeft, elemRight, 90); //угловое соединение левое нижнее  
            joinBott = new ElemJoining(iwin(), TypeJoin.VAR20, LayoutJoin.RBOT, elemBott, elemRight, 90); //угловое соединение правое нижнее
            joinRight = new ElemJoining(iwin(), TypeJoin.VAR20, LayoutJoin.RTOP, elemRight, elemTop, 90 - angl); //угловое соединение правое верхнее
            joinTop = new ElemJoining(iwin(), TypeJoin.VAR20, LayoutJoin.LTOP, elemTop, elemLeft, 90 + angl); //угловое соединение левое верхнее             
        } else {
            return;
        }
        iwin().mapJoin.put(elemLeft.joinPoint(1), joinLeft);
        iwin().mapJoin.put(elemBott.joinPoint(1), joinBott);
        iwin().mapJoin.put(elemRight.joinPoint(1), joinRight);
        iwin().mapJoin.put(elemTop.joinPoint(1), joinTop);

    }
}
