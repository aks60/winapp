package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;

public enum eGroups implements Field {
    up("0", "0", "0", "Группы наименований", "EMPTY"),
    id("4", "10", "0", "Идентификатор", "id"),
    grup("5", "5", "0", "Группа", "grup"),
    name("12", "256", "0", "Название группы", "name");
    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values());

    eGroups(Object... p) {
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

    public String toString() {
        return meta.descr();
    }
}
