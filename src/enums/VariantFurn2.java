package enums;

public enum VariantFurn2 {
    P1(1, "ведомая"),
    P2(2, "обычная"),
    P3(3, "ведущая"),
    P4(4, "адаптер");

    public int id;
    public String name;

    VariantFurn2(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String text() {
        return name;
    }
}
