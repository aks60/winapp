package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Record;

public enum eFurnityra implements Field {
    up("0", "0", "0", "Список фурнитуры", "FURNLST"),
    id("4", "10", "0", "Идентификатор", "id"),
    fname("12", "64", "1", "Название", "FNAME"),
    funic("4", "10", "1", "ID фурнитуры / фурнитурного набора", "FUNIC"),
    fmaxp("8", "15", "1", "Макс. P/2, мм", "FMAXP"),
    fmaxh("8", "15", "1", "Макс. выс., мм", "FMAXH"),
    fmaxl("8", "15", "1", "Макс. шир., мм", "FMAXL"),
    fmaxm("8", "15", "1", "Макс. вес, кг", "FMAXM"),
    fhand("5", "5", "1", "Сторона ручки", "FHAND"),
    ftype("5", "5", "1", "тип фурнитуры (0 - основная, 1 - дополнительная, -1 - фурнитурные наборы)", "FTYPE"),
    fview("12", "16", "1", "Вид (поворотная, раздвижная, фрамуга)", "FVIEW"),
    fpars("12", "96", "1", "Использ. параметры №№", "FPARS"),
    forml("12", "128", "1", "координаты ограничений", "FORML"),
    thand("5", "5", "1", "null", "THAND"),
    fways("5", "5", "1", "null", "FWAYS"),
    xdepa("5", "5", "1", "null", "XDEPA"),
    fdiff("8", "15", "1", "null", "FDIFF"),
    sunic("4", "10", "1", "тип комплекта фурнитуры: 0 - все, 15 - ROTO OK,", "SUNIC");
    //fprim("-4", "512", "1", "null", "FPRIM"),

    private MetaField meta = new MetaField(this);

    eFurnityra(Object... p) {
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
