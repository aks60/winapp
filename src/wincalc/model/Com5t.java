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
import java.util.HashMap;
import java.util.LinkedList;
import wincalc.Wincalc;

public abstract class Com5t {

    public static final int SIDE_START = 1;   //левая сторона
    public static final int SIDE_END = 2;     //правая сторона     
    public static final int TRANSLATE_X = 20; //сдвиг графика   
    public static final int TRANSLATE_Y = 20; //сдвиг графика    
    public static final int SPACE_DX = 200;   //пространство для линий    
    public static final int SPACE_DY = 240;   //пространство для линий              
    
    protected TypeElem type = TypeElem.NONE; //Тип элемента   
    protected LayoutArea layout = LayoutArea.FULL; //направление(AREA) сторона(ELEM) расположения компонентов

    private float id = -1; //идентификатор    
    private AreaSimple owner = null; //владелец
    public LinkedList<Com5t> listChild = new LinkedList(); //дети
    private Wincalc iwin = null; //главный класс калькуляции

    protected float x1 = 0, y1 = 0, x2 = 0, y2 = 0; //координаты area     
    public int color1 = -1, color2 = -1, color3 = -1; //1-базовый 2-внутренний 3-внешний 

    public HashMap<ParamJson, Object> mapParam = new HashMap(); //параметры элемента       

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
                            mapValue.put(p1, new Object[]{record.get(eParams.text), record.get(eParams.numb), 0});
                        }
                    }
                    mapParam.put(ParamJson.pro4Params2, mapValue); //второй вариант                
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

    public void setStroke(int s) {
        int sw = (int)(s + 1 - iwin.scale2 * .37);
        iwin.gc2d.setStroke(new BasicStroke(sw));
    }

    public void drawLine(float x1, float y1, float x2, float y2) {
        iwin.gc2d.drawLine((int) (x1 / iwin.scale2), (int) (y1 / iwin.scale2), (int) (x2 / iwin.scale2), (int) (y2 / iwin.scale2));
    }

    public void rotate(double theta, double x, double y) {
        iwin.gc2d.rotate(theta, x / iwin.scale2, y / iwin.scale2);
    }

    public void drawString(String str, float x, float y) {
        iwin.gc2d.drawString(str, x / iwin.scale2, y / iwin.scale2);
    }

    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        for (int i = 0; i < xPoints.length; i++) {
            xPoints[i] = (int) (xPoints[i] / iwin.scale2);
        }
        for (int i = 0; i < yPoints.length; i++) {
            yPoints[i] = (int) (yPoints[i] / iwin.scale2);
        }
        iwin.gc2d.drawPolygon(xPoints, yPoints, nPoints);
    }

    public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        for (int i = 0; i < xPoints.length; i++) {
            xPoints[i] = (int) (xPoints[i] / iwin.scale2);
        }
        for (int i = 0; i < yPoints.length; i++) {
            yPoints[i] = (int) (yPoints[i] / iwin.scale2);
        }
        iwin.gc2d.fillPolygon(xPoints, yPoints, nPoints);
    }

    public void drawArc(float x, float y, float width, float height, float startAngle, float arcAngle) {
        iwin.gc2d.drawArc((int) (x / iwin.scale2), (int) (y / iwin.scale2), (int) (width / iwin.scale2), (int) (height / iwin.scale2), (int) (startAngle / iwin.scale2), (int) (arcAngle / iwin.scale2));
    }

    public void fillArc(float x, float y, float width, float height, float startAngle, float arcAngle) {
        iwin.gc2d.fillArc((int) (x / iwin.scale2), (int) (y / iwin.scale2), (int) (width / iwin.scale2), (int) (height / iwin.scale2), (int) startAngle, (int) arcAngle);;
    }

    protected void strokePolygon(float x1, float x2, float x3, float x4, float y1,
            float y2, float y3, float y4, int rgbFill, Color rdbStroke) {
        setStroke(8); //толщина линии
        iwin.gc2d.setColor(rdbStroke);
        float sc = iwin.scale2;
        float dy = (iwin.heightAdd - iwin.height) / sc;
        iwin.gc2d.drawPolygon(new int[]{(int) (x1 / sc), (int) (x2 / sc), (int) (x3 / sc), (int) (x4 / sc)},
                new int[]{(int) (y1 / sc + dy), (int) (y2 / sc + dy), (int) (y3 / sc + dy), (int) (y4 / sc + dy)}, 4);
        iwin.gc2d.setColor(new java.awt.Color(rgbFill & 0x000000FF, (rgbFill & 0x0000FF00) >> 8, (rgbFill & 0x00FF0000) >> 16));
        iwin.gc2d.fillPolygon(new int[]{(int) (x1 / sc), (int) (x2 / sc), (int) (x3 / sc), (int) (x4 / sc)},
                new int[]{(int) (y1 / sc + dy), (int) (y2 / sc + dy), (int) (y3 / sc + dy), (int) (y4 / sc + dy)}, 4);
    }

    protected void strokeArc(double x, double y, double w, double h, double startAngle,
            double arcExtent, int rdbStroke, double lineWidth) {
        float sc = iwin.scale2;
        int sw = (int)(lineWidth /iwin.scale2);
        iwin.gc2d.setStroke(new BasicStroke(sw)); //толщина линии
        iwin.gc2d.setColor(new java.awt.Color(rdbStroke & 0x000000FF, (rdbStroke & 0x0000FF00) >> 8, (rdbStroke & 0x00FF0000) >> 16));
        iwin.gc2d.drawArc((int) (x / sc), (int) (y / sc), (int) (w / sc), (int) (h / sc), (int) startAngle, (int) arcExtent);
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
