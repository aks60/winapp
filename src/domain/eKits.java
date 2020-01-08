package domain;

import dataset.Field;
import dataset.MetaField;

public enum eKits implements Field {
    up("0", "0", "0", "Комплекты", "KOMPLST"),
    id("4", "10", "0", "Идентификатор", "id"),
    name("12", "64", "1", "Название комплекта (для пользователя)", "KNAME"),
    types("5", "5", "1", "Тип", "KTYPE"), //Продажа, Скатка, Ламинация, Стеклопакет
    categ("12", "32", "1", "Категория", "KPREF"),
    quant("8", "15", "1", "Количество", "AQTYK"),
    hide("5", "5", "1", "Флаг  Скрыт . Устанавливается для запрета использования комплекта", "KHIDE"),    
    artikl_id("4", "10", "0", "Ссылка", "id"),
    color_id("4", "10", "1", "Ссылка", "id");
//    anumb("12", "32", "1", "Aртикул, получаемый в случае использования  скатки  или ламинации", "ANUMB"),
//    clnum("4", "10", "1", "Текстура скатанного или ламинированного артикула", "CLNUM"),    
//    kpict("12", "64", "1", "null", "KPICT"),
//    kgrup("12", "96", "1", "null", "KGRUP");
//    xdepa("5", "5", "1", "null", "XDEPA"),
//    kdiff("8", "15", "1", "null", "KDIFF"),
//    pnump("5", "5", "1", "null", "PNUMP"),
//    gnumb("5", "5", "1", "null", "GNUMB"),
//    ksize("12", "96", "1", "null", "KSIZE");
//    kunic("4", "10", "1", "ID комплекта", "KUNIC"),
//    kblob("-4", "null", "1", "Примечания BLOB", "KBLOB"),    
    private MetaField meta = new MetaField(this);

    eKits(Object... p) {
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
