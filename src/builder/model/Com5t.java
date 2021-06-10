package builder.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataset.Record;
import enums.LayoutArea;
import enums.TypeElem;
import java.util.LinkedList;
import builder.Wincalc;
import builder.making.Uti3;

public abstract class Com5t {

    public static final int TRANSLATE_XY = 2; //сдвиг графика    
    public static final int SPACE_DX = 200;   //пространство для линий    
    public static final int SPACE_DY = 240;   //пространство для линий              

    protected TypeElem type = TypeElem.NONE; //Тип элемента   
    protected LayoutArea layout = LayoutArea.FULL; //направление(AREA) сторона(ELEM) расположения компонентов ...
    public Record sysprofRec = null; //профиль в системе
    public Record artiklRec = null;  //мат. средства
    public Record artiklRecAn = null;  //аналог мат. средства    

    private float id = -1; //идентификатор    
    protected AreaSimple owner = null; //владелец
    private Wincalc iwin = null; //главный класс калькуляции

    public float x1 = 0, y1 = 0, x2 = 0, y2 = 0; //координаты area     
    public int colorID1 = -1, colorID2 = -1, colorID3 = -1; //1-базовый 2-внутренний 3-внешний     

    public Com5t(TypeElem type) {
        this.type = type;
    }

    public Com5t(float id, Wincalc iwin, AreaSimple owner) {
        this.id = id;
        this.owner = owner;
        this.iwin = iwin;
    }

    public float id() {
        return id;
    }

    public AreaSimple owner() {
        return owner;
    }

    public Wincalc iwin() {
        return iwin;
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

    public int param(String param, String key) {

        if (param != null && param.isEmpty() == false) {
            JsonObject jsonObj = new Gson().fromJson(param, JsonObject.class);
            return (jsonObj.get(key) == null) ? -1 : jsonObj.get(key).getAsInt();
        }
        return -1;
    }

    public float length() {
        if (TypeElem.FRAME_SIDE == type() || TypeElem.STVORKA_SIDE == type()) {
            return (LayoutArea.TOP == layout() || LayoutArea.BOTT == layout()) ? x2 - x1 : y2 - y1;
        }
        return (LayoutArea.HORIZ == layout()) ? x2 - x1 : y2 - y1;
    }

    public float width() {
        return x2 - x1;
    }

    public float height() {
        return y2 - y1;
    }

    public TypeElem type() {
        return type;
    }

    public LayoutArea layout() {
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
        return ((x2 < x1 || x2 >= X) && (y2 < y1 || y2 >= Y));
    }

    public void paint() {
    }

    public void print() {
        System.out.println("ELEM: id=" + id + ", x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2=" + y2);
    }

    public String toString() {
        float ownerID = (owner == null) ? -1 : owner.id();
        return "ELEM " + type.name() + ", layout=" + layout() + ", owner=" + ownerID + ", id=" + id
                + ", x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2=" + y2 + ", width=" + width() + ", height=" + height();
    }

    public boolean equals(Object obj) {
        return id == ((Com5t) obj).id;
    }
}
