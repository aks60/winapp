package builder.geoms;

import builder.Geocalc;
import common.listener.ListenerMouse;
import java.awt.event.MouseEvent;

public class Elem2Simple { //extends MouseAdapter, MouseMotionAdapter {

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
            System.out.println("mousePressed()");
        };
        ListenerMouse mouseReleased = (event) -> {
            System.out.println("mouseReleased()");
        };
        ListenerMouse mouseDragge = (event) -> {
            System.out.println("mouseDragge()");
        };
        this.geo.mousePressedList.add(mousePressed);
        this.geo.mouseReleasedList.add(mouseReleased);
        this.geo.mouseDraggedList.add(mouseDragge);
    }
}
