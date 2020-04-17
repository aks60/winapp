package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import static domain.eSyssize.id;
import static domain.eSyssize.up;
import static domain.eSyssize.values;
import static domain.eSyspar1.systree_id;
import static domain.eSyspar1.up;
import static domain.eSyspar1.values;
import enums.LayoutProfile;
import enums.UserArtikl;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public enum eSysprof implements Field {
    up("0", "0", "0", "Профили, системы профилей", "SYSPROA"),
    id("4", "10", "0", "Идентификатор", "id"),
    prio("4", "10", "1", "Приоритет", "APRIO"), //0 - 1 -  2 -  3 -  4 -  5 -  6 - 10 -  1000 - "
    side("5", "5", "1", "Сторона", "ASETS"), //(см.ProfileSide)
    types("5", "5", "1", "Тип профиля (см.TypeProfile)", "ATYPE"),
    artikl_id("4", "10", "0", "Артикл", "artikl_id"),
    sysprod_id("4", "10", "1", "Продукция", "sysprod_id"),
    systree_id("4", "10", "0", "Система", "systree_id");
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
        }
        return query;
    }

    public static ArrayList<Record> find(int _nuni) {      
        if (conf.equals("calc")) {
            ArrayList<Record> sysproaList = new ArrayList();
            query().stream().filter(rec -> _nuni == rec.getInt(systree_id)).forEach(rec -> sysproaList.add(rec));
            return sysproaList;
        }
        return new Query(values()).select(up, "where", systree_id, "=", _nuni);
    }

    public static Record find2(int _nuni, UserArtikl _type) {
        if (_nuni == -1) {
            return record(_type);
        }
        if (conf.equals("calc")) {
            HashMap<Integer, Record> mapPrio = new HashMap();
            query().stream().filter(rec -> rec.getInt(systree_id) == _nuni && _type.id == rec.getInt(types))
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
                + systree_id.name() + " = " + _nuni + " and " + types.name() + " = " + _type.id + " order by " + prio.name());
        return (recordList.isEmpty() == true) ? null : recordList.get(0);
    }

    public static Record find3(int _nuni, UserArtikl _type, LayoutProfile _side) {
        if (_nuni == -1) {
            return record(_type);
        }
        if (conf.equals("calc")) {
            HashMap<Integer, Record> mapPrio = new HashMap();
            query().stream().filter(rec -> rec.getInt(systree_id) == _nuni && _type.id == rec.getInt(types)
                    && (_side.id == rec.getInt(side) || LayoutProfile.ANY.id == rec.getInt(side)))
                    .forEach(rec -> mapPrio.put(rec.getInt(prio), rec));
            int minLevel = 32767;
            for (Map.Entry<Integer, Record> entry : mapPrio.entrySet()) {

                if (entry.getKey() == 0) {
                    return entry.getValue();
                }
                if (minLevel > entry.getKey()) {
                    minLevel = entry.getKey();
                }
            }
            if (mapPrio.size() == 0) {
                return null;
            }
            return mapPrio.get(minLevel);
        }
        Query recordList = new Query(values()).select("select first 1 * from " + up.tname()
                + " where " + systree_id.name() + " = " + _nuni + " and types = " + _type.id + " and ("
                + side.name() + " = " + _side.id + " or " + side.name() + " = " + LayoutProfile.ANY.id + ") order by " + prio.name());
        return (recordList.isEmpty() == true) ? null : recordList.get(0);
    }

    public static Record record(UserArtikl _type) {

        Record record = query.newRecord(Query.SEL);
        record.setNo(id, -1);
        record.setNo(types, _type.id);
        record.setNo(side, LayoutProfile.ANY.id);
        record.setNo(systree_id, -1);
        record.setNo(artikl_id, -1);
        return record;
    }

    public String toString() {
        return meta.descr();
    }
}
