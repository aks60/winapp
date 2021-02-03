package enums;

import static enums.TypeOpen1.values;

public enum ParamJson implements Enam {

    sysprofID("ID профиля"),
    sysfurnID("ID Фурнитуры"),
    artglasID("ID заполнения"),
    typeOpen("Тип открывания"),
    artiklHandl("Ручка на створке"),
    colorHandl("Цвет ручки створки"),
    ioknaParam("Параметры iokna"),
    rama("Рама створки"),
    colorID1("Текстура"),
    colorID2("Текстура"),
    colorID3("Текстура");

    int id = 0;
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
