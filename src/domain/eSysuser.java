package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import static domain.eArtikl.id;

public enum eSysuser implements Field {

    up("0", "0", "0", "EMPTY"),
    user2("12", "2147483647", "0", "Пользователь"),
    uch("4", "10", "1", "Учреждение"),
    role("12", "2147483647", "1", "Роль"),
    fio("12", "2147483647", "1", "ФИО пользователя"),
    openkey("12", "2147483647", "1", "Открытый ключ");
    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values());

    eSysuser(Object... p) {
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
