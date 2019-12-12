package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Record;

public enum eArtikls implements Field {
    up("0", "0", "0", "Материальные цености", "ARTIKLS"),
    id("4", "10", "0", "Идентификатор", "id"),
    code("12", "32", "1", "артикул", "ANUMB"),
    level1("5", "5", "1", "главный тип", "ATYPM"),
    level2("5", "5", "1", "подтип артикула", "ATYPP"),
    group1("4", "10", "1", "группа материальных ценостей", "MUNIC"),
    group2("12", "32", "1", "категория", "APREF"),
    //group3("12", "196", "1", "группа печати", "AGRUP"),
    //nunic("4", "10", "1", "ИД компоненета", "NUNIC"),
    //cnumt("4", "10", "1", "null", "CNUMT"),
    analog("12", "32", "1", "артикул аналога?", "AMAIN"),
    name("12", "64", "1", "Название", "ANAME"),
    supplier("12", "64", "1", "у поставщика", "ANAMP"),
    tech_code("12", "64", "1", "технологический код контейнера", "ATECH"),
    tech_size("8", "15", "1", "t = размер технологический (размер n/t)", "ASIZV"),
    //series("12", "32", "1", "серия", "ASERI"),
    size_falz("8", "15", "1", "n - глубина до фальца (размер n/t)", "ASIZN"),
    size_centr("8", "15", "1", "смещение оси от центра", "ASIZB"),
    size_f("8", "15", "1", "размер F мм", "ASIZF"),
    unit("5", "5", "1", "ед.измерения", "ATYPI"),
    len_unit("8", "15", "1", "длина ед. поставки", "ALENG"),
    height("8", "15", "1", "ширина", "AHEIG"),
    thick("8", "15", "1", "толщина", "AFRIC"),
    //picture("12", "64", "1", "чертеж артикула", "APICT"),
    density("8", "15", "1", "удельный вес", "AMASS"),
    //mom_iner1("8", "15", "1", "момент инерции", "AJXXX"),
    //mom_iner2("8", "15", "1", "момент инерции", "AJYYY"),
    section("8", "15", "1", "сечение", "ASECH"),
    noopt("5", "5", "1", "не оптимизировать", "NOOPT"),
    //price_koef("8", "15", "1", "ценовой коэффицент", "AKOEF"),
    otx_norm("8", "15", "1", "норма отхода", "AOUTS"),
    ost_delov("8", "15", "1", "деловой остаток", "AOSTD"),
    cut_perim("8", "15", "1", "периметр сечения", "APERI"),
    //cut_perim2("8", "15", "1", "null", "APER1"),
    //cut_perim3("8", "15", "1", "null", "APER2"),
    min_rad("8", "15", "1", "минимальный радиус гиба", "AMINR"),
    group_disc("5", "5", "1", "ID группы скидок (см. DescLst)", "UDESC"),
    size_frez("8", "15", "1", "Фреза", "AFREZ"),
    //kant("8", "15", "1", "null", "AKANT"),
    //kname("12", "64", "1", "Поставщик из CLIENT", "KNAME"),
    //work("5", "5", "1", "исполнения", "AWORK"),
    nokom("5", "5", "1", "доступ для выбора ( -2 - Только в комплектах, -1 - Только в комплектации, 0- Доступен везде, 1 - Не доступен, 2 - Только в изделиях и ввод блоков, 4 - Только в изделиях)", "NOKOM"),
    noskl("5", "5", "1", "не для склада", "NOSKL"),
    sel_color("5", "5", "1", "подбор текстур", "ACOLL"),
    //xdepa("5", "5", "1", "null", "XDEPA"),
    //sunic("4", "10", "1", "null", "SUNIC"),
    //aspec("5", "5", "1", "null", "ASPEC"),
    //vruch("12", "196", "1", "null", "VRUCH"),
    //imain("5", "5", "1", "null", "IMAIN"),
    //acomp("5", "5", "1", "null", "ACOMP"),
    //abits("4", "10", "1", "null", "ABITS"),
    rate_id("4", "10", "1", "ID валюты", "rate_id");
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
