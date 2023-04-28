package builder.geoms;

import builder.Geocalc;
import builder.script.GeoElem;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.List;

public class Area2Polygon extends Area2Simple {

    public Area2Polygon(Geocalc winc, GeoElem gson) {
        super(winc, gson, null);
    }

    public void build() {
        try {
            GeneralPath p = new GeneralPath();
            p.reset();
            p.moveTo(winc.listFrame.get(0).x1(), winc.listFrame.get(0).y1());
            for (int i = 1; i < winc.listFrame.size(); ++i) {
                p.lineTo(winc.listFrame.get(i).x1(), winc.listFrame.get(i).y1());
            }
            p.closePath();
            area = new Area(p);

        } catch (Exception e) {
            System.err.println("Ошибка:Area2Simple.build()" + toString() + e);
        }
    }

    public void paint() {
        try {
            winc.gc2D.draw(area);
            if (winc.listCross.isEmpty() == false) {
                Elem2Cross cros = winc.listCross.get(0);

                Area area2[] = UGeo.split(area, cros);
                double line[] = UGeo.cross(area2);
                if (line != null) {
                    cros.setLocation(line[0], line[1], line[2], line[3]);
                }
                winc.gc2D.draw(new Line2D.Double(cros.x1(), cros.y1(), cros.x2(), cros.y2()));
            }
        } catch (Exception e) {
            System.err.println("Ошибка:Area2Simple.paint()" + toString() + e);
        }
    }
}
