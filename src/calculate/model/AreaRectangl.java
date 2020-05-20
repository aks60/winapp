package calculate.model;

import enums.LayoutArea;
import enums.TypeElem;
import enums.LayoutJoin;
import enums.TypeJoin;
import calculate.Wincalc;

public class AreaRectangl extends AreaSimple {

    public AreaRectangl(Wincalc iwin, AreaSimple owner, float id, TypeElem typeElem, LayoutArea layout, float width, float height, int color1, int color2, int color3, String param) {
        super(iwin, owner, id, typeElem, layout, width, height, color1, color2, color3);
        parsing(param);
    }

    @Override
    public void joinFrame() {
        for (int index = 0; index < 4; index++) {
            ElemJoining el = new ElemJoining(iwin());
            el.varJoin = TypeJoin.VAR20;
            el.id = id() + (index + 1) / 100;
            el.cutAngl1 = 45;
            el.cutAngl2 = 45;

            if (index == 0) {
                el.name = "Угловое соединение левое верхнее";
                el.typeJoin = LayoutJoin.LTOP;
                el.joinElement1 = mapFrame.get(LayoutArea.LEFT);
                el.joinElement2 = mapFrame.get(LayoutArea.TOP);
                iwin().mapJoin.put(String.valueOf(x1) + ":" + String.valueOf(y1), el);

            } else if (index == 1) {
                el.name = "Угловое соединение левое нижнее";
                el.typeJoin = LayoutJoin.LBOT;
                el.joinElement1 = mapFrame.get(LayoutArea.LEFT);
                el.joinElement2 = mapFrame.get(LayoutArea.BOTTOM);
                iwin().mapJoin.put(String.valueOf(x1) + ":" + String.valueOf(y2), el);

            } else if (index == 2) {
                el.name = "Угловое соединение правое нижнее";
                el.typeJoin = LayoutJoin.RBOT;
                el.joinElement1 = mapFrame.get(LayoutArea.RIGHT);
                el.joinElement2 = mapFrame.get(LayoutArea.BOTTOM);
                iwin().mapJoin.put(String.valueOf(x2) + ":" + String.valueOf(y2), el);

            } else if (index == 3) {
                el.name = "Угловое соединение правое верхнее";
                el.typeJoin = LayoutJoin.RTOP;
                el.joinElement1 = mapFrame.get(LayoutArea.RIGHT);
                el.joinElement2 = mapFrame.get(LayoutArea.TOP);
                iwin().mapJoin.put(String.valueOf(x2) + ":" + String.valueOf(y1), el);
            }
        }
    }
}
