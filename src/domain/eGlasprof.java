package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;

public enum eGlasprof implements Field {
    up("0", "0", "0", "Профили в группе заполнения", "GLASPRO"),
    id("4", "10", "0", "Идентификатор", "id"),     
    types("5", "5", "1", "Системные константы (7 - привязка установлена, 3 - привязка отсутствует)", "GTYPE"),
    sizeax("8", "15", "1", "Размер от оси, мм", "ASIZE"),
    glasgrp_id("4", "10", "0", "Ссылка", "glasgrp_id"),
    artikl_id("4", "10", "0", "Ссылка", "artikl_id");
    //zunic("4", "10", "1", "null", "ZUNIC"),
    //anumb("12", "32", "1", "Артикул", "ANUMB"),
    //gnumb("4", "10", "1", "ID группы", "GNUMB"),
        private MetaField meta = new MetaField(this);
    public static Query query = new Query(values()).table(up.tname());

    eGlasprof(Object... p) {
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
