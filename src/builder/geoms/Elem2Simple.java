package builder.geoms;

import builder.Wingeo;
import builder.making.Specific;
import builder.script.GeoElem;
import java.awt.Color;
import java.awt.geom.Area;
import java.awt.geom.Line2D;

public class Elem2Simple extends Comp {
    
    protected Specific spcRec = null; //спецификация элемента
    protected Color borderColor = Color.BLACK;    

    public Elem2Simple(Wingeo wing, GeoElem gson, Comp owner) {
        super(wing, gson, owner);
    }

    public void build() {
        try {
            super.build();
            if (owner.childs().size() == 3) {
                Area area2[] = UGeo.split(owner.area, owner.childs().get(1));
                owner.childs().get(0).area = area2[0];
                owner.childs().get(2).area = area2[1];
                double line[] = UGeo.cross(area2);
                if (line != null) {
                    this.setLocation(line[0], line[1], line[2], line[3]);
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка:Elem2Simple.build()" + toString() + e);
        }
    }
}
