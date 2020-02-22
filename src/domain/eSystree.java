package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import static domain.eArtdet.values;
import static domain.eArtikl.code;
import static domain.eArtikl.up;
import static domain.eArtikl.values;
import static domain.eSysprof.artikl_id;
import static domain.eSysprof.id;
import static domain.eSysprof.query;
import static domain.eSysprof.side;
import static domain.eSysprof.systree_id;
import static domain.eSysprof.types;
import static domain.eSysprof.up;
import enums.ProfileSide;
import enums.TypeProfile;
import java.sql.SQLException;

public enum eSystree implements Field {
    up("0", "0", "0", "Дерево системы профилей", "SYSPROF"),
    id("4", "10", "0", "Идентификатор", "id"),
    name("12", "64", "1", "Наименование ветки дерева", "TEXT"),
    glas("12", "32", "1", "Заполнение по умолчанию", "ANUMB"),
    size("12", "128", "1", "Доступные толщины стеклопакетов", "ZSIZE"),
    col1("12", "128", "1", "Доступные основные текстуры", "CCODE"),
    col2("12", "128", "1", "Доступные внутрение текстуры", "CCOD1"),
    col3("12", "128", "1", "Доступные внешние текстуры", "CCOD2"),
    coef("8", "15", "1", "Коэффициент рентабельности", "KOEF"),
    types("5", "5", "1", "Типы 1 - окно; 4,5 - двери", "TYPEW"),
    parent_id("4", "10", "1", "ID материнского профиля", "parent_id");
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
    public static Query query = new Query(values()).table(up.tname());

    eSystree(Object... p) {
        meta.init(p);
    }

    public MetaField meta() {
        return meta;
    }

    public Field[] fields() {
        return values();
    }

    @Override
    public Query select() {
        if (query.size() == 0) {
            query.select(up, "order by", id);
        }
        return query;
    }

    public static Record find(int _nuni) {
        if (conf.equals("calc")) {
            return query.stream().filter(rec -> _nuni == rec.getInt(id)).findFirst().orElse(null);
        }
        Query recordList = new Query(values()).select(up, "where", id, "=", _nuni).table(up.tname());
        return (recordList.isEmpty() == true) ? null : recordList.get(0);
    }

    @Override
    public void virtualRec() throws SQLException {
        Query q = query.table(up.tname());
        Record record = q.newRecord(Query.INS);
        record.setNo(id, -1);
        record.setNo(glas, "4x10x4x10x4");
        q.insert(record);
    }

    public String toString() {
        return meta.getDescr();
    }
}
