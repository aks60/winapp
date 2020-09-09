package enums;

import static enums.UseColcalc.values;

public enum TypeFormProf implements Enam {

    P00(0, "не проверять форму"),
    P01(1, "профиль прямой"),
    P02(2, "профиль с радиусом"),
    P03(3, "прямоугольное заполнение без арок"),
    P04(4, "не прямоугольное, произвольное"),
    P06(6, "не прямоугольное, не арочное заполнение"),
    P07(7, "не прямоугольное заполнение с арками"),
    P08(8, "прямоугольное заполнение с арками");

    public int id;
    public String name;

    private TypeFormProf(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int numb() {
        return id;
    }

    public String text() {
        return name;
    }

    public Enam[] fields() {
        return values();
    }
}
