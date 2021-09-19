package builder.script;

import builder.model.Com5t;
import enums.Layout;
import enums.Type;
import java.util.LinkedList;
import java.util.List;
import static java.util.stream.Collectors.toList;

public class GsonElem {

    protected float id = -1;  //идентификатор
    protected static transient float genId = -1;  //идентификатор
    public transient GsonElem owner = null;  //владелец 
    protected LinkedList<GsonElem> childs = new LinkedList();  //список детей
    protected Layout layout = null; //сторона расположения эл. рамы
    protected Type type = null; //тип элемента
    protected String param = null; //параметры элемента
    protected Float length = null; //ширина или высота добавляемой area (зависит от напрвления расположения)    

    public GsonElem() {
    }

    //Конструктор Elem
    public GsonElem(Type type) {
        this.id = ++genId;
        this.type = type;
    }

    //Конструктор Elem
    public GsonElem(Type type, String paramJson) {
        this.id = ++genId;
        this.type = type;
        this.param = paramJson;
    }

    //Конструктор Elem
    public GsonElem(Type type, Layout layoutRama) {
        this.id = ++genId;
        this.type = type;
        this.layout = layoutRama;
    }

    //Конструктор Area
    public GsonElem(Layout layout, Type type, float length) {
        this.id = ++genId;
        this.layout = layout;
        this.type = type;
        this.length = length; //длина стороны, сторона зависит от направлени расположения area
    }

    //Конструктор створки
    public GsonElem(Layout layout, Type type) {
        this.id = ++genId;
        this.layout = layout;
        this.type = type;
    }

    //Конструктор створки
    public GsonElem(Layout layout, Type type, String paramJson) {
        this.id = ++genId;
        this.layout = layout;
        this.type = type;
        this.param = paramJson; //параметры элемента
    }

    public GsonElem addArea(GsonElem area) {
        childs = (childs == null) ? new LinkedList() : childs;
        if (area.type == Type.STVORKA) {
            area.length = this.length;
        }
        this.childs.add(area);
        return area;
    }

    public GsonElem addElem(GsonElem element) {
        childs = (childs == null) ? new LinkedList() : childs;
        this.childs.add(element);
        return element;
    }

    public float id() {
        return id;
    }

    public Type type() {
        return type;
    }

    public Layout layout() {
        return layout;
    }

    public String param() {
        return (param == null || param.isEmpty() == true) ? "{}" : param;
    }

    public void param(String param) {
        this.param = param;
    }

    public float height() {
        return (owner.layout == Layout.VERT) ? length : owner.height();
    }

    public float width() {
        return (owner.layout == Layout.HORIZ) ? length : owner.width();
    }

    public void resizeAll(float length2, Layout layout2) {
        GsonElem this2 = (owner == null) ? this : (this.childs.isEmpty()) ? owner : this;
        List<GsonElem> areaList2 = this2.childs.stream().filter(it -> it.type == Type.AREA).collect(toList());

        if (layout2 == Layout.HORIZ) {
            if (this2.owner == null) {

                float dx = length2 / this2.width();
                for (GsonElem elem : areaList2) {
                    if (this2.layout == Layout.HORIZ) {
                        elem.length = dx * elem.width();
                        elem.resizeAll(elem.length, layout2);
                    } else {
                        elem.resizeAll(length2, layout2);
                    }
                }
                ((GsonRoot) this2).width = length2;
            } else {
                float dx = length2 / this2.width();
                for (GsonElem elem : areaList2) {
                    if (this2.layout == Layout.HORIZ) {
                        elem.length = (elem == this2) ? length2 : dx * elem.width();
                        elem.resizeAll(elem.length, layout2);
                    }
                    elem.resizeAll(length2, layout2);
                }
            }
        } else if (layout2 == Layout.VERT) {
            if (this2.owner == null) {
                float dy = length2 / this2.height();
                for (GsonElem elem : areaList2) {
                    if (this2.layout == Layout.VERT) {
                        elem.length = dy * elem.height();
                        elem.resizeAll(elem.length, layout2);
                    }
                    elem.resizeAll(length2, layout2);
                }
                GsonRoot gsonRoot = (GsonRoot) this2;
                gsonRoot.heightAdd = (length2 / gsonRoot.height) * gsonRoot.heightAdd;
                gsonRoot.height = length2;
            } else {
                float dy = length2 / this2.height();
                for (GsonElem elem : areaList2) {
                    if (this2.layout == Layout.VERT) {
                        elem.length = (elem == this2) ? length2 : dy * elem.height();
                        elem.resizeAll(elem.length, layout2);
                    }
                    elem.resizeAll(length2, layout2);
                }
            }
        }
    }

    public void resizeNext(float length, Layout layout) {
        System.err.println("В разработке!!!");
    }

    public LinkedList<GsonElem> childs() {
        return childs;
    }

    private LinkedList<GsonElem> areas() {
        LinkedList<GsonElem> list = new LinkedList();
        childs.forEach(el -> {
            if (el.type() == Type.STVORKA || el.type() == Type.AREA) {
                list.add(el);
            }
        });
        return list;
    }

    public LinkedList<GsonElem> elements() {
        LinkedList<GsonElem> list = new LinkedList();
        childs.forEach(el -> {
            if (el.type() != Type.STVORKA || el.type() != Type.AREA) {
                list.add(el);
            }
        });
        return list;
    }

    public GsonElem find(float id) {
        if (this.id == id) {
            return this;
        }
        for (GsonElem el : elements()) {
            if (el.id == id) {
                return el;
            }
        }
        for (GsonElem area2 : areas()) { //уровень 2
            if (area2.id == id) {
                return area2;
            }
            for (GsonElem el2 : area2.elements()) {
                if (el2.id == id) {
                    return el2;
                }
            }
            for (GsonElem area3 : area2.areas()) { //уровень 3
                if (area3.id == id) {
                    return area3;
                }
                for (GsonElem el3 : area3.elements()) {
                    if (el3.id == id) {
                        return el3;
                    }
                }
                for (GsonElem area4 : area3.areas()) { //уровень 4
                    if (area4.id == id) {
                        return area4;
                    }
                    for (GsonElem el4 : area4.elements()) {
                        if (el4.id == id) {
                            return el4;
                        }
                    }
                    for (GsonElem area5 : area4.areas()) { //уровень 5
                        if (area5.id == id) {
                            return area5;
                        }
                        for (GsonElem el5 : area5.elements()) {
                            if (el5.id == id) {
                                return el5;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public void setParent(GsonElem node) {
        node.elements().forEach(elem -> elem.owner = node);
        for (GsonElem area : node.areas()) {
            area.owner = node;
            area.elements().forEach(elem -> elem.owner = node);
            setParent(area); //реккурсия
        }
    }
}
