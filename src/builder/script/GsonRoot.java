package builder.script;

import enums.Layout;
import enums.Type;

public class GsonRoot extends GsonElem {

    public String name = "Конструкция";
    public int prj = 1; //PNUMB - номер тестируемого проекта, поле пока нужно только для тестов 
    public int ord = 1; //ONUMB - номер тестируемого заказа, поле пока нужно только для тестов 
    private Integer nuni = -3;  //nuni профиля (PRO4_SYSPROF.NUNI)
    private Float heightAdd = null;  //дополнительная высота, мм. Для прямоугольного изделия = height.
    public Integer color1 = -3;  //основная текстура
    public Integer color2 = -3;  //внутренняя текстура
    public Integer color3 = -3;  //внешняя текстура    
    
    public GsonRoot(int prj, int ord, int nuni, String name, Layout layoutArea, Type type, float width, float height, int color1, int color2, int color3) {
        this(prj, ord, nuni, name, layoutArea, type, width, height, 0, color1, color2, color3, null);
    }
    
    public GsonRoot(int prj, int ord, int nuni, String name, Layout layoutArea, Type type, float width, float height, int color1, int color2, int color3, String paramJson) {
        this(prj, ord, nuni, name,  layoutArea, type, width, height, 0, color1, color2, color3, paramJson);
    }
    
    public GsonRoot(int prj, int ord, int nuni, String name, Layout layoutArea, Type type, float width, float height, float heightAdd, int color1, int color2, int color3) {
        this(prj, ord, nuni, name,  layoutArea, type, width, height, heightAdd, color1, color2, color3, null);
    }

    public GsonRoot(int prj, int ord, int nuni, String name, Layout layoutArea, Type type, float width, float height, float heightAdd, int color1, int color2, int color3, String paramJson) {
        super.genId = 0;
        super.id = 0;
        this.prj = prj;
        this.ord = ord;
        this.nuni = nuni;
        this.name = name;
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
        this.prj = Integer.valueOf(prj);
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
