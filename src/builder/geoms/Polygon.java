/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package builder.geoms;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

//См. Java.2014-Том 2. Расш.средства прогр
/**
 * Makes a polygon defined by six corner points.
 */
class Polygon extends TShape {

    public Polygon() {
        super(6);
    }

    public Shape makeShape(Point2D[] p) {

        System.out.println("chapetest.PolygonMaker.makeShape()");
        GeneralPath s = new GeneralPath();

        s.moveTo((float) p[0].getX(), (float) p[0].getY());
        for (int i = 1; i < p.length; i++) {
            s.lineTo((float) p[i].getX(), (float) p[i].getY());
        }    
        s.closePath();
        return s;
    }
}
