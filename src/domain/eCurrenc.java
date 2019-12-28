package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Record;

public enum eCurrenc implements Field {
    up("0", "0", "0", "Валюта", "CORRENC"),
    id("4", "10", "0", "Идентификатор", "id"),
    name("12", "32", "1", "Название валюты", "CNAME"),
    par_case1("12", "32", "1", "Родит.падеж ед.ч.", "CRODE"),
    par_case3("12", "32", "1", "Родит.падеж мн.ч.", "CRODM"),
    design("12", "8", "1", "Обозначение", "CSHOR"),
    precis("5", "5", "1", "Точность", "CSIZE"),
    cross_cour("8", "15", "1", "Кросс курс", "CKURS"),
    check1("5", "5", "1", "Флаг  Основная ", "CSETS"),
    check2("5", "5", "1", "Флаг  Внутренняя ", "CINTO");
    //cnumb("4", "10", "1", "ID валюты", "CNUMB"),
    private MetaField meta = new MetaField(this);

    eCurrenc(Object... p) {
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
