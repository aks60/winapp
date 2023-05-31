package builder.model2;

import builder.Wingeo;
import builder.script.GeoElem;
import com.google.gson.JsonObject;
import domain.eArtikl;
import domain.eSysprof;
import enums.PKjson;
import enums.UseSide;
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
        System.out.println("------------------------------------");
        double w = owner.area.getBounds2D().getWidth();
        double h = owner.area.getBounds2D().getHeight();
        //Точки пересечение импостом Canvas2D
        double X1 = (this.y1() == this.x2()) ? 0 : (((0 - this.y1()) / (this.y2() - this.y1())) * (this.x2() - this.x1())) + this.x1();
        double Y1 = 0; //(this.x1() == this.x2()) ? 0 : (((0 - this.x1()) / (this.x2() - this.x1())) * (this.y2() - this.y1())) + this.y1();
        double X2 = (this.y1() == this.x2()) ? w : (((h - this.y1()) / (this.y2() - this.y1())) * (this.x2() - this.x1())) + this.x1();
        double Y2 = h; //(this.x1() ==  this.x2()) ? h : (((w - this.x1()) / (this.x2() - this.x1())) * (this.y2() - this.y1())) + this.y1();

        Area areaOwner = (Area) owner.area.clone();         
        Rectangle2D rectanglLeft = new Rectangle2D.Double(0, 0, X2, Y2);
        //UGeo.PRINT("imp1", this.x1(), this.y1(), this.x2(), this.y2());
        //UGeo.PRINT("XY", X1, Y1, X2, Y2);
        //UGeo.PRINT("A1", areaOwner);
        areaOwner.intersect(new Area(rectanglLeft));
        //UGeo.PRINT("A2", areaOwner);
        
        double lineCross[] = UGeo.segmentToCross(areaOwner, X2, Y2, X1, Y1);
        if (lineCross != null) {
            this.setDimension(lineCross[0], lineCross[1], lineCross[2], lineCross[3]);
            //UGeo.PRINT("imp2", lineCross[0], lineCross[1], lineCross[2], lineCross[3]);
        }
    }

    public void setLocation2() {
        anglHoriz = UGeo.horizontAngl(this);
//        if(this.x1() > this.x2()) {
//            double x1 = this.x1(), y1 = this.y1();           
//            this.x1(this.x2());
//            this.y1(this.y2());
//            this.x2(x1);
//            this.y1(y1);
//        }
        try {
            //Делим полигон
            if (owner.childs().size() == 3) {
                Area area2[] = UGeo.split(owner.area, owner.childs().get(1));
                owner.childs().get(0).area = area2[0];
                owner.childs().get(2).area = area2[1];
                double line[] = UGeo.cross(area2);
                if (line != null) {
                    this.setDimension(line[0], line[1], line[2], line[3]);
                }
            }
            //Строим полигон
            Elem2Simple elem[] = prevAndNext(owner.childs().get(0).area);

//            double ang1 = UGeo.horizontAngl(this.x1(), this.y1(), this.x2(), this.y2());
//            double h[] = UGeo.diff(ang1, this.artiklRec.getDbl(eArtikl.height) - this.artiklRec.getDbl(eArtikl.size_centr));
//            double point1[] = UGeo.cross(x1() + h[0], y1() + h[1], x2() + h[0], y2() + h[1], elem[0].x3, elem[0].y3, elem[0].x4, elem[0].y4);
//            double point2[] = UGeo.cross(x1() + h[0], y1() + h[1], x2() + h[0], y2() + h[1], elem[1].x3, elem[1].y3, elem[1].x4, elem[1].y4);  
            double ang1 = UGeo.horizontAngl(this.x1(), this.y1(), this.x2(), this.y2());
            double point1[] = pointCross(ang1, elem[0], elem[1]);
            double ang2 = UGeo.horizontAngl(this.x2(), this.y2(), this.x1(), this.y1());
            double point2[] = pointCross(ang2, elem[1], elem[0]);

            this.area = rectangl(point1[2], point1[3], point1[0], point1[1], point2[2], point2[3], point2[0], point2[1]);

        } catch (Exception e) {
            System.err.println("Ошибка:Elem2Cross.setLocation()" + toString() + " " + e);
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

    public void paint() {
        try {
//            int rgb = eColor.find(colorID2).getInt(eColor.rgb);
//            wing.gc2D.draw(area);
            wing.gc2D.draw(new Line2D.Double(this.x1(), this.y1(), this.x2(), this.y2()));
            //System.out.println("Elem2Cross.paint()");
            
        } catch (Exception e) {
            System.out.println("Ошибка:Elem2Cross.paint() " + e);
        }
    }
}
