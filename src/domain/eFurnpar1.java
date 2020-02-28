package domain;

import dataset.Field;
import static dataset.Field.conf;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import static domain.eFurnside1.furniture_id;
import static domain.eFurnside1.up;
import static domain.eFurnside1.values;

public enum eFurnpar1 implements Field {
    up("0", "0", "0", "Параметры ограничений сторон фурнитуры", "PARFURL"),
    id("4", "10", "0", "Идентификатор", "id"),   
    npp("5", "5", "1", "нпп параметра", "PPORN"),
    par1("4", "10", "1", "ссылка", "PNUMB"), //см. eEnum параметры
    par2("4", "10", "1", "номер параметра", "ZNUMB"), //пар. вводимые пользователем в системе профилей
    par3("12", "64", "1", "наименование значения параметра", "PTEXT"),
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

    public static Record find(int _id) {
        if (conf.equals("calc")) {
            return query().stream().filter(rec -> rec.getInt(furnside_id) == _id).findFirst().orElse(null);
        }
        Query recordList = new Query(values()).select(up, "where", furnside_id, "=", _id).table(up.tname());
        return (recordList.isEmpty() == true) ? null : recordList.get(0);
    }
    
    public String toString() {
        return meta.getDescr();
    }
}
