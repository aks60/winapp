package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import java.util.ArrayList;

public enum eSyspar1 implements Field {
    up("0", "0", "0", "Парамметры системы профилей", "PARSYSP"),
    id("4", "10", "0", "Идентификатор", "id"),
    npp("5", "5", "1", "нпп параметра", "PPORN"),    
    pnumb("4", "10", "1", "номер параметра", "PNUMB"),
    znumb("4", "10", "1", "пар. вводимые пользователем", "ZNUMB"),
    val("12", "64", "1", "наименование значения параметра", "PTEXT"),
    fixed("5", "5", "1", "закреплено", "PFIXX"),
    systree_id("4", "10", "1", "ссылка", "systree_id");
    
    private MetaField meta = new MetaField(this);
    public static Query query = new Query(values()).table(up.tname());

    eSyspar1(Object... p) {
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
            query.select(up, "order by", id);
        }
        return query;
    }

    public static ArrayList<Record> find(int nuni) {

        ArrayList<Record> recordList = new ArrayList();
        for (Record record : query) {
            if (nuni == record.getInt(systree_id)) {
                recordList.add(record);
            }
        }
        return recordList;
    }
    
    public String toString() {
        return meta.getDescr();
    }
}
