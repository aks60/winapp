package domain;

import dataset.Field;
import static dataset.Field.conf;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import static domain.eArtdet.artikl_id;
import static domain.eArtdet.id;
import static domain.eArtdet.up;
import static domain.eArtdet.values;
import java.util.List;
import static java.util.stream.Collectors.toList;

public enum eElemdet implements Field {
    up("0", "0", "0", "Спецификация составов", "VSTASPC"),
    id("4", "10", "0", "Идентификатор", "id"),
    types("5", "5", "1", "Подбор текстуры", "CTYPE"), // 0 - указана вручную 11 - профиль 31 - основная
    param_id("4", "10", "0", "Внешний ключ", "param_id"),
    color_st("4", "10", "0", "Текстура", "color_st"),
    artikl_id("4", "10", "0", "Артикл", "artikl_id"),
    element_id("4", "10", "0", "Состав", "element_id");
    //clnum("4", "10", "1", "текстура 0-Авто_подб 100000-Точн.подбор 1-. -ХХХ-ручн.парам.", "CLNUM"),
    //anumb("12", "32", "1", "название компонента", "ANUMB").    
    //vnumb("4", "10", "1", "ИД состава", "VNUMB"),
    //aunic("4", "10", "1", "ИД компонента", "AUNIC"),    
    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values()).table(up.tname());

    eElemdet(Object... p) {
        meta.init(p);
    }

    public MetaField meta() {
        return meta;
    }

    public Field[] fields() {
        return values();
    }

    public static List<Record> find(int _id) {
        if (conf.equals("calc")) {
            return query().stream().filter(rec -> rec.getInt(element_id) == _id).collect(toList());
        }
        return new Query(values()).select(up, "where", element_id.name(), "=", _id).table(up.tname());
    }

    public static Query query() {
        if (query.size() == 0) {
            query.select(up, "order by", id);
        }
        return query;
    }

    public String toString() {
        return meta.getDescr();
    }
}
