package builder.geoms;

import builder.Geocalc;
import builder.script.GeoElem;
import java.awt.geom.Area;
import java.awt.geom.Line2D;

public class Elem2Cross extends Elem2Simple {

    public Elem2Cross(Geocalc winc, GeoElem gson, Comp owner) {
        super(winc, gson, owner);
        mouseEvent();
    }

    public void build() {
        super.build();
    }

    public void paint() {
        winc.gc2D.draw(new Line2D.Double(this.x1(), this.y1(), this.x2(), this.y2()));
    }
}
