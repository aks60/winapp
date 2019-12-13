package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Record;

public enum eGlasgrp implements Field {
    up("0", "0", "0", "Группы заполнения", "GLASGRP"),
    id("4", "10", "0", "Идентификатор", "id"),
    gname("12", "64", "1", "Название группы", "GNAME"),
    gnumb("4", "10", "1", "id группы", "GNUMB"),
    gzazo("8", "15", "1", "Зазор", "GZAZO"),
    bfric("12", "128", "1", "Доступные толщины", "BFRIC"),
    gdiff("8", "15", "1", "null", "GDIFF"),
    pnump("5", "5", "1", "null", "PNUMP");
    private MetaField meta = new MetaField(this);

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
