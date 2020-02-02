package enums;

// Стороны для профилей (SYSPROA.ASETS)
// select distinct ASETS from PRO4_SYSPROA where region_id = 177 order by ASETS
public enum ProfileSide {
    VERT(-3, "Вертикальная"),
    HORIZ(-2, "Горизонтальная"),
    ANY(-1, "Любая"),
    MANUAL(0, "Вручную"),
    BOTTOM(1, "Низ"),
    RIGHT(2, "Правая"),
    TOP(3, "Верх"),
    LEFT(4, "Левая");

    public int value;
    public String name;

    ProfileSide(int value, String name) {
        this.value = value;
        this.name = name;
    }
     
    public static ProfileSide get(int layout) {
        if (VERT.ordinal() == layout) {
            return VERT;
        } else if (HORIZ.ordinal() == layout) {
            return HORIZ;
        } else if (BOTTOM.ordinal() == layout) {
            return BOTTOM;
        } else if (RIGHT.ordinal() == layout) {
            return RIGHT;
        } else if (TOP.ordinal() == layout) {
            return TOP;
        } else if (LEFT.ordinal() == layout) {
            return LEFT;
        }
        return null;
    }
}
