package builder.making;

import java.util.*;
import builder.Wincalc;
import builder.model.IArea5e;
import dataset.Query;

public abstract class Cal5e {

    public Wincalc winc = null;
    public Set setVariant = new HashSet();
    public String conf = Query.conf;
    public boolean shortPass = false;    

    public Cal5e(Wincalc winc) {
        this.winc = winc;
    }

    public void calc() {
        conf = Query.conf;
        Query.conf = "calc";
        setVariant.clear();
    }

    public IArea5e rootArea() {
        return winc.rootArea;
    }
}
