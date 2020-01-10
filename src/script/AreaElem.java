package script;

import com.google.gson.Gson;
import enums.eLayoutArea;
import enums.eTypeElem;
import java.util.LinkedList;

/**
 * Контернер передачи данных. В контейнере могут находиться другие контейнеры и
 * элементы.
 */
public class AreaElem extends Element {

    protected eLayoutArea layoutArea = null;                  //ориентация при размещении area
    protected float width = 0;                                //ширина area, мм
    protected float height = 0;                               //высота area, мм
    private LinkedList<Element> elements = new LinkedList();  //список элементов в area
    protected Float lengthSide = null; //ширина или высота добавляемой area, зависит от layoutArea, нужно на этапе конструирования (см. функцию add())

    public AreaElem() {
    }

    /**
     * Конструктор вложенной Area
     */
    public AreaElem(String id, eLayoutArea layoutArea, eTypeElem elemType, float lengthSide) {
        super.id = id;
        this.layoutArea = layoutArea;
        this.elemType = elemType;
        this.lengthSide = lengthSide; //длина стороны, сторона зависит от направлени расположения area
    }
    
    /**
     * Конструктор створки
     */
    public AreaElem(String id, eLayoutArea layoutArea, eTypeElem elemType, String paramJson) {
        super.id = id;
        this.layoutArea = layoutArea;
        this.elemType = elemType;
        this.paramJson = paramJson; //параметры элемента
    }

    /**
     * Добавление элемента в дерево
     */
    public Element add(Element element) {
        if (element instanceof AreaElem) {

            AreaElem area = (AreaElem) element;
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

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////// ТЕСТОВЫЕ ФУНКЦИИ  ///////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static String test(Integer id) {
        if (id == 601001) {

            RootArea rootArea = new RootArea("1", eLayoutArea.VERTICAL, eTypeElem.SQUARE, 900, 1300, 1300, 1009, 10009, 1009, "");
            rootArea.setParam(8, id.toString());
            rootArea.add(new Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
            rootArea.add(new Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
            rootArea.add(new Element("4", eTypeElem.FRAME, eLayoutArea.TOP));
            rootArea.add(new Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));
            AreaElem area2 = (AreaElem) rootArea.add(new AreaElem("6", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':1, 'funic':23}"));
            area2.add(new Element("7", eTypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 601002) {
            RootArea rootArea = new RootArea("1", eLayoutArea.HORIZONTAL, eTypeElem.SQUARE, 1300, 1400, 1400, 1009, 10009, 1009, "");
            rootArea.setParam(29, id.toString());
            rootArea.add(new Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
            rootArea.add(new Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
            rootArea.add(new Element("4", eTypeElem.FRAME, eLayoutArea.TOP));
            rootArea.add(new Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));

            AreaElem area2 = (AreaElem) rootArea.add(new AreaElem("6", eLayoutArea.HORIZONTAL, eTypeElem.AREA, 1300 / 2));
            rootArea.add(new Element("7", eTypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem("8", eLayoutArea.HORIZONTAL, eTypeElem.AREA, 1300 / 2));
            AreaElem area4 = (AreaElem) area2.add(new AreaElem("9", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':3, 'funic':20}"));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem("10", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':3, 'funic':20}"));

            area4.add(new Element("11", eTypeElem.GLASS));
            area5.add(new Element("12", eTypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 601003) {
            RootArea rootArea = new RootArea("1", eLayoutArea.VERTICAL, eTypeElem.SQUARE, 1440, 1700, 1700, 1009, 1009, 1009, "");
            rootArea.setParam(81, id.toString());
            rootArea.add(new Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
            rootArea.add(new Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
            rootArea.add(new Element("4", eTypeElem.FRAME, eLayoutArea.TOP));
            rootArea.add(new Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));

            AreaElem area2 = (AreaElem) rootArea.add(new AreaElem("6", eLayoutArea.HORIZONTAL, eTypeElem.AREA, 400));
            rootArea.add(new Element("7", eTypeElem.IMPOST));
            AreaElem area4 = (AreaElem) rootArea.add(new AreaElem("8", eLayoutArea.HORIZONTAL, eTypeElem.AREA, 1300));
            AreaElem area5 = (AreaElem) area4.add(new AreaElem("9", eLayoutArea.VERTICAL, eTypeElem.AREA, 720));
            area4.add(new Element("10", eTypeElem.IMPOST));
            AreaElem area6 = (AreaElem) area4.add(new AreaElem("11", eLayoutArea.VERTICAL, eTypeElem.AREA, 720));
            AreaElem area8 = (AreaElem) area5.add(new AreaElem("12", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':1, 'funic':337}"));
            AreaElem area9 = (AreaElem) area6.add(new AreaElem("13", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':3, 'funic':336}"));

            area2.add(new Element("14", eTypeElem.GLASS));
            area8.add(new Element("15", eTypeElem.GLASS));
            area9.add(new Element("16", eTypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 601004) {
            RootArea rootArea = new RootArea("1", eLayoutArea.VERTICAL, eTypeElem.SQUARE, 1440, 1700, 1700, 1009, 1009, 1009, "");
            rootArea.setParam(8, id.toString());
            rootArea.add(new Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
            rootArea.add(new Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
            rootArea.add(new Element("4", eTypeElem.FRAME, eLayoutArea.TOP));
            rootArea.add(new Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));

            AreaElem area2 = (AreaElem) rootArea.add(new AreaElem("6", eLayoutArea.HORIZONTAL, eTypeElem.AREA, 400));
            rootArea.add(new Element("7", eTypeElem.IMPOST));
            AreaElem area4 = (AreaElem) rootArea.add(new AreaElem("8", eLayoutArea.HORIZONTAL, eTypeElem.AREA, 1300));
            AreaElem area5 = (AreaElem) area4.add(new AreaElem("9", eLayoutArea.VERTICAL, eTypeElem.AREA, 720));
            area4.add(new Element("10", eTypeElem.IMPOST));
            AreaElem area6 = (AreaElem) area4.add(new AreaElem("11", eLayoutArea.VERTICAL, eTypeElem.AREA, 720));
            AreaElem area8 = (AreaElem) area5.add(new AreaElem("12", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':1, 'funic':23}"));
            AreaElem area9 = (AreaElem) area6.add(new AreaElem("13", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':4, 'funic':20}"));

            area2.add(new Element("14", eTypeElem.GLASS));
            area8.add(new Element("15", eTypeElem.GLASS));
            area9.add(new Element("16", eTypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 601005) {
            RootArea rootArea = new RootArea("1", eLayoutArea.HORIZONTAL, eTypeElem.SQUARE, 1600, 1700, 1700, 1009, 1009, 1009, "");
            rootArea.setParam(8, id.toString());
            rootArea.add(new Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
            rootArea.add(new Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
            rootArea.add(new Element("4", eTypeElem.FRAME, eLayoutArea.TOP));
            rootArea.add(new Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));

            AreaElem area2 = (AreaElem) rootArea.add(new AreaElem("6", eLayoutArea.VERTICAL, eTypeElem.AREA, 800));
            rootArea.add(new Element("7", eTypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem("8", eLayoutArea.VERTICAL, eTypeElem.AREA, 800));
            AreaElem area4 = (AreaElem) area2.add(new AreaElem("9", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':1, 'funic':23}"));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem("10", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':4, 'funic':20}"));

            area4.add(new Element("11", eTypeElem.GLASS));
            area5.add(new Element("12", eTypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 601006) {
            RootArea rootArea = new RootArea("1", eLayoutArea.HORIZONTAL, eTypeElem.SQUARE, 900, 1400, 1400, 1009, 1009, 1009, "");
            rootArea.setParam(110, id.toString());
            rootArea.add(new Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
            rootArea.add(new Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
            rootArea.add(new Element("4", eTypeElem.FRAME, eLayoutArea.TOP));
            rootArea.add(new Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));

            rootArea.add(new Element("6", eTypeElem.GLASS, "{'nunic_iwin':'615496322'}")); //или 'R4x10x4x10x4'
            return new Gson().toJson(rootArea);

        } else if (id == 601007) {
            RootArea rootArea = new RootArea("1", eLayoutArea.VERTICAL, eTypeElem.SQUARE, 1100, 1400, 1400, 1009, 10018, 10018, "");
            rootArea.setParam(87, id.toString());
            rootArea.add(new Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
            rootArea.add(new Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
            rootArea.add(new Element("4", eTypeElem.FRAME, eLayoutArea.TOP));
            rootArea.add(new Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));

            AreaElem area2 = (AreaElem) rootArea.add(new AreaElem("6", eLayoutArea.HORIZONTAL, eTypeElem.AREA, 300));
            rootArea.add(new Element("7", eTypeElem.IMPOST));
            AreaElem area4 = (AreaElem) rootArea.add(new AreaElem("8", eLayoutArea.HORIZONTAL, eTypeElem.AREA, 1100));
            AreaElem area5 = (AreaElem) area4.add(new AreaElem("9", eLayoutArea.VERTICAL, eTypeElem.AREA, 550));
            area4.add(new Element("10", eTypeElem.IMPOST));
            AreaElem area6 = (AreaElem) area4.add(new AreaElem("11", eLayoutArea.VERTICAL, eTypeElem.AREA, 550));
            AreaElem area8 = (AreaElem) area5.add(new AreaElem("12", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':1, 'funic':93}"));
            AreaElem area9 = (AreaElem) area6.add(new AreaElem("13", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':4, 'funic':92}"));

            area2.add(new Element("14", eTypeElem.GLASS));
            area8.add(new Element("15", eTypeElem.GLASS));
            area9.add(new Element("16", eTypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 601008) {
            RootArea rootArea = new RootArea("1", eLayoutArea.HORIZONTAL, eTypeElem.SQUARE, 1200, 1700, 1700, 1009, 28014, 21057, "");
            rootArea.setParam(99, id.toString());
            rootArea.add(new Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
            rootArea.add(new Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
            rootArea.add(new Element("4", eTypeElem.FRAME, eLayoutArea.TOP));
            rootArea.add(new Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));

            AreaElem area2 = (AreaElem) rootArea.add(new AreaElem("6", eLayoutArea.HORIZONTAL, eTypeElem.AREA, 600));
            rootArea.add(new Element("7", eTypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem("8", eLayoutArea.VERTICAL, eTypeElem.AREA, 600));
            AreaElem area4 = (AreaElem) area3.add(new AreaElem("9", eLayoutArea.VERTICAL, eTypeElem.AREA, 550));
            area3.add(new Element("10", eTypeElem.IMPOST));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem("11", eLayoutArea.VERTICAL, eTypeElem.AREA, 1150));
            AreaElem area6 = (AreaElem) area4.add(new AreaElem("12", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':4, 'funic':20}"));

            area2.add(new Element("13", eTypeElem.GLASS));
            area6.add(new Element("14", eTypeElem.GLASS));
            area5.add(new Element("15", eTypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 601009) {
            RootArea rootArea = new RootArea("1", eLayoutArea.HORIZONTAL, eTypeElem.SQUARE, 700, 1400, 1400, 1009, 1009, 1009, "");
            rootArea.setParam(54, id.toString());
            rootArea.add(new Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
            rootArea.add(new Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
            rootArea.add(new Element("4", eTypeElem.FRAME, eLayoutArea.TOP));
            rootArea.add(new Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));

            rootArea.add(new Element("6", eTypeElem.GLASS, "{'nunic_iwin':'1685457539'}")); //или '4x12x4x12x4' для nuni = 54
            return new Gson().toJson(rootArea);

        } else if (id == 601010) {
            RootArea rootArea = new RootArea("1", eLayoutArea.HORIZONTAL, eTypeElem.SQUARE, 1300, 1400, 1400, 1009, 1009, 1009, "{'pro4Params':[[-862071,295],[-862065,314],[-862062,325],[-862131,17],[-862097,195],[-862060,335]]}");
            rootArea.setParam(54, id.toString());
            rootArea.add(new Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
            rootArea.add(new Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
            rootArea.add(new Element("4", eTypeElem.FRAME, eLayoutArea.TOP));
            rootArea.add(new Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));

            AreaElem area2 = (AreaElem) rootArea.add(new AreaElem("6", eLayoutArea.VERTICAL, eTypeElem.AREA, 650));
            rootArea.add(new Element("7", eTypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem("8", eLayoutArea.VERTICAL, eTypeElem.AREA, 650));

            AreaElem area4 = (AreaElem) area2.add(new AreaElem("9", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':1,'funic':23, 'pro4Params': [[-862107,826],[-862106,830]]}"));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem("10", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':4,'funic':20, 'pro4Params': [[-862107,184],[-862106,186]]}"));

            area4.add(new Element("11", eTypeElem.GLASS, "{'nunic_iwin':'1685457539'}"));
            area5.add(new Element("12", eTypeElem.GLASS, "{'nunic_iwin':'1685457539'}"));
            return new Gson().toJson(rootArea);

        } else if (id == 604004) {
            RootArea rootArea = new RootArea("1", eLayoutArea.VERTICAL, eTypeElem.ARCH, 1300, 1700, 1050, 1009, 1009, 1009, "");
            rootArea.setParam(37, id.toString());
            rootArea.add(new Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
            rootArea.add(new Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
            rootArea.add(new Element("4", eTypeElem.FRAME, eLayoutArea.ARCH));
            rootArea.add(new Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));

            rootArea.add(new Element("6", eTypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem("7", eLayoutArea.HORIZONTAL, eTypeElem.AREA, 1050));

            AreaElem area4 = (AreaElem) area3.add(new AreaElem("8", eLayoutArea.VERTICAL, eTypeElem.AREA, 650));
            area3.add(new Element("9", eTypeElem.IMPOST));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem("10", eLayoutArea.VERTICAL, eTypeElem.AREA, 650));

            AreaElem area6 = (AreaElem) area5.add(new AreaElem("11", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':4, 'funic':20}"));

            rootArea.add(new Element("12", eTypeElem.GLASS));
            area4.add(new Element("13", eTypeElem.GLASS));
            area6.add(new Element("14", eTypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 604005) {
            RootArea rootArea = new RootArea("1", eLayoutArea.VERTICAL, eTypeElem.ARCH, 1300, 1500, 1200, 1009, 10009, 1009, "");
            rootArea.setParam(135, id.toString());
            rootArea.add(new Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
            rootArea.add(new Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
            rootArea.add(new Element("4", eTypeElem.FRAME, eLayoutArea.ARCH));
            rootArea.add(new Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));

            rootArea.add(new Element("6", eTypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem("7", eLayoutArea.HORIZONTAL, eTypeElem.AREA, 1200));
            AreaElem area4 = (AreaElem) area3.add(new AreaElem("8", eLayoutArea.VERTICAL, eTypeElem.AREA, 650));
            area3.add(new Element("9", eTypeElem.IMPOST));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem("10", eLayoutArea.VERTICAL, eTypeElem.AREA, 650));
            AreaElem area6 = (AreaElem) area4.add(new AreaElem("11", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':1, 'funic':23}"));
            AreaElem area7 = (AreaElem) area5.add(new AreaElem("12", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':4, 'funic':20}"));

            rootArea.add(new Element("13", eTypeElem.GLASS));
            area6.add(new Element("14", eTypeElem.GLASS));
            area7.add(new Element("15", eTypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 604006) {
            RootArea rootArea = new RootArea("1", eLayoutArea.VERTICAL, eTypeElem.ARCH, 1100, 1600, 1220, 1009, 1009, 10012, "");
            rootArea.setParam(135, id.toString());
            rootArea.add(new Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
            rootArea.add(new Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
            rootArea.add(new Element("4", eTypeElem.FRAME, eLayoutArea.ARCH));
            rootArea.add(new Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));

            rootArea.add(new Element("6", eTypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem("7", eLayoutArea.HORIZONTAL, eTypeElem.AREA, 1220));
            AreaElem area4 = (AreaElem) area3.add(new AreaElem("8", eLayoutArea.VERTICAL, eTypeElem.AREA, 550));
            area3.add(new Element("9", eTypeElem.IMPOST));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem("10", eLayoutArea.VERTICAL, eTypeElem.AREA, 550));
            AreaElem area6 = (AreaElem) area4.add(new AreaElem("11", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':1, 'funic':23}"));
            AreaElem area7 = (AreaElem) area5.add(new AreaElem("12", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':4, 'funic':20}"));

            rootArea.add(new Element("13", eTypeElem.GLASS));
            area6.add(new Element("14", eTypeElem.GLASS));
            area7.add(new Element("15", eTypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 604007) {
            RootArea rootArea = new RootArea("1", eLayoutArea.VERTICAL, eTypeElem.ARCH, 1400, 1700, 1300, 1009, 1009, 10001, "");
            rootArea.setParam(99, id.toString());
            rootArea.add(new Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
            rootArea.add(new Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
            rootArea.add(new Element("4", eTypeElem.FRAME, eLayoutArea.ARCH));
            rootArea.add(new Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));

            rootArea.add(new Element("6", eTypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem("7", eLayoutArea.HORIZONTAL, eTypeElem.AREA, 1300));
            AreaElem area4 = (AreaElem) area3.add(new AreaElem("8", eLayoutArea.VERTICAL, eTypeElem.AREA, 700));
            area3.add(new Element("9", eTypeElem.IMPOST));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem("10", eLayoutArea.VERTICAL, eTypeElem.AREA, 700));
            AreaElem area6 = (AreaElem) area4.add(new AreaElem("11", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':1, 'funic':23}"));
            AreaElem area7 = (AreaElem) area5.add(new AreaElem("12", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':3, 'funic':20}"));

            rootArea.add(new Element("13", eTypeElem.GLASS));
            area6.add(new Element("14", eTypeElem.GLASS));
            area7.add(new Element("15", eTypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 604008) {
            RootArea rootArea = new RootArea("1", eLayoutArea.VERTICAL, eTypeElem.ARCH, 1300, 1500, 1200, 1009, 10009, 1009, "");
            rootArea.setParam(8, id.toString());
            rootArea.add(new Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
            rootArea.add(new Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
            rootArea.add(new Element("4", eTypeElem.FRAME, eLayoutArea.ARCH));
            rootArea.add(new Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));

            rootArea.add(new Element("6", eTypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem("7", eLayoutArea.HORIZONTAL, eTypeElem.AREA, 1200));
            AreaElem area4 = (AreaElem) area3.add(new AreaElem("8", eLayoutArea.VERTICAL, eTypeElem.AREA, 650));
            area3.add(new Element("9", eTypeElem.IMPOST));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem("10", eLayoutArea.VERTICAL, eTypeElem.AREA, 650));
            AreaElem area6 = (AreaElem) area4.add(new AreaElem("11", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':3, 'funic':-1}"));
            AreaElem area7 = (AreaElem) area5.add(new AreaElem("12", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':3, 'funic':-1}"));

            rootArea.add(new Element("13", eTypeElem.GLASS));
            area6.add(new Element("14", eTypeElem.GLASS));
            area7.add(new Element("15", eTypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 604009) {
            RootArea rootArea = new RootArea("1", eLayoutArea.VERTICAL, eTypeElem.ARCH, 1300, 1500, 1200, 1009, 10009, 1009, "");
            rootArea.setParam(8, id.toString());
            rootArea.add(new Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
            rootArea.add(new Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
            rootArea.add(new Element("4", eTypeElem.FRAME, eLayoutArea.ARCH));
            rootArea.add(new Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));

            rootArea.add(new Element("6", eTypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem("7", eLayoutArea.HORIZONTAL, eTypeElem.AREA, 1200));

            rootArea.add(new Element("8", eTypeElem.GLASS));
            area3.add(new Element("9", eTypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 604010) {
            RootArea rootArea = new RootArea("1", eLayoutArea.VERTICAL, eTypeElem.ARCH, 1300, 1700, 1400, 1009, 10009, 1009, "");
            rootArea.setParam(29, id.toString());
            rootArea.add(new Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
            rootArea.add(new Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
            rootArea.add(new Element("4", eTypeElem.FRAME, eLayoutArea.ARCH));
            rootArea.add(new Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));

            rootArea.add(new Element("6", eTypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem("7", eLayoutArea.HORIZONTAL, eTypeElem.AREA, 1400));
            AreaElem area4 = (AreaElem) area3.add(new AreaElem("8", eLayoutArea.VERTICAL, eTypeElem.AREA, 650));
            area3.add(new Element("9", eTypeElem.IMPOST));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem("10", eLayoutArea.VERTICAL, eTypeElem.AREA, 650));
            AreaElem area6 = (AreaElem) area5.add(new AreaElem("11", eLayoutArea.VERTICAL, eTypeElem.AREA, 550));
            area5.add(new Element("12", eTypeElem.IMPOST));
            AreaElem area7 = (AreaElem) area5.add(new AreaElem("13", eLayoutArea.VERTICAL, eTypeElem.AREA, 850));

            AreaElem area8 = (AreaElem) area6.add(new AreaElem("14", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':4, 'funic':20}"));
            rootArea.add(new Element("15", eTypeElem.GLASS));
            area4.add(new Element("16", eTypeElem.GLASS));
            area7.add(new Element("17", eTypeElem.GLASS));
            area8.add(new Element("18", eTypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 605001) {

            RootArea rootArea = new RootArea("1", eLayoutArea.VERTICAL, eTypeElem.TRAPEZE, 1300, 1500, 1200, 1009, 10009, 1009, "");
            rootArea.setParam(8, id.toString());
            rootArea.add(new Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
            rootArea.add(new Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
            rootArea.add(new Element("4", eTypeElem.FRAME, eLayoutArea.ARCH));
            rootArea.add(new Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));

            rootArea.add(new Element("6", eTypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem("7", eLayoutArea.HORIZONTAL, eTypeElem.AREA, 1200));

            rootArea.add(new Element("8", eTypeElem.GLASS));
            area3.add(new Element("9", eTypeElem.GLASS));
            return new Gson().toJson(rootArea);
        }
        return null;
    }
}
