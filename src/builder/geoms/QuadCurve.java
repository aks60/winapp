/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package builder.geoms;

import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;

//См. Java.2014-Том 2. Расш.средства прогр
/**
 * Makes a quad curve defined by two end points and a control point.
 */
class QuadCurve extends TShape {
    public QuadCurve() {
        super(3);
    }

    public Shape makeShape(Point2D[] p) {
        return new QuadCurve2D.Double(p[0].getX(), p[0].getY(), p[1].getX(), p[1].getY(), p[2].getX(), p[2].getY());
    }
}
