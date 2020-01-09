package script;

import com.google.gson.Gson;
import enums.eLayoutArea;
import enums.eTypeElem;
import java.util.LinkedList;

/**
 * Контернер передачи данных.
 * В контейнере могут находиться другие контейнеры и элементы.
 */
public class Area extends Element {

    // Эти поля нужны для всех арий:
    protected eLayoutArea layoutArea = null; //ориентация при размещении area
    protected float width = 0;              //ширина area, мм
    protected float height = 0;             //высота area, мм
    private LinkedList<Element> elements = new LinkedList(); //список элементов в area

    // Это поле нужно для расчета размеров Арии на этапе конструирования, из Сервера не передается.
    private Float lengthSide = null;         //ширина или высота добавляемой area, зависит от layoutArea (см. функцию add())

    // Эти поля нужны только для корневой арии:
    private Integer nuni = null;             //nuni профиля (PRO4_SYSPROF.NUNI)
    protected Float heightLow = null;        //меньшая высота, мм (по аналогии с ПС-4). Для прямоугольного изделия = height.
    protected Integer colorBase = null;      //основная текстура (PRO4_COLSLST.CCODE)
    protected Integer colorInternal = null;  //внутренняя текстура (PRO4_COLSLST.CCODE)
    protected Integer colorExternal = null;  //внешняя текстура (PRO4_COLSLST.CCODE)

    // Это поле нужно только для тестов, из Сервера не передается.
    private String prj = null; //номер тестируемого проекта

    /**
     * Конструктор  створки
     * @param id          id элемента
     * @param layoutArea  расположения
     * @param elemType    тип элемента
     * @param paramJson   параметры элемента
     */
    public Area(String id, eLayoutArea layoutArea, eTypeElem elemType, String paramJson) {
        super.id = id;
        this.layoutArea = layoutArea;
        this.elemType = elemType;
        this.paramJson = paramJson;
    }

    /**
     * Конструктор вложенной Area
     * @param id           id элемента
     * @param layoutArea   расположения
     * @param elemType     тип элемента
     * @param lengthSide   длина стороны, сторона зависит от направлени расположения area
     */
    public Area(String id, eLayoutArea layoutArea, eTypeElem elemType, float lengthSide) {
        super.id = id;
        this.layoutArea = layoutArea;
        this.elemType = elemType;
        this.lengthSide = lengthSide;
    }

    /**
     * Конструктор прямоугольного окна
     * @param id             id элемента
     * @param layoutArea     расположения
     * @param elemType       тип элемента
     * @param width          ширина элемента
     * @param height         высота удуьента
     * @param colorBase      внутренняя текстура
     * @param colorInternal  внешняя текстура
     * @param colorExternal  внешняя текстура
     */
    public Area(String id, eLayoutArea layoutArea, eTypeElem elemType, float width, float height, int colorBase, int colorInternal, int colorExternal, String paramJson) {
        this(id, layoutArea, elemType, width, height, height, colorBase, colorInternal, colorExternal, paramJson);
    }

    /**
     * Конструктор непрямоугольного окна
     * @param id             id элемента
     * @param layoutArea     расположения
     * @param elemType       тип элемента
     * @param width          ширина элемента
     * @param height         высота удуьента
     * @param heightLow      меньшая высота
     * @param colorBase      внутренняя текстура
     * @param colorInternal  внешняя текстура
     * @param colorExternal  внешняя текстура
     */
    public Area(String id, eLayoutArea layoutArea, eTypeElem elemType, float width, float height, float heightLow, int colorBase, int colorInternal, int colorExternal, String paramJson) {
        init(id, layoutArea, elemType, width, height, heightLow, colorBase, colorInternal, colorExternal, paramJson);
    }

    /**
     * Инициализация
     * @param id             id элемента
     * @param layoutArea     расположения
     * @param elemType       тип элемента
     * @param width          ширина элемента
     * @param height         высота удуьента
     * @param heightLow      меньшая высота
     * @param colorBase      внутренняя текстура
     * @param colorInternal  внешняя текстура
     * @param colorExternal  внешняя текстура
     */
    private void init(String id, eLayoutArea layoutArea, eTypeElem elemType, float width, float height, float heightLow, int colorBase, int colorInternal, int colorExternal, String paramJson) {
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
                    area.height = area.lengthSide;
                    area.width = width;
                } else {
                    area.height = height;
                    area.width = area.lengthSide;
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

    public float getHeight() {
        return height;
    }

    public float getHeightLow() {
        return heightLow;
    }

    public float getWidth() {
        return width;
    }

    public int getNuni() {
        return nuni;
    }

    public eLayoutArea geteLayoutArea() {
        return layoutArea;
    }

    public LinkedList<Element> getElements() {
        return elements;
    }

    public String toString() {
        return "Область заполнения";
    }    
}
