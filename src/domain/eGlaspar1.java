package domain;

import dataset.Field;
import static dataset.Field.conf;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import static domain.eGlaspar2.values;
import static domain.eGlasprof.glasgrp_id;
import static domain.eGlasprof.up;
import static domain.eGlasprof.values;

public enum eGlaspar1 implements Field {
    up("0", "0", "0", "Параметры групп заполнения", "PARGRUP"),
    id("4", "10", "0", "Идентификатор", "id"),    
    grup("4", "10", "1", "Группа", "PNUMB"), //см. eEnum параметры
    numb("4", "10", "1", "Параметр", "ZNUMB"), //пар. вводимые пользователем в системе профилей
    text("12", "64", "1", "Значения параметра", "PTEXT"),
    glasgrp_id("4", "10", "1", "Глуппа", "glasgrp_id");
    //npp("5", "5", "1", "нпп параметра", "PPORN"),

    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values()).table(up.tname());

    eGlaspar1(Object... p) {
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
            return query().stream().filter(rec -> rec.getInt(glasgrp_id) == _id).findFirst().orElse(null);
        }
        Query recordList = new Query(values()).select(up, "where", glasgrp_id, "=", _id).table(up.tname());
        return (recordList.isEmpty() == true) ? null : recordList.get(0);
    }

    public String toString() {
        return meta.descr();
    }
}
