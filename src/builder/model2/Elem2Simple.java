package builder.model2;

import builder.Wingeo;
import builder.making.Specific;
import builder.script.GeoElem;
import domain.eArtikl;
import java.awt.Color;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Elem2Simple extends Comp {

    public double anglCut[] = {45, 45}; //угол реза
    public double[] anglFlat = {0, 0, 0, 0}; //мин/мах внутренний и мин/мах внешний угол к плоскости
    public double anglHoriz = 0; //угол к горизонту    
    public double[] betweenHoriz = {0, 0}; //угол между векторами    

    public Specific spcRec = null; //спецификация элемента
    public Color borderColor = Color.BLACK;

    public Elem2Simple(Wingeo wing, GeoElem gson, Comp owner) {
        super(wing, gson, owner);
        //spcRec = new Specific(id, this);
    }

    public void setLocation() {
//        try {
//            GeneralPath p = new GeneralPath();
//            p.reset();
//            p.moveTo(wing.listFrame.get(0).x1(), wing.listFrame.get(0).y1());
//            for (int i = 1; i < wing.listFrame.size(); ++i) {
//                p.lineTo(wing.listFrame.get(i).x1(), wing.listFrame.get(i).y1());
//            }
//            p.closePath();
//            area = new Area(p);
//
//        } catch (Exception e) {
//            System.err.println("Ошибка:Elem2Simple.build()" + toString() + e);
//        }        
    }

    public Elem2Simple[] prevAndNext(Area area) {
        //UGeo.printPoligon(area);
        Elem2Simple[] ret = {null, null};
        try {
            double[] v = new double[6];
            List<Point2D> list = new ArrayList(List.of(new Point2D.Double(-1, -1)));
            PathIterator iterator = area.getPathIterator(null);

            while (!iterator.isDone()) {
                if (iterator.currentSegment(v) != PathIterator.SEG_CLOSE) {
                    //System.out.println("x=" + v[0] + "  y=" + v[1]);

                    //Это Cross элемент
                    Point2D a = list.get(list.size() - 1);
                    if (a.getX() == this.x1() && a.getY() == this.y1() && v[0] == this.x2() && v[1] == this.y2()) {

                        //Это Prev элемент
                        Point2D b = list.get(list.size() - 2);
                        ret[0] = wing.listLine.stream().filter(e -> UGeo.pointOnLine(b.getX(), b.getY(), e)
                                && UGeo.pointOnLine(a.getX(), a.getY(), e)).findFirst().get();

                        //Это Next элемент
                        list.add(new Point2D.Double(v[0], v[1]));
                        Point2D c = list.get(list.size() - 1);
                        iterator.next();
                        ret[1] = wing.listLine.stream().filter(e -> UGeo.pointOnLine(c.getX(), c.getY(), e)
                                && UGeo.pointOnLine(v[0], v[1], e)).findFirst().get();

                        break;
                    }
                } else {
                    ret[0] = wing.listLine.stream().filter(e -> UGeo.pointOnLine(list.get(2).getX(), list.get(2).getY(), e)
                            && UGeo.pointOnLine(list.get(3).getX(), list.get(3).getY(), e)).findFirst().orElse(null);
                    ret[1] = wing.listLine.stream().filter(e -> UGeo.pointOnLine(list.get(list.size() - 1).getX(), list.get(list.size() - 1).getY(), e)
                            && UGeo.pointOnLine(list.get(list.size() - 2).getX(), list.get(list.size() - 2).getY(), e)).findFirst().orElse(null);
                }
                list.add(new Point2D.Double(v[0], v[1]));
                iterator.next();
            }
            System.out.println("cross= " + this);
            System.out.println("prev= " + ret[0]);
            System.out.println("next= " + ret[1]);

        } catch (Exception e) {
            System.err.println("Ошибка:Elem2Cross.prevAndNext()" + toString() + e);
        }
        return ret;
    }

    public double[] diffAndCross(Elem2Simple e0, Elem2Simple e1) {
        double h[] = UGeo.diff(this, this.artiklRec.getDbl(eArtikl.height) - this.artiklRec.getDbl(eArtikl.size_centr));
        double h1[] = UGeo.diff(e0, e0.artiklRec.getDbl(eArtikl.height) - e0.artiklRec.getDbl(eArtikl.size_centr));
        double h2[] = UGeo.diff(e1, e1.artiklRec.getDbl(eArtikl.height) - e1.artiklRec.getDbl(eArtikl.size_centr));
        double p1[] = UGeo.cross(x1() + h[0], y1() + h[1], x2() + h[0], y2() + h[1], e0.x1() + h1[0], e0.y1() + h1[1], e0.x2() + h1[0], e0.y2() + h1[1]);
        double p2[] = UGeo.cross(x1() + h[0], y1() + h[1], x2() + h[0], y2() + h[1], e1.x1() + h2[0], e1.y1() + h2[1], e1.x2() + h2[0], e1.y2() + h2[1]);
        this.area = rectangl(x1(), y1(), x2(), y2(), p2[0], p2[1], p1[0], p1[1]);
        return new double[]{p1[0], p1[1], p2[0], p2[1]};
    }

    @Override
    public String toString() {
        return super.toString() + ", anglHoriz=" + anglHoriz;
    }
}
