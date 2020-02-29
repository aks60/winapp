package wincalc.constr;

import dataset.Record;
import domain.eColor;
import java.util.*;
import wincalc.Wincalc;
import wincalc.model.AreaSimple;
import wincalc.model.Com5t;

public class Cal5e {

    protected Wincalc iwin = null;
    private CalcConstructiv calc = null;

    public Cal5e(Wincalc iwin, CalcConstructiv calc) {
        this.iwin = iwin;
        this.calc = calc;
    } 
    
    public CalcConstructiv calc() {
        return calc;
    }
    
    public Wincalc iwin() {
        return iwin;
    }

    public AreaSimple root() {
        return iwin.rootArea;
    }
}
