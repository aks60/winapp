package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Record;

public enum eVstaspc implements Field {
    up("0", "0", "0", "Спецификация составов", "VSTASPC"),
    id("4", "10", "0", "Идентификатор", "id"),
    aunic("4", "10", "1", "ИД компонента", "AUNIC"),
    vnumb("4", "10", "1", "ИД состава", "VNUMB"),
    anumb("12", "32", "1", "название компонента", "ANUMB"),
    clnum("4", "10", "1", "текстура", "CLNUM"),
    ctype("5", "5", "1", "Тип подбора 0 - указана вручную 11 - профиль 31 - основная", "CTYPE");
    private MetaField meta = new MetaField(this);

    eVstaspc(Object... p) {
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
