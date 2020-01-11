package script;

import enums.eLayoutArea;
import enums.eTypeElem;
import java.util.LinkedList;

public class AreaRoot extends AreaElem {

    private Integer nuni = null;       //nuni профиля (PRO4_SYSPROF.NUNI)
    protected Float heightLow = null;  //меньшая высота, мм (по аналогии с ПС-4). Для прямоугольного изделия = height.
    protected Integer color1 = null;   //основная текстура (PRO4_COLSLST.CCODE)
    protected Integer color2 = null;   //внутренняя текстура (PRO4_COLSLST.CCODE)
    protected Integer color3 = null;   //внешняя текстура (PRO4_COLSLST.CCODE)    
    private String prj = null;         //номер тестируемого проекта, поле нужно только для тестов       
     
    /**
     * Контруктор главного окна
     */
    public AreaRoot(String id, eLayoutArea layoutArea, eTypeElem elemType, float width, float height, float heightLow, int color1, int color2, int color3, String paramJson) {
        super.id = id;
        this.layoutArea = layoutArea;
        this.elemType = elemType;
        this.width = width;
        this.height = height;
        this.heightLow = heightLow;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.paramJson = paramJson;
    }  
    
    public void setParam(int nuni, String prj) {
        this.nuni = nuni;
        this.prj = prj;
    }
    
    public float getHeightLow() {
        return heightLow;
    }
    
    public int getNuni() {
        return nuni;
    }
}
