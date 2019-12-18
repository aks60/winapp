package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Record;

public enum eJoining implements Field {
    up("0", "0", "0", "Соединения", "CONNECT"), //CONNLST"),
    id("4", "10", "0", "Идентификатор", "id"),
    artik1("12", "32", "1", "Артикул 1", "ANUM1"),
    artik2("12", "32", "1", "Артикул 2", "ANUM2"),
    name("12", "64", "1", "Название соединения", "CNAME"),
    variant("5", "5", "1", " Битовая маска: 0x100=256 - установлен флаг   Основное соединение  . Смысл других бит пока неизвестен. ", "CVARF"),
    analog("12", "32", "1", "Аналоги", "CEQUV");
    //cconn("4", "10", "1", "ID соединения", "CCONN"),
    //cpref("12", "32", "1", "Категория", "CPREF"),
    
    private MetaField meta = new MetaField(this);

    eJoining(Object... p) {
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
