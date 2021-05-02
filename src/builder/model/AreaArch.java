package builder.model;

import domain.eArtikl;
import enums.LayoutJoin;
import enums.TypeJoin;
import enums.LayoutArea;
import enums.TypeElem;
import builder.Wincalc;
import builder.making.SpecificRec;

public class AreaArch extends AreaSimple {

    public double radiusArch = 0; //радиус арки

    public AreaArch(Wincalc iwin, AreaSimple owner, float id, TypeElem typeElem, LayoutArea layout, float width, float height, int color1, int color2, int color3, String param) {
        super(iwin, owner, id, typeElem, layout, width, height, color1, color2, color3, param);
    }

    @Override
    public void joinFrame() {

        //Угловое соединение левое верхнее
        ElemJoining elem1 = new ElemJoining(iwin());
        elem1.id = id() + .1f;
        elem1.init(TypeJoin.VAR20, LayoutJoin.LTOP, mapFrame.get(LayoutArea.ARCH), mapFrame.get(LayoutArea.LEFT));

        double dh = elem1.joinElement1.artiklRec.getFloat(eArtikl.height);
        double dw = elem1.joinElement2.artiklRec.getFloat(eArtikl.height);
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
        elem1.anglProf = (float) ang4;
        elem1.joinElement1.anglCut2 = (float) ang4;  //угол реза арки
        elem1.joinElement2.anglCut1 = (float) ang3;  //угол реза рамы
        iwin().mapJoin.put(x1 + ":" + y1, elem1);

        //Угловое соединение правое верхнее
        ElemJoining elem2 = new ElemJoining(iwin());
        elem2.id = id() + .2f;
        elem2.init(TypeJoin.VAR20, LayoutJoin.RBOT, mapFrame.get(LayoutArea.RIGHT), mapFrame.get(LayoutArea.ARCH));
        elem2.anglProf = (float) ang4;
        elem2.joinElement2.anglCut1 = (float) ang4;  //угол реза арки
        elem2.joinElement1.anglCut2 = (float) ang3;  //угол реза рамы                             
        iwin().mapJoin.put(x2 + ":" + y1, elem2);

        //Угловое соединение левое нижнее
        ElemJoining elem3 = new ElemJoining(iwin());
        elem3.id = id() + .3f;
        elem3.init(TypeJoin.VAR20, LayoutJoin.LBOT, mapFrame.get(LayoutArea.LEFT), mapFrame.get(LayoutArea.BOTTOM));
        elem3.anglProf = 90;
        iwin().mapJoin.put(x1 + ":" + y2, elem3);

        //Угловое соединение правое нижнее
        ElemJoining elem4 = new ElemJoining(iwin());
        elem4.id = id() + .4f;
        elem4.init(TypeJoin.VAR20, LayoutJoin.LBOT, mapFrame.get(LayoutArea.BOTTOM), mapFrame.get(LayoutArea.RIGHT));
        elem4.anglProf = 90;
        iwin().mapJoin.put(x2 + ":" + y2, elem4);
    }

    protected void frame(ElemFrame elemFrame, double katet) {
        AreaArch areaArch = (AreaArch) root();
        double angl = Math.toDegrees(Math.asin((width() / 2) / areaArch.radiusArch));
        elemFrame.length = (float) (2 * Math.PI * areaArch.radiusArch * angl) / 180;
        elemFrame.spcRec.width = elemFrame.length + (float) (katet / Math.sin(Math.toRadians(elemFrame.anglCut1)) + katet / Math.sin(Math.toRadians(elemFrame.anglCut2)));
        elemFrame.spcRec.height = artiklRec.getFloat(eArtikl.height);
    }

    protected void shtapik(ElemGlass elemGlass, SpecificRec spcAdd) {
        Float dw = spcAdd.width;
        ElemSimple imp = elemGlass.join(LayoutArea.BOTTOM);
        ElemSimple arch = mapFrame.get(LayoutArea.ARCH);
        ElemSimple rama = mapFrame.get(LayoutArea.LEFT);

        //По основанию арки
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
        elemGlass.spcRec.spcList.add(new SpecificRec(spcAdd)); //добавим спецификацию

        //По дуге арки         
        double rad4 = Math.asin(l1 / r1); 
        double l4 = (2 * Math.PI * r1 * Math.toDegrees(rad4)) / 180; //длина верхней стороны эллипса штапика
        double ang3 = (90 - Math.toDegrees(rad4)) -  ang1;
        
        System.out.println( Math.toDegrees(rad4));
        
        spcAdd.width = (float) (dw + l4);
        spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
        spcAdd.anglCut2 = (float) ang3;
        spcAdd.anglCut1 = (float) ang3;
        elemGlass.spcRec.spcList.add(new SpecificRec(spcAdd)); //добавим спецификацию
    }

    protected void padding(ElemGlass elemGlass, SpecificRec spcAdd) {

        //По основанию арки
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
        elemGlass.spcRec.spcList.add(new SpecificRec(spcAdd)); //добавим спецификацию

        //По дуге арки
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
        elemGlass.spcRec.spcList.add(new SpecificRec(spcAdd)); //добавим спецификацию                
    }
}
