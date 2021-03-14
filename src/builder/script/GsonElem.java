package builder.script;

import enums.LayoutArea;
import enums.TypeElem;

/**
 * Элементы передачи данных Элемент не может быть контейнером
 */
public class GsonElem {

    protected float id = -1;  //ориентация при размещении
    protected transient GsonArea parent = null;  //родитель
    protected LayoutArea layout = null; //сторона располодения эл. рамы
    protected TypeElem type = TypeElem.NONE; //тип элемента
    protected String param = ""; //параметры элемента

    //Конструктор
    public GsonElem() {
    }

    //Конструктор
    public GsonElem(float id, TypeElem elemType) {
        this.id = id;
        this.type = elemType;
    }

    //Конструктор
    public GsonElem(float id, TypeElem elemType, String paramJson) {
        this.id = id;
        this.type = elemType;
        this.param = paramJson;
    }

    //Конструктор
    public GsonElem(float id, TypeElem elemType, LayoutArea layoutRama) {
        this.id = id;
        this.type = elemType;
        this.layout = layoutRama;
    }

    public float id() {
        return id;
    }

    public TypeElem type() {
        return type;
    }

    public LayoutArea layout() {
        return layout;
    }

    public String param() {
        return (param.isEmpty() == true || param == null) ? "{}" : param;
    }

    public void param(String param) {
        this.param = param;
    }
}
