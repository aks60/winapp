package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;

public enum eElemgrp implements Field {
    up("0", "0", "0", "Категории составов", "EMPTY"),
    id("4", "10", "0", "Идентификатор", "id"),
    name("12", "32", "1", "Категория", "VPREF"),
    level("5", "5", "1", "Тип артикула", "ATYPM");

    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values());

    eElemgrp(Object... p) {
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
