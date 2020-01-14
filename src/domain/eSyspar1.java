package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;

public enum eSyspar1 implements Field {
    up("0", "0", "0", "Парамметры системы профилей", "PARSYSP"),
    id("4", "10", "0", "Идентификатор", "id"),
    numb("4", "10", "1", "номер параметра", "PNUMB"),
    val("12", "64", "1", "наименование значения параметра", "PTEXT"),
    fixed("5", "5", "1", "закреплено", "PFIXX"),
    systree_id("4", "10", "1", "ссылка", "systree_id");
    //psss("4", "10", "1", "ИД серии профилей", "PSSS"),
    //pporn("5", "5", "1", "номер параметра в таблицах ввода параметров в программу. Присваивается автоматически, возможно поменять вручную.  В рамках одного группы одноименных номеров быть не может.  Возможно менять вручную если такой номер не занят", "PPORN"),    
    //znumb("4", "10", "1", "значение параметра", "ZNUMB"),
    //punic("4", "10", "1", "ID параметра", "PUNIC"),
    //pfixx("5", "5", "1", "закреплено", "PFIXX");
        private MetaField meta = new MetaField(this);
    public static Query query = new Query(values()).table(up.tname());

    eSyspar1(Object... p) {
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
