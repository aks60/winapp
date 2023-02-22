package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import static domain.eArtikl.up;
import java.util.List;
import static java.util.stream.Collectors.toList;

//TODO COMMENT ON COLUMN PARCOLS.PSSS IS 'ссылка на текстуру COLSLST.PNUMB'
public enum eParmap implements Field {
    up("0", "0", "0", "Парметры текстур", "PARCOLS"),
    id("4", "10", "0", "Идентификатор", "id"),    
    joint("16", "5", "1", "Параметр соединений", "joint"),
    elem("16", "5", "1", "Параметр составов", "elem"),
    glas("16", "5", "1", "Параметр стеклопакетов", "glas"),
    furn("16", "5", "1", "Параметр фурнитуры", "furn"),
    otkos("16", "5", "1", "Параметр откосов", "otkos"),
    komp("16", "5", "1", "Параметр комплектов", "PKOMP"),
    groups_id("4", "10", "1", "Название параметра", "groups_id"),
    color_id1("4", "10", "1", "Значение параметра", "color_id1"),
    color_id2("4", "10", "1", "Текстура соответствия", "color_id2");

    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values());

    eParmap(Object... p) {
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
            Query.listOpenTable.add(query);
        }
        return query;
    }

    public static List<Record> find(int colorID) {

        if (Query.conf.equals("calc")) {
            return query().stream().filter(rec -> rec.getInt(color_id1) == colorID).collect(toList());
        }
        return new Query(values()).select(up, "where", color_id1, "=", colorID);
    }
    
    public static Record find2(int colorID, int groupsID) {

        if (Query.conf.equals("calc")) {
            return query().stream().filter(rec -> rec.getInt(color_id1) == colorID && rec.getInt(groups_id) == groupsID).findFirst().orElse(up.newRecord());
        }
        Query recordList =  new Query(values()).select(up, "where", color_id1, "=", colorID, "and", groups_id, "=", groupsID);
        return (recordList.isEmpty() == true) ? up.newRecord() : recordList.get(0);
    }
    
    public static List<Record> find3(int colorID, int groupsID) {

        if (Query.conf.equals("calc")) {
            return query().stream().filter(rec -> rec.getInt(color_id1) == colorID && rec.getInt(groups_id) == groupsID).collect(toList());
        }
        return new Query(values()).select(up, "where", color_id1, "=", colorID, "and", groups_id, "=", groupsID);
    }

    public String toString() {
        return meta.descr();
    }
}
