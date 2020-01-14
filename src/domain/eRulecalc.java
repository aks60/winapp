package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;

public enum eRulecalc implements Field {
    up("0", "0", "0", "Правила расчёта проектов", "RULECLK"),
    id("4", "10", "0", "Идентификатор", "id"),
    rnumb("4", "10", "1", "null", "RNUMB"),
    runic("4", "10", "1", "null", "RUNIC"),
    name("12", "64", "1", "Название правила", "RNAME"),
    artikl("12", "32", "1", "Артикул", "ANUMB"),
    type("5", "5", "1", "Тип материальных ценностей (502 - заполнение)", "RUSED"),
    coeff("8", "15", "1", "Коэффициент", "RKOEF"),
    quant("12", "96", "1", "Количество", "RLENG"),
    rcodm("12", "96", "1", "Коды текстур позиции (основная)", "RCODM"),
    rcod1("12", "96", "1", "Коды текстур позиции (??? внутренняя)", "RCOD1"),
    rcod2("12", "96", "1", "Коды текстур позиции (??? внешняя)", "RCOD2"),
    rallp("5", "5", "1", "Общее", "RALLP"),
    riskl("5", "5", "1", "Для формы позиций  0 - не проверять форму,  10 - не прямоугольное, не арочное заполнение,  12 - не прямоугольное заполнение с арками", "RISKL"),
    rsize("12", "96", "1", "null", "RSIZE"),
    rpric("8", "15", "1", "Надбавка", "RPRIC"),
    rsebe("5", "5", "1", "Себестоимость", "RSEBE"),
    rcalk("5", "5", "1", "null", "RCALK");
        private MetaField meta = new MetaField(this);
    public static Query query = new Query(values()).table(up.tname());

    eRulecalc(Object... p) {
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
