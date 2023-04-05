/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package builder.geoms;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

//См. Java.2014-Том 2. Расш.средства прогр
/**
 * Makes an ellipse contained in a bounding box with two given corner points.
 */
class Ellipse extends TShape {
    public Ellipse() {
        super(2);
    }

    public Shape makeShape(Point2D[] p) {
        Ellipse2D s = new Ellipse2D.Double();
        s.setFrameFromDiagonal(p[0], p[1]);
        return s;
    }
}
