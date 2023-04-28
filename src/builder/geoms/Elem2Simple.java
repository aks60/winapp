package builder.geoms;

import builder.Geocalc;
import builder.script.GeoElem;
import java.awt.geom.Area;
import java.awt.geom.Line2D;

public class Elem2Simple extends Comp {

    public Elem2Simple(Geocalc winc, GeoElem gson, Comp owner) {
        super(winc, gson, owner);
    }

    public void rebild() {
        try {
            if (owner.childs().size() == 3) {
                Area area2[] = UGeo.split(owner.area, owner.childs().get(1));
                owner.childs().get(0).area = area2[0];
                owner.childs().get(2).area = area2[1];
            }
        } catch (Exception e) {
            System.err.println("Ошибка:Area2Simple.rebild() " + e);
        }
    } 
    
    public void paint() {
        try {
            Area area2[] = UGeo.split(owner.area, this);
            double line[] = UGeo.cross(area2);
            if (line != null) {
                this.setLocation(line[0], line[1], line[2], line[3]);
            }
            winc.gc2D.draw(area2[0]);
            winc.gc2D.draw(area2[1]);
            winc.gc2D.draw(new Line2D.Double(this.x1(), this.y1(), this.x2(), this.y2()));
            
        } catch (Exception e) {
            System.err.println("Ошибка:Area2Simple.paint() " + e);
        }
    }
}
