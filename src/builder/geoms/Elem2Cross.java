package builder.geoms;

import builder.Geocalc;
import builder.script.GeoElem;
import com.google.gson.JsonObject;
import domain.eArtikl;
import domain.eSysprof;
import enums.Layout;
import enums.PKjson;
import enums.UseSide;
import java.awt.geom.Area;
import java.awt.geom.Line2D;

public class Elem2Cross extends Elem2Simple {

    public Elem2Cross(Geocalc winc, GeoElem gson, Comp owner) {
        super(winc, gson, owner);
        mouseEvent();
    }

    public void initСonstructiv(JsonObject param) {

        colorID1 = (isJson(param, PKjson.colorID1)) ? param.get(PKjson.colorID1).getAsInt() : winc.colorID1;
        colorID2 = (isJson(param, PKjson.colorID2)) ? param.get(PKjson.colorID2).getAsInt() : winc.colorID2;
        colorID3 = (isJson(param, PKjson.colorID3)) ? param.get(PKjson.colorID3).getAsInt() : winc.colorID3;

        if (isJson(param, PKjson.sysprofID)) { //профили через параметр
            sysprofRec = eSysprof.find3(param.get(PKjson.sysprofID).getAsInt());
        } else {
            if (Layout.VERT.equals(owner.layout)) { //сверху вниз
                sysprofRec = eSysprof.find4(winc.nuni, type.id2, UseSide.HORIZ);

            } else if (Layout.HORIZ.equals(owner.layout)) { //слева направо
                sysprofRec = eSysprof.find4(winc.nuni, type.id2, UseSide.VERT);
            }
        }
        spcRec.place = (Layout.HORIZ == owner.layout) ? Layout.VERT.name : Layout.HORIZ.name;
        artiklRec(eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), false));
        artiklRecAn = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), true);
    }
    
    public void build() {
        super.build();
    }

    public void paint() {
        winc.gc2D.draw(new Line2D.Double(this.x1(), this.y1(), this.x2(), this.y2()));
    }
}
