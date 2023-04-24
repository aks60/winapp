package builder;

import builder.geoms.Area2Polygon;
import builder.geoms.Area2Simple;
import builder.geoms.Area2Stvorka;
import builder.geoms.Comp;
import builder.geoms.Elem2Cross;
import builder.geoms.Elem2Frame;
import builder.geoms.Elem2Glass;
import builder.geoms.UGeo;
import builder.script.GeoElem;
import builder.script.GeoRoot;
import builder.script.test.Bimax2;
import com.google.gson.GsonBuilder;
import common.listener.ListenerMouse;
import enums.Type;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Geocalc {

    public Graphics2D gc2D = null; //графический котекст рисунка  
    public ArrayList<ListenerMouse> mousePressed = new ArrayList(),
            mouseReleased = new ArrayList(), mouseDragged = new ArrayList();

    public List<Elem2Frame> listFrame = new ArrayList();
    public List<Elem2Cross> listCross = new ArrayList();

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
                    Elem2Frame elem5e = new Elem2Frame(this, js, owner);
                    listFrame.add(elem5e);

                } else if (Type.IMPOST == js.type || Type.SHTULP == js.type || Type.STOIKA == js.type) {
                    Elem2Cross elem5e = new Elem2Cross(this, js, owner);
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
        GeneralPath polPath = new GeneralPath();
        polPath.moveTo(listFrame.get(0).x1(), listFrame.get(0).y1());
        for (int i = 1; i < listFrame.size(); ++i) {
            polPath.lineTo(listFrame.get(i).x1(), listFrame.get(i).y1());
        }
        polPath.closePath();

        //Преобразование
        Area polArea = new Area(polPath);
        Area area[] = UGeo.split(polArea, listCross.get(0));
        //gc2D.draw(area[1]); //рисую
        for (Elem2Cross cross : listCross) {
            Point2D[] point2D = UGeo.cross(polArea, cross);
            if (point2D != null && point2D.length > 1) {
                System.out.println(point2D[0].getX() + " " + point2D[0].getY() + " " + point2D[1].getX() + " " + point2D[1].getY());
                //listCross.get(0).setLocation(point2D[0].getX(), point2D[0].getY(), point2D[1].getX(), point2D[1].getY());
                gc2D.draw(new Line2D.Double(point2D[0], point2D[1])); //рисую
            }
        }
    }
}
