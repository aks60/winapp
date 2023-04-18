package builder.geoms;

import builder.Geocalc;
import common.listener.ListenerMouse;
import java.awt.event.MouseEvent;

public class Elem2Simple { //extends MouseAdapter, MouseMotionAdapter {
   
    public Geocalc geo = null;
    
    public Elem2Simple(Geocalc geo) {
        this.geo = geo;
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