package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Record;

public enum eParglas implements Field {
    up("0", "0", "0", "Параметры спецификаций групп заполнения", "PARGLAS"),
    id("4", "10", "0", "Идентификатор", "id"),
    psss("4", "10", "1", "null", "PSSS"),
    pporn("5", "5", "1", "null", "PPORN"),
    pnumb("4", "10", "1", "номер параметра", "PNUMB"),
    znumb("4", "10", "1", "значение параметра", "ZNUMB"),
    punic("4", "10", "1", "null", "PUNIC"),
    ptext("12", "64", "1", "наименование значения параметра", "PTEXT");
    private MetaField meta = new MetaField(this);

    eParglas(Object... p) {
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
