package wincalc;

import java.awt.Component;

public abstract class ElemBase extends Component implements IBase {

    protected String id = "0"; //идентификатор
    protected int width = 0; //ширина
    protected int height = 0; //высота
    protected int x1 = 0;
    protected int y1 = 0;
    protected int x2 = 0;
    protected int y2 = 0;
    protected int color1 = -1; //базовый 
    protected int color2 = -1; //внутренний
    protected int color3 = -1; //внешний

    public ElemBase(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public int width() {
        return width;
    }

    public int height() {
        return width;
    }

    public int getXY(int index) {
        int xy[] = {x1, y1, x2, y2};
        return xy[index - 1];
    }

    public int color(int index) {
        return (index == 1) ? color1 : (index == 2) ? color2 : color3;
    }    
    
}
