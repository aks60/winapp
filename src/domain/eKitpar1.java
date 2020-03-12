package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;

public enum eKitpar1 implements Field {
    up("0", "0", "0", "Парметры комплектов", "PARKOMP"),
    id("4", "10", "0", "Идентификатор", "id"),    
    grup("4", "10", "1", "Группа", "PNUMB"), //см. eEnum параметры
    numb("4", "10", "1", "Параметр", "ZNUMB"), //пар. вводимые пользователем в системе профилей
    text("12", "64", "1", "Значения параметра", "PTEXT"),
    kitdet_id("4", "10", "0", "Идентификатор", "kitdet_id");
    //npp("5", "5", "1", "Нпп параметра", "PPORN"),

    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values());

    eKitpar1(Object... p) {
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

    public String toString() {
        return meta.descr();
    }
}
