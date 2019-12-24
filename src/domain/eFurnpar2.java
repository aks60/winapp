package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Record;

public enum eFurnpar2 implements Field {
    up("0", "0", "0", "Параметры спецификаций фурнитуры", "PARFURS"),
    id("4", "10", "0", "Идентификатор", "id"),
    vtext("12", "64", "1", "null", "PTEXT"),
    pnumb_id("4", "10", "1", "null", "PNUMB"),
    furndet_id("12", "64", "1", "ссылка", "PTEXT");
    //psss("4", "10", "1", "null", "PSSS"),
    //pporn("5", "5", "1", "null", "PPORN"),
    //znumb("4", "10", "1", "null", "ZNUMB"),
    //punic("4", "10", "1", "null", "PUNIC"),
    
    private MetaField meta = new MetaField(this);

    eFurnpar2(Object... p) {
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
