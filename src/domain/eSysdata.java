package domain;

import dataset.Field;
import static dataset.Field.conf;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import static domain.eSystree.id;
import static domain.eSystree.record;
import static domain.eSystree.up;
import static domain.eSystree.values;
import java.util.List;

public enum eSysdata implements Field {
    up("0", "0", "0", "Расч. данные настройки", "SYSDATA"),
    id("4", "10", "0", "Идентификатор", "SUNIC"),    
    name("12", "64", "1", "Наименование", "SNAME"),
    text("12", "128", "1", "Значение техт", "STEXT"),
    val("8", "15", "1", "Значение цыфра", "SFLOT");
    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values());

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

    public static List<Record> get() {
        if (conf.equals("cal")) {
            return query();
        }
        return new Query(values()).select(up, "order by", id);
    }
    
    public static Record find(int _id) {
        if (conf.equals("cal")) {
            return query().stream().filter(rec -> _id == rec.getInt(id)).findFirst().orElse(up.newRecord());
        }
        Query recordList = new Query(values()).select(up, "where", id, "=", _id);
        return (recordList.isEmpty() == true) ? up.newRecord() : recordList.get(0);
    }
    
    public String toString() {
        return meta.descr();
    }
}
