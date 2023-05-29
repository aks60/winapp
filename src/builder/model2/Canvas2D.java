package builder.model2;

import builder.Wingeo;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;

public class Canvas2D extends JComponent {

    private Wingeo wing;

    public Canvas2D(Wingeo wing) {
        this.wing = wing;
        wing.canvas = this;
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                wing.mousePressed.forEach(e -> e.mouseEvent(event));
                repaint();
            }

            public void mouseReleased(MouseEvent event) {
                wing.mouseReleased.forEach(e -> e.mouseEvent(event));
                repaint();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {

            public void mouseDragged(MouseEvent event) {
                wing.mouseDragged.forEach(e -> e.mouseEvent(event));
                repaint();
            }
        });
        addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent event) {
                wing.scale = scale(wing, 4, 16);
            }
        });
    }

    public void paintComponent(Graphics g) {
        //System.out.println("Canvas2D.paintComponent()");
        wing.gc2D = (Graphics2D) g;
        wing.gc2D.scale(wing.scale, wing.scale);
        wing.draw();
    }

    public double scale(Wingeo wing, double dx, double dy) {
        Rectangle2D rec = wing.root.area.getBounds2D();
        return ((getWidth() + dx) / rec.getWidth() > (getHeight() + dx) / rec.getHeight())
                ? (getHeight() + dx) / (rec.getHeight() + dy) : (getWidth() + dx) / (rec.getWidth() + dy);
    }
}
