package enums;

public enum LayoutFrame implements Enam {
    P1(0, "Изнутри"),
    P2(1, "Снаружи"),
    P3(2, "Со стороны открывания");

    public int id;
    public String name;

    LayoutFrame(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int numb() {
        return this.ordinal();
    }

    public String text() {
        return name;
    }
    
    public Enam[] fields() {
        return values();
    }
}
