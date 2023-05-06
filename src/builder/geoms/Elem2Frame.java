package builder.geoms;

import builder.Wingeo;
import builder.script.GeoElem;
import com.google.gson.JsonObject;
import domain.eArtikl;
import domain.eSysprof;
import domain.eSyssize;
import enums.Layout;
import enums.PKjson;
import enums.UseSide;

public class Elem2Frame extends Elem2Simple {

    public Elem2Frame(Wingeo wing, GeoElem gson, Comp owner) {
        super(wing, gson, owner);
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
            if(angl >= 0 && angl < 90) {
                sysprofRec = eSysprof.find5(wing.nuni, type.id2, UseSide.BOT, UseSide.HORIZ);
            } else if (angl >= 90 && angl < 180) {
                sysprofRec = eSysprof.find5(wing.nuni, type.id2, UseSide.RIGHT, UseSide.VERT);
            } else if (angl >= 180 && angl < 270) {
                sysprofRec = eSysprof.find5(wing.nuni, type.id2, UseSide.TOP, UseSide.HORIZ);
            } else if (angl >= 270 && angl < 0) {
                sysprofRec = eSysprof.find5(wing.nuni, type.id2, UseSide.LEFT, UseSide.VERT);
            }
        }
        artiklRec = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), false);
        artiklRecAn = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), true);

        //Системные константы как правило на всю систему профилей
        if (wing.syssizeRec == null) {
            wing.syssizeRec = eSyssize.find(artiklRec);
        }
    }
    
    public void rebuild() {
        super.rebuild();
    }

    public void paint() {

    }
}
