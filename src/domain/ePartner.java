package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;

public enum ePartner implements Field {
    up("0", "0", "0", "Диллер", "CLIENTS"),
    id("4", "10", "0", "Идентификатор", "id"),
    name("12", "64", "1", "название контрагента", "KNAME"),
    categ("12", "32", "1", "категория (заказчик, поставщик, офис, дилер, специальный)", "KPREF"),
    types("5", "5", "1", "частное лицо (0 - частное, 1 - организация)", "KCHAS"),
    city("12", "64", "1", "город", "KTOWN"),
    land("12", "64", "1", "страна", "KCOUN"),
    addr("12", "192", "1", "почтовый адрес", "KADRP"),
    phone("12", "32", "1", "телефон", "KTELE"),
    email("12", "64", "1", "е-mail", "KMAIL"),
    bank_name("12", "128", "1", "банк", "KBANK"),
    bank_inn("12", "64", "1", "ИНН", "KBAN1"),
    bank_rs("12", "64", "1", "Р/С", "KBAN2"),
    bank_bik("12", "64", "1", "БИК", "KBAN3"),
    bank_ks("12", "64", "1", "К/С", "KBAN4"),
    bank_kpp("12", "64", "1", "КПП", "KBAN5"),
    bank_ogrn("12", "32", "1", "ОГРН", "KOGRN"),
    manager("12", "32", "1", "менеджер", "PNAME"),
    disc("8", "15", "1", "скидки по умолчанию", "CDESC");
//    kunic("4", "10", "1", "null", "KUNIC"),
//    krekl("4", "10", "1", "null", "KREKL"),
//    pricl("4", "10", "1", "null", "PRICL"),
//    knamf("12", "128", "1", "полное название контрагента", "KNAMF"),
//    idnum("12", "32", "1", "null", "IDNUM"),
//    tmark("12", "64", "1", "null", "TMARK"),
//    ktype("12", "32", "1", "тип организации", "KTYPE"),
//    kaddr("12", "192", "1", "юридический адрес", "KADDR"),
//    kvtel("12", "32", "1", "внутренний телефон", "KVTEL"),
//    kfaxx("12", "32", "1", "факс", "KFAXX"),
//    krnam("12", "48", "1", "null", "KRNAM"),
//    kkdol("12", "48", "1", "должность", "KKDOL"),
//    kbase("12", "48", "1", "null", "KBASE"),
//    kbnam("12", "48", "1", "null", "KBNAM"),
//    kknam("12", "48", "1", "контактное лицо", "KKNAM"),
//    kgrup("12", "96", "1", "null", "KGRUP"),
//    kpasp("12", "32", "1", "паспорт частного лица", "KPASP"),
//    kpasv("12", "128", "1", "паспорт выдан", "KPASV"),
//    kdepa("5", "5", "1", "номер отдела", "KDEPA"),
//    kprim("-4", "null", "1", "примечание", "KPRIM"),
//    jcntr("5", "5", "1", "заметки", "JCNTR"),
//    saldo("8", "15", "1", "сальдо", "SALDO"),
//    kcred("8", "15", "1", "null", "KCRED"),
//    kdate("93", "19", "1", "дата заведения", "KDATE"),
//    desc1("8", "15", "1", "скидки на профиль %", "DESC1"),
//    desc2("8", "15", "1", "скидки на аксессуары", "DESC2"),
//    desc3("8", "15", "1", "скидка на уплотнение", "DESC3"),
//    desc5("8", "15", "1", "скидка на заполнение", "DESC5"),
//    kflag("5", "5", "1", "скидка менеджера", "KFLAG"),
//    dnumb("12", "32", "1", "null", "DNUMB"),
//    face1("12", "48", "1", "null", "FACE1"),
//    post1("12", "48", "1", "null", "POST1"),
//    mail1("12", "48", "1", "null", "MAIL1"),
//    tele1("12", "48", "1", "null", "TELE1"),
//    face2("12", "48", "1", "null", "FACE2"),
//    post2("12", "48", "1", "null", "POST2"),
//    mail2("12", "48", "1", "null", "MAIL2"),
//    tele2("12", "48", "1", "null", "TELE2"),
//    face3("12", "48", "1", "null", "FACE3"),
//    post3("12", "48", "1", "null", "POST3"),
//    mail3("12", "48", "1", "null", "MAIL3"),
//    tele3("12", "48", "1", "null", "TELE3"),
//    gnumb("5", "5", "1", "null", "GNUMB"),
//    cuser("4", "10", "1", "null", "CUSER");    
    private MetaField meta = new MetaField(this);
    public static Query query = new Query(values()).table(up.tname());

    ePartner(Object... p) {
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
