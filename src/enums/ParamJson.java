package enums;

import static enums.TypeOpen1.values;

public enum ParamJson implements Enam {

    sysprofID(1, "ID профиля"),
    sysfurnID(2, "ID Фурнитуры"),
    artglasID(3, "ID заполнения"),
    typeOpen(4, "Тип открывания"),
    colorHandl(5, "Цвет ручки створки"),
    ioknaParam(6, "Параметры iokna");       
    
    int id = 0;
    String name = "";

    ParamJson(int id, String name) {
        this.id = id;
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
