package enums;

//Типы элементов
public enum TypeElem implements Enam {

    //TypeElem
    FRAME_SIDE(2, "Рама коробки"),
    IMPOST(3, "Импост"),
    STVORKA_SIDE(4, "Рама створки"), //элемент STVORKA
    GLASS(5, "Стеклопакет"),
    SHTULP(10, "Штульп"),    
    RASKLADKA(14, "Раскладка"),
    MOSKITKA(13, "Москитка"),
    MOSKITKA_SET(-13, "Москитка"), //непонятно
    //STVORKA(23, "Створка"), //без импоста
    SAND(100, "Сэндвич"),
    JALOUSIE(101, "Жалюзи"),
    //TypeArea
    AREA(1000, "Контейнер"),
    RECTANGL(1001, "Окно четырёхугольное"),
    TRAPEZE(1002, "Окно трапеция"),
    TRIANGL(1003, "Треугольное окно"),
    ARCH(1004, "Арочное окно"),
    STVORKA(1005, "Створка"), //см. ElemFrame.typeProfile, ElemFrame.typeElem
    SUPPORT(1006, "Подкладка"),
    NONE(0, "Не определено");

    public int id;
    public String name;

    TypeElem(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
