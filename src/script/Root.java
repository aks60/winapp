package script;

import enums.eLayoutArea;
import enums.eTypeElem;
import java.util.LinkedList;

public class Root extends Area {

    private Integer nuni = null;             //nuni профиля (PRO4_SYSPROF.NUNI)
    protected Float heightLow = null;        //меньшая высота, мм (по аналогии с ПС-4). Для прямоугольного изделия = height.
    protected Integer colorBase = null;      //основная текстура (PRO4_COLSLST.CCODE)
    protected Integer colorInternal = null;  //внутренняя текстура (PRO4_COLSLST.CCODE)
    protected Integer colorExternal = null;  //внешняя текстура (PRO4_COLSLST.CCODE)    
    private LinkedList<Element> elements = new LinkedList(); //список элементов в area
    private String prj = null; //номер тестируемого проекта, поле нужно только для тестов       
     
    public Root(String id, eLayoutArea layoutArea, eTypeElem elemType, float width, float height, float heightLow, int colorBase, int colorInternal, int colorExternal, String paramJson) {
        super.id = id;
        this.layoutArea = layoutArea;
        this.elemType = elemType;
        this.width = width;
        this.height = height;
        this.heightLow = heightLow;
        this.colorBase = colorBase;
        this.colorInternal = colorInternal;
        this.colorExternal = colorExternal;
        this.paramJson = paramJson;
    }  
    
    /**
     * Добавление элемента в дерево
     * @param element   добавляемый элемент в дерево
     * @return          добавляемый элемент в дерево
     */
    public Element add(Element element) {
        if (element instanceof Area) {

            Area area = (Area) element;
            if (eTypeElem.FULLSTVORKA == element.elemType) {

                area.width = this.width;
                area.height = this.height;
            } else {

                if (eLayoutArea.VERTICAL == layoutArea) {
                    area.height = lengthSide;
                    area.width = width;
                } else {
                    area.height = height;
                    area.width = lengthSide;
                }
            }
        }
        elements.add(element);
        return element;
    }
    
     /**
     *
     * @param nuni профиля
     * @param prj  проект
     */
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
