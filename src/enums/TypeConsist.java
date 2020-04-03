package enums;

public enum TypeConsist {
    P1(1, "внутренний"),
    P2(2, "армирование"),
    P3(3, "ламинирование"),
    P4(4, "покраска"),
    P5(5, "состав_С/П"),
    P6(6, "кронштейн_стойки"),
    P7(7, "дополнительно");

    int id;
    String name;

    TypeConsist(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
