package wincalc.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import wincalc.constr.Specification;
import dataset.Record;
import domain.eParams;
import enums.ParamJson;
import enums.TypeElem;
import java.util.HashMap;
import java.util.LinkedList;
import wincalc.Wincalc;

public abstract class Com5t {

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

    public Com5t(String id) {
        this.id = id;
        specificationRec = new Specification(id, this);
    }

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

    public abstract LinkedList<Com5t> listChild();

    /**
     * Прорисовка элемента на холсте
     */
    //TODO переименовать в paint()
    public void drawElemList() {
    }

    /**
     * Инициализация pro4Params
     */
    protected void parsing(String param) {
        try {
            Gson gson = new Gson();
            if (param != null && param.isEmpty() == false) {

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
            System.err.println("Ошибка Base.parsingParam() " + e);
        }
    }

    public boolean equals(Object obj) {
        return id == ((Com5t) obj).id;
    }
}
