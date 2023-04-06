/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package builder.geoms;

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
 * См, Java,2014-Том 2, Расш средства прогр 
 * This component draws a shape and allows
 * the user to move the points that define it.
 */
class TPaintComp extends JComponent {

    public TPaintComp() {
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

    /**
     * Set a shape maker and initialize it with a random point set.
     *
     * @param aShapeMaker a shape maker that defines a shape from a point set
     */
    public void setShapeMaker(TShape aShapeMaker) {
        System.out.println("chapetest.ShapeComponent.setShapeMaker()");
        shapeMaker = aShapeMaker;
        int n = shapeMaker.getPointCount();
        points = new Point2D[n];
        if (aShapeMaker instanceof Polygon) {
            points[0] = new Point2D.Double(100, 50);
            points[1] = new Point2D.Double(400, 50);
            points[2] = new Point2D.Double(450, 100);
            points[3] = new Point2D.Double(450, 450);
            points[4] = new Point2D.Double(50, 450);
            points[5] = new Point2D.Double(50, 100);
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
            //g2.fill(new Rectangle2D.Double(x, y, SIZE, SIZE));
            //Ellipse2D s = new Ellipse2D.Double();
            //s.setFrameFromDiagonal(x, y, x + 18, y + 18);
            //g2.draw(s);
        }
        //g2.clip(new Line2D.Double(10.0, 10.0, 490.0, 490.0));
        Shape s = shapeMaker.makeShape(points);
        g2.draw(s);
        g2.clip(s);

        g2.draw(new Line2D.Double(10.0, 10.0, 500.0, 500.0));
    }

    private Point2D[] points;
    private static Random generator = new Random();
    private static int SIZE = 10;
    private int current;
    private TShape shapeMaker;
}
