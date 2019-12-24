package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Record;

public enum eFurndet implements Field {
    up("0", "0", "0", "Спецификация фурнитуры", "FURNSPC"),
    id("4", "10", "0", "Идентификатор", "id"),
    fincs("4", "10", "1", "ID зависимого фурнитурного набора из  FINCB  по данной фурнитуре", "FINCS"),
    funic("4", "10", "1", "ID фурнитурного набора (из FURNLST.FUNIC )", "FUNIC"),
    anumb("12", "32", "1", "Артикул материала или слово  НАБОР .", "ANUMB"),
    fincb("4", "10", "1", "Ссылка на параметры и на зависимую/вложенную спецификацию фурнитуры:", "FINCB"),
    clnum("4", "10", "1", "Текстура (COLSLST.CNUMB). Но если это НАБОР, то тут FURNSPC.FUNIC набора.", "CLNUM"),
    ctype("5", "5", "1", "вариант подбора основной текстуры", "CTYPE"), // 0 - указана, 273 - на основе изделия, 546 - по внутр. изделия, 799 - по заполнению (зависимая?) 801 - по основе изделия, 819 - по внешн. изделия, 1092 - по параметру (внутр.), 1638 - по основе в серии, 1911 - по внутр. в серии, 2184 - по внешн. в серии, 3003 - по профилю, 3145 - по параметру (основа), 3276 - по параметру (внешн.), 4095 - по заполнению.
    fleve("5", "5", "1", "Тип спецификации (1 - основная, 2 - зависимая, 3 - вложенная)", "FLEVE");
    private MetaField meta = new MetaField(this);

    eFurndet(Object... p) {
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
