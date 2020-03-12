package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;

public enum eSpecific implements Field {
    up("0", "0", "0", "Спецификация", "SPECPAU"),
    id("4", "10", "0", "Идентификатор", "id"),
    punic("4", "10", "1", "Уник. номер", "PUNIC"),
    onumb("4", "10", "1", "Номер изделия", "ONUMB"),
    atypm("5", "5", "1", "Тип", "ATYPM"),
    atypp("5", "5", "1", "Подтип", "ATYPP"),
    anumb("12", "32", "1", "Артикул", "ANUMB"),
    nel("4", "10", "1", "Номер элемента", "NEL"),
    clnum("4", "10", "1", "Основная текстура", "CLNUM"),
    clnu1("4", "10", "1", "Внутренняя текстура", "CLNU1"),
    clnu2("4", "10", "1", "Внешняя текстура", "CLNU2"),
    gform("4", "10", "1", "Эскиз контура", "GFORM"),
    clkc("4", "10", "1", "Тип расчета", "CLKC"),
    clkk("4", "10", "1", "Индекс конструктива", "CLKK"),
    clke("4", "10", "1", "Элемент расчета", "CLKE"),
    aunic("4", "10", "1", "null", "AUNIC"),
    aview("5", "5", "1", "Положение", "AVIEW"),
    aradi("8", "15", "1", "Ширина", "ARADI"),
    aug01("8", "15", "1", "Угол реза профиля 1", "AUG01"),
    aug02("8", "15", "1", "Угол реза профиля 2", "AUG02"),
    aleng("8", "15", "1", "Длина", "ALENG"),
    atypr("4", "10", "1", "Тип дуги", "ATYPR"),
    aqtyp("8", "15", "1", "Количество", "AQTYP"),
    aqtya("8", "15", "1", "Погонаж 1", "AQTYA"),
    aperc("8", "15", "1", "Учет норм отходов", "APERC"),
    aseb1("8", "15", "1", "Себестоимость всего изделия", "ASEB1"),
    aprc1("8", "15", "1", "Цена без скидки всего изделия", "APRC1"),
    aprcd("8", "15", "1", "Цена со скидкой всего изделия", "APRCD"),
    augp1("8", "15", "1", "Угол реза плоскости 1", "AUGP1"),
    augp2("8", "15", "1", "Угол реза плоскости 2", "AUGP2"),
    aprim("12", "64", "1", "Примечание", "APRIM"),
    nopti("4", "10", "1", "null", "NOPTI"),
    adesc("8", "15", "1", "Скидка %", "ADESC"),
    nsq("5", "5", "1", "Плоскость", "NSQ"),
    nhave("5", "5", "1", "null", "NHAVE"),
    nrama("5", "5", "1", "null", "NRAMA"),
    gpict("-4", "80", "1", "null", "GPICT"),
    nunic("4", "10", "1", "null", "NUNIC");
    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values());

    eSpecific(Object... p) {
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

    public String toString() {
        return meta.descr();
    }
}
