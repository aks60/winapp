package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import static domain.eArtikl.up;
import java.sql.SQLException;

public enum eSyssize implements Field {
    up("0", "0", "0", "Системные размеры", "SYSSIZE"),
    id("4", "10", "0", "Идентификатор", "id"),
    name("12", "32", "1", "Система артикулов", "SNAME"),
    prip("8", "15", "1", "Припуск на сварку", "SSIZP"),
    napl("8", "15", "1", "Наплав системы", "SSIZN"),
    naxl("8", "15", "1", "Нахлест створки", "SSIZF"),
    zax("8", "15", "1", "Заход импоста", "SSIZI");
    //sunic("4", "10", "1", "ID системы", "SUNIC"),
    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values());

    eSyssize(Object... p) {
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

    public static Record find(int _id) {
        if (_id == -1) {
            return record();
        }
        if (Query.conf.equals("calc")) {
            return query().stream().filter(rec -> _id == rec.getInt(id)).findFirst().orElse(up.newRecord());
        }
        Query recordList = new Query(values()).select(up, "where", id, "=", _id);
        return (recordList.isEmpty() == true) ? up.newRecord() : recordList.get(0);
    }

    public static Record record() {
        Record record = query.newRecord(Query.SEL);
        record.setNo(id, -1);
        record.setNo(prip, 0);
        record.setNo(napl, 0);
        record.setNo(naxl, 0);
        record.setNo(zax, 0);
        return record;
//        Record record = query.newRecord(Query.SEL);
//        record.setNo(id, -1);
//        record.setNo(prip, 3);
//        record.setNo(napl, 20);
//        record.setNo(naxl, 8);
//        record.setNo(zax, 6);
//        return record;
    }

    public String toString() {
        return meta.descr();
    }
}
