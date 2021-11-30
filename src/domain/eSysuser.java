package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import static domain.eArtikl.id;

public enum eSysuser implements Field {

    up("0", "0", "0", "Пользователи системы", "EMPTY"),
    id("4", "10", "0", "Идентификатор", "id"),
    fio("12", "64", "1", "ФИО пользователя", "fio"),
    desc("12", "64", "0", "Описание", "user2"),    
    code("12", "24", "0", "Пользователь", "user2"),   
    role("12", "16", "1", "Роль", "role"), 
    openkey("12", "512", "1", "Открытый ключ", "openkey");
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
