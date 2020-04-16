package enums;

import static enums.TypeOpen.values;

public enum LayoutJoin implements Enam {

    NONE(1, "Не определено"),
    LTOP(1, "Угловое соединение левое верхнее"),
    LBOT(2, "Угловое соединение левое нижнее"),
    RTOP(3, "Угловое соединение правое верхнее"),
    RBOT(4, "Угловое соединение правое нижнее"),
    TTOP(5, "T - соединение верхнее"),
    TBOT(6, "T - соединение нижнее"),
    TLEFT(7, "T - соединение левое"),
    TRIGH(8, "T - соединение правое");
    
    public int id;
    public String name;

    LayoutJoin(int id, String name) {
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
