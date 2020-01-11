package wincalc;

import enums.eLayoutArea;
import enums.eTypeElem;

public class AreaScene extends AreaBase {

    public AreaScene(Wincalc iwin, AreaBase owner, String id, eLayoutArea layout, float width, float height) {
        super(iwin, owner, id, layout, width, height);
    }

    @Override
    public eTypeElem typeElem() {
        return eTypeElem.AREA;
    }

    @Override
    public void passJoin() {
        System.out.println("Функция не определена");
    }
}
