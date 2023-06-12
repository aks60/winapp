package builder.model2;

import builder.Wingeo;
import builder.script.GeoElem;
import com.google.gson.JsonObject;
import domain.eArtikl;
import domain.eSysprof;
import enums.PKjson;
import enums.UseSide;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Elem2Cross extends Elem2Simple {

    public Elem2Cross(Wingeo wing, GeoElem gson, Com6s owner) {
        super(wing, gson, owner);
        initСonstructiv(gson.param);
        mouseEvent();
    }

    public void initСonstructiv(JsonObject param) {

        colorID1 = (isJson(param, PKjson.colorID1)) ? param.get(PKjson.colorID1).getAsInt() : wing.colorID1;
        colorID2 = (isJson(param, PKjson.colorID2)) ? param.get(PKjson.colorID2).getAsInt() : wing.colorID2;
        colorID3 = (isJson(param, PKjson.colorID3)) ? param.get(PKjson.colorID3).getAsInt() : wing.colorID3;

        double angl = UGeo.horizontAngl(this);
        if (isJson(param, PKjson.sysprofID)) { //профили через параметр
            sysprofRec = eSysprof.find3(param.get(PKjson.sysprofID).getAsInt());
        } else {
            if (angl == 0 || angl == 180) { //сверху вниз
                sysprofRec = eSysprof.find5(wing.nuni, type.id2, UseSide.HORIZ, UseSide.ANY);

            } else if (angl == 90 || angl == 270) { //слева направо
                sysprofRec = eSysprof.find5(wing.nuni, type.id2, UseSide.VERT, UseSide.ANY);

            } else {
                sysprofRec = eSysprof.find4(wing.nuni, type.id2, UseSide.ANY);
            }
        }
        //spcRec.place = (angl == 0) ? Layout.HORIZ.name : Layout.VERT.name;
        artiklRec = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), false);
        artiklRecAn = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), true);
    }

    public void setLocation() {
        try {
            anglHoriz = UGeo.horizontAngl(this);
            double w = owner.area.getBounds2D().getMaxX();
            double h = owner.area.getBounds2D().getMaxY();

            //Точки пересечение импостом Canvas2D    
            double Y1 = 0, Y2 = h;
            double X1 = (this.y1() == this.y2()) ? 0 : (((0 - this.y1()) / (this.y2() - this.y1())) * (this.x2() - this.x1())) + this.x1();
            double X2 = (this.y1() == this.y2()) ? w : (((h - this.y1()) / (this.y2() - this.y1())) * (this.x2() - this.x1())) + this.x1();

            Area areaOwnerLeft = (Area) owner.area.clone();
            Area areaOwnerRigh = (Area) owner.area.clone();
            Area areaLeft = UGeo.area(0, 0, 0, h, X2, Y2, X1, Y1);
            Area areaRigh = UGeo.area(X1, Y1, X2, Y2, w, h, w, 0);
            areaOwnerLeft.intersect(areaLeft);
            areaOwnerRigh.intersect(areaRigh);
            
           //UGeo.PRINT(areaRigh);
           //UGeo.PRINT(areaOwnerRigh);

            //Вектор импоста
            double lineCross[] = UGeo.segmentFromLine(areaOwnerLeft, X1, Y1, X2, Y2);
            if (lineCross != null) {
                this.setDimension(lineCross[0], lineCross[1], lineCross[2], lineCross[3]);
            }

            //Полигон импоста
            Elem2Simple e[] = prevAndNext(areaOwnerLeft);
            double h0[] = UGeo.diff(UGeo.horizontAngl(this), this.artiklRec.getDbl(eArtikl.height) - this.artiklRec.getDbl(eArtikl.size_centr));

            double h1[] = UGeo.diff(UGeo.horizontAngl(e[0]), e[0].artiklRec.getDbl(eArtikl.height) - e[0].artiklRec.getDbl(eArtikl.size_centr));
            double h2[] = UGeo.diff(UGeo.horizontAngl(e[1]), e[1].artiklRec.getDbl(eArtikl.height) - e[1].artiklRec.getDbl(eArtikl.size_centr));
            double p1[] = UGeo.cross(x1() + h0[0], y1() + h0[1], x2() + h0[0], y2() + h0[1], e[0].x1() + h1[0], e[0].y1() + h1[1], e[0].x2() + h1[0], e[0].y2() + h1[1]);
            double p2[] = UGeo.cross(x1() + h0[0], y1() + h0[1], x2() + h0[0], y2() + h0[1], e[1].x1() + h2[0], e[1].y1() + h2[1], e[1].x2() + h2[0], e[1].y2() + h2[1]);

            UGeo.PRINT(areaOwnerRigh);
            
            //Elem2Simple k[] = prevAndNext(areaOwnerRigh);           
//            double h3[] = UGeo.diff(UGeo.horizontAngl(k[0]), k[0].artiklRec.getDbl(eArtikl.height) - k[0].artiklRec.getDbl(eArtikl.size_centr));
//            double h4[] = UGeo.diff(UGeo.horizontAngl(k[1]), k[1].artiklRec.getDbl(eArtikl.height) - k[1].artiklRec.getDbl(eArtikl.size_centr));
//            double p3[] = UGeo.cross(x1() + h0[0], y1() + h0[1], x2() + h0[0], y2() + h0[1], k[0].x1() + h3[0], k[0].y1() + h3[1], k[0].x2() + h3[0], k[0].y2() + h3[1]);
//            double p4[] = UGeo.cross(x1() + h0[0], y1() + h0[1], x2() + h0[0], y2() + h0[1], k[1].x1() + h4[0], k[1].y1() + h4[1], k[1].x2() + h4[0], k[1].y2() + h4[1]);
            this.addDimension(p2[0], p2[1], p1[0], p1[1]);
            //this.area = rectangl(p2[0], p2[1], p1[0], p1[1], p4[0], p4[1], p3[0], p3[1]);
            this.area = rectangl(x1(), y1(), x2(), y2(), p2[0], p2[1], p1[0], p1[1]);

        } catch (Exception e) {
            System.err.println("Ошибка:Elem2Cross.setLocation()" + toString() + e);
        }
    }

    public double[] pointCross(double horizontAngl, Elem2Simple e0, Elem2Simple e1) {
        try {
            System.out.println("-----------------");
            System.out.println(e0);
            System.out.println(e1);
            double h[] = UGeo.diff(horizontAngl, this.artiklRec.getDbl(eArtikl.height) - this.artiklRec.getDbl(eArtikl.size_centr));
            double h1[] = UGeo.diff(UGeo.horizontAngl(e0.x1(), e0.y1(), e0.x2(), e0.y2()), e0.artiklRec.getDbl(eArtikl.height) - e0.artiklRec.getDbl(eArtikl.size_centr));
            double h2[] = UGeo.diff(UGeo.horizontAngl(e1.x1(), e1.y1(), e1.x2(), e1.y2()), e1.artiklRec.getDbl(eArtikl.height) - e1.artiklRec.getDbl(eArtikl.size_centr));
            double p1[] = UGeo.cross(x1() + h[0], y1() + h[1], x2() + h[0], y2() + h[1], e0.x1() + h1[0], e0.y1() + h1[1], e0.x2() + h1[0], e0.y2() + h1[1]);
            double p2[] = UGeo.cross(x1() + h[0], y1() + h[1], x2() + h[0], y2() + h[1], e1.x1() + h2[0], e1.y1() + h2[1], e1.x2() + h2[0], e1.y2() + h2[1]);
            this.area = rectangl(x1(), y1(), x2(), y2(), p2[0], p2[1], p1[0], p1[1]);
            //System.out.println(p1[0] + ":" + p1[1] + ":" + p2[0] + ":" + p2[1]);
            return new double[]{p1[0], p1[1], p2[0], p2[1]};

        } catch (Exception e) {
            System.err.println("Ошибка:Elem2Cross.pointCross() " + e);
            return null;
        }
    }

    public void segmentElem(Area area, double x1, double y1, double x2, double y2) {
        try {
            double[] v = new double[6];
            List<Point2D> list = new ArrayList(List.of(new Point2D.Double(-1, -1)));
            PathIterator iterator = area.getPathIterator(null);
            while (!iterator.isDone()) {
                if (iterator.currentSegment(v) != PathIterator.SEG_CLOSE) {
                    Point2D a = list.get(list.size() - 1);

                    if (UGeo.pointOnLine(a.getX(), a.getY(), x1, y1, x2, y2)
                            && UGeo.pointOnLine(v[0], v[1], x1, y1, x2, y2)) {

                        if ((int) a.getX() != (int) v[0] && (int) a.getY() != (int) v[1]) {
                            //return new double[]{a.getX(), a.getY(), v[0], v[1]};
                        }
                    }
                }
                list.add(new Point2D.Double(v[0], v[1]));
                iterator.next();
            }
        } catch (Exception e) {
            System.err.println("Ошибка:" + toString() + e);
        }
    }

    public void paint() {
        try {
            wing.gc2D.draw(area);
            wing.gc2D.draw(new Line2D.Double(this.x1(), this.y1(), this.x2(), this.y2()));

        } catch (Exception e) {
            System.out.println("Ошибка:Elem2Cross.paint() " + e);
        }
    }
}
