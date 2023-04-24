package builder.geoms;

import builder.Geocalc;
import builder.script.GeoElem;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
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
        Area polArea = new Area(polygon);
       
        Area area[] = UGeo.split(polArea, winc.listCross.get(0));
        
        winc.gc2D.draw(area[0]); //рисую
        
        Elem2Cross cross = winc.listCross.get(0);
        Point2D[] point2D = UGeo.cross(polArea, cross);

        if (point2D != null && point2D.length > 1) {
            //System.out.println(point2D[0].getX() + " " + point2D[0].getY() + " " + point2D[1].getX() + " " + point2D[1].getY());
            cross.setLocation(point2D[0].getX(), point2D[0].getY(), point2D[1].getX(), point2D[1].getY());
            winc.gc2D.draw(new Line2D.Double(point2D[0], point2D[1])); //рисую
        }
        //gc2D.draw(new Line2D.Double(80, 10, 280, 400)); //рисую        
    }
}
