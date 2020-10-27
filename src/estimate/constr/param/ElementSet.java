package estimate.constr.param;

import estimate.Wincalc;
import estimate.constr.Specification;
import java.util.Map;

public class ElementSet extends Par5s {

    public ElementSet(Wincalc iwin) {
        super(iwin);
    }
    
    public void change(Specification spc) {
        int grup = -1;
        try {
            for (Map.Entry<Integer, String> entry : spc.mapParam.entrySet()) {
                grup = entry.getKey();
                switch (grup) {
                    case 34051:
                        //message(spc, grup);
                        spc.weight = spc.weight + spc.getParam(grup);
                        break;
                }
            }
        } catch (Exception e) {
            System.err.println("wincalc.constr.param.ElementDet.change()  parametr=" + grup + "    " + e);
        }
    }    
}
