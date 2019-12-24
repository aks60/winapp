package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Record;

public enum eFurnitura implements Field {
    up("0", "0", "0", "Список фурнитуры", "FURNLST"),
    id("4", "10", "0", "Идентификатор", "id"),
    name("12", "64", "1", "Название", "FNAME"),  
    fmaxp("8", "15", "1", "Макс. P/2, мм", "FMAXP"),
    height_max("8", "15", "1", "Макс. выс., мм", "FMAXH"),
    width_max("8", "15", "1", "Макс. шир., мм", "FMAXL"),
    weight_max("8", "15", "1", "Макс. вес, кг", "FMAXM"),
    hand_side("5", "5", "1", "Сторона ручки", "FHAND"),
    types("5", "5", "1", "тип фурнитуры (0 - основная, 1 - дополнительная, -1 - фурнитурные наборы)", "FTYPE"),
    view_open("12", "16", "1", "Вид (поворотная, раздвижная, фрамуга)", "FVIEW"),
    pars("12", "96", "1", "Использ. параметры №№", "FPARS"),
    coord_lim("12", "128", "1", "координаты ограничений", "FORML");
    //funic("4", "10", "1", "ID фурнитуры / фурнитурного набора", "FUNIC"),
    //thand("5", "5", "1", "null", "THAND"),
    //fways("5", "5", "1", "null", "FWAYS"),
    //xdepa("5", "5", "1", "null", "XDEPA"),
    //fdiff("8", "15", "1", "null", "FDIFF"),
    //sunic("4", "10", "1", "тип комплекта фурнитуры: 0 - все, 15 - ROTO OK,", "SUNIC");
    //fprim("-4", "512", "1", "null", "FPRIM"),

    private MetaField meta = new MetaField(this);

    eFurnitura(Object... p) {
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
