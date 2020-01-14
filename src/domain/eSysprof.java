package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;

public enum eSysprof implements Field {
    up("0", "0", "0", "Профили, системы профилей", "SYSPROA"),
    id("4", "10", "0", "Идентификатор", "id"),
    prio("5", "5", "1", " приоритет 0 - 1 -  2 -  3 -  4 -  5 -  6 - 10 -  1000 - ", "APRIO"),
    size("5", "5", "1", " сторона 1 -  нижняя 0 - вручную -1 - любая - 2 - горизонтальная  - 3 - вертикальная ", "ASETS"),
    types("5", "5", "1", " тип профиля 1 - рама 2 - створка 3 - импост 5 - стойка 6- поперечина 7 - раскладка 9 - штульп ", "ATYPE"),
    artikl_id("4", "10", "0", "Ссылка", "artikl_id"),
    systree_id("4", "10", "0", "Ссылка", "systree_id");
    //aunic("4", "10", "1", "ИД компонента", "AUNIC"),
    //nuni("4", "10", "1", "ID  серии профилей", "NUNI"),    
    //anumb("12", "32", "1", "артикул", "ANUMB"),
    //cflag("5", "5", "1", "Свои текстуры", "CFLAG");
        private MetaField meta = new MetaField(this);
    public static Query query = new Query(values()).table(up.tname());

    eSysprof(Object... p) {
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
