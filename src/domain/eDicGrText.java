package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Record;

public enum eDicGrText implements Field {
    up("0", "0", "0", "Группа текстур", "GRUPCOL"),
    id("4", "10", "0", "Идентификатор", "id"),
    gnumb("4", "10", "1", "номер группы", "GNUMB"),
    gname("12", "32", "1", "название группы", "GNAME"),
    gunic("4", "10", "1", "null", "GUNIC"),
    gkoef("8", "15", "1", "ценовой коэффицент", "GKOEF"),
    gprc1("8", "15", "1", "null", "GPRC1"),
    gprc2("8", "15", "1", "null", "GPRC2");
    private MetaField meta = new MetaField(this);

    eDicGrText(Object... p) {
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
