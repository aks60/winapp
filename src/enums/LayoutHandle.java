package enums;

import static enums.TypeOpen2.values;

public enum LayoutHandle implements Enam {
    P1(1, "По середине"),
    P3(2, "Константная");

    public int id = 0;
    public String name = "";

    LayoutHandle(int value, String name) {
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
}
