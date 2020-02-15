package wincalc.script;

import enums.LayoutArea;
import enums.TypeElem;

/**
 * Элементы передачи данных Элемент не может быть контейнером
 */
public class Element {

    protected int id = -1;  // идентификатор элемента
    protected LayoutArea layoutFrame = null; //сторона располодения эл. рамы
    protected TypeElem elemType = TypeElem.NONE; //тип элемента
    protected String paramJson = null; //параметры элемента

    //Конструктор по умолчанию
    public Element() {
    }

    //Конструктор
    public Element(int id, TypeElem elemType) {
        this.id = id * 10;
        this.elemType = elemType;
    }

    //Конструктор
    public Element(int id, TypeElem elemType, String paramJson) {
        this.id = id * 10;
        this.elemType = elemType;
        this.paramJson = paramJson;
    }

    //Конструктор
    public Element(int id, TypeElem elemType, LayoutArea layoutRama) {
        this.id = id * 10;
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
