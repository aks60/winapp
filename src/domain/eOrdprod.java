package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;

public enum eOrdprod implements Field {
    up("0", "0", "0", "Изделия заказов", "EMPTY"),
    id("4", "10", "0", "Идентификатор", "id"),
    npp("5", "5", "1", "Номе п/п", "npp"),
    name("12", "128", "1", "Название изделия", "name"),
    script("12", "2048", "0", "Скрипт построения окна", "script"),
    order_id("4", "10", "1", "Заказ", "order_id"),
    form("4", "10", "0", "Тип", "form");

    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values());

    eOrdprod(Object... p) {
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
            Query.listOpenTable.add(query);
        }
        return query;
    }

    public static Record find(int _id) {
        if (Query.conf.equals("calc")) {
            return query().stream().filter(rec -> _id == rec.getInt(id)).findFirst().orElse(up.newRecord());
        }
        Query recordList = new Query(values()).select(up, "where", id, "=", _id);
        return (recordList.isEmpty() == true) ? up.newRecord() : recordList.get(0);
    }

    public String toString() {
        return meta.descr();
    }
}

