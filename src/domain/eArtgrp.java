package domain;

import dataset.Field;
import static dataset.Field.conf;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import static domain.eArtikl.code;
import static domain.eArtikl.record2;
import static domain.eArtikl.up;
import static domain.eArtikl.values;

public enum eArtgrp implements Field {
    up("0", "0", "0", "Группа материальных ценостей", "GRUPART"),
    id("4", "10", "0", "Идентификатор", "id"),
    categ("12", "16", "1", "Категория группы МЦ", "MPREF"),
    name("12", "32", "1", "Название группы МЦ", "MNAME"),
    coef("8", "15", "1", "Ценовой коэффицент", "MKOEF");
    //unic("4", "10", "1", "ID группы МЦ", "MUNIC"),
    //ugrup("4", "10", "1", "Группа_пользователей", "UGRUP");
    
    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values());

    eArtgrp(Object... p) {
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
        }
        return query;
    }

    public static Record find(int artgrp_id) {
        if (conf.equals("cal")) {
            return query().stream().filter(rec -> artgrp_id == rec.getInt(id)).findFirst().orElse(up.newRecord());
        }
        Query recordList = new Query(values()).select(up, "where", id, "='", artgrp_id, "'");
        return (recordList.isEmpty() == true) ? up.newRecord() : recordList.get(0);
    } 
    
    public String toString() {
        return meta.descr();
    }
}
