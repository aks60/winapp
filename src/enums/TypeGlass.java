package enums;

import static enums.TypeOpen1.values;

/**
 * Заполнение стеклопакета
 * Пока не использую т.к. параметр текстовый
 */
public enum TypeGlass implements Enam {

    RECTANGL(1, "Прямоугольное"),
    NOT_RECTANGU(2, "Не прямоугольное"),
    ARCHED(3, "Арочное"),
    NOT_ARCHED(4, "Не арочное");

    public int id = 0;
    public String name = "";

    TypeGlass(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int numb() {
        return Integer.valueOf(id);
    }

    public String text() {
        return name;
    }
    
    public Enam[] fields() {
        return values();
    }    
}
