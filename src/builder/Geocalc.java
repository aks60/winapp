package builder;

import builder.geoms.UGeo;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Geocalc {

    public Graphics2D gc2D = null; //графический котекст рисунка  
    public double scale = 1; //коэффициент сжатия
    public List<Point2D> pPoly = new ArrayList(); //вершины многоугольника
    public List<Point2D> pLine = new ArrayList(); //вершины многоугольника

    public static Geocalc create() {
        Geocalc geom = new Geocalc();
        geom.shape();
        return geom;
    }

    public void shape() {
        //System.out.println("chapetest.ShapeComponent.setShapeMaker()");

        Area area = new Area(new Rectangle2D.Double(0, 0, 300, 300));
        pPoly.add(new Point2D.Double(350, 50));
        pPoly.add(new Point2D.Double(400, 100));
        pPoly.add(new Point2D.Double(400, 100));
        pPoly.add(new Point2D.Double(350, 350));
        pPoly.add(new Point2D.Double(100, 350));
        pPoly.add(new Point2D.Double(50, 100));
        pPoly.add(new Point2D.Double(100, 50));

        pLine.add(new Point2D.Double(200, 0.0));
        pLine.add(new Point2D.Double(280, 500));
        pLine.add(new Point2D.Double(280, 10));
        pLine.add(new Point2D.Double(280, 400));        
    }

    public void draw() {

        //Многоугольник
        GeneralPath poly = new GeneralPath();
        poly.moveTo(pPoly.get(0).getX(), pPoly.get(0).getY());
        for (int i = 1; i < pPoly.size(); i++) {
            poly.lineTo(pPoly.get(i).getX(), pPoly.get(i).getY());
        }
        poly.closePath();

        //Линия
        for (int j = 0; j < pLine.size(); j += 2) {
            gc2D.draw(new Line2D.Double(pLine.get(j), pLine.get(j + 1)));
        }

        Area area1 = new Area(poly);
        Area area2 = new Area(new Line2D.Double(pLine.get(0), pLine.get(1)));
        //area1.intersect(area2);

        gc2D.draw(area1);
    }
}
