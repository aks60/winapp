package enums;

import static enums.TypeOpen1.values;
import java.util.Arrays;

//Расположение area  и profile объединены в один список Enum
public enum Layout implements Enam {
    ANY(-1, "Любая"),
    HORIZ(-2, "Горизонт."),
    VERT(-3, "Вертикальн."),
    BOTT(1, "Нижняя"),
    RIGHT(2, "Правая"),
    TOP(3, "Верхняя"),
    LEFT(4, "Левая"),
    SPEC(5, "Специальн."),
    FULL(6, "");

    //LSKEW("Левый угол"),
    //RSKEW("Правый угол"),
    public int id;
    public String name;

    Layout(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int numb() {
        return id;
    }

    public String text() {
        return name;
    }

    public Enam[] fields() {
        return values();
    }
}
