package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;

public enum eSysprod implements Field {
    up("0", "0", "0", "Cписок типовых изделий по проф. системам", "EMPTY"),
    id("4", "10", "0", "Идентификатор", "id"),
    name("12", "64", "1", "Название типового изделия", "ONAME"),
    json("12", "1024", "0", "Скрипт построения окна", "json");

    private MetaField meta = new MetaField(this);
    public static Query query = new Query(values()).table(up.tname());

    eSysprod(Object... p) {
        meta.init(p);
    }

    public MetaField meta() {
        return meta;
    }

    public Field[] fields() {
        return values();
    }

    @Override
    public Query selectSql() {
        if (query.size() == 0) {
            query.select(up, "order by", id);
        }
        return query;
    }

    public String toString() {
        return meta.getDescr();
    }
}
