package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;

public enum eFurnside1 implements Field {
    up("0", "0", "0", "Ограничение сторон фурнитуры", "FURNLEN"),
    id("4", "10", "0", "Идентификатор", "id"),
    npp("5", "5", "1", "npp", "FNUMB"),
    type_side("4", "10", "0", "Идентификатор", "type_side"), //1-сторона, 2-ось поворота, 3-крепление петель
    furniture_id("4", "10", "0", "Ссылка", "furniture_id");
    //funic("4", "10", "1", "null", "FUNIC"),
    //fincr("4", "10", "1", "null", "FINCR"),    
    //ftype("12", "16", "1", "Тип стороны", "FTYPE"); //1-сторона, 2-ось поворота, 3-крепление петель
    private MetaField meta = new MetaField(this);
    public static Query query = new Query(values()).table(up.tname());

    eFurnside1(Object... p) {
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
