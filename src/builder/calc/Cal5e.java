package builder.calc;

import domain.eArtikl;
import enums.UseUnit;
import java.util.*;
import builder.Wincalc;
import builder.model.AreaSimple;
import builder.model.ElemSimple;
import dataset.Query;

public abstract class Cal5e {

    protected Wincalc iwin = null;
    public Set listVariants = new HashSet();
    protected String conf = Query.conf;

    public Cal5e(Wincalc iwin) {
        this.iwin = iwin;
    }

    public void calc() {
        conf = Query.conf;
        Query.conf = "calc";
        listVariants.clear();
    }

    public Wincalc iwin() {
        return iwin;
    }

    public AreaSimple root() {
        return iwin.rootArea;
    }
}
