package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import static domain.eSysprof.artikl_id;
import static domain.eSysprof.up;
import java.sql.SQLException;

public enum eArtikl implements Field {
    up("0", "0", "0", "Материальные цености", "ARTIKLS"),
    id("4", "10", "0", "Идентификатор", "id"),
    code("12", "32", "1", "Артикул", "ANUMB"),
    level1("5", "5", "1", "Главный тип", "ATYPM"),
    level2("5", "5", "1", "Подтип артикула", "ATYPP"),
    group1("4", "10", "1", "Группа мат.ценостей", "MUNIC"),
    group2("12", "32", "1", "Категория", "APREF"),
    name("12", "64", "1", "Название", "ANAME"),
    supplier("12", "64", "1", "У поставщика", "ANAMP"),
    tech_code("12", "64", "1", "Технолог.код контейнера", "ATECH"),
    tech_size("8", "15", "1", "t = размер технолог.(размер n/t)", "ASIZV"),
    size_falz("8", "15", "1", "n - глубина до фальца (размер n/t)", "ASIZN"),
    size_centr("8", "15", "1", "Смещение оси от центра", "ASIZB"),
    size_f("8", "15", "1", "Размер F мм", "ASIZF"),    
    len_unit("8", "15", "1", "Длина ед. поставки", "ALENG"),
    height("8", "15", "1", "Ширина", "AHEIG"),
    depth("8", "15", "1", "Толщина", "AFRIC"),
    density("8", "15", "1", "Удельный вес", "AMASS"),
    section("8", "15", "1", "Сечение", "ASECH"),
    noopt("5", "5", "1", "Не оптимизировать", "NOOPT"),
    otx_norm("8", "15", "1", "Норма отхода", "AOUTS"),
    ost_delov("8", "15", "1", "Деловой остаток", "AOSTD"),
    cut_perim("8", "15", "1", "Периметр сечения", "APERI"),
    min_rad("8", "15", "1", "Мин.радиус гиба", "AMINR"),
    group_disc("5", "5", "1", "ID группы скидок (см. DescLst)", "UDESC"),
    size_frez("8", "15", "1", "Фреза", "AFREZ"),
    nokom("5", "5", "1", "Доступ для выбора", "NOKOM"), // ( -2 - Только в комплектах, -1 - Только в комплектации, 0- Доступен везде, 1 - Не доступен, 2 - Только в изделиях и ввод блоков, 4 - Только в изделиях)
    noskl("5", "5", "1", "Не для склада", "NOSKL"),
    sel_color("5", "5", "1", "Подбор текстур", "ACOLL"),
    analog_id("4", "10", "1", "Ссылка", "analog_id"),
    series_id("4", "10", "1", "Ссылка(cерия)", "series_id"),
    syssize_id("4", "10", "1", "Ссылка", "syssize_id"),
    currenc_id("4", "10", "1", "Ссылка", "CNUMB");
    //series("12", "32", "1", "Серия", "ASERI"),
    //amain("12", "32", "1", "Артикул аналога?", "AMAIN"),
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
    private static Query query = new Query(values());

    eArtikl(Object... p) {
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

    public static Record find(int _id, boolean _analog) {
        if (_id == -1) {
            return record();
        }
        if (conf.equals("calc")) {
            Record recordRec = query().stream().filter(rec -> _id == rec.getInt(id)).findFirst().orElse(up.newRecord());
            if (_analog == true && recordRec.get(analog_id) != null) {

                int _analog_id = recordRec.getInt(analog_id);
                recordRec = query().stream().filter(rec -> _analog_id == rec.getInt(id)).findFirst().orElse(up.newRecord());
            }
            return recordRec;
        }

        Query recordList = new Query(values()).select(up, "where", id, "=", _id);
        if (_analog == true && recordList.isEmpty() == false && recordList.get(0, analog_id) != null) {

            int _analog_id = recordList.getAs(0, analog_id);
            recordList = new Query(values()).select(up, "where", id, "=", _analog_id);
        }
        return (recordList.isEmpty() == true) ? up.newRecord() : recordList.get(0);
    }

    public static Record find2(String _code) {
        if (_code.equals("0x0x0x0")) {
            return record2();
        }
        if (conf.equals("calc")) {
            return query().stream().filter(rec -> _code.equals(rec.getStr(code))).findFirst().orElse(up.newRecord());
        }
        Query recordList = new Query(values()).select(up, "where", code, "='", _code, "'");
        return (recordList.isEmpty() == true) ? up.newRecord() : recordList.get(0);
    }

    public static Record record() {
        Record record = query.newRecord(Query.SEL);
        record.setNo(id, -1);
        record.setNo(name, "Рама");
        record.setNo(height, 60);
        record.setNo(size_centr, 30);
        record.setNo(tech_code, "");
        record.setNo(size_falz, 20);
        record.setNo(syssize_id, -1);
        return record;
    }

    //[SEL, 2633, 4x10x4x10x4, 5, 2, 8, null, 32 Стеклопакет двухкамерный, null, null, 0.0, 1СП-1,5 все, 0.0, 10.0, 0.0, 2, 2250.0, 1605.0, 32.0, 30.0, 10.0, 0, 0.0, 0.0, 0.0, 0.0, 0, 0.0, 2, 0, 0, null, 3, null]
    public static Record record2() {
        Record record = query.newRecord(Query.SEL);
        record.setNo(id, -1);
        record.setNo(code, "0x0x0x0");
        record.setNo(name, "Стеклопакет");
        record.setNo(syssize_id, -1);
        return record;
    }

    public String toString() {
        return meta.descr();
    }
}
