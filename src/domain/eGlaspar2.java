package domain;

import dataset.Field;
import static dataset.Field.conf;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import static domain.eGlaspar1.glasgrp_id;
import static domain.eGlaspar1.up;
import static domain.eGlaspar1.values;

public enum eGlaspar2 implements Field {
    up("0", "0", "0", "Параметры спецификаций групп заполнения", "PARGLAS"),
    id("4", "10", "0", "Идентификатор", "id"),       
    grup("4", "10", "1", "Группа", "PNUMB"), //см. eEnum параметры
    numb("4", "10", "1", "Параметр", "ZNUMB"), //пар. вводимые пользователем в системе профилей
    text("12", "64", "1", "Значения параметра", "PTEXT"),
    glasdet_id("4", "10", "1", "Спецификация", "glasdet_id");
    //npp("5", "5", "1", "Нпп параметра", "PPORN"), 

    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values()).table(up.tname());

    eGlaspar2(Object... p) {
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
            return query().stream().filter(rec -> rec.getInt(glasdet_id) == _id).findFirst().orElse(null);
        }
        Query recordList = new Query(values()).select(up, "where", glasdet_id, "=", _id).table(up.tname());
        return (recordList.isEmpty() == true) ? null : recordList.get(0);
    }
    
    public String toString() {
        return meta.getDescr();
    }
}
