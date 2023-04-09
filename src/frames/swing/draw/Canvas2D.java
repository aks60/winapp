package frames.swing.draw;

import builder.geoms.UAlgo;
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
import javax.swing.JComponent;

/**
 * См, Java,2014-Том 2, Расш средства прогр This component draws a shape and
 * Позволяет пользователю перемещать точки вершин многоугольника
 */
public class Canvas2D extends JComponent {

    private static int SIZE = 10;
    private int current = -1; //текущей индекс вершины
    private Shape shapeMaker = null; //форма могоугольника
    private Point2D[] points; //вершины многоугольника

    public Canvas2D() {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                Point p = event.getPoint();
                for (int i = 0; i < points.length; i++) {
                    double x = points[i].getX() - SIZE / 2;
                    double y = points[i].getY() - SIZE / 2;
                    Rectangle2D r = new Rectangle2D.Double(x, y, SIZE, SIZE);
                    if (r.contains(p)) {
                        current = i;
                        //System.out.println(".mousePressed()");
                        return;
                    }
                }
                System.out.println(".mousePressed() = -1");
                current = -1;
                repaint();
            }

            public void mouseReleased(MouseEvent event) {
                //System.out.println(".mouseReleased() = -1");
                current = -1;
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {

            public void mouseDragged(MouseEvent event) {
                if (current == -1) {
                    return;
                }
                points[current] = event.getPoint();
                //System.out.println(".mouseDragged() " + points[current] + ", current=" + current);
                repaint();
            }
        });
        current = -1;
    }

    public void setShape() {
        //System.out.println("chapetest.ShapeComponent.setShapeMaker()");
        points = new Point2D[6];
        points[0] = new Point2D.Double(350, 50);
        points[1] = new Point2D.Double(400, 100);
        points[2] = new Point2D.Double(350, 350);
        points[3] = new Point2D.Double(100, 350);
        points[4] = new Point2D.Double(50, 100);
        points[5] = new Point2D.Double(100, 50);

        //shapeMaker = makeShape(points);
        GeneralPath polygon = new GeneralPath();
        polygon.moveTo((float) points[0].getX(), (float) points[0].getY());
        for (int i = 1; i < points.length; i++) {
            polygon.lineTo((float) points[i].getX(), (float) points[i].getY());
        }
        polygon.closePath();
        shapeMaker = polygon;

        repaint();
    }

    public void paintComponent(Graphics g) {
        if (points == null) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g;
        //System.out.println("frames.swing.draw.Canvas2D.paintComponent() " + points[2]);

        //Квадратик вокруг вершины
        if (current != -1) {
            for (int i = 0; i < points.length; i++) {
                double x = points[i].getX() - SIZE / 2;
                double y = points[i].getY() - SIZE / 2;
                g2.draw(new Rectangle2D.Double(x, y, SIZE, SIZE));
            }
        }

        //Многоугольник
        GeneralPath poligon = new GeneralPath();
        poligon.moveTo(points[0].getX(), points[0].getY());
        for (int i = 1; i < points.length; i++) {
            poligon.lineTo(points[i].getX(), points[i].getY());
        }
        poligon.closePath();
        g2.draw(poligon);

        //Линия
        Point2D lines[] = {new Point2D.Double(10.0, 310.0), new Point2D.Double(480, 400)};
        Point2D line2[] = {new Point2D.Double(200.0, 10.0), new Point2D.Double(480, 400)};

        lines = UAlgo.cut(points, lines, points.length);
        g2.draw(new Line2D.Double(lines[0].getX(), lines[0].getY(), lines[1].getX(), lines[1].getY()));

        line2 = UAlgo.cut(points, line2, points.length);
        g2.draw(new Line2D.Double(line2[0].getX(), line2[0].getY(), line2[1].getX(), line2[1].getY()));
    }
}
