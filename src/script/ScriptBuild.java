package script;

import com.google.gson.Gson;
import enums.eLayoutArea;
import enums.eTypeElem;

public class ScriptBuild {

    private static Area rootArea;

    public static void addArea(String str) {
        switch (str) {
            case ("areaSquare"):
                rootArea = new Area("1", eLayoutArea.VERTICAL, eTypeElem.SQUARE, 900, 1300, 1009, 10009, 1009, "");
                rootArea.setParam(8, "01");
                rootArea.add(new script.Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
                rootArea.add(new script.Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
                rootArea.add(new script.Element("4", eTypeElem.FRAME, eLayoutArea.TOP));
                rootArea.add(new script.Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));
                break;
            case ("areaArch"):
                break;
            case ("areaArch2"):
                rootArea = new Area("1", eLayoutArea.VERTICAL, eTypeElem.ARCH, 1300, 1700, 1050, 1009, 1009, 1009, "");
                rootArea.setParam(37, "01");
                rootArea.add(new script.Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
                rootArea.add(new script.Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
                rootArea.add(new script.Element("4", eTypeElem.FRAME, eLayoutArea.ARCH));
                rootArea.add(new script.Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));
                break;
            default:
                break;
        }
    }

    public static void addElem(String str) {
        switch (str) {
            case ("impostVert"):
                break;
            case ("impostGoriz"):
                break;
            default:
                break;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////// ТЕСТОВЫЕ ФУНКЦИИ  ///////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static String test(Integer id) {
        if (id == 601001) {

            Area rootArea = new Area("1", eLayoutArea.VERTICAL, eTypeElem.SQUARE, 900, 1300, 1009, 10009, 1009, "");
            rootArea.setParam(8, id.toString());
            rootArea.add(new Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
            rootArea.add(new Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
            rootArea.add(new Element("4", eTypeElem.FRAME, eLayoutArea.TOP));
            rootArea.add(new Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));
            Area area2 = (Area) rootArea.add(new Area("6", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':1, 'funic':23}"));
            area2.add(new Element("7", eTypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 601002) {
            Area rootArea = new Area("1", eLayoutArea.HORIZONTAL, eTypeElem.SQUARE, 1300, 1400, 1009, 10009, 1009, "");
            rootArea.setParam(29, id.toString());
            rootArea.add(new Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
            rootArea.add(new Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
            rootArea.add(new Element("4", eTypeElem.FRAME, eLayoutArea.TOP));
            rootArea.add(new Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));

            Area area2 = (Area) rootArea.add(new Area("6", eLayoutArea.HORIZONTAL, eTypeElem.AREA, 1300 / 2));
            rootArea.add(new Element("7", eTypeElem.IMPOST));
            Area area3 = (Area) rootArea.add(new Area("8", eLayoutArea.HORIZONTAL, eTypeElem.AREA, 1300 / 2));
            Area area4 = (Area) area2.add(new Area("9", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':3, 'funic':20}"));
            Area area5 = (Area) area3.add(new Area("10", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':3, 'funic':20}"));

            area4.add(new Element("11", eTypeElem.GLASS));
            area5.add(new Element("12", eTypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 601003) {
            Area rootArea = new Area("1", eLayoutArea.VERTICAL, eTypeElem.SQUARE, 1440, 1700, 1009, 1009, 1009, "");
            rootArea.setParam(81, id.toString());
            rootArea.add(new Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
            rootArea.add(new Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
            rootArea.add(new Element("4", eTypeElem.FRAME, eLayoutArea.TOP));
            rootArea.add(new Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));

            Area area2 = (Area) rootArea.add(new Area("6", eLayoutArea.HORIZONTAL, eTypeElem.AREA, 400));
            rootArea.add(new Element("7", eTypeElem.IMPOST));
            Area area4 = (Area) rootArea.add(new Area("8", eLayoutArea.HORIZONTAL, eTypeElem.AREA, 1300));
            Area area5 = (Area) area4.add(new Area("9", eLayoutArea.VERTICAL, eTypeElem.AREA, 720));
            area4.add(new Element("10", eTypeElem.IMPOST));
            Area area6 = (Area) area4.add(new Area("11", eLayoutArea.VERTICAL, eTypeElem.AREA, 720));
            Area area8 = (Area) area5.add(new Area("12", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':1, 'funic':337}"));
            Area area9 = (Area) area6.add(new Area("13", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':3, 'funic':336}"));

            area2.add(new Element("14", eTypeElem.GLASS));
            area8.add(new Element("15", eTypeElem.GLASS));
            area9.add(new Element("16", eTypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 601004) {
            Area rootArea = new Area("1", eLayoutArea.VERTICAL, eTypeElem.SQUARE, 1440, 1700, 1009, 1009, 1009, "");
            rootArea.setParam(8, id.toString());
            rootArea.add(new Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
            rootArea.add(new Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
            rootArea.add(new Element("4", eTypeElem.FRAME, eLayoutArea.TOP));
            rootArea.add(new Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));

            Area area2 = (Area) rootArea.add(new Area("6", eLayoutArea.HORIZONTAL, eTypeElem.AREA, 400));
            rootArea.add(new Element("7", eTypeElem.IMPOST));
            Area area4 = (Area) rootArea.add(new Area("8", eLayoutArea.HORIZONTAL, eTypeElem.AREA, 1300));
            Area area5 = (Area) area4.add(new Area("9", eLayoutArea.VERTICAL, eTypeElem.AREA, 720));
            area4.add(new Element("10", eTypeElem.IMPOST));
            Area area6 = (Area) area4.add(new Area("11", eLayoutArea.VERTICAL, eTypeElem.AREA, 720));
            Area area8 = (Area) area5.add(new Area("12", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':1, 'funic':23}"));
            Area area9 = (Area) area6.add(new Area("13", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':4, 'funic':20}"));

            area2.add(new Element("14", eTypeElem.GLASS));
            area8.add(new Element("15", eTypeElem.GLASS));
            area9.add(new Element("16", eTypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 601005) {
            Area rootArea = new Area("1", eLayoutArea.HORIZONTAL, eTypeElem.SQUARE, 1600, 1700, 1009, 1009, 1009, "");
            rootArea.setParam(8, id.toString());
            rootArea.add(new Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
            rootArea.add(new Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
            rootArea.add(new Element("4", eTypeElem.FRAME, eLayoutArea.TOP));
            rootArea.add(new Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));

            Area area2 = (Area) rootArea.add(new Area("6", eLayoutArea.VERTICAL, eTypeElem.AREA, 800));
            rootArea.add(new Element("7", eTypeElem.IMPOST));
            Area area3 = (Area) rootArea.add(new Area("8", eLayoutArea.VERTICAL, eTypeElem.AREA, 800));
            Area area4 = (Area) area2.add(new Area("9", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':1, 'funic':23}"));
            Area area5 = (Area) area3.add(new Area("10", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':4, 'funic':20}"));

            area4.add(new Element("11", eTypeElem.GLASS));
            area5.add(new Element("12", eTypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 601006) {
            Area rootArea = new Area("1", eLayoutArea.HORIZONTAL, eTypeElem.SQUARE, 900, 1400, 1009, 1009, 1009, "");
            rootArea.setParam(110, id.toString());
            rootArea.add(new Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
            rootArea.add(new Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
            rootArea.add(new Element("4", eTypeElem.FRAME, eLayoutArea.TOP));
            rootArea.add(new Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));

            rootArea.add(new Element("6", eTypeElem.GLASS, "{'nunic_iwin':'615496322'}")); //или 'R4x10x4x10x4'
            return new Gson().toJson(rootArea);

        } else if (id == 601007) {
            Area rootArea = new Area("1", eLayoutArea.VERTICAL, eTypeElem.SQUARE, 1100, 1400, 1009, 10018, 10018, "");
            rootArea.setParam(87, id.toString());
            rootArea.add(new Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
            rootArea.add(new Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
            rootArea.add(new Element("4", eTypeElem.FRAME, eLayoutArea.TOP));
            rootArea.add(new Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));

            Area area2 = (Area) rootArea.add(new Area("6", eLayoutArea.HORIZONTAL, eTypeElem.AREA, 300));
            rootArea.add(new Element("7", eTypeElem.IMPOST));
            Area area4 = (Area) rootArea.add(new Area("8", eLayoutArea.HORIZONTAL, eTypeElem.AREA, 1100));
            Area area5 = (Area) area4.add(new Area("9", eLayoutArea.VERTICAL, eTypeElem.AREA, 550));
            area4.add(new Element("10", eTypeElem.IMPOST));
            Area area6 = (Area) area4.add(new Area("11", eLayoutArea.VERTICAL, eTypeElem.AREA, 550));
            Area area8 = (Area) area5.add(new Area("12", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':1, 'funic':93}"));
            Area area9 = (Area) area6.add(new Area("13", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':4, 'funic':92}"));

            area2.add(new Element("14", eTypeElem.GLASS));
            area8.add(new Element("15", eTypeElem.GLASS));
            area9.add(new Element("16", eTypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 601008) {
            Area rootArea = new Area("1", eLayoutArea.HORIZONTAL, eTypeElem.SQUARE, 1200, 1700, 1009, 28014, 21057, "");
            rootArea.setParam(99, id.toString());
            rootArea.add(new Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
            rootArea.add(new Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
            rootArea.add(new Element("4", eTypeElem.FRAME, eLayoutArea.TOP));
            rootArea.add(new Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));

            Area area2 = (Area) rootArea.add(new Area("6", eLayoutArea.HORIZONTAL, eTypeElem.AREA, 600));
            rootArea.add(new Element("7", eTypeElem.IMPOST));
            Area area3 = (Area) rootArea.add(new Area("8", eLayoutArea.VERTICAL, eTypeElem.AREA, 600));
            Area area4 = (Area) area3.add(new Area("9", eLayoutArea.VERTICAL, eTypeElem.AREA, 550));
            area3.add(new Element("10", eTypeElem.IMPOST));
            Area area5 = (Area) area3.add(new Area("11", eLayoutArea.VERTICAL, eTypeElem.AREA, 1150));
            Area area6 = (Area) area4.add(new Area("12", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':4, 'funic':20}"));

            area2.add(new Element("13", eTypeElem.GLASS));
            area6.add(new Element("14", eTypeElem.GLASS));
            area5.add(new Element("15", eTypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 601009) {
            Area rootArea = new Area("1", eLayoutArea.HORIZONTAL, eTypeElem.SQUARE, 700, 1400, 1009, 1009, 1009, "");
            rootArea.setParam(54, id.toString());
            rootArea.add(new Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
            rootArea.add(new Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
            rootArea.add(new Element("4", eTypeElem.FRAME, eLayoutArea.TOP));
            rootArea.add(new Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));

            rootArea.add(new Element("6", eTypeElem.GLASS, "{'nunic_iwin':'1685457539'}")); //или '4x12x4x12x4' для nuni = 54
            return new Gson().toJson(rootArea);

        } else if (id == 601010) {
            Area rootArea = new Area("1", eLayoutArea.HORIZONTAL, eTypeElem.SQUARE, 1300, 1400, 1009, 1009, 1009, "{'pro4Params':[[-862071,295],[-862065,314],[-862062,325],[-862131,17],[-862097,195],[-862060,335]]}");
            rootArea.setParam(54, id.toString());
            rootArea.add(new Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
            rootArea.add(new Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
            rootArea.add(new Element("4", eTypeElem.FRAME, eLayoutArea.TOP));
            rootArea.add(new Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));

            Area area2 = (Area) rootArea.add(new Area("6", eLayoutArea.VERTICAL, eTypeElem.AREA, 650));
            rootArea.add(new Element("7", eTypeElem.IMPOST));
            Area area3 = (Area) rootArea.add(new Area("8", eLayoutArea.VERTICAL, eTypeElem.AREA, 650));

            Area area4 = (Area) area2.add(new Area("9", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':1,'funic':23, 'pro4Params': [[-862107,826],[-862106,830]]}"));
            Area area5 = (Area) area3.add(new Area("10", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':4,'funic':20, 'pro4Params': [[-862107,184],[-862106,186]]}"));

            area4.add(new Element("11", eTypeElem.GLASS, "{'nunic_iwin':'1685457539'}"));
            area5.add(new Element("12", eTypeElem.GLASS, "{'nunic_iwin':'1685457539'}"));
            return new Gson().toJson(rootArea);

        } else if (id == 604004) {
            Area rootArea = new Area("1", eLayoutArea.VERTICAL, eTypeElem.ARCH, 1300, 1700, 1050, 1009, 1009, 1009, "");
            rootArea.setParam(37, id.toString());
            rootArea.add(new Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
            rootArea.add(new Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
            rootArea.add(new Element("4", eTypeElem.FRAME, eLayoutArea.ARCH));
            rootArea.add(new Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));

            rootArea.add(new Element("6", eTypeElem.IMPOST));
            Area area3 = (Area) rootArea.add(new Area("7", eLayoutArea.HORIZONTAL, eTypeElem.AREA, 1050));

            Area area4 = (Area) area3.add(new Area("8", eLayoutArea.VERTICAL, eTypeElem.AREA, 650));
            area3.add(new Element("9", eTypeElem.IMPOST));
            Area area5 = (Area) area3.add(new Area("10", eLayoutArea.VERTICAL, eTypeElem.AREA, 650));

            Area area6 = (Area) area5.add(new Area("11", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':4, 'funic':20}"));

            rootArea.add(new Element("12", eTypeElem.GLASS));
            area4.add(new Element("13", eTypeElem.GLASS));
            area6.add(new Element("14", eTypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 604005) {
            Area rootArea = new Area("1", eLayoutArea.VERTICAL, eTypeElem.ARCH, 1300, 1500, 1200, 1009, 10009, 1009, "");
            rootArea.setParam(135, id.toString());
            rootArea.add(new Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
            rootArea.add(new Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
            rootArea.add(new Element("4", eTypeElem.FRAME, eLayoutArea.ARCH));
            rootArea.add(new Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));

            rootArea.add(new Element("6", eTypeElem.IMPOST));
            Area area3 = (Area) rootArea.add(new Area("7", eLayoutArea.HORIZONTAL, eTypeElem.AREA, 1200));
            Area area4 = (Area) area3.add(new Area("8", eLayoutArea.VERTICAL, eTypeElem.AREA, 650));
            area3.add(new Element("9", eTypeElem.IMPOST));
            Area area5 = (Area) area3.add(new Area("10", eLayoutArea.VERTICAL, eTypeElem.AREA, 650));
            Area area6 = (Area) area4.add(new Area("11", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':1, 'funic':23}"));
            Area area7 = (Area) area5.add(new Area("12", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':4, 'funic':20}"));

            rootArea.add(new Element("13", eTypeElem.GLASS));
            area6.add(new Element("14", eTypeElem.GLASS));
            area7.add(new Element("15", eTypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 604006) {
            Area rootArea = new Area("1", eLayoutArea.VERTICAL, eTypeElem.ARCH, 1100, 1600, 1220, 1009, 1009, 10012, "");
            rootArea.setParam(135, id.toString());
            rootArea.add(new Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
            rootArea.add(new Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
            rootArea.add(new Element("4", eTypeElem.FRAME, eLayoutArea.ARCH));
            rootArea.add(new Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));

            rootArea.add(new Element("6", eTypeElem.IMPOST));
            Area area3 = (Area) rootArea.add(new Area("7", eLayoutArea.HORIZONTAL, eTypeElem.AREA, 1220));
            Area area4 = (Area) area3.add(new Area("8", eLayoutArea.VERTICAL, eTypeElem.AREA, 550));
            area3.add(new Element("9", eTypeElem.IMPOST));
            Area area5 = (Area) area3.add(new Area("10", eLayoutArea.VERTICAL, eTypeElem.AREA, 550));
            Area area6 = (Area) area4.add(new Area("11", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':1, 'funic':23}"));
            Area area7 = (Area) area5.add(new Area("12", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':4, 'funic':20}"));

            rootArea.add(new Element("13", eTypeElem.GLASS));
            area6.add(new Element("14", eTypeElem.GLASS));
            area7.add(new Element("15", eTypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 604007) {
            Area rootArea = new Area("1", eLayoutArea.VERTICAL, eTypeElem.ARCH, 1400, 1700, 1300, 1009, 1009, 10001, "");
            rootArea.setParam(99, id.toString());
            rootArea.add(new Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
            rootArea.add(new Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
            rootArea.add(new Element("4", eTypeElem.FRAME, eLayoutArea.ARCH));
            rootArea.add(new Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));

            rootArea.add(new Element("6", eTypeElem.IMPOST));
            Area area3 = (Area) rootArea.add(new Area("7", eLayoutArea.HORIZONTAL, eTypeElem.AREA, 1300));
            Area area4 = (Area) area3.add(new Area("8", eLayoutArea.VERTICAL, eTypeElem.AREA, 700));
            area3.add(new Element("9", eTypeElem.IMPOST));
            Area area5 = (Area) area3.add(new Area("10", eLayoutArea.VERTICAL, eTypeElem.AREA, 700));
            Area area6 = (Area) area4.add(new Area("11", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':1, 'funic':23}"));
            Area area7 = (Area) area5.add(new Area("12", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':3, 'funic':20}"));

            rootArea.add(new Element("13", eTypeElem.GLASS));
            area6.add(new Element("14", eTypeElem.GLASS));
            area7.add(new Element("15", eTypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 604008) {
            Area rootArea = new Area("1", eLayoutArea.VERTICAL, eTypeElem.ARCH, 1300, 1500, 1200, 1009, 10009, 1009, "");
            rootArea.setParam(8, id.toString());
            rootArea.add(new Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
            rootArea.add(new Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
            rootArea.add(new Element("4", eTypeElem.FRAME, eLayoutArea.ARCH));
            rootArea.add(new Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));

            rootArea.add(new Element("6", eTypeElem.IMPOST));
            Area area3 = (Area) rootArea.add(new Area("7", eLayoutArea.HORIZONTAL, eTypeElem.AREA, 1200));
            Area area4 = (Area) area3.add(new Area("8", eLayoutArea.VERTICAL, eTypeElem.AREA, 650));
            area3.add(new Element("9", eTypeElem.IMPOST));
            Area area5 = (Area) area3.add(new Area("10", eLayoutArea.VERTICAL, eTypeElem.AREA, 650));
            Area area6 = (Area) area4.add(new Area("11", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':3, 'funic':-1}"));
            Area area7 = (Area) area5.add(new Area("12", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':3, 'funic':-1}"));

            rootArea.add(new Element("13", eTypeElem.GLASS));
            area6.add(new Element("14", eTypeElem.GLASS));
            area7.add(new Element("15", eTypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 604009) {
            Area rootArea = new Area("1", eLayoutArea.VERTICAL, eTypeElem.ARCH, 1300, 1500, 1200, 1009, 10009, 1009, "");
            rootArea.setParam(8, id.toString());
            rootArea.add(new Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
            rootArea.add(new Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
            rootArea.add(new Element("4", eTypeElem.FRAME, eLayoutArea.ARCH));
            rootArea.add(new Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));

            rootArea.add(new Element("6", eTypeElem.IMPOST));
            Area area3 = (Area) rootArea.add(new Area("7", eLayoutArea.HORIZONTAL, eTypeElem.AREA, 1200));

            rootArea.add(new Element("8", eTypeElem.GLASS));
            area3.add(new Element("9", eTypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 604010) {
            Area rootArea = new Area("1", eLayoutArea.VERTICAL, eTypeElem.ARCH, 1300, 1700, 1400, 1009, 10009, 1009, "");
            rootArea.setParam(29, id.toString());
            rootArea.add(new Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
            rootArea.add(new Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
            rootArea.add(new Element("4", eTypeElem.FRAME, eLayoutArea.ARCH));
            rootArea.add(new Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));

            rootArea.add(new Element("6", eTypeElem.IMPOST));
            Area area3 = (Area) rootArea.add(new Area("7", eLayoutArea.HORIZONTAL, eTypeElem.AREA, 1400));
            Area area4 = (Area) area3.add(new Area("8", eLayoutArea.VERTICAL, eTypeElem.AREA, 650));
            area3.add(new Element("9", eTypeElem.IMPOST));
            Area area5 = (Area) area3.add(new Area("10", eLayoutArea.VERTICAL, eTypeElem.AREA, 650));
            Area area6 = (Area) area5.add(new Area("11", eLayoutArea.VERTICAL, eTypeElem.AREA, 550));
            area5.add(new Element("12", eTypeElem.IMPOST));
            Area area7 = (Area) area5.add(new Area("13", eLayoutArea.VERTICAL, eTypeElem.AREA, 850));

            Area area8 = (Area) area6.add(new Area("14", eLayoutArea.FULL, eTypeElem.FULLSTVORKA, "{'typeOpen':4, 'funic':20}"));
            rootArea.add(new Element("15", eTypeElem.GLASS));
            area4.add(new Element("16", eTypeElem.GLASS));
            area7.add(new Element("17", eTypeElem.GLASS));
            area8.add(new Element("18", eTypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 605001) {

            Area rootArea = new Area("1", eLayoutArea.VERTICAL, eTypeElem.TRAPEZE, 1300, 1500, 1200, 1009, 10009, 1009, "");
            rootArea.setParam(8, id.toString());
            rootArea.add(new Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
            rootArea.add(new Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
            rootArea.add(new Element("4", eTypeElem.FRAME, eLayoutArea.ARCH));
            rootArea.add(new Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));

            rootArea.add(new Element("6", eTypeElem.IMPOST));
            Area area3 = (Area) rootArea.add(new Area("7", eLayoutArea.HORIZONTAL, eTypeElem.AREA, 1200));

            rootArea.add(new Element("8", eTypeElem.GLASS));
            area3.add(new Element("9", eTypeElem.GLASS));
            return new Gson().toJson(rootArea);

            /*
            Area rootArea = new Area("1", eLayoutArea.VERTICAL, eTypeElem.TRAPEZE, 1300, 1500, 1200, 1009, 10009, 1009);
            rootArea.setParam(8, id.toString());
            rootArea.add(new Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
            rootArea.add(new Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
            rootArea.add(new Element("4", eTypeElem.FRAME, eLayoutArea.TOP));
            rootArea.add(new Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));

            rootArea.add(new Element("6", eTypeElem.IMPOST));
            Area area4 = (Area) rootArea.add(new Area("7", eLayoutArea.HORIZONTAL, eTypeElem.AREA, 1200));

            area4.add(new Element("8", eTypeElem.GLASS));
            return new Gson().toJson(rootArea);*/
        }
        return null;
    }

}
