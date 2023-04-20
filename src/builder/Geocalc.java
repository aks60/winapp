package builder;

import builder.geoms.Area2Polygon;
import builder.geoms.Area2Simple;
import builder.geoms.Area2Stvorka;
import builder.geoms.Comp;
import builder.geoms.Elem2Cross;
import builder.geoms.Elem2Frame;
import builder.geoms.Elem2Glass;
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
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Geocalc {

    public Graphics2D gc2D = null; //графический котекст рисунка  
    public double scale = 1; //коэффициент сжатия

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
        
        GeneralPath clip = new GeneralPath();
        clip.moveTo(pointFrame.get(0).getX(), pointFrame.get(0).getY());
        for (int i = 1; i < pointFrame.size(); ++i) {
            clip.lineTo(pointFrame.get(i).getX(), pointFrame.get(i).getY());
        }
        clip.closePath();
        Area ar1 = new Area(clip); 
        //gc2D.draw(ar1);
        
        Line2D li = new Line2D.Double(10.0, 10, 280, 400);
        Rectangle2D re = new Rectangle2D.Double(10, 10, 200, 200);
        GeneralPath lp = new GeneralPath();
        
        lp.moveTo(0,0);
        lp.lineTo(200, 0);
        lp.lineTo(200, 400);
        lp.lineTo(0, 400);
        lp.closePath();
        
        Area ar2 = new Area(li);
        Area ar3 = new Area(re);
        Area ar4 = new Area(lp);
        
        ar1.intersect(ar4);
        gc2D.draw(ar1);
        
        //Линия
//        List<Point2D> pLine = new ArrayList();
//        pLine.add(new Point2D.Double(10.0, 0));
//        pLine.add(new Point2D.Double(280, 400));
//        Area area = new Area(clip); 
//        Area line = new Area(new Line2D.Double(pLine.get(0), pLine.get(1))); 
//        //line.intersect(area);          
//        gc2D.draw(line); 
        
//        listCross.forEach(e -> pointCross.add(new Line2D.Double(e.x1, e.y1, e.x2, e.y2)));
//        Area poly = new Area(clip);
//        Area line = new Area(pointCross.get(0)); 
//        //line.intersect(poly); 
//        gc2D.draw(line);
        
        //listCross.forEach(e -> gc2D.draw(new Line2D.Double(e.x1, e.y1, e.x2, e.y2)));
        //testCross.forEach(e -> gc2D.draw(new Line2D.Double(e.x1, e.y1, e.x2, e.y2)));
    }
}
