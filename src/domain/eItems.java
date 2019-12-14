package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Record;

public enum eItems implements Field {
    up("0", "0", "0", "Составы", "VSTALST"),
    id("4", "10", "0", "Идентификатор", "id"),
    //vnumb("4", "10", "0", "ID", "VNUMB"),
    //vpref("12", "32", "1", "категория", "VPREF"),
    articl("12", "32", "1", "наименование артикул", "ANUMB"),
    name("12", "64", "1", "наименование состав", "VNAME"),
    //atypm("5", "5", "1", "тип артикула  1 - профили  5 - заполнение", "ATYPM"),
    vtype("12", "16", "1", "тип состава (1 - внутренний, 5 - состав_С/П)", "VTYPE"),
    markup("8", "15", "1", "наценка %", "VPERC"),
    vsets("5", "5", "1", "установка обязательности: 0  - умолчание нет, обязательно нет 1 - умолчание да, обязательно да 2 - умолчание да, обязательно нет", "VSETS"),
    //vpict("12", "64", "1", "чертеж состава", "VPICT"),
    vlets("12", "32", "1", "для серии (из ARTIKLS.ASERI)", "VLETS"),
    //vgrup("12", "32", "1", "группа", "VGRUP")
    //vsign("12", "32", "1", "null", "VSIGN"),
    //xdepa("5", "5", "1", "null", "XDEPA"),
    //vdiff("8", "15", "1", "null", "VDIFF"),
    //pnump("5", "5", "1", "null", "PNUMP"),
    //vcomp("5", "5", "1", "null", "VCOMP");
    itemgrp_id("4", "10", "0", "Фнешний ключ", "itemgrp_id");
    
    private MetaField meta = new MetaField(this);

    eItems(Object... p) {
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
