package wincalc;

import enums.eTypeOpen;

public class AreaStvorka extends AreaBase {

    public AreaStvorka(String id) {
        super(id);
    }

    public eTypeOpen getTypeOpen() {
        return eTypeOpen.OM_INVALID;
    }
}
