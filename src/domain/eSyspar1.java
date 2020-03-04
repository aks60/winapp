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
    npp("5", "5", "1", "Нпп параметра", "PPORN"),
    par1("4", "10", "1", "Параметр 1", "PNUMB"), //см. eEnum параметры
    par2("4", "10", "1", "Параметр 2", "ZNUMB"), //пар. вводимые пользователем в системе профилей
    par3("12", "64", "1", "Наименование значения параметра", "PTEXT"),
    fixed("5", "5", "1", "Закреплено", "PFIXX"),
    systree_id("4", "10", "1", "Система", "systree_id");

    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values()).table(up.tname());

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
        return new Query(values()).select(up, "where", systree_id, "=", _nuni).table(up.tname());
    }

    public String toString() {
        return meta.getDescr();
    }
}
