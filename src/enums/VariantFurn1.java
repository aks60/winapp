package enums;

public enum VariantFurn1 {
    P1(1, "поворотная"),
    P2(2, "раздвижная"),
    P3(3, "раздвижная<=>"),
    P4(4, "раздвижная|^|");

    public int id;
    public String name;

    VariantFurn1(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String text() {
        return name;
    }
}
