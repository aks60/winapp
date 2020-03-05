package domain;

import dataset.Field;
import static dataset.Field.conf;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import static domain.eArtdet.artikl_id;
import static domain.eArtdet.id;
import static domain.eArtdet.up;
import static domain.eArtdet.values;
import java.util.List;
import static java.util.stream.Collectors.toList;

public enum eJoinvar implements Field {
    up("0", "0", "0", "Варианты соединений", "CONNVAR"),
    id("4", "10", "0", "Идентификатор", "id"),
    prio("5", "5", "1", "Приоритет", "CPRIO"),
    name("12", "64", "1", "Название варианта", "CNAME"),
    types("5", "5", "1", "Тип варианта", "CTYPE"),
    mirr("5", "5", "1", "Зеркальное использование", "CMIRR"), //1 - использовать зеркально, 0 - нельзя использовать зеркально
    joining_id("4", "10", "1", "Соединение", "joining_id");
    //conn("4", "10", "1", "null", "CCONN"),
    //cunic("4", "10", "1", "ID соединения", "CUNIC"),   
    //cnext("5", "5", "1", "null", "CNEXT"),
    //aser1("12", "32", "1", "Артикул 1", "ASER1"),
    //aser2("12", "32", "1", "Артикул 2", "ASER2"),
    //cpict("12", "64", "1", "Чертеж варианта", "CPICT"),    
    //cdiff("8", "15", "1", "null", "CDIFF");
    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values()).table(up.tname());

    eJoinvar(Object... p) {
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

    public static List<Record> find(int _id) {
        if (conf.equals("calc")) {
            return query().stream().filter(rec -> rec.getInt(artikl_id) == _id).collect(toList());
        }
        return new Query(values()).select(up, "where", artikl_id, "=", _id, "order by", id).table(up.tname());
    }

    public static List<Record> find2(int _id) {
        if (conf.equals("calc")) {
            return query().stream().filter(rec -> rec.getInt(joining_id) == _id).collect(toList());
        }
        return new Query(values()).select(up, "where", joining_id, "=", _id, "order by", id).table(up.tname());
    }

    public String toString() {
        return meta.descr();
    }
}
