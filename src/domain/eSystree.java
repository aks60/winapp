package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;
import static domain.eArtdet.values;

public enum eSystree implements Field {
    up("0", "0", "0", "Дерево системы профилей", "SYSPROF"),
    id("4", "10", "0", "Идентификатор", "id"),
    name("12", "64", "1", "Наименование ветки дерева", "TEXT"),
    glas("12", "32", "0", "Заполнение по умолчанию", "ANUMB"),
    size("12", "128", "0", "Доступные толщины стеклопакетов", "ZSIZE"),
    col1("12", "128", "0", "Доступные основные текстуры", "CCODE"),
    col2("12", "128", "0", "Доступные внутрение текстуры", "CCOD1"),
    col3("12", "128", "0", "Доступные внешние текстуры", "CCOD2"),
    coef("8", "15", "0", "Коэффициент рентабельности", "KOEF"),
    prip("4", "10", "0", "Припуск на сварку", "SSIZP"),
    napl("4", "10", "0", "Наплав системы", "SSIZN"),
    naxl("4", "10", "0", "Нахлест створки", "SSIZF"),
    zax("4", "10", "0", "Заход импоста", "SSIZI"),
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
    public Query selectSql() {
        if (query.size() == 0) {
            query.select(up, "order by", id);
        }
        return query;
    }

    public String toString() {
        return meta.getDescr();
    }
}
