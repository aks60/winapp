package enums;

import static enums.TypeOpen1.values;
import java.util.Arrays;

//Расположение area  и profile объединены в один список Enum
public enum LayoutArea implements Enam {
    ANY(-1, "Любая"),
    HORIZ(-2, "Горизонтальное"),
    VERT(-3, "Вертикальное"),
    BOTTOM(1, "Нижняя"),
    RIGHT(2, "Правая"),
    TOP(3, "Верхняя"),
    LEFT(4, "Левая"),
    ARCH(5, "Арочная"),
    FULL(6, "Везде");

    //LSKEW("Левый угол"),
    //RSKEW("Правый угол"),
    public int id;
    public String name;

    LayoutArea(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int numb() {
        return this.ordinal();
    }

    public String text() {
        return name;
    }

    public Enam[] fields() {
        return values();
    }

//    public static LayoutArea find(int side) {
//      return Arrays.asList(values()).stream().filter(it -> it.id == side).findFirst().orElse(null);
//    }
}
