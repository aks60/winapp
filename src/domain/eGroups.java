package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;

public enum eGroups implements Field {
    up("0", "0", "0", "Справочники", "EMPTY"),
    id("4", "10", "0", "Идентификатор", "id"),
    grup("5", "5", "0", "1-Список параметров \n2-Коэф. групп текстур \n3-Серии профилей  \n4-Наценки групп МЦ \n5-Скидки групп МЦ \n"
            + "6-Категории профилей \n7-Группы соотв. текстур \n8-Категории вставок \n9-Рассчётные параметры \n10-Категории комплектов", "grup"),
    npp("4", "10", "1", "Ном.п.п", "npp"),
    name("12", "96", "1", "Название группы", "name"),
    val("8", "15", "1", "Значение", "coeff");
    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values());

    eGroups(Object... p) {
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

    public static Record find(int _id) {
        if (Query.conf.equals("calc")) {
            return query().stream().filter(rec -> _id == rec.getInt(id)).findFirst().orElse(up.newRecord());
        }
        Query recordList = new Query(values()).select(up, "where", id, "='", _id, "'");
        return (recordList.isEmpty() == true) ? up.newRecord() : recordList.get(0);
    }

    public String toString() {
        return meta.descr();
    }
}
