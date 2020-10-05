package enums;

import static enums.TypeOpen1.values;

public enum ParamJson implements Enam {

    sysfurnID(1, "ID Фурнитуры"),
    typeOpen(2, "Тип открывания"),
    colorHandl(3, "Цвет ручки створки"),
    ioknaParam(4, "Параметры iokna"),    
    artikleID(6, "ID артикула"),
    sysprofID(7, "ID профиля");

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
