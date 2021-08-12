package builder.script.test;

import builder.script.GsonElem;
import builder.script.GsonRoot;
import static builder.script.Winscript.rootGson;
import com.google.gson.GsonBuilder;
import enums.Layout;
import enums.Type;

public class Sokol {

    public static String script(Integer prj, boolean model) {

        if (prj == 1) {
            rootGson = new GsonRoot(prj, 1, 13, "SOKOL\\МП-40",
                    Layout.HORIZ, Type.RECTANGL, 1000, 1000, 2001, 2001, 2001);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT));
            rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1000 / 2))
                    .addElem(new GsonElem(Type.GLASS));
            rootGson.addElem(new GsonElem(Type.IMPOST));
            rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1000 / 2))
                    .addElem(new GsonElem(Type.GLASS));
        } else {
            return null;
        }
        if (model == true) {
            rootGson.propery(prj.toString(), -3, null);
        }
        return new GsonBuilder().create().toJson(rootGson);
    }
}
