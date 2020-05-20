package enums;

// Стороны для профилей (SYSPROA.ASETS)

import static enums.UseArtiklTo.values;
import static enums.TypeOpen1.values;
import java.util.stream.Stream;

// select distinct ASETS from PRO4_SYSPROA where region_id = 177 order by ASETS
public enum UseSide implements Enam {    
    MANUAL(0, "Вручную"), 
    ANY(-1, "Любая"),
    HORIZ(-2, "Горизонтальная"),
    VERT(-3, "Вертикальная"),
    BOTTOM(1, "Нижняя"),
    RIGHT(2, "Правая"),
    TOP(3, "Верхняя"),
    LEFT(4, "Левая");

    public int id;
    public String name;

    UseSide(int id, String name) {
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
    
    public static UseSide get(int id) {
        return Stream.of(values()).filter(en -> en.numb() == id).findFirst().orElse(null);
    }
}
