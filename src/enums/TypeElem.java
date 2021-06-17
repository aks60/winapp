package enums;

//Типы элементов
public enum TypeElem implements Enam {
//1-"коробка", 2-"створка", 3-"импост", 4-"ригель/импост", 5-"стойка", 6-"стойка/коробка, 7-"эркер", 8-"грань
    
    //TypeElem
    FRAME_SIDE(1, 1, "Коробка сторона"),
    STVORKA_SIDE(2, 2, "Створка сторона"),
    IMPOST(3, 3, "Импост"),
    RIGEL_IMPOST(4, "Ригель/импост"),
    STOIKA(5, 5, "Стойка"),
    STOIKA_FRAME(6, "Стойка/коробка"),
    SHTULP(7, 9, "Штульп"),
    ERKER(8, 10, "Эркер"),
    GLASS(9, "Заполнение (Стеклопакет, стекло"),    
    PARAM(11, 9, "Параметры конструкции"),
    MOSKITKA(13, "Москитка"),
    RASKLADKA(14, 7, "Раскладка"),
    SAND(15, "Сэндвич"),
    JALOUSIE(15, "Жалюзи"),
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
    public int id2 = 0; //это UseArtiklTo
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
