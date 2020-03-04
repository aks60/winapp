package domain;

import dataset.Field;
import static dataset.Field.conf;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import static domain.eFurniture.id;
import static domain.eFurniture.up;
import static domain.eFurniture.values;

public enum eFurnside1 implements Field {
    up("0", "0", "0", "Ограничение сторон фурнитуры", "FURNLEN"),
    id("4", "10", "0", "Идентификатор", "id"),
    npp("5", "5", "1", "Нpp", "FNUMB"),
    type_side("4", "10", "0", "Сторона", "type_side"), //1-сторона, 2-ось поворота, 3-крепление петель
    furniture_id("4", "10", "0", "Фурнитура", "furniture_id");
    //funic("4", "10", "1", "null", "FUNIC"),
    //fincr("4", "10", "1", "null", "FINCR"),    
    //ftype("12", "16", "1", "Тип стороны", "FTYPE"); //1-сторона, 2-ось поворота, 3-крепление петель
    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values()).table(up.tname());

    eFurnside1(Object... p) {
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

    public static Record find(int _id) {
        if (conf.equals("calc")) {
            return query().stream().filter(rec -> rec.getInt(furniture_id) == _id).findFirst().orElse(null);
        }
        Query recordList = new Query(values()).select(up, "where", furniture_id, "=", _id).table(up.tname());
        return (recordList.isEmpty() == true) ? null : recordList.get(0);
    }
    
    public String toString() {
        return meta.getDescr();
    }
}
