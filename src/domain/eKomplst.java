package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Record;

public enum eKomplst implements Field {
    up("0", "0", "0", "Комплекты в коде под комплектом", "KOMPLST"),
    id("4", "10", "0", "Идентификатор", "id"),
    kname("12", "64", "1", "Название комплекта (для пользователя)", "KNAME"),
    kunic("4", "10", "1", "ID комплекта", "KUNIC"),
    kblob("-4", "512", "1", "Примечания BLOB", "KBLOB"),
    ktype("5", "5", "1", "null", "KTYPE"),
    anumb("12", "32", "1", "Aртикул, получаемый в случае использования  скатки  или ламинации", "ANUMB"),
    clnum("4", "10", "1", "Текстура скатанного или ламинированного артикула", "CLNUM"),
    aqtyk("8", "15", "1", "null", "AQTYK"),
    kpict("12", "64", "1", "null", "KPICT"),
    khide("5", "5", "1", "Флаг  Скрыт . Устанавливается для запрета использования комплекта", "KHIDE"),
    kpref("12", "32", "1", "Категория", "KPREF"),
    kgrup("12", "96", "1", "null", "KGRUP"),
    xdepa("5", "5", "1", "null", "XDEPA"),
    kdiff("8", "15", "1", "null", "KDIFF"),
    pnump("5", "5", "1", "null", "PNUMB"),
    gnumb("5", "5", "1", "null", "GNUMB"),
    ksize("12", "96", "1", "null", "KSIZE");
    private MetaField meta = new MetaField(this);

    eKomplst(Object... p) {
        meta.init(p);
    }

    public MetaField meta() {
        return meta;
    }

    public Field[] fields() {
        return values();
    }

    

    public String toString() {
        return meta.getColName();
    }
}
