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

    public void paint() {
//        winc.root.setPolygon(winc.listFrame);
//        Area area1 = new Area(winc.root.polygon);
        
//        Area area2[] = UGeo.split(area1, winc.listCross.get(0));
//        double line[] = UGeo.cross(area2, winc.listCross.get(0));
//        
//        winc.listCross.get(0).setLocation(line[0], line[1], line[2], line[3]);
//        winc.gc2D.draw(new Line2D.Double(line[0], line[1], line[2], line[3]));
//        
//        winc.gc2D.draw(area2[0]); 
//        System.out.println("Elem2Cross.paint()");
    }
}
