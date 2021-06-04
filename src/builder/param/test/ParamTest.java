package builder.param.test;

import builder.model.ElemImpost;
import builder.model.ElemSimple;
import builder.param.ElementDet;
import builder.param.ElementVar;
import dataset.Record;
import enums.LayoutArea;
import java.util.HashMap;

public class ParamTest {

    protected HashMap<Integer, String> hmParam = new HashMap();
    protected int grup = -1;
    protected Record record = null;
    protected ElemSimple frame_side_left_2 = null;
    protected ElemSimple imp_horiz_2 = null;
    protected ElemSimple imp_vert_2 = null;
    
    builder.Wincalc iwin_2 = new builder.Wincalc(); //601004
    protected ElementVar elemVar_2 = null;
    protected ElementDet elemDet_2 = null;

    public ParamTest() {
        
        iwin_2.build(builder.script.Winscript.test(601004, false));
        iwin_2.constructiv(true);
        elemVar_2 = new ElementVar(iwin_2);
        elemDet_2 = new ElementDet(iwin_2);
        frame_side_left_2 = iwin_2.rootArea.mapFrame.get(LayoutArea.LEFT);
        imp_horiz_2 = iwin_2.listElem.stream().filter(it -> it.id() == 12).findFirst().orElse(null);
        imp_vert_2 = iwin_2.listElem.stream().filter(it -> it.id() == 7).findFirst().orElse(null);
    }

    public static Record param(String txt, int grup) {
        Record record = new Record();
        record.add("SEL");
        record.add(-3);
        record.add(txt);
        record.add(grup);
        record.add(-3);
        return record;
    }
}
