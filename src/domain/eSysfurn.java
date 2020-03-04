package domain;

import dataset.Field;
import static dataset.Field.conf;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import static domain.eColor.code;
import static domain.eColor.up;
import static domain.eColor.values;

public enum eSysfurn implements Field {
    up("0", "0", "0", "Фурнитуры системы профилей", "SYSPROS"),
    id("4", "10", "0", "Идентификатор", "id"),
    npp("5", "5", "1", "Номер по порядку", "FPORN"),
    replac("5", "5", "1", "Замена", "FWHAT"),
    side_open("12", "8", "1", "Сторона открывания по умолчанию", "NOTKR"),
    hand_pos("12", "32", "1", "Расположение ручки по умолчанию", "NRUCH"),
    furniture_id("4", "10", "0", "Ссылка", "furniture_id"),
    systree_id("4", "10", "0", "Ссылка", "systree_id");
    //hand_art_id("4", "10", "0", "Артикул ручки по умолчанию", "hand_art_id"), - Думаю что это лишнее
    //loop_art_id("4", "10", "0", "Артиккул подвеса (петли) по умолчанию", "loop_art_id"); - Думаю что это лишнее    
    //nuni("4", "10", "1", "ИД серии профилей", "NUNI"),
    //funic("4", "10", "1", "ИД фурнитурного набора", "FUNIC"),            
    //anumbt("12", "32", "1", "Артикул ручки по умолчанию", "ARUCH"),
    //apetl("12", "32", "1", "Артиккул подвеса (петли) по умолчанию", "APETL");    
    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values()).table(up.tname());

    eSysfurn(Object... p) {
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

    public static Record find(int _nuni) {
        if (conf.equals("calc")) {
            return query().stream().filter(rec -> rec.getInt(systree_id) == _nuni).findFirst().orElse(null);
        }
        Query recordList = new Query(values()).select(up, "where", systree_id, "=", _nuni).table(up.tname());
        return (recordList.isEmpty() == true) ? null : recordList.get(0);
    }

    public String toString() {
        return meta.getDescr();
    }
}
