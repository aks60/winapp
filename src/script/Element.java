package script;

import enums.eLayoutArea;
import enums.eTypeElem;

/**
 * Элементы передачи данных
 * Элемент не может быть контейнером
 */
public class Element {

    protected String id = null;  // идентификатор элемента
    protected eLayoutArea layoutFrame = null; //сторона располодения эл. рамы
    protected eTypeElem elemType = eTypeElem.NONE; //тип элемента
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
    public Element(String id, eTypeElem elemType) {
        this.id = id;
        this.elemType = elemType;
    }
    /**
     * Конструктор
     * @param id        профиля
     * @param elemType  тип профиля
     */
    public Element(String id, eTypeElem elemType, String paramJson) {
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
    public Element(String id, eTypeElem elemType, eLayoutArea layoutRama) {
        this.id = id;
        this.elemType = elemType;
        this.layoutFrame = layoutRama;
    }

    public eTypeElem getElemType() {
        return elemType;
    }

    public eLayoutArea getLayoutRama() {
        return layoutFrame;
    }
    
    public String toString() {
        return elemType.name();
    }     
}
