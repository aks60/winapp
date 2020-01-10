package wincalc;

import enums.eLayoutArea;
import enums.eTypeElem;
import enums.eTypeOpen;

public class AreaStvorka extends AreaBase {

    public AreaStvorka(Wincalc iwin, String id, eLayoutArea layout, float width, float height, int color1, int color2, int color3, String paramJson) {
        super(null, id, layout, width, height, color1, color2, color3);
        this.iwin = iwin;
        parsingParamJson(this, paramJson);
    }

    public eTypeOpen getTypeOpen() {
        return eTypeOpen.OM_INVALID;
    }
    
    @Override
    public eTypeElem getTypeArea() {
        return eTypeElem.STVORKA;
    }    
}
