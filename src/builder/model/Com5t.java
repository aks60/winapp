package builder.model;

import com.google.gson.Gson;
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
    public Type type = Type.NONE; //Тип элемента или конструкции  
    public Layout layout = Layout.FULL; //направление(AREA) сторона(ELEM) - расположения компонентов ...
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
        this.winc = winc;
        this.root = winc.rootArea;
        this.gson = gson;
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

    public void length(float v) {
        ElemSimple elem5e = (ElemSimple) this;
        
        //По горизонтали
        if (elem5e.anglHoriz == 0 || elem5e.anglHoriz == 180) {
            if (id == root.id()) {
                float k = v / gson.width(); //коэффициент
                winc.rootGson.width(v);
                winc.listSortAr.forEach(e -> {
                    if (e.layout == Layout.HORIZ) {
                        e.childs.forEach(e2 -> { //изменение всех по ширине
                            e2.gson.length(k * e2.gson.length());
                        });
                    }
                });
            } else {
                float k = v / this.gson.length(); //коэффициент
                gson.length(v);
                ((AreaSimple) this).childs.forEach(e -> {
                    if (e.owner.layout == Layout.HORIZ && (e.type == Type.AREA || e.type == Type.STVORKA)) {
                        e.length(k * e.length()); //рекурсия изменения детей

                    } else {
                        ((AreaSimple) e).childs.forEach(e2 -> {
                            if (e2.owner.layout == Layout.HORIZ && (e2.type == Type.AREA || e2.type == Type.STVORKA)) {
                                e2.length(k * e2.length()); //рекурсия изменения детей
                            }
                        });
                    }
                });
            }
            
            //По вертикали
        } else if (elem5e.anglHoriz == 90 || elem5e.anglHoriz == 270) {
            if (id == root.id()) {
                float k = v / gson.height(); //коэффициент
                winc.rootGson.height(v);
                winc.rootGson.heightAdd(k * winc.rootGson.heightAdd());                
                winc.listSortAr.forEach(e -> {
                    if (e.layout == Layout.VERT) {
                        e.childs.forEach(e2 -> { //изменение всех по ширине
                            e2.gson.length(k * e2.gson.length());
                        });
                    }
                });
            } else {
                float k = v / this.gson.length(); //коэффициент       
                gson.length(v);                             
                ((AreaSimple) this).childs.forEach(e -> {
                    if (e.owner.layout == Layout.VERT && (e.type == Type.AREA || e.type == Type.STVORKA)) {
                        e.length(k * e.length()); //рекурсия изменения детей

                    } else {
                        ((AreaSimple) e).childs.forEach(e2 -> {
                            if (e2.owner.layout == Layout.VERT && (e2.type == Type.AREA || e2.type == Type.STVORKA)) {
                                e2.length(k * e2.length()); //рекурсия изменения детей
                            }
                        });
                    }
                });
            }
        }
    }

    public int index() {
        if (owner != null) {
            for (int index = 0; index < owner.childs.size(); ++index) {
                if (owner.childs.get(index) == this) {
                    return index;
                }
            }
        }
        return -1;
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
    public boolean inside(float X, float Y) {
        if (((int) x2 | (int) y2) < 0) {
            return false;
        }
        if (X < x1 || Y < y1) {
            return false;
        }
        //return ((x2 < x1 || x2 >= X) && (y2 < y1 || y2 >= Y));
        return ((x2 >= X) && (y2 >= Y));
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
