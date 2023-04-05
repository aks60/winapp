/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package builder.geoms;

import java.awt.Shape;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;

//См. Java.2014-Том 2. Расш.средства прогр
/**
 * Makes a cubic curve defined by two end points and two control points.
 */
class CubicCurve extends TShape {
    public CubicCurve() {
        super(4);
    }

    public Shape makeShape(Point2D[] p) {
        return new CubicCurve2D.Double(p[0].getX(), p[0].getY(), p[1].getX(), p[1].getY(), p[2].getX(), p[2].getY(),
                p[3].getX(), p[3].getY());
    }
}
