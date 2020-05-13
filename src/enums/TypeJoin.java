package enums;

public enum TypeJoin {

    EMPTY(0, 0, "Тип соединеия не установлен"),
    VAR1(1, 0, "Прилегающее соединение"),
    VAR2(2, 0, "Угловое на ус"),
    VAR3L(3, 0, "Угловое левое"),
    VAR3R(3, 1, "Угловое правое"),
    VAR4(4, 0, "Т - образное соединение (импост,ригель)"),
    VAR5(4, 1, "Т - образное соединение `конус` (импост)");

    public int value;
    public int value2;
    public String name;

    TypeJoin(int value, int value2, String name) {
        this.value = value;
        this.name = name;
    }
}
