package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import static domain.eGlaspar2.values;

public enum eGlaspar1 implements Field {
    up("0", "0", "0", "Параметры групп заполнения", "PARGRUP"),
    id("4", "10", "0", "Идентификатор", "id"),    
    npp("5", "5", "1", "нпп параметра", "PPORN"),
    pnumb("4", "10", "1", "номер параметра", "PNUMB"), //см. eEnum параметры
    znumb("4", "10", "1", "номер параметра", "ZNUMB"), //пар. вводимые пользователем в системе профилей
    val("12", "64", "1", "наименование значения параметра", "PTEXT"),
    glasgrp_id("4", "10", "1", "ссылка", "glasgrp_id");

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

    public String toString() {
        return meta.getDescr();
    }
}
