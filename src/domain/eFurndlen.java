package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Record;

public enum eFurndlen implements Field {
    up("0", "0", "0", "Ограничения сторон для спецификации фурнитуры", "FURNLES"),
    id("4", "10", "0", "Идентификатор", "id"),
    fincs("4", "10", "1", "null", "FINCS"),
    funic("4", "10", "1", "ID фурнитурного набора", "FUNIC"),
    lunic("4", "10", "1", "null", "LUNIC"),
    lnumb("5", "5", "1", "Номер стороны ( 1 - нижняя, 3 - верхняя, 2 - правая, 4 - левая, 5 - нижняя(для изделия формы трапеции)", "LNUMB"),
    lminl("8", "15", "1", "Мин. длина, мм", "LMINL"),
    lmaxl("8", "15", "1", "Макс. длина, мм", "LMAXL"),
    lminu("8", "15", "1", "Мин. угол, градусы", "LMINU"),
    lmaxu("8", "15", "1", "Макс. угол, градусы", "LMAXU");
    private MetaField meta = new MetaField(this);

    eFurndlen(Object... p) {
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
