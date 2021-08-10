package builder.script.test;

import builder.script.GsonElem;
import builder.script.GsonRoot;
import static builder.script.Winscript.rootGson;
import com.google.gson.GsonBuilder;
import enums.Layout;
import enums.Type;

public final class Vidnal {

    public static String script(Integer prj, boolean model) {
        
        if (prj == 26) {

            rootGson = new GsonRoot(prj, 2, 5, "VIDNAL V60/V60 окна-витражи",
                    Layout.VERT, Type.RECTANGL, 1090, 2470, 1000, 1000, 1000);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT));
            rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1430))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{'typeOpen':3, 'sysfurnID':180}"))
                    .addElem(new GsonElem(Type.GLASS, "{'artglasID':150}"));
            rootGson.addElem(new GsonElem(Type.IMPOST));
            rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1040))
                    .addElem(new GsonElem(Type.GLASS, "{'artglasID':150}"));

        } else {
            return null;
        }
        if (model == true) {
            rootGson.propery(prj.toString(), -3, null);
        }
        return new GsonBuilder().create().toJson(rootGson);
    }
}
