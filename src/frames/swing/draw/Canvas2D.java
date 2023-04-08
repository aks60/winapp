package frames.swing.draw;

import builder.geoms.shape.CyrusBeck;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import javax.swing.JComponent;

/**
 * См, Java,2014-Том 2, Расш средства прогр This component draws a shape and
 * Позволяет пользователю перемещать точки вершин многоугольника
 */
public class Canvas2D extends JComponent {

    private Point2D[] points; //вершины многоугольника
    private static int SIZE = 10;
    private int current; //индекс текущей вершины
    private Shape shapeMaker = null;

    public Canvas2D() {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                System.out.println(".mousePressed()");
                Point p = event.getPoint();
                for (int i = 0; i < points.length; i++) {
                    double x = points[i].getX() - SIZE / 2;
                    double y = points[i].getY() - SIZE / 2;
                    Rectangle2D r = new Rectangle2D.Double(x, y, SIZE, SIZE);
                    if (r.contains(p)) {
                        current = i;
                        return;
                    }
                }
            }

            public void mouseReleased(MouseEvent event) {
                System.out.println(".mouseReleased()");
                current = -1;
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {

            public void mouseDragged(MouseEvent event) {
                System.out.println(".mouseDragged()");
                if (current == -1) {
                    return;
                }
                points[current] = event.getPoint();
                repaint();
            }
        });
        current = -1;
    }


    public void setShapeMaker(Shape aShapeMaker) {
        //System.out.println("chapetest.ShapeComponent.setShapeMaker()");
        points = new Point2D[6];
        points[0] = new Point2D.Double(200, 50);
        points[1] = new Point2D.Double(250, 100);
        points[2] = new Point2D.Double(200, 150);
        points[3] = new Point2D.Double(100, 150);
        points[4] = new Point2D.Double(50, 100);
        points[5] = new Point2D.Double(100, 50);
        
        shapeMaker = makeShape(points);
        
        repaint();
    }

    public void paintComponent(Graphics g) {
        if (points == null) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g;

        // Квадратик вокруг вершины.
        for (int i = 0; i < points.length; i++) {
            double x = points[i].getX() - SIZE / 2;
            double y = points[i].getY() - SIZE / 2;
            g2.draw(new Rectangle2D.Double(x, y, SIZE, SIZE));
        }
        
        
        g2.draw(shapeMaker);

        Point2D lines[] = {new Point2D.Double(180.0, 10.0), new Point2D.Double(180, 480)};
        //g2.draw(new Line2D.Double(lines[0].getX() + 5, lines[0].getY() + 5, lines[1].getX() + 5, lines[1].getY() + 5));
        lines = CyrusBeck.calc(points, lines, points.length);
        g2.draw(new Line2D.Double(lines[0].getX(), lines[0].getY(), lines[1].getX(), lines[1].getY()));
    }
    
    public Shape makeShape(Point2D[] p) {

        //System.out.println("chapetest.PolygonMaker.makeShape()");
        GeneralPath polygon = new GeneralPath();

        polygon.moveTo((float) p[0].getX(), (float) p[0].getY());
        for (int i = 1; i < p.length; i++) {
            polygon.lineTo((float) p[i].getX(), (float) p[i].getY());
        }    
        polygon.closePath();
        return polygon;
    }    
}
