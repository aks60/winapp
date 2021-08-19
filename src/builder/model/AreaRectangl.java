package builder.model;

import enums.Layout;
import enums.LayoutJoin;
import enums.TypeJoin;
import builder.Wincalc;
import enums.PKjson;
import enums.Type;

public class AreaRectangl extends AreaSimple {

    public AreaRectangl(Wincalc iwin, AreaSimple owner, Type type, Layout layout, float width, float height, int color1, int color2, int color3, String param) {
        super(iwin, owner, type, layout, width, height, color1, color2, color3, param);

        if (param(param, PKjson.colorID1) != -1) {
            this.colorID1 = param(param, PKjson.colorID1);
        }
    }

    //@Override
    public void joinFrame() {
        ElemSimple elemBott = mapFrame.get(Layout.BOTT), elemRight = mapFrame.get(Layout.RIGHT),
                elemTop = mapFrame.get(Layout.TOP), elemLeft = mapFrame.get(Layout.LEFT);
        //Цикл по сторонам рамы
        for (int index = 0; index < 4; index++) {

            if (index == 0) { //Угловое соединение правое нижнее
                ElemJoining el = new ElemJoining( TypeJoin.VAR20, LayoutJoin.RBOT, elemBott, elemRight, 90);
                iwin().mapJoin.put(elemBott.joinPoint(1), el);

            } else if (index == 1) { //Угловое соединение правое верхнее
                ElemJoining el = new ElemJoining(TypeJoin.VAR20, LayoutJoin.RTOP, elemRight, elemTop, 90);
                iwin().mapJoin.put(elemRight.joinPoint(1), el);

            } else if (index == 2) { //Угловое соединение левое верхнее    
                ElemJoining el = new ElemJoining(TypeJoin.VAR20, LayoutJoin.LTOP, elemTop, elemLeft, 90);
                iwin().mapJoin.put(elemTop.joinPoint(1), el);

            } else if (index == 3) { //Угловое соединение левое нижнее
                ElemJoining el = new ElemJoining(TypeJoin.VAR20, LayoutJoin.LBOT, elemLeft, elemRight, 90);
                iwin().mapJoin.put(elemLeft.joinPoint(1), el);
            }
        }
    }   
}
