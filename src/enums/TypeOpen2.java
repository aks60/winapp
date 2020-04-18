
package enums;

import static enums.UseUnit.values;

public enum TypeOpen2 implements Enam {
    P1(1, "Запрос"),
    P2(2, "Левое"),
    P3(3, "Правое");

    public int id = 0;
    public String name = "";

    TypeOpen2(int value, String name) {
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