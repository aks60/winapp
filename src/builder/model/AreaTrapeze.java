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

    // см. IArea5e
    @Override
    public void addSpecific(Specific spcAdd, IElem5e elem5e) {
        if (elem5e.type() == Type.GLASS) {
            if (winc.form == Form.RIGHT) {

                if (elem5e.anglHoriz() == 0) {
                    ElemJoining ej = winc.listJoin.get(root().frames().get(Layout.RIGHT), 1);
                    spcAdd.width += elem5e.width() + 2 * elem5e.gzazo();
                    spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
                    spcAdd.anglCut1 = 45;
                    spcAdd.anglCut2 = 45;
                    spcAdd.anglHoriz = 0;

                } else if (elem5e.anglHoriz() == 90) {
                    IElem5e insideTop = root().frames().get(Layout.TOP), insideBott = elem5e.joinFlat(Layout.BOTT), insideRight = root().frames().get(Layout.RIGHT);
                    ElemJoining ej = winc.listJoin.get(insideRight, 1);
                    float dy1 = (insideTop.artiklRec().getFloat(eArtikl.height) - insideTop.artiklRec().getFloat(eArtikl.size_falz)) / UCom.sin(ej.angl);
                    float dy2 = (insideRight.artiklRec().getFloat(eArtikl.height) - insideRight.artiklRec().getFloat(eArtikl.size_falz)) * UCom.tan(90 - ej.angl);
                    float Y1 = insideRight.y1() + dy1 + dy2;
                    float Y2 = insideBott.y1() + insideBott.artiklRec().getFloat(eArtikl.size_falz);
                    spcAdd.width += Y2 - Y1;
                    spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
                    spcAdd.anglCut1 = 45;
                    spcAdd.anglCut2 = insideRight.anglCut()[1];
                    spcAdd.anglHoriz = insideRight.anglHoriz();

                } else if (elem5e.anglHoriz() == 180) {
                    IElem5e insideLeft = root().frames().get(Layout.LEFT), insideTop = root().frames().get(Layout.TOP), insideRight = root().frames().get(Layout.RIGHT);
                    ElemJoining ej = winc.listJoin.get(insideTop, 1);
                    float dx1 = insideLeft.x2() - insideLeft.artiklRec().getFloat(eArtikl.size_falz);
                    float dx2 = insideRight.x1() + insideRight.artiklRec().getFloat(eArtikl.size_falz);
                    spcAdd.width += (dx2 - dx1) / UCom.sin(ej.angl);
                    spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
                    spcAdd.anglCut1 = root().frames().get(Layout.TOP).anglCut()[0];
                    spcAdd.anglCut2 = root().frames().get(Layout.TOP).anglCut()[1];
                    spcAdd.anglHoriz = root().frames().get(Layout.LEFT).anglHoriz();

                } else if (elem5e.anglHoriz() == 270) {
                    IElem5e insideLeft = root().frames().get(Layout.LEFT), insideTop = root().frames().get(Layout.TOP), insideBott = elem5e.joinFlat(Layout.BOTT);
                    ElemJoining ej = winc.listJoin.get(insideLeft, 0);
                    float dy1 = (insideTop.artiklRec().getFloat(eArtikl.height) - insideTop.artiklRec().getFloat(eArtikl.size_falz)) / UCom.sin(ej.angl);
                    float dy2 = (insideLeft.artiklRec().getFloat(eArtikl.height) - insideLeft.artiklRec().getFloat(eArtikl.size_falz)) * UCom.tan(90 - ej.angl);
                    float Y1 = insideLeft.y1() + dy1 + dy2;
                    float Y2 = insideBott.y1() + insideBott.artiklRec().getFloat(eArtikl.size_falz);
                    spcAdd.width += Y2 - Y1;
                    spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
                    spcAdd.anglCut1 = insideLeft.anglCut()[0];
                    spcAdd.anglCut2 = 45;
                    spcAdd.anglHoriz = insideLeft.anglHoriz();
                }
            } else if (winc.form == Form.LEFT) {

                if (elem5e.anglHoriz() == 0) {
                    ElemJoining ej = winc.listJoin.get(root().frames().get(Layout.RIGHT), 1);
                    spcAdd.width += elem5e.width() + 2 * elem5e.gzazo();
                    spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
                    spcAdd.anglCut1 = 45;
                    spcAdd.anglCut2 = 45;
                    spcAdd.anglHoriz = 0;

                } else if (elem5e.anglHoriz() == 90) {
                    IElem5e insideRirht = root().frames().get(Layout.LEFT), insideTop = root().frames().get(Layout.TOP), insideBott = elem5e.joinFlat(Layout.BOTT);
                    ElemJoining ej = winc.listJoin.get(insideRirht, 1);
                    float dy1 = (insideTop.artiklRec().getFloat(eArtikl.height) - insideTop.artiklRec().getFloat(eArtikl.size_falz)) / UCom.sin(ej.angl);
                    float dy2 = (insideRirht.artiklRec().getFloat(eArtikl.height) - insideRirht.artiklRec().getFloat(eArtikl.size_falz)) * UCom.tan(90 - ej.angl);
                    float Y1 = insideRirht.y1() + dy1 - dy2;
                    float Y2 = insideBott.y1() + insideBott.artiklRec().getFloat(eArtikl.size_falz);
                    spcAdd.width += Y2 - Y1;
                    spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
                    spcAdd.anglCut2 = insideRirht.anglCut()[0];
                    spcAdd.anglCut1 = 45;
                    spcAdd.anglHoriz = insideRirht.anglHoriz();

                } else if (elem5e.anglHoriz() == 180) {
                    IElem5e insideLeft = root().frames().get(Layout.LEFT), insideTop = root().frames().get(Layout.TOP), insideRight = root().frames().get(Layout.RIGHT);
                    ElemJoining ej = winc.listJoin.get(insideTop, 1);
                    float dx1 = insideLeft.x2() - insideLeft.artiklRec().getFloat(eArtikl.size_falz);
                    float dx2 = insideRight.x1() + insideRight.artiklRec().getFloat(eArtikl.size_falz);
                    spcAdd.width += (dx2 - dx1) / UCom.sin(ej.angl);
                    spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
                    spcAdd.anglCut1 = root().frames().get(Layout.TOP).anglCut()[0];
                    spcAdd.anglCut2 = root().frames().get(Layout.TOP).anglCut()[1];
                    spcAdd.anglHoriz = root().frames().get(Layout.LEFT).anglHoriz();

                } else if (elem5e.anglHoriz() == 270) {
                    IElem5e insideTop = root().frames().get(Layout.TOP), insideBott = elem5e.joinFlat(Layout.BOTT), insideLeft = root().frames().get(Layout.RIGHT);
                    ElemJoining ej = winc.listJoin.get(insideLeft, 0);
                    float dy1 = (insideTop.artiklRec().getFloat(eArtikl.height) - insideTop.artiklRec().getFloat(eArtikl.size_falz)) / UCom.cos(90 - ej.angl);
                    float dy2 = (insideLeft.artiklRec().getFloat(eArtikl.height) - insideLeft.artiklRec().getFloat(eArtikl.size_falz)) * UCom.tan(90 - ej.angl);
                    float Y1 = insideLeft.y1() + dy1 + dy2;
                    float Y2 = insideBott.y1() + insideBott.artiklRec().getFloat(eArtikl.size_falz);
                    spcAdd.width += Y2 - Y1;
                    spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
                    spcAdd.anglCut2 = 45;
                    spcAdd.anglCut1 = insideLeft.anglCut()[1];
                    spcAdd.anglHoriz = insideLeft.anglHoriz();
                }
            }
            elem5e.spcRec().spcList.add(spcAdd); //добавим спецификацию
        }
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
            float angl = (float) Math.toDegrees(Math.atan(Math.abs(winc.height1() - winc.height2()) / width()));
            winc.listJoin.add(ElemJoining.create(winc, TypeJoin.VAR20, LayoutJoin.RBOT, elemBott, elemRight, 90)); //угловое соединение правое нижнее 
            winc.listJoin.add(ElemJoining.create(winc, TypeJoin.VAR20, LayoutJoin.RTOP, elemRight, elemTop, 90 + angl)); //угловое соединение правое верхнее
            winc.listJoin.add(ElemJoining.create(winc, TypeJoin.VAR20, LayoutJoin.LTOP, elemTop, elemLeft, 90 - angl));    //угловое соединение левое верхнее 
            winc.listJoin.add(ElemJoining.create(winc, TypeJoin.VAR20, LayoutJoin.LBOT, elemLeft, elemBott, 90)); //угловое соединение левое нижнее

        } else if (winc.form == Form.LEFT) {
            float angl = (float) Math.toDegrees(Math.atan(Math.abs(winc.height1() - winc.height2()) / width()));
            winc.listJoin.add(ElemJoining.create(winc, TypeJoin.VAR20, LayoutJoin.RBOT, elemBott, elemRight, 90)); //угловое соединение правое нижнее
            winc.listJoin.add(ElemJoining.create(winc, TypeJoin.VAR20, LayoutJoin.RTOP, elemRight, elemTop, 90 - angl)); //угловое соединение правое верхнее
            winc.listJoin.add(ElemJoining.create(winc, TypeJoin.VAR20, LayoutJoin.LTOP, elemTop, elemLeft, 90 + angl)); //угловое соединение левое верхнее  
            winc.listJoin.add(ElemJoining.create(winc, TypeJoin.VAR20, LayoutJoin.LBOT, elemLeft, elemBott, 90)); //угловое соединение левое нижнее 

        } else if (winc.form == Form.SYMM) {

        }
    }
}
