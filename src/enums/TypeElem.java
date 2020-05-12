package enums;

//Типы элементов
public enum TypeElem implements Enam {

    //TypeElem
    FRAME_BOX(2, "Рама коробки"),
    IMPOST(3, "Импост"),
    FRAME_STV(4, "Рама створки"), //элемент FULLSTVORKA
    GLASS(5, "Стеклопакет"),
    SHTULP(10, "Штульп"),
    MOSKITKA(13, "Москитка"),
    RASKLADKA(14, "Раскладка"),
    MOSKITKA_SET(15, "Москитка"),
    STVORKA(23, "Створка"), //без импоста
    SAND(100, "Сэндвич"),
    JALOUSIE(101, "Жалюзи"),
    //TypeArea
    AREA(1000, "Контейнер компонентов"),
    RECTANGL(1001, "Окно четырёхугольное в сборе"),
    TRAPEZE(1002, "Окно трапеция в сборе"),
    TRIANGL(1003, "Треугольное окно в сборе"),
    ARCH(1004, "Арочное окно в сборе"),
    FULLSTVORKA(1005, "Створка в сборе"), //см. ElemFrame.typeProfile, ElemFrame.typeElem
    SUPPORT(1006, "Подкладка"),
    NONE(0, "Не определено");

    public int value;
    public String name;

    TypeElem(int value, String name) {
        this.value = value;
        this.name = name;
    }
}
