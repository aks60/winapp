package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;

public enum eJoining implements Field {
    up("0", "0", "0", "Соединения", "CONNLST"), //или CONNECT"),
    id("4", "10", "0", "Идентификатор", "id"),
    name("12", "64", "1", "Название соединения", "CNAME"),
    variant("5", "5", "1", " Битовая маска: 0x100=256 - установлен флаг   Основное соединение  . Смысл других бит пока неизвестен. ", "CVARF"),
    analog("12", "32", "1", "Аналоги", "CEQUV"),
    artikl_id1("4", "10", "0", "ссылка", "artikl_id1"),
    artikl_id2("4", "10", "0", "ссылка", "artikl_id2");
    //anum1("12", "32", "1", "Артикул 1", "ANUM1"),
    //anum2("12", "32", "1", "Артикул 2", "ANUM2"),    
    //cconn("4", "10", "1", "ID соединения", "CCONN"),
    //cpref("12", "32", "1", "Категория", "CPREF"),

    private MetaField meta = new MetaField(this);
    public static Query query = new Query(values()).table(up.tname());

    eJoining(Object... p) {
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

    public String toString() {
        return meta.getDescr();
    }
}
