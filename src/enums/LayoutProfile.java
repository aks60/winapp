package enums;

// Стороны для профилей (SYSPROA.ASETS)

import static enums.TypeOpen.values;

// select distinct ASETS from PRO4_SYSPROA where region_id = 177 order by ASETS
public enum LayoutProfile implements Enam {
    VERT(-3, "Вертикальная"),
    HORIZ(-2, "Горизонтальная"),
    ANY(-1, "Любая"),
    MANUAL(0, "Вручную"),
    BOTTOM(1, "Низ"),
    RIGHT(2, "Правая"),
    TOP(3, "Верх"),
    LEFT(4, "Левая");

    public int id;
    public String name;

    LayoutProfile(int id, String name) {
        this.id = id;
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
    
    public static LayoutProfile get(int side) {
        for (LayoutProfile profileSide : values()) {
            if (profileSide.id == side) {
                return profileSide;
            }
        }
        return null;
    }
}
