package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Record;

public enum eElempar1 implements Field {
    up("0", "0", "0", "Параметры составов", "PARVSTM"),
    id("4", "10", "0", "Идентификатор", "id"),
    param_id("4", "10", "1", "ссылка", "PNUMB"), 
    element_id("4", "10", "1", "ссылка", "element_id"), 
    val("12", "64", "1", "значения параметра", "PTEXT");    
    
    //punic("4", "10", "1", "номер параметра", "PUNIC"),
    //psss("4", "10", "1", "ссылка", "PSSS"),
    //pnumb("4", "10", "1", "ссылка", "PNUMB"),
    //pporn("5", "5", "1", "номер параметра в таблицах ввода параметров в программу. Присваивается автоматически, возможно поменять вручную. В рамках одного группы одноименных номеров быть не может. Возможно менять вручную если такой номер не занят", "PPORN"),   
    //znumb("4", "10", "1", "значение параметра (параметры вводимые пользователем)", "ZNUMB"),
    private MetaField meta = new MetaField(this);

    eElempar1(Object... p) {
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
