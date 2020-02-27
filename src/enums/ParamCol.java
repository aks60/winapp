package enums;

/**
 *
 * @author aks
 */
public class ParamCol {

    int par1;
    int par2;
    String par3;
    
    public enum P {
        PAR1(3, "Ключ 1"),
        PAR2(4, "Ключ 2"),
        PAR3(5, "Значение");

        String name = "";
        int value = -1;

        P(int valuse, String name) {
            this.name = name;
            this.value = value;
        }
    }
}
