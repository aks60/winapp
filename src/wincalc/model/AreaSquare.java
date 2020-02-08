package wincalc.model;

import enums.LayoutArea;
import enums.TypeElem;
import enums.JoinLocate;
import enums.JoinVariant;
import wincalc.Wincalc;

public class AreaSquare extends AreaSimple {

    public AreaSquare(Wincalc iwin, String id, LayoutArea layout, float width, float height, int color1, int color2, int color3, String param) {
        super(iwin, null, id, layout, width, height, color1, color2, color3);
        this.owner = this;
        this.typeElem = TypeElem.SQUARE;
        parsing(param);
    }

    @Override
    public void joinFrame() {
        for (int index = 0; index < 4; index++) {
            ElemJoining el = new ElemJoining(iwin);
            el.varJoin = JoinVariant.VAR2;
            el.id = id + "." + (index + 1) + "R";
            el.cutAngl1 = 45;
            el.cutAngl2 = 45;

            if (index == 0) {
                el.name = "Угловое соединение левое верхнее";
                el.typeJoin = JoinLocate.LTOP;
                el.joinElement1 = mapFrame.get(LayoutArea.LEFT);
                el.joinElement2 = mapFrame.get(LayoutArea.TOP);
                iwin.mapJoin.put(String.valueOf(x1) + ":" + String.valueOf(y1), el);

            } else if (index == 1) {
                el.name = "Угловое соединение левое нижнее";
                el.typeJoin = JoinLocate.LBOT;
                el.joinElement1 = mapFrame.get(LayoutArea.LEFT);
                el.joinElement2 = mapFrame.get(LayoutArea.BOTTOM);
                iwin.mapJoin.put(String.valueOf(x1) + ":" + String.valueOf(y2), el);

            } else if (index == 2) {
                el.name = "Угловое соединение правое нижнее";
                el.typeJoin = JoinLocate.RBOT;
                el.joinElement1 = mapFrame.get(LayoutArea.RIGHT);
                el.joinElement2 = mapFrame.get(LayoutArea.BOTTOM);
                iwin.mapJoin.put(String.valueOf(x2) + ":" + String.valueOf(y2), el);

            } else if (index == 3) {
                el.name = "Угловое соединение правое верхнее";
                el.typeJoin = JoinLocate.RTOP;
                el.joinElement1 = mapFrame.get(LayoutArea.RIGHT);
                el.joinElement2 = mapFrame.get(LayoutArea.TOP);
                iwin.mapJoin.put(String.valueOf(x2) + ":" + String.valueOf(y1), el);
            }
        }
    }    
}
