package enums;

import static enums.TypeOpen1.values;

public enum ParamJson implements Enam {

    sysfurnID("Фурнитура"),
    typeOpen("Тип открывания"),
    colorHandl("Цвет ручки створки"),
    pro4Params("Параметры iokna"),
    pro4Params2("Параметры iokna"),
    nunic_iwin("Уникальный идентификатор артикула");
    String name = "";

    ParamJson(String name) {
        this.name = name;
    }

    public int numb() {
        return this.ordinal();
    }

    public String text() {
        return name;
    }
    
    public Enam[] fields() {
        return values();
    }
}
