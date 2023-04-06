/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package builder.geoms;

import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;

//См. Java.2014-Том 2. Расш.средства прогр
/**
 * Makes a round rectangle that joins two given corner points.
 */
class RectangleRound extends TShape {
    public RectangleRound() {
        super(2);
    }

    public Shape makeShape(Point2D[] p) {
        RoundRectangle2D s = new RoundRectangle2D.Double(0, 0, 0, 0, 20, 20);
        s.setFrameFromDiagonal(p[0], p[1]);
        return s;
    }
}
