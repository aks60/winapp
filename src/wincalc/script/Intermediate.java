package wincalc.script;

import enums.LayoutArea;
import enums.TypeElem;
import java.util.LinkedList;

public class Intermediate {

    private String name = "Конструкция";
    private String prj = null;   //номер тестируемого проекта, поле нужно только для тестов
    private Integer nuni = null; //nuni профиля (PRO4_SYSPROF.NUNI) 
    protected String id = null;  // идентификатор элемента
    protected Intermediate owner = null; //владелец

    protected LayoutArea layoutFrame = null; //сторона располодения эл. рамы
    protected LayoutArea layoutArea = null;  //ориентация при размещении area
    protected TypeElem elemType = TypeElem.NONE; //тип элемента
    protected String paramJson = null; //параметры элемента       
    
    protected Integer color1 = null;   //основная текстура (PRO4_COLSLST.CCODE)
    protected Integer color2 = null;   //внутренняя текстура (PRO4_COLSLST.CCODE)
    protected Integer color3 = null;   //внешняя текстура (PRO4_COLSLST.CCODE)            
     
    protected float width = 0;  //ширина area, мм
    protected float height = 0; //высота area, мм   
    protected Float heightAdd = null;  //дополнительная высота, мм (по аналогии с ПС-4). Для прямоугольного изделия = height.
    protected Float lengthSide = null; //ширина или высота добавляемой area, зависит от layoutArea, нужно на этапе конструирования (см. функцию add())

    private LinkedList<Element> elements = new LinkedList();  //список элементов в area
}
