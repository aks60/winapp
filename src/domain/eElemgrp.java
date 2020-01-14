package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;

public enum eElemgrp implements Field {
    up("0", "0", "0", "Категории составов", "EMPTY"),
    id("4", "10", "0", "Идентификатор", "id"),
    name("12", "32", "1", "категория", "VPREF"),
    level("5", "5", "1", "тип артикула", "ATYPM");
    
        private MetaField meta = new MetaField(this);
    public static Query q = new Query(values()).table(up.tname());

    eElemgrp(Object... p) {
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
