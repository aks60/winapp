package builder.geoms;

import builder.Wingeo;
import builder.making.Specific;
import builder.script.GeoElem;
import java.awt.Color;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;

public class Elem2Simple extends Comp {
    
    public double anglCut[] = {45, 45}; //угол реза
    public double[] anglFlat = {0, 0, 0, 0}; //мин/мах внутренний и мин/мах внешний угол к плоскости
    public double anglHoriz = 0; //угол к горизонту    
    public double[] betweenHoriz = {0,0}; //угол между векторами    

    public Specific spcRec = null; //спецификация элемента
    public Color borderColor = Color.BLACK;  

    public Elem2Simple(Wingeo wing, GeoElem gson, Comp owner) {
        super(wing, gson, owner);
        //spcRec = new Specific(id, this);
    }
    
    public void setLocation() {
        try {
            GeneralPath p = new GeneralPath();
            p.reset();
            p.moveTo(wing.listFrame.get(0).x1(), wing.listFrame.get(0).y1());
            for (int i = 1; i < wing.listFrame.size(); ++i) {
                p.lineTo(wing.listFrame.get(i).x1(), wing.listFrame.get(i).y1());
            }
            p.closePath();
            area = new Area(p);

        } catch (Exception e) {
            System.err.println("Ошибка:Elem2Simple.build()" + toString() + e);
        }        
    }
}
