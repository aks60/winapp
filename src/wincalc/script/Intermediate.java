package wincalc.script;

import enums.LayoutArea;
import enums.TypeElem;
import java.util.LinkedList;
import wincalc.model.AreaSimple;

public class Intermediate {

    public String name = "Конструкция";
    public String id = null;  // идентификатор элемента
    public String intermediate_id = null; //владелец

    public AreaSimple owner = null; //владелец
    public LayoutArea layoutFrame = null; //сторона располодения эл. рамы
    public LayoutArea layoutArea = null;  //ориентация при размещении area
    public TypeElem elemType = TypeElem.NONE; //тип элемента
    public String paramJson = null; //параметры элемента       

    public int color1 = -1;   //основная текстура (PRO4_COLSLST.CCODE)
    public int color2 = -1;   //внутренняя текстура (PRO4_COLSLST.CCODE)
    public int color3 = -1;   //внешняя текстура (PRO4_COLSLST.CCODE)            

    public float width = 0;  //ширина area, мм
    public float height = 0; //высота area, мм   
    public float heightAdd = 0;  //дополнительная высота, мм (по аналогии с ПС-4). Для прямоугольного изделия = height.
    public float lengthSide = 0; //ширина или высота добавляемой area, зависит от layoutArea, нужно на этапе конструирования (см. функцию add())

    //private int nuni = null; //nuni профиля (PRO4_SYSPROF.NUNI)
    //private String prj = null;   //номер тестируемого проекта, поле нужно только для тестов
    //private LinkedList<Element> elements = new LinkedList();  //список элементов в area
    
    
    //
    public Intermediate(String name, String id, String parent, AreaSimple owner, LayoutArea layout, TypeElem elemType) {
        
        this.name = name;
        this.id = id;
        this.intermediate_id = parent;
        this.owner = owner;
        this.layoutFrame = layout;
        this.layoutArea = layout;
        this.elemType = elemType;
    }
    
    public Intermediate(String name, String id, String parent, AreaSimple owner, LayoutArea layout, TypeElem elemType,
            String paramJson, int color1, int color2, int color3, float width, float height, float heightAdd, float lengthSide) {
        
        this.paramJson = paramJson;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.width = width;
        this.height = height;
        this.heightAdd = heightAdd;
        this.lengthSide = lengthSide;
    }
}
