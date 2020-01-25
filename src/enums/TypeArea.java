package enums;

//НЕ ИСПОЛЬЗУЮ!!!
public enum TypeArea {
    AREA(1000, "Контейнер компонентов"),
    SQUARE(1001, "Окно четырёхугольное"),
    TRAPEZE(1002, "Окно трапеция"),
    TRIANGL(1003, "Треугольное окно"),
    ARCH(1004, "Арочное окно"),
    FULLSTVORKA(1005, "Створка в сборе"),
    NONE(1009, "Не определено");
   
    public int value;
    public String name;

    TypeArea(int value, String name) {
        this.value = value;
        this.name = name;
    }
}
