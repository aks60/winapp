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
        return this;
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

    public void resizRoot(float length2, Layout layout2) {
        List<GsonElem> areaList = this.childs.stream().filter(it -> it.type == Type.AREA).collect(toList());

        //    Горизонтальное перераспределение
        if (layout2 == Layout.HORIZ) {
            for (GsonElem elem : areaList) {
                if (this.layout == Layout.HORIZ) { //расположение по горизонтали
                    elem.length = (length2 / this.width()) * elem.width();
                    elem.resizAll(elem.length, layout2);
                } else {
                    elem.resizAll(length2, layout2);
                }
            }
            ((GsonRoot) this).width = length2;

            //Вертикальное перераспределение
        } else if (layout2 == Layout.VERT) {
            for (GsonElem elem : areaList) {
                if (this.layout == Layout.VERT) { //расположение по вертикали
                    elem.length = (length2 / this.height()) * elem.height();
                    elem.resizAll(elem.length, layout2);
                }
                elem.resizAll(length2, layout2);
            }
            GsonRoot gsonRoot = (GsonRoot) this;
            gsonRoot.heightAdd = (length2 / gsonRoot.height) * gsonRoot.heightAdd;
            gsonRoot.height = length2;
        }
    }

    public void resizAll(float length2, Layout layout2) {
        List<GsonElem> areaList = this.childs.stream().filter(it -> it.type == Type.AREA).collect(toList());
        for (GsonElem elem : areaList) {

            if (layout2 == Layout.HORIZ && this.layout == Layout.HORIZ) { //горизонтальное перераспределение и расположение
                elem.length = (length2 / this.width()) * elem.width();
                elem.resizAll(elem.length, layout2);

            } else if (layout2 == Layout.VERT && this.layout == Layout.VERT) { //вертикальное перераспределение и расположение
                elem.length = (length2 / this.height()) * elem.height();
                elem.resizAll(elem.length, layout2);
            }
            elem.resizAll(length2, layout2);
        }
    }

    public void resizNext(float length2, Layout layout2) {
        List<GsonElem> areaList = this.owner.childs.stream().filter(it -> it.type == Type.AREA).collect(toList());

        //   Горизонтальное перераспределение
        if (layout2 == Layout.HORIZ) {
            if (this.owner.layout == Layout.HORIZ) { //расположение по горизонтали                
                for (int index = 0; index < areaList.size(); ++index) {
                    if (this == areaList.get(index)) {
                        if (index < areaList.size() - 1) {
                            GsonElem elem = areaList.get(index + 1);
                            elem.length = elem.width() + this.length - length2;
                            elem.resizAll(elem.length, layout2);
                        }
                        break;
                    }
                }
            }

            //Вертикальное перераспределение
        } else if (layout2 == Layout.VERT) {
            if (this.owner.layout == Layout.VERT) { //расположение по вертикали                
                for (int index = 0; index < areaList.size(); ++index) {
                    if (this == areaList.get(index)) {
                        if (index < areaList.size() - 1) {
                            GsonElem elem = areaList.get(index + 1);
                            elem.length = elem.height() + this.length - length2;
                            elem.resizAll(elem.length, layout2);
                        }
                        break;
                    }
                }
            }
        }
        this.length = length2;
        this.resizAll(this.length, layout2);
    }

    public void resizWay(float length2, Layout layout2) {
        List<GsonElem> areaList = this.owner.childs.stream().filter(it -> it.type == Type.AREA).collect(toList());

        //   Горизонтальное перераспределение
        if (layout2 == Layout.HORIZ) {
            if (this.owner.layout == Layout.HORIZ) { //расположение по горизонтали                  
                for (int index = 0; index < areaList.size(); ++index) {
                    if (this == areaList.get(index)) {
                        if (index < areaList.size() - 1) {
                            float size = 0;
                            for (int index2 = index + 1; index2 < areaList.size(); ++index2) {
                                size += areaList.get(index2).width();
                            }
                            float dx = (this.length - length2) / size;
                            for (int index2 = index + 1; index2 < areaList.size(); ++index2) {
                                GsonElem elem = areaList.get(index2);
                                elem.length += dx * elem.length;
                                elem.resizAll(elem.length, layout2);
                            }
                        }
                        break;
                    }
                }
                this.length = length2;
                this.resizAll(this.length, layout2);
            }

            //Вертикальное перераспределение
        } else if (layout2 == Layout.VERT) {
            if (this.owner.layout == Layout.VERT) { //расположение по вертикали                
			   for (int index = 0; index < areaList.size(); ++index) {
                    if (this == areaList.get(index)) {
                        if (index < areaList.size() - 1) {
                            float size = 0;
                             for (int index2 = index + 1; index2 < areaList.size(); ++index2) {
                                size += areaList.get(index2).height();
                            }
                            float dy = (this.length - length2) / size;
                            for (int index2 = index + 1; index2 < areaList.size(); ++index2) {
                                GsonElem elem = areaList.get(index2);
                                elem.length += dy * elem.length;
                                elem.resizAll(elem.length, layout2);
                            }
                        }
                        break;
                    }
                }
                this.length = length2;
                this.resizAll(this.length, layout2);
            }
        }
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

    public LinkedList<GsonElem> elems() {
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
        for (GsonElem el : elems()) {
            if (el.id == id) {
                return el;
            }
        }
        for (GsonElem area2 : areas()) { //уровень 2
            if (area2.id == id) {
                return area2;
            }
            for (GsonElem el2 : area2.elems()) {
                if (el2.id == id) {
                    return el2;
                }
            }
            for (GsonElem area3 : area2.areas()) { //уровень 3
                if (area3.id == id) {
                    return area3;
                }
                for (GsonElem el3 : area3.elems()) {
                    if (el3.id == id) {
                        return el3;
                    }
                }
                for (GsonElem area4 : area3.areas()) { //уровень 4
                    if (area4.id == id) {
                        return area4;
                    }
                    for (GsonElem el4 : area4.elems()) {
                        if (el4.id == id) {
                            return el4;
                        }
                    }
                    for (GsonElem area5 : area4.areas()) { //уровень 5
                        if (area5.id == id) {
                            return area5;
                        }
                        for (GsonElem el5 : area5.elems()) {
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
        node.elems().forEach(elem -> elem.owner = node);
        for (GsonElem area : node.areas()) {
            area.owner = node;
            area.elems().forEach(elem -> elem.owner = node);
            setParent(area); //реккурсия
        }
    }
}
