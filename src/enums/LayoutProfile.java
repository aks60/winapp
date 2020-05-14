package enums;

public enum LayoutProfile implements Enam {
    
    LEFT("Левая"),
    RIGHT("Правая"),
    TOP("Верхняя"),
    BOTTOM("Нижняя"),
    //LSKEW("Левый угол"),
    //RSKEW("Правый угол"),
    ARCH("Арка");

    public String name;
    public int value;

    LayoutProfile(String name) {
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
