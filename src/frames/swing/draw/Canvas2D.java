package frames.swing.draw;

import builder.Geocalc;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JComponent;

public class Canvas2D extends JComponent {

    private Geocalc geo;

    public Canvas2D(Geocalc geo) {
        this.geo = geo;
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                geo.mousePressedList.forEach(e -> e.mouseEvent(event));
                repaint();
            }

            public void mouseReleased(MouseEvent event) {
                geo.mouseReleasedList.forEach(e -> e.mouseEvent(event));
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {

            public void mouseDragged(MouseEvent event) {
                geo.mouseDraggedList.forEach(e -> e.mouseEvent(event));
                repaint();
            }
        });
    }

    public void paintComponent(Graphics g) {
        //System.out.println("frames.swing.draw.Canvas2D.paintComponent() 2D2D2D2D2D2D");
        geo.gc2D = (Graphics2D) g;
        geo.draw();
    }
}
