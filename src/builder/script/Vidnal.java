package builder.script;

import static builder.script.Winscript.rootGson;
import com.google.gson.GsonBuilder;
import enums.LayoutArea;
import enums.TypeElem;

public final class Vidnal {

    public static String script(Integer prj, boolean model) {
        
        if (prj == 26) {

            rootGson = new GsonRoot(prj, 2, 5, "VIDNAL V60/V60 окна-витражи",
                    LayoutArea.VERT, TypeElem.RECTANGL, 1090, 2470, 1000, 1000, 1000);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addArea(new GsonElem(LayoutArea.HORIZ, TypeElem.AREA, 1430))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':3, 'sysfurnID':180}"))
                    .addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':150}"));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));
            rootGson.addArea(new GsonElem(LayoutArea.HORIZ, TypeElem.AREA, 1040))
                    .addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':150}"));

        } else {
            return null;
        }
        if (model == true) {
            rootGson.propery(prj.toString(), -3, null);
        }
        return new GsonBuilder().create().toJson(rootGson);
    }
}
