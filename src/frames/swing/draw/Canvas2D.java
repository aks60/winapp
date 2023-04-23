package frames.swing.draw;

import builder.Geocalc;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JComponent;

public class Canvas2D extends JComponent {

    private Geocalc winc;

    public Canvas2D(Geocalc winc) {
        this.winc = winc;
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                winc.mousePressed.forEach(e -> e.mouseEvent(event));
                repaint();
            }

            public void mouseReleased(MouseEvent event) {
                winc.mouseReleased.forEach(e -> e.mouseEvent(event));
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {

            public void mouseDragged(MouseEvent event) {
                winc.mouseDragged.forEach(e -> e.mouseEvent(event));
                repaint();
            }
        });
        addComponentListener(new ComponentAdapter() {
 
            public void componentResized(ComponentEvent event) {
            }
        });
    }

    public void paintComponent(Graphics g) {
        //System.out.println("frames.swing.draw.Canvas2D.paintComponent() 2D2D2D2D2D2D");
        winc.gc2D = (Graphics2D) g;
        winc.draw();
    }
}
