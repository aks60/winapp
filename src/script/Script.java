package script;

import com.google.gson.Gson;
import enums.LayoutArea;
import enums.TypeElem;

public class Script {

    public static String test(Integer id) {
        if (id == 601001) {

            AreaRoot rootArea = new AreaRoot("1", LayoutArea.VERTICAL, TypeElem.SQUARE, 900, 1300, 1300, 1009, 10009, 1009, "");
            rootArea.setParam(8, id.toString());
            rootArea.add(new Element("2", TypeElem.FRAME, LayoutArea.LEFT));
            rootArea.add(new Element("3", TypeElem.FRAME, LayoutArea.RIGHT));
            rootArea.add(new Element("4", TypeElem.FRAME, LayoutArea.TOP));
            rootArea.add(new Element("5", TypeElem.FRAME, LayoutArea.BOTTOM));
            AreaElem area2 = (AreaElem) rootArea.add(new AreaElem("6", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':1, 'funic':23}"));
            area2.add(new Element("7", TypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 601002) {
            AreaRoot rootArea = new AreaRoot("1", LayoutArea.HORIZONTAL, TypeElem.SQUARE, 1300, 1400, 1400, 1009, 10009, 1009, "");
            rootArea.setParam(29, id.toString());
            rootArea.add(new Element("2", TypeElem.FRAME, LayoutArea.LEFT));
            rootArea.add(new Element("3", TypeElem.FRAME, LayoutArea.RIGHT));
            rootArea.add(new Element("4", TypeElem.FRAME, LayoutArea.TOP));
            rootArea.add(new Element("5", TypeElem.FRAME, LayoutArea.BOTTOM));

            AreaElem area2 = (AreaElem) rootArea.add(new AreaElem("6", LayoutArea.HORIZONTAL, TypeElem.AREA, 1300 / 2));
            rootArea.add(new Element("7", TypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem("8", LayoutArea.HORIZONTAL, TypeElem.AREA, 1300 / 2));
            AreaElem area4 = (AreaElem) area2.add(new AreaElem("9", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':3, 'funic':20}"));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem("10", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':3, 'funic':20}"));

            area4.add(new Element("11", TypeElem.GLASS));
            area5.add(new Element("12", TypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 601003) {
            AreaRoot rootArea = new AreaRoot("1", LayoutArea.VERTICAL, TypeElem.SQUARE, 1440, 1700, 1700, 1009, 1009, 1009, "");
            rootArea.setParam(81, id.toString());
            rootArea.add(new Element("2", TypeElem.FRAME, LayoutArea.LEFT));
            rootArea.add(new Element("3", TypeElem.FRAME, LayoutArea.RIGHT));
            rootArea.add(new Element("4", TypeElem.FRAME, LayoutArea.TOP));
            rootArea.add(new Element("5", TypeElem.FRAME, LayoutArea.BOTTOM));

            AreaElem area2 = (AreaElem) rootArea.add(new AreaElem("6", LayoutArea.HORIZONTAL, TypeElem.AREA, 400));
            rootArea.add(new Element("7", TypeElem.IMPOST));
            AreaElem area4 = (AreaElem) rootArea.add(new AreaElem("8", LayoutArea.HORIZONTAL, TypeElem.AREA, 1300));
            AreaElem area5 = (AreaElem) area4.add(new AreaElem("9", LayoutArea.VERTICAL, TypeElem.AREA, 720));
            area4.add(new Element("10", TypeElem.IMPOST));
            AreaElem area6 = (AreaElem) area4.add(new AreaElem("11", LayoutArea.VERTICAL, TypeElem.AREA, 720));
            AreaElem area8 = (AreaElem) area5.add(new AreaElem("12", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':1, 'funic':337}"));
            AreaElem area9 = (AreaElem) area6.add(new AreaElem("13", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':3, 'funic':336}"));

            area2.add(new Element("14", TypeElem.GLASS));
            area8.add(new Element("15", TypeElem.GLASS));
            area9.add(new Element("16", TypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 601004) {
            AreaRoot rootArea = new AreaRoot("1", LayoutArea.VERTICAL, TypeElem.SQUARE, 1440, 1700, 1700, 1009, 1009, 1009, "");
            rootArea.setParam(8, id.toString());
            rootArea.add(new Element("2", TypeElem.FRAME, LayoutArea.LEFT));
            rootArea.add(new Element("3", TypeElem.FRAME, LayoutArea.RIGHT));
            rootArea.add(new Element("4", TypeElem.FRAME, LayoutArea.TOP));
            rootArea.add(new Element("5", TypeElem.FRAME, LayoutArea.BOTTOM));

            AreaElem area2 = (AreaElem) rootArea.add(new AreaElem("6", LayoutArea.HORIZONTAL, TypeElem.AREA, 400));
            rootArea.add(new Element("7", TypeElem.IMPOST));
            AreaElem area4 = (AreaElem) rootArea.add(new AreaElem("8", LayoutArea.HORIZONTAL, TypeElem.AREA, 1300));
            AreaElem area5 = (AreaElem) area4.add(new AreaElem("9", LayoutArea.VERTICAL, TypeElem.AREA, 720));
            area4.add(new Element("10", TypeElem.IMPOST));
            AreaElem area6 = (AreaElem) area4.add(new AreaElem("11", LayoutArea.VERTICAL, TypeElem.AREA, 720));
            AreaElem area8 = (AreaElem) area5.add(new AreaElem("12", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':1, 'funic':23}"));
            AreaElem area9 = (AreaElem) area6.add(new AreaElem("13", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':4, 'funic':20}"));

            area2.add(new Element("14", TypeElem.GLASS));
            area8.add(new Element("15", TypeElem.GLASS));
            area9.add(new Element("16", TypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 601005) {
            AreaRoot rootArea = new AreaRoot("1", LayoutArea.HORIZONTAL, TypeElem.SQUARE, 1600, 1700, 1700, 1009, 1009, 1009, "");
            rootArea.setParam(8, id.toString());
            rootArea.add(new Element("2", TypeElem.FRAME, LayoutArea.LEFT));
            rootArea.add(new Element("3", TypeElem.FRAME, LayoutArea.RIGHT));
            rootArea.add(new Element("4", TypeElem.FRAME, LayoutArea.TOP));
            rootArea.add(new Element("5", TypeElem.FRAME, LayoutArea.BOTTOM));

            AreaElem area2 = (AreaElem) rootArea.add(new AreaElem("6", LayoutArea.VERTICAL, TypeElem.AREA, 800));
            rootArea.add(new Element("7", TypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem("8", LayoutArea.VERTICAL, TypeElem.AREA, 800));
            AreaElem area4 = (AreaElem) area2.add(new AreaElem("9", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':1, 'funic':23}"));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem("10", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':4, 'funic':20}"));

            area4.add(new Element("11", TypeElem.GLASS));
            area5.add(new Element("12", TypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 601006) {
            AreaRoot rootArea = new AreaRoot("1", LayoutArea.HORIZONTAL, TypeElem.SQUARE, 900, 1400, 1400, 1009, 1009, 1009, "");
            rootArea.setParam(110, id.toString());
            rootArea.add(new Element("2", TypeElem.FRAME, LayoutArea.LEFT));
            rootArea.add(new Element("3", TypeElem.FRAME, LayoutArea.RIGHT));
            rootArea.add(new Element("4", TypeElem.FRAME, LayoutArea.TOP));
            rootArea.add(new Element("5", TypeElem.FRAME, LayoutArea.BOTTOM));

            rootArea.add(new Element("6", TypeElem.GLASS, "{'nunic_iwin':'615496322'}")); //или 'R4x10x4x10x4'
            return new Gson().toJson(rootArea);

        } else if (id == 601007) {
            AreaRoot rootArea = new AreaRoot("1", LayoutArea.VERTICAL, TypeElem.SQUARE, 1100, 1400, 1400, 1009, 10018, 10018, "");
            rootArea.setParam(87, id.toString());
            rootArea.add(new Element("2", TypeElem.FRAME, LayoutArea.LEFT));
            rootArea.add(new Element("3", TypeElem.FRAME, LayoutArea.RIGHT));
            rootArea.add(new Element("4", TypeElem.FRAME, LayoutArea.TOP));
            rootArea.add(new Element("5", TypeElem.FRAME, LayoutArea.BOTTOM));

            AreaElem area2 = (AreaElem) rootArea.add(new AreaElem("6", LayoutArea.HORIZONTAL, TypeElem.AREA, 300));
            rootArea.add(new Element("7", TypeElem.IMPOST));
            AreaElem area4 = (AreaElem) rootArea.add(new AreaElem("8", LayoutArea.HORIZONTAL, TypeElem.AREA, 1100));
            AreaElem area5 = (AreaElem) area4.add(new AreaElem("9", LayoutArea.VERTICAL, TypeElem.AREA, 550));
            area4.add(new Element("10", TypeElem.IMPOST));
            AreaElem area6 = (AreaElem) area4.add(new AreaElem("11", LayoutArea.VERTICAL, TypeElem.AREA, 550));
            AreaElem area8 = (AreaElem) area5.add(new AreaElem("12", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':1, 'funic':93}"));
            AreaElem area9 = (AreaElem) area6.add(new AreaElem("13", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':4, 'funic':92}"));

            area2.add(new Element("14", TypeElem.GLASS));
            area8.add(new Element("15", TypeElem.GLASS));
            area9.add(new Element("16", TypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 601008) {
            AreaRoot rootArea = new AreaRoot("1", LayoutArea.HORIZONTAL, TypeElem.SQUARE, 1200, 1700, 1700, 1009, 28014, 21057, "");
            rootArea.setParam(99, id.toString());
            rootArea.add(new Element("2", TypeElem.FRAME, LayoutArea.LEFT));
            rootArea.add(new Element("3", TypeElem.FRAME, LayoutArea.RIGHT));
            rootArea.add(new Element("4", TypeElem.FRAME, LayoutArea.TOP));
            rootArea.add(new Element("5", TypeElem.FRAME, LayoutArea.BOTTOM));

            AreaElem area2 = (AreaElem) rootArea.add(new AreaElem("6", LayoutArea.HORIZONTAL, TypeElem.AREA, 600));
            rootArea.add(new Element("7", TypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem("8", LayoutArea.VERTICAL, TypeElem.AREA, 600));
            AreaElem area4 = (AreaElem) area3.add(new AreaElem("9", LayoutArea.VERTICAL, TypeElem.AREA, 550));
            area3.add(new Element("10", TypeElem.IMPOST));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem("11", LayoutArea.VERTICAL, TypeElem.AREA, 1150));
            AreaElem area6 = (AreaElem) area4.add(new AreaElem("12", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':4, 'funic':20}"));

            area2.add(new Element("13", TypeElem.GLASS));
            area6.add(new Element("14", TypeElem.GLASS));
            area5.add(new Element("15", TypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 601009) {
            AreaRoot rootArea = new AreaRoot("1", LayoutArea.HORIZONTAL, TypeElem.SQUARE, 700, 1400, 1400, 1009, 1009, 1009, "");
            rootArea.setParam(54, id.toString());
            rootArea.add(new Element("2", TypeElem.FRAME, LayoutArea.LEFT));
            rootArea.add(new Element("3", TypeElem.FRAME, LayoutArea.RIGHT));
            rootArea.add(new Element("4", TypeElem.FRAME, LayoutArea.TOP));
            rootArea.add(new Element("5", TypeElem.FRAME, LayoutArea.BOTTOM));

            rootArea.add(new Element("6", TypeElem.GLASS, "{'nunic_iwin':'1685457539'}")); //или '4x12x4x12x4' для nuni = 54
            return new Gson().toJson(rootArea);

        } else if (id == 601010) {
            AreaRoot rootArea = new AreaRoot("1", LayoutArea.HORIZONTAL, TypeElem.SQUARE, 1300, 1400, 1400, 1009, 1009, 1009, "{'pro4Params':[[-862071,295],[-862065,314],[-862062,325],[-862131,17],[-862097,195],[-862060,335]]}");
            rootArea.setParam(54, id.toString());
            rootArea.add(new Element("2", TypeElem.FRAME, LayoutArea.LEFT));
            rootArea.add(new Element("3", TypeElem.FRAME, LayoutArea.RIGHT));
            rootArea.add(new Element("4", TypeElem.FRAME, LayoutArea.TOP));
            rootArea.add(new Element("5", TypeElem.FRAME, LayoutArea.BOTTOM));

            AreaElem area2 = (AreaElem) rootArea.add(new AreaElem("6", LayoutArea.VERTICAL, TypeElem.AREA, 650));
            rootArea.add(new Element("7", TypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem("8", LayoutArea.VERTICAL, TypeElem.AREA, 650));

            AreaElem area4 = (AreaElem) area2.add(new AreaElem("9", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':1,'funic':23, 'pro4Params': [[-862107,826],[-862106,830]]}"));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem("10", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':4,'funic':20, 'pro4Params': [[-862107,184],[-862106,186]]}"));

            area4.add(new Element("11", TypeElem.GLASS, "{'nunic_iwin':'1685457539'}"));
            area5.add(new Element("12", TypeElem.GLASS, "{'nunic_iwin':'1685457539'}"));
            return new Gson().toJson(rootArea);

        } else if (id == 604004) {
            AreaRoot rootArea = new AreaRoot("1", LayoutArea.VERTICAL, TypeElem.ARCH, 1300, 1700, 1050, 1009, 1009, 1009, "");
            rootArea.setParam(37, id.toString());
            rootArea.add(new Element("2", TypeElem.FRAME, LayoutArea.LEFT));
            rootArea.add(new Element("3", TypeElem.FRAME, LayoutArea.RIGHT));
            rootArea.add(new Element("4", TypeElem.FRAME, LayoutArea.ARCH));
            rootArea.add(new Element("5", TypeElem.FRAME, LayoutArea.BOTTOM));

            rootArea.add(new Element("6", TypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem("7", LayoutArea.HORIZONTAL, TypeElem.AREA, 1050));

            AreaElem area4 = (AreaElem) area3.add(new AreaElem("8", LayoutArea.VERTICAL, TypeElem.AREA, 650));
            area3.add(new Element("9", TypeElem.IMPOST));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem("10", LayoutArea.VERTICAL, TypeElem.AREA, 650));

            AreaElem area6 = (AreaElem) area5.add(new AreaElem("11", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':4, 'funic':20}"));

            rootArea.add(new Element("12", TypeElem.GLASS));
            area4.add(new Element("13", TypeElem.GLASS));
            area6.add(new Element("14", TypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 604005) {
            AreaRoot rootArea = new AreaRoot("1", LayoutArea.VERTICAL, TypeElem.ARCH, 1300, 1500, 1200, 1009, 10009, 1009, "");
            rootArea.setParam(135, id.toString());
            rootArea.add(new Element("2", TypeElem.FRAME, LayoutArea.LEFT));
            rootArea.add(new Element("3", TypeElem.FRAME, LayoutArea.RIGHT));
            rootArea.add(new Element("4", TypeElem.FRAME, LayoutArea.ARCH));
            rootArea.add(new Element("5", TypeElem.FRAME, LayoutArea.BOTTOM));

            rootArea.add(new Element("6", TypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem("7", LayoutArea.HORIZONTAL, TypeElem.AREA, 1200));
            AreaElem area4 = (AreaElem) area3.add(new AreaElem("8", LayoutArea.VERTICAL, TypeElem.AREA, 650));
            area3.add(new Element("9", TypeElem.IMPOST));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem("10", LayoutArea.VERTICAL, TypeElem.AREA, 650));
            AreaElem area6 = (AreaElem) area4.add(new AreaElem("11", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':1, 'funic':23}"));
            AreaElem area7 = (AreaElem) area5.add(new AreaElem("12", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':4, 'funic':20}"));

            rootArea.add(new Element("13", TypeElem.GLASS));
            area6.add(new Element("14", TypeElem.GLASS));
            area7.add(new Element("15", TypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 604006) {
            AreaRoot rootArea = new AreaRoot("1", LayoutArea.VERTICAL, TypeElem.ARCH, 1100, 1600, 1220, 1009, 1009, 10012, "");
            rootArea.setParam(135, id.toString());
            rootArea.add(new Element("2", TypeElem.FRAME, LayoutArea.LEFT));
            rootArea.add(new Element("3", TypeElem.FRAME, LayoutArea.RIGHT));
            rootArea.add(new Element("4", TypeElem.FRAME, LayoutArea.ARCH));
            rootArea.add(new Element("5", TypeElem.FRAME, LayoutArea.BOTTOM));

            rootArea.add(new Element("6", TypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem("7", LayoutArea.HORIZONTAL, TypeElem.AREA, 1220));
            AreaElem area4 = (AreaElem) area3.add(new AreaElem("8", LayoutArea.VERTICAL, TypeElem.AREA, 550));
            area3.add(new Element("9", TypeElem.IMPOST));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem("10", LayoutArea.VERTICAL, TypeElem.AREA, 550));
            AreaElem area6 = (AreaElem) area4.add(new AreaElem("11", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':1, 'funic':23}"));
            AreaElem area7 = (AreaElem) area5.add(new AreaElem("12", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':4, 'funic':20}"));

            rootArea.add(new Element("13", TypeElem.GLASS));
            area6.add(new Element("14", TypeElem.GLASS));
            area7.add(new Element("15", TypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 604007) {
            AreaRoot rootArea = new AreaRoot("1", LayoutArea.VERTICAL, TypeElem.ARCH, 1400, 1700, 1300, 1009, 1009, 10001, "");
            rootArea.setParam(99, id.toString());
            rootArea.add(new Element("2", TypeElem.FRAME, LayoutArea.LEFT));
            rootArea.add(new Element("3", TypeElem.FRAME, LayoutArea.RIGHT));
            rootArea.add(new Element("4", TypeElem.FRAME, LayoutArea.ARCH));
            rootArea.add(new Element("5", TypeElem.FRAME, LayoutArea.BOTTOM));

            rootArea.add(new Element("6", TypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem("7", LayoutArea.HORIZONTAL, TypeElem.AREA, 1300));
            AreaElem area4 = (AreaElem) area3.add(new AreaElem("8", LayoutArea.VERTICAL, TypeElem.AREA, 700));
            area3.add(new Element("9", TypeElem.IMPOST));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem("10", LayoutArea.VERTICAL, TypeElem.AREA, 700));
            AreaElem area6 = (AreaElem) area4.add(new AreaElem("11", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':1, 'funic':23}"));
            AreaElem area7 = (AreaElem) area5.add(new AreaElem("12", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':3, 'funic':20}"));

            rootArea.add(new Element("13", TypeElem.GLASS));
            area6.add(new Element("14", TypeElem.GLASS));
            area7.add(new Element("15", TypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 604008) {
            AreaRoot rootArea = new AreaRoot("1", LayoutArea.VERTICAL, TypeElem.ARCH, 1300, 1500, 1200, 1009, 10009, 1009, "");
            rootArea.setParam(8, id.toString());
            rootArea.add(new Element("2", TypeElem.FRAME, LayoutArea.LEFT));
            rootArea.add(new Element("3", TypeElem.FRAME, LayoutArea.RIGHT));
            rootArea.add(new Element("4", TypeElem.FRAME, LayoutArea.ARCH));
            rootArea.add(new Element("5", TypeElem.FRAME, LayoutArea.BOTTOM));

            rootArea.add(new Element("6", TypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem("7", LayoutArea.HORIZONTAL, TypeElem.AREA, 1200));
            AreaElem area4 = (AreaElem) area3.add(new AreaElem("8", LayoutArea.VERTICAL, TypeElem.AREA, 650));
            area3.add(new Element("9", TypeElem.IMPOST));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem("10", LayoutArea.VERTICAL, TypeElem.AREA, 650));
            AreaElem area6 = (AreaElem) area4.add(new AreaElem("11", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':3, 'funic':-1}"));
            AreaElem area7 = (AreaElem) area5.add(new AreaElem("12", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':3, 'funic':-1}"));

            rootArea.add(new Element("13", TypeElem.GLASS));
            area6.add(new Element("14", TypeElem.GLASS));
            area7.add(new Element("15", TypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 604009) {
            AreaRoot rootArea = new AreaRoot("1", LayoutArea.VERTICAL, TypeElem.ARCH, 1300, 1500, 1200, 1009, 10009, 1009, "");
            rootArea.setParam(8, id.toString());
            rootArea.add(new Element("2", TypeElem.FRAME, LayoutArea.LEFT));
            rootArea.add(new Element("3", TypeElem.FRAME, LayoutArea.RIGHT));
            rootArea.add(new Element("4", TypeElem.FRAME, LayoutArea.ARCH));
            rootArea.add(new Element("5", TypeElem.FRAME, LayoutArea.BOTTOM));

            rootArea.add(new Element("6", TypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem("7", LayoutArea.HORIZONTAL, TypeElem.AREA, 1200));

            rootArea.add(new Element("8", TypeElem.GLASS));
            area3.add(new Element("9", TypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 604010) {
            AreaRoot rootArea = new AreaRoot("1", LayoutArea.VERTICAL, TypeElem.ARCH, 1300, 1700, 1400, 1009, 10009, 1009, "");
            rootArea.setParam(29, id.toString());
            rootArea.add(new Element("2", TypeElem.FRAME, LayoutArea.LEFT));
            rootArea.add(new Element("3", TypeElem.FRAME, LayoutArea.RIGHT));
            rootArea.add(new Element("4", TypeElem.FRAME, LayoutArea.ARCH));
            rootArea.add(new Element("5", TypeElem.FRAME, LayoutArea.BOTTOM));

            rootArea.add(new Element("6", TypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem("7", LayoutArea.HORIZONTAL, TypeElem.AREA, 1400));
            AreaElem area4 = (AreaElem) area3.add(new AreaElem("8", LayoutArea.VERTICAL, TypeElem.AREA, 650));
            area3.add(new Element("9", TypeElem.IMPOST));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem("10", LayoutArea.VERTICAL, TypeElem.AREA, 650));
            AreaElem area6 = (AreaElem) area5.add(new AreaElem("11", LayoutArea.VERTICAL, TypeElem.AREA, 550));
            area5.add(new Element("12", TypeElem.IMPOST));
            AreaElem area7 = (AreaElem) area5.add(new AreaElem("13", LayoutArea.VERTICAL, TypeElem.AREA, 850));

            AreaElem area8 = (AreaElem) area6.add(new AreaElem("14", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':4, 'funic':20}"));
            rootArea.add(new Element("15", TypeElem.GLASS));
            area4.add(new Element("16", TypeElem.GLASS));
            area7.add(new Element("17", TypeElem.GLASS));
            area8.add(new Element("18", TypeElem.GLASS));
            return new Gson().toJson(rootArea);

        } else if (id == 605001) {

            AreaRoot rootArea = new AreaRoot("1", LayoutArea.VERTICAL, TypeElem.TRAPEZE, 1300, 1500, 1200, 1009, 10009, 1009, "");
            rootArea.setParam(8, id.toString());
            rootArea.add(new Element("2", TypeElem.FRAME, LayoutArea.LEFT));
            rootArea.add(new Element("3", TypeElem.FRAME, LayoutArea.RIGHT));
            rootArea.add(new Element("4", TypeElem.FRAME, LayoutArea.ARCH));
            rootArea.add(new Element("5", TypeElem.FRAME, LayoutArea.BOTTOM));

            rootArea.add(new Element("6", TypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem("7", LayoutArea.HORIZONTAL, TypeElem.AREA, 1200));

            rootArea.add(new Element("8", TypeElem.GLASS));
            area3.add(new Element("9", TypeElem.GLASS));
            return new Gson().toJson(rootArea);
        }
        return null;
    }
}
