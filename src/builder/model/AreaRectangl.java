package builder.model;

import enums.LayoutArea;
import enums.TypeElem;
import enums.LayoutJoin;
import enums.TypeJoin;
import builder.Wincalc;
import common.Util;
import enums.PKjson;

public class AreaRectangl extends AreaSimple {

    public AreaRectangl(Wincalc iwin, AreaSimple owner, float id, TypeElem typeElem, LayoutArea layout, float width, float height, int color1, int color2, int color3, String param) {
        super(iwin, owner, id, typeElem, layout, width, height, color1, color2, color3, param);

        if (param(param, PKjson.colorID1) != -1) {
            this.colorID1 = param(param, PKjson.colorID1);
        }
    }

    //@Override
    public void joinFrame() {
        ElemSimple elemBott = mapFrame.get(LayoutArea.BOTT), elemRight = mapFrame.get(LayoutArea.RIGHT),
                elemTop = mapFrame.get(LayoutArea.TOP), elemLeft = mapFrame.get(LayoutArea.LEFT);
        //Цикл по сторонам рамы
        for (int index = 0; index < 4; index++) {
            ElemJoining el = new ElemJoining(iwin());
            el.id = id() + (float) (index + 1) / 100;
            el.anglProf = 90;

            if (index == 0) { //Угловое соединение правое нижнее
                el.init(TypeJoin.VAR20, LayoutJoin.RBOT, elemBott, elemRight);
                iwin().mapJoin.put(elemBott.joinPoint(1), el);

            } else if (index == 1) { //Угловое соединение правое верхнее
                el.init(TypeJoin.VAR20, LayoutJoin.RTOP, elemRight, elemTop);
                iwin().mapJoin.put(elemRight.joinPoint(1), el);

            } else if (index == 2) { //Угловое соединение левое верхнее    
                el.init(TypeJoin.VAR20, LayoutJoin.LTOP, elemTop, elemLeft);
                iwin().mapJoin.put(elemTop.joinPoint(1), el);

            } else if (index == 3) { //Угловое соединение левое нижнее
                el.init(TypeJoin.VAR20, LayoutJoin.LBOT, elemLeft, elemRight);
                iwin().mapJoin.put(elemLeft.joinPoint(1), el);
            }
        }
    }   
}
