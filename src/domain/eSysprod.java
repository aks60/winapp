package domain;

import dataset.Field;
import static dataset.Field.conf;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import static domain.eSystree.id;
import static domain.eSystree.up;
import static domain.eSystree.values;

public enum eSysprod implements Field {
    up("0", "0", "0", "Cписок типовых изделий по проф. системам", "EMPTY"),
    id("4", "10", "0", "Идентификатор", "id"),
    npp("4", "10", "0", "Номе п/п", "npp"),
    name("12", "64", "1", "Название типового изделия", "ONAME"),
    script("12", "2048", "0", "Скрипт построения окна", "script");

    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values()).table(up.tname());

    eSysprod(Object... p) {
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
            return query().stream().filter(rec -> _id == rec.getInt(id)).findFirst().orElse(null);
        }
        Query recordList = new Query(values()).select(up, "where", id, "=", _id).table(up.tname());
        return (recordList.isEmpty() == true) ? null : recordList.get(0);
    }

    public String toString() {
        return meta.descr();
    }
}
