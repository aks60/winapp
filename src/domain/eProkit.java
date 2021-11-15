package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import static domain.eKitdet.values;

public enum eProkit implements Field {
    up("0", "0", "0", "Комплекты", "KOMPLST"),
    id("4", "10", "0", "Идентификатор", "id"),    
    name("12", "64", "1", "Название комплекта", "name"),
    type("5", "5", "1", "Флаг", "type"),
    categ("5", "5", "1", "Флаг", "categ"),
    numb("8", "15", "1", "Количество", "count"),
    width("8", "15", "1", "Длина", "width"),
    height("8", "15", "1", "Ширина", "height"),
    color1_id("4", "10", "1", "Ссылка", "color1_id"),
    color2_id("4", "10", "1", "Ссылка", "color2_id"),
    color3_id("4", "10", "1", "Ссылка", "color3_id"),
    flag("5", "5", "1", "Флаг", "KMAIN"), //Основного элемента комплекта
    artikl_id("4", "10", "1", "Ссылка", "artikl_id"),
    project_id("4", "10", "0", "Ссылка", "project_id"),
    prokit_id("4", "10", "0", "Ссылка", "prokit_id");
   
    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values());

    eProkit(Object... p) {
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

    public String toString() {
        return meta.descr();
    }    
}
