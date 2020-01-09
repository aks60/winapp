
package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Record;

public enum eSysprod implements Field {
    up("0", "0", "0", "Cписок типовых изделий по проф. системам", "EMPTY"),
    id("4", "10", "0", "Идентификатор", "id"),    
    name("12", "64", "1", "название типового изделия", "ONAME"),
    json("12", "1024", "0", "Скрипт построения окна", "json"),
    systree_id("4", "10", "0", "Ссылка", "systree_id");    

    private MetaField meta = new MetaField(this);

    eSysprod(Object... p) {
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

