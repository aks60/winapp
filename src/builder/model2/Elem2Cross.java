package builder.model2;

import builder.Wingeo;
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

    public Elem2Cross(Wingeo wing, GeoElem gson, Comp owner) {
        super(wing, gson, owner);
        initСonstructiv(gson.param);
        mouseEvent();
    }

    public void initСonstructiv(JsonObject param) {

        colorID1 = (isJson(param, PKjson.colorID1)) ? param.get(PKjson.colorID1).getAsInt() : wing.colorID1;
        colorID2 = (isJson(param, PKjson.colorID2)) ? param.get(PKjson.colorID2).getAsInt() : wing.colorID2;
        colorID3 = (isJson(param, PKjson.colorID3)) ? param.get(PKjson.colorID3).getAsInt() : wing.colorID3;

        double angl = UGeo.horizontAngl(this);
        if (isJson(param, PKjson.sysprofID)) { //профили через параметр
            sysprofRec = eSysprof.find3(param.get(PKjson.sysprofID).getAsInt());
        } else {
            if (angl == 0 || angl == 180) { //сверху вниз
                sysprofRec = eSysprof.find5(wing.nuni, type.id2, UseSide.HORIZ, UseSide.ANY);

            } else if (angl == 90 || angl == 270) { //слева направо
                sysprofRec = eSysprof.find5(wing.nuni, type.id2, UseSide.VERT, UseSide.ANY);

            } else {
                sysprofRec = eSysprof.find4(wing.nuni, type.id2, UseSide.ANY);
            }
        }
        //spcRec.place = (angl == 0) ? Layout.HORIZ.name : Layout.VERT.name;
        artiklRec = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), false);
        artiklRecAn = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), true);
    }

    public void setLocation() {
        try {
            if (owner.childs().size() == 3) {
                Area area2[] = UGeo.split(owner.area, owner.childs().get(1));
                owner.childs().get(0).area = area2[0];
                owner.childs().get(2).area = area2[1];
                double line[] = UGeo.cross(area2);
                if (line != null) {
                    this.setLocation(line[0], line[1], line[2], line[3]);
                }
            }
            
//        this.anglHoriz = UGeo.horizontAngl(this);
//        Elem2Simple e0 = null, e1 = null;
//        for (int i = 0; i < wing.listFrame.size(); i++) {
//            if (wing.listFrame.get(i).id == this.id) {
//                if (i == 0) {
//                    e0 = wing.listFrame.get(wing.listFrame.size() - 1);
//                    e1 = wing.listFrame.get(i + 1);
//                    
//                } else if(i == wing.listFrame.size() - 1) {
//                    e0 = wing.listFrame.get(i - 1);
//                    e1 = wing.listFrame.get(0);                   
//                } else {
//                    e0 = wing.listFrame.get(i - 1);
//                    e1 = wing.listFrame.get(i + 1);
//                }
//            }
//        }
//        double h[] = UGeo.diff(this, this.artiklRec.getDbl(eArtikl.height) - this.artiklRec.getDbl(eArtikl.size_centr));
//        double h1[] = UGeo.diff(e0, e0.artiklRec.getDbl(eArtikl.height) - e0.artiklRec.getDbl(eArtikl.size_centr));
//        double h2[] = UGeo.diff(e1, e1.artiklRec.getDbl(eArtikl.height) - e1.artiklRec.getDbl(eArtikl.size_centr));
//        double p1[] = UGeo.cross(x1() + h[0], y1() + h[1], x2() + h[0], y2() + h[1], e0.x1() + h1[0], e0.y1() + h1[1], e0.x2() + h1[0], e0.y2() + h1[1]);
//        double p2[] = UGeo.cross(x1() + h[0], y1() + h[1], x2() + h[0], y2() + h[1], e1.x1() + h2[0], e1.y1() + h2[1], e1.x2() + h2[0], e1.y2() + h2[1]);
//        polygon(x1(), y1(), x2(), y2(), p2[0], p2[1], p1[0], p1[1]);
//        //paint();
        
        } catch (Exception e) {
            System.err.println("Ошибка:Elem2Simple.build()" + toString() + e);
        }
    }

    public void paint() {
        wing.gc2D.draw(new Line2D.Double(this.x1(), this.y1(), this.x2(), this.y2()));
    }
}
