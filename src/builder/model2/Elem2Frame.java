package builder.model2;

import builder.Wingeo;
import builder.script.GeoElem;
import com.google.gson.JsonObject;
import domain.eArtikl;
import domain.eColor;
import static domain.eColor.rgb;
import domain.eSysprof;
import domain.eSyssize;
import enums.PKjson;
import enums.UseSide;
import frames.swing.DrawStroke;
import java.util.List;

public class Elem2Frame extends Elem2Simple {

    public Elem2Frame(Wingeo wing, GeoElem gson, Comp owner) {
        super(wing, gson, owner);
        initСonstructiv(gson.param);
        mouseEvent();
    }

    /**
     * Профиль через параметр или первая запись в системе см. табл. sysprof Цвет
     * если нет параметра то берём wing.color.
     */
    public void initСonstructiv(JsonObject param) {

        colorID1 = (isJson(param, PKjson.colorID1)) ? param.get(PKjson.colorID1).getAsInt() : wing.colorID1;
        colorID2 = (isJson(param, PKjson.colorID2)) ? param.get(PKjson.colorID2).getAsInt() : wing.colorID2;
        colorID3 = (isJson(param, PKjson.colorID3)) ? param.get(PKjson.colorID3).getAsInt() : wing.colorID3;

        if (isJson(param, PKjson.sysprofID)) { //профили через параметр
            sysprofRec = eSysprof.find3(param.get(PKjson.sysprofID).getAsInt());

        } else if (owner.sysprofRec != null) { //профили через параметр рамы, створки
            sysprofRec = owner.sysprofRec;
        } else {
            double angl = UGeo.horizontAngl(this);
            if (angl == 0) {
                sysprofRec = eSysprof.find5(wing.nuni, type.id2, UseSide.BOT, UseSide.HORIZ);
            } else if (angl == 90) {
                sysprofRec = eSysprof.find5(wing.nuni, type.id2, UseSide.RIGHT, UseSide.VERT);
            } else if (angl == 180) {
                sysprofRec = eSysprof.find5(wing.nuni, type.id2, UseSide.TOP, UseSide.HORIZ);
            } else if (angl == 270) {
                sysprofRec = eSysprof.find5(wing.nuni, type.id2, UseSide.LEFT, UseSide.VERT);
            } else {
                sysprofRec = eSysprof.find4(wing.nuni, type.id2, UseSide.ANY);
            }
        }
        artiklRec = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), false);
        artiklRecAn = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), true);

        //Системные константы как правило на всю систему профилей
        if (wing.syssizeRec == null) {
            wing.syssizeRec = eSyssize.find(artiklRec);
        }
    }

    public void setLocation() {
        this.anglHoriz = UGeo.horizontAngl(this);
        Elem2Simple e0 = null, e1 = null;
        for (int i = 0; i < wing.listFrame.size(); i++) {
            if (wing.listFrame.get(i).id == this.id) {
                if (i == 0) {
                    e0 = wing.listFrame.get(wing.listFrame.size() - 1);
                    e1 = wing.listFrame.get(i + 1);
                    
                } else if(i == wing.listFrame.size() - 1) {
                    e0 = wing.listFrame.get(i - 1);
                    e1 = wing.listFrame.get(0);                   
                } else {
                    e0 = wing.listFrame.get(i - 1);
                    e1 = wing.listFrame.get(i + 1);
                }
            }
        }
        double h[] = UGeo.diff(this, this.artiklRec.getDbl(eArtikl.height) - this.artiklRec.getDbl(eArtikl.size_centr));
        double h1[] = UGeo.diff(e0, e0.artiklRec.getDbl(eArtikl.height) - e0.artiklRec.getDbl(eArtikl.size_centr));
        double h2[] = UGeo.diff(e1, e1.artiklRec.getDbl(eArtikl.height) - e1.artiklRec.getDbl(eArtikl.size_centr));
        double p1[] = UGeo.cross(x1() + h[0], y1() + h[1], x2() + h[0], y2() + h[1], e0.x1() + h1[0], e0.y1() + h1[1], e0.x2() + h1[0], e0.y2() + h1[1]);
        double p2[] = UGeo.cross(x1() + h[0], y1() + h[1], x2() + h[0], y2() + h[1], e1.x1() + h2[0], e1.y1() + h2[1], e1.x2() + h2[0], e1.y2() + h2[1]);
        polygon(x1(), y1(), x2(), y2(), p2[0], p2[1], p1[0], p1[1]);
        paint();
    }

    public void paint() {
        int rgb = eColor.find(colorID2).getInt(eColor.rgb);
        wing.gc2D.draw(area);
        //DrawStroke.strokePolygon(wing, x1(), x2(), p2[0], p1[0], y1(), y2(), p2[1], p1[1], rgb, borderColor);
    }
}
