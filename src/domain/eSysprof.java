package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Record;

public enum eSysprof implements Field {
    up("0", "0", "0", "Профили, системы профилей", "SYSPROF"),
    nuni("4", "10", "1", "ID ветки дерева", "NUNI"),
    nlev("5", "5", "1", "номер ступеньки в дереве", "NLEV"),
    npar("4", "10", "1", "ID материнского профиля (SYSPROF.NUNI)", "NPAR"),
    anumb("12", "32", "1", "  Заполнение по умолчанию", "ANUMB"),
    text("12", "64", "1", "наименование ветки дерева", "TEXT"),
    koef("8", "15", "1", "Коэффициент рентабельности", "KOEF"),
    zsize("12", "128", "1", "Доступные толщины стеклопакетов", "ZSIZE"),
    ccode("12", "128", "1", "Доступные основные текстуры", "CCODE"),
    ccod1("12", "128", "1", "Доступные внутрение текстуры", "CCOD1"),
    ccod2("12", "128", "1", "Доступные внешние текстуры", "CCOD2"),
    tcalk("5", "5", "1", "Спецификация профилей и пакетов для сборки", "TCALK"),
    tview("5", "5", "1", "Примечание", "TVIEW"),
    tdrit("5", "5", "1", "Режим проволоки в системе (0 - запрещен, 1 - разрешен)", "TDRIT"),
    nprim("-4", "512", "1", "null", "NPRIM"),
    ngrup("12", "96", "1", "Система доступная для групп пользователя", "NGRUP"),
    npref("12", "32", "1", "Замена / код", "NPREF"),
    pnump("5", "5", "1", "null", "PNUMP"),
    pnumn("5", "5", "1", "null", "PNUMN"),
    typew("5", "5", "1", "1 - окно; 4,5 - двери", "TYPEW");
    private MetaField meta = new MetaField(this);

    eSysprof(String... p) {
        meta.init(p);
    }

    public MetaField meta() {
        return meta;
    }

    public Field[] fields() {
        return values();
    }

    public String table() {
        return "SYSPROF";
    }

    public String toString() {
        return meta.getDescr();
    }
}
