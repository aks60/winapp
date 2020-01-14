package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;

public enum eGlasgrp implements Field {
    up("0", "0", "0", "Группы заполнения", "GLASGRP"),
    id("4", "10", "0", "Идентификатор", "id"),
    name("12", "64", "1", "Название группы", "GNAME"),
    gap("8", "15", "1", "Зазор", "GZAZO"),
    thick("12", "128", "1", "Доступные толщины", "BFRIC"),
    artikl_id("4", "10", "0", "Ссылка", "artikl_id");
    //gnumb("4", "10", "1", "id группы", "GNUMB"),    
    //gdiff("8", "15", "1", "null", "GDIFF"),
    //pnump("5", "5", "1", "null", "PNUMP");
        private MetaField meta = new MetaField(this);
    public static Query q = new Query(values()).table(up.tname());

    eGlasgrp(Object... p) {
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
