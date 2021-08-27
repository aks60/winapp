package builder.model;

import enums.Layout;
import enums.LayoutJoin;
import enums.TypeJoin;
import builder.Wincalc;
import builder.script.GsonRoot;
import domain.eArtikl;
import domain.eSyssize;
import enums.Type;

public class AreaRectangl extends AreaSimple {

    public AreaRectangl(Wincalc iwin, GsonRoot gson, int color1, int color2, int color3) {
        super(iwin, null, gson.id(), Type.RECTANGL, gson.layout(), gson.width(), gson.height(), color1, color2, color3, gson.param());
        setDimension(0, 0, gson.width(), gson.height());
    }

/**
//    @Override
//    public void setLocation(ElemFrame frm) {
//        AreaSimple owner = frm.owner();
//        if (Layout.BOTT == frm.layout) {
//            frm.setDimension(owner.x1, owner.y2 - frm.artiklRec.getFloat(eArtikl.height), owner.x2, owner.y2);
//            frm.anglHoriz = 0;
//        } else if (Layout.RIGHT == frm.layout) {
//            frm.setDimension(owner.x2 - frm.artiklRec.getFloat(eArtikl.height), owner.y1, owner.x2, owner.y2);
//            frm.anglHoriz = 90;
//        } else if (Layout.TOP == frm.layout) {
//            frm.setDimension(owner.x1, owner.y1, owner.x2, owner.y1 + frm.artiklRec.getFloat(eArtikl.height));
//            frm.anglHoriz = 180;
//        } else if (Layout.LEFT == frm.layout) {
//            frm.setDimension(owner.x1, owner.y1, owner.x1 + frm.artiklRec.getFloat(eArtikl.height), owner.y2);
//            frm.anglHoriz = 270;
//        }
//    }
    
//    @Override
//    public void setSpecific(ElemFrame frm) {
//        double katet = iwin().syssizeRec.getDbl(eSyssize.prip) * Math.cos(Math.PI / 4);
//
//        if (Layout.TOP == frm.layout) {
//            frm.spcRec.width = frm.x2 - frm.x1 + (float) (katet / Math.sin(Math.toRadians(frm.anglCut[0])) + katet / Math.sin(Math.toRadians(frm.anglCut[1])));
//            frm.spcRec.height = frm.artiklRec.getFloat(eArtikl.height);
//
//        } else if (Layout.BOTT == frm.layout) {
//            frm.spcRec.width = frm.x2 - frm.x1 + +(float) (katet / Math.sin(Math.toRadians(frm.anglCut[0])) + katet / Math.sin(Math.toRadians(frm.anglCut[1])));
//            frm.spcRec.height = frm.artiklRec.getFloat(eArtikl.height);
//
//        } else if (Layout.LEFT == frm.layout) {
//            frm.spcRec.width = frm.y2 - frm.y1 + (float) (katet / Math.sin(Math.toRadians(frm.anglCut[0])) + katet / Math.sin(Math.toRadians(frm.anglCut[1])));
//            frm.spcRec.height = frm.artiklRec.getFloat(eArtikl.height);
//
//        } else if (Layout.RIGHT == frm.layout) {
//            frm.spcRec.width = frm.y2 - frm.y1 + (float) (katet / Math.sin(Math.toRadians(frm.anglCut[0])) + katet / Math.sin(Math.toRadians(frm.anglCut[1])));
//            frm.spcRec.height = frm.artiklRec.getFloat(eArtikl.height);
//        }
//}
*/    
    
    //@Override
    public void joinFrame() {
        ElemSimple elemBott = mapFrame.get(Layout.BOTT), elemRight = mapFrame.get(Layout.RIGHT),
                elemTop = mapFrame.get(Layout.TOP), elemLeft = mapFrame.get(Layout.LEFT);
        //Угловое соединение правое нижнее
        ElemJoining joinBot = new ElemJoining(iwin(), TypeJoin.VAR20, LayoutJoin.RBOT, elemBott, elemRight, 90);
        iwin().mapJoin.put(elemBott.joinPoint(1), joinBot);

        //Угловое соединение правое верхнее
        ElemJoining joinRight = new ElemJoining(iwin(), TypeJoin.VAR20, LayoutJoin.RTOP, elemRight, elemTop, 90);
        iwin().mapJoin.put(elemRight.joinPoint(1), joinRight);

        //Угловое соединение левое верхнее    
        ElemJoining joinTop = new ElemJoining(iwin(), TypeJoin.VAR20, LayoutJoin.LTOP, elemTop, elemLeft, 90);
        iwin().mapJoin.put(elemTop.joinPoint(1), joinTop);

        //Угловое соединение левое нижнее
        ElemJoining joinLeft = new ElemJoining(iwin(), TypeJoin.VAR20, LayoutJoin.LBOT, elemLeft, elemRight, 90);
        iwin().mapJoin.put(elemLeft.joinPoint(1), joinBot);
    }
}
