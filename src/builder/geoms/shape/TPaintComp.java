package builder.geoms.shape;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import javax.swing.JComponent;

/**
 * См, Java,2014-Том 2, Расш средства прогр This component draws a shape and
 * Позволяет пользователю перемещать точки вершин многоугольника
 */
class TPaintComp extends JComponent {

    private Point2D[] points; //вершины многоугольника
    private static Random generator = new Random();
    private static int SIZE = 10;
    private int current; //индекс текущей вершины
    private TShape shapeMaker;
    
    public TPaintComp() {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                //System.out.println(".mousePressed()");
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
                //System.out.println(".mouseReleased()");
                current = -1;
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {

            public void mouseDragged(MouseEvent event) {
                //System.out.println(".mouseDragged()");
                if (current == -1) {
                    return;
                }
                points[current] = event.getPoint();
                repaint();
            }
        });
        current = -1;
    }

    /**
     * Set a shape maker and initialize it with a random point set.
     *
     * @param aShapeMaker a shape maker that defines a shape from a point set
     */
    public void setShapeMaker(TShape aShapeMaker) {
        //System.out.println("chapetest.ShapeComponent.setShapeMaker()");
        shapeMaker = aShapeMaker;
        int n = shapeMaker.getPointCount();
        points = new Point2D[n];
        if (aShapeMaker instanceof Polygon) {
            points[0] = new Point2D.Double(200, 50);
            points[1] = new Point2D.Double(250, 100);
            points[2] = new Point2D.Double(200, 150);
            points[3] = new Point2D.Double(100, 150);
            points[4] = new Point2D.Double(50, 100);
            points[5] = new Point2D.Double(100, 50);
        } else {
            for (int i = 0; i < n; i++) {
                double x = generator.nextDouble() * getWidth();
                double y = generator.nextDouble() * getHeight();
                points[i] = new Point2D.Double(x, y);
            }
        }
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
        Shape s = shapeMaker.makeShape(points);
        g2.draw(s);
        
        if (shapeMaker instanceof Polygon) {
            Point2D lines[] = {new Point2D.Double(180.0, 10.0), new Point2D.Double(180, 480)};      
            //g2.draw(new Line2D.Double(lines[0].getX() + 5, lines[0].getY() + 5, lines[1].getX() + 5, lines[1].getY() + 5));
            lines = CyrusBeck.calc(points, lines, points.length);            
            g2.draw(new Line2D.Double(lines[0].getX(), lines[0].getY(), lines[1].getX(), lines[1].getY()));
        }
    }
}
