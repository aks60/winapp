package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;

public enum eJoinpar1 implements Field {
    up("0", "0", "0", "Параметры вариантов соединений", "PARCONV"),
    id("4", "10", "0", "Идентификатор", "id"), 
    npp("5", "5", "1", "нпп параметра", "PPORN"),
    pnumb("4", "10", "1", "ссылка", "PNUMB"),
    znumb("4", "10", "1", "пар. вводимые пользователем", "ZNUMB"),
    val("12", "64", "1", "наименование значения параметра", "PTEXT"),
    joinvar_id("4", "10", "1", "ссылка", "joinvar_id");

    private MetaField meta = new MetaField(this);
    public static Query query = new Query(values()).table(up.tname());

    eJoinpar1(Object... p) {
        meta.init(p);
    }

    public MetaField meta() {
        return meta;
    }

    public Field[] fields() {
        return values();
    }

    @Override
    public Query select() {
        if (query.size() == 0) {
            query.select(up, "order by", id);
        }
        return query;
    }

    public String toString() {
        return meta.getDescr();
    }
}
