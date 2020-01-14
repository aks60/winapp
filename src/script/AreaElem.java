package script;

import com.google.gson.Gson;
import enums.LayoutArea;
import enums.TypeElem;
import java.util.LinkedList;

/**
 * Контернер передачи данных. В контейнере могут находиться другие контейнеры и
 * элементы.
 */
public class AreaElem extends Element {

    protected LayoutArea layoutArea = null;                  //ориентация при размещении area
    protected float width = 0;                                //ширина area, мм
    protected float height = 0;                               //высота area, мм
    private LinkedList<Element> elements = new LinkedList();  //список элементов в area
    protected Float lengthSide = null; //ширина или высота добавляемой area, зависит от layoutArea, нужно на этапе конструирования (см. функцию add())

    public AreaElem() {
    }

    /**
     * Конструктор вложенной Area
     */
    public AreaElem(String id, LayoutArea layoutArea, TypeElem elemType, float lengthSide) {
        super.id = id;
        this.layoutArea = layoutArea;
        this.elemType = elemType;
        this.lengthSide = lengthSide; //длина стороны, сторона зависит от направлени расположения area
    }
    
    /**
     * Конструктор створки
     */
    public AreaElem(String id, LayoutArea layoutArea, TypeElem elemType, String paramJson) {
        super.id = id;
        this.layoutArea = layoutArea;
        this.elemType = elemType;
        this.paramJson = paramJson; //параметры элемента
    }

    /**
     * Добавление элемента в дерево
     */
    public Element add(Element element) {
        if (element instanceof AreaElem) {

            AreaElem area = (AreaElem) element;
            if (TypeElem.FULLSTVORKA == element.elemType) {

                area.width = this.width;
                area.height = this.height;
            } else {

                if (LayoutArea.VERTICAL == layoutArea) {
                    area.height = area.lengthSide;
                    area.width = width;
                } else {
                    area.height = height;
                    area.width = area.lengthSide;
                }
            }
        }
        elements.add(element);
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

    public LinkedList<Element> getElements() {
        return elements;
    }

    public String toString() {
        return "Область заполнения";
    }
}
