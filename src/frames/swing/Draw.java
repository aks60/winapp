package frames.swing;

import builder.Wincalc;
import java.awt.BasicStroke;
import java.awt.Color;

public class Draw {

    public Wincalc iwin = null; //главный класс калькуляции

    public Draw(Wincalc iwin) {
        this.iwin = iwin;
    }
    
    public void setStroke(int s) {
        int sw = (int) (s + 1 - iwin.scale2 * .37);
        iwin.gc2d.setStroke(new BasicStroke(sw));
    }

    public void drawLine(float x1, float y1, float x2, float y2) {
        iwin.gc2d.drawLine((int) (x1 / iwin.scale2), (int) (y1 / iwin.scale2), (int) (x2 / iwin.scale2), (int) (y2 / iwin.scale2));
    }

    public void rotate(double theta, double x, double y) {
        iwin.gc2d.rotate(theta, x / iwin.scale2, y / iwin.scale2);
    }

    public void drawString(String str, float x, float y) {
        iwin.gc2d.drawString(str, x / iwin.scale2, y / iwin.scale2);
    }

    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        for (int i = 0; i < xPoints.length; i++) {
            xPoints[i] = (int) (xPoints[i] / iwin.scale2);
        }
        for (int i = 0; i < yPoints.length; i++) {
            yPoints[i] = (int) (yPoints[i] / iwin.scale2);
        }
        iwin.gc2d.drawPolygon(xPoints, yPoints, nPoints);
    }

    public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        for (int i = 0; i < xPoints.length; i++) {
            xPoints[i] = (int) (xPoints[i] / iwin.scale2);
        }
        for (int i = 0; i < yPoints.length; i++) {
            yPoints[i] = (int) (yPoints[i] / iwin.scale2);
        }
        iwin.gc2d.fillPolygon(xPoints, yPoints, nPoints);
    }

    public void drawArc(float x, float y, float width, float height, float startAngle, float arcAngle) {
        iwin.gc2d.drawArc((int) (x / iwin.scale2), (int) (y / iwin.scale2), (int) (width / iwin.scale2), (int) (height / iwin.scale2), (int) (startAngle / iwin.scale2), (int) (arcAngle / iwin.scale2));
    }

    public void fillArc(float x, float y, float width, float height, float startAngle, float arcAngle) {
        iwin.gc2d.fillArc((int) (x / iwin.scale2), (int) (y / iwin.scale2), (int) (width / iwin.scale2), (int) (height / iwin.scale2), (int) startAngle, (int) arcAngle);;
    }

    public void strokePolygon(float x1, float x2, float x3, float x4, float y1,
            float y2, float y3, float y4, int rgbFill, Color rgbStroke) {
        setStroke(8); //толщина линии
        iwin.gc2d.setColor(rgbStroke);
        float sc = iwin.scale2;
        float dy = (iwin.heightAdd - iwin.height) / sc;
        iwin.gc2d.drawPolygon(new int[]{(int) (x1 / sc), (int) (x2 / sc), (int) (x3 / sc), (int) (x4 / sc)},
                new int[]{(int) (y1 / sc + dy), (int) (y2 / sc + dy), (int) (y3 / sc + dy), (int) (y4 / sc + dy)}, 4);
        iwin.gc2d.setColor(new java.awt.Color(rgbFill));
        iwin.gc2d.fillPolygon(new int[]{(int) (x1 / sc), (int) (x2 / sc), (int) (x3 / sc), (int) (x4 / sc)},
                new int[]{(int) (y1 / sc + dy), (int) (y2 / sc + dy), (int) (y3 / sc + dy), (int) (y4 / sc + dy)}, 4);
    }

    public void strokeArc(double x, double y, double w, double h, double startAngle,
            double arcExtent, int rgbStroke, double lineWidth) {
        float sc = iwin.scale2;
        int sw = (int) (lineWidth / iwin.scale2);
        iwin.gc2d.setStroke(new BasicStroke(sw)); //толщина линии
        iwin.gc2d.setColor(new java.awt.Color(rgbStroke));
        iwin.gc2d.drawArc((int) (x / sc), (int) (y / sc), (int) (w / sc), (int) (h / sc), (int) startAngle, (int) arcExtent);
    }
}
