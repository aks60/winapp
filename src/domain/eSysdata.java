package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;

public enum eSysdata implements Field {
    up("0", "0", "0", "Расчетные данные настройки", "SYSDATA"),
    id("4", "10", "0", "Идентификатор", "id"),
    sunic("4", "10", "1", "null", "SUNIC"),
    sname("12", "64", "1", "наименование", "SNAME"),
    stext("12", "128", "1", "null", "STEXT"),
    sflot("8", "15", "1", "значение параметра", "SFLOT");
        private MetaField meta = new MetaField(this);
    public static Query query = new Query(values()).table(up.tname());

    eSysdata(Object... p) {
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
