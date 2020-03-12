package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;

public enum eKitdet implements Field {
    up("0", "0", "0", "Спецификация комплектов", "KOMPSPC"),
    id("4", "10", "0", "Идентификатор", "id"),
    color1_id("4", "10", "1", "Текстуоа 1", "color1_id"),
    color2_id("4", "10", "1", "Текстуоа 2", "color2_id"),
    color3_id("4", "10", "1", "Текстуоа 3", "color3_id"),
    flag("5", "5", "1", "Флаг основного элемента комплекта", "KMAIN"),
    artikl_id("4", "10", "0", "Артикл", "artikl_id"),
    kits_id("4", "10", "0", "Набор", "kits_id");
//    clnum("4", "10", "1", "Основная текстура", "CLNUM"),
//    clnu1("4", "10", "1", "Внутренняя текстура", "CLNU1"),
//    clnu2("4", "10", "1", "Внешняя текстура", "CLNU2"),    
//    kunic("4", "10", "1", "ID комплекта", "KUNIC"),
//    anumb("12", "32", "1", "Артикул, входящий в состав комплекта", "ANUMB"),
//    kincr("4", "10", "1", "ID набора параметров комплекта для артикула", "KINCR"),    
    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values());

    eKitdet(Object... p) {
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
