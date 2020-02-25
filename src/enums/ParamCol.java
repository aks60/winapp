package enums;

/**
 *
 * @author aks
 */
public enum ParamCol {
    PAR1(3, "Ключ 1"),
    PAR2(4, "Ключ 2"),
    PAR3(5, "Значение");

    String name = "";
    int value = -1;

    ParamCol(int valuse, String name) {
        this.name = name;
        this.value = value;
    }
}
