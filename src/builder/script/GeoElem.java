package builder.script;

import enums.Type;
import java.util.LinkedList;

public class GeoElem {

    public transient GeoElem owner = null;  //владелец
    public LinkedList<GeoElem> childs = null; //список детей
    protected Type type = null; //тип элемента
    public double x1, y1, x2, y2;

    public GeoElem() {
    }

    public GeoElem(Type type, double x1, double y1) {
        this.type = type;
        this.x1 = x1;
        this.y1 = y1;
    }

    public GeoElem(Type type) {
        this.type = type;
    }

    public GeoElem(Type type, double x1, double y1, double x2, double y2) {
        this.type = type;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public GeoElem addElem(GeoElem elem) {
        elem.owner = this;
        childs = (childs == null) ? new LinkedList() : childs;
        this.childs.add(elem);
        return this;
    }
}
