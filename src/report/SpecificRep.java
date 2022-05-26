package report;

import builder.making.Specific;
import domain.eColor;
import enums.UseUnit;
import java.text.DecimalFormat;

public class SpecificRep {

    private static DecimalFormat df1 = new DecimalFormat("#0.0");
    private static DecimalFormat df2 = new DecimalFormat("#0.00");
    private Specific spc;

    public SpecificRep(Specific spc) {
        this.spc = spc;
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
            return String.valueOf(df2.format(spc.quant2));
        }
    }

    public String getUnit() {
        return UseUnit.getName(spc.unit);
    }

    public String getPrice() {
        return String.valueOf(df1.format(spc.price1));
    }

    public String getCost() {
        return String.valueOf(df1.format(spc.cost1));
    }  
    
    public float getCost1() {
        return spc.cost1;
    }    
}
