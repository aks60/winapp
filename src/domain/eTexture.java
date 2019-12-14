package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Record;

public enum eTexture implements Field {
    up("0", "0", "0", "Описание текстур", "COLSLST"),
    id("4", "10", "0", "Идентификатор", "id"),
    name("12", "32", "1", "название текстуры", "CNAME"),
    name2("12", "32", "1", "название у поставщика", "CNAMP"),
    color("4", "10", "1", "цвет отображения", "CVIEW"),
    coef1("8", "15", "1", "ценовой коэффицент основной", "CKOEF"),
    coef2("8", "15", "1", "ценовой коэффицент внутренний", "KOEF1"),
    coef3("8", "15", "1", "ценовой коэффицент внешний", "KOEF2"),
    suffix1("12", "8", "1", "суффикс основной тестуры", "CMAIN"),
    suffix2("12", "8", "1", "суффикс внутренний тестуры", "CINTO"),
    suffix3("12", "8", "1", "суффикс внешний текстуры", "COUTS"),    
    orient("5", "5", "1", "ориентация", "CORIE"),
    pain("5", "5", "1", "покраска", "CTYPE"),
    textgrp_id("5", "5", "1", "группа", "textgrp_id");
    //ccode("4", "10", "1", "код текстуры", "CCODE"),
    //cnumb("4", "10", "1", "id", "CNUMB"),
    //cgrup("5", "5", "1", "группа", "CGRUP");    
    //cpict("-4", "80", "1", "null", "CPICT"),
    //xdepa("5", "5", "1", "null", "XDEPA"),
    //nouse("5", "5", "1", "null", "NOUSE"),
    //cprc1("8", "15", "1", "null", "CPRC1"),
    //cprc2("8", "15", "1", "null", "CPRC2");
    private MetaField meta = new MetaField(this);

    eTexture(Object... p) {
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
