package builder.param;

import builder.model.ElemSimple;
import dataset.Record;
import domain.eArtikl;
import enums.LayoutArea;
import java.util.HashMap;

public class ParamTest {

    public static void param() {

        Record record = null;
        ElemSimple elem = null;
        HashMap<Integer, String> hmParam = new HashMap();
        builder.Wincalc iwin_601004 = new builder.Wincalc();
        iwin_601004.build(builder.script.Winscript.test(601004, false));
        iwin_601004.constructiv(true);
        elem = iwin_601004.rootArea.mapFrame.get(LayoutArea.LEFT);
        ElementVar elemVar_601004 = new ElementVar(iwin_601004);
     
        assert true == elemVar_601004.check(elem, newRecord("KBE 58;XXX 58;", 31000));
        assert false == elemVar_601004.check(elem, newRecord("KBE58;", 31000));
        
        //System.out.println(elem.artiklRecAn);
        
//        JoiningVar joiningVar = new JoiningVar(iwin);        
//        JoiningDet joiningDet = new JoiningDet(iwin);        
//        joiningDet.check(hmParam, elem5e, elemdetRec);
//*/     
    }

    public static Record newRecord(String txt, int grup) {
        Record record = new Record();
        record.add("SEL");
        record.add(-3);
        record.add(txt);
        record.add(grup);
        record.add(-3);
        return record;
    }
}
