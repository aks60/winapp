package domain;

import dataset.Field;
import static dataset.Field.conf;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import static domain.eSysfurn.systree_id;
import static domain.eSysfurn.up;
import static domain.eSysfurn.values;

public enum eFurniture implements Field {
    up("0", "0", "0", "Список фурнитуры", "FURNLST"),
    id("4", "10", "0", "Идентификатор", "id"),
    name("12", "64", "1", "Название", "FNAME"),    
    p2_max("8", "15", "1", "Макс. P/2, мм", "FMAXP"),
    height_max("8", "15", "1", "Макс.выс., мм", "FMAXH"),
    width_max("8", "15", "1", "Макс.шир., мм", "FMAXL"),
    weight_max("8", "15", "1", "Макс.вес, кг", "FMAXM"),
    types("5", "5", "1", "Тип фурнитуры", "FTYPE"), //0 - основная, 1 - дополнительная, -1 - фурнитурные наборы
    hand_side("5", "5", "1", "Сторона ручки", "FHAND"),
    ways_use("5", "5", "1", "Использовать створку как...", "FWAYS"),
    view_open("5", "5", "1", "Вид открывания", "view_open"), //(поворотная, раздвижная, фрамуга)
    pars("12", "96", "1", "Использ. параметры №№", "FPARS"),
    coord_lim("12", "128", "1", "Координаты ограничений", "FORML");
    //fview("12", "16", "1", "Вид открывания", "FVIEW"), //(поворотная, раздвижная, фрамуга)
    //funic("4", "10", "1", "ID фурнитуры / фурнитурного набора", "FUNIC"),
    //thand("5", "5", "1", "null", "THAND"),
    //xdepa("5", "5", "1", "null", "XDEPA"),
    //fdiff("8", "15", "1", "null", "FDIFF"),
    //sunic("4", "10", "1", "тип комплекта фурнитуры: 0 - все, 15 - ROTO OK,", "SUNIC");
    //fprim("-4", "512", "1", "null", "FPRIM"),

    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values());

    eFurniture(Object... p) {
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
            return query().stream().filter(rec -> rec.getInt(id) == _id).findFirst().orElse(null);
        }
        Query recordList = new Query(values()).select(up, "where", id, "=", _id);
        return (recordList.isEmpty() == true) ? null : recordList.get(0);
    }

    public String toString() {
        return meta.descr();
    }
}
