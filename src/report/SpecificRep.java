package report;

import builder.making.Specific;
import domain.eColor;
import enums.UseUnit;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class SpecificRep {

    private static DecimalFormat df1 = new DecimalFormat("#0.0");
    private static DecimalFormat df2 = new DecimalFormat("#0.00");
    private Specific spc;
    private boolean otx = true;

    public SpecificRep(Specific spc) {
        this.spc = spc;
    }

    public SpecificRep(Specific spc, boolean otx) {
        this.spc = spc;
        this.otx = otx;
    }

    public Specific spc() {
        return spc;
    }

    public String getArtikl() {
        return spc.artikl;
    }

    public String getName() {
        return spc.name;
    }

    public String getColorID1() {
        return eColor.find(spc.colorID1).getStr(eColor.name);
    }

    public String getCount() {
        if (spc.unit == UseUnit.PIE.id) {
            return String.valueOf(spc.count);
        } else {
            return df2.format(spc.quant2);
        }
    }

    public String getUnit() {
        return UseUnit.getName(spc.unit);
    }

    public String getWidth() {
        if (spc.width > 0) {
            return df2.format(spc.width);
        }
        return "";
    }

    public String getAngl() {
        if (spc.anglCut1 == 0 || spc.anglCut2 == 0) {
            return "";
        }
        return df1.format(spc.anglCut1) + " x " + df1.format(spc.anglCut2);
    }

    public String getWeight() {
        if (spc.weight > 0) {
            return df2.format(spc.weight);
        }
        return "";
    }

    public String getSpace() {
        return df1.format(spc.width) + " x " + df2.format(spc.height);
    }

    public String getPrice() {
        if (otx) {
            return df1.format(spc.price2);
        }
        return df1.format(spc.price1);
    }

    public String getCost() {
        if (otx) {
            return df1.format(spc.cost2);
        }
        return df1.format(spc.cost1);
    }

    //--------------------------------------------------------------------------  
    public static List<Specific> groups(List<Specific> listSpec, int num) {
        HashSet<String> hs = new HashSet();
        List<Specific> list = new ArrayList();
        Map<String, Specific> map = new HashMap();

        for (Specific spc : listSpec) {
            String key = (num == 1)
                    ? spc.name + spc.artikl + spc.colorID1 + spc.colorID2 + spc.colorID3 + spc.width + spc.height + spc.anglCut1 + spc.anglCut2 + spc.wastePrc + spc.price1
                    : (num == 2) ? spc.name + spc.artikl + spc.colorID1 + spc.colorID2 + spc.colorID3 + spc.wastePrc + spc.price1 : spc.artikl;
            if (hs.add(key)) {
                map.put(key, new Specific(spc));
            } else {
                Specific s = map.get(key);
                s.weight = s.weight + spc.weight;
                s.anglCut1 = 0;
                s.anglCut2 = 0;
                s.anglHoriz = 0;
                s.count = s.count + spc.count;
                s.quant1 = s.quant1 + spc.quant1;
                s.quant2 = s.quant2 + spc.quant2;
                s.price2 = s.price2 + spc.price2;
                s.cost1 = s.cost1 + spc.cost1;
                s.cost2 = s.cost2 + spc.cost2;
            }
        }
        map.entrySet().forEach(act -> list.add(act.getValue()));
        Collections.sort(list, (o1, o2) -> (o1.place.subSequence(0, 3) + o1.name).compareTo(o2.place.subSequence(0, 3) + o2.name));
        return list;
    } 
    
    public float getCost1() {
        return spc.cost1;
    }
}
