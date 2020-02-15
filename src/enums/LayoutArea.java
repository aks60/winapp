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

//    public static LayoutArea get(String str) {
//        LayoutArea layout2 = Arrays.asList(LayoutArea.values()).stream().filter(it -> it.name().equals(str)).findFirst().orElse(null);
//        for (LayoutArea val : values()) {
//            if (val.name().equals(str)) {
//                layout2 = val;
//            }
//        }
//        return layout2;
//    }
}
