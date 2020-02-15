package wincalc.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import wincalc.constr.Specification;
import dataset.Record;
import domain.eParams;
import enums.LayoutArea;
import enums.ParamJson;
import enums.TypeElem;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.LinkedList;
import javafx.scene.shape.ArcType;
import wincalc.Wincalc;

public abstract class Com5t {

    public static final int SIDE_START = 1;   //левая сторона
    public static final int SIDE_END = 2;     //правая сторона     
    public static final int TRANSLATE_X = 80; //сдвиг графика   
    public static final int TRANSLATE_Y = 20; //сдвиг графика    
    public static final int SPACE_DX = 200;   //пространство для линий    
    public static final int SPACE_DY = 240;   //пространство для линий   

    protected TypeElem typeElem = TypeElem.NONE;
    private LinkedList<Com5t> listChild = new LinkedList(); //список компонентов в окне
    protected LayoutArea layout = LayoutArea.FULL; //направление(AREA) сторона(ELEM) расположения компонентов

    protected int id = -1; //идентификатор
    protected AreaSimple owner = null; //владелец
    protected Wincalc iwin = null; //главный класс калькуляции 

    protected float x1 = 0, y1 = 0, x2 = 0, y2 = 0; //координаты area     
    protected int color1 = -1, color2 = -1, color3 = -1; //1-базовый 2-внутренний 3-внешний 

    protected Record sysprofRec = null; //профиль в системе
    protected Record artiklRec = null;  //мат. средства, основной профиль
    protected Specification specificationRec = null; //спецификация элемента
    protected HashMap<ParamJson, Object> mapParam = new HashMap(); //параметры элемента       

    public Com5t(int id) {
        this.id = id;
        specificationRec = new Specification(id, this);
    }

    public int getId() {
        return id;
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

    public float xy(int index) {
        float xy[] = {x1, y1, x2, y2};
        return xy[index - 1];
    }

    public float width() {
        return x2 - x1;
    }

    public float height() {
        return y2 - y1;
    }

    protected void parsing(String param) {
        try {
            Gson gson = new Gson();
            if (param != null && param.isEmpty() == false && param.equals("null") == false) {

                String str = param.replace("'", "\"");
                JsonElement jsonElem = gson.fromJson(str, JsonElement.class);
                JsonObject jsonObj = jsonElem.getAsJsonObject();
                JsonArray jsonArr = jsonObj.getAsJsonArray(ParamJson.pro4Params.name());

                if (jsonArr != null && jsonArr.isJsonArray()) {
                    mapParam.put(ParamJson.pro4Params, jsonObj.get(ParamJson.pro4Params.name())); //первый вариант    
                    HashMap<Integer, Object[]> mapValue = new HashMap();
                    for (int index = 0; index < jsonArr.size(); index++) {
                        JsonArray jsonRec = (JsonArray) jsonArr.get(index);
                        int p1 = jsonRec.get(0).getAsInt();
                        int p2 = jsonRec.get(1).getAsInt();
                        Record record = eParams.find(p1, p2);
                        if (p1 < 0 && record != null) {
                            mapValue.put(p1, new Object[]{record.get(eParams.name), record.get(eParams.mixt), 0});
                        }
                    }
                    mapParam.put(ParamJson.pro4Params2, mapValue); //второй вариант                
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка Com5t.parsingParam() " + e);
        }
    }

    public LinkedList<Com5t> listChild() {
        return listChild;
    }

    public TypeElem typeElem() {
        return typeElem;
    }

    public LayoutArea layout() {
        return layout;
    }

    public void paint() {
    }

    protected void strokePolygon(float x1, float x2, float x3, float x4, float y1,
            float y2, float y3, float y4, int rgbFill, Color rdbStroke) {
        iwin.gc2d.setStroke(new BasicStroke(8)); //толщина линии
        iwin.gc2d.setColor(rdbStroke);
        float dy = iwin.heightAdd - iwin.height;
        iwin.gc2d.drawPolygon(new int[]{(int) x1, (int) x2, (int) x3, (int) x4},
                new int[]{(int) (y1 + dy), (int) (y2 + dy), (int) (y3 + dy), (int) (y4 + dy)}, 4);
        iwin.gc2d.setColor(new java.awt.Color(rgbFill & 0x000000FF, (rgbFill & 0x0000FF00) >> 8, (rgbFill & 0x00FF0000) >> 16));
        //iwin.gc2d.setColor(new java.awt.Color(rgbFill, false));
        iwin.gc2d.fillPolygon(new int[]{(int) x1, (int) x2, (int) x3, (int) x4},
                new int[]{(int) (y1 + dy), (int) (y2 + dy), (int) (y3 + dy), (int) (y4 + dy)}, 4);
    }

    protected void strokeArc(double x, double y, double w, double h, double startAngle,
            double arcExtent, ArcType closure, int rdbStroke, double lineWidth) {
        iwin.gc2d.setStroke(new BasicStroke((float) lineWidth)); //толщина линии
        iwin.gc2d.setColor(new java.awt.Color(rdbStroke & 0x000000FF, (rdbStroke & 0x0000FF00) >> 8, (rdbStroke & 0x00FF0000) >> 16));
        //iwin.gc2d.setColor(new java.awt.Color(rdbStroke, false));
        iwin.gc2d.drawArc((int) x, (int) y, (int) w, (int) h, (int) startAngle, (int) arcExtent);
    }

    public void print() {
        System.out.println("ELEM: id=" + id + ", x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2=" + y2);
    }

    public String toString() {
        //TODO owner не должен быть null
        int ownerID = (owner == null) ? -1 : owner.id;
        return "ELEM: owner=" + ownerID + ", id=" + id + ", x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2=" + y2;
    }

    public boolean equals(Object obj) {
        return id == ((Com5t) obj).id;
    }
}
