package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Record;

public enum eFurnPar1 implements Field {
    up("0", "0", "0", "Параметры ограничений сторон фурнитуры", "PARFURL"),
    id("4", "10", "0", "Идентификатор", "id"),
    psss("4", "10", "1", "null", "PSSS"),
    pporn("5", "5", "1", "null", "PPORN"),
    pnumb("4", "10", "1", "null", "PNUMB"),
    znumb("4", "10", "1", "null", "ZNUMB"),
    punic("4", "10", "1", "null", "PUNIC"),
    ptext("12", "64", "1", "null", "PTEXT");
    private MetaField meta = new MetaField(this);

    eFurnPar1(Object... p) {
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
