package builder.script;

import static builder.script.Winscript2.rootGson;
import com.google.gson.GsonBuilder;
import enums.Layout;
import enums.Type;

public class WinModell {

    public static String script(String name) {

        switch (name) {
            case "xxx":
                rootGson = new GsonRoot("1.0", -1, -1, -1, "KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)",
                        Layout.VERT, Type.RECTANGL, 900, 1300, 1009, 10009, 1009);
                rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                        .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                        .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                        .addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                        .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen:1, sysfurnID:1634}"))
                        .addElem(new GsonElem(Type.GLASS));
                break;
            default:
                throw new AssertionError();
        }

        return new GsonBuilder().create().toJson(rootGson);
    }
}
