package estimate.model;

import enums.LayoutArea;
import enums.TypeElem;
import enums.LayoutJoin;
import enums.TypeJoin;
import estimate.Wincalc;

public class AreaRectangl extends AreaSimple {

    public AreaRectangl(Wincalc iwin, AreaSimple owner, float id, TypeElem typeElem, LayoutArea layout, float width, float height, int color1, int color2, int color3, String param) {
        super(iwin, owner, id, typeElem, layout, width, height, color1, color2, color3, param);

        mapFrame.put(LayoutArea.BOTTOM, new ElemFrame(this, 2, LayoutArea.BOTTOM));
        mapFrame.put(LayoutArea.RIGHT, new ElemFrame(this, 3, LayoutArea.RIGHT));
        mapFrame.put(LayoutArea.TOP, new ElemFrame(this, 4, LayoutArea.TOP));
        mapFrame.put(LayoutArea.LEFT, new ElemFrame(this, 5, LayoutArea.LEFT));
    }

    @Override
    public void joinFrame() {
        //Цикл по сторонам рамы
        for (int index = 0; index < 4; index++) {
            ElemJoining el = new ElemJoining(iwin());
            el.id = id() + (float) (index + 1) / 100;
            mapFrame.get(LayoutArea.BOTTOM).anglHoriz = 0;
            mapFrame.get(LayoutArea.RIGHT).anglHoriz = 90;
            mapFrame.get(LayoutArea.TOP).anglHoriz = 180;
            mapFrame.get(LayoutArea.LEFT).anglHoriz = 270;
            el.anglProf = 90;
            if (index == 0) { //Угловое соединение левое верхнее
                el.init(TypeJoin.VAR20, LayoutJoin.LTOP, mapFrame.get(LayoutArea.LEFT), mapFrame.get(LayoutArea.TOP));
                iwin().mapJoin.put(String.valueOf(x1) + ":" + String.valueOf(y1), el);

            } else if (index == 1) { //Угловое соединение левое нижнее
                el.init(TypeJoin.VAR20, LayoutJoin.LBOT, mapFrame.get(LayoutArea.LEFT), mapFrame.get(LayoutArea.BOTTOM));
                iwin().mapJoin.put(String.valueOf(x1) + ":" + String.valueOf(y2), el);

            } else if (index == 2) { //Угловое соединение правое нижнее
                el.init(TypeJoin.VAR20, LayoutJoin.RBOT, mapFrame.get(LayoutArea.RIGHT), mapFrame.get(LayoutArea.BOTTOM));
                iwin().mapJoin.put(String.valueOf(x2) + ":" + String.valueOf(y2), el);

            } else if (index == 3) { //Угловое соединение правое верхнее
                el.init(TypeJoin.VAR20, LayoutJoin.RTOP, mapFrame.get(LayoutArea.RIGHT), mapFrame.get(LayoutArea.TOP));
                iwin().mapJoin.put(String.valueOf(x2) + ":" + String.valueOf(y1), el);
            }
        }
    }
}
