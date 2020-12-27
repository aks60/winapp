package builder.specif;

import domain.eArtikl;
import enums.UseUnit;
import java.util.*;
import builder.Wincalc;
import builder.model.AreaSimple;
import builder.model.ElemSimple;

public abstract class Cal5e {

    protected Wincalc iwin = null;
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
