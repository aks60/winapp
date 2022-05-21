package report;

import builder.Wincalc;
import builder.making.Specific;
import common.ArrayList2;
import java.util.List;

public class ReportDocx {
    
    public static void outGoMaterial(List<Wincalc> listWinc, ArrayList2<Specific> listSpec, String in) {
        ArrayList2<Specific> list = new ArrayList2();
        for (Wincalc win: listWinc) {
                win.constructiv(true);
                list.addAll(win.listSpec);
            }
        }        
}
