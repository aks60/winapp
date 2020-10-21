package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public enum eSysfurn implements Field {
    up("0", "0", "0", "Фурнитуры системы профилей", "SYSPROS"),
    id("4", "10", "0", "Идентификатор", "id"),
    npp("5", "5", "1", "Номер по порядку", "FPORN"),
    replac("5", "5", "1", "Замена", "FWHAT"),
    side_open("5", "10", "1", "Сторона открывания по умолчанию", "side_open"), 
    hand_pos("5", "10", "1", "Расположение ручки по умолчанию", "hand_pos"), 
    furniture_id("4", "10", "1", "Ссылка", "furniture_id"),
    artikl_id1("4", "10", "1", "Артикул ручки по умолчанию", "artikl_id1"),
    artikl_id2("4", "10", "1", "Артиккул подвеса (петли) по умолчанию", "artikl_id2"),   
    systree_id("4", "10", "0", "Система профилей", "systree_id");
    //hand_art_id("4", "10", "0", "Артикул ручки по умолчанию", "hand_art_id"), - Думаю что это лишнее
    //loop_art_id("4", "10", "0", "Артиккул подвеса (петли) по умолчанию", "loop_art_id"); - Думаю что это лишнее    
    //nuni("4", "10", "1", "ИД серии профилей", "NUNI"),
    //artikl_id1("4", "10", "1", "Артикул ручки по умолчанию", "ARUCH"),
    //artikl_id2("4", "10", "1", "Артиккул подвеса (петли) по умолчанию", "APETL"), 
    //funic("4", "10", "1", "ИД фурнитурного набора", "FUNIC"),            
    //anumbt("12", "32", "1", "Артикул ручки по умолчанию", "ARUCH"),
    //apetl("12", "32", "1", "Артиккул подвеса (петли) по умолчанию", "APETL");    
    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values());

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
            query.select(up, "order by", npp);
        }
        return query;
    }

    public static List<Record> find(int _nuni) {
        if (Query.conf.equals("calc")) {
            return query().stream().filter(rec -> rec.getInt(systree_id) == _nuni).collect(Collectors.toList());
        }
        Query recordList = new Query(values()).select(up, "where", systree_id, "=", _nuni, "order by", npp);
        return (recordList.isEmpty() == true) ? new ArrayList() : recordList;
    }

    public String toString() {
        return meta.descr();
    }
}
