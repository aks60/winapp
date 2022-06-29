package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public enum eFurnpar2 implements Field {
    up("0", "0", "0", "Парам.спецификаций фурнитуры", "PARFURS"),
    id("4", "10", "0", "Идентификатор", "id"),
    text("12", "64", "1", "Значения параметр", "PTEXT"),
    params_id("4", "10", "0", "Название параметра", "PNUMB"),
    furndet_id("4", "10", "0", "Детализация", "furndet_id");
    //npp("5", "5", "1", "Нпп параметра", "PPORN"),
    //numb("4", "10", "1", "Параметр", "ZNUMB"), //пар. вводимые пользователем в системе профилей    

    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values());

    eFurnpar2(Object... p) {
        meta.init(p);
    }

    public static Query query() {
        if (query.size() == 0) {
            query.select(up, "order by", id);
            Query.listOpenTable.add(query);
        }
        return query;
    }

    public MetaField meta() {
        return meta;
    }

    public Field[] fields() {
        return values();
    }

    public static List<Record> find(int _id) {
        if (Query.conf.equals("calc")) {
            return query().stream().filter(rec -> rec.getInt(furndet_id) == _id).collect(Collectors.toList());
        }
        Query recordList = new Query(values()).select(up, "where", furndet_id, "=", _id);
        return (recordList.isEmpty() == true) ? new ArrayList() : recordList;
    }
    
    public String toString() {
        return meta.descr();
    }
}
