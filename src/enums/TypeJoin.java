package enums;

//То что пишет профстрой в базу данных
public enum TypeJoin {

    EMPTY(00, "Тип соединеия не установлен"),
    VAR10(10, "Прилегающее соединение"),
    VAR20(20, "Угловое на ус"),
    VAR30(30, "Угловое левое"),
    VAR31(31, "Угловое правое"),
    VAR40(40, "Т - образное соединение (импост,ригель)"),
    VAR41(41, "Т - образное соединение `конус` (импост)");

    public int id;
    public String name;

    TypeJoin(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
