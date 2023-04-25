package builder.geoms;

import builder.Geocalc;
import builder.script.GeoElem;
import java.awt.geom.Area;

public class Elem2Cross extends Elem2Simple {
  
    public Elem2Cross(Geocalc winc, GeoElem gson, Comp owner) {
        super(winc, gson, owner);
        init();
    } 
   
    public void init() {
        Area polArea = new Area(owner.polygon);
        Area area[] = UGeo.split(polArea, this);               
    }      
}
