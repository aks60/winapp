package builder.model;

import builder.IElem5e;
import domain.eArtikl;
import enums.LayoutJoin;
import enums.TypeJoin;
import enums.Layout;
import builder.Wincalc;
import builder.making.Specific;
import enums.Type;

public class AreaArch extends AreaSimple {

    public double radiusArch = 0; //радиус арки

    public AreaArch(Wincalc winc) {
        super(winc);
        setDimension(0, 0, winc.rootGson.width(), winc.rootGson.height());
    }

    //Угловые соединения
    @Override
    public void joining() {

        super.joining(); //T - соединения

        IElem5e elemBott = frames.get(Layout.BOTT), elemRight = frames.get(Layout.RIGHT),
                elemArch = frames.get(Layout.TOP), elemLeft = frames.get(Layout.LEFT);

        double dh = elemArch.artiklRec().getDbl(eArtikl.height);
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

        //Угловое соединение левое нижнее
        ElemJoining elem3 = new ElemJoining(winc, TypeJoin.VAR20, LayoutJoin.LBOT, elemLeft, elemBott);
        winc.listJoin.add(elem3);

        //Угловое соединение правое нижнее
        ElemJoining elem4 = new ElemJoining(winc, TypeJoin.VAR20, LayoutJoin.RBOT, elemBott, elemRight);
        winc.listJoin.add(elem4);

        //Угловое соединение правое верхнее
        ElemJoining elemJoin2 = new ElemJoining(winc, TypeJoin.VAR20, LayoutJoin.RTOP, elemRight, elemArch);
        elemJoin2.elem2.anglCut(1, ang4);  //угол реза арки
        elemJoin2.elem1.anglCut(2, ang3);  //угол реза рамы                             
        winc.listJoin.add(elemJoin2);

        //Угловое соединение левое верхнее
        ElemJoining elemJoin1 = new ElemJoining(winc, TypeJoin.VAR20, LayoutJoin.LTOP, elemArch, elemLeft);
        elemJoin1.elem1.anglCut(2, ang4);  //угол реза арки
        elemJoin1.elem2.anglCut(1, ang3);  //угол реза рамы
        winc.listJoin.add(elemJoin1);
    }
}
