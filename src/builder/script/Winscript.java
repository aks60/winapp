package builder.script;

import builder.script.test.Alutech3;
import builder.script.test.Alutex3;
import builder.script.test.Vidnal;
import builder.script.test.Sial3;
import builder.script.test.Krauss;
import builder.script.test.Bimax;
import builder.script.test.Sokol;
import common.eProp;
import java.util.Arrays;
import java.util.List;

public class Winscript {

    public static GsonRoot rootGson;

    public static String test(Integer prj, boolean nuni) {
        String base_name = (eProp.base_num.read().equals("1")) ? eProp.base1.read()
                : (eProp.base_num.read().equals("2")) ? eProp.base2.read() : eProp.base3.read();

        if (base_name.toLowerCase().contains("sial3")) {
            return Sial3.script(prj, nuni);

        } else if (base_name.toLowerCase().contains("alutex3")) {
            return Alutex3.script(prj, nuni);

        } else if (base_name.toLowerCase().contains("alutech3")) {
            return Alutech3.script(prj, nuni);

        } else if (base_name.toLowerCase().contains("bimax")) {
            return Bimax.script(prj, nuni);

        } else if (base_name.toLowerCase().contains("vidnal")) {
            return Vidnal.script(prj, nuni);

        } else if (base_name.toLowerCase().contains("krauss")) {
            return Krauss.script(prj, nuni);

        } else if (base_name.toLowerCase().contains("sokol")) {
            return Sokol.script(prj, nuni);
        }
        return null;
    }

    public static List<Integer> models(String p) {
        String base_name = (eProp.base_num.read().equals("1")) ? eProp.base1.read()
                : (eProp.base_num.read().equals("2")) ? eProp.base2.read() : eProp.base3.read();

        if (base_name.toLowerCase().contains("sial3.fdb")) {
            return List.of(601001, 601002, 601003, 601004, 601007, 601008);

        } else if (base_name.toLowerCase().contains("alutech3.fdb")) {
            return List.of(601001, 601002, 601003, 601004);

        } else if (base_name.toLowerCase().contains("alutex3.fdb")) {
            return List.of(4);

        } else if (base_name.toLowerCase().contains("bimax.fdb")) {
            return ("max".equals(p)) ? List.of(
                    601001, 601002, 601003, 601004, 601005, 601006, 601007, 601008, 601009, 601010, //прямоугольные окна
                    700027, 604004, 604005, 604006, 604007, 604008, 604009, 604010, //арки
                    605001, 508916, 508945, 508841, 700009, 700014) //трапеции, двери
                    : List.of(601001, 601002, 601003, 601004, 601005, 601006,
                            601007, 601008, 601009, 601010, 604005, 604006, 604007, 604008, 604009, 604010);

        } else if (base_name.toLowerCase().contains("vidnal.fdb")) {
            return List.of(26);

        } else if (base_name.toLowerCase().contains("krauss.fdb")) {
            return null;

        } else if (base_name.toLowerCase().contains("sokol.fdb")) {
            return List.of(1);
        }
        return null;
    }

    public static String path() {
        String base_name = (eProp.base_num.read().equals("1")) ? eProp.base1.read()
                : (eProp.base_num.read().equals("2")) ? eProp.base2.read() : eProp.base3.read();

        if (base_name.toLowerCase().contains("sial3.fdb")) {
            return "D:\\Okna\\Database\\ps3\\sial3b.fdb";

        } else if (base_name.toLowerCase().contains("alutech3.fdb")) {
            return "D:\\Okna\\Database\\ps3\\alutech3.FDB";

        } else if (base_name.toLowerCase().contains("alutex3.fdb")) {
            return "D:\\Okna\\Database\\ps3\\alutex3.FDB";

        } else if (base_name.toLowerCase().contains("bimax.fdb")) {
            return "D:\\Okna\\Database\\ps4\\ITEST.FDB";

        } else if (base_name.toLowerCase().contains("vidnal.fdb")) {
            return "D:\\Okna\\Database\\ps4\\vidnal.fdb";

        } else if (base_name.toLowerCase().contains("krauss.fdb")) {
            return "D:\\Okna\\Database\\ps4\\krauss.fdb";

        } else if (base_name.toLowerCase().contains("sokol.fdb")) {
            return "D:\\Okna\\Database\\ps4\\sokol.fdb";
        }
        return null;
    }
}
