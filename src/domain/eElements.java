package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Record;

public enum eElements implements Field {
    up("0", "0", "0", "Составы", "VSTALST"),
    id("4", "10", "0", "Идентификатор", "id"),
    name("12", "64", "1", "наименование состав", "VNAME"),
    vtype("12", "16", "1", "тип состава (1 - внутренний, 5 - состав_С/П)", "VTYPE"),
    markup("8", "15", "1", "наценка %", "VPERC"),
    binding("5", "5", "1", "установка обязательности: 0  - умолчание нет, обязательно нет 1 - умолчание да, обязательно да 2 - умолчание да, обязательно нет", "VSETS"),
    //anumb("12", "32", "1", "артикул", "ANUMB"),    
    //atypm("5", "5", "1", "тип артикула  1 - профили  5 - заполнение", "ATYPM"),    
    //vnumb("4", "10", "0", "ID", "VNUMB"),
    //vpref("12", "32", "1", "категория", "VPREF"),    
    //vpict("12", "64", "1", "чертеж состава", "VPICT"),
    //vlets("12", "32", "1", "для серии (из ARTIKLS.ASERI)", "VLETS"),
    //vgrup("12", "32", "1", "группа", "VGRUP")
    //vsign("12", "32", "1", "null", "VSIGN"),
    //xdepa("5", "5", "1", "null", "XDEPA"),
    //vdiff("8", "15", "1", "null", "VDIFF"),
    //pnump("5", "5", "1", "null", "PNUMP"),
    //vcomp("5", "5", "1", "null", "VCOMP");
    elemgrp_id("4", "10", "0", "Фнешний ключ", "elemgrp_id"),
    artikl_id("4", "10", "0", "Фнешний ключ", "artikl_id");

    private MetaField meta = new MetaField(this);

    eElements(Object... p) {
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
