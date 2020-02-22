package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import static domain.eArtikl.query;
import static domain.eArtikl.syscons_id;
import static domain.eArtikl.up;
import java.sql.SQLException;

public enum eSyscons implements Field {
    up("0", "0", "0", "Системные константы", "SYSSIZE"),
    id("4", "10", "0", "Идентификатор", "id"),
    name("12", "32", "1", "Система артикулов", "SNAME"),
    prip("8", "15", "1", "Припуск на сварку", "SSIZP"),
    napl("8", "15", "1", "Наплав системы", "SSIZN"),
    naxl("8", "15", "1", "Нахлест створки", "SSIZF"),
    zax("8", "15", "1", "Заход импоста", "SSIZI");
    //sunic("4", "10", "1", "ID системы", "SUNIC"),
    private MetaField meta = new MetaField(this);
    public static Query query = new Query(values()).table(up.tname());

    eSyscons(Object... p) {
        meta.init(p);
    }

    public MetaField meta() {
        return meta;
    }

    public Field[] fields() {
        return values();
    }

    @Override
    public Query select() {
        if (query.size() == 0) {
            query.select(up, "order by", id);
        }
        return query;
    }

    public static Record find(int _id) {
        if (conf.equals("calc")) {
            return query.stream().filter(rec -> _id == rec.getInt(id)).findFirst().orElse(null);
        }
        Query recordList = new Query(values()).select(up, "where", id, "=", _id).table(up.tname());
        return (recordList.isEmpty() == true) ? null : recordList.get(0);
    }

    @Override
    public void virtualRec() throws SQLException {
        Query q = query.table(up.tname());
        Record record = q.newRecord(Query.INS);
        record.setNo(id, -1);
        record.setNo(prip, 3);
        record.setNo(napl, 20);
        record.setNo(naxl, 8);
        record.setNo(zax, 6);
        q.insert(record);
    }

    public String toString() {
        return meta.getDescr();
    }
}
