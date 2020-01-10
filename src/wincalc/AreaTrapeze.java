package wincalc;

import enums.eLayoutArea;
import enums.eTypeElem;

public class AreaTrapeze extends AreaBase {

    public AreaTrapeze(Wincalc iwin, String id, eLayoutArea layout, float width, float height, int color1, int color2, int color3, String paramJson) {
        super(null, id, layout, width, height, color1, color2, color3);
        this.iwin = iwin;
        parsingParamJson(this, paramJson);
    }
    
    @Override
    public eTypeElem getTypeArea() {
        return eTypeElem.TRAPEZE;
    }    
}
