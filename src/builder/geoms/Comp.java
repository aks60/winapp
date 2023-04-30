package builder.geoms;

import builder.Geocalc;
import builder.script.GeoElem;
import common.listener.ListenerMouse;
import java.awt.Point;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.List;

public abstract class Comp {

    public int SIZE = 24;
    public double id;
    public Geocalc winc = null;
    public Comp owner = null; //владелец
    public GeoElem gson = null; //Gson object конструкции
    public Area area = null;
    public boolean ev[] = {false, false};

    public Comp(Geocalc winc, GeoElem gson, Comp owner) {
        this.id = gson.id;
        this.winc = winc;
        this.owner = owner;
        this.gson = gson;
    }

    public void build() {
    }

    public void paint() {
    }

    public List<Comp> childs() {
        return null;
    }

    public void mouseEvent() {
        ListenerMouse mousePressed = (event) -> {
            //SIZE = (int) (SIZE * winc.scale);
            Rectangle2D r1 = new Rectangle2D.Double(x1() * winc.scale - SIZE / 2, y1() * winc.scale - SIZE / 2, SIZE, SIZE);
            Rectangle2D r2 = new Rectangle2D.Double(x2() * winc.scale - SIZE / 2, y2() * winc.scale - SIZE / 2, SIZE, SIZE);
            //Point point = new Point((int) (event.getX() * winc.scale) ,(int) (event.getY() * winc.scale));
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
            //winc.draw();
        };
        ListenerMouse mouseDragge = (event) -> {
            if (ev[0] == true) {
                //System.out.println("mouseDragge()=0  " + event.getX() + " : " + event.getY());
                x1(event.getX() / winc.scale);
                y1(event.getY() / winc.scale);
                // winc.draw();
            } else if (ev[1] == true) {
                //System.out.println("mouseDragge()=1 " + event.getX() + " : " + event.getY());
                x2(event.getX() / winc.scale);
                y2(event.getY() / winc.scale);
                //winc.draw();
            }
        };
        this.winc.mousePressed.add(mousePressed);
        this.winc.mouseReleased.add(mouseReleased);
        this.winc.mouseDragged.add(mouseDragge);
    }

    public String toString() {
        return " id=" + id + " ";
    }
    
    // <editor-fold defaultstate="collapsed" desc="GET-SET">
    public void setLocation(double x1, double y1, double x2, double y2) {
        if (ev[0] == false && ev[1] == false) {
            gson.x1 = x1;
            gson.y1 = y1;
            gson.x2 = x2;
            gson.y2 = y2;
        }
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
    // </editor-fold>
}
