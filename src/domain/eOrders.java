package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;

public enum eOrders implements Field {
    up("0", "0", "0", "Список заказов (проектов)", "LISTPRJ"),
    id("4", "10", "0", "Идентификатор", "id"),
    numb("12", "32", "1", "номер заказа", "ZNUMB"),
    partner_id("4", "10", "0", "контрагент", "partner_id"),
    manager_id("4", "10", "1", "менеджер", "manager_id"),
    constr_id("4", "10", "1", "конструктор", "constr_id"),
    sale_name("12", "64", "1", "продавец", "SNAME"),
    space("8", "15", "1", "площадь изделий", "PSQRA"),
    weight("8", "15", "1", "вес изделий", "EMPTY"),
    desc("8", "15", "1", "дополнительная скидка", "XDESC"),
    dat1("93", "19", "1", "дата отправки в производство", "WDATE"),
    dat2("93", "19", "1", "дополнительная дата", "dat2");

//    зnumb("4", "10", "1", "номер проекта", "PNUMB"),
//    ndepa("5", "5", "1", "номер отдела", "NDEPA"),    
//    punic("4", "10", "1", "null", "PUNIC"),
//    cnumb("4", "10", "1", "валюта название", "CNUMB"),
//    pnum2("5", "5", "1", "суффикс проекта", "PNUM2"),
//    pincn("4", "10", "1", "сквозной номер", "PINCN"),    
//    inumb("12", "32", "1", "номер счета", "INUMB"),
//    ppref("12", "32", "1", "категория", "PPREF"),
//    pdate("93", "19", "1", "дата регистрации", "PDATE"),
//    cdate("93", "19", "1", "дата расчета", "CDATE"),    
//    cname("12", "64", "1", "конструктор", "CNAME"),
//    pobja("12", "128", "1", "объект", "POBJA"),
//    mname("12", "64", "1", "менеджер", "MNAME"),
//    kname("12", "64", "1", "контрагент", "KNAME"),  
//    pobjl("-4", "null", "1", "описание объекта", "POBJL"),
//    pprim("-4", "null", "1", "примечание", "PPRIM"),
//    ptype("5", "5", "1", "тип расчтета", "PTYPE"),
//    ctype("5", "5", "1", "учет норм отходов", "CTYPE"),
//    kdesc("8", "15", "1", "скидка на конструкции", "KDESC"),
//    adesc("8", "15", "1", "скидка на комплектацию", "ADESC"),
//    wdesc("8", "15", "1", "скидка на работы", "WDESC"),
//    pdesc("8", "15", "1", "скидка общая", "PDESC"),
//    psebe("8", "15", "1", "себестоимость проекта", "PSEBE"),
//    kpric("8", "15", "1", "стоимость конструкций без скидки", "KPRIC"),
//    apric("8", "15", "1", "стоимость комплектации без скидки", "APRIC"),
//    wpric("8", "15", "1", "стоимость работ без скидок", "WPRIC"),
//    ppric("8", "15", "1", "стоимость проекта без скидок", "PPRIC"),
//    kprcd("8", "15", "1", "стоимость конструкции со скидками", "KPRCD"),
//    aprcd("8", "15", "1", "стоимость комплектации сос кидками", "APRCD"),
//    wprcd("8", "15", "1", "стоимость работ со скидками", "WPRCD"),
//    pprcd("8", "15", "1", "стоимость проекта соскидками", "PPRCD"),
//    kursm("8", "15", "1", "курс основной валюты", "KURSM"),
//    kursv("8", "15", "1", "курс внктренний валюты", "KURSV"),
//    pwork("5", "5", "1", "отправка в производства", "PWORK"),
//    tunic("5", "5", "1", "null", "TUNIC"),
//    prese("5", "5", "1", "резерв", "PRESE"),
//    pstat("12", "64", "1", "статус", "PSTAT"),
//    wdate("93", "19", "1", "дата отправки в производство", "WDATE"),
//    psend("5", "5", "1", "обмен", "PSEND"),
//    pdocs("5", "5", "1", "null", "PDOCS"),
//    psqra("8", "15", "1", "площадь изделия кв. м", "PSQRA"),
//    exp1c("5", "5", "1", "экспорт в 1 С", "EXP1C"),
//    pflag("4", "10", "1", "флаг пользователя", "PFLAG"),    
//    sname("12", "64", "1", "продавец", "SNAME"),
//    msumm("8", "15", "1", "оплата", "MSUMM"),
//    kprim("-4", "null", "1", "конфиденциально", "KPRIM"),
//    nunic("4", "10", "1", "null", "NUNIC"),
//    pricl("4", "10", "1", "null", "PRICL"),
//    pmarg("8", "15", "1", "наценка %", "PMARG"),
//    descr("4", "10", "1", "null", "DESCR"),
//    pequp("5", "5", "1", "флаг состояния выгрузки", "PEQUP"),
//    preme("12", "100", "1", "null", "PREME"),
//    ework("5", "5", "1", "флаг планирования", "EWORK"),
//    pnump("4", "10", "1", "null", "PNUMP"),
//    xdesc("8", "15", "1", "дополнительная скидка", "XDESC"),
//    desc1("8", "15", "1", "скидка на профиль", "DESC1"),
//    desc2("8", "15", "1", "скидка на аксессуары", "DESC2"),
//    desc3("8", "15", "1", "скидка на уплотнения", "DESC3"),
//    desc5("8", "15", "1", "скидка на заполнения", "DESC5"),
//    knacd("8", "15", "1", "null", "KNACD"),
//    anacd("8", "15", "1", "null", "ANACD"),
//    wnacd("8", "15", "1", "null", "WNACD"),
//    kdesd("8", "15", "1", "null", "KDESD"),
//    adesd("8", "15", "1", "null", "ADESD"),
//    wdesd("8", "15", "1", "null", "WDESD"),
//    pdesd("8", "15", "1", "null", "PDESD"),
//    xdesd("8", "15", "1", "null", "XDESD"),
//    pricd("8", "15", "1", "null", "PRICD"),
//    wdiff("8", "15", "1", "null", "WDIFF"),
//    adiff("8", "15", "1", "null", "ADIFF"),
//    gdate("93", "19", "1", "null", "GDATE"),
//    dowin("8", "15", "1", "null", "DOWIN"),
//    dsumm("8", "15", "1", "null", "DSUMM"),
//    prcda("8", "15", "1", "null", "PRCDA"),
//    prcdw("8", "15", "1", "null", "PRCDW"),
//    prcdx("8", "15", "1", "null", "PRCDX"),
//    prcw1("8", "15", "1", "null", "PRCW1"),
//    prcw2("8", "15", "1", "null", "PRCW2"),
//    pname("12", "64", "1", "null", "PNAME"),
//    psysp("4", "10", "1", "null", "PSYSP"),
//    clnum("4", "10", "1", "null", "CLNUM"),
//    clnu1("4", "10", "1", "null", "CLNU1"),
//    clnu2("4", "10", "1", "null", "CLNU2"),
//    anumb("12", "32", "1", "null", "ANUMB"),
//    dates("12", "48", "1", "null", "DATES"),
//    date1("93", "19", "1", "настраиваемая дата", "DATE1"),
//    date2("93", "19", "1", "null", "DATE2"),
//    date3("93", "19", "1", "null", "DATE3");
    private MetaField meta = new MetaField(this);
    public static Query query = new Query(values()).table(up.tname());

    eOrders(Object... p) {
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
