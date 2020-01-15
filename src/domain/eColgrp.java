package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;

public enum eColgrp implements Field {
    up("0", "0", "0", "Группа текстур", "GRUPCOL"),
    id("4", "10", "0", "Идентификатор", "id"),
    //groups("4", "10", "1", "номер группы", "GNUMB"),
    name("12", "32", "1", "название группы", "GNAME"),
    //gunic("4", "10", "1", "null", "GUNIC"),
    coeff("8", "15", "1", "ценовой коэффицент", "GKOEF");
    //gprc1("8", "15", "1", "null", "GPRC1"),
    //gprc2("8", "15", "1", "null", "GPRC2");
    private MetaField meta = new MetaField(this);
    public static Query query = new Query(values()).table(up.tname());

    eColgrp(Object... p) {
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
