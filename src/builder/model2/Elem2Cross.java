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
            //x = (y - y1)/(y2 -y1)*(x2 - x1) + x1  y = (x - x1)/(x2 - x1)*(y2 - y1) + y1
            //https://www.interestprograms.ru/source-codes-tochka-peresecheniya-dvuh-pryamyh-na-ploskosti#uravnenie-v-programmnyj-kod            
            double X1 = (this.y1() == this.y2()) ? 0 : (((0 - this.y1()) / (this.y2() - this.y1())) * (this.x2() - this.x1())) + this.x1();
            //double Y1 = (this.x1() == this.x2()) ? 0 : (((0 - this.x1()) / (this.x2() - this.x1())) * (this.y2() - this.y1())) + this.y1();
            double X2 = (this.y1() == this.y2()) ? w : (((h - this.y1()) / (this.y2() - this.y1())) * (this.x2() - this.x1())) + this.x1();
            //double Y2 = (this.x1() ==  this.x2()) ? h : (((w - this.x1()) / (this.x2() - this.x1())) * (this.y2() - this.y1())) + this.y1();

            Area areaLeft = (Area) owner.area.clone();
            Area areaRigh = (Area) owner.area.clone();
            Area clipLeft = UGeo.area(0, 0, 0, h, X2, h, X1, 0);
            Area clipRigh = UGeo.area(X1, 0, X2, h, w, h, w, 0);
            areaLeft.intersect(clipLeft);
            areaRigh.intersect(clipRigh);
            
            UGeo.PRINT(areaLeft);
            UGeo.PRINT(areaRigh);

            //Вектор импоста
            double lineCross[] = UGeo.generalSegment(areaLeft, areaRigh);
            if (lineCross != null) {
                this.setDimension(lineCross[0], lineCross[1], lineCross[2], lineCross[3]);
            }

            Line2D.Double d[] = UGeo.prevAndNextSegment(areaLeft, this);
            Elem2Simple e0 = UGeo.elementOnSegment(wing.listLine, d[0].x1, d[0].y1, d[0].x2, d[0].y2);
            Elem2Simple e1 = UGeo.elementOnSegment(wing.listLine, d[1].x1, d[1].y1, d[1].x2, d[1].y2);
            Elem2Simple e[] = {e0, e1};
            
            //Полигон импоста
            double h0[] = UGeo.diff(UGeo.horizontAngl(this), this.artiklRec.getDbl(eArtikl.height) - this.artiklRec.getDbl(eArtikl.size_centr));

            double h1[] = UGeo.diff(UGeo.horizontAngl(e[0]), e[0].artiklRec.getDbl(eArtikl.height) - e[0].artiklRec.getDbl(eArtikl.size_centr));
            double h2[] = UGeo.diff(UGeo.horizontAngl(e[1]), e[1].artiklRec.getDbl(eArtikl.height) - e[1].artiklRec.getDbl(eArtikl.size_centr));
            double p1[] = UGeo.cross(x1() + h0[0], y1() + h0[1], x2() + h0[0], y2() + h0[1], e[0].x1() + h1[0], e[0].y1() + h1[1], e[0].x2() + h1[0], e[0].y2() + h1[1]);
            double p2[] = UGeo.cross(x1() + h0[0], y1() + h0[1], x2() + h0[0], y2() + h0[1], e[1].x1() + h2[0], e[1].y1() + h2[1], e[1].x2() + h2[0], e[1].y2() + h2[1]);

            double h3[] = UGeo.diff(UGeo.horizontAngl(e0), e0.artiklRec.getDbl(eArtikl.size_centr) - e0.artiklRec.getDbl(eArtikl.height));
            double h4[] = UGeo.diff(UGeo.horizontAngl(e1), e1.artiklRec.getDbl(eArtikl.size_centr) - e1.artiklRec.getDbl(eArtikl.height));
            double p3[] = UGeo.cross(x1() + h0[0], y1() + h0[1], x2() + h0[0], y2() + h0[1], e[0].x1() + h3[0], e[0].y1() + h3[1], e[0].x2() + h3[0], e[0].y2() + h3[1]);
            double p4[] = UGeo.cross(x1() + h0[0], y1() + h0[1], x2() + h0[0], y2() + h0[1], e[1].x1() + h4[0], e[1].y1() + h4[1], e[1].x2() + h4[0], e[1].y2() + h4[1]);
            //this.addDimension(p2[0], p2[1], p1[0], p1[1]);
            //this.area = rectangl(p2[0], p2[1], p1[0], p1[1], p4[0], p4[1], p3[0], p3[1]);
            //this.area = rectangl(p2[0], p2[1], p1[0], p1[1], x1(), y1(), x2(), y2());
            //this.area =  rectangl(x1(), y1(), x2(), y2(), p4[0], p4[1], p3[0], p3[1]);
            this.area = areaLeft;

        } catch (Exception e) {
            this.area = null;
            System.err.println("Ошибка:Elem2Cross.setLocation() " + e);
        }
    }

    public void paint() {
        try {
            //wing.gc2D.draw(this.area);
            wing.gc2D.draw(new Line2D.Double(this.x1(), this.y1(), this.x2(), this.y2()));

        } catch (Exception e) {
            System.out.println("Ошибка:Elem2Cross.paint() " + e);
        }
    }
}
