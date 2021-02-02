package builder.script;

import com.google.gson.Gson;
import enums.LayoutArea;
import enums.TypeElem;
import java.util.LinkedList;

/**
 * Контернер передачи данных. В контейнере могут находиться другие контейнеры и
 * элементы.
 */
public class JsonArea extends JsonElem {

    protected LayoutArea layoutArea = null; //ориентация при размещении area
    protected float width = 0; //ширина area, мм
    protected float height = 0; //высота area, мм
    protected Float lengthSide = null; //ширина или высота добавляемой area, зависит от layoutArea, нужна на этапе конструирования (см. функцию add())
    private LinkedList<JsonElem> elements = new LinkedList();  //список area
    private LinkedList<JsonArea> areas = new LinkedList();  //список элементов

    public JsonArea() {
    }

    //Конструктор вложенной Area
    public JsonArea(float id, LayoutArea layoutArea, TypeElem elemType, float lengthSide) {
        this.id = id;
        this.layoutArea = layoutArea;
        this.elemType = elemType;
        this.lengthSide = lengthSide; //длина стороны, сторона зависит от направлени расположения area
    }

    //Конструктор створки
    public JsonArea(int id, LayoutArea layoutArea, TypeElem elemType, String paramJson) {
        this.id = id;
        this.layoutArea = layoutArea;
        this.elemType = elemType;
        this.paramJson = paramJson; //параметры элемента
    }

    //Добавление элемента в дерево
    public JsonArea addArea(JsonArea area) {

        if (TypeElem.STVORKA == area.elemType) {
            area.width = this.width;
            area.height = this.height;

        } else {
            if (LayoutArea.VERT == layoutArea) {
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

    public LayoutArea layoutArea() {
        return layoutArea;
    }

    public LinkedList<JsonArea> areas() {
        return areas;
    }

    public LinkedList<JsonElem> elems() {
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
                        for (JsonArea area5 : area4.areas) { //уровень 4
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
        }
        return null;
    }
}
