/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package builder.script;

import static builder.script.GsonElem.genId;
import com.google.gson.JsonObject;
import java.awt.geom.Point2D;
import java.util.LinkedList;

/**
 *
 * @author aks
 */
public class GsonGeo {

    protected static transient double genId = 0;  //идентификатор  
    protected GsonGeo owner = null;  //владелец    
    protected LinkedList<GsonGeo> childs = null; //список детей
    protected LinkedList<Point2D> poly = null; //полигон
    protected JsonObject param = new JsonObject(); //параметры элемента

    public GsonGeo() {
    }
}
