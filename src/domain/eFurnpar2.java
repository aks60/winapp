package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;

public enum eFurnpar2 implements Field {
    up("0", "0", "0", "Параметры спецификаций фурнитуры", "PARFURS"),
    id("4", "10", "0", "Идентификатор", "id"), 
    npp("5", "5", "1", "Нпп параметра", "PPORN"),
    par1("4", "10", "1", "Параметр 1", "PNUMB"), //см. eEnum параметры
    par2("4", "10", "1", "Параметр 2", "ZNUMB"), //пар. вводимые пользователем в системе профилей
    par3("12", "64", "1", "Наименование значения параметр", "PTEXT"),
    furndet_id("4", "10", "1", "Фурнитура", "furndet_id");

    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values()).table(up.tname());

    eFurnpar2(Object... p) {
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

    public String toString() {
        return meta.getDescr();
    }
}
