package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Record;

public enum eSysfurn implements Field {
    up("0", "0", "0", "Фурнитуры системы профилей", "SYSPROS"),
    id("4", "10", "0", "Идентификатор", "id"),
    nuni("4", "10", "1", "ИД серии профилей", "NUNI"),
    funic("4", "10", "1", "ИД фурнитурного набора", "FUNIC"),
    fporn("5", "5", "1", "null", "FPORN"),
    fwhat("5", "5", "1", "Замена", "FWHAT"),
    notkr("12", "8", "1", "сторона открывания по умолчанию", "NOTKR"),
    nruch("12", "32", "1", "расположение ручки по умолчанию", "NRUCH"),
    aruch("12", "32", "1", "Артикул ручки по умолчанию", "ARUCH"),
    apetl("12", "32", "1", "Артиккул подвеса (петли) по умолчанию", "APETL");
    private MetaField meta = new MetaField(this);

    eSysfurn(Object... p) {
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
