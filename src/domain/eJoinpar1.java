package domain;

import dataset.Field;
import static dataset.Field.conf;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import static domain.eJoinvar.id;
import static domain.eJoinvar.joining_id;
import static domain.eJoinvar.up;
import static domain.eJoinvar.values;
import java.util.List;
import static java.util.stream.Collectors.toList;

public enum eJoinpar1 implements Field {
    up("0", "0", "0", "Параметры вариантов соединений", "PARCONV"),
    id("4", "10", "0", "Идентификатор", "id"), 
    npp("5", "5", "1", "Нпп параметра", "PPORN"),
    par1("4", "10", "1", "Параметр 1", "PNUMB"), //см. eEnum параметры
    par2("4", "10", "1", "Параметр 2", "ZNUMB"), //пар. вводимые пользователем в системе профилей
    par3("12", "64", "1", "Наименование значения параметра", "PTEXT"),
    joinvar_id("4", "10", "1", "Вариант соединения", "joinvar_id");

    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values()).table(up.tname());

    eJoinpar1(Object... p) {
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
            return query().stream().filter(rec -> rec.getInt(joinvar_id) == _id).collect(toList());
        }
        return new Query(values()).select(up, "where", joinvar_id, "=", _id, "order by", id).table(up.tname());
    }
    
    public String toString() {
        return meta.getDescr();
    }
}
