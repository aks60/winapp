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
    numb("4", "10", "1", "номер параметра", "PNUMB"),
    name("12", "64", "1", "название параметра или его значения", "PNAME"),
    part("16", "5", "1", " параметр уровня", "PTYPF"),
    mixt("4", "10", "1", " параметр (смесь)", "ZNUMB"),
    joint("16", "5", "1", " параметры соединений 0 - не влияют  1 - влияют ", "PCONN"),
    elem("16", "5", "1", " парметры составов 0 - не влияют  1 - влияют ", "PVSTA"),
    glas("16", "5", "1", " параметры стеклопакетов 0 - не влияют  1 - влияют ", "PGLAS"),
    furn("16", "5", "1", " параметры фурнитуры 0 - не влияют  1 - влияют ", "PFURN"),
    otkos("16", "5", "1", " параметры откосов 0 - не влияют  1 - влияют ", "POTKO"),
    komp("16", "5", "1", " парметры комплектов 0 - не влияют  1 - влияют ", "PKOMP"),
    text("16", "5", "1", " парметры текстур 0 - не влияют  1 - влияют ", "PCOLL"),
    prog("16", "5", "1", "системные (вшитые в систему)", "VPROG");
    //pporn("5", "5", "1", "номер параметра", "PPORN");
    //pmacr("12", "32", "1", "null", "PMACR"),
    //xdepa("5", "5", "1", "null", "XDEPA"),
    //psize("12", "96", "1", "null", "PSIZE");

    //PPORN - номер параметра в таблицах ввода параметров в программу. Присваивается автоматически, возможно поменять вручную.  В рамках одного группы одноименных номеров быть не может.  Возможно менять вручную если такой номер не занят
    //PNUMB - номер параметра < 0 - параметры, вводимые пользователем > 0 - параметры, зашитые в программу
    //ZNUMB - Номер параметра или значения: -1   - параметр не имеет определенного в таблицах настройки значения   0 - наименование параметра    > 0  - значение параметра 
    //PTYPF - Тип параметра, с znum = -1 : -1 -  значение для с znum > 0  0 -   значение для с znum = 0  1 - параметры спецификаций соединений 2 - параметры спецификаций соединений 3 - параметры спецификаций соединений 4 - параметры спецификаций соединений 7 - параметры комплектов 8 - параметры комплектов 9 - параметры комплектов 11 - параметры соединений 12 - параметры соединений 13 - параметры групп заполнения 14 - параметры заполнения 15 - параметры заполнения 21 - параметры фурнитуры 24 -параметры спецификаций фурнитуры 25 - 31 - параметры составов профилей 33 - параметры  спецификаций составов профилей 34 - параметры спецификаций составов профилей 37 - параметры составовстеклопакетов 38 - параметры спецификаций составов стеклопакетов 39 - параметры спецификаций составов стеклопакетов 40 - "
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
            return query().stream().filter(rec -> _numb == rec.getInt(numb) && _mixt == rec.getInt(mixt)).findFirst().orElse(null);
        }
        Query recordList = new Query(values()).select(up, "where", numb, "=", _numb, "and", mixt, "=", _mixt).table(up.tname());
        return (recordList.isEmpty() == true) ? null : recordList.get(0);
    }

    public String toString() {
        return meta.getDescr();
    }
}
