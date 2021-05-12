package builder.script;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import common.eProperty;
import domain.eSetting;
import enums.LayoutArea;
import enums.TypeElem;
import java.util.Arrays;
import java.util.List;

public class Winscript {

    public static GsonRoot rootGson;

    public static String test(Integer prj, boolean model) {
        String base_name = (eProperty.base_num.read().equals("1")) ? eProperty.base1.read()
                : (eProperty.base_num.read().equals("2")) ? eProperty.base2.read() : eProperty.base3.read();
        
        if (base_name.toLowerCase().contains("sial3.fdb")) {
            return Sial3.script(prj, model);
            
        } else if (base_name.toLowerCase().contains("bimax.fdb")) {
            return Bimax.script(prj, model);
            
        } else if (base_name.toLowerCase().contains("vidnal.fdb")) {
            return Vidnal.script(prj, model);
            
        } else if (base_name.toLowerCase().contains("krauss.fdb")) {
            return Krauss.script(prj, model);
        }
        return null;
    }
    
    public static List<Integer> models() {
        String base_name = (eProperty.base_num.read().equals("1")) ? eProperty.base1.read()
                : (eProperty.base_num.read().equals("2")) ? eProperty.base2.read() : eProperty.base3.read();
        
        if (base_name.toLowerCase().contains("sial3.fdb")) {
            return Arrays.asList(601001, 601002, 601003, 601004);
            
        } else if (base_name.toLowerCase().contains("bimax.fdb")) {
            return Arrays.asList(601001, 601002, 601003, 601004, 601005, 601006, 601007, 601008, 601009, 601010, 604004, 604005, 604006, 604007, 604008, 604009, 604010);
            
        } else if (base_name.toLowerCase().contains("vidnal.fdb")) {
            return Arrays.asList(26);

        } else if (base_name.toLowerCase().contains("krauss.fdb")) {
            return null;
        }
        return null;
    }
}
