package builder.script.test;

import builder.script.GsonElem;
import builder.script.GsonRoot;
import static builder.script.Winscript.rootGson;
import com.google.gson.GsonBuilder;
import enums.Layout;
import enums.TypeElem;

public final class Sial3 {
    
    public static String script(Integer prj, boolean model) {

        if (prj == 601001) {
            rootGson = new GsonRoot(prj, 1, 12, "СИАЛ\\КП45\\Окна",
                    Layout.VERT, TypeElem.RECTANGL, 800, 1200, 25, 25, 25);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, Layout.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, Layout.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, Layout.TOP));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, Layout.BOTT));
            rootGson.addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':285}"));

        } else if (prj == 601002) {
            rootGson = new GsonRoot(prj, 1, 32, "СИАЛ\\КП40\\Окна",
                    Layout.VERT, TypeElem.RECTANGL, 900, 600, 25, 25, 25);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, Layout.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, Layout.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, Layout.TOP));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, Layout.BOTT));
            rootGson.addArea(new GsonElem(Layout.VERT, TypeElem.STVORKA, "{'typeOpen':2}"))
                    .addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':285}"));

        } else if (prj == 601003) {
            rootGson = new GsonRoot(prj, 1, 32, "СИАЛ\\КП40\\Окна",
                    Layout.HORIZ, TypeElem.RECTANGL, 1200, 1600, 30, 30, 30);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, Layout.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, Layout.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, Layout.TOP));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, Layout.BOTT));
            rootGson.addArea(new GsonElem(Layout.HORIZ, TypeElem.AREA, 1200 / 2))
                    .addArea(new GsonElem(Layout.VERT, TypeElem.STVORKA, "{'typeOpen':3, 'sysfurnID':143}"))
                    .addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':285}")); 
            rootGson.addElem(new GsonElem(TypeElem.IMPOST, "{'sysprofID':357}"));
            rootGson.addArea(new GsonElem(Layout.HORIZ, TypeElem.AREA, 1200 / 2))
                    .addArea(new GsonElem(Layout.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':143}"))
                    .addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':285}"));

        } else if (prj == 601004) {
            rootGson = new GsonRoot(prj, 1, 12, "СИАЛ\\КП45\\Окна\\",
                    Layout.HORIZ, TypeElem.RECTANGL, 2010, 1600, 27, 27, 27);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, Layout.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, Layout.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, Layout.TOP));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, Layout.BOTT));
            rootGson.addArea(new GsonElem(Layout.HORIZ, TypeElem.AREA, 670))
                    .addArea(new GsonElem(Layout.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':13}"))
                    .addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':283}"));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));
            rootGson.addArea(new GsonElem(Layout.HORIZ, TypeElem.AREA, 670))
                    .addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':283}"));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));
            rootGson.addArea(new GsonElem(Layout.HORIZ, TypeElem.AREA, 670))
                    .addArea(new GsonElem(Layout.VERT, TypeElem.STVORKA, "{'typeOpen':2, 'sysfurnID':13}"))
                    .addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':283}"));

        } else if (prj == 601005) {
            rootGson = new GsonRoot(prj, 1, 12, "СИАЛ\\КП45\\Окна\\",
                    Layout.HORIZ, TypeElem.RECTANGL, 1800, 2020, 30, 30, 30);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, Layout.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, Layout.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, Layout.TOP));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, Layout.BOTT));
            GsonElem area2 = rootGson.addArea(new GsonElem(Layout.VERT, TypeElem.AREA, 900))
                    .addArea(new GsonElem(Layout.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':147}"))
                    .addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':286}"));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));
            GsonElem area3 = rootGson.addArea(new GsonElem(Layout.VERT, TypeElem.AREA, 900))
                    .addArea(new GsonElem(Layout.VERT, TypeElem.AREA, 800))
                    .addArea(new GsonElem(Layout.VERT, TypeElem.STVORKA, "{'typeOpen':2, 'sysfurnID':147}"))
                    .addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':286}"));
            area3.addElem(new GsonElem(TypeElem.IMPOST));
            area3.addArea(new GsonElem(Layout.VERT, TypeElem.AREA, 1220))
                    .addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':286}"));

        } else if (prj == 601006) {
            rootGson = new GsonRoot(prj, 1, 12, "СИАЛ\\КП45\\Окна\\",
                    Layout.HORIZ, TypeElem.RECTANGL, 3800, 1800, 37, 37, 37);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, Layout.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, Layout.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, Layout.TOP));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, Layout.BOTT));

            rootGson.addArea(new GsonElem(Layout.VERT, TypeElem.AREA, 633.33f))
                    .addArea(new GsonElem(Layout.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':147}"))
                    .addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':286}"));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));

            rootGson.addArea(new GsonElem(Layout.VERT, TypeElem.AREA, 633.33f))
                    .addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':286}"));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));

            rootGson.addArea(new GsonElem(Layout.VERT, TypeElem.AREA, 633.33f))
                    .addArea(new GsonElem(Layout.VERT, TypeElem.STVORKA, "{'typeOpen':2, 'sysfurnID':147}"))
                    .addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':286}"));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));

            rootGson.addArea(new GsonElem(Layout.VERT, TypeElem.AREA, 633.33f))
                    .addArea(new GsonElem(Layout.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':147}"))
                    .addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':286}"));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));

            rootGson.addArea(new GsonElem(Layout.VERT, TypeElem.AREA, 633.33f))
                    .addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':286}"));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));

            rootGson.addArea(new GsonElem(Layout.VERT, TypeElem.AREA, 633.33f))
                    .addArea(new GsonElem(Layout.VERT, TypeElem.STVORKA, "{'typeOpen':2, 'sysfurnID':147}"))
                    .addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':286}"));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));
        } else {
            return null;
        }
        if (model == true) {
            rootGson.propery(prj.toString(), -3, null);
        }

        return new GsonBuilder().create().toJson(rootGson);
    } 
}
