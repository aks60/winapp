package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public enum eFurndet implements Field {
    up("0", "0", "0", "Спецификация фурнитуры", "FURNSPC"),
    id("4", "10", "0", "Идентификатор", "FINCB"),    
    types("5", "5", "1", "Подбор текстуры", "CTYPE"),
    color_fk("4", "10", "1", "Текстура", "CLNUM"),
    artikl_id("4", "10", "1", "Артикул", "artikl_id"),    
    furniture_id1("4", "10", "1", "Фурнитура", "furniture_id1"),
    furniture_id2("4", "10", "1", "Набор", "furniture_id2"),
    furndet_id("4", "10", "0", "Зависимая детализация", "FINCS");

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
            Query.listOpenTable.add(query);
        }
        return query;
    }

    public static List<Record> find(int _id) {
        if (Query.conf.equals("calc")) {
            return query().stream().filter(rec -> rec.getInt(furniture_id1) == _id).collect(Collectors.toList());
        }
        Query recordList = new Query(values()).select(up, "where", furniture_id1, "=", _id);
        return (recordList.isEmpty() == true) ? new ArrayList() : recordList;
    }

    public String toString() {
        return meta.descr();
    }
}
