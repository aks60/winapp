package builder.model;

import domain.eArtikl;
import enums.LayoutJoin;
import enums.TypeJoin;
import enums.LayoutArea;
import enums.TypeElem;
import builder.Wincalc;
import builder.calculate.SpecificRec;
import enums.TypeArtikl;

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
        float dz = elem1.joinElement1.artiklRec.getFloat(eArtikl.height);
        float h = iwin().heightAdd - height();
        float w = width();
        double r = (Math.pow(w / 2, 2) + Math.pow(h, 2)) / (2 * h);  //R = (L2 + H2) / 2H - радиус арки
        radiusArch = r; //запишем радиус дуги в AreaArch
        double angl = 90 - Math.toDegrees(Math.asin(w / (r * 2))); // Math.toDegrees() — преобразование радианов в градусы ... Math.asin() — арксинус
        double ang2 = 90 - Math.toDegrees(Math.asin((w - 2 * dz) / ((r - dz) * 2)));
        double a1 = r * Math.sin(Math.toRadians(angl));
        double a2 = (r - dz) * Math.sin(Math.toRadians(ang2));
        double ang3 = 90 - Math.toDegrees(Math.atan((a1 - a2) / dz)); //угол реза рамы
        double a3 = Math.sqrt(Math.pow(r, 2) + Math.pow(r - dz, 2) - 2 * r * (r - dz) * Math.cos(Math.toRadians(ang2 - angl)));
        double ang4 = 90 - Math.toDegrees((Math.acos((Math.pow(a3, 2) + Math.pow(r, 2) - Math.pow(r - dz, 2)) / (2 * r * a3))));
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

    public void calcShtapik(ElemGlass elemGlass, SpecificRec spcAdd) {

        Float overLength = (spcAdd.getParam(null, 15050) == null) ? 0.f : Float.valueOf(spcAdd.getParam(0, 15050).toString());
        //По основанию арки
        double dh2 = spcAdd.artiklRec.getDbl(eArtikl.height) - elemGlass.gzazo;
        double r1 = elemGlass.radiusGlass - dh2;
        double h1 = elemGlass.height() - 2 * dh2;
        double l1 = Math.sqrt(2 * h1 * r1 - h1 * h1);  //верхний периметр
        double r2 = elemGlass.radiusGlass;
        double h2 = elemGlass.height();
        double l2 = Math.sqrt(2 * h2 * r2 - h2 * h2); //нижний периметр
        double l3 = l2 - l1;
        double ang = Math.toDegrees(Math.atan(dh2 / l3)); //угол реза

        double r5 = elemGlass.radiusGlass + elemGlass.gzazo;
        double h5 = elemGlass.height() + 2 * elemGlass.gzazo;
        double l5 = overLength + 2 * Math.sqrt(2 * h5 * r5 - h5 * h5); //хорда

        spcAdd.width = (float) l5;
        spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
        spcAdd.anglCut2 = (float) ang;
        spcAdd.anglCut1 = (float) ang;
        elemGlass.spcRec.spcList.add(new SpecificRec(spcAdd)); //добавим спецификацию

        //По дуге арки
        double ang2 = Math.toDegrees(Math.asin(l2 / r2));
        double ang3 = 90 - (90 - ang2 + ang);
        double koef = 2; //TODO  ВАЖНО !!! Длина дуги штапика сделал примерный расчёт. Почему так, пока не понял. Поправочный коэф. надо вводить в зависимости от высоты импоста                
        ElemSimple ramaArch = root().mapFrame.get(LayoutArea.ARCH);
        double R2 = ((AreaArch) iwin().rootArea).radiusArch - ramaArch.spcRec.height + spcAdd.artiklRec.getDbl(eArtikl.height);
        double L2 = iwin().rootArea.width() - ramaArch.spcRec.height * 2 + spcAdd.artiklRec.getDbl(eArtikl.height) * 2 - koef;
        double ANGL2 = Math.toDegrees(Math.asin(L2 / (R2 * 2)));
        double M2 = (R2 * 2) * Math.toRadians(ANGL2); // +  overLength;
        double Z = 3 * elemGlass.gzazo;
        double R = elemGlass.radiusGlass;
        double L = elemGlass.width();
        double ang5 = Math.toDegrees(Math.asin((L + (2 * Z)) / ((R + Z) * 2)));
        double M = ((R + Z) * 2) * Math.toRadians(ang5);
        spcAdd.width = (float) (overLength + M2);
        spcAdd.height = spcAdd.artiklRec.getFloat(eArtikl.height);
        spcAdd.anglCut2 = (float) ang3;
        spcAdd.anglCut1 = (float) ang3;
        elemGlass.spcRec.spcList.add(new SpecificRec(spcAdd)); //добавим спецификацию
    }

    public void calcPadding(ElemGlass elemGlass, SpecificRec spcAdd) {

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
