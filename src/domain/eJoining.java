package domain;

import dataset.Field;
import static dataset.Field.conf;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import static domain.eArtikl.code;
import static domain.eArtikl.up;
import static domain.eArtikl.values;

public enum eJoining implements Field {
    up("0", "0", "0", "Соединения", "CONNLST"), //или CONNECT"),
    id("4", "10", "0", "Идентификатор", "id"),
    name("12", "64", "1", "Название соединения", "CNAME"),
    variant("5", "5", "1", " Битовая маска: 0x100=256 - установлен флаг   Основное соединение  . Смысл других бит пока неизвестен. ", "CVARF"),
    analog("12", "32", "1", "Аналоги", "CEQUV"),
    artikl_id1("4", "10", "0", "Артикл 1", "artikl_id1"),
    artikl_id2("4", "10", "0", "Артикл 2", "artikl_id2");
    //anum1("12", "32", "1", "Артикул 1", "ANUM1"),
    //anum2("12", "32", "1", "Артикул 2", "ANUM2"),    
    //cconn("4", "10", "1", "ID соединения", "CCONN"),
    //cpref("12", "32", "1", "Категория", "CPREF"),

    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values()).table(up.tname());

    eJoining(Object... p) {
        meta.init(p);
    }

    public MetaField meta() {
        return meta;
    }

    public Field[] fields() {
        return values();
    }

    public static Record find(int _artikl_id1, int _artikl_id2) {

        if (conf.equals("calc")) {
            return query().stream().filter(rec -> _artikl_id1 == rec.getInt(artikl_id1) && _artikl_id2 == rec.getInt(artikl_id2)).findFirst().orElse(null);
        }
        Query recordList = new Query(values()).select(up, "where", artikl_id1, "=", _artikl_id1, "and", artikl_id2, "=", _artikl_id2).table(up.tname());
        return (recordList.isEmpty() == true) ? null : recordList.get(0);
    }

    public static Record find2(String _analog) {
        if (conf.equals("calc")) {
            return query().stream().filter(rec -> _analog.equals(rec.getStr(analog))).findFirst().orElse(null);
        }
        Query recordList = new Query(values()).select(up, "where", analog, "='", _analog, "'").table(up.tname());
        return (recordList.isEmpty() == true) ? null : recordList.get(0);
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
