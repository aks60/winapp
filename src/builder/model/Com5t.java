package builder.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataset.Record;
import enums.Layout;
import builder.Wincalc;
import enums.Type;
import java.util.Comparator;

public abstract class Com5t {

    public static final int TRANSLATE_XY = 2; //сдвиг графика    
    public static final int SPACE_DX = 200;   //пространство для линий    
    public static final int SPACE_DY = 240;   //пространство для линий              

    private float id = -1; //идентификатор 
    public Type type = Type.NONE; //Тип элемента или конструкции  
    public Layout layout = Layout.FULL; //направление(AREA) сторона(ELEM) - расположения компонентов ...
    public Record sysprofRec = null; //профиль в системе
    public Record artiklRec = null;  //мат. средства
    public Record artiklRecAn = null;  //аналог мат. средства    

    public AreaSimple owner = null; //владелец
    public Wincalc iwin = null; //главный класс калькуляции
    public AreaSimple root = null; //главный класс конструкции

    public float x1 = 0, x2 = 0, y1 = 0, y2 = 0;//координаты area     
    public int colorID1 = -1, colorID2 = -1, colorID3 = -1; //1-базовый 2-внутренний 3-внешний     

    public Com5t(Type type) {
        this.type = type;
    }

    public Com5t(float id, Wincalc iwin, AreaSimple owner) {
        this.id = id;
        this.owner = owner;
        this.iwin = iwin;
        this.root = iwin.rootArea;
    }

    public float id() {
        return id;
    }

    public AreaSimple owner() {
        return owner;
    }

    public AreaSimple root() {
        return iwin.rootArea;
    }

    public void setDimension(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public int param(String par, String key) {

        if (par != null && par.isEmpty() == false && par.equals("{}") == false) {
            JsonObject jsonObj = new Gson().fromJson(par, JsonObject.class);
            return (jsonObj.get(key) == null) ? -1 : jsonObj.get(key).getAsInt();
        }
        return -1;
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

    public float width() {
        return (x2 > x1) ? x2 - x1 : x1 - x2;
    }

    public float height() {

        return (y2 > y1) ? y2 - y1 : y1 - y2;
    }

    //Тип ElemXxx или тип AreaXxx
    public Type type() {
        return type;
    }

    //Расположение элемента
    public Layout layout() {
        return layout;
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
        float ownerID = (owner == null) ? -1 : owner.id();
        return "type=" + type() + ", layout=" + layout() + ", owner=" + ownerID + ", id=" + id
                + ", x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2=" + y2 + ", width=" + width() + ", height=" + height();
    }

    public boolean equals(Object obj) {
        return id == ((Com5t) obj).id;
    }
}
