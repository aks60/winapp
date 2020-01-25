
package enums;


public enum TypeSys {
    
    EMPTY(0, "Не определено"),
    WIN_OPEN_IN(1, "Окно открывание внутрь"),
    WIN_OPEN_OUT(2, "Окно открывание наружу"),
    WIN_EXP(3, "Окно раздвижное"),
    DOOR_OPEN_IN(4, "Дверь открывание внутрь"),
    DOOR_OPEN_OUT(5, "Дверь открывание наружу"),
    MOSQUITO_NET(6, "Москитная сетка"),
    GLASS_PAC(7, "Стеклопакет"),
    ROLLT(8, "Роллета"),
    VITRAG(9, "Витраж");

    public int value;
    public String name;

    TypeSys(int value, String name) {
        this.value = value;
        this.name = name;
    }   
}
