package builder.script;

import builder.Wincalc;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import enums.Form;
import enums.Layout;
import enums.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Класс создаётся из см. пакет builder.script.test или скрипта из бд, после
 * трансформации класс генерит json скрипт конструкции в бд.
 */
public class GsonElem {

    protected static transient float genId = 0;  //идентификатор    
    protected float id = -1;  //идентификатор
    protected transient GsonElem owner = null;  //владелец     
    protected LinkedList<GsonElem> childs = null; //список детей
    protected Layout layout = null; //направление расположения
    protected Type type = null; //тип элемента
    protected Form form = null; //форма контура (параметр в развитии)
    protected JsonObject param = new JsonObject(); //параметры элемента
    protected Float length = null; //ширина или высота добавляемой area (зависит от напрвления расположения) 

    /**
     *  Срабатывает при  десериализации и при new GsonRoot()
     *  new GsonBuilder().create().fromJson(script, GsonRoot.class);
     */
    public GsonElem() {
        ++genId;
    }

    /**
     * Все эти конструкторы запускаются при тестировании и создании моделей. см.
     * builder.script.test.Bimax.java - при тестировании см.
     * builder.script.GsonScript.java при создании моделей
     */
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

    //Конструктор Area
    public GsonElem(Layout layout, Type type, float length, Form form) {
        this.id = ++genId;
        this.layout = layout;
        this.type = type;
        this.length = length; //длина стороны, сторона зависит от направления расположения area
        this.form = form;
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
        area.owner = this;
        childs = (childs == null) ? new LinkedList() : childs;
        if (area.type == Type.STVORKA) {
            area.length = (this.layout == Layout.VERT) ? this.height() : this.width();
        }
        this.childs.add(area);
        return area;
    }

    public GsonElem addElem(GsonElem elem) {
        elem.owner = this;
        childs = (childs == null) ? new LinkedList() : childs;
        this.childs.add(elem);
        return this;
    }

    public float id() {
        return id;
    }

    public Type type() {
        return type;
    }

    public Form form() {
        return form;
    }

    public Layout layout() {
        return layout;
    }

    public JsonObject param() {
        return param;
    }

    public void param(JsonObject param) {
        this.param = param;
    }

    public Float height() {
        return (owner.layout == Layout.VERT) ? length : owner.height();
    }

    public Float width() {
        return (owner.layout == Layout.HORIZ) ? length : owner.width();
    }

    public Float length() {
        return this.length;
    }

    public void length(float length) {
        this.length = length;
    }

    public GsonElem owner() {
        return owner;
    }
    
    public static float genId() {
        return ++genId;
    }

    public LinkedList<GsonElem> childs() {
        return childs;
    }

    //Назначить родителей всем детям и поднять elem.form до Wincalc
    public void setOwner(Wincalc winc) {
        if (this instanceof GsonRoot && this.form != null) {
            ((GsonRoot) this).form = this.form;
        }
        this.childs.forEach(el -> {
            el.owner = this;
            if (el.form != null) {
                winc.form = el.form;
            }
            if (List.of(Type.STVORKA, Type.AREA, Type.RECTANGL, Type.ARCH, Type.TRAPEZE, Type.TRIANGL).contains(el.type())) {
                el.setOwner(winc); //рекурсия 
            }
        });
    }

    public void notSerialize(GsonElem gsonElem) {
        if (gsonElem == this) {
            this.param = null;
        }
        if (this.childs != null) {
            for (GsonElem el : this.childs) {
                if (el.param != null && el.param.size() == 0) {
                    el.param = null;
                }
                el.notSerialize(this); //рекурсия  
            }
        }
    }

    public static JsonObject deepMerge(JsonObject source, JsonObject target) {

        for (Map.Entry<String, JsonElement> sourceEntry : source.entrySet()) {
            String key = sourceEntry.getKey();
            JsonElement value = sourceEntry.getValue();

            if (target.has(key) == false) {
                if (value.isJsonNull() == false) {
                    target.add(key, value);
                }

            } else {
                if (value.isJsonNull() == false) {

                    if (value.isJsonObject()) {
                        deepMerge(value.getAsJsonObject(), target.get(key).getAsJsonObject());
                    } else {
                        target.add(key, value);
                    }
                } else {
                    target.remove(key);
                }
            }
        }
        return target;
    }
}
