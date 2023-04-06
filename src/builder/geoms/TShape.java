package builder.geoms;

import java.awt.Shape;
import java.awt.geom.Point2D;

/**
 * См, Java,2014-Том 2, Расш.средства прогр
 * A shape maker can make a shape from a point set. Concrete subclasses must return a shape in the
 * makeShape method.
 */
abstract class TShape {
    
    /**
     * Constructs a shape maker.
     * @param aPointCount the number of points needed to define this shape.
     */
    public TShape(int aPointCount) {
        pointCount = aPointCount;
    }

    /**
     * Gets the number of points needed to define this shape.
     * @return the point count
     */
    public int getPointCount() {
        return pointCount;
    }

    /**
     * Makes a shape out of the given point set.
     * @param p the points that define the shape
     * @return the shape defined by the points
     */
    public abstract Shape makeShape(Point2D[] p);

    public String toString() {
        return getClass().getName();
    }

    private int pointCount;
}