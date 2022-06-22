package domain;

import dataset.Field;
import dataset.MetaField;
import dataset.Query;
import dataset.Record;

public enum eProject implements Field {
    up("0", "0", "0", "Список заказов (проектов)", "LISTPRJ"),
    id("4", "10", "0", "Идентификатор", "id"),
    num_ord("12", "32", "1", "Номер заказа", "ZNUMB"),
    num_acc("12", "32", "1", "Номер счета", "INUMB"),         
    manager("12", "64", "1", "Менеджер", "MNAME"), //это user который создаёт проект
    square("8", "15", "1", "Площадь изделий", "PSQRA"),
    weight("8", "15", "1", "Вес изделий", "EMPTY"),   
    type_calc("5", "5", "1", "Тип расчтета", "PTYPE"),
    type_norm("5", "5", "1", "Учет норм отходов", "CTYPE"),
    pric1("8", "15", "1", "Cебестоимость проекта", "PSEBE"),
    pric2("8", "15", "1", "Cтоимость проекта без скидок", "PPRIC"),
    pric3("8", "15", "1", "Cтоимость конструкций без скидки", "KPRIC"),
    pric4("8", "15", "1", "Cтоимость комплектации без скидки", "APRIC"),    
    cost2("8", "15", "1", "Cтоимость проекта со скидками", "PPRCD"),    
    cost3("8", "15", "1", "Cтоимость конструкции со скидками", "KPRCD"),
    cost4("8", "15", "1", "Cтоимость комплектации со скидками", "APRCD"),            
    decr1("8", "15", "1", "Cкидка общая", "PDESC"),
    decr2("8", "15", "1", "Cкидка на конструкции", "KDESC"),
    decr3("8", "15", "1", "Cкидка на комплектацию", "ADESC"),
    decr4("8", "15", "1", "Скидка на заполнения", "DESC5"),
    incr1("8", "15", "1", "Наценка %", "PMARG"),  
    flag1("5", "5", "1", "Отправка в производство", "PWORK"),   
    date4("93", "19", "1", "Дата регистрации заказа", "PDATE"),
    date5("93", "19", "1", "Дата расчета заказа", "CDATE"),
    date6("93", "19", "1", "Дата отпр. в производство", "WDATE"),
    owner("12", "32", "1",  "User", "owner"),
    currenc_id("4", "10", "1", "Валюта", "CNUMB"),
    prjpart_id("4", "10", "1", "Партнёр", "prjpart_id");

    private MetaField meta = new MetaField(this);
    private static Query query = new Query(values());

    eProject(Object... p) {
        meta.init(p);
    }
    public static Record find(int _id) {
        if (_id == -3) {
            return up.newRecord();
        }
        if (Query.conf.equals("calc")) {
            return query().stream().filter(rec -> _id == rec.getInt(id)).findFirst().orElse(up.newRecord());
        }
        Query recordList = new Query(values()).select(up, "where", id, "='", _id, "'");
        return (recordList.isEmpty() == true) ? up.newRecord() : recordList.get(0);
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
