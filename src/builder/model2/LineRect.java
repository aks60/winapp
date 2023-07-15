package builder.model2;

import java.awt.geom.*;
import java.util.ArrayList;

public class LineRect {
    public static Line2D.Double intersection(Line2D.Double line, Rectangle2D.Double bounds) {
        //line.x1, line.y1, line.x2, line.y2
        Point2D.Double bottom = computeSegmentIntersection(line.x1, line.y1, line.x2, line.y2, bounds.getMinX(),
                bounds.getMaxY(), bounds.getMaxX(), bounds.getMaxY());

        Point2D.Double right = computeSegmentIntersection(line.x1, line.y1, line.x2, line.y2, bounds.getMaxX(),
                bounds.getMaxY(), bounds.getMaxX(), bounds.getMinY());

        Point2D.Double top = computeSegmentIntersection(line.x1, line.y1, line.x2, line.y2, bounds.getMinX(),
                bounds.getMinY(), bounds.getMaxX(), bounds.getMinY());

        Point2D.Double left = computeSegmentIntersection(line.x1, line.y1, line.x2, line.y2, bounds.getMinX(),
                bounds.getMinY(), bounds.getMinX(), bounds.getMaxY());

        ArrayList<Point2D.Double> pts = new ArrayList<Point2D.Double>();
        if (bottom != null) {
            pts.add(bottom);
        }

        if (right != null) {
            pts.add(right);
        }

        if (top != null) {
            pts.add(top);
        }

        if (left != null) {
            pts.add(left);
        }

        if (pts.size() != 2) {
            return null;
        } else {
            return new Line2D.Double(pts.get(0), pts.get(1));
        }
    }

    private static Point2D.Double computeSegmentIntersection(double x1, double y1, double x2, double y2, double x3,
            double y3, double x4, double y4) {
        /*
        double cp1, cp2, cp3, cp4;
            
        cp1 = crossProduct(x1, y1, x2, y2, x3, y3);
        cp2 = crossProduct(x1, y1, x2, y2, x4, y4);
        cp3 = crossProduct(x3, y3, x4, y4, x1, y1);
        cp4 = crossProduct(x3, y3, x4, y4, x2, y2);
         */

        if (isSegIntersect(x1, y1, x2, y2, x3, y3, x4, y4)) {
            // 1 intersection point !
            return getLineIntersection(x1, y1, x2, y2, x3, y3, x4, y4);
        } else {
            // none or many intersection point !
            return null;
        }
    }

    /**
     * Is (x1y1)(x2y2) (strictly) intersects (x3y3)(x4y4) ?
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     * @param x4
     * @param y4
     * @return
     */
    private static boolean isSegIntersect(double x1, double y1, double x2, double y2, double x3, double y3,
            double x4, double y4) {
        double cp1, cp2, cp3, cp4;

        cp1 = crossProduct(x1, y1, x2, y2, x3, y3);
        cp2 = crossProduct(x1, y1, x2, y2, x4, y4);
        cp3 = crossProduct(x3, y3, x4, y4, x1, y1);
        cp4 = crossProduct(x3, y3, x4, y4, x2, y2);

        return (cp1 * cp2 < 0 && cp3 * cp4 < 0);
    }

    /**
     * Compute intersection between two line.
     * The first line is passing by points (x1,y1) & (x2, y2). The second by (x3,y3) and (x4,y4)
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     * @param x4
     * @param y4
     * @return null if lines are parallel, the intersection point otherwise
     */
    private static Point2D.Double getLineIntersection(double x1, double y1, double x2, double y2, double x3,
            double y3, double x4, double y4) {

        double denom1 = x2 - x1;
        double denom2 = x4 - x3;

        if (Math.abs(denom1) < 0.0001) {
            denom1 = 0;
        }
        if (Math.abs(denom2) < 0.0001) {
            denom2 = 0;
        }

        double a1 = (y2 - y1) / denom1;
        double a2 = (y4 - y3) / denom2;

        double b1 = y2 - a1 * x2;
        double b2 = y4 - a2 * x4;

        double x;
        double y;

        if (Double.isInfinite(a1) && Double.isInfinite(a2)) {
            return null;
        } else if (Double.isInfinite(a1)) {
            x = x1;
            y = a2 * x + b2;
        } else if (Double.isInfinite(a2)) {
            x = x3;
            y = a1 * x + b1;
        } else {
            x = (b2 - b1) / (a1 - a2);
            y = a1 * x + b1;
            if (Double.isNaN(x) || Double.isInfinite(x)) {
                return null;
            }
        }
        return new Point2D.Double(x, y);
    }

    /**
     * Compute cross product :
     *
     *           o(x2,y2)
     *          /
     * cp > 0  /
     *        /    cp < 0
     *       /
     *      /
     *     o (x1, y1)
     *
     * @param x1 seg first point x coord
     * @param y1 seg first point y coord
     * @param x2 seg second point x coord
     * @param y2 seg second point y coord
     * @param x3 the point to check x coord
     * @param y3 the point to check y coord
     *
     */
    static double crossProduct(double x1, double y1, double x2, double y2, double x3, double y3) {
        return (x2 - x1) * (y3 - y1) - (x3 - x1) * (y2 - y1);
    }
}

