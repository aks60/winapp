package enums;

import static enums.TypeOpen1.values;
import java.util.Arrays;

public enum LayoutArea implements Enam {
    NONE("Любая"),
    FULL("Везде"),
    HORIZ("Горизонтальное"),
    VERT("Вертикальное"),
    LEFT("Левая"),
    RIGHT("Правая"),
    TOP("Верхняя"),
    BOTTOM("Нижняя"),
    LSKEW("Левый угол"),
    RSKEW("Правый угол"),
    ARCH("Арка");

    public String name;
    public int value;

    LayoutArea(String name) {
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
}
