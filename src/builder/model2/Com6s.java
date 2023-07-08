package builder.model2;

import builder.Wingeo;
import builder.script.GeoElem;
import com.google.gson.JsonObject;
import common.listener.ListenerMouse;
import dataset.Record;
import domain.eArtikl;
import enums.Type;
import java.awt.Point;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.List;

public abstract class Com6s {

    public int SIZE = 24;
    public double id;
    public Wingeo wing = null;
    public Com6s owner = null; //владелец
    public GeoElem gson = null; //Gson object конструкции
    public Type type = Type.NONE; //Тип элемента или окна
    public Area area = null;
    private boolean ev[] = {false, false};
    public Double x3 = null, y3 = null, x4 = null, y4 = null;//внутренние координаты area
    public int colorID1 = -1, colorID2 = -1, colorID3 = -1; //1-базовый 2-внутренний 3-внешний 
    public Record sysprofRec = null; //профиль в системе
    public Record artiklRec = null;  //мат. средства
    public Record artiklRecAn = null;  //аналог мат. средства     

    public Com6s(Wingeo wing, GeoElem gson, Com6s owner) {
        this.id = gson.id;
        this.wing = wing;
        this.owner = owner;
        this.gson = gson;
        this.type = gson.type;
    }

    public void setLocation() {
    }

    public void paint() {
    }

    public List<Com6s> childs() {
        return null;
    }

    public void mouseEvent() {
        ListenerMouse mousePressed = (event) -> {
            //SIZE = (int) (SIZE * wing.scale);
            Rectangle2D r1 = new Rectangle2D.Double(x1() * wing.scale - SIZE / 2, y1() * wing.scale - SIZE / 2, SIZE, SIZE);
            Rectangle2D r2 = new Rectangle2D.Double(x2() * wing.scale - SIZE / 2, y2() * wing.scale - SIZE / 2, SIZE, SIZE);
            //System.out.println(x1() + " " + y1() + "  :  " + );
            if (r1.contains(event.getPoint())) {
                //System.out.println("mousePressed()+0");
                ev[0] = true;
            } else if (r2.contains(event.getPoint())) {
                //System.out.println("mousePressed()+1");
                ev[1] = true;
            }
        };
        ListenerMouse mouseReleased = (event) -> {
            //System.out.println("mouseReleased()-1");
            ev[0] = false;
            ev[1] = false;
        };
        ListenerMouse mouseDragge = (event) -> {
            int m = 4;
            int dx = wing.canvas.getWidth() - event.getX();
            int dy = wing.canvas.getHeight()- event.getY();
            if (ev[0] == true) {
                if (event.getX() > m && dx > m && event.getY() > m && dy > m) { //контроль выхода за канву
                    x1(Math.round(event.getX() / wing.scale));
                    y1(Math.round(event.getY() / wing.scale));
                }
            } else if (ev[1] == true) {
                if (event.getX() > m && dx > m && event.getY() > m && dy > m) { //контроль выхода за канву
                    x2(Math.round(event.getX() / wing.scale));
                    y2(Math.round(event.getY() / wing.scale));
                }
            }
        };
        this.wing.mousePressed.add(mousePressed);
        this.wing.mouseReleased.add(mouseReleased);
        this.wing.mouseDragged.add(mouseDragge);
    }

    public boolean isJson(JsonObject jso, String key) {
        if (jso == null) {
            return false;
        }
        if (jso.isJsonNull()) {
            return false;
        }
        if (jso.get(key) == null) {
            return false;
        }
        return true;
    }

    public Area rectangl(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        try {
            GeneralPath p = new GeneralPath();
            p.moveTo(x1, y1);
            p.lineTo(x2, y2);
            p.lineTo(x3, y3);
            p.lineTo(x4, y4);
            p.closePath();
            return new Area(p);
            
        } catch (Exception e) {
            System.err.println("Ошибка:Comp.polygon()" + toString() + e);
            return null;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="GET-SET">
    public void setDimension(double x1, double y1, double x2, double y2) {
        if (ev[0] == false && ev[1] == false) {
            gson.x1 = x1;
            gson.y1 = y1;
            gson.x2 = x2;
            gson.y2 = y2;
        }
    }

    public void addDimension(double x3, double y3, double x4, double y4) {
        this.x3 = x3;
        this.y3 = y3;
        this.x4 = x4;
        this.y4 = y4;
    }

    public double x1() {
        return (gson.x1 != null) ? gson.x1 : -1;
    }

    public double y1() {
        return (gson.y1 != null) ? gson.y1 : -1;
    }

    public double x2() {
        return (gson.x2 != null) ? gson.x2 : -1;
    }

    public double y2() {
        return (gson.y2 != null) ? gson.y2 : -1;
    }

    public void x1(double v) {
        gson.x1 = v;
    }

    public void y1(double v) {
        gson.y1 = v;
    }

    public void x2(double v) {
        gson.x2 = v;
    }

    public void y2(double v) {
        gson.y2 = v;
    }

    @Override
    public String toString() {
        String art = (artiklRecAn == null) ? "null" : artiklRecAn.getStr(eArtikl.code);
        double ownerID = (owner == null) ? -1 : owner.id;
        return " art=" + art + ", type=" + type + ", owner=" + ownerID + ", id=" + id
                + ", x1=" + x1() + ", y1=" + y1() + ", x2=" + x2() + ", y2=" + y2();
    }
    // </editor-fold>
}
