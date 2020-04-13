package enums;

import static enums.SideFurn1.values;

public enum VariantFurn2 implements Enam {
    P1(-1, "ведомая"),
    P2(0, "обычная"),
    P3(1, "ведущая"),
    P4(2, "адаптер");

    public int id;
    public String name;

    VariantFurn2(int id, String name) {
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
