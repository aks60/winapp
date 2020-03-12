package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import static domain.eArtdet.artikl_id;
import static domain.eArtdet.color_fk;
import static domain.eArtdet.id;
import static domain.eArtikl.code;
import static domain.eArtikl.up;
import static domain.eArtikl.values;

public enum eColor implements Field {
    up("0", "0", "0", "Описание текстур", "COLSLST"),
    id("4", "10", "0", "Идентификатор", "id"),
    code("4", "10", "1", "Код текстуры", "CCODE"),
    numb("4", "10", "1", "id", "CNUMB"),
    name("12", "32", "1", "Название текстуры", "CNAME"),
    name2("12", "32", "1", "Название у поставщика", "CNAMP"),
    code_rgb("4", "10", "1", "Цвет отображения", "CVIEW"),
    coef1("8", "15", "1", "Ценовой коэффицент основной", "CKOEF"),
    coef2("8", "15", "1", "Ценовой коэффицент внутренний", "KOEF1"),
    coef3("8", "15", "1", "Ценовой коэффицент внешний", "KOEF2"),
    suffix1("12", "8", "1", "Суффикс основной тестуры", "CMAIN"),
    suffix2("12", "8", "1", "Суффикс внутренний тестуры", "CINTO"),
    suffix3("12", "8", "1", "Суффикс внешний текстуры", "COUTS"),
    orient("5", "5", "1", "Ориентация", "CORIE"),
    pain("5", "5", "1", "Покраска", "CTYPE"),
    colgrp_id("5", "5", "0", "Группа", "colgrp_id");
    //cgrup("5", "5", "1", "группа", "CGRUP");    
    //cpict("-4", "80", "1", "null", "CPICT"),
    //xdepa("5", "5", "1", "null", "XDEPA"),
    //nouse("5", "5", "1", "null", "NOUSE"),
    //cprc1("8", "15", "1", "null", "CPRC1"),
    //cprc2("8", "15", "1", "null", "CPRC2");
    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values()).table(up.tname());

    eColor(Object... p) {
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

    public static Record find(int _id) {
        if(_id == -1) {
            return record();
        }
        if (conf.equals("calc")) {
            return query().stream().filter(rec -> rec.getInt(id) == _id).findFirst().orElse(null);
        }
        Query recordList = new Query(values()).select(up, "where", id, "=", _id).table(up.tname());
        return (recordList.isEmpty() == true) ? null : recordList.get(0);
    }

    public static Record find2(int _code) {
        if(_code == -1) {
            return record();
        }        
        if (conf.equals("calc")) {
            return query().stream().filter(rec -> rec.getInt(code) == _code).findFirst().orElse(null);
        }
        Query recordList = new Query(values()).select(up, "where", code, "= '", _code, "'").table(up.tname());
        return (recordList.isEmpty() == true) ? null : recordList.get(0);
    }

    public static Record find3(int _numb) {
        if (conf.equals("calc")) {
            return query().stream().filter(rec -> rec.getInt(eColor.numb) == _numb).findFirst().orElse(null);
        }
        Query recordList = new Query(values()).select(up, "where", numb, "=", _numb).table(up.tname());
        return (recordList.isEmpty() == true) ? null : recordList.get(0);
    }

    public static Record record() {
        Record record = query.newRecord(Query.SEL);
        record.setNo(id, -1);
        record.setNo(code, 240);
        record.setNo(name, "Виртуал");
        record.setNo(code_rgb, -1);
        return record;
    }
    
    public String toString() {
        return meta.descr();
    }
}
