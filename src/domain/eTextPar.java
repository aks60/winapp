package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Record;

public enum eTextPar implements Field {
    up("0", "0", "0", "Парметры текстур", "PARCOLS"),
    id("4", "10", "0", "Идентификатор", "id"),
    //psss("4", "10", "1", "null", "PSSS"),
    //pporn("5", "5", "1", "null", "PPORN"),
    //pnumb("4", "10", "1", "номер параметра", "PNUMB"),
    //znumb("4", "10", "1", "null", "ZNUMB"),
    //punic("4", "10", "1", "null", "PUNIC"),
    ptext("12", "64", "1", "наименование значения параметра", "PTEXT"),
    param_id("4", "10", "1", "номер параметра", "eDicParam");
    private MetaField meta = new MetaField(this);

    eTextPar(Object... p) {
        meta.init(p);
    }

    public MetaField meta() {
        return meta;
    }

    public Field[] fields() {
        return values();
    }

    

    public String toString() {
        return meta.getDescr();
    }
}
