package builder.script.test;

import builder.script.GeoRoot;
import static builder.script.GeoScript.rootGeo;
import java.awt.geom.Point2D;
import java.util.List;

public final class Bimax2 {

    public static String script(Integer prj) {

        if (prj == 501001) { //PUNIC = 427595
            rootGeo = new GeoRoot("2.0", prj, 2, 8, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)",
                    List.of(350, 5, 400, 100,400, 100, 350, 350,100, 350, 50, 100, 100, 50), 
                    List.of(200, 0.0, 280, 500, 280, 10, 280, 400));

//            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
//                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
//                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
//                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
//                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen: 4}"))
//                    .addElem(new GsonElem(Type.MOSKITKA, "{artiklID: 2700, elementID: 84}"))
//                    .addElem(new GsonElem(Type.GLASS, "{artglasID: 4267}"));

        } else {
            return null;
        }
        return rootGeo.toJson();
    }
}
