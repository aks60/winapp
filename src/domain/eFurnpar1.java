package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;

public enum eFurnpar1 implements Field {
    up("0", "0", "0", "Параметры ограничений сторон фурнитуры", "PARFURL"),
    id("4", "10", "0", "Идентификатор", "id"),   
    npp("5", "5", "1", "нпп параметра", "PPORN"),
    pnumb("4", "10", "1", "ссылка", "PNUMB"), //см. eEnum параметры
    znumb("4", "10", "1", "номер параметра", "ZNUMB"), //пар. вводимые пользователем в системе профилей
    val("12", "64", "1", "наименование значения параметра", "PTEXT"),
    furnside_id("4", "10", "1", "ссылка", "furnside_id");

    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values()).table(up.tname());

    eFurnpar1(Object... p) {
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
