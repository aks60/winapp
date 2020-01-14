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
    glas("12", "32", "1", "Заполнение по умолчанию", "ANUMB"),
    size("12", "128", "1", "Доступные толщины стеклопакетов", "ZSIZE"),
    tex1("12", "128", "1", "Доступные основные текстуры", "CCODE"),
    tex2("12", "128", "1", "Доступные внутрение текстуры", "CCOD1"),
    tex3("12", "128", "1", "Доступные внешние текстуры", "CCOD2"),
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
    public static Query q = new Query(values()).table(up.tname());

    eSystree(Object... p) {
        meta.init(p);
    }

    public MetaField meta() {
        return meta;
    }

    public Field[] fields() {
        return values();
    }

    public String toString() {
        return meta.getDescr();
    }
}
