package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import java.util.List;

public enum eRulecalc implements Field {
    up("0", "0", "0", "Правила расчёта проектов", "RULECLK"),
    id("4", "10", "0", "Идентификатор", "id"),
    numb("4", "10", "1", "Номер", "RNUMB"),
    unic("4", "10", "1", "Идентификатор 2", "RUNIC"),
    name("12", "64", "1", "Название правила", "RNAME"),
    artikl("12", "32", "1", "Артикул", "ANUMB"),
    type("5", "5", "1", "Тип материальных ценностей (502 - заполнение)", "RUSED"),
    coeff("8", "15", "1", "Коэффициент", "RKOEF"),
    quant("12", "96", "1", "Количество", "RLENG"),
    rcodm("12", "96", "1", "Коды текстур позиции (основная)", "RCODM"),
    code1("12", "96", "1", "Коды текстур позиции (??? внутренняя)", "RCOD1"),
    code2("12", "96", "1", "Коды текстур позиции (??? внешняя)", "RCOD2"),
    common("5", "5", "1", "Общее", "RALLP"),
    form("5", "5", "1", "Форма позиций", "RISKL"), //0 - не проверять форму,  10 - не прямоугольное, не арочное заполнение,  12 - не прямоугольное заполнение с арками"
    size("12", "96", "1", "Размер", "RSIZE"),
    rpric("8", "15", "1", "Надбавка", "RPRIC"),
    sebes("5", "5", "1", "Себестоимость", "RSEBE"),
    calk("5", "5", "1", "Калькуляция", "RCALK");
    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values());

    eRulecalc(Object... p) {
        meta.init(p);
    }

    public MetaField meta() {
        return meta;
    }

    public Field[] fields() {
        return values();
    }

    public static Query query() {
        if (query.size() == 0) {
            query.select(up, "order by", id);
        }
        return query;
    }

    public static List<Record> get() {
        if (conf.equals("calc")) {
            return query();
        }
        Query recordList = new Query(values()).select(up, "order by", id);
        return (recordList.isEmpty() == true) ? up.newRecord() : recordList.get(0);
    }

    public String toString() {
        return meta.descr();
    }
}
