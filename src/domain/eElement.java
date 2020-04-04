package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import java.util.List;

public enum eElement implements Field {
    up("0", "0", "0", "Составы", "VSTALST"),
    id("4", "10", "0", "Идентификатор", "id"),
    name("12", "64", "1", "Наименование состав", "VNAME"),
    typset("4", "10", "1", "Тип состава", "typset"),
    markup("8", "15", "1", "Наценка %", "VPERC"),
    series("12", "32", "1", "Для серии (из ARTIKLS.ASERI)", "VLETS"),
    bind("5", "5", "1", "Установка обязательности", "VSETS"), //0  - умолчание нет, обязательно нет 1 - умолчание да, обязательно да 2 - умолчание да, обязательно нет"
    artikl_id("4", "10", "1", "Артикл", "artikl_id"),
    elemgrp_id("4", "10", "0", "Группа", "elemgrp_id");
    //vtype("12", "16", "1", "Тип состава (1 - внутренний, 5 - состав_С/П)", "VTYPE"),
    //anumb("12", "32", "1", "артикул", "ANUMB"),    
    //atypm("5", "5", "1", "тип артикула  1 - профили  5 - заполнение", "ATYPM"),    
    //vnumb("4", "10", "0", "ID", "VNUMB"),
    //vpict("12", "64", "1", "чертеж состава", "VPICT"),
    //vgrup("12", "32", "1", "группа", "VGRUP")
    //vsign("12", "32", "1", "null", "VSIGN"),
    //xdepa("5", "5", "1", "null", "XDEPA"),
    //vdiff("8", "15", "1", "null", "VDIFF"),
    //pnump("5", "5", "1", "null", "PNUMP"),
    //vcomp("5", "5", "1", "null", "VCOMP");
    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values());

    eElement(Object... p) {
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

    public static List<Record> find(String _series) {
        if (conf.equals("calc")) {
            return query().stream().filter(rec -> _series.equals(rec.getStr(series)) && rec.getInt(bind) > 0).findAny().orElse(null);
        }
        return new Query(values()).select(up, "where", series, "= '", _series, "' and", bind, "> 0");
    }

    public static List<Record> find2(int _artikl_id) {
        if (conf.equals("calc")) {
            return query().stream().filter(rec -> _artikl_id == rec.getInt(artikl_id)).findAny().orElse(null);
        }
        return new Query(values()).select(up, "where", artikl_id, "=", _artikl_id);
    }

    public static List<Record> find3(int _artikl_id, String _series) {
        if (conf.equals("calc")) {
            return query().stream().filter(rec -> _artikl_id == rec.getInt(artikl_id)
                    && _series.equals(rec.getStr(series)) && rec.getInt(bind) > 0).findAny().orElse(null);
        }
        return new Query(values()).select(up, "where", artikl_id, "=", _artikl_id, "and '", series, "'='", _series, "'");
    }

    public String toString() {
        return meta.descr();
    }
}
