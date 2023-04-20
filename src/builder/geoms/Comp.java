package builder.geoms;

import builder.Geocalc;
import builder.script.GeoElem;
import java.util.List;

public class Comp {

    public double id;
    public double x1 = -1, y1 = -1, x2 = -1, y2 = -1;
    public Geocalc winc = null;
    public Comp owner = null; //владелец
    public GeoElem gson = null; //Gson object конструкции

    public Comp(Geocalc winc, GeoElem gson, Comp owner) {
        this.id = gson.id;
        this.winc = winc;
        this.owner = owner;
        this.gson = gson;
    }

    public Comp(Geocalc winc, GeoElem gson, Comp owner, double x1, double y1, double x2, double y2) {
        this.id = gson.id;
        this.winc = winc;
        this.owner = owner;
        this.gson = gson;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public List<Comp> childs() {
        return null;
    }
}
