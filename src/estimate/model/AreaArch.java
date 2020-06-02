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
        super(iwin, owner, id, typeElem, layout, width, height, color1, color2, color3);
        parsing(param);
    }

    @Override
    public void joinFrame() {

        
        ElemJoining elem = new ElemJoining(iwin());
        elem.id = id() + .1f;
        //Угловое соединение левое верхнее
        elem.joinElement1 = mapFrame.get(LayoutArea.LEFT);
        elem.joinElement2 = mapFrame.get(LayoutArea.ARCH);

        float dz = elem.joinElement1.artiklRec.getFloat(eArtikl.height);
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

        elem.cutAngl1 = (float) ang3; //угол реза 1
        elem.cutAngl2 = (float) ang4; //угол реза 2
        elem.anglProf = (float) ang4;
        elem.joinElement1.anglCut(ElemSimple.SIDE_END, elem.cutAngl2);
        elem.joinElement2.anglCut(ElemSimple.SIDE_START, elem.cutAngl1);
        iwin().mapJoin.put(x1 + ":" + y1, elem);

        for (int index = 0; index < 3; index++) {
            ElemJoining el = new ElemJoining(iwin());
            el.typeJoin = TypeJoin.VAR20;
            el.id = id() + (float)(index + 2) / 100;
            el.cutAngl1 = 45;
            el.cutAngl2 = 45;

            if (index == 0) { //угловое соединение правое верхнее
                el.joinElement1 = mapFrame.get(LayoutArea.RIGHT);
                el.joinElement2 = mapFrame.get(LayoutArea.ARCH);
                el.cutAngl1 = (float) ang3;
                el.cutAngl2 = (float) ang4;
                el.anglProf = (float) ang4;
                el.joinElement1.anglCut(ElemSimple.SIDE_START, el.cutAngl2);
                el.joinElement2.anglCut(ElemSimple.SIDE_END, el.cutAngl1);
                iwin().mapJoin.put(x2 + ":" + y1, el);

            } else if (index == 1) {  //угловое соединение левое нижнее
                el.init(TypeJoin.VAR20, LayoutJoin.LBOT, mapFrame.get(LayoutArea.LEFT), mapFrame.get(LayoutArea.BOTTOM));
                iwin().mapJoin.put(x1 + ":" + y2, el);

            } else if (index == 2) { //угловое соединение правое нижнее
                el.init(TypeJoin.VAR20, LayoutJoin.RBOT, mapFrame.get(LayoutArea.RIGHT), mapFrame.get(LayoutArea.BOTTOM));
                iwin().mapJoin.put(x2 + ":" + y2, el);
            }
        }
    }
    
    @Override
    public String toString() {
        return super.toString() + ", radiusArch=" + radiusArch;
    }
}
