package enums;

import java.util.Arrays;

public enum LayoutArea {
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
}
