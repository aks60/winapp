package domain;

import dataset.Field;
import static dataset.Field.conf;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import static domain.eColor.id;
import static domain.eColor.up;
import static domain.eColor.values;

public enum eGlasdet implements Field {
    up("0", "0", "0", "Спецификация групп заполнения", "GLASART"),
    id("4", "10", "0", "Идентификатор", "id"),
    types("5", "5", "1", "Подбор текстуры", "CTYPE"),
    depth("8", "15", "1", "Толщина", "AFRIC"),
    color_fk("4", "10", "1", "Текстура", "CLNUM"),
    artikl_id("4", "10", "1", "Артикл", "artikl_id"),
    glasgrp_id("4", "10", "0", "Заполнение", "glassgrp_id");
    //gnumb("4", "10", "1", "GLASS_ID", "GNUMB"),
    //gunic("4", "10", "1", "ID_GLASDET", "GUNIC"), 
    //anumb("12", "32", "1", "Артикул", "ANUMB"), // ANUMB->ARTIKL.ANUMB
    //clnum("4", "10", "1", "текстура 0-Авто_подб 100000-Точн.подбор 1-. -ХХХ-ручн.парам.", "CLNUM"), //CLNUM=>COLOT.CNUMB
    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values());

    eGlasdet(Object... p) {
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

    public static Record find(int _id, float _depth) {
        if (conf.equals("calc")) {
            return query().stream().filter(rec -> rec.getInt(glasgrp_id) == _id && rec.getFloat(depth) == _depth).findFirst().orElse(null);
        }
        Query recordList = new Query(values()).select(up, "where", id, "=", glasgrp_id, "and", depth, "=", _depth);
        return (recordList.isEmpty() == true) ? null : recordList.get(0);
    }
    
    public String toString() {
        return meta.descr();
    }
}
