package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Record;

public enum eElementP1 implements Field {
    up("0", "0", "0", "Параметры составов", "PARVSTM"),
    id("4", "10", "0", "Идентификатор", "id"),
    vnumb("4", "10", "1", "номер параметра", "PNUMB"),
    vtext("12", "64", "1", "наименование значения параметра", "PTEXT");    
    
    //punic("4", "10", "1", "номер параметра", "PUNIC"),
    //psss("4", "10", "1", "ИД компонента состава (Вряд.ли имеет повторение)", "PSSS"),
    //pporn("5", "5", "1", "номер параметра в таблицах ввода параметров в программу. Присваивается автоматически, возможно поменять вручную. В рамках одного группы одноименных номеров быть не может. Возможно менять вручную если такой номер не занят", "PPORN"),   
    //znumb("4", "10", "1", "значение параметра (параметры вводимые пользователем)", "ZNUMB"),
    private MetaField meta = new MetaField(this);

    eElementP1(Object... p) {
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
