package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import enums.ProfileSide;
import enums.TypeProfile;

public enum eSysprof implements Field {
    up("0", "0", "0", "Профили, системы профилей", "SYSPROA"),
    id("4", "10", "0", "Идентификатор", "id"),
    prio("5", "5", "1", " приоритет 0 - 1 -  2 -  3 -  4 -  5 -  6 - 10 -  1000 - ", "APRIO"),
    side("5", "5", "1", " сторона (см.ProfileSide)", "ASETS"),
    types("5", "5", "1", " тип профиля (см.TypeProfile)", "ATYPE"),
    artikl_id("4", "10", "0", "Ссылка", "artikl_id"),
    systree_id("4", "10", "0", "Ссылка", "systree_id");
    //aunic("4", "10", "1", "ИД компонента", "AUNIC"),
    //nuni("4", "10", "1", "ID  серии профилей", "NUNI"),    
    //anumb("12", "32", "1", "артикул", "ANUMB"),
    //cflag("5", "5", "1", "Свои текстуры", "CFLAG");
    private MetaField meta = new MetaField(this);
    public static Query query = new Query(values()).table(up.tname());

    eSysprof(Object... p) {
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
            query.select(up, "order by", prio);
        }
        return query;
    }

    public static Record find(int id) {
        return query.select().stream().filter(rec -> id == rec.getInt(eSysprof.systree_id)
                && TypeProfile.FRAME.value == rec.getInt(eSysprof.types)
                && (ProfileSide.Left.value == rec.getInt(eSysprof.side)
                || -1 == rec.getInt(eSysprof.side))).findFirst().orElse(null);
    }

    public String toString() {
        return meta.getDescr();
    }
}
