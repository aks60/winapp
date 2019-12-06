package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Record;

public enum eCorrenc implements Field {
    up("0", "0", "0", "Валюта", "CORRENC"),
    id("4", "10", "0", "Идентификатор", "id"),
    cname("12", "32", "1", "Название валюты", "CNAME"),
    cnumb("4", "10", "1", "ID валюты", "CNUMB"),
    crode("12", "32", "1", "Родит.падеж ед.ч.", "CRODE"),
    crodm("12", "32", "1", "Родит.падеж мн.ч.", "CRODM"),
    cshor("12", "8", "1", "Обозначение", "CSHOR"),
    csize("5", "5", "1", "Точность", "CSIZE"),
    ckurs("8", "15", "1", "Кросс курс", "CKURS"),
    csets("5", "5", "1", "Флаг  Основная ", "CSETS"),
    cinto("5", "5", "1", "Флаг  Внутренняя ", "CINTO");
    private MetaField meta = new MetaField(this);

    eCorrenc(Object... p) {
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
