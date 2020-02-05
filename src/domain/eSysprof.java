package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import enums.ProfileSide;
import enums.TypeProfile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public enum eSysprof implements Field {
    up("0", "0", "0", "Профили, системы профилей", "SYSPROA"),
    id("4", "10", "0", "Идентификатор", "id"),
    prio("4", "10", "1", " приоритет 0 - 1 -  2 -  3 -  4 -  5 -  6 - 10 -  1000 - ", "APRIO"),
    side("5", "5", "1", " сторона (см.ProfileSide)", "ASETS"),
    types("5", "5", "1", " тип профиля (см.TypeProfile)", "ATYPE"),
    artikl_id("4", "10", "0", "Ссылка", "artikl_id"),
    systree_id("4", "10", "0", "Ссылка", "systree_id");
    //aunic("4", "10", "1", "ИД компонента", "AUNIC"),
    //nuni("4", "10", "1", "ID  серии профилей", "NUNI"),    
    //anumb("12", "32", "1", "артикул", "ANUMB"),
    //cflag("5", "5", "1", "Свои текстуры", "CFLAG");
    private MetaField meta = new MetaField(this);
    public static Query query = new Query(values()).table(up.tname());

    eSysprof(Object... p) {
        meta.init(p);
    }

    public MetaField meta() {
        return meta;
    }

    public Field[] fields() {
        return values();
    }

    @Override
    public Query selectSql() {
        if (query.size() == 0) {
            query.select(up, "order by", prio);
        }
        virtualRec();
        return query;
    }
    
    public  ArrayList<Record> find(int nuni) {

        ArrayList<Record> sysproaList = new ArrayList();
        query.select().stream().filter(rec -> nuni == rec.getInt(id)).forEach(rec -> sysproaList.add(rec));
        return sysproaList;
    }

    public Record find2(int nuni, TypeProfile type) {

        HashMap<Integer, Record> mapPrio = new HashMap();
        query.select().stream().filter(rec -> rec.getInt(systree_id) == nuni && type.value == rec.getInt(types))
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
    
    public Record find3(int nuni, TypeProfile type, ProfileSide _side) {

        HashMap<Integer, Record> mapPrio = new HashMap();
        query.select().stream().filter(rec -> rec.getInt(systree_id) == nuni && type.value == rec.getInt(types)
                && (_side.value == rec.getInt(side) || ProfileSide.ANY.value == rec.getInt(side)))
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

    public void virtualRec() {
        Query q = query.table(up.tname());
        Record rec1 = q.newRecord(Query.SEL);
        rec1.setNo(id, -1);
        rec1.setNo(types, TypeProfile.FRAME.value);
        rec1.setNo(side, ProfileSide.ANY.value);
        rec1.setNo(systree_id, -1);
        rec1.setNo(artikl_id, -1);
        q.add(rec1);
        Record rec2 = q.newRecord(Query.SEL);
        rec2.setNo(id, -2);
        rec2.setNo(types, TypeProfile.STVORKA.value);
        rec2.setNo(side, ProfileSide.ANY.value);
        rec2.setNo(systree_id, -1);
        rec2.setNo(artikl_id, -1);
        q.add(rec2);
    }
    
    public String toString() {
        return meta.getDescr();
    }
}
