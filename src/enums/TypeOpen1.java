package enums;

import static enums.TypeUse.values;

/**
 * Типы открывания створок
 */
public enum TypeOpen1 implements Enam {

    OM_INVALID(-1, "empty", "Ошибка"),
    OM_FIXED(0, "empty", "Глухая створка (не открывается)"),
    OM_LEFT(1, "Левое", "Левая поворотная (открывается справа-налево, ручка справа)"),
    OM_RIGHT(2, "Правое", "Правая поворотная (открывается слева-направо, ручка слева)"),
    OM_LEFTUPPER(3, "Левое", "Левая поворотно-откидная"),
    OM_RIGHTUPPER(4, "Правое", "Правая поворотно-откидная"),
    OM_UPPER(5, "", "Откидная (открывается сверху)"),
    OM_LEFTSHIFT(11, "Левое", "Раздвижная влево (открывается справа-налево, защелка справа"),
    OM_RIGHTSHIFT(12, "Правое", "Раздвижная вправо (открывается слева-направо, защелка слева");

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
