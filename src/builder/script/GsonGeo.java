package builder.script;

import com.google.gson.JsonObject;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

public class GsonGeo {

    protected static transient double genId = 0;  //идентификатор  
    protected double id = -1;  //идентификатор   
    protected GsonGeo owner = null;  //владелец    
    protected LinkedList<GsonGeo> childs = null; //список детей
    protected LinkedList<Point2D> pPoly = null; //многоугольник
    protected LinkedList<Point2D> pLine = null; //линии
    protected JsonObject param = new JsonObject(); //параметры элемента

    public GsonGeo(GsonGeo owner, LinkedList<Point2D> pPoly, LinkedList<Point2D> pLine) {
        this.id = ++genId;
        this.owner = owner;
        owner.pPoly = pPoly;
        owner.pLine = pLine;
    }
}
