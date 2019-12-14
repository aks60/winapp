package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Record;

public enum eElemdet implements Field {
    up("0", "0", "0", "Спецификация составов", "VSTASPC"),
    id("4", "10", "0", "Идентификатор", "id"),
    mtyp("5", "5", "1", "Тип подбора 0 - указана вручную 11 - профиль 31 - основная", "CTYPE"),
    text("4", "10", "1", "текстура 0-Авто_подб 100000-Точн.подбор 1-. -ХХХ-ручн.парам.", "CLNUM"),
    element_id("4", "10", "0", "Внешний ключ", "element_id"),
    artikl_id("4", "10", "0", "Внешний ключ", "artikl_id");
    //anumb("12", "32", "1", "название компонента", "ANUMB").    
    //vnumb("4", "10", "1", "ИД состава", "VNUMB"),
    //aunic("4", "10", "1", "ИД компонента", "AUNIC"),    
    private MetaField meta = new MetaField(this);

    eElemdet(Object... p) {
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
