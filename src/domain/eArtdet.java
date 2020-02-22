package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.toList;

public enum eArtdet implements Field {
    up("0", "0", "0", "Тариф. мат. цености", "ARTSVST"),
    id("4", "10", "0", "Идентификатор", "id"),
    coef_nakl("8", "15", "1", "коэффициент накладных расходов", "KNAKL"),
    cost_unit("8", "15", "1", "тариф единицы измерения", "CLPRV"),
    cost_cl1("8", "15", "1", "тариф основной текстуры", "CLPRC"),
    cost_cl2("8", "15", "1", "тариф внутренний текстуры", "CLPR1"),
    cost_cl3("8", "15", "1", "тариф внешний текстуры", "CLPR2"),
    cost_cl4("8", "15", "1", "тариф двухсторонний текстуры", "CLPRA"),
    artikl1("12", "32", "1", "артикул склада", "ASKL1"),
    artikl2("12", "32", "1", "артикул 1С", "ASKL2"),
    cways("5", "5", "1", "галочки по приоритетности текстур (основной, внутренней, внешней): 0 - нет галочек (по всем текстурам этот цвет не основной для материала), 1 - галочка на внутренней текстуре, 2 - галочка на внешней текстуре, 3 - галочки на внутренней и внешней текстурах, 4 - галочка на основной текстуре, 5 - галочки на основной и внутреней текстурах, 6 - галочки на основной и внешней текстурах, 7 - галочки на всех текстурах (по всем текстурам основной цвет). ", "CWAYS"),
    artikl_id("4", "10", "1", "ссылка", "artikl_id"),
    color_id("4", "10", "1", "ссылка", "color_id");
    //anumb("12", "32", "1", "артикул", "ANUMB"),
    //clcod("4", "10", "1", "id техстуры", "CLCOD"),
    //clnum("4", "10", "1", "id группы текстуры", "CLNUM"),
    //cluni("4", "10", "1", "null", "CLUNI"), 
    //cminp("8", "15", "1", "минимальный тариф", "CMINP"),

    private MetaField meta = new MetaField(this);
    public static Query query = new Query(values()).table(up.tname());

    eArtdet(Object... p) {
        meta.init(p);
    }

    public static Query query() {
        if (query.size() == 0) {
            query.select(up, "order by", id);
        }
        return query;
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

    public static List<Record> find(int _id) {
        if (conf.equals("calc")) {
            return query.stream().filter(rec -> rec.getInt(artikl_id) == _id).collect(toList());
        }
        return new Query(values()).select(up, "where", artikl_id, "=", _id, "order by", id).table(up.tname());
    }

    public static Record find2(int _id) {
        if (conf.equals("calc")) {
            return query.stream().filter(rec -> rec.getInt(artikl_id) == _id).findFirst().orElse(null);
        }
        List<Record> record = new Query(values()).select("select first 1 * from " + up.tname() + " where " + artikl_id.name() + " = " + _id).table(up.tname());    
        return record.get(0);
    }

    public String toString() {
        return meta.getDescr();
    }
}
