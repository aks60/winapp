package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;

public enum eSysdata implements Field {
    up("0", "0", "0", "Расчетные данные настройки", "SYSDATA"),
    id("4", "10", "0", "Идентификатор", "id"),
    unic("4", "10", "1", "Уник. номер", "SUNIC"),
    name("12", "64", "1", "Наименование", "SNAME"),
    text("12", "128", "1", "null", "STEXT"),
    val("8", "15", "1", "Значение параметра", "SFLOT");
    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values()).table(up.tname());

    eSysdata(Object... p) {
        meta.init(p);
    }

    public MetaField meta() {
        return meta;
    }

    public Field[] fields() {
        return values();
    }

        public static Query query() {
        if (query.size() == 0) {
            query.select(up, "order by", id);
        }
        return query;
    }

    public String toString() {
        return meta.descr();
    }
}
