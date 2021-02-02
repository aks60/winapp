package builder.script;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import enums.LayoutArea;
import enums.ParamJson;
import enums.TypeElem;

public class JsonRoot extends JsonArea {

    private String name = "Конструкция";
    private Integer nuni = null;  //nuni профиля (PRO4_SYSPROF.NUNI)
    private Float heightAdd = null;  //дополнительная высота, мм (по аналогии с ПС-4). Для прямоугольного изделия = height.
    private Integer color1 = null;  //основная текстура (PRO4_COLSLST.CCODE)
    private Integer color2 = null;  //внутренняя текстура (PRO4_COLSLST.CCODE)
    private Integer color3 = null;  //внешняя текстура (PRO4_COLSLST.CCODE)    
    private String prj = null; //номер тестируемого проекта, поле нужно только для тестов       

    //Контруктор главного окна
    public JsonRoot(float id, LayoutArea layoutArea, TypeElem type, float width, float height, float heightAdd, int color1, int color2, int color3, String paramJson) {
        super.id = id;
        this.layout = layoutArea;
        this.type = type;
        this.width = width;
        this.height = height;
        this.heightAdd = heightAdd;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.paramJson = paramJson;
    }

    public void param(String prj, int nuni, String name) {
        this.nuni = nuni;
        this.prj = prj;
        this.name = name;
    }

    public float heightAdd() {
        return heightAdd;
    }

    public int color(int index) {
        if (paramJson != null && paramJson.isEmpty() == false) {
            JsonObject jsonObj = new Gson().fromJson(paramJson, JsonObject.class);

            if (jsonObj.get(ParamJson.colorID1.name()) != null) {
                this.color1 = jsonObj.get(ParamJson.colorID1.name()).getAsInt();
            }
            if (jsonObj.get(ParamJson.colorID2.name()) != null) {
                this.color2 = jsonObj.get(ParamJson.colorID2.name()).getAsInt();
            }
            if (jsonObj.get(ParamJson.colorID3.name()) != null) {
                this.color3 = jsonObj.get(ParamJson.colorID3.name()).getAsInt();
            }
        }        
        return (index == 1) ? color1 : (index == 2) ? color2 : color3;
    }

    public int nuni() {
        return nuni;
    }
}
