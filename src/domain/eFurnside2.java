package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;

public enum eFurnside2 implements Field {
    up("0", "0", "0", "Ограничения сторон для спецификации фурнитуры", "FURNLES"),
    id("4", "10", "0", "Идентификатор", "id"),
    side_num("5", "5", "1", "Номер стороны", "LNUMB"), // 1 - нижняя, 3 - верхняя, 2 - правая, 4 - левая, 5 - нижняя(для изделия формы трапеции)"
    len_min("8", "15", "1", "Мин. длина, мм", "LMINL"),
    len_max("8", "15", "1", "Макс. длина, мм", "LMAXL"),
    ang_min("8", "15", "1", "Мин. угол, градусы", "LMINU"),
    ang_max("8", "15", "1", "Макс. угол, градусы", "LMAXU"),
    furndet_id("8", "15", "0", "Детализация", "furndet_id");
    //fincs("4", "10", "1", "спецификация_ID", "FINCS"),
    //funic("4", "10", "1", "фурнитура_ID", "FUNIC"),
    //lunic("4", "10", "1", "ID", "LUNIC"),    
    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values());

    eFurnside2(Object... p) {
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
        return meta.descr();
    }
}
