package builder.script;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import common.eProperty;
import domain.eSetting;
import enums.LayoutArea;
import enums.TypeElem;

public class Winscript {

    public static GsonRoot rootGson;

    public static String test(Integer project, boolean model) {
        String base_name = (eProperty.base_num.read().equals("1")) ? eProperty.base1.read()
                : (eProperty.base_num.read().equals("2")) ? eProperty.base2.read() : eProperty.base3.read();
        
        if (base_name.contains("sial3.fdb")) {
            return ITest.script(project, model);
            
        } else if (base_name.contains("itest.fdb")) {
            return ITest.script(project, model);
            
        } else if (base_name.contains("vidnal.fdb")) {
            return Vidnal.script(project, model);
        }
        return null;
    }
}
