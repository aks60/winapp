package wincalc.model;

import enums.LayoutArea;
import enums.TypeElem;
import wincalc.Wincalc;

public class AreaScene extends AreaBase {

    public AreaScene(Wincalc iwin, AreaBase owner, String id, LayoutArea layout, float width, float height) {
        super(iwin, owner, id, layout, width, height);
    }

    @Override
    public TypeElem typeElem() {
        return TypeElem.AREA;
    }

    @Override
    public void joinRama() {
        System.out.println("Функция не определена");
    }
}
