package builder.script;

import com.google.gson.Gson;
import enums.LayoutArea;
import enums.TypeElem;
import java.util.LinkedList;

/**
 * Контернер передачи данных. В контейнере могут находиться другие контейнеры и
 * элементы.
 */
public class AreaElem extends Element {

    protected LayoutArea layoutArea = null; //ориентация при размещении area
    protected float width = 0; //ширина area, мм
    protected float height = 0; //высота area, мм
    protected Float lengthSide = null; //ширина или высота добавляемой area, зависит от layoutArea, нужна на этапе конструирования (см. функцию add())
    private LinkedList<Element> elements = new LinkedList();  //список area
    private LinkedList<AreaElem> areas = new LinkedList();  //список элементов

    public AreaElem() {
    }

    //Конструктор вложенной Area
    public AreaElem(float id, LayoutArea layoutArea, TypeElem elemType, float lengthSide) {
        this.id = id;
        this.layoutArea = layoutArea;
        this.elemType = elemType;
        this.lengthSide = lengthSide; //длина стороны, сторона зависит от направлени расположения area
    }

    //Конструктор створки
    public AreaElem(int id, LayoutArea layoutArea, TypeElem elemType, String paramJson) {
        this.id = id;
        this.layoutArea = layoutArea;
        this.elemType = elemType;
        this.paramJson = (paramJson.isEmpty()) ? "{}" : paramJson; //параметры элемента
    }

    //Добавление элемента в дерево
    public AreaElem addArea(AreaElem area) {

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

    public Element addElem(Element element) {
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

    public LinkedList<AreaElem> areas() {
        return areas;
    }

    public LinkedList<Element> elems() {
        return elements;
    }

    public Element find(float id) {
        if (this.id == id) {
            return this;
        }
        for (Element el : elements) {
            if (el.id == id) {
                return el;
            }
            for (AreaElem area2 : areas) { //уровень 2
                for (Element el2 : area2.elements) {
                    if (el2.id == id) {
                        return el2;
                    }
                    for (AreaElem area3 : area2.areas) { //уровень 3
                        for (Element el3 : area3.elements) {
                            if (el3.id == id) {
                                return el3;
                            }
                        }
                        for (AreaElem area4 : area3.areas) { //уровень 4
                            for (Element el4 : area4.elements) {
                                if (el4.id == id) {
                                    return el4;
                                }
                            }
                            for (AreaElem area5 : area4.areas) { //уровень 4
                                for (Element el5 : area5.elements) {
                                    if (el5.id == id) {
                                        return el5;
                                    }
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
