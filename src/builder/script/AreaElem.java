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

    protected LayoutArea layoutArea = null;                   //ориентация при размещении area
    protected float width = 0;                                //ширина area, мм
    protected float height = 0;                               //высота area, мм
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
        this.paramJson = paramJson; //параметры элемента
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

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public LayoutArea geteLayoutArea() {
        return layoutArea;
    }

    public LinkedList<AreaElem> getAreas() {
        return areas;
    }
    
    public LinkedList<Element> getElements() {
        return elements;
    }

    public String toString() {
        return "Область заполнения";
    }
}
