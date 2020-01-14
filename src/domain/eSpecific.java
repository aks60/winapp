package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;

public enum eSpecific implements Field {
    up("0", "0", "0", "Спецификация", "SPECPAU"),
    id("4", "10", "0", "Идентификатор", "id"),
    punic("4", "10", "1", "null", "PUNIC"),
    onumb("4", "10", "1", "номер изделия", "ONUMB"),
    atypm("5", "5", "1", "тип", "ATYPM"),
    atypp("5", "5", "1", "подтип", "ATYPP"),
    anumb("12", "32", "1", "артикул", "ANUMB"),
    nel("4", "10", "1", "номер элемента", "NEL"),
    clnum("4", "10", "1", "основная текстура", "CLNUM"),
    clnu1("4", "10", "1", "внутренняя текстура", "CLNU1"),
    clnu2("4", "10", "1", "внешняя текстура", "CLNU2"),
    gform("4", "10", "1", "эскиз контура", "GFORM"),
    clkc("4", "10", "1", "тип расчета", "CLKC"),
    clkk("4", "10", "1", "индекс конструктива", "CLKK"),
    clke("4", "10", "1", "элемент расчета", "CLKE"),
    aunic("4", "10", "1", "null", "AUNIC"),
    aview("5", "5", "1", "положение", "AVIEW"),
    aradi("8", "15", "1", "ширина", "ARADI"),
    aug01("8", "15", "1", "угол реза профиля 1", "AUG01"),
    aug02("8", "15", "1", "угол реза профиля 2", "AUG02"),
    aleng("8", "15", "1", "длина", "ALENG"),
    atypr("4", "10", "1", "тип дуги", "ATYPR"),
    aqtyp("8", "15", "1", "количество", "AQTYP"),
    aqtya("8", "15", "1", "погонаж 1", "AQTYA"),
    aperc("8", "15", "1", "учет норм отходов", "APERC"),
    aseb1("8", "15", "1", "себестоимость всего изделия", "ASEB1"),
    aprc1("8", "15", "1", "цена без скидки всего изделия", "APRC1"),
    aprcd("8", "15", "1", "цена со скидкой всего изделия", "APRCD"),
    augp1("8", "15", "1", "угол реза плоскости 1", "AUGP1"),
    augp2("8", "15", "1", "угол реза плоскости 2", "AUGP2"),
    aprim("12", "64", "1", "примечание", "APRIM"),
    nopti("4", "10", "1", "null", "NOPTI"),
    adesc("8", "15", "1", "скидка %", "ADESC"),
    nsq("5", "5", "1", "плоскость", "NSQ"),
    nhave("5", "5", "1", "null", "NHAVE"),
    nrama("5", "5", "1", "null", "NRAMA"),
    gpict("-4", "80", "1", "null", "GPICT"),
    nunic("4", "10", "1", "null", "NUNIC");
        private MetaField meta = new MetaField(this);
    public static Query query = new Query(values()).table(up.tname());

    eSpecific(Object... p) {
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
