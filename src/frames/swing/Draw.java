package frames.swing;

import builder.Wincalc;
import java.awt.BasicStroke;
import java.awt.Color;

public class Draw {

    public Wincalc iwin = null; //главный класс калькуляции

    public Draw(Wincalc iwin) {
        this.iwin = iwin;
    }

    public void drawLine(float x1, float y1, float x2, float y2) {
        iwin.gc2d.drawLine((int) (x1), (int) (y1), (int) (x2), (int) (y2));
    }

    public void strokePolygon(float x1, float x2, float x3, float x4, float y1,
            float y2, float y3, float y4, int rgbFill, Color rgbStroke) {

        if(rgbStroke == Color.RED) {
           iwin.gc2d.setStroke(new BasicStroke(6)); //толщина линии 
        } else {
           iwin.gc2d.setStroke(new BasicStroke(1)); //толщина линии 
        }
        float dy = iwin.heightAdd - iwin.height;   
        iwin.gc2d.setColor(new java.awt.Color(rgbFill));
        iwin.gc2d.fillPolygon(new int[]{(int) x1, (int) x2, (int) x3, (int) x4},
                new int[]{(int) (y1 + dy), (int) (y2 + dy), (int) (y3 + dy), (int) (y4 + dy)}, 4);
        iwin.gc2d.setColor(rgbStroke);
        iwin.gc2d.drawPolygon(new int[]{(int) (x1), (int) (x2), (int) (x3), (int) (x4)},
                new int[]{(int) (y1 + dy), (int) (y2+ dy), (int) (y3 + dy), (int) (y4 + dy)}, 4);        
    }

    public void strokeArc(double x, double y, double w, double h, double startAngle,
            double arcExtent, int rgbStroke, double lineWidth) {
        int sw = (int) (lineWidth);
        iwin.gc2d.setStroke(new BasicStroke(sw)); //толщина линии
        iwin.gc2d.setColor(new java.awt.Color(rgbStroke));
        iwin.gc2d.drawArc((int) (x), (int) (y), (int) (w), (int) (h), (int) startAngle, (int) arcExtent);
    }
}
