package wincalc.model;

import enums.LayoutArea;
import enums.TypeElem;
import wincalc.Wincalc;

public class AreaTriangl extends AreaContainer {

    public AreaTriangl(Wincalc iwin, String id, LayoutArea layout, float width, float height, int color1, int color2, int color3, String param) {
        super(null, id, layout, width, height, color1, color2, color3);
        this.iwin = iwin;
        parsing(param);
    }

    @Override
    public TypeElem typeElem() {
        return TypeElem.TRIANGL;
    }

    @Override
    public void joinRama() {
        System.out.println("Функция не определена");
    }
}
