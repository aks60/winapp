package estimate.param;

import enums.LayoutArea;
import estimate.constr.Specification;
import estimate.model.AreaSimple;
import estimate.model.ElemFrame;
import java.util.HashMap;

public class Processing {

    public static void set(Specification spc, int code) {
        try {
            switch (code) {

                case 34000:

                    break;
                case 33001:

                    break;
            }
        } catch (Exception e) {
            System.err.println("Ошибка:param.Processing.set() " + e);
        }
    }

    public static ElemFrame determOfSide(HashMap<Integer, String> mapParam, AreaSimple area5e) {

        if ("1".equals(mapParam.get(25010))) {
            return area5e.mapFrame.get(LayoutArea.BOTTOM);
        } else if ("2".equals(mapParam.get(25010))) {
            return area5e.mapFrame.get(LayoutArea.RIGHT);
        } else if ("3".equals(mapParam.get(25010))) {
            return area5e.mapFrame.get(LayoutArea.TOP);
        } else if ("4".equals(mapParam.get(25010))) {
            return area5e.mapFrame.get(LayoutArea.LEFT);
        } else {
            return area5e.mapFrame.values().stream().findFirst().get();  //первая попавшаяся
        }
    }
}
