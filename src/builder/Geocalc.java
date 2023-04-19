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
import common.eProp;
import common.listener.ListenerMouse;
import enums.Type;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Geocalc {

    public Graphics2D gc2D = null; //графический котекст рисунка  
    public double scale = 1; //коэффициент сжатия

    public ArrayList<ListenerMouse> mousePressedList = new ArrayList();
    public ArrayList<ListenerMouse> mouseReleasedList = new ArrayList();
    public ArrayList<ListenerMouse> mouseDraggedList = new ArrayList();

    public List<Elem2Frame> listFrame = new ArrayList();
    public List<Elem2Frame> listFrame2 = new ArrayList();
    public List<Elem2Cross> listCross = new ArrayList();
    public List<Elem2Cross> listCross2 = new ArrayList();

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
        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(new com.google.gson.JsonParser().parse(script)));

        gson = new GsonBuilder().create().fromJson(script, GeoRoot.class);      
        root = new Area2Polygon(this, gson);
        
        for (int i = 0; i < gson.line.size(); ++i) {
            listCross2.add(new Elem2Cross(this, gson, null, gson.line.get(i), gson.line.get(++i), gson.line.get(++i), gson.line.get(++i)));
        }
        if (gson.poly.size() % 4 != 0) {
            gson.poly.addAll(List.of(gson.poly.get(0), gson.poly.get(1)));
        }
        for (int i = 0; i < gson.poly.size(); ++i) {
            listFrame2.add(new Elem2Frame(this, gson, null, gson.poly.get(i), gson.poly.get(++i), gson.poly.get(++i), gson.poly.get(++i)));
        }
        
        elements(root, gson);
    }

    public void draw() {

        //Линия
        listCross2.forEach(e -> gc2D.draw(new Line2D.Double(e.x1, e.y1, e.x2, e.y2)));

//        //Клип
//        GeneralPath clip = new GeneralPath();
//        clip.moveTo(pLine.get(0).getX(), pLine.get(0).getY());
//        clip.lineTo(pLine.get(1).getX(), pLine.get(1).getY());
//        clip.lineTo(400, pLine.get(1).getY());
//        clip.lineTo(400, pLine.get(0).getY());
//        clip.closePath();
        //Многоугольник    
        GeneralPath pathPoly = new GeneralPath();
        pathPoly.moveTo(listFrame2.get(0).x1, listFrame2.get(0).y1);
        pathPoly.lineTo(listFrame2.get(0).x2, listFrame2.get(0).y2);
        for (int i = 1; i < listFrame2.size(); ++i) {
            pathPoly.lineTo(listFrame2.get(i).x1, listFrame2.get(i).y1);
            pathPoly.lineTo(listFrame2.get(i).x2, listFrame2.get(i).y2);
        }
        pathPoly.closePath();

        gc2D.draw(pathPoly);
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
}
