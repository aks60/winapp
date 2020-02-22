package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import java.util.List;

public enum eElement implements Field {
    up("0", "0", "0", "Составы", "VSTALST"),
    id("4", "10", "0", "Идентификатор", "id"),
    name("12", "64", "1", "наименование состав", "VNAME"),
    vtype("12", "16", "1", "тип состава (1 - внутренний, 5 - состав_С/П)", "VTYPE"),
    markup("8", "15", "1", "наценка %", "VPERC"),
    series("12", "32", "1", "для серии (из ARTIKLS.ASERI)", "VLETS"),
    bind("5", "5", "1", "установка обязательности", "VSETS"), //0  - умолчание нет, обязательно нет 1 - умолчание да, обязательно да 2 - умолчание да, обязательно нет"
    elemgrp_id("4", "10", "0", "Внешний ключ", "elemgrp_id"),
    artikl_id("4", "10", "0", "Внешний ключ", "artikl_id");

    //anumb("12", "32", "1", "артикул", "ANUMB"),    
    //atypm("5", "5", "1", "тип артикула  1 - профили  5 - заполнение", "ATYPM"),    
    //vnumb("4", "10", "0", "ID", "VNUMB"),
    //vpref("12", "32", "1", "категория", "VPREF"),    
    //vpict("12", "64", "1", "чертеж состава", "VPICT"),
    //vgrup("12", "32", "1", "группа", "VGRUP")
    //vsign("12", "32", "1", "null", "VSIGN"),
    //xdepa("5", "5", "1", "null", "XDEPA"),
    //vdiff("8", "15", "1", "null", "VDIFF"),
    //pnump("5", "5", "1", "null", "PNUMP"),
    //vcomp("5", "5", "1", "null", "VCOMP");
    private MetaField meta = new MetaField(this);
    public static Query query = new Query(values()).table(up.tname());

    eElement(Object... p) {
        meta.init(p);
    }

    public MetaField meta() {
        return meta;
    }

    public Field[] fields() {
        return values();
    }

    @Override
    public Query select() {
        if (query.size() == 0) {
            query.select(up, "order by", id);
        }
        return query;
    }

    public static List<Record> find(String _series) {
        if (conf.equals("calc")) {
            return query.stream().filter(rec -> _series.equals(rec.getStr(series)) && rec.getInt(bind) > 0).findAny().orElse(null);
        }
        return new Query(values()).select(up, "where", series, "= '", _series, "' and", bind, "> 0").table(up.tname());
    }

    public static List<Record> find2(String _artikl_id) {
        if (conf.equals("calc")) {
            return query.stream().filter(rec -> _artikl_id.equals(rec.getStr(artikl_id))).findAny().orElse(null);
        }
        return new Query(values()).select(up, "where", artikl_id, "=", _artikl_id).table(up.tname());
    }

    public String toString() {
        return meta.getDescr();
    }
}
