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
//    protected Float width = null; //ширина area, мм
//    protected Float height = null; //высота area, мм
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
    public GsonElem(Layout layout, Type type, float lengthSide) {
        this.id = ++genId;
        this.layout = layout;
        this.type = type;
        this.length = lengthSide; //длина стороны, сторона зависит от направлени расположения area
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
        if (owner != null) {
            if (area.length == null) {
                area.length = this.length;
            }
        }
         area.owner = this;
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
        return length();
    }

    public float width() {
        return length();
    }

    public float length() {
        try {
            if (owner.layout == Layout.HORIZ) {
                return width();
            } else {
                return height();
            }
        } catch (Exception e) {
            System.err.println("builder.script.GsonElem.length() " + e);
            return -1;
        }
    }

    public void resizeAll(float length, Layout layoutAdd) {
//        GsonElem p = (owner == null) ? this : owner;
//        List<GsonElem> areaList = p.childs.stream().filter(it -> it.type == Type.AREA).collect(toList());
//
//        if (layoutAdd == Layout.HORIZ) {
//            if (owner == null) {
//                float dx = (width - length) / width;
//                for (GsonElem gsonElem : areaList) {
//                    if (this.layout == Layout.HORIZ) {
//                        gsonElem.width += dx * gsonElem.width;
//                        List<GsonElem> areaList2 = gsonElem.childs.stream().filter(it -> it.type == Type.AREA).collect(toList());
//                        if (areaList2.isEmpty() == false) {
//                            //areaList2.get(0).resizeAll(gsonElem.width, layout);
//                        }
//                    } else {
//                        gsonElem.width = length;
//                    }
//                }
//                this.width = length;
//            } else {
//                float dx = (width - length) / (owner.width - width);
//                for (GsonElem gsonElem : areaList) {
//                    if (owner.layout == Layout.HORIZ) {
//                        gsonElem.width = (gsonElem == this) ? length : gsonElem.width + (dx * gsonElem.width);
//                        List<GsonElem> areaList2 = gsonElem.childs.stream().filter(it -> it.type == Type.AREA).collect(toList());
//                        if (areaList2.isEmpty() == false) {
//                            //areaList2.get(0).resizeAll(gsonElem.width, layout);
//                        }
//                    } else {
//                        gsonElem.width = length;
//                    }
//                }
//            }
//        } else if (layoutAdd == Layout.VERT) {
//            if (owner == null) {
//                float dy = (height - length) / height;
//                for (GsonElem gsonElem : areaList) {
//                    if (this.layout == Layout.VERT) {
//                        gsonElem.height -= dy * gsonElem.height;
//                        List<GsonElem> areaList2 = gsonElem.childs.stream().filter(it -> it.type == Type.AREA).collect(toList());
//                        if (areaList2.isEmpty() == false) {
//                            //areaList2.get(0).resizeAll(gsonElem.height, layout);
//                        }
//                    } else {
//                        gsonElem.height = length;
//                    }
//                }
//                this.height = length;
//            } else {
//                float dy = (height - length) / (owner.height - height);
//                for (GsonElem gsonElem : areaList) {
//                    if (owner.layout == Layout.VERT) {
//                        gsonElem.height = (gsonElem == this) ? length : gsonElem.height + (dy * gsonElem.height);
//                        List<GsonElem> areaList2 = gsonElem.childs.stream().filter(it -> it.type == Type.AREA).collect(toList());
//                        if (areaList2.isEmpty() == false) {
//                            //areaList2.get(0).resizeAll(gsonElem.height, layout);
//                        }
//                    } else {
//                        gsonElem.height = length;
//                    }
//                }
//            }
//        }
    }

    public void resizeNext(float length, Layout layout) {
        System.err.println("В разработке!!!");
    }

    public LinkedList<GsonElem> childs() {
        return childs;
    }

    public LinkedList<GsonElem> areas() {
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
}
