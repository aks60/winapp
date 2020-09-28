package domain;

import dataset.Field;
import static dataset.Field.conf;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import static domain.eColor.id;
import static domain.eColor.record;
import static domain.eColor.up;
import static domain.eColor.values;

public enum eCurrenc implements Field {
    up("0", "0", "0", "Валюта", "CORRENC"),
    id("4", "10", "0", "Идентификатор", "cnumb"),
    name("12", "32", "1", "Название валюты", "CNAME"),
    par_case1("12", "32", "1", "Родит.падеж ед.ч.", "CRODE"),
    par_case3("12", "32", "1", "Родит.падеж мн.ч.", "CRODM"),
    design("12", "8", "1", "Обозначение", "CSHOR"),
    precis("5", "5", "1", "Точность", "CSIZE"),
    cross_cour("8", "15", "1", "Кросс курс", "CKURS"),
    check1("5", "5", "1", "Основная ", "CSETS"),
    check2("5", "5", "1", "Внутренняя ", "CINTO");
    //cnumb("4", "10", "1", "ID валюты", "CNUMB"),
    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values());

    eCurrenc(Object... p) {
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

    public static Record find(int _id) {
        if (conf.equals("calc")) {
            return query().stream().filter(rec -> rec.getInt(id) == _id).findFirst().orElse(up.newRecord());
        }
        Query recordList = new Query(values()).select(up, "where", id, "=", _id);
        return (recordList.isEmpty() == true) ? up.newRecord() : recordList.get(0);
    }
    
    public String toString() {
        return meta.descr();
    }
}
