package builder.geoms;

import builder.Geocalc;
import builder.script.GeoElem;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
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
        Elem2Cross ln2 = winc.listCross.get(0);
        setPolygon(winc.listFrame);
        Area area1 = new Area(polygon);
        Area area2[] = UGeo.split(area1, winc.listCross.get(0));
        double ln1[] = UGeo.cross(area2, winc.listCross.get(0));
            
        winc.listCross.get(0).setLocation(ln1[0], ln1[1], ln1[2], ln1[3]);
        
        //winc.gc2D.draw(new Line2D.Double(line[0], line[1], line[2], line[3]));
        winc.gc2D.draw(area2[0]);
        //split(area1, winc.listCross.get(0));

        //winc.gc2D.draw(new Line2D.Double(winc.listCross.get(0).x1(), winc.listCross.get(0).y1(), winc.listCross.get(0).x2(), winc.listCross.get(0).y2()));
        System.out.println("ln2= " + ln2.x1() + ":" + ln2.y1() + " = " + ln2.x2() + ":" + ln2.y2());
        System.out.println("ln1= " + ln1[0] + ":" + ln1[1] + " = " + ln1[2] + ":" + ln1[3]);
    }

    public void split(Area a, Elem2Cross e) {

        //Вычисление угла линии к оси x
        double dx = e.x2() - e.x1();
        double dy = e.y2() - e.y1();
        double angl = Math.atan2(dy, dx);

        //Выравниваем область так, чтобы линия совпадала с осью x
        AffineTransform at = new AffineTransform();
        at.rotate(-angl);
        at.translate(-e.x1(), -e.y1());
        Area aa = a.createTransformedArea(at);
        Rectangle2D bounds = aa.getBounds2D();

        double half0minY = Math.min(0, bounds.getMinY());
        double half0maxY = Math.min(0, bounds.getMaxY());
        Rectangle2D half0 = new Rectangle2D.Double(
                bounds.getX(), half0minY, bounds.getWidth(), half0maxY - half0minY);

        double half1minY = Math.max(0, bounds.getMinY());
        double half1maxY = Math.max(0, bounds.getMaxY());
        Rectangle2D half1 = new Rectangle2D.Double(
                bounds.getX(), half1minY, bounds.getWidth(), half1maxY - half1minY);

        //Вычисляем получившиеся площади путем пересечения исходной области с 
        //обеими половинками, и возвращаем их в исходное положение
        Area a0 = new Area(aa);
        a0.intersect(new Area(half0));

        Area a1 = new Area(aa);
        //a1.intersect(new Area(half1));
        winc.gc2D.draw(new Area(half1));

//        try {
//            at.invert();
//        } catch (NoninvertibleTransformException event) {
//            System.out.println("Ошибка:Geocalc.split() " + event);
//        }
        a1 = a1.createTransformedArea(at);

        //winc.gc2D.draw(a0);
        winc.gc2D.draw(a1);
    }
}
