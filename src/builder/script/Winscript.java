package builder.script;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import common.eProperty;
import domain.eSetting;
import enums.LayoutArea;
import enums.TypeElem;

public class Winscript {

    public static GsonRoot rootGson;

    public static String test(Integer prj, boolean model) {
        String base_name = (eProperty.base_num.read().equals("1")) ? eProperty.base1.read()
                : (eProperty.base_num.read().equals("2")) ? eProperty.base2.read() : eProperty.base3.read();
        
        if (base_name.toLowerCase().contains("sial.fdb")) {
            return Sial.script(prj, model);
            
        } else if (base_name.toLowerCase().contains("bimax.fdb")) {
            return Bimax.script(prj, model);
            
        } else if (base_name.toLowerCase().contains("vidnal.fdb")) {
            return Vidnal.script(prj, model);
        }
        return null;
    }
}
