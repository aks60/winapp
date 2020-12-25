package estimate.param;

import estimate.constr.Specification;

public class Processing {

    public static void calc(Specification spc, int code) {
        try {
            switch (code) {

                case 34000:

                    break;
                case 33001:

                    break;
            }
        } catch (Exception e) {
            System.err.println("Ошибка: param.Processing.calc() " + e);
        }
    }
}
