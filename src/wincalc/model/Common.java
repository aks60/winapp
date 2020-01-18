package wincalc.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import wincalc.constr.Specification;
import dataset.Record;
import domain.eArtikl;
import domain.eParams;
import enums.ParamJson;
import enums.TypeElem;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import wincalc.Wincalc;

public abstract class Common {

    public static final int SIDE_START = 1; //левая сторона
    public static final int SIDE_END = 2;   //правая сторона 
    protected static float moveXY = 40;     //смещение рисунка

    protected String id = "0"; //идентификатор
    protected AreaContainer owner = null; //владелец
    protected Wincalc iwin = null; //главный класс калькуляции 

    protected float x1 = 0;
    protected float y1 = 0;
    protected float x2 = 0;
    protected float y2 = 0;

    protected float width = 0;  //ширина
    protected float height = 0; //высота     

    protected int color1 = -1;  //базовый 
    protected int color2 = -1;  //внутренний
    protected int color3 = -1;  //внешний

    protected Record sysprofRec = null; //профиль в системе
    protected Record articlRec = null; //мат. средства, основной профиль
    protected Specification specificationRec = null; //спецификация элемента
    protected HashMap<ParamJson, Object> mapParam = new HashMap(); //параметры элемента       

    public String getId() {
        return id;
    }

    public AreaContainer root() {
        return iwin.rootArea;
    }

    public void dimension(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }


    public boolean inside(int x, int y) {
        return (x >= 0) && (x < width) && (y >= 0) && (y < height);
    }
    
    public float width() {
        return width;
    }

    public float height() {
        return height;
    }

    public float xy(int index) {
        float xy[] = {x1, y1, x2, y2};
        return xy[index - 1];
    }

    public int color(int index) {
        return (index == 1) ? color1 : (index == 2) ? color2 : color3;
    }

    public abstract TypeElem typeElem();

    public abstract LinkedList<Common> listChild();

    /**
     * Прорисовка элемента на холсте
     */
    public void drawElemList() {
    }

    /**
     * Инициализация pro4Params
     */
    protected void parsingParam(AreaContainer root, String paramJson) {
        try {
            Gson gson = new Gson();
            if (paramJson != null && paramJson.isEmpty() == false) {

                String str = paramJson.replace("'", "\"");
                JsonElement jsonElem = gson.fromJson(str, JsonElement.class);
                JsonObject jsonObj = jsonElem.getAsJsonObject();
                JsonArray jsonArr = jsonObj.getAsJsonArray(ParamJson.pro4Params.name());

                if (!jsonArr.isJsonNull() && jsonArr.isJsonArray()) {
                    mapParam.put(ParamJson.pro4Params, jsonObj.get(ParamJson.pro4Params.name())); //первый вариант    
                    HashMap<Integer, Object[]> mapValue = new HashMap();
                    for (int index = 0; index < jsonArr.size(); index++) {
                        JsonArray jsonRec = (JsonArray) jsonArr.get(index);
                        int pnumb = jsonRec.getAsInt();
                        String p1 = jsonRec.get(0).getAsString();
                        String p2 = jsonRec.get(1).getAsString();
                        Record rec = eParams.query.select(eParams.up, "where", eParams.numb, "=", p1, "and", eParams.mixt, "=", p2).get(0);
                        if (pnumb < 0 && rec != null) {
                            mapValue.put(pnumb, new Object[]{rec.get(eParams.name), rec.get(eParams.mixt), 0});
                        }
                    }
                    mapParam.put(ParamJson.pro4Params2, mapValue); //второй вариант                
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка Base.parsingParam() " + e);
        }
    }

    protected void strokePolygon(float x1, float x2, float x3, float x4, float y1,
            float y2, float y3, float y4, int rgbFill, Color rdbStroke, double lineWidth) {

        float scale = iwin.scale;
        Graphics2D gc = iwin.img.createGraphics();
        gc.setStroke(new BasicStroke((float) lineWidth)); //толщина линии
        gc.setColor(java.awt.Color.BLACK);
        float h = iwin.heightAdd - iwin.height;
        gc.drawPolygon(new int[]{(int) ((x1 + moveXY) * scale), (int) ((x2 + moveXY) * scale), (int) ((x3 + moveXY) * scale), (int) ((x4 + moveXY) * scale)},
                new int[]{(int) ((y1 + moveXY + h) * scale), (int) ((y2 + moveXY + h) * scale), (int) ((y3 + moveXY + h) * scale), (int) ((y4 + moveXY + h) * scale)}, 4);
        gc.setColor(new java.awt.Color(rgbFill & 0x000000FF, (rgbFill & 0x0000FF00) >> 8, (rgbFill & 0x00FF0000) >> 16));
        gc.fillPolygon(new int[]{(int) ((x1 + moveXY) * scale), (int) ((x2 + moveXY) * scale), (int) ((x3 + moveXY) * scale), (int) ((x4 + moveXY) * scale)},
                new int[]{(int) ((y1 + moveXY + h) * scale), (int) ((y2 + moveXY + h) * scale), (int) ((y3 + moveXY + h) * scale), (int) ((y4 + moveXY + h) * scale)}, 4);
    }

    protected void strokeArc(double x, double y, double w, double h, double startAngle,
            double arcExtent, ArcType closure, int rdbStroke, double lineWidth) {

        float scale = iwin.scale;
        Graphics2D gc = iwin.img.createGraphics();
        gc.setStroke(new BasicStroke((float) lineWidth * scale)); //толщина линии
        gc.setColor(new java.awt.Color(rdbStroke & 0x000000FF, (rdbStroke & 0x0000FF00) >> 8, (rdbStroke & 0x00FF0000) >> 16));
        gc.drawArc((int) ((x + moveXY) * scale), (int) ((y + moveXY) * scale), (int) (w * scale), (int) (h * scale), (int) startAngle, (int) arcExtent);
    }

    protected void fillArc(double x, double y, double w, double h, double startAngle, double arcExtent) {

        float scale = iwin.scale;
        Graphics2D gc = iwin.img.createGraphics();
        gc.setColor(new java.awt.Color(226, 255, 250));
        gc.fillArc((int) ((x + moveXY) * scale), (int) ((y + moveXY) * scale), (int) (w * scale), (int) (h * scale), (int) startAngle, (int) arcExtent);
    }

    protected void fillPoligon(float x1, float x2, float x3, float x4, float y1, float y2, float y3, float y4) {

        float scale = iwin.scale;
        Graphics2D gc = iwin.img.createGraphics();
        gc.setColor(new java.awt.Color(226, 255, 250));
        float h = iwin.heightAdd - iwin.height;
        gc.fillPolygon(new int[]{(int) ((x1 + moveXY) * scale), (int) ((x2 + moveXY) * scale), (int) ((x3 + moveXY) * scale), (int) ((x4 + moveXY) * scale)},
                new int[]{(int) ((y1 + moveXY + h) * scale), (int) ((y2 + moveXY + h) * scale), (int) ((y3 + moveXY + h) * scale), (int) ((y4 + moveXY + h) * scale)}, 4);
    }

    public boolean equals(Object obj) {
        return id == ((Common) obj).id;
    }
}
