package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Record;

public enum eConnvar implements Field {
    up("0", "0", "0", "Варианты соединений", "CONNVAR"),
    id("4", "10", "0", "Идентификатор", "id"),
    cconn("4", "10", "1", "null", "CCONN"),
    cunic("4", "10", "1", "ID соединения", "CUNIC"),
    cprio("5", "5", "1", "Приоритет", "CPRIO"),
    cname("12", "64", "1", "Название варианта", "CNAME"),
    ctype("5", "5", "1", "Тип варианта", "CTYPE"),
    cnext("5", "5", "1", "null", "CNEXT"),
    aser1("12", "32", "1", "Артикул 1", "ASER1"),
    aser2("12", "32", "1", "Артикул 2", "ASER2"),
    cpict("12", "64", "1", "Чертеж варианта", "CPICT"),
    cmirr("5", "5", "1", "1 - использовать зеркально, 0 - нельзя использовать зеркально", "CMIRR"),
    cdiff("8", "15", "1", "null", "CDIFF");
    private MetaField meta = new MetaField(this);

    eConnvar(Object... p) {
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
