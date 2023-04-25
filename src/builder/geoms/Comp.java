package builder.geoms;

import builder.Geocalc;
import builder.script.GeoElem;
import common.listener.ListenerMouse;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.List;

public class Comp {

    public double id;
    public Geocalc winc = null;
    public Comp owner = null; //владелец
    public GeoElem gson = null; //Gson object конструкции
    public GeneralPath polygon = new GeneralPath();
    public boolean ev[] = {false, false};

    public Comp(Geocalc winc, GeoElem gson, Comp owner) {
        this.id = gson.id;
        this.winc = winc;
        this.owner = owner;
        this.gson = gson;
    }

    public List<Comp> childs() {
        return null;
    }

    public void mouseEvent() {
        ListenerMouse mousePressed = (event) -> {
                //System.out.println("mousePressed()+++");
                Rectangle2D r1 = new Rectangle2D.Double(x1() - 8, y1() - 8, 16, 16);
                Rectangle2D r2 = new Rectangle2D.Double(x2() - 8, y2() - 8, 16, 16);
                if (r1.contains(event.getPoint())) {
                    ev[0] = true;
                } else if (r2.contains(event.getPoint())) {
                    ev[1] = true;
                }
        };
        ListenerMouse mouseReleased = (event) -> {
            //System.out.println("mouseReleased()---");
            ev[0] = false;
            ev[1] = false;
        };
        ListenerMouse mouseDragge = (event) -> {
            if (ev[0] == true) {
                System.out.println("mouseDragge()-0  " + event.getX() + " : " + event.getY());
                    x1(event.getX());
                    y1(event.getY());
                    winc.draw();
            } else if (ev[1] == true) {
                System.out.println("mouseDragge()-1 " + event.getX() + " : " + event.getY());
                    x2(event.getX());
                    y2(event.getY());
                    winc.draw();
            }
        };
        this.winc.mousePressed.add(mousePressed);
        this.winc.mouseReleased.add(mouseReleased);
        this.winc.mouseDragged.add(mouseDragge);
    }
    
    public void paint() {
        
    }

    // <editor-fold defaultstate="collapsed" desc="GET-SET">
    public void setLocation(double x1, double y1, double x2, double y2) {
        gson.x1 = x1;
        gson.y1 = y1;
        gson.x2 = x2;
        gson.y2 = y2;
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
