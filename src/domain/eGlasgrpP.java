package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Record;
import static domain.eGlasartP.values;

public enum eGlasgrpP implements Field {
    up("0", "0", "0", "Параметры групп заполнения", "PARGRUP"),
    id("4", "10", "0", "Идентификатор", "id"),
    vnumb("4", "10", "1", "номер параметра", "PNUMB"),
    vtext("12", "64", "1", "наименование значения параметра", "PTEXT");
    //psss("4", "10", "1", "null", "PSSS"),
    //pporn("5", "5", "1", "null", "PPORN"),
    //znumb("4", "10", "1", "значение параметра", "ZNUMB"),
    //punic("4", "10", "1", "null", "PUNIC"),
    
    private MetaField meta = new MetaField(this);

    eGlasgrpP(Object... p) {
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
