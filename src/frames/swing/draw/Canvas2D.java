package frames.swing.draw;

import builder.Geocalc;
import builder.geoms.UGeo;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import javax.swing.JComponent;

/**
 * См, Java,2014-Том 2, Расш средства прогр This component draws a shape and
 * Позволяет пользователю перемещать точки вершин многоугольника
 */
public class Canvas2D extends JComponent {

    private static int SIZE = 16;
    private Geocalc geo;
    private int indexPoly = -1; //текущей индекс вершины
    private int indexLine = -1; //текущей индекс вершины

    public Canvas2D(Geocalc geo) {
        this.geo = geo;
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                indexPoly = -1;
                indexLine = -1;
                Point p = event.getPoint();
                for (int i = 0; i < geo.pPoly.size(); i++) {
                    double x = geo.pPoly.get(i).getX() - SIZE / 2;
                    double y = geo.pPoly.get(i).getY() - SIZE / 2;                    
                    Rectangle2D r = new Rectangle2D.Double(x, y, SIZE, SIZE);
                    if (r.contains(p)) {
                        indexPoly = i;
                        return;
                    }
                }                
                for (int i = 0; i < geo.pLine.size(); i++) {
                    double x = geo.pLine.get(i).getX() - SIZE / 2;
                    double y = geo.pLine.get(i).getY() - SIZE / 2;                    
                    Rectangle2D r = new Rectangle2D.Double(x, y, SIZE, SIZE);
                    if (r.contains(p)) {
                        indexLine = i;
                        return;
                    }
                }                
                repaint();
            }

            public void mouseReleased(MouseEvent event) {
                indexPoly = -1;
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {

            public void mouseDragged(MouseEvent event) {
                if (indexPoly != -1) {
                    geo.pPoly.set(indexPoly, event.getPoint());
                }
                if (indexLine != -1) {
                    geo.pLine.set(indexLine, event.getPoint());
                }
                repaint();
            }
        });
        indexPoly = -1;
    }

    public void paintComponent(Graphics g) {
        if (geo.pPoly != null) {
            geo.gc2D = (Graphics2D) g;

            //Квадратик вокруг вершины
            if (indexPoly != -1) {
                for (int i = 0; i < geo.pPoly.size(); i++) {
                    double x = geo.pPoly.get(i).getX() - SIZE / 2;
                    double y = geo.pPoly.get(i).getY() - SIZE / 2;
                    geo.gc2D.draw(new Rectangle2D.Double(x, y, SIZE, SIZE));
                }
            }
            geo.draw();
        }
    }
}
