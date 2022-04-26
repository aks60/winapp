package builder.script;

import frames.swing.draw.Scale;
import builder.Wincalc;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import enums.Form;
import enums.Layout;
import enums.Type;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import startup.Main;

public class GsonRoot extends GsonElem {

    public String name = "Конструкция";
    public int prj = 1; //PNUMB - номер тестируемого проекта, поле пока нужно только для тестов 
    public int ord = 1; //ONUMB - номер тестируемого заказа, поле пока нужно только для тестов 
    private Integer nuni = -3;  //nuni профиля (PRO4_SYSPROF.NUNI)
    protected Float width = null; //ширина area, мм.
    protected Float widthAdd = null;  //дополнительная ширина, мм.
    protected Float height = null; //высота area, мм 
    protected Float heightAdd = null;  //дополнительная высота, мм.
    public Integer color1 = -3;  //основная текстура
    public Integer color2 = -3;  //внутренняя текстура
    public Integer color3 = -3;  //внешняя текстура    

    public GsonRoot(int prj, int ord, int nuni, String name, Layout layout, Type type, float width, float height, int color1, int color2, int color3) {
        init(prj, ord, nuni, name, layout, type, null, width, height, 0, color1, color2, color3, null);
    }

    public GsonRoot(int prj, int ord, int nuni, String name, Layout layout, Type type, float width, float height, int color1, int color2, int color3, String paramJson) {
        init(prj, ord, nuni, name, layout, type, null, width, height, 0, color1, color2, color3, paramJson);
    }

    public GsonRoot(int prj, int ord, int nuni, String name, Layout layout, Type type, float width, float height1, float height2, int color1, int color2, int color3) {
        init(prj, ord, nuni, name, layout, type, null, width, height1, height2, color1, color2, color3, null);
    }

    public GsonRoot(int prj, int ord, int nuni, String name, Layout layout, Type type, Form form, float width, float height1, float height2, int color1, int color2, int color3) {
        init(prj, ord, nuni, name, layout, type, form, width, height1, height2, color1, color2, color3, null);
    }

    public void init(int prj, int ord, int nuni, String name, Layout layout, Type type, Form form, float width1, float height1, float height2, int color1, int color2, int color3, String paramJson) {
        super.genId = 0;
        super.id = 0;
        this.prj = prj;
        this.ord = ord;
        this.nuni = nuni;
        this.name = name;
        this.layout = layout;
        this.type = type;
        this.form = form;
        this.width = width1;
        this.height = height1;
        //this.widthAdd = (width2 == 0) ?null :width2;
        this.heightAdd = (height2 == 0) ? null : height2;
        this.length = null;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        if (paramJson != null) {
            this.param = new Gson().fromJson(paramJson, JsonObject.class);
        }
    }

    public void propery(String prj, int nuni, String name) {
        this.nuni = nuni;
        this.prj = Integer.valueOf(prj);
        this.name = (name == null) ? this.name : name;
        if (nuni == -3 && Main.dev == false) {
            this.color1 = -3;
            this.color2 = -3;
            this.color3 = -3;
        }
    }

    public int nuni() {
        return nuni;
    }

    public Float height() {
        return height;
    }

    public void height(float h) {
        height = h;
    }
    
    public Float widthAdd() {
        return widthAdd;
    }
    
    public void widthAdd(float w) {
        widthAdd = w;
    }
    
    public Float heightAdd() {
        return heightAdd;
    }

    public void heightAdd(float h) {
        heightAdd = h;
    }

    public Float width() {
        return width;
    }

    public void width(float w) {
        width = w;
    }
}
