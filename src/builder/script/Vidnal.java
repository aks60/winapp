package builder.script;

import static builder.script.Winscript.rootGson;
import com.google.gson.GsonBuilder;
import enums.LayoutArea;
import enums.TypeElem;

public final class Vidnal {

    public static String script(Integer project, boolean model) {
        
        if (project == 601001) {
            rootGson = new GsonRoot(project, 8, "KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)",
                    LayoutArea.VERT, TypeElem.RECTANGL, 900, 1300, 1009, 10009, 1009);
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
            rootGson.propery(project.toString(), -3, null);
        }
        return new GsonBuilder().create().toJson(rootGson);
    }
}
