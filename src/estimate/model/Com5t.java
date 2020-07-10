package estimate.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import estimate.constr.Specification;
import dataset.Record;
import domain.eParams;
import enums.LayoutArea;
import enums.ParamJson;
import enums.TypeArtikl;
import enums.TypeElem;
import java.awt.BasicStroke;
import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import estimate.Wincalc;

public abstract class Com5t {

    public static final int TRANSLATE_X = 20; //сдвиг графика   
    public static final int TRANSLATE_Y = 20; //сдвиг графика    
    public static final int SPACE_DX = 200;   //пространство для линий    
    public static final int SPACE_DY = 240;   //пространство для линий              

    protected TypeElem type = TypeElem.NONE; //Тип элемента   
    protected LayoutArea layout = LayoutArea.FULL; //направление(AREA) сторона(ELEM) расположения компонентов ...

    private float id = -1; //идентификатор    
    private AreaSimple owner = null; //владелец
    public LinkedList<Com5t> listChild = new LinkedList(); //дети
    private Wincalc iwin = null; //главный класс калькуляции

    protected float x1 = 0, y1 = 0, x2 = 0, y2 = 0; //координаты area     
    public int color1 = -1, color2 = -1, color3 = -1; //1-базовый 2-внутренний 3-внешний 

    public HashMap<Integer, Record> mapParamUse = new HashMap(); //клиентские параметры       

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

    public float length() {
        if (TypeElem.FRAME_SIDE == type() || TypeElem.STVORKA_SIDE == type()) {
            return (LayoutArea.TOP == layout() || LayoutArea.BOTTOM == layout()) ? x2 - x1 : y2 - y1;
        }
        return (LayoutArea.HORIZ == owner.layout()) ? x2 - x1 : y2 - y1;
    }

    public float width() {
        return x2 - x1;
    }

    public float height() {
        return y2 - y1;
    }

    protected void parsing(String param) {
        try {
            if (param != null && param.isEmpty() == false && param.equals("null") == false) {
                String str = param.replace("'", "\"");
                JsonObject jsonObj = new Gson().fromJson(str, JsonObject.class);
                JsonArray jsonArr = jsonObj.getAsJsonArray(ParamJson.ioknaParam.name());
                if (jsonArr != null && !jsonArr.isJsonNull() && jsonArr.isJsonArray()) {
                    jsonArr.forEach(it -> {
                        Record paramRec = eParams.find(it.getAsJsonArray().get(0).getAsInt(), it.getAsJsonArray().get(1).getAsInt());
                        mapParamUse.put(paramRec.getInt(eParams.grup), paramRec);
                    });
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка Com5t.parsingParam() " + e);
        }
    }

    public TypeElem type() {
        return type;
    }

    public LayoutArea layout() {
        return layout;
    }

    //Точка попадает в контур элемента
    public boolean inside(float x, float y) {
        if (((int) x2 | (int) y2) < 0) {
            return false;
        }
        if (x < x1 || y < y1) {
            return false;
        }
        return ((x2 < x1 || x2 >= x) && (y2 < y1 || y2 >= y));
    }

    public void paint() {
    }

    public void print() {
        System.out.println("ELEM: id=" + id + ", x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2=" + y2);
    }

    public String toString() {
        float ownerID = (owner == null) ? -1 : owner.id();
        return "ELEM " + type.name() + ", owner=" + ownerID + ", id=" + id + ", x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2=" + y2;
    }

    public boolean equals(Object obj) {
        return id == ((Com5t) obj).id;
    }
}
