package builder.script.test;

import builder.script.GsonElem;
import builder.script.GsonRoot;
import static builder.script.GsonScript.rootGson;
import com.google.gson.GsonBuilder;
import enums.Layout;
import enums.Type;

public final class Sokol {

    public static String script(Integer prj) {

        if (prj == 1) {
            rootGson = new GsonRoot("1.0", prj, 1, 13, "SOKOL\\МП-40",
                    Layout.HORIZ, Type.RECTANGL, 1000, 1000, 2001, 2001, 2001);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1000 / 2))
                    .addElem(new GsonElem(Type.GLASS));
            rootGson.addElem(new GsonElem(Type.STOIKA))
                    .addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1000 / 2))
                    .addElem(new GsonElem(Type.GLASS));
        } else {
            return null;
        }
        return new GsonBuilder().create().toJson(rootGson);
    }
}
