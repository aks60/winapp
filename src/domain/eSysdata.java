package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import java.util.List;

public enum eSysdata implements Field {
    up("0", "0", "0", "Расч. данные настройки", "SYSDATA"),
    id("4", "10", "0", "Идентификатор", "SUNIC"), 
    grup("5", "5", "0", "Группа", "grup"),
    npp("4", "10", "1", "Ном.п.п", "npp"),
    name("12", "64", "1", "Наименование", "SNAME"),
    txt("12", "32", "1", "Значение текст", "STEXT"),
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
            Query.listOpenTable.add(query);
        }
        return query;
    }

    public static List<Record> get() {
        if (Query.conf.equals("calc")) {
            return query();
        }
        return new Query(values()).select(up, "order by", id);
    }
    
    public static Record find(int _id) {
        if (Query.conf.equals("calc")) {
            return query().stream().filter(rec -> _id == rec.getInt(id)).findFirst().orElse(up.newRecord());
        }
        Query recordList = new Query(values()).select(up, "where", id, "=", _id);
        return (recordList.isEmpty() == true) ? up.newRecord() : recordList.get(0);
    }
    
    public String toString() {
        return meta.descr();
    }
}
