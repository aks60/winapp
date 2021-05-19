package enums;

//Типы элементов
public enum TypeElem implements Enam {

    //TypeElem
    FRAME_SIDE(2, 1, "Рама коробки"),
    IMPOST(3, 3, "Импост"),
    STVORKA_SIDE(4, 2, "Рама створки"), //элемент STVORKA
    GLASS(5, "Стеклопакет"),
    //STOIKA(6, 5, "Стойка"),
    SHTULP(10, 9, "Штульп"),
    PARAM(11, 9, "Параметры конструкции"),
    /*RASKLADKA(14, 7, "Раскладка"),
    MOSKITKA(13, "Москитка"),
    MOSKITKA_SET(-13, "Москитка"), //непонятно
    SAND(100, "Сэндвич"),
    JALOUSIE(101, "Жалюзи"),*/
    //TypeArea
    AREA(1000, "Контейнер"),
    RECTANGL(1001, "Окно четырёхугольное"),
    TRAPEZE(1002, "Окно трапеция"),
    TRIANGL(1003, "Треугольное окно"),
    ARCH(1004, "Арочное окно"),
    STVORKA(1005, "Створка"), //см. ElemFrame.typeProfile, ElemFrame.typeElem
    SUPPORT(1006, "Подкладка"),
    NONE(0, 0, "Не определено");

    public int id;
    public int id2; //это UseArtiklTo
    public String name;

    TypeElem(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    TypeElem(int id, int id2, String name) {
        this.id = id;
        this.id2 = id2;
        this.name = name;
    }
    
    public int numb() {
        return id;
    }    
}
