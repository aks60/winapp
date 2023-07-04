package builder.script.test;

import builder.script.GeoElem;
import builder.script.GeoRoot;
import static builder.script.GeoScript.rootGeo;
import builder.script.GsonElem;
import static builder.script.GsonScript.rootGson;
import com.google.gson.GsonBuilder;
import enums.Type;
import java.util.List;

public final class Bimax2 {

    public static GeoRoot rootGeo;

    public static String script(Integer prj) {

        if (prj == 50100145) { //PUNIC = 427595
            rootGeo = new GeoRoot("2.0", prj, 2, 8, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)");
            rootGeo.addElem(new GeoElem(Type.FRAME_SIDE, 100, 350, 350, 350))
                    .addElem(new GeoElem(Type.FRAME_SIDE, 350, 350, 400, 100))
                    .addElem(new GeoElem(Type.FRAME_SIDE, 400, 100, 350, 50))
                    .addElem(new GeoElem(Type.FRAME_SIDE, 350, 50, 100, 50))
                    .addElem(new GeoElem(Type.FRAME_SIDE, 100, 50, 50, 100))
                    .addElem(new GeoElem(Type.FRAME_SIDE, 50, 100, 40, 177))
                    .addElem(new GeoElem(Type.FRAME_SIDE, 40, 177, 100, 350));

            rootGeo.addArea(new GeoElem(Type.AREA))
                    .addElem(new GeoElem(Type.GLASS));
            rootGeo.addElem(new GeoElem(Type.IMPOST, 100, 10, 280, 500));
            GeoElem area = rootGeo.addArea(new GeoElem(Type.AREA));

            area.addArea(new GeoElem(Type.AREA))
                    .addElem(new GeoElem(Type.GLASS));
            area.addElem(new GeoElem(Type.IMPOST, 80, 200, 200, 200));
            area.addArea(new GeoElem(Type.AREA))
                    .addElem(new GeoElem(Type.GLASS));

        } else if (prj == 501001) { //PUNIC = 427595
            rootGeo = new GeoRoot("2.0", prj, 2, 8, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)");
            rootGeo.addElem(new GeoElem(Type.FRAME_SIDE, 0, 0, 0, 900))
                    .addElem(new GeoElem(Type.FRAME_SIDE, 0, 900, 600, 900))
                    .addElem(new GeoElem(Type.FRAME_SIDE, 600, 900, 0, 0));

            rootGeo.addArea(new GeoElem(Type.AREA))
                    .addElem(new GeoElem(Type.GLASS));
            rootGeo.addElem(new GeoElem(Type.IMPOST, 200, 900, 201, 300));
            //rootGeo.addElem(new GeoElem(Type.IMPOST, 200, 900, 219.04762268066406, 328.5714416503906));
            //rootGeo.addElem(new GeoElem(Type.IMPOST, 0, 600, 400, 600));
            rootGeo.addArea(new GeoElem(Type.AREA))
                    .addElem(new GeoElem(Type.GLASS));   
            
        } else if (prj == 50100134) { //PUNIC = 427595
            rootGeo = new GeoRoot("2.0", prj, 2, 8, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)");
            rootGeo.addElem(new GeoElem(Type.FRAME_SIDE, 0, 0, 0, 900))
                    .addElem(new GeoElem(Type.FRAME_SIDE, 0, 900, 600, 900))
                    .addElem(new GeoElem(Type.FRAME_SIDE, 600, 900, 0, 0));

            rootGeo.addArea(new GeoElem(Type.AREA)).addElem(new GeoElem(Type.GLASS));
            rootGeo.addElem(new GeoElem(Type.IMPOST, 0, 600, 400, 600));           
            GeoElem area = rootGeo.addArea(new GeoElem(Type.AREA));
            area.addArea(new GeoElem(Type.AREA)).addElem(new GeoElem(Type.GLASS));
            area.addElem(new GeoElem(Type.IMPOST, 200, 900, 200, 600));
            area.addArea(new GeoElem(Type.AREA)).addElem(new GeoElem(Type.GLASS));
            
        } else {
            return null;
        }
        return new GsonBuilder().create().toJson(rootGeo);
    }
}
