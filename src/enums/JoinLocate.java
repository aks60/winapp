package enums;

public enum JoinLocate {

    NONE(1, "Не определено"),
    LTOP(1, "Угловое соединение левое верхнее"),
    LBOT(2, "Угловое соединение левое нижнее"),
    RTOP(3, "Угловое соединение правое верхнее"),
    RBOT(4, "Угловое соединение правое нижнее"),
    TTOP(5, "T - соединение верхнее"),
    TBOT(6, "T - соединение нижнее"),
    TLEFT(7, "T - соединение левое"),
    TRIGH(8, "T - соединение правое");
    
    public int value;
    public String name;

    JoinLocate(int value, String name) {
        this.value = value;
        this.name = name;
    }    
}
