package builder.script;

import frames.swing.draw.Scale;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import enums.Layout;
import enums.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GsonElem {

    protected static transient float genId = -1;  //идентификатор    
    protected float id = -1;  //идентификатор
    protected transient GsonElem owner = null;  //владелец     
    protected LinkedList<GsonElem> childs = new LinkedList();  //список детей
    protected Layout layout = null; //сторона расположения эл. рамы
    protected Type type = null; //тип элемента
    protected JsonObject param = null; //параметры элемента
    protected Float length = null; //ширина или высота добавляемой area (зависит от напрвления расположения) 

    public transient float point = 0;  //точка scale 

    public GsonElem() {
    }

    //Конструктор Elem
    public GsonElem(Type type) {
        this.id = ++genId;
        this.type = type;
    }

    //Конструктор Elem
    public GsonElem(Type type, String paramJson) {
        this.id = ++genId;
        this.type = type;
        this.param = new Gson().fromJson(paramJson, JsonObject.class);
    }

    //Конструктор Elem
    public GsonElem(Type type, Layout layoutRama) {
        this.id = ++genId;
        this.type = type;
        this.layout = layoutRama;
    }

    //Конструктор Elem
    public GsonElem(Type type, Layout layoutRama, String paramJson) {
        this.id = ++genId;
        this.type = type;
        this.layout = layoutRama;
        this.param = new Gson().fromJson(paramJson, JsonObject.class);
    }

    //Конструктор Area
    public GsonElem(Layout layout, Type type, float length) {
        this.id = ++genId;
        this.layout = layout;
        this.type = type;
        this.length = length; //длина стороны, сторона зависит от направления расположения area
    }

    //Конструктор створки
    public GsonElem(Layout layout, Type type) {
        this.id = ++genId;
        this.layout = layout;
        this.type = type;
    }

    //Конструктор створки
    public GsonElem(Layout layout, Type type, String paramJson) {
        this.id = ++genId;
        this.layout = layout;
        this.type = type;
        this.param = new Gson().fromJson(paramJson, JsonObject.class); //параметры элемента
    }

    public GsonElem addArea(GsonElem area) {
        childs = (childs == null) ? new LinkedList() : childs;
        if (area.type == Type.STVORKA) {
            area.length = this.length;
        }
        this.childs.add(area);
        return area;
    }

    public GsonElem addElem(GsonElem element) {
        childs = (childs == null) ? new LinkedList() : childs;
        this.childs.add(element);
        return this;
    }

    public float id() {
        return id;
    }

    public Type type() {
        return type;
    }

    public Layout layout() {
        return layout;
    }

    public JsonObject param() {
        if (param instanceof JsonObject == false) {
            param = new JsonObject();
        }
        return param;
    }

    public void param(JsonObject param) {
        this.param = param;
    }

    public float height() {
        return (owner.layout == Layout.VERT) ? length : owner.height();
    }

    public float width() {
        return (owner.layout == Layout.HORIZ) ? length : owner.width();
    }

    public float length() {
        return this.length;
    }

    public void length(float length) {
        this.length = length;
    }

    public GsonElem owner() {
        return owner;
    }

    public LinkedList<GsonElem> childs() {
        return childs;
    }

    public void parent(GsonElem node) {
        node.childs.forEach(el -> {
            el.owner = node;
            if (el.type() == Type.STVORKA || el.type() == Type.AREA || el.type() == Type.ARCH || el.type() == Type.TRAPEZE || el.type() == Type.TRIANGL) {
                parent(el); //реккурсия 
            }
        });
    }

    public LinkedList<GsonElem> elems() {
        return childs.stream().filter(el
                -> (el.type() != Type.STVORKA && el.type() != Type.AREA && el.type() != Type.ARCH && el.type() != Type.TRAPEZE || el.type() == Type.TRIANGL))
                .collect(Collectors.toCollection(() -> new LinkedList<GsonElem>()));
    }
}
