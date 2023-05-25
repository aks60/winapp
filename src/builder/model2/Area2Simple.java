package builder.model2;

import builder.Wingeo;
import builder.model1.ElemSimple;
import builder.script.GeoElem;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class Area2Simple extends Com6s {

    public List<Com6s> childs = new ArrayList(); //дети

    public Area2Simple(Wingeo wing, GeoElem gson, Com6s owner) {
        super(wing, gson, owner);
    }

    public Area2Simple(Wingeo wing, GeoElem gson, Com6s owner, double x1, double y1, double x2, double y2) {
        super(wing, gson, owner);
    }

    @Override
    public List<Com6s> childs() {
        return childs;
    }

    public void paint() {
        try {
            if (area != null) {
                wing.gc2D.draw(area);
            }
        } catch (Exception e) {
            System.err.println("Ошибка:Area2Simple.paint()" + toString() + e);
        }
    }

    public void mouseEvent() {

    }
}
