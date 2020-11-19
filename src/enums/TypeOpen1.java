package enums;

import static enums.TypeUse.values;

/**
 * Типы открывания створок
 */
public enum TypeOpen1 implements Enam {

    INVALID(-1, "empty", "Ошибка"),
    FIXED(0, "empty", "Глухая створка (не открывается)"),
    LEFT(1, "Левое", "Левая поворотная (открывается справа-налево, ручка справа)"),
    RIGHT(2, "Правое", "Правая поворотная (открывается слева-направо, ручка слева)"),
    LEFTUP(3, "Левое", "Левая поворотно-откидная"),
    RIGHTUP(4, "Правое", "Правая поворотно-откидная"),
    UPPER(5, "", "Откидная (открывается сверху)"),
    LEFTSHIFT(11, "Левое", "Раздвижная влево (открывается справа-налево, защелка справа"),
    RIGHTSHIFT(12, "Правое", "Раздвижная вправо (открывается слева-направо, защелка слева");

    public int id;
    public String side;
    public String name;

    TypeOpen1(int id, String side, String name) {
        this.id = id;
        this.side = side;
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

    public static TypeOpen1 get(int id) {
        for (TypeOpen1 typeOpen : values()) {
            if (typeOpen.id == id) {
                return typeOpen;
            }
        }
        return null;
    }
}
