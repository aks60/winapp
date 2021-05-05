package builder.script;

import static builder.script.Winscript.rootGson;
import com.google.gson.GsonBuilder;
import enums.LayoutArea;
import enums.TypeElem;

public final class Vidnal {

    public static String script(Integer prj, boolean model) {
        
        if (prj == 1) {
            rootGson = new GsonRoot(prj, 3, 5, "VIDNAL F50/F50 стоечно-ригельная",
                    LayoutArea.VERT, TypeElem.RECTANGL, 2500, 2500, 1009, 10009, 1009);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            
            rootGson.addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':1634}"))
                    .addElem(new GsonElem(TypeElem.GLASS));

        } else {
            return null;
        }
        if (model == true) {
            rootGson.propery(prj.toString(), -3, null);
        }
        return new GsonBuilder().create().toJson(rootGson);
    }
}
