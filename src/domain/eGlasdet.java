package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;

public enum eGlasdet implements Field {
    up("0", "0", "0", "Спецификация групп заполнения", "GLASART"),
    id("4", "10", "0", "Идентификатор", "id"),
    depth("8", "15", "1", "Толщина", "AFRIC"),
    artikl_id("4", "10", "0", "Ссылка", "artikl_id"),
    textyre_id("4", "10", "0", "Ссылка", "textyre_id"),
    glasgrp_id("4", "10", "0", "Ссылка", "glassgrp_id");
    //gnumb("4", "10", "1", "ID группы заполнения", "GNUMB"),
    //gunic("4", "10", "1", "null", "GUNIC"), 
    //anumb("12", "32", "1", "Артикул элемента", "ANUMB"),
    //clnum("4", "10", "1", "Текстура", "CLNUM"),
    //ctype("5", "5", "1", "null", "CTYPE"),    
        private MetaField meta = new MetaField(this);
    public static Query query = new Query(values()).table(up.tname());

    eGlasdet(Object... p) {
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
