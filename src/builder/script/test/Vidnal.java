package builder.script.test;

import builder.script.GsonElem;
import builder.script.GsonRoot;
import static builder.script.GsonScript.rootGson;
import com.google.gson.GsonBuilder;
import enums.Layout;
import enums.Type;

public final class Vidnal {

    public static String script(Integer prj) {

        if (prj == 26) {

            rootGson = new GsonRoot("1.0", prj, 2, 5, "VIDNAL V60/V60 окна-витражи",
                    Layout.VERT, Type.RECTANGL, 1090, 2470, 1000, 1000, 1000);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1430))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen:3, sysfurnID:180}"))
                    .addElem(new GsonElem(Type.GLASS, "{artglasID:150}"));
            rootGson.addElem(new GsonElem(Type.IMPOST))
                    .addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1040))
                    .addElem(new GsonElem(Type.GLASS, "{artglasID:150}"));

        } else {
            return null;
        }
        return rootGson.toJson();
    }
}
