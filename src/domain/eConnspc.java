package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Record;

public enum eConnspc implements Field {
    up("0", "0", "0", "Спецификация вариантов соединения", "CONNSPC"),
    id("4", "10", "0", "Идентификатор", "id"),
    cunic("4", "10", "1", "null", "CUNIC"),
    aunic("4", "10", "1", "null", "AUNIC"),
    anumb("12", "32", "1", "артикул", "ANUMB"),
    clnum("4", "10", "1", "null", "CLNUM"),
    ctype("5", "5", "1", "null", "CTYPE");
    private MetaField meta = new MetaField(this);

    eConnspc(Object... p) {
        meta.init(p);
    }

    public MetaField meta() {
        return meta;
    }

    public Field[] fields() {
        return values();
    }

    

    public String toString() {
        return meta.getColName();
    }
}
