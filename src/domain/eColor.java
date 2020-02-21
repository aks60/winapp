package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import static domain.eArtikl.code;
import static domain.eArtikl.up;
import static domain.eArtikl.values;

public enum eColor implements Field {
    up("0", "0", "0", "Описание текстур", "COLSLST"),
    id("4", "10", "0", "Идентификатор", "id"),
    code("4", "10", "1", "код текстуры", "CCODE"),
    numb("4", "10", "1", "id", "CNUMB"),
    name("12", "32", "1", "название текстуры", "CNAME"),
    name2("12", "32", "1", "название у поставщика", "CNAMP"),
    code_rgb("4", "10", "1", "цвет отображения", "CVIEW"),
    coef1("8", "15", "1", "ценовой коэффицент основной", "CKOEF"),
    coef2("8", "15", "1", "ценовой коэффицент внутренний", "KOEF1"),
    coef3("8", "15", "1", "ценовой коэффицент внешний", "KOEF2"),
    suffix1("12", "8", "1", "суффикс основной тестуры", "CMAIN"),
    suffix2("12", "8", "1", "суффикс внутренний тестуры", "CINTO"),
    suffix3("12", "8", "1", "суффикс внешний текстуры", "COUTS"),
    orient("5", "5", "1", "ориентация", "CORIE"),
    pain("5", "5", "1", "покраска", "CTYPE"),
    colgrp_id("5", "5", "0", "группа", "colgrp_id");
    //cgrup("5", "5", "1", "группа", "CGRUP");    
    //cpict("-4", "80", "1", "null", "CPICT"),
    //xdepa("5", "5", "1", "null", "XDEPA"),
    //nouse("5", "5", "1", "null", "NOUSE"),
    //cprc1("8", "15", "1", "null", "CPRC1"),
    //cprc2("8", "15", "1", "null", "CPRC2");
    private MetaField meta = new MetaField(this);
    public static Query query = new Query(values()).table(up.tname());

    eColor(Object... p) {
        meta.init(p);
    }

    public MetaField meta() {
        return meta;
    }

    public Field[] fields() {
        return values();
    }

    @Override
    public Query selectSql() {
        if (query.size() == 0) {
            query.select(up, "order by", id);
        }
        return query;
    }

    public static Record find(int _id) {
        if (conf.equals("calc")) {
            return query.stream().filter(rec -> rec.getInt(eColor.id) == _id).findFirst().orElse(null);
        }
        Query recordList = new Query(values()).select(up, "where", id, "=", _id).table(up.tname());
        return (recordList.isEmpty() == true) ? null : recordList.get(0);
    }

    public static Record find2(int _code) {
        if (conf.equals("calc")) {
            return query.stream().filter(rec -> rec.getInt(eColor.code) == _code).findFirst().orElse(null);
        }
        Query recordList = new Query(values()).select(up, "where", code, "=", _code).table(up.tname());
        return (recordList.isEmpty() == true) ? null : recordList.get(0);
    }

    public static Record find3(int _numb) {
        if (conf.equals("calc")) {
            return query.stream().filter(rec -> rec.getInt(eColor.numb) == _numb).findFirst().orElse(null);
        }
        Query recordList = new Query(values()).select(up, "where", numb, "=", _numb).table(up.tname());
        return (recordList.isEmpty() == true) ? null : recordList.get(0);
    }

    public String toString() {
        return meta.getDescr();
    }
}
