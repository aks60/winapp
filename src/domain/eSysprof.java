package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import static domain.eColgrp.id;
import static domain.eColgrp.up;
import static domain.eColgrp.values;
import enums.UseSide;
import enums.UseArtiklTo;
import estimate.Wincalc;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum eSysprof implements Field {
    up("0", "0", "0", "Профили сист.профилей", "SYSPROA"),
    id("4", "10", "0", "Идентификатор", "id"),
    prio("4", "10", "1", "Приоритет", "APRIO"), //0 - 1 -  2 -  3 -  4 -  5 -  6 - 10 -  1000 - "
    use_type("5", "5", "1", "Тип использования", "ATYPE"),
    use_side("5", "5", "1", "Сторона использования", "ASETS"),
    artikl_id("4", "10", "0", "Артикул", "artikl_id"),
    models_id("4", "10", "1", "Ссылка", "models_id"), //не использую
    systree_id("4", "10", "0", "Ссылка", "systree_id");
    //aunic("4", "10", "1", "ИД компонента", "AUNIC"),
    //nuni("4", "10", "1", "ID  серии профилей", "NUNI"),    
    //anumb("12", "32", "1", "артикул", "ANUMB"),
    //cflag("5", "5", "1", "Свои текстуры", "CFLAG");
    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values());

    eSysprof(Object... p) {
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
            query.select(up, "order by", prio);
            Query.listOpenTable.add(query);
        }
        return query;
    }

    public static ArrayList<Record> find(int _nuni) {
        if (Query.conf.equals("calc")) {
            ArrayList<Record> sysproaList = new ArrayList();
            query().stream().filter(rec -> _nuni == rec.getInt(systree_id)).forEach(rec -> sysproaList.add(rec));
            return sysproaList;
        }
        return new Query(values()).select(up, "where", systree_id, "=", _nuni, "order by", prio);
    }

    public static Record find2(int _nuni, UseArtiklTo _type) {
        if (_nuni == -3) {
            return record(_type);
        }
        if (Query.conf.equals("calc")) {
            HashMap<Integer, Record> mapPrio = new HashMap();
            query().stream().filter(rec -> rec.getInt(systree_id) == _nuni && rec.getInt(use_type) == _type.id)
                    .forEach(rec -> mapPrio.put(rec.getInt(prio), rec));
            int minLevel = 32767;
            for (Map.Entry<Integer, Record> entry : mapPrio.entrySet()) {

                if (entry.getKey() == 0) { //если нулевой приоритет
                    return entry.getValue();
                }
                if (minLevel > entry.getKey()) { //поднимаемся вверх по приоритету
                    minLevel = entry.getKey();
                }
            }
            if (mapPrio.size() == 0) {
                return null;
            }
            return mapPrio.get(minLevel);
        }
        Query recordList = new Query(values()).select("select first 1 * from " + up.tname() + " where "
                + systree_id.name() + " = " + _nuni + " and " + use_type.name() + " = " + _type.id + " order by " + prio.name());
        return (recordList.isEmpty() == true) ? null : recordList.get(0);
    }
   
    public static Record find3(int _id) {
        if (Query.conf.equals("calc")) {
            return query().stream().filter(rec -> rec.getInt(id) == _id).findFirst().orElse(up.newRecord());
        }
        Query recordList = new Query(values()).select(up, "where", id, "=", _id);
        return (recordList.isEmpty() == true) ? up.newRecord() : recordList.get(0);
    }
    
    public static Record find4(int nuni, UseArtiklTo _type, UseSide... _side) {
        if (nuni == -3) {
            return record(_type);
        }
        List<Integer> _side2 = Arrays.asList(_side).stream().map(s -> s.id).collect(Collectors.toList());
        if (Query.conf.equals("calc")) {

            return query().stream().filter(rec -> rec.getInt(systree_id) == nuni && rec.getInt(use_type) == _type.id
                    && _side2.contains(rec.getInt(use_side))).findFirst().orElse(up.newRecord());
        }
        String str = _side2.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(",", "(", ")"));
        Query sysprofList = new Query(values()).select(up, "where", systree_id, " = ",
                nuni, "and ", use_type, "=", _type.id, "and", use_side, "in", str, "order by", prio);
        return (sysprofList.isEmpty() == true) ? up.newRecord() : sysprofList.get(0);
    }

    public static Record record(UseArtiklTo _type) {

        Record record = up.newRecord();
        record.setNo(id, -3);
        record.setNo(use_type, _type.id);
        record.setNo(use_side, UseSide.ANY.id);
        record.setNo(systree_id, -3);
        record.setNo(artikl_id, -3);
        return record;
    }

    public String toString() {
        return meta.descr();
    }
}
