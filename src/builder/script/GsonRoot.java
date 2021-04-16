package builder.script;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import enums.LayoutArea;
import enums.PKjson;
import enums.TypeElem;

public class GsonRoot extends GsonElem {

    public String name = "Конструкция";
    private Integer nuni = null;  //nuni профиля (PRO4_SYSPROF.NUNI)
    private Float heightAdd = null;  //дополнительная высота, мм. Для прямоугольного изделия = height.
    public Integer color1 = null;  //основная текстура
    public Integer color2 = null;  //внутренняя текстура
    public Integer color3 = null;  //внешняя текстура    
    public String prj = null; //номер тестируемого проекта, поле нужно только для тестов       

    public GsonRoot(LayoutArea layoutArea, TypeElem type, float width, float height, int color1, int color2, int color3) {
        this(++genId, layoutArea, type, width, height, 0, color1, color2, color3, null);
    }

    public GsonRoot(LayoutArea layoutArea, TypeElem type, float width, float height, int color1, int color2, int color3, String paramJson) {
        this(++genId, layoutArea, type, width, height, 0, color1, color2, color3, paramJson);
    }

    public GsonRoot(LayoutArea layoutArea, TypeElem type, float width, float height, float heightAdd, int color1, int color2, int color3) {
        this(++genId, layoutArea, type, width, height, heightAdd, color1, color2, color3, null);
    }

    public GsonRoot(float id, LayoutArea layoutArea, TypeElem type, float width, float height, int color1, int color2, int color3) {
        this(id, layoutArea, type, width, height, 0, color1, color2, color3, null);
    }

    public GsonRoot(float id, LayoutArea layoutArea, TypeElem type, float width, float height, int color1, int color2, int color3, String paramJson) {
        this(id, layoutArea, type, width, height, 0, color1, color2, color3, paramJson);
    }

    public GsonRoot(float id, LayoutArea layoutArea, TypeElem type, float width, float height, float heightAdd, int color1, int color2, int color3) {
        this(id, layoutArea, type, width, height, heightAdd, color1, color2, color3, null);
    }

    public GsonRoot(float id, LayoutArea layoutArea, TypeElem type, float width, float height, float heightAdd, int color1, int color2, int color3, String paramJson) {
        super.id = id;
        this.layout = layoutArea;
        this.type = type;
        this.width = width;
        this.height = height;
        this.heightAdd = (heightAdd == 0) ? null : heightAdd;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.param = paramJson;
    }

    public void propery(String prj, int nuni, String name) {
        this.nuni = nuni;
        this.prj = prj;
        this.name = (name == null) ? this.name : name;
        if (nuni == -3) {
            this.color1 = -3;
            this.color2 = -3;
            this.color3 = -3;
        }
    }

    public Float heightAdd() {
        return heightAdd;
    }

    public void heightAdd(float heightAdd) {
        this.heightAdd = heightAdd;
    }

    public int nuni() {
        return nuni;
    }
}
