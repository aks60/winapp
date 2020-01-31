package wincalc.model;

import domain.eArtikl;
import enums.LayoutArea;
import enums.TypeElem;
import wincalc.Wincalc;

public class AreaArch extends AreaSimple {

    protected double radiusArch = 0; //радиус арки

    public AreaArch(Wincalc iwin, String id, LayoutArea layout, float width, float height, int color1, int color2, int color3, String param) {
        super(iwin, null, id, layout, width, height, color1, color2, color3);
        parsing(param);
    }

    @Override
    public void joinFrame() {

        String key1 = String.valueOf(x1) + ":" + String.valueOf(y1);
        String key2 = String.valueOf(x2) + ":" + String.valueOf(y1);
        String key3 = String.valueOf(x2) + ":" + String.valueOf(y2);
        String key4 = String.valueOf(x1) + ":" + String.valueOf(y2);

        //Угловое соединение левое верхнее
        ElemJoining elemJoin1 = new ElemJoining(iwin);
        elemJoin1.elemJoinRight = mapFrame.get(LayoutArea.ARCH);
        elemJoin1.elemJoinBottom = mapFrame.get(LayoutArea.LEFT);
        iwin.mapJoin.put(key1, elemJoin1);
        float dz = elemJoin1.elemJoinRight.articlRec.getFloat(eArtikl.height);
        float h = iwin.heightAdd - height;
        float w = width;
        double r = (Math.pow(w / 2, 2) + Math.pow(h, 2)) / (2 * h);  //R = (L2 + H2) / 2H - радиус арки
        radiusArch = r; //запишем радиус дуги в AreaArch
        double angl = 90 - Math.toDegrees(Math.asin(w / (r * 2))); // Math.toDegrees() — преобразование радианов в градусы ... Math.asin() — арксинус
        double ang2 = 90 - Math.toDegrees(Math.asin((w - 2 * dz) / ((r - dz) * 2)));
        double a1 = r * Math.sin(Math.toRadians(angl));
        double a2 = (r - dz) * Math.sin(Math.toRadians(ang2));
        double ang3 = 90 - Math.toDegrees(Math.atan((a1 - a2) / dz)); //угол реза рамы
        double a3 = Math.sqrt(Math.pow(r, 2) + Math.pow(r - dz, 2) - 2 * r * (r - dz) * Math.cos(Math.toRadians(ang2 - angl)));
        double ang4 = 90 - Math.toDegrees((Math.acos((Math.pow(a3, 2) + Math.pow(r, 2) - Math.pow(r - dz, 2)) / (2 * r * a3))));
        elemJoin1.cutAngl1 = (float) ang3; //угол реза 1
        elemJoin1.cutAngl2 = (float) ang4; //угол реза 2
        elemJoin1.anglProf = (float) ang4;
        elemJoin1.elemJoinRight.anglCut(ElemSimple.SIDE_END, elemJoin1.cutAngl2);
        elemJoin1.elemJoinBottom.anglCut(ElemSimple.SIDE_START, elemJoin1.cutAngl1);

        //Угловое соединение правое верхнее
        ElemJoining elemJoin2 = new ElemJoining(iwin);
        elemJoin2.elemJoinLeft = mapFrame.get(LayoutArea.ARCH);
        elemJoin2.elemJoinBottom = mapFrame.get(LayoutArea.RIGHT);
        iwin.mapJoin.put(key2, elemJoin2);
        elemJoin2.cutAngl1 = (float) ang3;
        elemJoin2.cutAngl2 = (float) ang4;
        elemJoin2.anglProf = (float) ang4;
        elemJoin2.elemJoinLeft.anglCut(ElemSimple.SIDE_START, elemJoin2.cutAngl2);
        elemJoin2.elemJoinBottom.anglCut(ElemSimple.SIDE_END, elemJoin2.cutAngl1);

        //Угловое соединение правое нижнее
        ElemJoining elemJoin3 = new ElemJoining(iwin);
        elemJoin3.elemJoinTop = mapFrame.get(LayoutArea.RIGHT);
        elemJoin3.elemJoinLeft = mapFrame.get(LayoutArea.BOTTOM);
        iwin.mapJoin.put(key3, elemJoin3);
        elemJoin3.cutAngl1 = 45;
        elemJoin3.cutAngl2 = 45;
        elemJoin3.anglProf = 90;
        elemJoin3.elemJoinTop.anglCut(ElemSimple.SIDE_START, elemJoin3.cutAngl1);
        elemJoin3.elemJoinLeft.anglCut(ElemSimple.SIDE_END, elemJoin3.cutAngl2);

        //Угловое соединение левое нижнее
        ElemJoining elemJoin4 = new ElemJoining(iwin);
        elemJoin4.elemJoinRight = mapFrame.get(LayoutArea.BOTTOM);
        elemJoin4.elemJoinTop = mapFrame.get(LayoutArea.LEFT);
        iwin.mapJoin.put(key4, elemJoin4);
        elemJoin4.cutAngl1 = 45;
        elemJoin4.cutAngl2 = 45;
        elemJoin4.anglProf = 90;
        elemJoin4.elemJoinRight.anglCut(ElemSimple.SIDE_START, elemJoin4.cutAngl2);
        elemJoin4.elemJoinTop.anglCut(ElemSimple.SIDE_END, elemJoin4.cutAngl1);
    }

    @Override
    public TypeElem typeElem() {
        return TypeElem.ARCH;
    }
}
