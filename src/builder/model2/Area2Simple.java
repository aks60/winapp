package builder.model2;

import builder.Wingeo;
import builder.script.GeoElem;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Area2Simple extends Com6s {

    public Area area2 = null;
    public LinkedList<Point2D> listSkin = new LinkedList();
    public List<Com6s> childs = new ArrayList(); //дети

    public Area2Simple(Wingeo wing, GeoElem gson, Com6s owner) {
        super(wing, gson, owner);
    }

    @Override
    public List<Com6s> childs() {
        return childs;
    }

//    public void paint() {
//        try {
//            if (area != null) {
//                wing.gc2D.draw(area);
//            }
//        } catch (Exception e) {
//            System.err.println("Ошибка:Area2Simple.paint()" + toString() + e);
//        }
//    }

//    public void mouseEvent() {
//
//    }
}
