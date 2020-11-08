package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;

public enum eSysprod implements Field {
    up("0", "0", "0", "Cписок типовых изделий", "EMPTY"),
    id("4", "10", "0", "Идентификатор", "id"),
    models_id("4", "10", "0", "Ссылка", "models_id"),
    systree_id("4", "10", "0", "Ссылка", "systree_id");

    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values());

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
