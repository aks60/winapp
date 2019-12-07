package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Record;

public enum eGlasProf implements Field {
    up("0", "0", "0", "Профили в группе заполнения", "GLASPRO"),
    id("4", "10", "0", "Идентификатор", "id"),
    anumb("12", "32", "1", "Артикул", "ANUMB"),
    zunic("4", "10", "1", "null", "ZUNIC"),
    gnumb("4", "10", "1", "ID группы", "GNUMB"),
    gtype("5", "5", "1", "Системные константы (7 - привязка установлена, 3 - привязка отсутствует)", "GTYPE"),
    asize("8", "15", "1", "Размер от оси, мм", "ASIZE");
    private MetaField meta = new MetaField(this);

    eGlasProf(Object... p) {
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
