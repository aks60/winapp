package script;

import enums.LayoutArea;
import enums.TypeElem;

/**
 * Элементы передачи данных
 * Элемент не может быть контейнером
 */
public class Element {

    protected String id = null;  // идентификатор элемента
    protected LayoutArea layoutFrame = null; //сторона располодения эл. рамы
    protected TypeElem elemType = TypeElem.NONE; //тип элемента
    protected String paramJson = null; //параметры элемента

    /**
     * Конструктор по умолчанию
     */
    public Element() {
    }

    /**
     * Конструктор
     * @param id        профиля
     * @param elemType  тип профиля
     */
    public Element(String id, TypeElem elemType) {
        this.id = id;
        this.elemType = elemType;
    }
    /**
     * Конструктор
     * @param id        профиля
     * @param elemType  тип профиля
     */
    public Element(String id, TypeElem elemType, String paramJson) {
        this.id = id;
        this.elemType = elemType;
        this.paramJson = paramJson;
    }

    /**
     * Конструктор
     * @param id          профиля
     * @param elemType    тип профиля
     * @param layoutRama  расположение
     */
    public Element(String id, TypeElem elemType, LayoutArea layoutRama) {
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
