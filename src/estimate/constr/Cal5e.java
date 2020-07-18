package estimate.constr;

import domain.eArtikl;
import enums.UseUnit;
import java.util.*;
import estimate.Wincalc;
import estimate.model.AreaSimple;
import estimate.model.ElemSimple;

public abstract class Cal5e {

    private Wincalc iwin = null;
    public Set listVariants = new HashSet();

    public Cal5e(Wincalc iwin) {
        this.iwin = iwin;
    }

    public abstract void calc();

    public Wincalc iwin() {
        return iwin;
    }

    public AreaSimple root() {
        return iwin.rootArea;
    }  
}
