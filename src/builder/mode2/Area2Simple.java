package builder.mode2;

import builder.Wingeo;
import builder.mode1.ElemSimple;
import builder.script.GeoElem;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class Area2Simple extends Comp {

    public List<Comp> childs = new ArrayList(); //дети

    public Area2Simple(Wingeo wing, GeoElem gson, Comp owner) {
        super(wing, gson, owner);
    }

    public Area2Simple(Wingeo wing, GeoElem gson, Comp owner, double x1, double y1, double x2, double y2) {
        super(wing, gson, owner);
    }

    @Override
    public List<Comp> childs() {
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
