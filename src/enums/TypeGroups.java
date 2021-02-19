package enums;

import static enums.UseUnit.values;

public enum TypeGroups implements Enam {
    P1(1, "Empty"),
    COLOR(2, "Группы цветов"),
    SERI_PROF(3, "Серии профилей"),
    PRICE_INC(4, "Группы наценок"),
    PRICE_DEC(5, "Группы скидок"),
    FILTER(6, "Категогии профилей"),
    COLMAP(7, "Группы соответствия цветов"),
    CATEGINS(8, "Категории вставок");

    public int id = 0;
    public String name = "";

    TypeGroups(int id, String name) {
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