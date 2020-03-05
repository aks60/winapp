package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;

public enum eArtgrp implements Field {
    up("0", "0", "0", "Группа материальных ценостей", "GRUPART"),
    id("4", "10", "0", "Идентификатор", "id"),
    mpref("12", "16", "1", "Категория группы МЦ", "MPREF"),
    mname("12", "32", "1", "Название группы МЦ", "MNAME"),
    munic("4", "10", "1", "ID группы МЦ", "MUNIC"),
    mkoef("8", "15", "1", "Ценовой коэффицент", "MKOEF"),
    ugrup("4", "10", "1", "Группа_пользователей", "UGRUP");
    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values()).table(up.tname());

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

    public String toString() {
        return meta.descr();
    }
}
