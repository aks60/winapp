package enums;

import dataset.Record;
import java.util.ArrayList;

/**
 *
 * @author aks
 */
public class ParamCol extends Record {

    int par1;
    int par2;
    String par3;

    ParamCol() {
        super(3);
    }

    public enum E {
        PAR1(3, "Ключ 1"),
        PAR2(4, "Ключ 2"),
        PAR3(5, "Значение");

        String name = "";
        int value = -1;

        E(int valuse, String name) {
            this.name = name;
            this.value = value;
        }
    }
}
