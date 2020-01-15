package wincalc;

import constr.Specification;
import dataset.Record;
import domain.eArtikls;
import enums.TypeElem;
import java.util.LinkedList;

public abstract class Base {

    public static final int SIDE_START = 1; //левая сторона
    public static final int SIDE_END = 2; //правая сторона 

    protected String id = "0"; //идентификатор
    protected AreaBase owner = null; //владелец
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

    public String getId() {
        return id;
    }

    public AreaBase root() {
        return iwin.rootArea;
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

    public abstract TypeElem typeElem();

    public abstract LinkedList<Base> listChild();

}
