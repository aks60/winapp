package builder.script.test;

import builder.script.GsonElem;
import builder.script.GsonRoot;
import static builder.script.WinScript.rootGson;
import com.google.gson.GsonBuilder;
import enums.Layout;
import enums.Type;

public final class Krauss {

    public static String script(Integer prj, boolean nuni) {

        if (prj == 4) {

            rootGson = new GsonRoot("1.0", prj, 1, 0, "VIDNAL V60/V60 окна-витражи",
                    Layout.VERT, Type.RECTANGL, 1090, 2470, 1000, 1000, 1000);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1430))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen:3, sysfurnID:180}"))
                    .addElem(new GsonElem(Type.GLASS, "{artglasID:150}"));
            rootGson.addElem(new GsonElem(Type.IMPOST))
                    .addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1040))
                    .addElem(new GsonElem(Type.GLASS, "{artglasID:150}"));

        } else {
            return null;
        }
        if (nuni == false) {
            rootGson.propery(prj.toString(), -3, null);
        }
        return new GsonBuilder().create().toJson(rootGson);
    }
}
