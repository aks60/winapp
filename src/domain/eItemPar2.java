package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Record;

public enum eItemPar2 implements Field {
    up("0", "0", "0", "Параметры спецификаций составов", "PARVSTS"),
    id("4", "10", "0", "Идентификатор", "id"),
    punic("4", "10", "1", "PK*", "PUNIC"),
    psss("4", "10", "1", "ИД компонента состава", "PSSS"),
    pporn("5", "5", "1", "номер параметра в таблицах ввода параметров в программу. Присваивается автоматически, возможно поменять вручную. В рамках одного группы одноименных номеров быть не может. Возможно менять вручную если такой номер не занят", "PPORN"),
    pnumb("4", "10", "1", "номер параметра", "PNUMB"),
    znumb("4", "10", "1", "null", "ZNUMB"),
    ptext("12", "64", "1", "наименование значения параметра", "PTEXT");
    private MetaField meta = new MetaField(this);

    eItemPar2(Object... p) {
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
