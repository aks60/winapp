package domain;

import dataset.Field;
import static dataset.Field.conf;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public enum eGlasprof implements Field {
    up("0", "0", "0", "Профили в группе заполнения", "GLASPRO"),
    id("4", "10", "0", "Идентификатор", "id"),
    sizeax("8", "15", "1", "Размер от оси, мм", "ASIZE"),
    toin("16", "5", "1", "Внутреннее", "toin"),
    toout("16", "5", "1", "Внншнее", "toout"),
    artikl_id("4", "10", "1", "Ссылка", "artikl_id"),
    glasgrp_id("4", "10", "0", "Ссылка", "glasgrp_id");
    //gtype("5", "5", "1", "Привязка к размеру", "GTYPE"), //1-внут.да, внеш.нет ; 2- внутр.нет, внешн.да: 3- внеш.да, внут.да
    //zunic("4", "10", "1", "null", "ZUNIC"),
    //anumb("12", "32", "1", "Артикул", "ANUMB"),
    //gnumb("4", "10", "1", "ID группы", "GNUMB"),
    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values());

    eGlasprof(Object... p) {
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

    public static List<Record> findAll() {
        if (conf.equals("calc")) {
            return query();
        }
        Query recordList = new Query(values()).select(up);
        return (recordList.isEmpty() == true) ? new ArrayList() : recordList;
    }

    public static List<Record> find(int glasgrpId) {
        if (conf.equals("calc")) {
            return query().stream().filter(rec -> rec.getInt(glasgrp_id) == glasgrpId).collect(Collectors.toList());
        }
        Query recordList = new Query(values()).select(up, "where", glasgrp_id, "=", glasgrpId);
        return (recordList.isEmpty() == true) ? new ArrayList() : recordList;
    }

    public static List<Record> find2(int artiklId) {
        if (conf.equals("calc")) {
            return query().stream().filter(rec -> rec.getInt(artikl_id) == artiklId).collect(Collectors.toList());
        }
        Query recordList = new Query(values()).select(up, "where", artikl_id, "=", artiklId);
        return (recordList.isEmpty() == true) ? new ArrayList() : recordList;
    }

    public String toString() {
        return meta.descr();
    }
}
