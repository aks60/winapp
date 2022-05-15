package builder.model;

import domain.eArtikl;
import enums.LayoutJoin;
import enums.TypeJoin;
import enums.Layout;
import builder.Wincalc;
import builder.making.Specific;
import builder.script.GsonRoot;
import enums.Form;
import enums.Type;

public class AreaArch extends AreaSimple {

    public double radiusArch = 0; //радиус арки

    public AreaArch(Wincalc winc) {
        super(winc);
        setDimension(0, 0, winc.rootGson.width(), winc.rootGson.height());
    }

    //@Override
    protected void addFilling(ElemGlass glass, Specific spcAdd) {
        Float dw = spcAdd.width;
        ElemSimple imp = glass.joinFlat(Layout.BOTT);
        ElemSimple arch = frames.get(Layout.TOP);

        if (glass.anglHoriz == glass.sideHoriz[0]) { //по основанию арки
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
            glass.spcRec.spcList.add(spcAdd); //добавим спецификацию

        } else if (glass.anglHoriz == glass.sideHoriz[2]) { //по дуге арки   
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
            glass.spcRec.spcList.add(spcAdd); //добавим спецификацию
        }
    }
    
    @Override
    //Угловые соединения
    public void joining() {
        
        super.joining(); //T - соединения
        
        ElemSimple elemBott = frames.get(Layout.BOTT), elemRight = frames.get(Layout.RIGHT),
                elemArch = frames.get(Layout.TOP), elemLeft = frames.get(Layout.LEFT);

        double dh = elemArch.artiklRec.getFloat(eArtikl.height);
        double h = height() - winc.height2();
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
        ElemJoining elemJoin1 = new ElemJoining(winc, TypeJoin.VAR20, LayoutJoin.LTOP, elemArch, elemLeft, (float) ang4);
        elemJoin1.elem1.anglCut[1] = (float) ang4;  //угол реза арки
        elemJoin1.elem2.anglCut[0] = (float) ang3;  //угол реза рамы
        winc.mapJoin.put(elemLeft.joinPoint(0), elemJoin1);

        //Угловое соединение правое верхнее
        ElemJoining elemJoin2 = new ElemJoining(winc, TypeJoin.VAR20, LayoutJoin.RTOP, elemRight, elemArch, (float) ang4);
        elemJoin2.elem2.anglCut[0] = (float) ang4;  //угол реза арки
        elemJoin2.elem1.anglCut[1] = (float) ang3;  //угол реза рамы                             
        winc.mapJoin.put(elemRight.joinPoint(1), elemJoin2);

        //Угловое соединение левое нижнее
        ElemJoining elem3 = new ElemJoining(winc, TypeJoin.VAR20, LayoutJoin.LBOT, elemLeft, elemBott, 90);
        winc.mapJoin.put(elemBott.joinPoint(0), elem3);

        //Угловое соединение правое нижнее
        ElemJoining elem4 = new ElemJoining(winc, TypeJoin.VAR20, LayoutJoin.RBOT, elemBott, elemRight, 90);
        winc.mapJoin.put(elemBott.joinPoint(1), elem4);
    }    
}
