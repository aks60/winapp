package wincalc;

import enums.eLayoutArea;
import enums.eTypeElem;

public class AreaScene extends AreaBase {

    public AreaScene(Wincalc iwin, AreaBase root, AreaBase owner, String id, eLayoutArea layout, float width, float height) {
        super(iwin, root, owner, id, layout, width, height);
    }

    @Override
    public eTypeElem getTypeElem() {
        return eTypeElem.AREA;
    }
}
