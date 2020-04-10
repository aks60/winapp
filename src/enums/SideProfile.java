package enums;

// Стороны для профилей (SYSPROA.ASETS)
// select distinct ASETS from PRO4_SYSPROA where region_id = 177 order by ASETS
public enum SideProfile {
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

    SideProfile(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static SideProfile get(int side) {
        for (SideProfile profileSide : values()) {
            if (profileSide.value == side) {
                return profileSide;
            }
        }
        return null;
    }
}
