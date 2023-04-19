package builder;

import builder.geoms.Elem2Cross;
import builder.geoms.Elem2Frame;
import builder.script.GeoRoot;
import builder.script.test.Bimax2;
import com.google.gson.GsonBuilder;
import common.listener.ListenerMouse;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class Geocalc {

    public Graphics2D gc2D = null; //графический котекст рисунка  
    public double scale = 1; //коэффициент сжатия

    public ArrayList<ListenerMouse> mousePressedList = new ArrayList();
    public ArrayList<ListenerMouse> mouseReleasedList = new ArrayList();
    public ArrayList<ListenerMouse> mouseDraggedList = new ArrayList();

    public List<Elem2Frame> listFrame = new ArrayList();
    public List<Elem2Cross> listCross = new ArrayList();
    
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
        //System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(new com.google.gson.JsonParser().parse(script)));


        rootGeo = new GsonBuilder().create().fromJson(script, GeoRoot.class);
        for (int i = 0; i < rootGeo.line.size(); ++i) {
            listCross.add(new Elem2Cross(this, rootGeo.line.get(i), rootGeo.line.get(++i), rootGeo.line.get(++i), rootGeo.line.get(++i)));
        }            
        rootGeo.poly.addAll(List.of(rootGeo.poly.get(0), rootGeo.poly.get(1)));
        for (int i = 0; i < rootGeo.poly.size(); ++i) {
            listFrame.add(new Elem2Frame(this, rootGeo.poly.get(i), rootGeo.poly.get(++i), rootGeo.poly.get(++i), rootGeo.poly.get(++i)));
        }
    }

    public void draw() {

        //Линия
        listCross.forEach(e -> gc2D.draw(new Line2D.Double(e.x1, e.y1, e.x2, e.y2)));

//        //Клип
//        GeneralPath clip = new GeneralPath();
//        clip.moveTo(pLine.get(0).getX(), pLine.get(0).getY());
//        clip.lineTo(pLine.get(1).getX(), pLine.get(1).getY());
//        clip.lineTo(400, pLine.get(1).getY());
//        clip.lineTo(400, pLine.get(0).getY());
//        clip.closePath();
        
        //Многоугольник    
        GeneralPath pathPoly = new GeneralPath();
        pathPoly.moveTo(listFrame.get(0).x1, listFrame.get(0).y1);
        pathPoly.lineTo(listFrame.get(0).x2, listFrame.get(0).y2);
        for (int i = 1; i < listFrame.size(); ++i) {
            pathPoly.lineTo(listFrame.get(i).x1, listFrame.get(i).y1);
            pathPoly.lineTo(listFrame.get(i).x2, listFrame.get(i).y2);
        }
        pathPoly.closePath();  
       
        gc2D.draw(pathPoly);
    }
}
