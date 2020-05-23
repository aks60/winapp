package estimate.script;

import enums.LayoutArea;
import enums.TypeElem;

/**
 * Элементы передачи данных Элемент не может быть контейнером
 */
public class Element {

    protected float id = -1;  // идентификатор элемента
    protected LayoutArea layoutFrame = null; //сторона располодения эл. рамы
    protected TypeElem elemType = TypeElem.NONE; //тип элемента
    protected String paramJson = null; //параметры элемента

    //Конструктор по умолчанию
    public Element() {
    }

    //Конструктор
    public Element(float id, TypeElem elemType) {
        this.id = id;
        this.elemType = elemType;
    }

    //Конструктор
    public Element(float id, TypeElem elemType, String paramJson) {
        this.id = id;
        this.elemType = elemType;
        this.paramJson = paramJson;
    }

    //Конструктор
    public Element(float id, TypeElem elemType, LayoutArea layoutRama) {
        this.id = id;
        this.elemType = elemType;
        this.layoutFrame = layoutRama;
    }

    public TypeElem getElemType() {
        return elemType;
    }

    public LayoutArea getLayoutRama() {
        return layoutFrame;
    }

    public String toString() {
        return elemType.name();
    }
}
