package enums;

import static enums.UseUnit.values;

public enum TypeGroups implements Enam {
    EMPTY(1, "Empty"),
    COLOR_GRP(2, "МЦ группы цветов"),
    SERI_PROF(3, "Серии профилей"),
    PRICE_INC(4, "МЦ группы наценок"),
    PRICE_DEC(5, "МЦ группы скидок"),
    CATEG_PRF(6, "Категогии профилей"),
    COLOR_MAP(7, "Группы соответствия цветов"),
    CATEG_VST(8, "Категории вставок"),
    SYS_DATA(9, "Расчётные данные"),
    CATEG_KIT(10, "Категории комплектов");

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