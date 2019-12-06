package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Record;

public enum eGlasart implements Field {
    up("0", "0", "0", "Спецификация групп заполнения", "GLASART"),
    id("4", "10", "0", "Идентификатор", "id"),
    gnumb("4", "10", "1", "ID группы заполнения", "GNUMB"),
    gunic("4", "10", "1", "null", "GUNIC"),
    afric("8", "15", "1", "Толщина", "AFRIC"),
    anumb("12", "32", "1", "Артикул элемента", "ANUMB"),
    clnum("4", "10", "1", "Текстура", "CLNUM"),
    ctype("5", "5", "1", "null", "CTYPE");
    private MetaField meta = new MetaField(this);

    eGlasart(Object... p) {
        meta.init(p);
    }

    public MetaField meta() {
        return meta;
    }

    public Field[] fields() {
        return values();
    }

    

    public String toString() {
        return meta.getColName();
    }
}
