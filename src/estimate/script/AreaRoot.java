package estimate.script;

import enums.LayoutArea;
import enums.TypeElem;
import java.util.LinkedList;

public class AreaRoot extends AreaElem {

    private String name = "Конструкция";
    private Integer nuni = null;       //nuni профиля (PRO4_SYSPROF.NUNI)
    protected Float heightAdd = null;  //дополнительная высота, мм (по аналогии с ПС-4). Для прямоугольного изделия = height.
    protected Integer color1 = null;   //основная текстура (PRO4_COLSLST.CCODE)
    protected Integer color2 = null;   //внутренняя текстура (PRO4_COLSLST.CCODE)
    protected Integer color3 = null;   //внешняя текстура (PRO4_COLSLST.CCODE)    
    private String prj = null;         //номер тестируемого проекта, поле нужно только для тестов       

    //Контруктор главного окна
    public AreaRoot(float id, LayoutArea layoutArea, TypeElem elemType, float width, float height, float heightAdd, int color1, int color2, int color3, String paramJson) {
        super.id = id;
        this.layoutArea = layoutArea;
        this.elemType = elemType;
        this.width = width;
        this.height = height;
        this.heightAdd = heightAdd;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.paramJson = paramJson;
    }

    public void setParam(String prj, int nuni) {
        this.nuni = nuni;
        this.prj = prj;
        if (nuni == -1) {
            this.color1 = -1;
            this.color2 = -1;
            this.color3 = -1;
        }
    }

    public void setParam(String prj, int nuni, String name) {
        this.nuni = nuni;
        this.prj = prj;
        this.name = name;
    }

    public float getHeightAdd() {
        return heightAdd;
    }

    public int getNuni() {
        return nuni;
    }
}
