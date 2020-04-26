package enums;

import static enums.UseUnit.values;

public enum TypeGroups implements Enam {
    P1(1, "Empty"),
    P2(2, "Empty"),
    SERIES(3, "Серии профилей");

    public int id = 0;
    public String name = "";

    TypeGroups(int id, String name) {
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