package enums;

import static enums.TypeOpen1.values;

public enum UseUnit implements Enam {
    METR(1, "пог.м."),
    METR2(2, "кв.м."),
    PIE(3, "шт."),
    ML(4, "мл."),
    SET(5, "комп."),
    KG(7, "кг"),
    LT(8, "литры"),
    PAIR(11, "пара");

    public int id = 0;
    public String name = "";

    UseUnit(int value, String name) {
        this.id = value;
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
    
    public static String getName(int index) {
        for (UseUnit unit: values()) {
            if (unit.id == index) {
                return unit.name;
            }
        }
        return "";
    }
}
