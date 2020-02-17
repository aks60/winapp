package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;

public enum eFurndet implements Field {
    up("0", "0", "0", "Спецификация фурнитуры", "FURNSPC"),
    id("4", "10", "0", "Идентификатор", "id"),
    types("5", "5", "1", "Подбор текстуры", "CTYPE"), // 0 - указана, 273 - на основе изделия, 546 - по внутр. изделия, 799 - по заполнению (зависимая?) 801 - по основе изделия, 819 - по внешн. изделия, 1092 - по параметру (внутр.), 1638 - по основе в серии, 1911 - по внутр. в серии, 2184 - по внешн. в серии, 3003 - по профилю, 3145 - по параметру (основа), 3276 - по параметру (внешн.), 4095 - по заполнению.
    furniture_id("4", "10", "0", "ссылка", "furniture_id"),
    artikl_id("4", "10", "0", "ссылка", "artikl_id");
    //fincs("4", "10", "1", "ID зависимого фурнитурного набора из  FINCB  по данной фурнитуре", "FINCS"),       
    //fincb("4", "10", "1", "Ссылка на параметры и на зависимую/вложенную спецификацию фурнитуры:", "FINCB"),
    //clnum("4", "10", "1", "Текстура (COLSLST.CNUMB). Но если это НАБОР, то тут FURNSPC.FUNIC набора.", "CLNUM"),
    //fleve("5", "5", "1", "Тип спецификации (1 - основная, 2 - зависимая, 3 - вложенная)", "FLEVE"),
    //funic("4", "10", "1", "ID фурнитурного набора (из FURNLST.FUNIC )", "FUNIC"),
    //anumb("12", "32", "1", "Артикул материала или слово  НАБОР .", "ANUMB"),
    private MetaField meta = new MetaField(this);
    public static Query query = new Query(values()).table(up.tname());

    eFurndet(Object... p) {
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
