package builder.script;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import enums.LayoutArea;
import enums.PKjson;
import enums.TypeElem;

public class GsonRoot extends GsonArea {

    private String name = "Конструкция";
    private Integer nuni = null;  //nuni профиля (PRO4_SYSPROF.NUNI)
    private Float heightAdd = null;  //дополнительная высота, мм. Для прямоугольного изделия = height.
    private Integer color1 = null;  //основная текстура
    private Integer color2 = null;  //внутренняя текстура
    private Integer color3 = null;  //внешняя текстура    
    private String prj = null; //номер тестируемого проекта, поле нужно только для тестов       

    //Контруктор главного окна
    public GsonRoot(float id, LayoutArea layoutArea, TypeElem type, float width, float height, float heightAdd, int color1, int color2, int color3, String paramJson) {
        super.id = id;
        this.layout = layoutArea;
        this.type = type;
        this.width = width;
        this.height = height;
        this.heightAdd = heightAdd;
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

    public float heightAdd() {
        return heightAdd;
    }

    public int color(int index) {
        if (param != null && param.isEmpty() == false) {
            JsonObject jsonObj = new Gson().fromJson(param, JsonObject.class);

            if (jsonObj.get(PKjson.colorID1) != null) {
                this.color1 = jsonObj.get(PKjson.colorID1).getAsInt();
            }
            if (jsonObj.get(PKjson.colorID2) != null) {
                this.color2 = jsonObj.get(PKjson.colorID2).getAsInt();
            }
            if (jsonObj.get(PKjson.colorID3) != null) {
                this.color3 = jsonObj.get(PKjson.colorID3).getAsInt();
            }
        }
        return (index == 1) ? color1 : (index == 2) ? color2 : color3;
    }

    public int nuni() {
        return nuni;
    }
}
