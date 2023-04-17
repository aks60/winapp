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

        pLine.add(new Point2D.Double(200.0, 0));
        pLine.add(new Point2D.Double(280, 400));
        //pLine.add(new Point2D.Double(0.0, 280));
        //pLine.add(new Point2D.Double(400, 280));        
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
        GeneralPath line = new GeneralPath();
        //line.moveTo(pLine.get(0).getX(), pLine.get(0).getY());
        for (int j = 1; j < pLine.size() / 2; j += 1) {
            //line.lineTo(pLine.get(j).getX(), pLine.get(j).getY());
            //line.lineTo(pLine.get(j + 1).getX(), pLine.get(j + 1).getY());
            gc2D.draw(new Line2D.Double()); 
        }
        //line.closePath();

        Area area1 = new Area(poly);
        Area area2 = new Area(line);
        //area1.intersect(area2);

        gc2D.draw(area1);      
    }
}
