package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;

public enum eKitdet implements Field {
    up("0", "0", "0", "Спецификация комплектов", "KOMPSPC"),
    id("4", "10", "0", "Идентификатор", "id"),
    color1_id("4", "10", "1", "Ссылка", "color1_id"),
    color2_id("4", "10", "1", "Ссылка", "color2_id"),
    color3_id("4", "10", "1", "Ссылка", "color3_id"),    
    flag("5", "5", "1", "Флаг основного элемента комплекта", "KMAIN"),
    artikl_id("4", "10", "0", "Ссылка", "artikl_id"),
    kits_id("4", "10", "0", "Ссылка", "kits_id");    
//    clnum("4", "10", "1", "Основная текстура", "CLNUM"),
//    clnu1("4", "10", "1", "Внутренняя текстура", "CLNU1"),
//    clnu2("4", "10", "1", "Внешняя текстура", "CLNU2"),    
//    kunic("4", "10", "1", "ID комплекта", "KUNIC"),
//    anumb("12", "32", "1", "Артикул, входящий в состав комплекта", "ANUMB"),
//    kincr("4", "10", "1", "ID набора параметров комплекта для артикула", "KINCR"),    
        private MetaField meta = new MetaField(this);
    public static Query q = new Query(values()).table(up.tname());

    eKitdet(Object... p) {
        meta.init(p);
    }
    
    public MetaField meta() {
        return meta;
    }

    public Field[] fields() {
        return values();
    }

    public String toString() {
        return meta.getDescr();
    }
}
