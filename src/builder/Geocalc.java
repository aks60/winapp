package builder;

import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Geocalc {

    public Graphics2D gc2D = null; //графический котекст рисунка  
    public double scale = 1; //коэффициент сжатия
    public List<Point2D> pPoly = new ArrayList(); //вершины многоугольника
    public List<Point2D> pLine = new ArrayList(); //вершины многоугольника
    public List<Point2D> pClone = new ArrayList(); //вершины многоугольника

    public static Geocalc create() {
        Geocalc geom = new Geocalc();
        geom.shape();
        return geom;
    }

    public void shape() {
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

        //Клип
        GeneralPath clip = new GeneralPath();
        clip.moveTo(pLine.get(0).getX(), pLine.get(0).getY());
        clip.lineTo(pLine.get(1).getX(), pLine.get(1).getY());
        clip.lineTo(400, pLine.get(1).getY());
        clip.lineTo(400, pLine.get(0).getY());
        clip.closePath();

        Area polyArea = new Area(poly);
        Area lineArea = new Area(new Line2D.Double(pLine.get(0), pLine.get(1)));
        Area clipArea = new Area(clip);
        polyArea.intersect(clipArea);

        
        GeneralPath clon = new GeneralPath();
        final double[] coords = new double[6];
        PathIterator iterator = polyArea.getPathIterator(null);
        while (!iterator.isDone()) {
            final int segmentType = iterator.currentSegment(coords);
            if (segmentType == PathIterator.SEG_LINETO) {
                clon.lineTo(coords[0], coords[1]);
            } else if (segmentType == PathIterator.SEG_MOVETO) {
                clon.moveTo(coords[0], coords[1]);
            } else if (segmentType == PathIterator.SEG_CLOSE) {
                clon.closePath();
            }
            pClone.add(new Point2D.Double(coords[0], coords[1]));
            iterator.next();
        }

        GeneralPath clo2 = new GeneralPath();
        clo2.moveTo(pPoly.get(0).getX(), pPoly.get(0).getY());
        for (int i = 1; i < pPoly.size(); i++) {
            clo2.lineTo(pPoly.get(i).getX(), pPoly.get(i).getY());
        }
        clo2.closePath();
        
        gc2D.draw(clon);
    }
}
