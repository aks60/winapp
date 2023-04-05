package builder.geoms;

import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

//См. Java.2014-Том 2. Расш.средства прогр
/**
 * Makes an arc contained in a bounding box with two given corner points, and with starting and
 * ending angles given by lines emanating from the center of the bounding box and ending in two
 * given points. To show the correctness of the angle computation, the returned shape contains the
 * arc, the bounding box, and the lines.
 */
class Arc extends TShape {
    public Arc() {
        super(4);
    }

    public Shape makeShape(Point2D[] p) {
        double centerX = (p[0].getX() + p[1].getX()) / 2;
        double centerY = (p[0].getY() + p[1].getY()) / 2;
        double width = Math.abs(p[1].getX() - p[0].getX());
        double height = Math.abs(p[1].getY() - p[0].getY());

        double skewedStartAngle = Math
                .toDegrees(Math.atan2(-(p[2].getY() - centerY) * width, (p[2].getX() - centerX) * height));
        double skewedEndAngle = Math
                .toDegrees(Math.atan2(-(p[3].getY() - centerY) * width, (p[3].getX() - centerX) * height));
        double skewedAngleDifference = skewedEndAngle - skewedStartAngle;
        if (skewedStartAngle < 0)
            skewedStartAngle += 360;
        if (skewedAngleDifference < 0)
            skewedAngleDifference += 360;

        Arc2D s = new Arc2D.Double(0, 0, 0, 0, skewedStartAngle, skewedAngleDifference, Arc2D.OPEN);
        s.setFrameFromDiagonal(p[0], p[1]);

        GeneralPath g = new GeneralPath();
        g.append(s, false);
        Rectangle2D r = new Rectangle2D.Double();
        r.setFrameFromDiagonal(p[0], p[1]);
        g.append(r, false);
        Point2D center = new Point2D.Double(centerX, centerY);
        g.append(new Line2D.Double(center, p[2]), false);
        g.append(new Line2D.Double(center, p[3]), false);
        return g;
    }
}
