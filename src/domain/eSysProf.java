package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Record;

public enum eSysProf implements Field {
    up("0", "0", "0", "Профили, системы профилей", "SYSPROA"),
    id("4", "10", "0", "Идентификатор", "id"),
    aunic("4", "10", "1", "ИД компонента", "AUNIC"),
    nuni("4", "10", "1", "ID  серии профилей", "NUNI"),
    aprio("5", "5", "1", " приоритет 0 - 1 -  2 -  3 -  4 -  5 -  6 - 10 -  1000 - ", "APRIO"),
    anumb("12", "32", "1", "артикул", "ANUMB"),
    asets("5", "5", "1", " сторона 1 -  нижняя 0 - вручную -1 - любая - 2 - горизонтальная  - 3 - вертикальная ", "ASETS"),
    atype("5", "5", "1", " тип профиля 1 - рама 2 - створка 3 - импост 5 - стойка 6- поперечина 7 - раскладка 9 - штульп ", "ATYPE"),
    cflag("5", "5", "1", "Свои текстуры", "CFLAG");
    private MetaField meta = new MetaField(this);

    eSysProf(Object... p) {
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
