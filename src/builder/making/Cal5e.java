package builder.making;

import java.util.*;
import builder.Wincalc;
import builder.model.AreaSimple;
import dataset.Query;
import frames.swing.listener.ListenerCheck;

public abstract class Cal5e {

    public Wincalc iwin = null;
    public Set listVariants = new HashSet();
    public String conf = Query.conf;
    public boolean shortPass = false;    

    public Cal5e(Wincalc iwin) {
        this.iwin = iwin;
    }

    public void calc() {
        conf = Query.conf;
        Query.conf = "calc";
        listVariants.clear();
    }

    public AreaSimple rootArea() {
        return iwin.rootArea;
    }
}
