package wincalc.model;

import enums.LayoutArea;
import enums.TypeElem;
import wincalc.Wincalc;

public class AreaTriangl extends AreaSimple {

    public AreaTriangl(Wincalc iwin, String id, LayoutArea layout, float width, float height, int color1, int color2, int color3, String param) {
        super(iwin, null, id, layout, width, height, color1, color2, color3);
        this.owner = this;
        this.typeElem = TypeElem.TRIANGL;
        parsing(param);
    }

    @Override
    public void joinFrame() {
        System.out.println("Функция не определена");
    }
}
