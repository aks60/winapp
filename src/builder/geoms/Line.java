/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package builder.geoms;

import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

//См. Java.2014-Том 2. Расш.средства прогр
/**
 * Makes a line that joins two given points.
 */
class Line extends TShape {
    public Line() {
        super(2);
    }

    public Shape makeShape(Point2D[] p) {
        return new Line2D.Double(p[0], p[1]);
    }
}
