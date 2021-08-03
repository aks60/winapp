package builder.model;

import enums.Layout;
import enums.TypeElem;
import builder.Wincalc;

public class AreaTrapeze extends AreaSimple {

    public AreaTrapeze(Wincalc iwin, AreaSimple owner, float id, TypeElem typeElem, Layout layout, float width, float height, int color1, int color2, int color3, String param) {
        super(iwin, owner, id, typeElem, layout, width, height, color1, color2, color3, param);
    }

    @Override
    public void joinFrame() {
        System.out.println("Ркализация не определена");
    }
}
