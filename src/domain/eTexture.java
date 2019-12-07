package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Record;

public enum eTexture implements Field {
    up("0", "0", "0", "Описание текстур", "COLSLST"),
    id("4", "10", "0", "Идентификатор", "id"),
    ccode("4", "10", "1", "код текстуры", "CCODE"),
    cname("12", "32", "1", "название текстуры", "CNAME"),
    cnumb("4", "10", "1", "null", "CNUMB"),
    cnamp("12", "32", "1", "название у поставщика", "CNAMP"),
    cview("4", "10", "1", "цвет отображения", "CVIEW"),
    ckoef("8", "15", "1", "ценовой коэффицент основной", "CKOEF"),
    koef1("8", "15", "1", "ценовой коэффицент внутренний", "KOEF1"),
    koef2("8", "15", "1", "ценовой коэффицент внешний", "KOEF2"),
    cmain("12", "8", "1", "суффикс основной тестуры", "CMAIN"),
    cinto("12", "8", "1", "суффикс внутренний тестуры", "CINTO"),
    couts("12", "8", "1", "суффикс внешний текстуры", "COUTS"),
    cgrup("5", "5", "1", "группа", "CGRUP"),
    corie("5", "5", "1", "ориентация", "CORIE"),
    ctype("5", "5", "1", "покраска", "CTYPE"),
    cpict("-4", "80", "1", "null", "CPICT"),
    xdepa("5", "5", "1", "null", "XDEPA"),
    nouse("5", "5", "1", "null", "NOUSE"),
    cprc1("8", "15", "1", "null", "CPRC1"),
    cprc2("8", "15", "1", "null", "CPRC2");
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
        return meta.getColName();
    }
}
