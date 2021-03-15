package builder.script;

import builder.model.AreaSimple;
import enums.LayoutArea;
import enums.TypeElem;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class GsonElem {

    protected float id = -1;  //ориентация при размещении
    public transient GsonElem parenet = null;  //владелец 
    protected LinkedList<GsonElem> childs = null;  //список детей
    protected LayoutArea layout = null; //сторона располодения эл. рамы
    protected TypeElem type = null; //тип элемента
    protected String param = null; //параметры элемента
    protected Float width = null; //ширина area, мм
    protected Float height = null; //высота area, мм
    protected transient Float lengthSide = null; //ширина или высота добавляемой area    

    public GsonElem() {
    }

    //Конструктор Elem
    public GsonElem(float id, TypeElem elemType) {
        this.id = id;
        this.type = elemType;
    }

    //Конструктор Elem
    public GsonElem(float id, TypeElem elemType, String paramJson) {
        this.id = id;
        this.type = elemType;
        this.param = paramJson;
    }

    //Конструктор Elem
    public GsonElem(float id, TypeElem elemType, LayoutArea layoutRama) {
        this.id = id;
        this.type = elemType;
        this.layout = layoutRama;
    }

    //Конструктор вложенной Area
    public GsonElem(float id, LayoutArea layout, TypeElem elemType, float lengthSide) {
        this.id = id;
        this.layout = layout;
        this.type = elemType;
        this.lengthSide = lengthSide; //длина стороны, сторона зависит от направлени расположения area
    }

    //Конструктор створки
    public GsonElem(int id, LayoutArea layout, TypeElem type, String paramJson) {
        this.id = id;
        this.layout = layout;
        this.type = type;
        this.param = paramJson; //параметры элемента
    }

    public GsonElem addArea(GsonElem area) {
        childs = (childs == null) ? new LinkedList() : childs;

        if (TypeElem.STVORKA == area.type) {
            area.width = this.width;
            area.height = this.height;

        } else {
            if (LayoutArea.VERT == layout) {
                area.height = area.lengthSide;
                area.width = width;
            } else {
                area.height = height;
                area.width = area.lengthSide;
            }
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

    public TypeElem type() {
        return type;
    }

    public LayoutArea layout() {
        return layout;
    }

    public String param() {
        return (param == null || param.isEmpty() == true) ? "{}" : param;
    }

    public void param(String param) {
        this.param = param;
    }

    public float height() {
        return height;
    }

    public void height(float height) {
        this.height = height;
    }

    public float width() {
        return width;
    }

    public void width(float width) {
        this.width = width;
    }

    public void heightUp(float h_new) {

        if (this.parenet.areas().size() == 1 || this.parenet.layout == LayoutArea.HORIZ) {
            this.parenet.heightUp(h_new);

        } else {
            float dy = this.height - h_new;
            for (GsonElem area2 : this.parenet.areas()) {

                float h_old = area2.height;
                if (this.parenet.layout == LayoutArea.VERT) {
                    area2.height = (area2 == this) ? h_new : area2.height + dy / (this.parenet.areas().size() - 1);

                } else {
                    area2.height = h_new;
                }
                heightDown(area2, area2.height / h_old);
            }
        }
    }

    public void widthUp(float w_new) {

        if (this.parenet.areas().size() == 1 || this.parenet.layout == LayoutArea.VERT) {
            this.parenet.widthUp(w_new);

        } else {
            float dx = this.width - w_new;
            for (GsonElem area2 : this.parenet.areas()) {

                float w_old = area2.width;
                if (this.parenet.layout == LayoutArea.HORIZ) {
                    area2.width = (area2 == this) ? w_new : area2.width + dx / (this.parenet.areas().size() - 1);

                } else {
                    area2.width = w_new;
                }
                widthDown(area2, area2.width / w_old);
            }
        }
    }

    public void heightDown(GsonElem area, float wt) {
        for (GsonElem area2 : area.areas()) {

            widthDown(area2, (wt * area2.height) / area2.height);
            area2.height = wt * area2.height;
        }
    }

    public void widthDown(GsonElem area, float wt) {
        for (GsonElem area2 : area.areas()) {

            widthDown(area2, (wt * area2.width) / area2.width);
            area2.width = wt * area2.width;
        }
    }

    public LinkedList<GsonElem> childs() {
        return childs;
    }

    public LinkedList<GsonElem> areas() {
        LinkedList<GsonElem> list = new LinkedList();
        childs.forEach(el -> {
            if (el.type() == TypeElem.STVORKA || el.type() == TypeElem.AREA) {
                list.add(el);
            }
        });
        return list;
    }

    public LinkedList<GsonElem> elements() {
        LinkedList<GsonElem> list = new LinkedList();
        childs.forEach(el -> {
            if (el.type() != TypeElem.STVORKA || el.type() != TypeElem.AREA) {
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

    public void setParent(GsonElem trunk) {
        for (GsonElem area : trunk.areas()) {
            area.parenet = trunk;
            area.elements().forEach(elem -> elem.parenet = trunk);
            setParent(area);
        }
    }
}
