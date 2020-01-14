package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;

public enum eJoindet implements Field {
    up("0", "0", "0", "Спецификация вариантов соединения", "CONNSPC"),
    id("4", "10", "0", "Идентификатор", "id"),
    match("5", "5", "1", "подбор", "CTYPE"),
    color_id("4", "10", "1", "ссылка", "color_id"),
    artikl_id("4", "10", "1", "ссылка", "artikl_id"),
    joinvar_id("4", "10", "1", "ссылка", "joinvar_id");
    //anumb("12", "32", "1", "артикул", "ANUMB"),
    //cunic("4", "10", "1", "null", "CUNIC"),
    //aunic("4", "10", "1", "ID", "AUNIC"),  
    //clnum("4", "10", "1", "null", "CLNUM");    
        private MetaField meta = new MetaField(this);
    public static Query q = new Query(values()).table(up.tname());

    eJoindet(Object... p) {
        meta.init(p);
    }

    public MetaField meta() {
        return meta;
    }

    public Field[] fields() {
        return values();
    }

    public String toString() {
        return meta.getDescr();
    }
}
