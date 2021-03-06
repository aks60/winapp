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

            if (index == 0) { //Угловое соединение правое нижнее
                ElemJoining el = new ElemJoining(id() + (float) (index + 1) / 100, TypeJoin.VAR20, LayoutJoin.RBOT, elemBott, elemRight, 90);
                iwin().mapJoin.put(elemBott.joinPoint(1), el);

            } else if (index == 1) { //Угловое соединение правое верхнее
                ElemJoining el = new ElemJoining(id() + (float) (index + 1) / 100, TypeJoin.VAR20, LayoutJoin.RTOP, elemRight, elemTop, 90);
                iwin().mapJoin.put(elemRight.joinPoint(1), el);

            } else if (index == 2) { //Угловое соединение левое верхнее    
                ElemJoining el = new ElemJoining(id() + (float) (index + 1) / 100, TypeJoin.VAR20, LayoutJoin.LTOP, elemTop, elemLeft, 90);
                iwin().mapJoin.put(elemTop.joinPoint(1), el);

            } else if (index == 3) { //Угловое соединение левое нижнее
                ElemJoining el = new ElemJoining(id() + (float) (index + 1) / 100, TypeJoin.VAR20, LayoutJoin.LBOT, elemLeft, elemRight, 90);
                iwin().mapJoin.put(elemLeft.joinPoint(1), el);
            }
        }
    }   
}
