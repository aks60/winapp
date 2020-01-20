package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;

public enum eSyssize implements Field {
    up("0", "0", "0", "Системные константы", "SYSSIZE"),
    id("4", "10", "0", "Идентификатор", "id"),    
    name("12", "32", "1", "Система артикулов", "SNAME"),
    prip("8", "15", "1", "Припуск на сварку", "SSIZP"),
    napl("8", "15", "1", "Наплав системы", "SSIZN"),
    naxl("8", "15", "1", "Нахлест створки", "SSIZF"),
    zax("8", "15", "1", "Заход импоста", "SSIZI");
    //sunic("4", "10", "1", "ID системы", "SUNIC"),
    private MetaField meta = new MetaField(this);
    public static Query query = new Query(values()).table(up.tname());

    eSyssize(Object... p) {
        meta.init(p);
    }

    public MetaField meta() {
        return meta;
    }

    public Field[] fields() {
        return values();
    }

    @Override
    public Query selectSql() {
        if (query.size() == 0) {
            query.select(up, "order by", id);
        }
        return query;
    }

    public String toString() {
        return meta.getDescr();
    }
}
