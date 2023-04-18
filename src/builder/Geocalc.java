package builder;

import builder.geoms.Elem2Cross;
import builder.geoms.Elem2Frame;
import builder.script.GeoRoot;
import builder.script.test.Bimax2;
import com.google.gson.GsonBuilder;
import common.listener.ListenerMouse;
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
    
    public ArrayList<ListenerMouse> mousePressedList = new ArrayList();
    public ArrayList<ListenerMouse> mouseReleasedList = new ArrayList();
    public ArrayList<ListenerMouse> mouseDraggedList = new ArrayList();

    public List<Elem2Frame> listFrame = new ArrayList();
    public List<Elem2Cross> listCross = new ArrayList();
    public GeneralPath pathPoly = new GeneralPath();

    public GeoRoot rootGeo = null; //объектная модель конструкции 1-го уровня
    public IArea5e rootArea = null; //объектная модель конструкции 2-го уровня

    public Geocalc() {
        String script = Bimax2.script(501001);
        build(script);
    }


    public IArea5e build(String script) {
        try {

            parsing(script);

        } catch (Exception e) {
            System.err.println("Ошибка:Geocalc.build() " + e);
        }
        return rootArea;
    }


    private void parsing(String script) {
        //Для тестирования
        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(new com.google.gson.JsonParser().parse(script)));

        rootGeo = new GsonBuilder().create().fromJson(script, GeoRoot.class);

        pathPoly.moveTo(rootGeo.poly.get(0), rootGeo.poly.get(1 + 1));
        for (int i = 2; i < rootGeo.poly.size(); i += 2) {
            pathPoly.lineTo(rootGeo.poly.get(0), rootGeo.poly.get(1 + 1));  
        }
        pathPoly.closePath();

        for (int i = 0; i < rootGeo.poly.size(); i += 2) {
            pPoly.add(new Point2D.Double(rootGeo.poly.get(i), rootGeo.poly.get(i + 1)));
            listFrame.add(new Elem2Frame(this, rootGeo.poly.get(i), rootGeo.poly.get(i + 1)));
        }
        for (int i = 0; i < rootGeo.line.size(); i += 2) {
            pLine.add(new Point2D.Double(rootGeo.poly.get(i), rootGeo.poly.get(i + 1)));
            listCross.add(new Elem2Cross(this, rootGeo.poly.get(i), rootGeo.poly.get(i + 1)));
        }

//        GeneralPath clon = new GeneralPath();
//        final double[] coords = new double[6];
//        PathIterator iterator = polyArea.getPathIterator(null);
//        while (!iterator.isDone()) {
//            final int segmentType = iterator.currentSegment(coords);
//            if (segmentType == PathIterator.SEG_LINETO) {
//                clon.lineTo(coords[0], coords[1]);
//            } else if (segmentType == PathIterator.SEG_MOVETO) {
//                clon.moveTo(coords[0], coords[1]);
//            } else if (segmentType == PathIterator.SEG_CLOSE) {
//                clon.closePath();
//            }
//            pClone.add(new Point2D.Double(coords[0], coords[1]));
//            iterator.next();
//        }        
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
        //polyArea.intersect(clipArea);

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

//        GeneralPath clo2 = new GeneralPath();
//        clo2.moveTo(pPoly.get(0).getX(), pPoly.get(0).getY());
//        for (int i = 1; i < pPoly.size(); i++) {
//            clo2.lineTo(pPoly.get(i).getX(), pPoly.get(i).getY());
//        }
//        clo2.closePath();
        gc2D.draw(clon);
    }
}
