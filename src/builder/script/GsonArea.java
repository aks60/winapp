package builder.script;

import enums.LayoutArea;
import enums.TypeElem;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * Контернер передачи данных. В контейнере могут находиться другие контейнеры и
 * элементы.
 */
public class GsonArea extends GsonElem {

    protected float width = 0; //ширина area, мм
    protected float height = 0; //высота area, мм
    protected transient Float lengthSide = null; //ширина или высота добавляемой area, зависит от layoutArea, нужна на этапе конструирования (см. функцию add())
    private LinkedList<GsonElem> childs = new LinkedList();  //список детей

    public GsonArea() {
    }

    //Конструктор вложенной Area
    public GsonArea(float id, LayoutArea layout, TypeElem elemType, float lengthSide) {
        this.id = id;
        this.layout = layout;
        this.type = elemType;
        this.lengthSide = lengthSide; //длина стороны, сторона зависит от направлени расположения area
    }

    //Конструктор створки
    public GsonArea(int id, LayoutArea layout, TypeElem type, String paramJson) {
        this.id = id;
        this.layout = layout;
        this.type = type;
        this.param = paramJson; //параметры элемента
    }

    public GsonArea addArea(GsonArea area) {

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
        this.childs.add(element);
        return element;
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

        if (this.parent.areas().size() == 1 || this.parent.layout == LayoutArea.HORIZ) {
            this.parent.heightUp(h_new);

        } else {
            float dy = this.height - h_new;
            for (GsonArea area2 : this.parent.areas()) {

                float h_old = area2.height;
                if (this.parent.layout == LayoutArea.VERT) {
                    area2.height = (area2 == this) ? h_new : area2.height + dy / (this.parent.areas().size() - 1);

                } else {
                    area2.height = h_new;
                }
                heightDown(area2, area2.height / h_old);
            }
        }
    }

    public void widthUp(float w_new) {

        if (this.parent.areas().size() == 1 || this.parent.layout == LayoutArea.VERT) {
            this.parent.widthUp(w_new);

        } else {
            float dx = this.width - w_new;
            for (GsonArea area2 : this.parent.areas()) {

                float w_old = area2.width;
                if (this.parent.layout == LayoutArea.HORIZ) {
                    area2.width = (area2 == this) ? w_new : area2.width + dx / (this.parent.areas().size() - 1);

                } else {
                    area2.width = w_new;
                }
                widthDown(area2, area2.width / w_old);
            }
        }
    }

    public void heightDown(GsonArea area, float wt) {
        for (GsonArea area2 : area.areas()) {

            widthDown(area2, (wt * area2.height) / area2.height);
            area2.height = wt * area2.height;
        }
    }

    public void widthDown(GsonArea area, float wt) {
        for (GsonArea area2 : area.areas()) {

            widthDown(area2, (wt * area2.width) / area2.width);
            area2.width = wt * area2.width;
        }
    }

    public LinkedList<GsonArea> areas() {
        LinkedList<GsonArea> list = new LinkedList();
        childs.forEach(el -> {
            if (el instanceof GsonArea) {
                list.add((GsonArea) el);
            }
        });
        return list;
    }

    public LinkedList<GsonElem> elements() {
        LinkedList<GsonElem> list = new LinkedList();
        childs.forEach(el -> {
            if (el instanceof GsonArea == false) {
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
        for (GsonArea area2 : areas()) { //уровень 2
            if (area2.id == id) {
                return area2;
            }
            for (GsonElem el2 : area2.elements()) {
                if (el2.id == id) {
                    return el2;
                }
            }
            for (GsonArea area3 : area2.areas()) { //уровень 3
                if (area3.id == id) {
                    return area3;
                }
                for (GsonElem el3 : area3.elements()) {
                    if (el3.id == id) {
                        return el3;
                    }
                }
                for (GsonArea area4 : area3.areas()) { //уровень 4
                    if (area4.id == id) {
                        return area4;
                    }
                    for (GsonElem el4 : area4.elements()) {
                        if (el4.id == id) {
                            return el4;
                        }
                    }
                    for (GsonArea area5 : area4.areas()) { //уровень 5
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

    public void setParent(GsonArea trunk) {
        for (GsonArea area : trunk.areas()) {
            area.parent = trunk;
            area.elements().forEach(elem -> elem.parent = trunk);
            setParent(area);
        }
    }
}
