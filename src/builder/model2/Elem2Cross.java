package builder.model2;

import builder.Wingeo;
import builder.script.GeoElem;
import com.google.gson.JsonObject;
import domain.eArtikl;
import domain.eSysprof;
import enums.PKjson;
import enums.UseSide;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

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
            sysprofRec = eSysprof.find4(wing.nuni, type.id2, UseSide.ANY);
        }
        artiklRec = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), false);
        artiklRecAn = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), true);
    }

    public void setLocation() {
        try {
            anglHoriz = UGeo.horizontAngl(this);
            double w = owner.area.getBounds2D().getMaxX();
            double h = owner.area.getBounds2D().getMaxY();
            double M[] = UGeo.diffOnAngl(UGeo.horizontAngl(this), this.artiklRec.getDbl(eArtikl.height) - this.artiklRec.getDbl(eArtikl.size_centr));

            //Точки пересечение импостом Canvas2D x = (y - y1)/(y2 -y1)*(x2 - x1) + x1,  y = (x - x1)/(x2 - x1)*(y2 - y1) + y1
            //https://www.interestprograms.ru/source-codes-tochka-peresecheniya-dvuh-pryamyh-na-ploskosti#uravnenie-v-programmnyj-kod   
            double L0[] = crossLine(this.x1(), this.y1(), this.x2(), this.y2(), w, h);
            System.out.println(L0[0] + " ? " + L0[1] + "  =  " + L0[2] + "  " + L0[3]);

            Area areaLeft = (Area) owner.area.clone();
            Area areaRigh = (Area) owner.area.clone();
            Area clipLeft = UGeo.area(0, 0, 0, h, L0[2], h, L0[0], 0);
            Area clipRigh = UGeo.area(L0[0], 0, L0[2], h, w, h, w, 0);
            areaLeft.intersect(clipLeft);
            areaRigh.intersect(clipRigh);

            //Вектор импоста
            Line2D.Double d[] = UGeo.prevAndNextSegment(areaLeft, areaRigh);
            if (d != null) {

                this.setDimension(d[2].x1, d[2].y1, d[2].x2, d[2].y2);
                double L1[] = crossLine(this.x1() - 40, this.y1(), this.x2() - 40, this.y2(), w, h);
                double L2[] = crossLine(this.x1() + 40, this.y1(), this.x2() + 40, this.y2(), w, h);
//            double L1[] = crossLine(this.x1() + M[0], this.y1() + M[1], this.x2() + M[0], this.y2() + M[1], w, h);
//            double L2[] = crossLine(this.x1() - M[0], this.y1() - M[1], this.x2() - M[0], this.y2() - M[1], w, h);

                Area areaImp = (Area) owner.area.clone();
                Area areaClip = UGeo.area(L1[0], L1[1], L1[2], L1[3], L2[2], L2[3], L2[0], L2[1]);
                areaImp.intersect(areaClip);
                UGeo.PRINT(areaImp);
                this.area = areaImp;
            }

        } catch (Exception e) {
            this.area = null;
            System.err.println("Ошибка:Elem2Cross.setLocation() " + e);
        }
    }

    public void setLocation2() {
        try {
            anglHoriz = UGeo.horizontAngl(this);
            double w = owner.area.getBounds2D().getMaxX();
            double h = owner.area.getBounds2D().getMaxY();

            //Точки пересечение импостом Canvas2D
            //x = (y - y1)/(y2 -y1)*(x2 - x1) + x1,  y = (x - x1)/(x2 - x1)*(y2 - y1) + y1
            //https://www.interestprograms.ru/source-codes-tochka-peresecheniya-dvuh-pryamyh-na-ploskosti#uravnenie-v-programmnyj-kod            
            double X1 = (this.y1() == this.y2()) ? 0 : (((0 - this.y1()) / (this.y2() - this.y1())) * (this.x2() - this.x1())) + this.x1();
            double Y1 = (this.x1() == this.x2()) ? 0 : (((0 - this.x1()) / (this.x2() - this.x1())) * (this.y2() - this.y1())) + this.y1();
            double X2 = (this.y1() == this.y2()) ? w : (((h - this.y1()) / (this.y2() - this.y1())) * (this.x2() - this.x1())) + this.x1();
            double Y2 = (this.x1() == this.x2()) ? h : (((w - this.x1()) / (this.x2() - this.x1())) * (this.y2() - this.y1())) + this.y1();

            System.out.println(X1 + "  " + Y1 + "  =  " + X2 + "  " + Y2);

            double M[] = UGeo.diffOnAngl(UGeo.horizontAngl(this), this.artiklRec.getDbl(eArtikl.height) - this.artiklRec.getDbl(eArtikl.size_centr));
            double L1[] = crossLine(this.x1() + M[0], this.y1() + M[1], this.x2() + M[0], this.y2() + M[1], w, h);
            double L2[] = crossLine(this.x1() - M[0], this.y1() - M[1], this.x2() - M[0], this.y2() - M[1], w, h);
            Area areaImp = (Area) owner.area.clone();
            Area areaClip = UGeo.area(L2[0], L2[1], L2[2], L2[3], L1[2], L1[3], L1[0], L1[1]);
            areaImp.intersect(areaClip);
            this.area = areaImp;

            Area areaLeft = (Area) owner.area.clone();
            Area areaRigh = (Area) owner.area.clone();
            Area clipLeft = UGeo.area(0, 0, 0, h, X2, h, X1, 0);
            Area clipMidl = UGeo.area(x1() - M[0], y1() - M[1], x2() - M[0], y2() - M[1], x2() + M[0], y2() + M[1], x1() + M[0], y1() + M[1]);
            Area clipRigh = UGeo.area(X1, 0, X2, h, w, h, w, 0);

            areaLeft.intersect(clipLeft);
            areaRigh.intersect(clipRigh);
            owner.childs().get(0).area = areaLeft;
            owner.childs().get(2).area = areaRigh;

            //Вектор импоста
            Line2D.Double d[] = UGeo.prevAndNextSegment(areaLeft, areaRigh);
            if (d != null) {

                this.setDimension(d[2].x1, d[2].y1, d[2].x2, d[2].y2);
                double h0[] = UGeo.diffOnAngl(UGeo.horizontAngl(this), this.artiklRec.getDbl(eArtikl.height) - this.artiklRec.getDbl(eArtikl.size_centr));

                Elem2Simple e0 = UGeo.elemFromSegment(wing.listLine, d[0].x1, d[0].y1, d[0].x2, d[0].y2);
                Elem2Simple e1 = UGeo.elemFromSegment(wing.listLine, d[1].x1, d[1].y1, d[1].x2, d[1].y2);
                double h1[] = UGeo.diffOnAngl(UGeo.horizontAngl(e0), e0.artiklRec.getDbl(eArtikl.height) - e0.artiklRec.getDbl(eArtikl.size_centr));
                double h2[] = UGeo.diffOnAngl(UGeo.horizontAngl(e1), e1.artiklRec.getDbl(eArtikl.height) - e1.artiklRec.getDbl(eArtikl.size_centr));
                double p1[] = UGeo.crossOnLine(x1() + h0[0], y1() + h0[1], x2() + h0[0], y2() + h0[1], e0.x1() + h1[0], e0.y1() + h1[1], e0.x2() + h1[0], e0.y2() + h1[1]);
                double p2[] = UGeo.crossOnLine(x1() + h0[0], y1() + h0[1], x2() + h0[0], y2() + h0[1], e1.x1() + h2[0], e1.y1() + h2[1], e1.x2() + h2[0], e1.y2() + h2[1]);

                Elem2Simple e3 = UGeo.elemFromSegment(wing.listLine, d[0].x1, d[0].y1, d[0].x2, d[0].y2);
                Elem2Simple e4 = UGeo.elemFromSegment(wing.listLine, d[1].x1, d[1].y1, d[1].x2, d[1].y2);
                double h3[] = UGeo.diffOnAngl(UGeo.horizontAngl(e3), e3.artiklRec.getDbl(eArtikl.size_centr) - e3.artiklRec.getDbl(eArtikl.height));
                double h4[] = UGeo.diffOnAngl(UGeo.horizontAngl(e4), e4.artiklRec.getDbl(eArtikl.size_centr) - e4.artiklRec.getDbl(eArtikl.height));
                double p3[] = UGeo.crossOnLine(x1() - h0[0], y1() - h0[1], x2() - h0[0], y2() - h0[1], e3.x1() - h3[0], e3.y1() - h3[1], e3.x2() - h3[0], e3.y2() - h3[1]);
                double p4[] = UGeo.crossOnLine(x1() - h0[0], y1() - h0[1], x2() - h0[0], y2() - h0[1], e4.x1() - h4[0], e4.y1() - h4[1], e4.x2() - h4[0], e4.y2() - h4[1]);

                //this.addDimension(p2[0], p2[1], p1[0], p1[1]);
                this.area = UGeo.rectangl(p1[0], p1[1], p2[0], p2[1], p4[0], p4[1], p3[0], p3[1]);
                //this.area = UGeo.rectangl(p2[0], p2[1], p1[0], p1[1], x1(), y1(), x2(), y2());
                //this.area = UGeo.rectangl(x2(), y2(), x1(), y1(), p3[0], p3[1], p4[0], p4[1]);
            }

        } catch (Exception e) {
            this.area = null;
            System.err.println("Ошибка:Elem2Cross.setLocation() " + e);
        }
    }

    public double[] crossLine(double x1, double y1, double x2, double y2, double w, double h) {
        double X1 = (y1 == y2) ? 0 : (((0 - y1) / (y2 - y1)) * (x2 - x1)) + x1;
        double Y1 = (x1 == x2) ? 0 : (((0 - x1) / (x2 - x1)) * (y2 - y1)) + y1;
        double X2 = (y1 == y2) ? w : (((h - y1) / (y2 - y1)) * (x2 - x1)) + x1;
        double Y2 = (x1 == x2) ? h : (((w - x1) / (x2 - x1)) * (y2 - y1)) + y1;
        //System.out.println(X1 + "  " + Y1 + "  =  " + X2 + "  " + Y2);
        return new double[]{X1, Y1, X2, Y2};
    }

    public void paint() {
        try {
            wing.gc2D.draw(this.area);
            wing.gc2D.draw(new Line2D.Double(this.x1(), this.y1(), this.x2(), this.y2()));

        } catch (Exception e) {
            System.err.println("Ошибка:Elem2Cross.paint() " + e);
        }
    }
}
