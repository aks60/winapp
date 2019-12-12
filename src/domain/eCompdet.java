package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Record;

public enum eCompdet implements Field {
    up("0", "0", "0", "Спецификация комплектов", "KOMPSPC"),
    id("4", "10", "0", "Идентификатор", "id"),
    kunic("4", "10", "1", "ID комплекта", "KUNIC"),
    anumb("12", "32", "1", "Артикул, входящий в состав комплекта", "ANUMB"),
    kincr("4", "10", "1", "ID набора параметров комплекта для артикула", "KINCR"),
    clnum("4", "10", "1", "Основная текстура", "CLNUM"),
    clnu1("4", "10", "1", "Внутренняя текстура", "CLNU1"),
    clnu2("4", "10", "1", "Внешняя текстура", "CLNU2"),
    kmain("5", "5", "1", "Флаг основного элемента комплекта", "KMAIN");
    private MetaField meta = new MetaField(this);

    eCompdet(Object... p) {
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
