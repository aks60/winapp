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
        iwin.gc2d.setStroke(new BasicStroke(s));
    }

    public void drawLine(float x1, float y1, float x2, float y2) {
        iwin.gc2d.drawLine((int) (x1), (int) (y1), (int) (x2), (int) (y2));
    }

    public void rotate(double theta, double x, double y) {
        iwin.gc2d.rotate(theta, x, y);
    }

    public void drawString(String str, float x, float y) {
        iwin.gc2d.drawString(str, x, y);
    }

    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        for (int i = 0; i < xPoints.length; i++) {
            xPoints[i] = (int) (xPoints[i]);
        }
        for (int i = 0; i < yPoints.length; i++) {
            yPoints[i] = (int) (yPoints[i]);
        }
        iwin.gc2d.drawPolygon(xPoints, yPoints, nPoints);
    }

    public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        for (int i = 0; i < xPoints.length; i++) {
            xPoints[i] = (int) (xPoints[i]);
        }
        for (int i = 0; i < yPoints.length; i++) {
            yPoints[i] = (int) (yPoints[i]);
        }
        iwin.gc2d.fillPolygon(xPoints, yPoints, nPoints);
    }

    public void drawArc(float x, float y, float width, float height, float startAngle, float arcAngle) {
        iwin.gc2d.drawArc((int) (x), (int) (y), (int) (width), (int) (height), (int) (startAngle), (int) (arcAngle));
    }

    public void fillArc(float x, float y, float width, float height, float startAngle, float arcAngle) {
        iwin.gc2d.fillArc((int) (x), (int) (y), (int) (width), (int) (height), (int) startAngle, (int) arcAngle);;
    }

    public void strokePolygon(float x1, float x2, float x3, float x4, float y1,
            float y2, float y3, float y4, int rgbFill, Color rgbStroke) {
        setStroke(8); //толщина линии
        iwin.gc2d.setColor(rgbStroke);
        float dy = (iwin.heightAdd - iwin.height);
        iwin.gc2d.drawPolygon(new int[]{(int) (x1), (int) (x2), (int) (x3), (int) (x4)},
                new int[]{(int) (y1 + dy), (int) (y2+ dy), (int) (y3 + dy), (int) (y4 + dy)}, 4);
        iwin.gc2d.setColor(new java.awt.Color(rgbFill));
        iwin.gc2d.fillPolygon(new int[]{(int) (x1), (int) (x2), (int) (x3), (int) (x4)},
                new int[]{(int) (y1 + dy), (int) (y2 + dy), (int) (y3 + dy), (int) (y4 + dy)}, 4);
    }
    
    public void strokePolygon2(int x1, int x2, int x3, int x4, int y1, int y2, int y3, int y4, Color rgbFill, Color rgbStroke) {
        //setStroke(2); //толщина линии
        
        iwin.gc2d.setColor(rgbStroke);
        iwin.gc2d.drawPolygon(new int[]{x1, x2, x3, x4}, new int[]{y1, y2, y3, y4}, 4);
        
        iwin.gc2d.setColor(rgbFill);
        iwin.gc2d.fillPolygon(new int[]{x1+2, x2-2, x3-2, x4+2},  new int[]{y1+2, y2+2, y3-2, y4-2}, 4);
    }

    public void strokeArc(double x, double y, double w, double h, double startAngle,
            double arcExtent, int rgbStroke, double lineWidth) {
        int sw = (int) (lineWidth);
        iwin.gc2d.setStroke(new BasicStroke(sw)); //толщина линии
        iwin.gc2d.setColor(new java.awt.Color(rgbStroke));
        iwin.gc2d.drawArc((int) (x), (int) (y), (int) (w), (int) (h), (int) startAngle, (int) arcExtent);
    }
}
