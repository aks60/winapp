package domain;

import dataset.Field;
import static dataset.Field.conf;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import static domain.eCurrenc.id;
import static domain.eCurrenc.up;
import static domain.eCurrenc.values;

public enum eColgrp implements Field {
    up("0", "0", "0", "Группа текстур", "GRUPCOL"),
    id("4", "10", "0", "Идентификатор", "GNUMB"),
    name("12", "32", "1", "Название группы", "GNAME"),
    coeff("8", "15", "1", "Ценовой коэффицент", "GKOEF");
    //groups("4", "10", "1", "номер группы", "GNUMB"),
    //gunic("4", "10", "1", "null", "GUNIC"),
    //gprc1("8", "15", "1", "null", "GPRC1"),
    //gprc2("8", "15", "1", "null", "GPRC2");
    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values());

    eColgrp(Object... p) {
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

    public static Record find(int _id) {
        if (conf.equals("cal")) {
            return query().stream().filter(rec -> rec.getInt(id) == _id).findFirst().orElse(up.newRecord());
        }
        Query recordList = new Query(values()).select(up, "where", id, "=", _id);
        return (recordList.isEmpty() == true) ? up.newRecord() : recordList.get(0);
    }

    public String toString() {
        return meta.descr();
    }
}
