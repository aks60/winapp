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

        if (prj == 50100177) { //PUNIC = 427595
//            rootGeo = new GeoRoot("2.0", prj, 2, 8, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)");
//            rootGeo.addElem(new GeoElem(Type.FRAME, 100, 350, 350, 350))
//                    .addElem(new GeoElem(Type.FRAME, 350, 350, 400, 100))
//                    .addElem(new GeoElem(Type.FRAME, 400, 100, 350, 50))
//                    .addElem(new GeoElem(Type.FRAME, 350, 50, 100, 50))
//                    .addElem(new GeoElem(Type.FRAME, 100, 50, 50, 100))
//                    .addElem(new GeoElem(Type.FRAME, 50, 100, 40, 177))
//                    .addElem(new GeoElem(Type.FRAME, 40, 177, 100, 350));
//
////            rootGeo.addArea(new GeoElem(Type.STVORKA))            
////                    .addElem(new GeoElem(Type.GLASS));            
////            rootGeo.addElem(new GeoElem(Type.IMPOST, 100, 10, 280, 500));
//            rootGeo.addArea(new GeoElem(Type.AREA))
//                    .addElem(new GeoElem(Type.GLASS));
//            rootGeo.addElem(new GeoElem(Type.IMPOST, 280, 400, 200, 0));
//            rootGeo.addArea(new GeoElem(Type.AREA))
//                    .addElem(new GeoElem(Type.GLASS));

        } else if (prj == 50100177) { //PUNIC = 427595
            rootGeo = new GeoRoot("2.0", prj, 2, 8, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)");
            rootGeo.addElem(new GeoElem(Type.FRAME, 20, 0, 100, 120))
                    .addElem(new GeoElem(Type.FRAME, 100, 120, 20, 120))
                    .addElem(new GeoElem(Type.FRAME, 20, 120, 20, 0));

            rootGeo.addArea(new GeoElem(Type.AREA))
                    .addElem(new GeoElem(Type.GLASS));
            rootGeo.addElem(new GeoElem(Type.IMPOST, 20, 75, 70, 75));
            GeoElem area = rootGeo.addArea(new GeoElem(Type.AREA));
            
            area.addArea(new GeoElem(Type.AREA))
                    .addElem(new GeoElem(Type.GLASS));
            area.addElem(new GeoElem(Type.IMPOST, 50, 120, 50, 75));
            area.addArea(new GeoElem(Type.AREA))
                    .addElem(new GeoElem(Type.GLASS));

            
        } else if (prj == 501001) { //PUNIC = 427595
            rootGeo = new GeoRoot("2.0", prj, 2, 8, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)");
            rootGeo.addElem(new GeoElem(Type.FRAME, 200, 0, 800, 800))
                    .addElem(new GeoElem(Type.FRAME, 800, 800, 200, 800))
                    .addElem(new GeoElem(Type.FRAME, 200, 800, 200, 0));

            GeoElem area = rootGeo.addArea(new GeoElem(Type.AREA))
                    .addElem(new GeoElem(Type.GLASS));
            rootGeo.addElem(new GeoElem(Type.IMPOST, 200, 400, 500, 400));
            GeoElem area2 = rootGeo.addArea(new GeoElem(Type.AREA));
            
            area.addArea(new GeoElem(Type.AREA))
                    .addElem(new GeoElem(Type.GLASS));
            area.addElem(new GeoElem(Type.IMPOST, 400, 800, 400, 400));
            area.addArea(new GeoElem(Type.AREA))
                    .addElem(new GeoElem(Type.GLASS));

        } else {
            return null;
        }
        return new GsonBuilder().create().toJson(rootGeo);
    }
}
