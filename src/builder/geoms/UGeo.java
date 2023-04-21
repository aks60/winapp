package builder.geoms;

import builder.IElem5e;
import enums.Layout;
import enums.Type;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//
//https://www.geeksforgeeks.org/line-clipping-set-2-cyrus-beck-algorithm/
//
public class UGeo {

    public static GeneralPath clipping(Graphics2D gc2D, Point2D p1, Point2D p2, boolean side) {

        GeneralPath clipPath = new GeneralPath();
        Rectangle2D r = gc2D.getClipBounds().getBounds2D();
        List<Point2D> p3 = null;
        if (p1.getX() == p2.getX()) {
            p3 = List.of(new Point2D.Double(r.getX(), r.getY()), new Point2D.Double(p2.getX(), r.getY()),
                    new Point2D.Double(p2.getX(), r.getHeight()), new Point2D.Double(r.getX(), r.getHeight()));

        } else if (p1.getY() == p2.getY()) {
            p3 = List.of(new Point2D.Double(r.getX(), r.getY()), new Point2D.Double(r.getWidth(), r.getY()),
                    new Point2D.Double(r.getWidth(), p2.getY()), new Point2D.Double(r.getX(), p2.getY()));

        } else {
            Point2D px1[] = {new Point2D.Double(r.getX(), r.getY()), new Point2D.Double(r.getWidth(), r.getY()), p1, p2};
            Point2D px2[] = {new Point2D.Double(r.getX(), r.getHeight()), new Point2D.Double(r.getWidth(), r.getHeight()), p1, p2};
            Point2D py1[] = {new Point2D.Double(r.getX(), r.getY()), new Point2D.Double(r.getX(), r.getHeight()), p1, p2};
            Point2D py2[] = {new Point2D.Double(r.getWidth(), r.getY()), new Point2D.Double(r.getWidth(), r.getHeight()), p1, p2};

            double cx1[] = UGeo.cross2(px1[0], px1[1], px1[2], px1[3]);
            double cx2[] = UGeo.cross2(px2[0], px2[1], px2[3], px2[2]);
            double cy1[] = UGeo.cross2(py1[0], py1[1], py1[2], py1[3]);
            double cy2[] = UGeo.cross2(py2[0], py2[1], py2[3], py2[2]);

            if (cx1[0] > 0) {
                p3 = List.of(new Point2D.Double(cy1[0], cy1[1]), new Point2D.Double(cy2[0], cy2[1]), new Point2D.Double(0, cy2[1]));
            } else {
                p3 = List.of(new Point2D.Double(cx1[0], cx1[1]), new Point2D.Double(cx2[0], cx2[1]), new Point2D.Double(cx1[0], cx2[1]));
            }
        }

        clipPath.moveTo(p3.get(0).getX(), p3.get(0).getY());
        for (int i = 1; i < p3.size(); i++) {
            clipPath.lineTo(p3.get(i).getX(), p3.get(i).getY());
        }
        clipPath.closePath();

        if (side == false) {
            Area sceneArea = new Area(r);
            Area clipArea = new Area(clipPath);
            sceneArea.subtract(clipArea);
            return generalPath(sceneArea);  
        }
        return clipPath;
    }

    public static GeneralPath generalPath(Area shape) {
        final double[] dbl = new double[6];
        List<Point2D> p = new ArrayList();
        PathIterator iterator = shape.getPathIterator(null);
        while (!iterator.isDone()) {
            final int segmentType = iterator.currentSegment(dbl);
            if (segmentType == PathIterator.SEG_LINETO) {
                p.add(new Point2D.Double(dbl[0], dbl[1]));
            } else if (segmentType == PathIterator.SEG_MOVETO) {
                p.add(new Point2D.Double(dbl[0], dbl[1]));
            } else if (segmentType == PathIterator.SEG_CLOSE) {
                p.add(new Point2D.Double(dbl[0], dbl[1]));
            }
            iterator.next();
        }
        GeneralPath genPath = new GeneralPath();
        genPath.moveTo(p.get(0).getX(), p.get(0).getY());
        for (int i = 1; i < p.size(); ++i) {
            genPath.lineTo(p.get(i).getX(), p.get(i).getY());
        }
        genPath.closePath();
        return genPath;
    }

    //Точка пересечения двух векторов 
    public static double[] cross2(Point2D A, Point2D B, Point2D C, Point2D D) {

        // Line AB represented as a1x + b1y = c1
        double a1 = B.getY() - A.getY();
        double b1 = A.getX() - B.getX();
        double c1 = a1 * (A.getX()) + b1 * (A.getY());

        // Line CD represented as a2x + b2y = c2
        double a2 = D.getY() - C.getY();
        double b2 = C.getX() - D.getX();
        double c2 = a2 * (C.getX()) + b2 * (C.getY());

        double determinant = a1 * b2 - a2 * b1;

        if (determinant == 0) {
            // The lines are parallel. This is simplified
            // by returning a pair of FLT_MAX
            return new double[]{Integer.MAX_VALUE, Integer.MAX_VALUE};
        } else {
            double x = (b2 * c1 - b1 * c2) / determinant;
            double y = (a1 * c2 - a2 * c1) / determinant;
            return new double[]{x, y};
        }
    }

    public static double sin(double angl) {
        return Math.sin(Math.toRadians(angl));
    }

    public static double asin(double angl) {
        return Math.toDegrees(Math.asin(angl));
    }

    public static double cos(double angl) {
        return Math.cos(Math.toRadians(angl));
    }

    public static double tan(double angl) {
        return Math.tan(Math.toRadians(angl));
    }

    public static double acos(double angl) {
        return Math.toDegrees(Math.acos(angl));
    }

    public static double atan(double angl) {
        return Math.toDegrees(Math.atan(angl));
    }

    //http://ru.solverbook.com/spravochnik/vektory/ugol-mezhdu-vektorami/
    public static double betweenAngl(IElem5e e1, IElem5e e2) {

        double dx1 = e1.x2() - e1.x1();
        double dy1 = e1.y2() - e1.y1();
        double dx2 = e2.x2() - e2.x1();
        double dy2 = e2.y2() - e2.y1();

        double s = dx1 * dx2 + dy1 * dy2;
        double a = Math.sqrt(Math.pow(dx1, 2) + Math.pow(dy1, 2));
        double b = Math.sqrt(Math.pow(dx2, 2) + Math.pow(dy2, 2));
        double c = s / (a * b);
        return 180 - acos(c);
    }

    //https://www.onemathematicalcat.org/Math/Precalculus_obj/horizVertToDirMag.htm
    public static double horizontAngl(IElem5e e) {
        double x = e.x2() - e.x1();
        double y = e.y1() - e.y2();

        if (x > 0 && y == 0) {
            return 0;
        } else if (x < 0 && y == 0) {
            return 180;
        } else if (x == 0 && y > 0) {
            return 90;
        } else if (x == 0 & y < 0) {
            return 270;
        } else if (x > 0 && y > 0) {
            return atan(y / x);
        } else if (x < 0 && y > 0) {
            return 180 + atan(y / x);
        } else if (x < 0 && y < 0) {
            return 180 + atan(y / x);
        } else if (x > 0 && y < 0) {
            return 360 + atan(y / x);
        } else {
            return 0;
        }
    }

    //https://www.onemathematicalcat.org/Math/Precalculus_obj/horizVertToDirMag.htm
    public static double horizontAngl(double x1, double y1, double x2, double y2) {
        double x = x2 - x1;
        double y = y1 - y2;

        if (x > 0 && y == 0) {
            return 0;
        } else if (x < 0 && y == 0) {
            return 180;
        } else if (x == 0 && y > 0) {
            return 90;
        } else if (x == 0 & y < 0) {
            return 270;
        } else if (x > 0 && y > 0) {
            return atan(y / x);
        } else if (x < 0 && y > 0) {
            return 180 + atan(y / x);
        } else if (x < 0 && y < 0) {
            return 180 + atan(y / x);
        } else if (x > 0 && y < 0) {
            return 360 + atan(y / x);
        } else {
            return 0;
        }
    }

// <editor-fold defaultstate="collapsed" desc="XLAM">
    //Скалярное произведение
    public static int dot(Point2D p0, Point2D p1) {
        return (int) (p0.getX() * p1.getX() + p0.getY() * p1.getY());
    }

    //Вычисления максимума из вектора с плавающей запятой
    private static double max(List<Double> t) {
        double maximum = 0;
        for (int i = 0; i < t.size(); i++) {
            if (t.get(i) > maximum) {
                maximum = t.get(i);
            }
        }
        return maximum;
    }

    //Вычисления минимума из вектора с плавающей запятой
    private static double min(List<Double> t) {
        double minimum = 500;
        for (int i = 0; i < t.size(); i++) {
            if (t.get(i) < minimum) {
                minimum = t.get(i);
            }
        }
        return minimum;
    }

    //Функция Сайруса Бека возвращает пару значений, которые затем 
    //отображаются в виде вершины многоугольника
    public static Point2D[] cut(Point2D vertices[], Point2D line[], int n) {

        Point2D newPair[] = {new Point2D.Double(), new Point2D.Double()}; //значение временного держателя, которое будет возвращено        
        Point2D normal[] = new Point2D.Double[n]; //нормали инициализируются динамически (можно и статически, не имеет значения)

        //Расчет нормалей
        for (int i = 0; i < n; i++) {
            double y = vertices[(i + 1) % n].getX() - vertices[i].getX();
            double x = vertices[i].getY() - vertices[(i + 1) % n].getY();
            normal[i] = new Point2D.Double();
            normal[i].setLocation(x, y);
        }

        Point2D P1_P0 = new Point2D.Double((int) (line[1].getX() - line[0].getX()), (int) (line[1].getY() - line[0].getY())); //расчет P1 - P0       
        Point2D[] P0_PEi = new Point2D.Double[n];  //инициализация всех значений P0 - PEi

        //Вычисление значений P0 - PEi для всех ребер
        for (int i = 0; i < n; i++) {
            //Вычисление PEi - P0, чтобы знаменатель не умножался на -1
            double P0_PEx = vertices[i].getX() - line[0].getX();
            double P0_PEy = vertices[i].getY() - line[0].getY();
            P0_PEi[i] = new Point2D.Double();
            P0_PEi[i].setLocation(P0_PEx, P0_PEy); //при расчете t 
        }

        int numerator[] = new int[n], denominator[] = new int[n];
        //Вычисление числителя и знаменателя с помощью точечной функции
        for (int i = 0; i < n; i++) {
            numerator[i] = dot(normal[i], P0_PEi[i]);
            denominator[i] = dot(normal[i], P1_P0);
        }

        double t[] = new double[n]; //динамическая инициализация значений t
        //Создание двух векторов, называемых «не входящими» и 
        //«не выходящими», для группировки «t» в соответствии с их знаменателями
        LinkedList<Double> tE = new LinkedList(), tL = new LinkedList();

        // Вычисление 't' и их группировка соответственно
        for (int i = 0; i < n; i++) {
            t[i] = (double) (numerator[i]) / (double) (denominator[i]);
            if (denominator[i] > 0) {
                tE.add(t[i]);
            } else {
                tL.add(t[i]);
            }
        }
        double temp[] = new double[2]; //инициализация последних двух значений 't'      
        tE.add(0.0);  //берем максимум всех «tE» и 0, поэтому нажимаем 0
        temp[0] = max(tE);
        tL.add(1.0); //принимая минимальное значение всех «tL» и 1, поэтому нажмите 1
        temp[1] = min(tL);

        //Ввод значения 't' не может быть больше, чем выход значения 't', 
        //следовательно, это тот случай, когда линия полностью выходит за пределы
        if (temp[0] > temp[1]) {
            newPair[0] = new Point2D.Double(-1, -1);
            newPair[1] = new Point2D.Double(-1, -1);
            return newPair;
        }

        //Вычисление координат по x и y
        double newPair0x = line[0].getX() + P1_P0.getX() * temp[0];
        double newPair0y = line[0].getY() + P1_P0.getY() * temp[0];
        double newPair1x = line[0].getX() + P1_P0.getX() * temp[1];
        double newPair1y = line[0].getY() + P1_P0.getY() * temp[1];
        newPair[0].setLocation(newPair0x, newPair0y);
        newPair[1].setLocation(newPair1x, newPair1y);

        //System.out.println("(" + newPair[0].getX() + ", " + newPair[0].getY() + ") (" + newPair[1].getX() + ", " + newPair[1].getY() + ")");
        return newPair;
    }

    //Точка пересечения двух векторов 
    //https://habr.com/ru/articles/523440/ 
    public static double[] cross(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        double n;
        double dot[] = {0, 0};
        if (y2 - y1 != 0) {  // a(y)
            double q = (x2 - x1) / (y1 - y2);
            double sn = (x3 - x4) + (y3 - y4) * q;
            if (sn == 0) {
                return null;
            }  // c(x) + c(y)*q

            double fn = (x3 - x1) + (y3 - y1) * q;   // b(x) + b(y)*q
            n = fn / sn;
        } else {
            if ((y3 - y4) == 0) {
                return null;
            }  // b(y)

            n = (y3 - y1) / (y3 - y4);   // c(y)/b(y)
        }
        dot[0] = x3 + (x4 - x3) * n;  // x3 + (-b(x))*n
        dot[1] = y3 + (y4 - y3) * n;  // y3 +(-b(y))*n
        return dot;
    }

    //Точка пересечения двух векторов 
    public static double[] cross(IElem5e e1, IElem5e e2) {
        return cross(e1.x1(), e1.y1(), e1.x2(), e1.y2(), e2.x1(), e2.y1(), e2.x2(), e2.y2());
    }

    //Точка пересечения двух векторов 
    public static Point cross(Point A, Point B, Point C, Point D) {

        // Line AB represented as a1x + b1y = c1
        double a1 = B.y - A.y;
        double b1 = A.x - B.x;
        double c1 = a1 * (A.x) + b1 * (A.y);

        // Line CD represented as a2x + b2y = c2
        double a2 = D.y - C.y;
        double b2 = C.x - D.x;
        double c2 = a2 * (C.x) + b2 * (C.y);

        double determinant = a1 * b2 - a2 * b1;

        if (determinant == 0) {
            // The lines are parallel. This is simplified
            // by returning a pair of FLT_MAX
            return new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
        } else {
            double x = (b2 * c1 - b1 * c2) / determinant;
            double y = (a1 * c2 - a2 * c1) / determinant;
            return new Point((int) x, (int) y);
        }
    }

    //Ширина рамки по оси x и y
    public static double[] diff(IElem5e e, double dh) {

        double x = -1 * cos(e.anglHoriz());
        double y = -1 * sin(e.anglHoriz());

        if (Math.abs(x) >= Math.abs(y)) {
            return new double[]{0, dh / x};
        } else {
            return new double[]{dh / y, 0};
        }
    }

    public static double[] diff(Area shape, IElem5e e, double dh) {
        boolean imp = false;
        if (e.type() == Type.IMPOST || e.type() == Type.STOIKA || e.type() == Type.RIGEL_IMP) {
            if (e.layout() == Layout.VERT && (shape.getBounds2D().getX() == e.x1() || shape.getBounds2D().getX() == e.x2())) {
                imp = true;
            }
            if (e.layout() == Layout.HORIZ && (shape.getBounds2D().getY() == e.y1() || shape.getBounds2D().getY() == e.y2())) {
                imp = true;
            }
        }
        double x = cos(e.anglHoriz());
        double y = sin(e.anglHoriz());

        if (Math.abs(x) >= Math.abs(y)) {
            return (imp) ? new double[]{0, dh / x} : new double[]{0, -dh / x};
        } else {
            return (imp) ? new double[]{dh / y, 0} : new double[]{-dh / y, 0};
        }
    }

    public static Point get_line_intersection(Line2D.Double pLine1, Line2D.Double pLine2) {
        Point result = null;

        double s1_x = pLine1.x2 - pLine1.x1,
                s1_y = pLine1.y2 - pLine1.y1,
                s2_x = pLine2.x2 - pLine2.x1,
                s2_y = pLine2.y2 - pLine2.y1,
                s = (-s1_y * (pLine1.x1 - pLine2.x1) + s1_x * (pLine1.y1 - pLine2.y1)) / (-s2_x * s1_y + s1_x * s2_y),
                t = (s2_x * (pLine1.y1 - pLine2.y1) - s2_y * (pLine1.x1 - pLine2.x1)) / (-s2_x * s1_y + s1_x * s2_y);

        if (s >= 0 && s <= 1 && t >= 0 && t <= 1) {
            // Collision detected
            result = new Point(
                    (int) (pLine1.x1 + (t * s1_x)),
                    (int) (pLine1.y1 + (t * s1_y)));
        }   // end if

        return result;
    }

    //Пример PathIterator
    public static void testArea() {
        Area a = new Area(new Rectangle(1, 1, 5, 5));
        PathIterator iterator = a.getPathIterator(null);
        float[] floats = new float[6];
        Polygon polygon = new Polygon();
        while (!iterator.isDone()) {
            int type = iterator.currentSegment(floats);
            int x = (int) floats[0];
            int y = (int) floats[1];
            if (type != PathIterator.SEG_CLOSE) {
                polygon.addPoint(x, y);
                System.out.println("adding x = " + x + ", y = " + y);
            }
            iterator.next();
        }
    }

//public static List<Point> jarvis(List<Point> points) {
//    Point p0 = new Point(points.get(0));
//    for (Point p : points)
//        if (p.x < p0.x || (p.x == p0.x && p.y < p0.y))
//        p0 = p;
//    List<Point> hull = List.of(p0);
//    do () {
//        Point t = new Point(p0); //кандидат на следующую точку
//        for (Point p : points)
//            // лучше никакие полярные углы не считать
//            if ((p - p0) ^ (t - p0) > 0)
//                t = p;
//        if (t == p0)
//            continue;
//        else {
//            p0 = t;
//            hull.add(t);
//        }
//    } while(t != p);
//    return hull;
//}  
// </editor-fold>    
}
