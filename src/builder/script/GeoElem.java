package builder.script;

import static builder.script.GsonElem.genId;
import com.google.gson.JsonObject;
import enums.Layout;
import enums.Type;
import java.util.LinkedList;

public class GeoElem {

    private static transient double genId = 0;  //идентификатор    
    public double id = 0;  //идентификатор
    public transient GeoElem owner = null;  //владелец
    public LinkedList<GeoElem> childs = null; //список детей
    public JsonObject param = null; //параметры элемента
    public Type type = null; //тип элемента
    public Double x1, y1, x2, y2;

    public GeoElem() {
        ++genId;
    }
    
    public GeoElem(Type type) {
        this.id = ++genId;
        this.type = type;
    }

    public GeoElem(Type type, double x1, double y1, double x2, double y2) {
        this.id = ++genId;
        this.type = type;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public GeoElem addArea(GeoElem area) {
        area.owner = this;
        childs = (childs == null) ? new LinkedList() : childs;
        this.childs.add(area);
        return area;
    }
    
    public GeoElem addElem(GeoElem elem) {
        elem.owner = this;
        childs = (childs == null) ? new LinkedList() : childs;
        this.childs.add(elem);
        return this;
    }
}
