package enums;

import static enums.UseColcalc.values;

public enum TypeFormProf implements Enam {

    P00(1, "не проверять форму"), //или 0 по умолчанию
    P02(2, "профиль прямой"),
    P04(4, "профиль с радиусом"),
    P06(6, "прямоугольное заполнение без арок"),
    P08(8, "не прямоугольное, произвольное"),
    P10(10, "не прямоугольное, не арочное заполнение"),
    P12(12, "не прямоугольное заполнение с арками"),
    P14(14, "прямоугольное заполнение с арками");

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
