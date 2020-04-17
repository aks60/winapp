package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import static domain.eArtdet.values;
import static domain.eArtikl.code;
import static domain.eArtikl.record;
import static domain.eArtikl.up;
import static domain.eArtikl.values;
import static domain.eSysprof.artikl_id;
import static domain.eSysprof.id;
import static domain.eSysprof.systree_id;
import static domain.eSysprof.up;
import enums.LayoutProfile;
import enums.UserArtikl;
import java.sql.SQLException;
import static domain.eSysprof.use_type;
import static domain.eSysprof.use_side;

public enum eSystree implements Field {
    up("0", "0", "0", "Дерево системы профилей", "SYSPROF"),
    id("4", "10", "0", "Идентификатор", "id"),
    name("12", "64", "1", "Наимен. ветки дерева", "TEXT"),
    glas("12", "32", "1", "Заполнение по умолчанию", "ANUMB"),
    size("12", "128", "1", "Дост. толщины стеклопакетов", "ZSIZE"),
    col1("12", "128", "1", "Дост. основные текстуры", "CCODE"),
    col2("12", "128", "1", "Дост. внутрение текстуры", "CCOD1"),
    col3("12", "128", "1", "Дост. внешние текстуры", "CCOD2"),
    coef("8", "15", "1", "Коэф. рентабельности", "KOEF"),
    pref("12", "32", "1", "Замена / код", "NPREF"),
    types("5", "5", "1", "Типы 1-окно; 4,5-двери", "TYPEW"),
    sysprod_id("4", "10", "1", "Ссылка", "sysprod_id"),
    parent_id("4", "10", "1", "Ссылка", "parent_id");
    //nlev("5", "5", "1", "Номер ступеньки в дереве", "NLEV"),
    //npar("4", "10", "1", "ID материнского профиля (SYSPROF.NUNI)", "NPAR"),
    //tcalk("5", "5", "1", "Спецификация профилей и пакетов для сборки", "TCALK"),
    //tview("5", "5", "1", "Примечание", "TVIEW"),
    //tdrit("5", "5", "1", "Режим проволоки в системе (0 - запрещен, 1 - разрешен)", "TDRIT"),
    //nprim("-4", "512", "1", "null", "NPRIM"),
    //ngrup("12", "96", "1", "Система доступная для групп пользователя", "NGRUP"),
    //npref("12", "32", "1", "Замена / код", "NPREF"),
    //pnump("5", "5", "1", "null", "PNUMP"),
    //pnumn("5", "5", "1", "null", "PNUMN"),
    //nuni("4", "10", "1", "ID ветки дерева", "NUNI"),
    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values());

    eSystree(Object... p) {
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

    private static String patch = "";

    public static String patch(int _nuni) {
        Query recordList = new Query(values()).select(up, "where", id, "=", _nuni);
        Record record = recordList.get(0);
        if (record.getInt(id) == record.getInt(parent_id)) {
            return patch + record.getStr(name);
        }
        patch = patch + patch(record.getInt(parent_id));
        return patch + " / " + record.getStr(name);
    }

    public static Record find(int _nuni) {
        if (_nuni == -1) {
            return record();
        }
        if (conf.equals("calc")) {
            return query().stream().filter(rec -> _nuni == rec.getInt(id)).findFirst().orElse(null);
        }
        Query recordList = new Query(values()).select(up, "where", id, "=", _nuni);
        return (recordList.isEmpty() == true) ? null : recordList.get(0);
    }

    public static Record record() {
        Record record = query.newRecord(Query.SEL);
        record.setNo(id, -1);
        record.setNo(glas, "0x0x0x0");
        return record;
    }

    public String toString() {
        return meta.descr();
    }
}
