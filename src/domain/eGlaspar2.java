package domain;

import dataset.Field;
import static dataset.Field.conf;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public enum eGlaspar2 implements Field {
    up("0", "0", "0", "Парам. спецификации", "PARGLAS"),
    id("4", "10", "0", "Идентификатор", "id"),       
    grup("4", "10", "1", "Группа", "PNUMB"), //см. eEnum параметры
    numb("4", "10", "1", "Параметр", "ZNUMB"), //пар. вводимые пользователем в системе профилей
    text("12", "64", "1", "Значения параметра", "PTEXT"),
    glasdet_id("4", "10", "0", "Ссылка", "glasdet_id");
    //npp("5", "5", "1", "Нпп параметра", "PPORN"), 

    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values());

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

    public static List<Record> find(int _id) {
        if (conf.equals("cal")) {
            return query().stream().filter(rec -> rec.getInt(glasdet_id) == _id).collect(Collectors.toList());
        }
        Query recordList = new Query(values()).select(up, "where", glasdet_id, "=", _id);
        return (recordList.isEmpty() == true) ? new ArrayList() : recordList;
    }
    
    public String toString() {
        return meta.descr();
    }
}
