package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import static domain.eParams.up;
import static domain.eParams.values;
import java.util.ArrayList;
import static domain.eParams.numb;
import static domain.eParams.grup;

public enum eSyspar1 implements Field {
    up("0", "0", "0", "Парамметры системы профилей", "PARSYSP"),
    id("4", "10", "0", "Идентификатор", "id"),    
    grup("4", "10", "1", "Группа", "PNUMB"), //см. eEnum параметры
    numb("4", "10", "1", "Параметр", "ZNUMB"), //пар. вводимые пользователем в системе профилей
    text("12", "64", "1", "Значения параметра", "PTEXT"),
    fixed("16", "5", "1", "Закреплено", "PFIXX"),
    systree_id("4", "10", "0", "Ссылка", "systree_id");
    //npp("5", "5", "1", "Нпп параметра", "PPORN"),

    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values());

    eSyspar1(Object... p) {
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

    public static ArrayList<Record> find(int _nuni) {
        if (conf.equals("calc")) {
            ArrayList<Record> recordList = new ArrayList();
            for (Record record : query) {
                if (_nuni == record.getInt(systree_id)) {
                    recordList.add(record);
                }
            }
            return recordList;
        }
        return new Query(values()).select(up, "where", systree_id, "=", _nuni);
    }

    public String toString() {
        return meta.descr();
    }
}
