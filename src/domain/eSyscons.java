package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import static domain.eArtikl.query;
import static domain.eArtikl.syscons_id;

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
    public Query selectSql() {
        if (query.size() == 0) {
            query.select(up, "order by", id);
        }
        return query;
    }

    public static Record find(int id) {
        return query.select().stream().filter(rec -> id == rec.getInt(eSyscons.id)).findFirst().orElse(null);
    }
    
    public Record virtualRec() {
        Record record = query.newRecord(Query.SEL);
        record.set(prip, 3);
        record.set(napl, 20);
        record.set(naxl, 8);
        record.set(zax, 6);
        return record;
    }
    
    public String toString() {
        return meta.getDescr();
    }
}
