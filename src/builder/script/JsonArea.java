package builder.script;

import enums.LayoutArea;
import enums.TypeElem;
import java.util.LinkedList;

/**
 * Контернер передачи данных. В контейнере могут находиться другие контейнеры и
 * элементы.
 */
public class JsonArea extends JsonElem {

    protected float width = 0; //ширина area, мм
    protected float height = 0; //высота area, мм
    protected Float lengthSide = null; //ширина или высота добавляемой area, зависит от layoutArea, нужна на этапе конструирования (см. функцию add())
    private LinkedList<JsonElem> elements = new LinkedList();  //список area
    private LinkedList<JsonArea> areas = new LinkedList();  //список элементов

    public JsonArea() {
    }

    //Конструктор вложенной Area
    public JsonArea(float id, LayoutArea layout, TypeElem elemType, float lengthSide) {
        this.id = id;
        this.layout = layout;
        this.type = elemType;
        this.lengthSide = lengthSide; //длина стороны, сторона зависит от направлени расположения area
    }

    //Конструктор створки
    public JsonArea(int id, LayoutArea layout, TypeElem type, String paramJson) {
        this.id = id;
        this.layout = layout;
        this.type = type;
        this.param = paramJson; //параметры элемента
    }

    //Добавление элемента в дерево
    public JsonArea addArea(JsonArea area) {

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
        this.areas.add(area);
        return area;
    }

    public JsonElem addElem(JsonElem element) {
        this.elements.add(element);
        return element;
    }

    public float height() {
        return height;
    }

    public float width() {
        return width;
    }

    public void updateHeight(float height) {
        float dy = this.height - height;
        for (JsonArea area : parent.areas) {
            area.height = area.height - dy / areas.size();
        }
        this.height = height;
    }

    public void updateWidth(float width) {
        this.width = width;
        float dx = this.width - width;

        for (JsonArea area1 : parent.areas) {
            if (area1 == this) {
                area1.width = width;
            } else {
                area1.width = area1.width + dx / parent.areas.size();
            }
            
            for (JsonArea area2 : area1.parent.areas) {
                if (area1 == area2) {
                    area2.width = width;
                } else {
                    area2.width = area2.width + dx / area1.parent.areas.size();
                }
                
                for (JsonArea area3 : area2.parent.areas) {
                    if (area2 == area3) {
                        area3.width = width;
                    } else {
                        area3.width = area3.width + dx / area3.parent.areas.size();
                    }
                }
            }
        }
    }

    public LinkedList<JsonArea> areas() {
        return areas;
    }

    public LinkedList<JsonElem> elements() {
        return elements;
    }

    public JsonElem find(float id) {
        if (this.id == id) {
            return this;
        }
        for (JsonElem el : elements) {
            if (el.id == id) {
                return el;
            }
        }
        for (JsonArea area2 : areas) { //уровень 2
            if (area2.id == id) {
                return area2;
            }
            for (JsonElem el2 : area2.elements) {
                if (el2.id == id) {
                    return el2;
                }
            }
            for (JsonArea area3 : area2.areas) { //уровень 3
                if (area3.id == id) {
                    return area3;
                }
                for (JsonElem el3 : area3.elements) {
                    if (el3.id == id) {
                        return el3;
                    }
                }
                for (JsonArea area4 : area3.areas) { //уровень 4
                    if (area4.id == id) {
                        return area4;
                    }
                    for (JsonElem el4 : area4.elements) {
                        if (el4.id == id) {
                            return el4;
                        }
                    }
                    for (JsonArea area5 : area4.areas) { //уровень 5
                        if (area5.id == id) {
                            return area5;
                        }
                        for (JsonElem el5 : area5.elements) {
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

    public void setParent(JsonArea trunk) {
        for (JsonArea area : trunk.areas) {
            area.parent = trunk;
            area.elements.forEach(elem -> elem.parent = trunk);
            setParent(area);
        }
    }
}
