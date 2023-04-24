package builder.geoms;

import builder.Geocalc;
import builder.script.GeoElem;
import common.listener.ListenerMouse;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class Elem2Simple extends Comp { //extends MouseAdapter, MouseMotionAdapter {

    public boolean f[] = {false, false};

    public Elem2Simple(Geocalc winc, GeoElem gson, Comp owner) {
        super(winc, gson, owner);
        mouseEvent();
    }

    public Elem2Simple(Geocalc winc, GeoElem gson, Comp owner, double x1, double y1, double x2, double y2) {
        super(winc, gson, owner, x1, y1, x2, y2);
        mouseEvent();
    }

    public void mouseEvent() {
        ListenerMouse mousePressed = (event) -> {
            //System.out.println("mousePressed()+++");
            Rectangle2D r1 = new Rectangle2D.Double(x1 - 8, y1 - 8, 16, 16);
            Rectangle2D r2 = new Rectangle2D.Double(x2 - 8, y2 - 8, 16, 16);
            if (r1.contains(event.getPoint())) {
                f[0] = true;
            } else if (r2.contains(event.getPoint())) {
                f[1] = true;
            }
            //winc.testCross.add(new Elem2Cross(winc, event.getX(), event.getY(), vent.getX(), event.getY()));
        };
        ListenerMouse mouseReleased = (event) -> {
            //System.out.println("mouseReleased()---");
            f[0] = false;
            f[1] = false;
        };
        ListenerMouse mouseDragge = (event) -> {
            if (f[0] == true) {
                //System.out.println("mouseDragge()===");
                x1 = event.getX();
                y1 = event.getY();
                winc.draw();
            } else if (f[1] == true) {
                System.out.println("mouseDragge()===");
                x2 = event.getX();
                y2 = event.getY();
                winc.draw();
            }
        };
        this.winc.mousePressed.add(mousePressed);
        this.winc.mouseReleased.add(mouseReleased);
        this.winc.mouseDragged.add(mouseDragge);
    }

    public void createShape() {
        
    }
    
    public void paint() {
        if (f[0] == true) {

        } else if (f[1] == true) {

        }
    }
}
