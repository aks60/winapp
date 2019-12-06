package enums;

public enum eLayoutArea {
    NONE("Любая"),
    FULL("Везде"),
    HORIZONTAL("Горизонтальное"),
    VERTICAL("Вертикальное"),
    LEFT("Левая"),
    RIGHT("Правая"),
    TOP("Верхняя"),
    BOTTOM("Нижняя"),
    LSKEW("Левый угол"),
    RSKEW("Правый угол"),
    ARCH("Арка");

    public String name = "";

    eLayoutArea(String name) {
        this.name = name;
    }
}
