package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;

public enum eGlasdet implements Field {
    up("0", "0", "0", "Спецификация групп заполнения", "GLASART"),
    id("4", "10", "0", "Идентификатор", "id"),
    types("5", "5", "1", "Подбор текстуры", "CTYPE"),
    depth("8", "15", "1", "Толщина", "AFRIC"),
    color_id("4", "10", "0", "Ссылка", "color_id"),
    glasgrp_id("4", "10", "0", "Ссылка", "glassgrp_id"),
    artikl_id("4", "10", "0", "Ссылка", "artikl_id");
    //gnumb("4", "10", "1", "ID группы заполнения", "GNUMB"),
    //gunic("4", "10", "1", "null", "GUNIC"), 
    //anumb("12", "32", "1", "Артикул элемента", "ANUMB"),
    //clnum("4", "10", "1", "Текстура", "CLNUM"),
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
