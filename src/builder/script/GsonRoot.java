package builder.script;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
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
    private Float width1 = null;  //ширина area мм. верхняя
    private Float width2 = null; //ширина area мм. нижняя
    private Float height1 = null; //высота area мм левая 
    private Float height2 = null;  //высота area мм. правая
    private Integer color1 = -3;  //основная текстура
    private Integer color2 = -3;  //внутренняя текстура
    private Integer color3 = -3;  //внешняя текстура    
    
    /**
     * Конструктор по умолчанию.
     * Запускается при сериализации GSON библиотекой.
     * Происходит инициализация полей. Пример: param = new JsonObject()
     */
    public GsonRoot() {
        super();
    }
    
    /**
     * Все эти конструкторы запускаются при тестировании и создании моделей.
     * см. builder.script.test.Bimax.java - при тестировании
     * см. builder.script.GsonScript.java при создании моделей
     * При этом зап. констр. по умолч. GsonElem().
     */
    public GsonRoot(String version, Integer prj, Integer ord, Integer nuni, String name, Layout layout, Type type, float width, float height, Integer color1, Integer color2, Integer color3) {
        init(version, prj, ord, nuni, name, layout, type, null, 0, width, height, 0, color1, color2, color3, null);
    }

    public GsonRoot(String version, Integer prj, Integer ord, Integer nuni, String name, Layout layout, Type type, float width, float height, Integer color1, Integer color2, Integer color3, String paramJson) {
        init(version, prj, ord, nuni, name, layout, type, null, 0, width, height, 0, color1, color2, color3, paramJson);
    }

    public GsonRoot(String version, Integer prj, Integer ord, Integer nuni, String name, Layout layout, Type type, float width, float height1, float height2, Integer color1, Integer color2, Integer color3) {
        init(version, prj, ord, nuni, name, layout, type, null, 0, width, height1, height2, color1, color2, color3, null);
    }

    public GsonRoot(String version, Integer prj, Integer ord, Integer nuni, String name, Layout layout, Type type, Form form, float width, float height1, float height2, Integer color1, Integer color2, Integer color3) {
        init(version, prj, ord, nuni, name, layout, type, form, 0, width, height1, height2, color1, color2, color3, null);
    }

    public GsonRoot(String version, Integer prj, Integer ord, Integer nuni, String name, Layout layout, Type type, Form form, float width1, float width2, float height1, float height2, Integer color1, Integer color2, Integer color3) {
        init(version, prj, ord, nuni, name, layout, type, form, width1, width2, height1, height2, color1, color2, color3, null);
    }

    public void init(String version, Integer prj, Integer ord, Integer nuni, String name, Layout layout, Type type, Form form, float width1, float width2, float height1, float height2, Integer color1, Integer color2, Integer color3, String paramJson) {
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
        this.width1 = width1;
        this.width2 = width2;
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

    public Integer nuni() {
        return (nuni == null) ? -3 : nuni;
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
        height1 = h;
    }

    public Float height2() {
        return height2;
    }

    public void height2(float h) {
        height2 = h;
    }

    public Float width1() {
        return width1;
    }

    public void width1(float w) {
        width1 = w;
    }

    public Float width2() {
        return width2;
    }

    public void width2(float w) {
        width2 = w;
    }

    public int color1() {
        return (color1 == null) ? -3 : color1;
    }

    public void color1(int color1) {
        this.color1 = color1;
    }

    public int color2() {
        return (color2 == null) ? -3 : color2;
    }

    public void color2(int color2) {
        this.color2 = color2;
    }

    public int color3() {
        return (color3 == null) ? -3 : color3;
    }

    public void color3(int color3) {
        this.color3 = color3;
    }

    public String toJson() {
        this.notSerialize(this);
        return new GsonBuilder().create().toJson(this);
    }
}
