package builder.model;

import com.google.gson.JsonObject;
import dataset.Record;
import enums.Layout;
import builder.Wincalc;
import builder.script.GsonElem;
import domain.eArtikl;
import enums.Type;

public abstract class Com5t {

    public static final int TRANSLATE_XY = 2; //сдвиг графика                 
    private float id = -1; //идентификатор 
    private Type type = Type.NONE; //Тип элемента или конструкции  
    protected Layout layout = Layout.FULL; //направление(AREA) сторона(ELEM) - расположения компонентов ...

    public Record sysprofRec = null; //профиль в системе
    public Record artiklRec = null;  //мат. средства
    public Record artiklRecAn = null;  //аналог мат. средства    

    public AreaSimple owner = null; //владелец
    public Wincalc winc = null; //главный класс калькуляции
    public AreaSimple root = null; //главный класс конструкции
    public GsonElem gson = null; //Gson object конструкции

    protected float x1 = 0, x2 = 0, y1 = 0, y2 = 0;//координаты area     
    protected int colorID1 = -1, colorID2 = -1, colorID3 = -1; //1-базовый 2-внутренний 3-внешний     

    public Com5t(Type type) {
        this.type = type;
    }

    public Com5t(float id, Wincalc winc, AreaSimple owner, GsonElem gson) {
        this.id = id;
        this.owner = owner;
        this.type = gson.type();
        this.winc = winc;
        this.root = winc.rootArea;
        this.gson = gson;
        if (this instanceof ElemFrame && owner.typeArea() == Type.STVORKA) {
            this.type = Type.STVORKA_SIDE; //фича створки
        }
    }

    public float id() {
        return id;
    }

    public AreaSimple root() {
        return winc.rootArea;
    }

    public void setDimension(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public boolean isJson(JsonObject jso, String key) {
        if (jso == null) {
            return false;
        }
        if (jso.isJsonNull()) {
            return false;
        }
        if (jso.get(key) == null) {
            return false;
        }
        return true;
    }

    public boolean isJson(JsonObject jso) {
        if (jso == null) {
            return false;
        }
        return !jso.isJsonNull();
    }

    public Type type() {
        return type;
    }

    public Layout layout() {
        return layout;
    }

    public float length() {
        ElemSimple elem5e = (ElemSimple) this;
        if (elem5e.anglHoriz == 0 || elem5e.anglHoriz == 180) {
            return (x2 > x1) ? x2 - x1 : x1 - x2;
        } else if (elem5e.anglHoriz == 90 || elem5e.anglHoriz == 270) {
            return (y2 > y1) ? y2 - y1 : y1 - y2;
        } else {
            return (float) Math.sqrt(x2 * x2 + y2 * y2);
        }
    }

    public float lengthX() {
        return (this.id == 0) ? this.gson.width() : this.gson.length();
    }

    public float lengthY() {
        return (this.id == 0) ? this.gson.height() : this.gson.length();
    }

    public void lengthX(float v) {
        try {
            if (this.id == 0) {
                float k = v / gson.width(); //коэффициент
                if (k != 1) {
                    winc.rootGson.width(v);
                    if (winc.rootGson.widthAdd() != null) {
                        winc.rootGson.widthAdd(k * winc.rootGson.widthAdd());
                    }
                    for (AreaSimple e : winc.listArea) { //перебор всех вертикальных area
                        if (e.layout == Layout.HORIZ) {
                            for (Com5t e2 : e.childs) { //изменение детей по высоте
                                if (e2.type == Type.AREA) {
                                    e2.gson.length(k * e2.gson.length());
                                }
                            }
                        }
                    }
                }
            } else {
                float k = v / this.lengthY(); //коэффициент 
                if (k != 1) {
                    this.gson.length(v);
                    if (((AreaSimple) this).typeArea() == Type.ARCH || ((AreaSimple) this).typeArea() == Type.TRAPEZE) {
                        this.winc.rootGson.widthAdd(this.winc.rootGson.width() - v);
                    }
                    for (Com5t e : ((AreaSimple) this).childs) { //изменение детей по ширине
                        if (e.owner.layout == Layout.HORIZ && (e.type == Type.AREA || e.type == Type.STVORKA)) {
                            e.lengthY(k * e.lengthY()); //рекурсия изменения детей

                        } else {
                            if (e instanceof AreaSimple) {
                                for (Com5t e2 : ((AreaSimple) e).childs) {
                                    if (e2.owner.layout == Layout.HORIZ && (e2.type == Type.AREA || e2.type == Type.STVORKA)) {
                                        e2.lengthY(k * e2.lengthY()); //рекурсия изменения детей
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка: Com5t.lengthY() " + e);
        }
    }

    public void lengthY(float v) {
        try {
            if (this.id == 0) {
                float k = v / gson.height(); //коэффициент
                if (k != 1) {
                    winc.rootGson.height(v);
                    if (winc.rootGson.heightAdd() != null) {
                        winc.rootGson.heightAdd(k * winc.rootGson.heightAdd());
                    }
                    for (AreaSimple e : winc.listArea) { //перебор всех вертикальных area
                        if (e.layout == Layout.VERT) {
                            for (Com5t e2 : e.childs) { //изменение детей по высоте
                                if (e2.type == Type.AREA) {
                                    e2.gson.length(k * e2.gson.length());
                                }
                            }
                        }
                    }
                }
            } else {
                float k = v / this.lengthY(); //коэффициент 
                if (k != 1) {
                    this.gson.length(v);
                    if (((AreaSimple) this).typeArea() == Type.ARCH || ((AreaSimple) this).typeArea() == Type.TRAPEZE) {
                        this.winc.rootGson.heightAdd(this.winc.rootGson.height() - v);
                    }
                    for (Com5t e : ((AreaSimple) this).childs) { //изменение детей по высоте
                        if (e.owner.layout == Layout.VERT && (e.type == Type.AREA || e.type == Type.STVORKA)) {
                            e.lengthY(k * e.lengthY()); //рекурсия изменения детей

                        } else {
                            if (e instanceof AreaSimple) {
                                for (Com5t e2 : ((AreaSimple) e).childs) {
                                    if (e2.owner.layout == Layout.VERT && (e2.type == Type.AREA || e2.type == Type.STVORKA)) {
                                        e2.lengthY(k * e2.lengthY()); //рекурсия изменения детей
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка: Com5t.lengthY() " + e);
        }
    }

    public float x1() {
        return x1;
    }

    public float y1() {
        return y1;
    }

    public float x2() {
        return x2;
    }

    public float y2() {
        return y2;
    }

    public Float width() {
        return (x2 > x1) ? x2 - x1 : x1 - x2;
    }

    public Float height() {

        return (y2 > y1) ? y2 - y1 : y1 - y2;
    }

    public int colorID1() {
        return colorID1;
    }

    public int colorID2() {
        return colorID2;
    }

    public int colorID3() {
        return colorID3;
    }

    //Точка попадает в контур элемента
    public boolean inside1(float x, float y) {
        if (((int) x2 | (int) y2) < 0) {
            return false;
        }
        if (x < x1 || y < y1) {
            return false;
        }
        return ((x2 < x1 || x2 >= x) && (y2 < y1 || y2 >= y));
    }

    public boolean inside(float x, float y) {
        double X = Math.floor(x), Y = Math.floor(y);
        double X1 = Math.floor(x1), X2 = Math.floor(x2), Y1 = Math.floor(y1), Y2 = Math.floor(y2);
        
        if (((int)X2 | (int)Y2) < 0) {
            return false;
        }
        if (X < X1 || Y < Y1) {
            return false;
        }
        return ((X2 >= X) && (Y2 >= Y));
    }

    public void paint() {
    }

    public void print() {
        System.out.println("ELEM: id=" + id + ", x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2=" + y2);
    }

    public String toString() {
        String art = (artiklRecAn == null) ? "null" : artiklRecAn.getStr(eArtikl.code);
        float ownerID = (owner == null) ? -1 : owner.id();
        return "art=" + art + ", type=" + type + ", layout=" + layout + ", owner=" + ownerID + ", id=" + id
                + ", x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2=" + y2 + ", width=" + width() + ", height=" + height();
    }

    public boolean equals(Object obj) {
        return id == ((Com5t) obj).id;
    }
}
