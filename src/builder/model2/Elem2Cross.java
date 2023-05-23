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
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Elem2Cross extends Elem2Simple {

    public Elem2Cross(Wingeo wing, GeoElem gson, Comp owner) {
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
            //Делим полигон
            if (owner.childs().size() == 3) {
                Area area2[] = UGeo.split(owner.area, owner.childs().get(1));
                owner.childs().get(0).area = area2[0];
                owner.childs().get(2).area = area2[1];
                double line[] = UGeo.cross(area2);
                if (line != null) {
                    this.setLocation(line[0], line[1], line[2], line[3]);
                }
            }

            prevAndNext(0);

            //Вычисляем полигон
            double[] v = new double[6];
            ArrayList<Boolean> hit = new ArrayList(List.of(false));
            PathIterator i = owner.childs().get(0).area.getPathIterator(null);
            while (!i.isDone()) {
                if (i.currentSegment(v) != PathIterator.SEG_CLOSE) {
                    for (Elem2Simple e : wing.listLine) {

                        hit.add(UGeo.pointOnLine(v[0], v[1], e.x1(), e.y1(), e.x2(), 1));
                        if (hit.get(hit.size() - 1) && hit.get(hit.size() - 2)) {

                            System.out.println("x = " + v[0] + ", y = " + v[1]);
                            System.out.println("el = " + e);
                        }
                    }
                }
                i.next();
            }
        } catch (Exception e) {
            System.err.println("Ошибка:Elem2Cross.setLocation()" + toString() + e);
        }
    }

    public Elem2Simple[] prevAndNext(int index) {
        Elem2Simple[] ret = {null, null};
        double[] v = new double[6];
        List<Point2D> p = new ArrayList(List.of(new Point2D.Double(-1, -1)));
        int ind = -1;
        PathIterator iterator = owner.childs().get(index).area.getPathIterator(null);
        while (!iterator.isDone()) {
            Point2D k = p.get(p.size() - 1);
            if (iterator.currentSegment(v) != PathIterator.SEG_CLOSE) {

                //Это Cross элемент
                if (k.getX() == this.x1() && k.getY() == this.y1() && v[0] == this.x2() && v[1] == this.y2()) {
                    System.out.println(this);

                    Point2D j = p.get(p.size() - 2);
                    for (Elem2Simple el : wing.listLine) {

                        //Это Prev элемент
                        if (UGeo.pointOnLine(j.getX(), j.getY(), el.x1(), el.y1(), el.x2(), el.y2())
                                && UGeo.pointOnLine(k.getX(), k.getY(), el.x1(), el.y1(), el.x2(), el.y2())) {
                            ret[0] = el;
                            System.out.println(el);
                        }
                    }
                    iterator.next();
                    p.add(new Point2D.Double(v[0], v[1]));

                    Point2D z = p.get(p.size() - 1);
                    for (Elem2Simple el : wing.listLine) {
                        if (UGeo.pointOnLine(id, id, id, id, id, id)) {
                            ret[1] = el;
                            System.out.println(el);
                        }
                    }
                }
            }
            p.add(new Point2D.Double(v[0], v[1]));
            iterator.next();
        }
        return ret;
    }

    public void paint() {
//        int rgb = eColor.find(colorID2).getInt(eColor.rgb);
//        wing.gc2D.draw(area);        
        wing.gc2D.draw(new Line2D.Double(this.x1(), this.y1(), this.x2(), this.y2()));
    }
}
