package builder.script;

import java.util.LinkedList;

public class GeoElem {

    public GeoRoot owner = null;  //владелец    
    public LinkedList<GeoRoot> childs = null; //список детей
    
    public double x = -1;
    public double y = -1;

    public GeoElem() {
    } 
}
