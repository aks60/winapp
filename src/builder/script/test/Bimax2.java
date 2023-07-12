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

        if (prj == 501001) { //PUNIC = 427595
            rootGeo = new GeoRoot("2.0", prj, 2, 8, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)");
            rootGeo.addElem(new GeoElem(Type.FRAME_SIDE, 1000, 3500, 3500, 3500))
                    .addElem(new GeoElem(Type.FRAME_SIDE, 3500, 3500, 4000, 1000))
                    .addElem(new GeoElem(Type.FRAME_SIDE, 4000, 1000, 3500, 500))
                    .addElem(new GeoElem(Type.FRAME_SIDE, 3500, 500, 1000, 500))
                    .addElem(new GeoElem(Type.FRAME_SIDE, 1000, 500, 500, 1000))
                    .addElem(new GeoElem(Type.FRAME_SIDE, 500, 1000, 400, 1770))
                    .addElem(new GeoElem(Type.FRAME_SIDE, 400, 1770, 1000, 3500));

            GeoElem area = rootGeo.addArea(new GeoElem(Type.AREA));
            area.addElem(new GeoElem(Type.GLASS));
            rootGeo.addElem(new GeoElem(Type.IMPOST, 2600, 100, 2600, 5000));
            rootGeo.addArea(new GeoElem(Type.AREA))
                    .addElem(new GeoElem(Type.GLASS));

//            area.addArea(new GeoElem(Type.AREA))
//                    .addElem(new GeoElem(Type.GLASS));
//            area.addElem(new GeoElem(Type.IMPOST, 1000, 100, 2000, 5000));
//            area.addArea(new GeoElem(Type.AREA))
//                    .addElem(new GeoElem(Type.GLASS));

        } else if (prj == 501002) { //PUNIC = 427595
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

        } else if (prj == 501003) { //PUNIC = 427595
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
