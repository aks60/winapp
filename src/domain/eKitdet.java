package domain;

import dataset.Field;
import dataset.MetaField;

public enum eKitdet implements Field {
    up("0", "0", "0", "Спецификация комплектов", "KOMPSPC"),
    id("4", "10", "0", "Идентификатор", "id"),
    color1("4", "10", "1", "Основная текстура", "CLNUM"),
    color2("4", "10", "1", "Внутренняя текстура", "CLNU1"),
    color3("4", "10", "1", "Внешняя текстура", "CLNU2"),
    flag("5", "5", "1", "Флаг основного элемента комплекта", "KMAIN"),
    artikl_id("4", "10", "0", "Ссылка", "artikl_id"),
    kits_id("4", "10", "0", "Ссылка", "kits_id");
//    kunic("4", "10", "1", "ID комплекта", "KUNIC"),
//    anumb("12", "32", "1", "Артикул, входящий в состав комплекта", "ANUMB"),
//    kincr("4", "10", "1", "ID набора параметров комплекта для артикула", "KINCR"),    
    private MetaField meta = new MetaField(this);

    eKitdet(String... p) {
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
