package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Record;

public enum eArtikls implements Field {
    up("0", "0", "0", "Материальные цености", "ARTIKLS"),
    id("4", "10", "0", "Идентификатор", "id"),
    code("12", "32", "1", "Артикул", "ANUMB"),
    level1("5", "5", "1", "Главный тип", "ATYPM"),
    level2("5", "5", "1", "Подтип артикула", "ATYPP"),
    group1("4", "10", "1", "Группа материальных ценостей", "MUNIC"),
    group2("12", "32", "1", "Категория", "APREF"),
    analog("12", "32", "1", "Артикул аналога?", "AMAIN"),
    name("12", "64", "1", "Название", "ANAME"),
    supplier("12", "64", "1", "У поставщика", "ANAMP"),
    tech_code("12", "64", "1", "Технологический код контейнера", "ATECH"),
    tech_size("8", "15", "1", "t = размер технологический (размер n/t)", "ASIZV"),
    series("12", "32", "1", "Серия", "ASERI"),
    size_falz("8", "15", "1", "n - глубина до фальца (размер n/t)", "ASIZN"),
    size_centr("8", "15", "1", "Смещение оси от центра", "ASIZB"),
    size_f("8", "15", "1", "Размер F мм", "ASIZF"),
    unit("5", "5", "1", "Ед.измерения", "ATYPI"),
    len_unit("8", "15", "1", "Длина ед. поставки", "ALENG"),
    height("8", "15", "1", "Ширина", "AHEIG"),
    thick("8", "15", "1", "Толщина", "AFRIC"),
    density("8", "15", "1", "Удельный вес", "AMASS"),
    section("8", "15", "1", "Сечение", "ASECH"),
    noopt("5", "5", "1", "Не оптимизировать", "NOOPT"),
    otx_norm("8", "15", "1", "Норма отхода", "AOUTS"),
    ost_delov("8", "15", "1", "Деловой остаток", "AOSTD"),
    cut_perim("8", "15", "1", "Периметр сечения", "APERI"),
    min_rad("8", "15", "1", "Минимальный радиус гиба", "AMINR"),
    group_disc("5", "5", "1", "ID группы скидок (см. DescLst)", "UDESC"),
    size_frez("8", "15", "1", "Фреза", "AFREZ"),
    nokom("5", "5", "1", "Доступ для выбора ( -2 - Только в комплектах, -1 - Только в комплектации, 0- Доступен везде, 1 - Не доступен, 2 - Только в изделиях и ввод блоков, 4 - Только в изделиях)", "NOKOM"),
    noskl("5", "5", "1", "Не для склада", "NOSKL"),
    sel_color("5", "5", "1", "Подбор текстур", "ACOLL"),
    rate_id("4", "10", "1", "ID валюты", "rate_id");
    //cut_perim2("8", "15", "1", "null", "APER1"),
    //cut_perim3("8", "15", "1", "null", "APER2"),    
    //price_koef("8", "15", "1", "ценовой коэффицент", "AKOEF"),    
    //mom_iner1("8", "15", "1", "момент инерции", "AJXXX"),
    //mom_iner2("8", "15", "1", "момент инерции", "AJYYY"),    
    //picture("12", "64", "1", "чертеж артикула", "APICT"),    
    //kant("8", "15", "1", "null", "AKANT"),
    //kname("12", "64", "1", "Поставщик из CLIENT", "KNAME"),
    //work("5", "5", "1", "исполнения", "AWORK"),    
    //group3("12", "196", "1", "группа печати", "AGRUP"),
    //nunic("4", "10", "1", "ИД компоненета", "NUNIC"),
    //cnumt("4", "10", "1", "null", "CNUMT"),    
    //xdepa("5", "5", "1", "null", "XDEPA"),
    //sunic("4", "10", "1", "null", "SUNIC"),
    //aspec("5", "5", "1", "null", "ASPEC"),
    //vruch("12", "196", "1", "null", "VRUCH"),
    //imain("5", "5", "1", "null", "IMAIN"),
    //acomp("5", "5", "1", "null", "ACOMP"),
    //abits("4", "10", "1", "null", "ABITS"),

    private MetaField meta = new MetaField(this);

    eArtikls(Object... p) {
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
