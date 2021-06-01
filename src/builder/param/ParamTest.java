package builder.param;

import builder.model.ElemSimple;
import dataset.Record;
import enums.Enam;
import enums.TypeElem;
import java.util.Arrays;
import java.util.HashMap;

public class ParamTest {

    public static void param(Integer prj, boolean model) {
        
        HashMap<Integer, String> hmParam = new HashMap();
        builder.Wincalc iwin = new builder.Wincalc();
        iwin.build(builder.script.Winscript.test(prj, model));
        iwin.constructiv(true);
        ElemSimple elemImpost = iwin.listElem.stream().filter(el -> el.type() == TypeElem.IMPOST).findFirst().orElse(null);
        System.out.println(elemImpost);   
/*        
        ElementVar elementVar = new ElementVar(iwin);
        ElementDet elementDet = new ElementDet(iwin);        
        System.out.println(elementVar.check(elemImpost, new Record(Arrays.asList("SEL", -3, "", 31033, -3))));
        
//        JoiningVar joiningVar = new JoiningVar(iwin);        
//        JoiningDet joiningDet = new JoiningDet(iwin);        
//        joiningDet.check(hmParam, elem5e, elemdetRec);
//*/
        for (Enam value : ParamList.values()) {

        }       
    }
}
