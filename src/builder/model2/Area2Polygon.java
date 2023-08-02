package builder.model2;

import builder.Wingeo;
import builder.script.GeoElem;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;

public class Area2Polygon extends Area2Simple {

    public Area2Polygon(Wingeo wing, GeoElem gson) {
        super(wing, gson, null);
    }

    public void setLocation() {

        try {
            GeneralPath p = new GeneralPath();
            p.reset();
            p.moveTo((float) wing.listFrame.get(0).x1(), (float) wing.listFrame.get(0).y1());
            wing.listFrame.get(0).enext = wing.listFrame.get(1);
            for (int i = 1; i < wing.listFrame.size(); ++i) {
                p.lineTo((float) wing.listFrame.get(i).x1(), (float) wing.listFrame.get(i).y1());
                wing.listFrame.get(i).enext = (i + 1 == wing.listFrame.size()) ? wing.listFrame.get(0) : wing.listFrame.get(i + 1);
            }
            p.closePath();
            area = new Area(p);

        } catch (Exception e) {
            System.err.println("Ошибка:Area2Polygon.setLocation()" + toString() + e);
        }
    }

    public void setLocation2() {

       Coordinate[] coordinates = new Coordinate[wing.listFrame.size()];
        Polygon polygon = wing.geomFact.createPolygon();
        //for (int i = 1; i < wing.listFrame.size(); ++i) {
            for (Elem2Frame frame : wing.listFrame) {
            
           //coordinates[i] = new Coordinate(frame.x1(), frame.y1(), frame.x2(), frame.y2());
        }
        
    }

    public void paint() {
//        Area area1 = UGeo.area(0, 0, 0, 900, 600, 800, 0, 0);
//        UGeo.PRINT("", area1);
//
//        Area area2 = new Area(new Rectangle(0, 0, 200, 900));
//        area1.intersect(area2);
//        UGeo.PRINT("", area1);
//        
//        wing.gc2D.draw(area1);
//        wing.gc2D.draw(area2);

//        try {
//            wing.gc2D.draw(area);
//            
//            if (wing.listCross.isEmpty() == false) {
//                Elem2Cross cros = wing.listCross.get(0);
//
//                Area area2[] = UGeo.split(area, cros);
//                double line[] = UGeo.cross(area2);
//                if (line != null) {
//                    cros.setLocation(line[0], line[1], line[2], line[3]);
//                }
//                wing.gc2D.draw(new Line2D.Double(cros.x1(), cros.y1(), cros.x2(), cros.y2()));
//            }
//        } catch (Exception e) {
//            System.err.println("Ошибка:Area2Simple.paint()" + toString() + e);
//        }
    }
}
