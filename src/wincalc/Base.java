package wincalc;

import enums.eLayoutArea;
import enums.eTypeElem;
import java.util.LinkedList;
import java.util.Map;

public abstract class Base {

    protected String id = "0"; //идентификатор
    protected Wincalc iwin = null; //главный класс калькуляции
    public static final int SIDE_START = 1; //левая сторона
    public static final int SIDE_END = 2; //правая сторона  
    
    protected float x1 = 0;
    protected float y1 = 0;
    protected float x2 = 0;
    protected float y2 = 0;
    
    protected float width = 0;  //ширина
    protected float height = 0; //высота  
    
    protected int color1 = -1;  //базовый 
    protected int color2 = -1;  //внутренний
    protected int color3 = -1;  //внешний

    public String getId() {
        return id;
    }
    public Wincalc iwin() {
        return iwin;
    }
    
     public AreaBase root() {
         return iwin.rootArea;
     }
     
    public float width() {
        return width;
    }

    public float height() {
        return width;
    }
    
    public void dimension(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    } 
    
    public float xy(int index) {
        float xy[] = {x1, y1, x2, y2};
        return xy[index - 1];
    } 
    
    public int color(int index) {
        return (index == 1) ? color1 : (index == 2) ? color2 : color3;
    } 
    
    public abstract eTypeElem typeElem();
    
    public abstract LinkedList<Base> listChild();
}
