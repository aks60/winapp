package builder.script.test;

import builder.script.GeoElem;
import builder.script.GeoRoot;
import static builder.script.GeoScript.rootGeo;
import static builder.script.GsonScript.rootGson;
import enums.Type;
import java.util.List;

public final class Bimax2 {

    public static GeoRoot rootGeo;

    public static String script(Integer prj) {
        
        if (prj == 501001) { //PUNIC = 427595
            rootGeo = new GeoRoot("2.0", prj, 2, 8, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)",
                    List.of(100, 350, 350, 350, 400, 100, 350, 50, 100, 50, 50, 100, 40, 177),
                    List.of(200, 10, 280, 500, 280, 10, 280, 400));
            rootGeo.addElem(new GeoElem(Type.FRAME, 100, 350))
                    .addElem(new GeoElem(Type.FRAME, 350, 350))
                    .addElem(new GeoElem(Type.FRAME, 400, 100))
                    .addElem(new GeoElem(Type.FRAME, 350, 50))
                    .addElem(new GeoElem(Type.FRAME, 100, 50))
                    .addElem(new GeoElem(Type.FRAME, 50, 100))
                    .addElem(new GeoElem(Type.FRAME, 40, 177));

            rootGeo.addArea(new GeoElem(Type.AREA))
                    .addElem(new GeoElem(Type.STVORKA))            
                    .addElem(new GeoElem(Type.GLASS));            
            rootGeo.addElem(new GeoElem(Type.IMPOST, 200, 10, 280, 500));
            rootGeo.addArea(new GeoElem(Type.AREA))               
                    .addElem(new GeoElem(Type.GLASS));            
            rootGeo.addElem(new GeoElem(Type.IMPOST, 280, 10, 280, 400));  
            rootGeo.addArea(new GeoElem(Type.AREA))
                    .addElem(new GeoElem(Type.GLASS));

        } else if (prj == 501002) { //PUNIC = 427595
            rootGeo = new GeoRoot("2.0", prj, 2, 8, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)");

        } else {
            return null;
        }
        return rootGeo.toJson();
    }
}
