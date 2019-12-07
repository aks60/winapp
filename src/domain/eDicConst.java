package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Record;

public enum eDicConst implements Field {
    up("0", "0", "0", "Системные константы", "SYSSIZE"),
    id("4", "10", "0", "Идентификатор", "id"),
    sunic("4", "10", "1", "ID системы", "SUNIC"),
    sname("12", "32", "1", "Система артикулов", "SNAME"),
    ssizp("8", "15", "1", "Припуск на сварку", "SSIZP"),
    ssizn("8", "15", "1", "Наплав системы", "SSIZN"),
    ssizf("8", "15", "1", "Нахлест створки", "SSIZF"),
    ssizi("8", "15", "1", "Заход импоста", "SSIZI");
    private MetaField meta = new MetaField(this);

    eDicConst(Object... p) {
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
