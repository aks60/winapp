package common;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class DecimalFormat2 extends DecimalFormat {

    public DecimalFormat2(String pattern) {
        super(pattern);
    }

    public DecimalFormat2(String pattern, DecimalFormatSymbols symbols) {
        super(pattern, symbols);
    }
    
    public String frm(Object obj) {
        Object ret = (obj == null) ? 0 : obj;
        return super.format(ret);
    }
}
