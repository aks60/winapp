package builder.geoms;

import builder.Geocalc;
import common.listener.ListenerMouse;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class Elem2Simple { //extends MouseAdapter, MouseMotionAdapter {

    public boolean f[] = {false, false};
    public double x1 = -1, y1 = -1, x2 = -1, y2 = -1;

    public Geocalc geo = null;

    public Elem2Simple(Geocalc geo, double x1, double y1, double x2, double y2) {
        this.geo = geo;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        mouseEvent();
    }

    public void mouseEvent() {
        ListenerMouse mousePressed = (event) -> {
            System.out.println("mousePressed()+++");
            Rectangle2D r1 = new Rectangle2D.Double(x1 - 8, y1 - 8, 16, 16);
            Rectangle2D r2 = new Rectangle2D.Double(x2 - 8, y2 - 8, 16, 16);
            if (r1.contains(event.getPoint())) {
                f[0] = true; 
            } else if (r2.contains(event.getPoint())) {
                f[1] = true; 
            }
        };
        ListenerMouse mouseReleased = (event) -> {
            System.out.println("mouseReleased()---");
            f[0] = false;
            f[1] = false;
        };
        ListenerMouse mouseDragge = (event) -> {
            if (f[0] == true) {
                System.out.println("mouseDragge()===");
                x1 = event.getX();
                y1 = event.getY();
            } else if (f[1] == true) {
                System.out.println("mouseDragge()===");
                x2 = event.getX();
                y2 = event.getY();
            }
        };
        this.geo.mousePressedList.add(mousePressed);
        this.geo.mouseReleasedList.add(mouseReleased);
        this.geo.mouseDraggedList.add(mouseDragge);
    }

//    public void paint() {
//
//    }
}
