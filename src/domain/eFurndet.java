package domain;

import dataset.Field;
import static dataset.Field.conf;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import static domain.eFurnside1.furniture_id;
import static domain.eFurnside1.up;
import static domain.eFurnside1.values;

public enum eFurndet implements Field {
    up("0", "0", "0", "Спецификация фурнитуры", "FURNSPC"),
    id("4", "10", "0", "Идентификатор", "FINCB"),    
    types("5", "5", "1", "Подбор текстуры", "CTYPE"),
    isset("16", "5", "1", "Признак набора", "isset"),
    furndet_id("4", "10", "0", "Ссылка", "FINCS"),
    color_fk("4", "10", "1", "Ссылка", "CLNUM"),
    artikl_id("4", "10", "1", "Ссылка", "artikl_id"),
    furniture_id("4", "10", "0", "Ссылка", "furniture_id");
    //FINCB Первичный ключ
    //CTYPE -> 0 - указана, 273 - на основе изделия, 546 - по внутр. изделия, 
    //        799 - по заполнению (зависимая?) 801 - по основе изделия, 
    //        819 - по внешн. изделия, 1092 - по параметру (внутр.), 
    //        1638 - по основе в серии, 1911 - по внутр. в серии, 2184 - по внешн. в серии, 
    //        3003 - по профилю, 3145 - по параметру (основа), 3276 - по параметру (внешн.), 4095 - по заполнению. 
    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values());

    eFurndet(Object... p) {
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
            return query().stream().filter(rec -> rec.getInt(furniture_id) == _id).findFirst().orElse(up.newRecord());
        }
        Query recordList = new Query(values()).select(up, "where", furniture_id, "=", _id);
        return (recordList.isEmpty() == true) ? up.newRecord() : recordList.get(0);
    }

    public String toString() {
        return meta.descr();
    }
}
