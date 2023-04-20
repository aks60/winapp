package builder;

import builder.geoms.Area2Polygon;
import builder.geoms.Area2Simple;
import builder.geoms.Area2Stvorka;
import builder.geoms.Comp;
import builder.geoms.Elem2Cross;
import builder.geoms.Elem2Frame;
import builder.geoms.Elem2Glass;
import builder.geoms.UGeo;
import builder.geoms.xlam.CrossLineShape;
import builder.script.GeoElem;
import builder.script.GeoRoot;
import builder.script.test.Bimax2;
import com.google.gson.GsonBuilder;
import common.listener.ListenerMouse;
import enums.Type;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Geocalc {

    public Graphics2D gc2D = null; //графический котекст рисунка  
    public double scale = 1; //коэффициент сжатия
    public double[] scene = new double[2];

    public ArrayList<ListenerMouse> listMousePressed = new ArrayList();
    public ArrayList<ListenerMouse> listMouseReleased = new ArrayList();
    public ArrayList<ListenerMouse> listMouseDragged = new ArrayList();

    public List<Elem2Frame> listFrame = new ArrayList();
    public List<Elem2Cross> listCross = new ArrayList();
    public transient List<Point2D> pointFrame = new ArrayList();
    public transient List<Line2D> pointCross = new ArrayList();

    public transient List<Elem2Cross> testCross = new ArrayList();

    public GeoRoot gson = null; //объектная модель конструкции 1-го уровня
    public Area2Polygon root = null; //объектная модель конструкции 2-го уровня

    public Geocalc() {
        String script = Bimax2.script(501001);
        build(script);
    }

    public void build(String script) {
        try {

            parsing(script);

        } catch (Exception e) {
            System.err.println("Ошибка:Geocalc.build() " + e);
        }
    }

    private void parsing(String script) {
        //Для тестирования
        //System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(new com.google.gson.JsonParser().parse(script)));

        gson = new GsonBuilder().create().fromJson(script, GeoRoot.class);
        root = new Area2Polygon(this, gson);
        elements(root, gson);
    }

    private void elements(Comp owner, GeoElem gson) {
        try {
            LinkedHashMap<Comp, GeoElem> hm = new LinkedHashMap();
            for (GeoElem js : gson.childs) {

                if (Type.STVORKA == js.type) {
                    Area2Simple area5e = new Area2Stvorka(this, gson, owner);
                    owner.childs().add(area5e); //добавим ребёна родителю
                    hm.put(area5e, js);

                } else if (Type.AREA == js.type) {
                    Area2Simple area5e = new Area2Simple(this, js, owner);
                    owner.childs().add(area5e); //добавим ребёна родителю
                    hm.put(area5e, js);

                } else if (Type.FRAME == js.type) {
                    Elem2Frame elem5e = new Elem2Frame(this, js, owner, js.x1, js.y1, -1, -1);
                    listFrame.add(elem5e);

                } else if (Type.IMPOST == js.type || Type.SHTULP == js.type || Type.STOIKA == js.type) {
                    Elem2Cross elem5e = new Elem2Cross(this, js, owner, js.x1, js.y1, js.x2, js.y2);
                    owner.childs().add(elem5e); //добавим ребёна родителю
                    listCross.add(elem5e);

                } else if (Type.GLASS == js.type) {
                    Elem2Glass elem5e = new Elem2Glass(this, js, owner);
                    owner.childs().add(elem5e); //добавим ребёна родителю

                } else if (Type.MOSKITKA == js.type) {
                    //Elem2Mosquit elem5e = new Elem2Mosquit(this, js, owner);
                    //owner.childs().add(elem5e); //добавим ребёна родителю

                }
            }

            //Теперь вложенные элементы
            for (Map.Entry<Comp, GeoElem> entry : hm.entrySet()) {
                elements(entry.getKey(), entry.getValue());
            }

        } catch (Exception e) {
            System.err.println("Ошибка:Wincalc.elements(*) " + e);
        }
    }

    public void draw() {

        //Многоугольник  
        pointFrame.clear();
        listFrame.forEach(e -> pointFrame.add(new Point2D.Double(e.x1, e.y1)));

        GeneralPath polPath = new GeneralPath();
        polPath.moveTo(pointFrame.get(0).getX(), pointFrame.get(0).getY());
        for (int i = 1; i < pointFrame.size(); ++i) {
            polPath.lineTo(pointFrame.get(i).getX(), pointFrame.get(i).getY());
        }
        polPath.closePath();
        Area polArea = new Area(polPath);

        //Линия
        List<Point2D> pLine = new ArrayList();
        pLine.add(new Point2D.Double(0.0, 100));
        pLine.add(new Point2D.Double(300, 400));

        clipping(pLine.get(0).getX(), pLine.get(0).getY(), pLine.get(1).getX(), pLine.get(1).getY());
        Line2D.Double line = new Line2D.Double(pLine.get(0), pLine.get(1));
        
        int dx = (int) scene[0];
        int dy = (int) scene[1];
        Polygon ppp = new Polygon(new int[] {0, dx, dx, 0}, new int[] {0, 0, dy, dy}, 4);           
        Set set = CrossLineShape.getIntersections(ppp, line);
        System.out.println(set.toArray()[0] + "  " + set.toArray()[0]);
        
        //Area lineArea = new Area(line);
        //lineArea.intersect(polArea);  
        gc2D.draw(line);

//        listCross.forEach(e -> pointCross.add(new Line2D.Double(e.x1, e.y1, e.x2, e.y2)));
//        Area poly = new Area(clip);
//        Area line = new Area(pointCross.get(0)); 
//        //line.intersect(poly); 
//        gc2D.draw(line);
        //listCross.forEach(e -> gc2D.draw(new Line2D.Double(e.x1, e.y1, e.x2, e.y2)));
        //testCross.forEach(e -> gc2D.draw(new Line2D.Double(e.x1, e.y1, e.x2, e.y2))); 
//        GeneralPath recPath = new GeneralPath();
//        recPath.moveTo(0, 0);
//        recPath.lineTo(200, 0);
//        recPath.lineTo(200, canvas[1]);
//        recPath.lineTo(0, canvas[1]);
//        recPath.closePath();
//
//        //listCross.forEach(e -> pointCross.add(new Line2D.Double(e.x1, e.y1, e.x2, e.y2)));
//        Area polArea = new Area(polPath);
//        Area recArea = new Area(recPath);
//        polArea.intersect(recArea);
//
//        gc2D.draw(polArea);
    }

    public void getPathIterator(Area area) {
        final double[] dbl = new double[6];

        pointFrame.clear();
        PathIterator iterator = area.getPathIterator(null);
        while (!iterator.isDone()) {
            final int segmentType = iterator.currentSegment(dbl);
            if (segmentType == PathIterator.SEG_LINETO) {
                //pixelPath.lineTo(floats[0], floats[1]);
                pointFrame.add(new Point2D.Double(dbl[0], dbl[1]));
                System.out.println(dbl[0] + " " + dbl[1]);
            } else if (segmentType == PathIterator.SEG_MOVETO) {
                //pixelPath.moveTo(floats[0], floats[1]);
                pointFrame.add(new Point2D.Double(dbl[0], dbl[1]));
                System.out.println(dbl[0] + " " + dbl[1]);
            } else if (segmentType == PathIterator.SEG_CLOSE) {
                //pixelPath.closePath();
                pointFrame.add(new Point2D.Double(dbl[0], dbl[1]));
                System.out.println(dbl[0] + " " + dbl[1]);
            }
            iterator.next();
        }
        GeneralPath polPath = new GeneralPath();
        polPath.moveTo(pointFrame.get(0).getX(), pointFrame.get(0).getY());
        for (int i = 1; i < pointFrame.size(); ++i) {
            polPath.lineTo(pointFrame.get(i).getX(), pointFrame.get(i).getY());
        }
        polPath.closePath();
        Area polArea = new Area(polPath);
        gc2D.draw(polArea);
    }

    public void clipping(double x1, double y1, double x2, double y2) {
        //double angl = UGeo.horizontAngl(x1, y1, x2, y2);

            double[] crossX1 = UGeo.cross(x1, y1, x2, y2, 0, 0, scene[0], 0);
            double[] crossX2 = UGeo.cross(x1, y1, x2, y2, 0, scene[1], scene[0], scene[1]);
            double[] crossY1 = UGeo.cross(x1, y1, x2, y2, 0, 0, 0, scene[1]);
            double[] crossY2 = UGeo.cross(x1, y1, x2, y2, scene[0], 0, scene[0], scene[1]);
            
            System.out.println(crossX1[0] + ":" + crossX1[1] + "   " + crossX2[0] + ":" + crossX2[1]);
            System.out.println(crossY1[0] + ":" + crossY1[1] + "   " + crossY2[0] + ":" + crossY2[1]);
            
            
//        } else {
//            double[] cross1 = UGeo.cross(x1, y1, x2, y2, 0, 0, scene[0], 0);
//            double[] cross2 = UGeo.cross(x1, y1, x2, y2, 0, 0, 0, scene[1]); 
//            System.out.println(cross1[0] + ":" + cross1[1] + "   " + cross2[0] + ":" + cross2[1]);
//        }    
           //double[] cross1 = UGeo.cross2(new Point2D.Double(x1, y1), new Point2D.Double(x2, y2), new Point2D.Double(0, 0), new Point2D.Double(scene[0], 0));
           //double[] cross2 = UGeo.cross2(new Point2D.Double(x1, y1), new Point2D.Double(x2, y2), new Point2D.Double(0, 0), new Point2D.Double(0, scene[1]));
            

//            GeneralPath recPath = new GeneralPath();
//            recPath.moveTo(0, 0);
//            recPath.lineTo(200, 0);
//            recPath.lineTo(200, scene[1]);
//            recPath.lineTo(0, scene[1]);
//            recPath.closePath();
        ///} 
    }

}
