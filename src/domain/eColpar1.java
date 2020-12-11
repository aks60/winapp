package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import static domain.eColor.colgrp_id;
import static domain.eColor.id;
import static domain.eColor.record;
import static domain.eColor.up;
import static domain.eColor.values;
import java.util.List;
import static java.util.stream.Collectors.toList;

public enum eColpar1 implements Field {
    up("0", "0", "0", "Парметры текстур", "PARCOLS"),
    id("4", "10", "0", "Идентификатор", "id"),
    grup("4", "10", "1", "Группа", "PNUMB"), //см. eEnum параметры
    numb("4", "10", "1", "Параметр", "ZNUMB"), //пар. вводимые пользователем в системе профилей
    text("12", "64", "1", "Значения параметра", "PTEXT"),
    color_id("4", "10", "0", "Ссылка", "color_id");
    //npp("5", "5", "1", "Нпп параметра", "PPORN"),

    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values());

    eColpar1(Object... p) {
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

    public static List<Record> find(int _color_id) {

        if (Query.conf.equals("calc")) {
            return query().stream().filter(rec -> rec.getInt(color_id) == _color_id).collect(toList());
        }
        return new Query(values()).select(up, "where", color_id, "=", _color_id);
    }
    
    public static List<Record> find2(int _grup) {

        if (Query.conf.equals("calc")) {
            return query().stream().filter(rec -> rec.getInt(grup) == _grup).collect(toList());
        }
        return new Query(values()).select(up, "where", grup, "=", _grup);
    }

    public static Record find3(int _grup, int _numb) {

        if (Query.conf.equals("calc")) {
            return query().stream().filter(rec -> rec.getInt(grup) == _grup && numb == numb).findFirst().orElse(up.newRecord());
        }
        Query recordList = new Query(values()).select(up, "where", grup, "=", _grup, "and", numb, _numb);
        return (recordList.isEmpty() == true) ? up.newRecord() : recordList.get(0);
    }

    public String toString() {
        return meta.descr();
    }
}
