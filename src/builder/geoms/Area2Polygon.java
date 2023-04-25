package builder.geoms;

import builder.Geocalc;
import builder.script.GeoElem;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Area2Polygon extends Area2Simple {

    public Area2Polygon(Geocalc winc, GeoElem gson) {
        super(winc, gson, null);
    }

    public void setPolygon(List<Elem2Frame> listFrame) {
        polygon.reset();
        polygon.moveTo(listFrame.get(0).x1(), listFrame.get(0).y1());
        for (int i = 1; i < listFrame.size(); ++i) {
            polygon.lineTo(listFrame.get(i).x1(), listFrame.get(i).y1());
        }
        polygon.closePath();
    }

    public void paint() {
        setPolygon(winc.listFrame);
        Area area1 = new Area(polygon);
        Area area2[] = UGeo.split(area1, winc.listCross.get(0));
        double line[] = UGeo.cross(area2, winc.listCross.get(0));
        winc.listCross.get(0).setLocation(line[0], line[1], line[2], line[3]);
        winc.gc2D.draw(new Line2D.Double(line[0], line[1], line[2], line[3]));
        winc.gc2D.draw(area1);
        //winc.gc2D.draw(area2[1]);
    }
}
