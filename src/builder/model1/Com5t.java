package builder.model1;

import builder.Wincalc;
import builder.IArea5e;
import builder.IElem5e;
import builder.ICom5t;
import com.google.gson.JsonObject;
import dataset.Record;
import enums.Layout;
import builder.script.GsonElem;
import domain.eArtikl;
import enums.Type;
import java.util.Arrays;
import java.util.List;

public class Com5t implements ICom5t {

    private double id = -1; //идентификатор 
    private Type type = Type.NONE; //Тип элемента или окна  
    protected Layout layout = Layout.FULL; //направление(AREA) сторона(ELEM) - расположения компонентов ...

    protected Record sysprofRec = null; //профиль в системе
    protected Record artiklRec = null;  //мат. средства
    protected Record artiklRecAn = null;  //аналог мат. средства    

    protected IArea5e owner = null; //владелец
    protected Wincalc winc = null; //главный класс калькуляции
    protected IArea5e root = null; //главный класс конструкции
    public GsonElem gson = null; //Gson object конструкции

    protected double x1 = 0, y1 = 0, x2 = 0, y2 = 0;//координаты area     
    protected int colorID1 = -1, colorID2 = -1, colorID3 = -1; //1-базовый 2-внутренний 3-внешний     

    public Com5t(Type type) {
        this.type = type;
    }

    public Com5t(double id, Wincalc winc, IArea5e owner, GsonElem gson) {
        this.id = id;
        this.owner = owner;
        this.type = gson.type();
        this.winc = winc;
        this.root = winc.rootArea;
        this.gson = gson;
        if (this.type() == Type.STVORKA && owner.type() == Type.STVORKA) {
            this.type = Type.STVORKA_SIDE; //фича створки, см.конструктор ElemFrame
        }
    }

    @Override
    public double id() {
        return id;
    }

    @Override
    public IArea5e owner() {
        return owner;
    }

    @Override
    public IArea5e root() {
        return winc.rootArea;
    }

    public Wincalc winc() {
        return winc;
    }

    @Override
    public GsonElem gson() {
        return gson;
    }

    @Override
    public void setDimension(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
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

    @Override
    public boolean isJson(JsonObject jso) {
        if (jso == null || "".equals(jso)) {
            return false;
        }
        return !jso.isJsonNull();
    }

    @Override
    public Type type() {
        return type;
    }

    @Override
    public Layout layout() {
        return layout;
    }

    @Override
    public Record sysprofRec() {
        return sysprofRec;
    }

    @Override
    public Record artiklRec() {
        return artiklRec;
    }

    @Override
    public void artiklRec(Record record) {
        this.artiklRec = record;
    }

    @Override
    public Record artiklRecAn() {
        return artiklRecAn;
    }

    @Override
    public void artiklRecAn(Record record) {
        this.artiklRecAn = record;
    }

    /**
     * Длина компонента
     */
    @Override
    public double length() {
        IElem5e elem5e = (IElem5e) this;
        if (elem5e.anglHoriz() == 0 || elem5e.anglHoriz() == 180) {
            return (x2 > x1) ? x2 - x1 : x1 - x2;
        } else if (elem5e.anglHoriz() == 90 || elem5e.anglHoriz() == 270) {
            return (y2 > y1) ? y2 - y1 : y1 - y2;
        } else {
            return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
        }
    }

    /**
     * Ширина в gson
     */
    @Override
    public double lengthX() {
        return (this == winc.rootArea) ? this.gson.width() : this.gson.length();
    }

    //Высота в gson
    @Override
    public double lengthY() {
        return (this == winc.rootArea) ? this.gson.height() : this.gson.length();
    }

    @Override
    public double x1() {
        return x1;
    }

    @Override
    public double y1() {
        return y1;
    }

    @Override
    public double x2() {
        return x2;
    }

    @Override
    public double y2() {
        return y2;
    }

    @Override
    public Double width() {
        return (x2 > x1) ? x2 - x1 : x1 - x2;
    }

    @Override
    public Double height() {
        return (y2 > y1) ? y2 - y1 : y1 - y2;
    }

    @Override
    public int colorID1() {
        return colorID1;
    }

    @Override
    public int colorID2() {
        return colorID2;
    }

    @Override
    public int colorID3() {
        return colorID3;
    }

    /**
     * Точка попадает в контур четырёхугольника
     */
    @Override
    public boolean inside(double x, double y) {
        int X = (int) x, Y = (int) y;
        int X1 = (int) x1, Y1 = (int) y1, X2 = (int) x2, Y2 = (int) y2;

        if ((X2 | Y2) < 0) {
            return false;
        }

        if (x1 > x2) {
            X1 = (int) x2;
            X2 = (int) x1;
        }

        if (y1 > y2) {
            Y1 = (int) y2;
            Y2 = (int) y1;
        }

        if (X < X1 || Y < Y1) {
            return false;
        }
        return ((X2 >= X) && (Y2 >= Y));
    }

    @Override
    public void paint() {
    }

    public void print() {
        System.out.println("ELEM: id=" + id + ", x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2=" + y2);
    }

    @Override
    public String toString() {
        String art = (artiklRecAn == null) ? "null" : artiklRecAn.getStr(eArtikl.code);
        double ownerID = (owner == null) ? -1 : owner.id();
        return "art=" + art + ", type=" + type + ", layout=" + layout + ", owner=" + ownerID + ", id=" + id
                + ", x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2=" + y2 + ", width=" + width() + ", height=" + height();
    }

    @Override
    public boolean equals(Object obj) {
        return id == ((Com5t) obj).id;
    }

// <editor-fold defaultstate="collapsed" desc="inside2 см.инет Задача о принадлежности точки многоугольнику"> 
//    public boolean inside2(double x, double y) {
//        int X = (int) x, Y = (int) y;
//        //int X1 = (int) x1, X2 = (int) x2, Y1 = (int) y1, Y2 = (int) y2;
//        //int xp[] = {X1, X2, X2, X1}; // массив X-координат полигона 
//        //int yp[] = {Y1, Y1, Y2, Y2}; // массив Y-координат полигона 
//        
//        int xp[] = {4, 800, 800, 4}, yp[] = {4, 4, 20, 20}; //test  
//        int j = xp.length - 1;
//        boolean result = false;
//        for (int i = 0; i < 4; ++i) {
//            if ((((yp[i] <= Y) && (Y < yp[j])) || ((yp[j] <= Y) && (Y < yp[i])))
//                    && ((X > (xp[j] - xp[i]) * (Y - yp[i]) / (yp[j] - yp[i]) + xp[i]))) {
//                result = !result;
//            }
//            j = i;
//        }
//        return result;
//    } 
//    //http://sbp-program.ru/java/sbp-graphics.htm
//    //принадлежит многоугольнику    
//    Polygon poly = new Polygon(arrayX, arrayY, 8);
//    g.drawPolygon(poly);
//    Point aPoint = new Point(50, 190);
//    if(poly.contains(aPoint))
//    {g.drawString(«Yes», 50, 190);}      
// </editor-fold>      
}
