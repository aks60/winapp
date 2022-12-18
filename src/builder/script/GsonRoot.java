package builder.script;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import common.JsonSerializer2;
import common.eProp;
import enums.Form;
import enums.Layout;
import enums.Type;

/**
 * Класс создаётся из см. пакет builder.script.test или скрипта из бд, после
 * трансформации класс генерит json скрипт конструкции в бд.
 */
public class GsonRoot extends GsonElem {

    private String version = "1.0";
    private String name = "Конструкция";
    private Integer prj = null; //PNUMB - номер тестируемого проекта, поле пока нужно только для тестов при сравнении с PS4
    private Integer ord = null; //ONUMB - номер тестируемого заказа, поле пока нужно только для тестов при сравнении с PS4 
    private Integer nuni = null;  //nuni профиля (PRO4_SYSPROF.NUNI)
    protected Float width1 = null;  //ширина area мм. верхняя
    protected Float width2 = null; //ширина area мм. нижняя
    protected Float height1 = null; //высота area мм левая 
    protected Float height2 = null;  //высота area мм. правая
    protected Integer color1 = -3;  //основная текстура
    protected Integer color2 = -3;  //внутренняя текстура
    protected Integer color3 = -3;  //внешняя текстура    

    public GsonRoot(String version, Integer prj, Integer ord, Integer nuni, String name, Layout layout, Type type, float width, float height, int color1, int color2, int color3) {
        init(version, prj, ord, nuni, name, layout, type, null, width, height, 0, color1, color2, color3, null);
    }

    public GsonRoot(String version, Integer prj, Integer ord, Integer nuni, String name, Layout layout, Type type, float width, float height, int color1, int color2, int color3, String paramJson) {
        init(version, prj, ord, nuni, name, layout, type, null, width, height, 0, color1, color2, color3, paramJson);
    }

    public GsonRoot(String version, Integer prj, Integer ord, Integer nuni, String name, Layout layout, Type type, float width, float height1, float height2, int color1, int color2, int color3) {
        init(version, prj, ord, nuni, name, layout, type, null, width, height1, height2, color1, color2, color3, null);
    }

    public GsonRoot(String version, Integer prj, Integer ord, Integer nuni, String name, Layout layout, Type type, Form form, float width, float height1, float height2, int color1, int color2, int color3) {
        init(version, prj, ord, nuni, name, layout, type, form, width, height1, height2, color1, color2, color3, null);
    }

    public void init(String version, Integer prj, Integer ord, Integer nuni, String name, Layout layout, Type type, Form form, float width1, float height1, float height2, int color1, int color2, int color3, String paramJson) {
        super.genId = 0;
        super.id = 0;
        this.version = version;
        this.prj = prj;
        this.ord = ord;
        this.nuni = nuni;
        this.name = name;
        this.layout = layout;
        this.type = type;
        this.form = form;
        this.width2 = width1;
        this.height1 = height1;
        this.height2 = (height2 == 0) ? null : height2;
        this.length = null;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        if (paramJson != null) {
            this.param(new Gson().fromJson(paramJson, JsonObject.class));
        }
    }

    public void propery(String prj, int nuni, String name) {
        this.nuni = nuni;
        this.prj = Integer.valueOf(prj);
        this.name = (name == null) ? this.name : name;
        if (nuni == -3 && eProp.dev == false) {
            this.color1 = -3;
            this.color2 = -3;
            this.color3 = -3;
        }
    }

    public int nuni() {
        return nuni;
    }

    public Integer project() {
        return prj;
    }

    public Integer order() {
        return ord;
    }

    public String name() {
        return name;
    }

    public Float height() {
        if (height1 == null) {
            return height2;
        } else if (height2 == null) {
            return height1;
        } else if (height1 > height2) {
            return height1;
        } else {
            return height2;
        }
    }

    public Float width() {
        if (width1 == null) {
            return width2;
        } else if (width2 == null) {
            return width1;
        } else if (width1 > width2) {
            return width1;
        } else {
            return width2;
        }
    }

    public Float height1() {
        return height1;
    }

    public void height1(float h) {
        // height1 = UCom.round(h, 1);
        height1 = h;
    }

    public Float height2() {
        return height2;
    }

    public void height2(float h) {
        //height2 = UCom.round(h, 1);
        height2 = h;
    }

    public Float width1() {
        return width1;
    }

    public void width1(float w) {
        //width1 = UCom.round(w, 1);
        width1 = w;
    }

    public Float width2() {
        return width2;
    }

    public void width2(float w) {
        //width2 = UCom.round(w, 1);
        width2 = w;
    }

    public int color1() {
        return color1;
    }

    public void color1(int color1) {
        this.color1 = color1;
    }

    public int color2() {
        return color2;
    }

    public void color2(int color2) {
        this.color2 = color2;
    }

    public int color3() {
        return color3;
    }

    public void color3(int color3) {
        this.color3 = color3;
    }

    public String toJson() {
        this.notSerialize();        
        return new GsonBuilder().create().toJson(this);
        //return new GsonBuilder().registerTypeAdapter(GsonRoot.class, new JsonSerializer2()).create().toJson(this);
    }
}
