package builder.model;

import domain.eArtikl;
import enums.LayoutJoin;
import enums.TypeJoin;
import enums.Layout;
import builder.Wincalc;
import builder.making.Specific;
import builder.script.GsonRoot;
import domain.eSyssize;
import enums.Type;

public class AreaArch extends AreaSimple {

    public double radiusArch = 0; //радиус арки

    public AreaArch(Wincalc iwin, GsonRoot gson, int color1, int color2, int color3) {
        super(iwin, null, gson.id(), Type.ARCH, gson.layout(), gson.width(), gson.height(), color1, color2, color3, gson.param());
        setDimension(0, iwin.heightAdd - iwin().height, gson.width(), iwin.heightAdd - iwin().height + gson.height());
    }

    /**
//    @Override
//    public void setLocation(ElemFrame frm) {
//        AreaSimple owner = frm.owner();
//        if (Layout.BOTT == frm.layout) {
//            frm.setDimension(owner.x1, owner.y2 - frm.artiklRec.getFloat(eArtikl.height), owner.x2, owner.y2);
//            frm.anglHoriz = 0;
//
//        } else if (Layout.RIGHT == frm.layout) {
//            frm.setDimension(owner.x2 - frm.artiklRec.getFloat(eArtikl.height), owner.y1, owner.x2, owner.y2);
//            frm.anglHoriz = 90;
//
//        } else if (Layout.LEFT == frm.layout) {
//            frm.setDimension(owner.x1, owner.y1, owner.x1 + frm.artiklRec.getFloat(eArtikl.height), owner.y2);
//            frm.anglHoriz = 270;
//
//        } else if (Layout.TOP == frm.layout) {
//            frm.setDimension(owner.x1, owner.y1, owner.x2, owner.y1); // + frm.artiklRec.getFloat(eArtikl.height));
//            frm.anglHoriz = 180;
//        }
//    }

//    @Override
//    public void setSpecific(ElemFrame frm) {
//        double katet = iwin().syssizeRec.getDbl(eSyssize.prip) * Math.cos(Math.PI / 4);
//
//        if (frm.owner.type == Type.ARCH && Layout.TOP == frm.layout()) {
//            AreaArch areaArch = (AreaArch) rootArea();
//            double angl = Math.toDegrees(Math.asin((width() / 2) / areaArch.radiusArch));
//            frm.length = (float) ((2 * Math.PI * areaArch.radiusArch) / 360 * angl * 2);
//            frm.spcRec.width = frm.length + (float) (katet / Math.sin(Math.toRadians(frm.anglCut[0])) + katet / Math.sin(Math.toRadians(frm.anglCut[1])));
//            frm.spcRec.height = mapFrame.get(Layout.TOP).artiklRec.getFloat(eArtikl.height);
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
//    }
*/
    
    @Override
    public void joinFrame() {
        ElemSimple elemBott = mapFrame.get(Layout.BOTT), elemRight = mapFrame.get(Layout.RIGHT),
                elemArch = mapFrame.get(Layout.TOP), elemLeft = mapFrame.get(Layout.LEFT);

        double dh = elemArch.artiklRec.getFloat(eArtikl.height);
        double dw = elemLeft.artiklRec.getFloat(eArtikl.height);
        double h = iwin().heightAdd - height();
        double w = width();
        double r = (Math.pow(w / 2, 2) + Math.pow(h, 2)) / (2 * h);  //R = (L2 + H2) / 2H - радиус арки        
        double rad1 = Math.acos((w / 2) / r); // Math.toDegrees() — преобразование радианов в градусы ... Math.asin() — арксинус
        double rad2 = Math.acos((w - 2 * dh) / ((r - dh) * 2));
        double a1 = r * Math.sin(rad1);
        double a2 = (r - dh) * Math.sin(rad2);
        double ang3 = 90 - Math.toDegrees(Math.atan((a1 - a2) / dh)); //угол реза рамы
        double ang4 = 90 - (Math.toDegrees(rad1) - (90 - ang3)); //угол реза арки
        radiusArch = r;
        //Угловое соединение левое верхнее
        ElemJoining elemJoin1 = new ElemJoining(iwin(), TypeJoin.VAR20, LayoutJoin.LTOP, elemArch, elemLeft, (float) ang4);
        elemJoin1.elem1.anglCut[1] = (float) ang4;  //угол реза арки
        elemJoin1.elem2.anglCut[0] = (float) ang3;  //угол реза рамы
        iwin().mapJoin.put(elemArch.joinPoint(1), elemJoin1);

        //Угловое соединение правое верхнее
        ElemJoining elemJoin2 = new ElemJoining(iwin(), TypeJoin.VAR20, LayoutJoin.RTOP, elemRight, elemArch, (float) ang4);
        elemJoin2.elem2.anglCut[0] = (float) ang4;  //угол реза арки
        elemJoin2.elem1.anglCut[1] = (float) ang3;  //угол реза рамы                             
        iwin().mapJoin.put(elemRight.joinPoint(1), elemJoin2);

        //Угловое соединение левое нижнее
        ElemJoining elem3 = new ElemJoining(iwin(), TypeJoin.VAR20, LayoutJoin.LBOT, elemLeft, elemBott, 90);
        iwin().mapJoin.put(elemLeft.joinPoint(1), elem3);

        //Угловое соединение правое нижнее
        ElemJoining elem4 = new ElemJoining(iwin(), TypeJoin.VAR20, LayoutJoin.RBOT, elemBott, elemRight, 90);
        iwin().mapJoin.put(elemBott.joinPoint(1), elem4);
    }

    protected void shtapik(ElemGlass elemGlass, Specific spcAdd) {
        Float dw = spcAdd.width;
        ElemSimple imp = elemGlass.joinFlat(Layout.BOTT);
        ElemSimple arch = mapFrame.get(Layout.TOP);
        ElemSimple rama = mapFrame.get(Layout.LEFT);

        if (elemGlass.anglHoriz == elemGlass.sideHoriz[0]) { //по основанию арки
            double r1 = radiusArch - arch.artiklRec.getFloat(eArtikl.height) + arch.artiklRec.getDbl(eArtikl.size_falz); //внешний радиус
            double h1 = imp.y1 + imp.artiklRec.getDbl(eArtikl.size_falz) - arch.artiklRec.getDbl(eArtikl.height) + arch.artiklRec.getDbl(eArtikl.size_falz);
            double l1 = Math.sqrt((2 * r1 * h1) - (h1 * h1)); //длина нижней стороны штапика
            double r2 = r1 - spcAdd.artiklRec.getDbl(eArtikl.height); //внутренний радиус
            double h2 = h1 - 2 * spcAdd.artiklRec.getDbl(eArtikl.height);
            double l2 = Math.sqrt((2 * r2 * h2) - (h2 * h2)); //длина верхней стороны штапика
            double ang1 = Math.toDegrees(Math.atan(spcAdd.artiklRec.getDbl(eArtikl.height) / (l1 - l2))); //угол реза
            spcAdd.width = (float) (2 * l1 + dw);
            spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
            spcAdd.anglCut2 = (float) ang1;
            spcAdd.anglCut1 = (float) ang1;
            elemGlass.spcRec.spcList.add(spcAdd); //добавим спецификацию

        } else if (elemGlass.anglHoriz == elemGlass.sideHoriz[2]) { //по дуге арки   
            double r1 = radiusArch - arch.artiklRec.getFloat(eArtikl.height) + arch.artiklRec.getDbl(eArtikl.size_falz); //внешний радиус
            double h1 = imp.y1 + imp.artiklRec.getDbl(eArtikl.size_falz) - arch.artiklRec.getDbl(eArtikl.height) + arch.artiklRec.getDbl(eArtikl.size_falz);
            double l1 = Math.sqrt((2 * r1 * h1) - (h1 * h1)); //длина нижней стороны штапика
            double r2 = r1 - spcAdd.artiklRec.getDbl(eArtikl.height); //внутренний радиус
            double h2 = h1 - 2 * spcAdd.artiklRec.getDbl(eArtikl.height);
            double l2 = Math.sqrt((2 * r2 * h2) - (h2 * h2)); //длина верхней стороны штапика   
            double ang1 = Math.toDegrees(Math.atan(spcAdd.artiklRec.getDbl(eArtikl.height) / (l1 - l2))); //угол реза
            double ang2 = Math.toDegrees(Math.asin(l1 / r1));
            double l4 = ((2 * Math.PI * r1) / 360) * ang2 * 2; //длина верхней стороны арки штапика
            double ang3 = 90 - (90 - ang2 + ang1);
            spcAdd.width = (float) (dw + l4);
            spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
            spcAdd.anglCut2 = (float) ang3;
            spcAdd.anglCut1 = (float) ang3;
            elemGlass.spcRec.spcList.add(spcAdd); //добавим спецификацию
        }
    }

    protected void padding(ElemGlass elemGlass, Specific spcAdd) {

        if (elemGlass.anglHoriz == elemGlass.sideHoriz[0]) { //по основанию арки
            double dh2 = spcAdd.artiklRec.getFloat(eArtikl.height) - elemGlass.gzazo;
            double r1 = elemGlass.radiusGlass - dh2;
            double h1 = elemGlass.height() - 2 * dh2;
            double l1 = Math.sqrt(2 * h1 * r1 - h1 * h1);  //верхний перимет
            double r2 = elemGlass.radiusGlass;
            double h2 = elemGlass.height();
            double l2 = Math.sqrt(2 * h2 * r2 - h2 * h2); //нижний периметр
            double l3 = l2 - l1;
            double r5 = elemGlass.radiusGlass + elemGlass.gzazo;
            double h5 = elemGlass.height() + 2 * elemGlass.gzazo;
            double l5 = 2 * Math.sqrt(2 * h5 * r5 - h5 * h5); //хорда
            double ang = Math.toDegrees(Math.atan(dh2 / l3)); //угол реза
            spcAdd.width = (float) l5;
            spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
            spcAdd.anglCut2 = (float) ang;
            spcAdd.anglCut1 = (float) ang;
            elemGlass.spcRec.spcList.add(new Specific(spcAdd)); //добавим спецификацию

        } else if (elemGlass.anglHoriz == elemGlass.sideHoriz[2]) { //по дуге арки 
            double dh2 = spcAdd.artiklRec.getFloat(eArtikl.height) - elemGlass.gzazo;
            double r1 = elemGlass.radiusGlass - dh2;
            double h1 = elemGlass.height() - 2 * dh2;
            double l1 = Math.sqrt(2 * h1 * r1 - h1 * h1);  //верхний перимет
            double r2 = elemGlass.radiusGlass;
            double h2 = elemGlass.height();
            double l2 = Math.sqrt(2 * h2 * r2 - h2 * h2); //нижний периметр
            double l3 = l2 - l1;
            double r5 = elemGlass.radiusGlass + elemGlass.gzazo;
            double h5 = elemGlass.height() + 2 * elemGlass.gzazo;
            double l5 = 2 * Math.sqrt(2 * h5 * r5 - h5 * h5); //хорда
            double ang = Math.toDegrees(Math.atan(dh2 / l3)); //угол реза            
            double ang2 = Math.toDegrees(Math.asin(l2 / r2));
            double ang3 = 90 - (90 - ang2 + ang);
            double Z = 3 * elemGlass.gzazo;
            double R = elemGlass.radiusGlass;
            double L = elemGlass.width();
            double ang5 = Math.toDegrees(Math.asin((L + (2 * Z)) / ((R + Z) * 2)));
            double M = ((R + Z) * 2) * Math.toRadians(ang5);
            spcAdd.width = (float) M;
            spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
            spcAdd.anglCut2 = (float) ang3;
            spcAdd.anglCut1 = (float) ang3;
            elemGlass.spcRec.spcList.add(new Specific(spcAdd)); //добавим спецификацию   
        }
    }
}
