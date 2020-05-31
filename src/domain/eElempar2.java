package domain;

import dataset.Field;
import static dataset.Field.conf;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.toList;

public enum eElempar2 implements Field {
    up("0", "0", "0", "Параметры специф.составов", "PARVSTS"),
    id("4", "10", "0", "Идентификатор", "id"),    
    grup("4", "10", "1", "Группа", "PNUMB"), //см. eEnum параметры
    numb("4", "10", "1", "Параметр", "ZNUMB"), //пар. вводимые пользователем в системе профилей
    text("12", "64", "1", "Значения параметра", "PTEXT"),
    elemdet_id("4", "10", "0", "Спецификацмя", "element_id");
    //npp("5", "5", "1", "Нпп параметра", "PPORN"),

    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values());

    eElempar2(Object... p) {
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
            return query().stream().filter(rec -> _id == rec.getInt(id)).findFirst().orElse(up.newRecord());
        }
        Query recordList = new Query(values()).select(up, "where", id, "=", _id);
        return (recordList.isEmpty() == true) ? up.newRecord() : recordList.get(0);
    }
    
    public static Record find2(int _grup) {
        if (conf.equals("calc")) {
            return query().stream().filter(rec -> _grup == rec.getInt(grup)).findFirst().orElse(up.newRecord());
        }
        Query recordList = new Query(values()).select(up, "where", grup, "=", _grup);
        return (recordList.isEmpty() == true) ? up.newRecord() : recordList.get(0);
    }
    
    public static List<Record> find3(int _elemdet_id) {
        if (conf.equals("calc")) {
            return query().stream().filter(rec -> _elemdet_id == rec.getInt(elemdet_id)).collect(toList());
        }
        Query recordList = new Query(values()).select(up, "where", elemdet_id, "=", _elemdet_id);
        return (recordList.isEmpty() == true) ? new ArrayList() : recordList;
    }
    
    public String toString() {
        return meta.descr();
    }
}
