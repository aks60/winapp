package wincalc.model;

import enums.LayoutArea;
import enums.TypeElem;
import wincalc.Wincalc;

public class AreaTrapeze extends AreaSimple {

    public AreaTrapeze(Wincalc iwin, String id, TypeElem typeElem, LayoutArea layout, float width, float height, int color1, int color2, int color3, String param) {
        super(iwin, null, id, typeElem, layout, width, height, color1, color2, color3);
        parsing(param);
    }

    @Override
    public void joinFrame() {
        System.out.println("Функция не определена");
    }
}
