package estimate.model;

import enums.LayoutArea;
import enums.TypeElem;
import estimate.Wincalc;

public class AreaTriangl extends AreaSimple {

    public AreaTriangl(Wincalc iwin, AreaSimple owner, float id, TypeElem typeElem, LayoutArea layout, float width, float height, int color1, int color2, int color3, String param) {
        super(iwin, owner, id, typeElem, layout, width, height, color1, color2, color3);
        parsing(param);
    }

    @Override
    public void joinFrame() {
        System.out.println("Ркализация не определена");
    }
}
