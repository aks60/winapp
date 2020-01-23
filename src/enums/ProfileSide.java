package enums;

// Стороны для профилей (SYSPROA.ASETS)
// select distinct ASETS from PRO4_SYSPROA where region_id = 177 order by ASETS
public enum ProfileSide {
    Vert(-3, "Вертикальная"),
    Horiz(-2, "Горизонтальная"),
    Any(-1, "Любая"),
    Manual(0, "Вручную"),
    Bottom(1, "Низ"),
    Right(2, "Правая"),
    Top(3, "Верх"),
    Left(4, "Левая");

    public int value;
    public String name;

    ProfileSide(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static ProfileSide get(LayoutArea layout) {
        if (layout.VERT == layout) {
            return Vert;
        } else if (layout.HORIZ == layout) {
            return Horiz;
        } else if (layout.BOTTOM == layout) {
            return Bottom;
        } else if (layout.RIGHT == layout) {
            return Right;
        } else if (layout.TOP == layout) {
            return Top;
        } else if (layout.LEFT == layout) {
            return Left;
        }
        return null;
    }
}
