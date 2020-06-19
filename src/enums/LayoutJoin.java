package enums;

import static enums.TypeOpen1.values;

public enum LayoutJoin implements Enam {

    NONE(0, 0, 0, "Не определено"),
    LTOP(1, 1, 2, "Угловое соединение левое верхнее"),
    LBOT(2, 1, 1, "Угловое соединение левое нижнее"),
    RTOP(3, 2, 2, "Угловое соединение правое верхнее"),
    RBOT(4, 2, 1, "Угловое соединение правое нижнее"),
    TTOP(5, 1, 0, "T - соединение верхнее"),
    TBOT(6, 1, 0, "T - соединение нижнее"),
    TLEFT(7, 1, 0, "T - соединение левое"),
    TRIGH(8, 1, 0, "T - соединение правое"),
    CRIGH(9, 0, 0, "Прилегающее правое"),
    CLEFT(10, 0, 0, "Прилегающее левое"),
    CTOP(11, 0, 0, "Прилегающее верхнее"),
    CBOT(12, 0, 0, "Прилегающее нижнее");

    public int id;
    public int angl1;
    public int angl2;
    public String name;

    LayoutJoin(int id, int angl1, int angl2, String name) {
        this.id = id;
        this.angl1 = angl1;
        this.angl2 = angl2;
        this.name = name;
    }

    LayoutJoin(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int numb() {
        return id;
    }

    public String text() {
        return name;
    }

    public Enam[] fields() {
        return values();
    }
}
