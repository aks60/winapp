package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import static domain.eArtikl.code;
import static domain.eArtikl.up;
import static domain.eArtikl.values;

public enum eParams implements Field {
    up("0", "0", "0", "Список параметров", "PARLIST"),
    id("4", "10", "0", "Идентификатор", "id"),    
    group("4", "10", "1", "Параметр 1", "PNUMB"),
    numb("4", "10", "1", "Параметр 2", "ZNUMB"),
    text("12", "64", "1", "Название параметра или его значения", "PNAME"),     
    joint("16", "5", "1", "Параметры соединений 0 - не влияют  1 - влияют ", "PCONN"),
    elem("16", "5", "1", " Парметры составов 0 - не влияют  1 - влияют ", "PVSTA"),
    glas("16", "5", "1", " Параметры стеклопакетов 0 - не влияют  1 - влияют ", "PGLAS"),
    furn("16", "5", "1", " Параметры фурнитуры 0 - не влияют  1 - влияют ", "PFURN"),
    otkos("16", "5", "1", " Параметры откосов 0 - не влияют  1 - влияют ", "POTKO"),
    komp("16", "5", "1", " Парметры комплектов 0 - не влияют  1 - влияют ", "PKOMP"),
    color("16", "5", "1", " Парметры текстур 0 - не влияют  1 - влияют ", "PCOLL"),
    label("12", "32", "1", "Надпись на эскизе", "PMACR"),
    prog("16", "5", "1", "Системные (вшитые в систему)", "VPROG");
    
    //npp("5", "5", "1", "Номер параметра", "PPORN"),
    //ptype("16", "5", "1", "Количество тысяч par1", "PTYPF"),
    //pmacr("12", "32", "1", "null", "PMACR"),
    //xdepa("5", "5", "1", "null", "XDEPA"),
    //psize("12", "96", "1", "null", "PSIZE");

    //ПОЯСНЕНИЯ !!!
    //npp -  Присваивается автоматически, возможно поменять вручную.  В рамках одного группы одноименных номеров быть не может.  Возможно менять вручную если такой номер не занят
    //par1 - par1 < 0 - параметры вводимые пользователем, par1 > 0 - параметры, зашитые в программу
    //par2 - Номер параметра или значения: -1 - параметр не имеет определенного в таблицах настройки значения,  0 - наименование параметра, > 0  - значение параметра 
    //types - Тип параметра, с znum = -1 : -1 -  значение для с znum > 0  0 -   значение для с znum = 0  1 - параметры спецификаций соединений 2 - параметры спецификаций соединений 3 - параметры спецификаций соединений 4 - параметры спецификаций соединений 7 - параметры комплектов 8 - параметры комплектов 9 - параметры комплектов 11 - параметры соединений 12 - параметры соединений 13 - параметры групп заполнения 14 - параметры заполнения 15 - параметры заполнения 21 - параметры фурнитуры 24 -параметры спецификаций фурнитуры 25 - 31 - параметры составов профилей 33 - параметры  спецификаций составов профилей 34 - параметры спецификаций составов профилей 37 - параметры составовстеклопакетов 38 - параметры спецификаций составов стеклопакетов 39 - параметры спецификаций составов стеклопакетов 40 - "
    //
    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values()).table(up.tname());

    eParams(Object... p) {
        meta.init(p);
    }

    public MetaField meta() {
        return meta;
    }

    public Field[] fields() {
        return values();
    }

    public static Query query() {
        if (query.size() == 0) {
            query.select(up, "order by", id);
        }
        return query;
    }

    public static Record find(int _numb, int _mixt) {
        if (conf.equals("calc")) {
            return query().stream().filter(rec -> _numb == rec.getInt(group) && _mixt == rec.getInt(numb)).findFirst().orElse(null);
        }
        Query recordList = new Query(values()).select(up, "where", group, "=", _numb, "and", numb, "=", _mixt).table(up.tname());
        return (recordList.isEmpty() == true) ? null : recordList.get(0);
    }

    public String toString() {
        return meta.getDescr();
    }
}
