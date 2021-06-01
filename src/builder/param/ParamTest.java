package builder.param;

import builder.model.ElemSimple;
import dataset.Record;
import domain.eArtikl;
import enums.LayoutArea;
import java.util.HashMap;

public class ParamTest {

    public static void param() {

        Record record = null;
        HashMap<Integer, String> hmParam = new HashMap();
        builder.Wincalc iwin = new builder.Wincalc();
        iwin.build(builder.script.Winscript.test(601003, false));
        iwin.constructiv(true);
        //ElemSimple elem = iwin.listElem.stream().filter(el -> el.type() == TypeElem.IMPOST).findFirst().orElse(null);
        ElemSimple elem = iwin.rootArea.mapFrame.get(LayoutArea.LEFT);
        ElementVar elementVar = new ElementVar(iwin);
        ElementDet elementDet = new ElementDet(iwin);
        
        System.out.println(elem.artiklRecAn);
        
        boolean bool = elementVar.check(elem, newRecord("KBE 58;", 31000));
        
        
        System.out.println(bool);
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
