package estimate.model;

import dataset.Record;
import domain.eArtikl;
import enums.LayoutJoin;
import enums.TypeJoin;
import enums.LayoutArea;
import enums.TypeElem;
import estimate.Wincalc;

public class AreaArch extends AreaSimple {

    public double radiusArch = 0; //радиус арки

    public AreaArch(Wincalc iwin, AreaSimple owner, float id, TypeElem typeElem, LayoutArea layout, float width, float height, int color1, int color2, int color3, String param) {
        super(iwin, owner, id, typeElem, layout, width, height, color1, color2, color3, param);

        mapFrame.put(LayoutArea.LEFT, new ElemFrame(this, 2, LayoutArea.LEFT));
        mapFrame.put(LayoutArea.RIGHT, new ElemFrame(this, 3, LayoutArea.RIGHT));
        mapFrame.put(LayoutArea.ARCH, new ElemFrame(this, 4, LayoutArea.ARCH));
        mapFrame.put(LayoutArea.BOTTOM, new ElemFrame(this, 5, LayoutArea.BOTTOM));
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
        elem1.joinElement1.anglCut1 = (float) ang4;  //угол реза арки
        elem1.joinElement2.anglCut2 = (float) ang3;  //угол реза рамы
        iwin().mapJoin.put(x1 + ":" + y1, elem1);

        //Угловое соединение правое верхнее
        ElemJoining elem2 = new ElemJoining(iwin());
        elem2.id = id() + .2f;
        elem2.init(TypeJoin.VAR20, LayoutJoin.RBOT, mapFrame.get(LayoutArea.RIGHT), mapFrame.get(LayoutArea.ARCH));
        elem2.anglProf = (float) ang4;
        elem2.joinElement2.anglCut2 = (float) ang4;  //угол реза арки
        elem2.joinElement1.anglCut2 = (float) ang3;  //угол реза рамы               
        iwin().mapJoin.put(x2 + ":" + y1, elem2);

        //Угловое соединение левое нижнее
        ElemJoining elem3 = new ElemJoining(iwin());
        elem3.id = id() + .3f;
        elem3.init(TypeJoin.VAR20, LayoutJoin.LBOT, mapFrame.get(LayoutArea.LEFT), mapFrame.get(LayoutArea.BOTTOM));
        elem3.anglProf = 90;
        elem3.joinElement2.anglCut2 = 45;  //угол реза рамы
        elem3.joinElement1.anglCut2 = 45;  //угол реза рамы               
        iwin().mapJoin.put(x1 + ":" + y2, elem3);

        //Угловое соединение правое нижнее
        ElemJoining elem4 = new ElemJoining(iwin());
        elem4.id = id() + .4f;
        elem4.init(TypeJoin.VAR20, LayoutJoin.LBOT, mapFrame.get(LayoutArea.BOTTOM), mapFrame.get(LayoutArea.RIGHT));
        elem4.anglProf = 90;
        elem4.joinElement2.anglCut2 = 45;  //угол реза рамы
        elem4.joinElement1.anglCut2 = 45;  //угол реза рамы               
        iwin().mapJoin.put(x2 + ":" + y2, elem4);
    }

    @Override
    public String toString() {
        return super.toString() + ", radiusArch=" + radiusArch;
    }
}
