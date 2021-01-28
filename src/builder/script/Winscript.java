package builder.script;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import domain.eSetting;
import enums.LayoutArea;
import enums.TypeElem;

public class Winscript {

    public static AreaRoot rootArea;

    public static String test(Integer project, Integer nuni) {
        if ("ps4".equals(eSetting.find(2).get(eSetting.val)) == true) {
            return builder.script.Winscript.testPs4(project, null);
        } else {
            return builder.script.Winscript.testPs3(project, null);
        }
    }

    public static String testPs4(Integer project, Integer nuni) {

        if (project == 601001) {
            rootArea = new AreaRoot(1, LayoutArea.VERT, TypeElem.RECTANGL, 900, 1300, 1300, 1009, 10009, 1009, "");
            rootArea.setParam(project.toString(), 387, "KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)");
            rootArea.add(new Element(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.add(new Element(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.add(new Element(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootArea.add(new Element(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            AreaElem area2 = (AreaElem) rootArea.add(new AreaElem(6, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':1634}"));
            area2.add(new Element(7, TypeElem.GLASS));

        } else if (project == 601002) {
            rootArea = new AreaRoot(1, LayoutArea.HORIZ, TypeElem.RECTANGL, 1300, 1400, 1400, 1009, 10009, 1009, "");
            rootArea.setParam(project.toString(), 433, "Montblanc\\Nord\\1 ОКНА");
            rootArea.add(new Element(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.add(new Element(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.add(new Element(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootArea.add(new Element(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            AreaElem area2 = (AreaElem) rootArea.add(new AreaElem(6, LayoutArea.HORIZ, TypeElem.AREA, 1300 / 2));
            rootArea.add(new Element(7, TypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem(8, LayoutArea.HORIZ, TypeElem.AREA, 1300 / 2));
            AreaElem area4 = (AreaElem) area2.add(new AreaElem(9, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':3, 'sysfurnID':860}"));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem(10, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':860}"));
            area4.add(new Element(11, TypeElem.GLASS));
            area5.add(new Element(12, TypeElem.GLASS));

        } else if (project == 601003) {
            rootArea = new AreaRoot(1, LayoutArea.VERT, TypeElem.RECTANGL, 1440, 1700, 1700, 1009, 1009, 1009, "");
            rootArea.setParam(project.toString(), 4, "Darrio\\DARRIO 200\\1 ОКНА");
            rootArea.add(new Element(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.add(new Element(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.add(new Element(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootArea.add(new Element(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            AreaElem area2 = (AreaElem) rootArea.add(new AreaElem(6, LayoutArea.HORIZ, TypeElem.AREA, 400));
            rootArea.add(new Element(7, TypeElem.IMPOST));
            AreaElem area4 = (AreaElem) rootArea.add(new AreaElem(8, LayoutArea.HORIZ, TypeElem.AREA, 1300));
            AreaElem area5 = (AreaElem) area4.add(new AreaElem(9, LayoutArea.VERT, TypeElem.AREA, 720));
            area4.add(new Element(10, TypeElem.IMPOST));
            AreaElem area6 = (AreaElem) area4.add(new AreaElem(11, LayoutArea.VERT, TypeElem.AREA, 720));
            AreaElem area8 = (AreaElem) area5.add(new AreaElem(12, LayoutArea.HORIZ, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':389}"));
            AreaElem area9 = (AreaElem) area6.add(new AreaElem(13, LayoutArea.HORIZ, TypeElem.STVORKA, "{'typeOpen':3, 'sysfurnID':819}"));
            area2.add(new Element(14, TypeElem.GLASS));
            area8.add(new Element(15, TypeElem.GLASS));
            area9.add(new Element(16, TypeElem.GLASS));

        } else if (project == 601004) {
            rootArea = new AreaRoot(1, LayoutArea.VERT, TypeElem.RECTANGL, 1440, 1700, 1700, 1009, 1009, 1009, "");
            rootArea.setParam(project.toString(), 387, "KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)");
            rootArea.add(new Element(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.add(new Element(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.add(new Element(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootArea.add(new Element(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            AreaElem area2 = (AreaElem) rootArea.add(new AreaElem(6, LayoutArea.HORIZ, TypeElem.AREA, 400));
            rootArea.add(new Element(7, TypeElem.IMPOST));
            AreaElem area4 = (AreaElem) rootArea.add(new AreaElem(8, LayoutArea.HORIZ, TypeElem.AREA, 1300));
            AreaElem area5 = (AreaElem) area4.add(new AreaElem(9, LayoutArea.VERT, TypeElem.AREA, 720));
            area4.add(new Element(10, TypeElem.IMPOST));
            AreaElem area6 = (AreaElem) area4.add(new AreaElem(11, LayoutArea.VERT, TypeElem.AREA, 720));
            AreaElem area8 = (AreaElem) area5.add(new AreaElem(12, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':1634}"));
            AreaElem area9 = (AreaElem) area6.add(new AreaElem(13, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':1633}"));
            area2.add(new Element(14, TypeElem.GLASS));
            area8.add(new Element(15, TypeElem.GLASS));
            area9.add(new Element(16, TypeElem.GLASS));

        } else if (project == 601005) {
            rootArea = new AreaRoot(1, LayoutArea.HORIZ, TypeElem.RECTANGL, 1600, 1700, 1700, 1009, 1009, 1009, "");
            rootArea.setParam(project.toString(), 387, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)");
            rootArea.add(new Element(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.add(new Element(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.add(new Element(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootArea.add(new Element(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            AreaElem area2 = (AreaElem) rootArea.add(new AreaElem(6, LayoutArea.VERT, TypeElem.AREA, 800));
            rootArea.add(new Element(7, TypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem(8, LayoutArea.VERT, TypeElem.AREA, 800));
            AreaElem area4 = (AreaElem) area2.add(new AreaElem(9, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':1634}"));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem(10, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':1633}"));
            area4.add(new Element(11, TypeElem.GLASS));
            area5.add(new Element(12, TypeElem.GLASS));

        } else if (project == 601006) {
            rootArea = new AreaRoot(1, LayoutArea.HORIZ, TypeElem.RECTANGL, 900, 1400, 1400, 1009, 1009, 1009, "");
            rootArea.setParam(project.toString(), 10, "RAZIO\\RAZIO 58 N\\1 ОКНА");
            rootArea.add(new Element(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.add(new Element(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.add(new Element(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootArea.add(new Element(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootArea.add(new Element(6, TypeElem.GLASS, "{'artglasID':5746}")); //или 'R4x10x4x10x4'

        } else if (project == 601007) {
            rootArea = new AreaRoot(1, LayoutArea.VERT, TypeElem.RECTANGL, 1100, 1400, 1400, 1009, 10018, 10018, "");
            rootArea.setParam(project.toString(), 5, "NOVOTEX\\Techno 58\\1 ОКНА");
            rootArea.add(new Element(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.add(new Element(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.add(new Element(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootArea.add(new Element(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            AreaElem area2 = (AreaElem) rootArea.add(new AreaElem(6, LayoutArea.HORIZ, TypeElem.AREA, 300));
            rootArea.add(new Element(7, TypeElem.IMPOST));
            AreaElem area4 = (AreaElem) rootArea.add(new AreaElem(8, LayoutArea.HORIZ, TypeElem.AREA, 1100));
            AreaElem area5 = (AreaElem) area4.add(new AreaElem(9, LayoutArea.VERT, TypeElem.AREA, 550));
            area4.add(new Element(10, TypeElem.IMPOST));
            AreaElem area6 = (AreaElem) area4.add(new AreaElem(11, LayoutArea.VERT, TypeElem.AREA, 550));
            AreaElem area8 = (AreaElem) area5.add(new AreaElem(12, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':1537}"));
            AreaElem area9 = (AreaElem) area6.add(new AreaElem(13, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':1536}"));
            area2.add(new Element(14, TypeElem.GLASS));
            area8.add(new Element(15, TypeElem.GLASS));
            area9.add(new Element(16, TypeElem.GLASS));

        } else if (project == 601008) {
            rootArea = new AreaRoot(1, LayoutArea.HORIZ, TypeElem.RECTANGL, 1200, 1700, 1700, 1009, 28014, 21057, "");
            rootArea.setParam(project.toString(), 9, "Rehau\\Blitz new\\1 ОКНА");
            rootArea.add(new Element(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.add(new Element(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.add(new Element(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootArea.add(new Element(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            AreaElem area2 = (AreaElem) rootArea.add(new AreaElem(6, LayoutArea.HORIZ, TypeElem.AREA, 600));
            rootArea.add(new Element(7, TypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem(8, LayoutArea.VERT, TypeElem.AREA, 600));
            AreaElem area4 = (AreaElem) area3.add(new AreaElem(9, LayoutArea.VERT, TypeElem.AREA, 550));
            area3.add(new Element(10, TypeElem.IMPOST));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem(11, LayoutArea.VERT, TypeElem.AREA, 1150));
            AreaElem area6 = (AreaElem) area4.add(new AreaElem(12, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':534}"));
            area2.add(new Element(13, TypeElem.GLASS));
            area6.add(new Element(14, TypeElem.GLASS));
            area5.add(new Element(15, TypeElem.GLASS));

        } else if (project == 601009) {
            rootArea = new AreaRoot(1, LayoutArea.HORIZ, TypeElem.RECTANGL, 700, 1400, 1400, 1009, 1009, 1009, "");
            rootArea.setParam(project.toString(), 371, "KBE\\KBE Эксперт\\1 ОКНА\\Открывание внутрь (ств. Z 77)");
            rootArea.add(new Element(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.add(new Element(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.add(new Element(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootArea.add(new Element(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootArea.add(new Element(6, TypeElem.GLASS, "{'artglasID':4663}")); //или '4x12x4x12x4'

        } else if (project == 601010) {
            rootArea = new AreaRoot(1, LayoutArea.HORIZ, TypeElem.RECTANGL, 1300, 1400, 1400, 1009, 1009, 1009, "{'sysprofID':1120, 'ioknaParam':[-2147482183,-2147482226, -2147482193]}");
            rootArea.setParam(project.toString(), 371, "KBE\\KBE Эксперт\\1 ОКНА\\Открывание внутрь (ств. Z 77)");
            rootArea.add(new Element(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.add(new Element(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.add(new Element(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootArea.add(new Element(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            AreaElem area2 = (AreaElem) rootArea.add(new AreaElem(6, LayoutArea.VERT, TypeElem.AREA, 650));
            rootArea.add(new Element(7, TypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem(8, LayoutArea.VERT, TypeElem.AREA, 650));
            AreaElem area4 = (AreaElem) area2.add(new AreaElem(9, LayoutArea.VERT, TypeElem.STVORKA, "{'sysprofID':1121, 'typeOpen':1,'sysfurnID':2335}"));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem(10, LayoutArea.VERT, TypeElem.STVORKA, "{'sysprofID':1121, 'typeOpen':4,'sysfurnID':2916}"));
            area4.add(new Element(11, TypeElem.GLASS, "{'artglasID':4663}"));
            area5.add(new Element(12, TypeElem.GLASS, "{'artglasID':4663}"));

        } else if (project == 604004) {
            rootArea = new AreaRoot(1, LayoutArea.VERT, TypeElem.ARCH, 1300, 1050, 1700, 1009, 1009, 1009, "");
            rootArea.setParam(project.toString(), 435, "Rehau\\Delight\\1 ОКНА");
            rootArea.add(new Element(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.add(new Element(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.add(new Element(4, TypeElem.FRAME_SIDE, LayoutArea.ARCH));
            rootArea.add(new Element(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootArea.add(new Element(6, TypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem(7, LayoutArea.HORIZ, TypeElem.AREA, 1050));
            AreaElem area4 = (AreaElem) area3.add(new AreaElem(8, LayoutArea.VERT, TypeElem.AREA, 650));
            area3.add(new Element(9, TypeElem.IMPOST));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem(10, LayoutArea.VERT, TypeElem.AREA, 650));
            AreaElem area6 = (AreaElem) area5.add(new AreaElem(11, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':91}"));
            rootArea.add(new Element(12, TypeElem.GLASS));
            area4.add(new Element(13, TypeElem.GLASS));
            area6.add(new Element(14, TypeElem.GLASS));

        } else if (project == 604005) {
            rootArea = new AreaRoot(1, LayoutArea.VERT, TypeElem.ARCH, 1300, 1200, 1500, 1009, 10009, 1009, "");
            rootArea.setParam(project.toString(), 14, "Wintech\\Termotech 742\\1 ОКНА");
            rootArea.add(new Element(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.add(new Element(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.add(new Element(4, TypeElem.FRAME_SIDE, LayoutArea.ARCH));
            rootArea.add(new Element(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootArea.add(new Element(6, TypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem(7, LayoutArea.HORIZ, TypeElem.AREA, 1200));
            AreaElem area4 = (AreaElem) area3.add(new AreaElem(8, LayoutArea.VERT, TypeElem.AREA, 650));
            area3.add(new Element(9, TypeElem.IMPOST));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem(10, LayoutArea.VERT, TypeElem.AREA, 650));
            AreaElem area6 = (AreaElem) area4.add(new AreaElem(11, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':2745}"));
            AreaElem area7 = (AreaElem) area5.add(new AreaElem(12, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':2744}"));
            rootArea.add(new Element(13, TypeElem.GLASS));
            area6.add(new Element(14, TypeElem.GLASS));
            area7.add(new Element(15, TypeElem.GLASS));

        } else if (project == 604006) {
            rootArea = new AreaRoot(1, LayoutArea.VERT, TypeElem.ARCH, 1100, 1220, 1600, 1009, 1009, 10012, "");
            rootArea.setParam(project.toString(), 14, "Wintech\\Termotech 742\\1 ОКНА");
            rootArea.add(new Element(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.add(new Element(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.add(new Element(4, TypeElem.FRAME_SIDE, LayoutArea.ARCH));
            rootArea.add(new Element(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootArea.add(new Element(6, TypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem(7, LayoutArea.HORIZ, TypeElem.AREA, 1220));
            AreaElem area4 = (AreaElem) area3.add(new AreaElem(8, LayoutArea.VERT, TypeElem.AREA, 550));
            area3.add(new Element(9, TypeElem.IMPOST));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem(10, LayoutArea.VERT, TypeElem.AREA, 550));
            AreaElem area6 = (AreaElem) area4.add(new AreaElem(11, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':2745}"));
            AreaElem area7 = (AreaElem) area5.add(new AreaElem(12, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':2744}"));
            rootArea.add(new Element(13, TypeElem.GLASS));
            area6.add(new Element(14, TypeElem.GLASS));
            area7.add(new Element(15, TypeElem.GLASS));

        } else if (project == 604007) {
            rootArea = new AreaRoot(1, LayoutArea.VERT, TypeElem.ARCH, 1400, 1300, 1700, 1009, 1009, 10001, "");
            rootArea.setParam(project.toString(), 9, "Rehau\\Blitz new\\1 ОКНА");
            rootArea.add(new Element(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.add(new Element(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.add(new Element(4, TypeElem.FRAME_SIDE, LayoutArea.ARCH));
            rootArea.add(new Element(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootArea.add(new Element(6, TypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem(7, LayoutArea.HORIZ, TypeElem.AREA, 1300));
            AreaElem area4 = (AreaElem) area3.add(new AreaElem(8, LayoutArea.VERT, TypeElem.AREA, 700));
            area3.add(new Element(9, TypeElem.IMPOST));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem(10, LayoutArea.VERT, TypeElem.AREA, 700));
            AreaElem area6 = (AreaElem) area4.add(new AreaElem(11, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':535}"));
            AreaElem area7 = (AreaElem) area5.add(new AreaElem(12, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':3, 'sysfurnID':534}"));
            rootArea.add(new Element(13, TypeElem.GLASS));
            area6.add(new Element(14, TypeElem.GLASS));
            area7.add(new Element(15, TypeElem.GLASS));

        } else if (project == 604008) {
            rootArea = new AreaRoot(1, LayoutArea.VERT, TypeElem.ARCH, 1300, 1200, 1500, 1009, 10009, 1009, "");
            rootArea.setParam(project.toString(), 387, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)");
            rootArea.add(new Element(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.add(new Element(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.add(new Element(4, TypeElem.FRAME_SIDE, LayoutArea.ARCH));
            rootArea.add(new Element(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootArea.add(new Element(6, TypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem(7, LayoutArea.HORIZ, TypeElem.AREA, 1200));
            AreaElem area4 = (AreaElem) area3.add(new AreaElem(8, LayoutArea.VERT, TypeElem.AREA, 650));
            area3.add(new Element(9, TypeElem.IMPOST));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem(10, LayoutArea.VERT, TypeElem.AREA, 650));
            AreaElem area6 = (AreaElem) area4.add(new AreaElem(11, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':3, 'sysfurnID':-1}"));
            AreaElem area7 = (AreaElem) area5.add(new AreaElem(12, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':3, 'sysfurnID':-1}"));
            rootArea.add(new Element(13, TypeElem.GLASS));
            area6.add(new Element(14, TypeElem.GLASS));
            area7.add(new Element(15, TypeElem.GLASS));

        } else if (project == 604009) {
            rootArea = new AreaRoot(1, LayoutArea.VERT, TypeElem.ARCH, 1300, 1200, 1500, 1009, 10009, 1009, "");
            rootArea.setParam(project.toString(), 387, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)");
            rootArea.add(new Element(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.add(new Element(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.add(new Element(4, TypeElem.FRAME_SIDE, LayoutArea.ARCH));
            rootArea.add(new Element(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootArea.add(new Element(6, TypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem(7, LayoutArea.HORIZ, TypeElem.AREA, 1200));
            rootArea.add(new Element(8, TypeElem.GLASS));
            area3.add(new Element(9, TypeElem.GLASS));

        } else if (project == 604010) {
            rootArea = new AreaRoot(1, LayoutArea.VERT, TypeElem.ARCH, 1300, 1400, 1700, 1009, 10009, 1009, "");
            rootArea.setParam(project.toString(), 433, "Montblanc\\Nord\\1 ОКНА");
            rootArea.add(new Element(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.add(new Element(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.add(new Element(4, TypeElem.FRAME_SIDE, LayoutArea.ARCH));
            rootArea.add(new Element(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootArea.add(new Element(6, TypeElem.IMPOST));
            AreaElem area7 = (AreaElem) rootArea.add(new AreaElem(7, LayoutArea.HORIZ, TypeElem.AREA, 1400));
            AreaElem area8 = (AreaElem) area7.add(new AreaElem(8, LayoutArea.VERT, TypeElem.AREA, 650));
            area7.add(new Element(9, TypeElem.IMPOST));
            AreaElem area10 = (AreaElem) area7.add(new AreaElem(10, LayoutArea.VERT, TypeElem.AREA, 650));
            AreaElem area11 = (AreaElem) area10.add(new AreaElem(11, LayoutArea.VERT, TypeElem.AREA, 550));
            area10.add(new Element(12, TypeElem.IMPOST));
            AreaElem area13 = (AreaElem) area10.add(new AreaElem(13, LayoutArea.VERT, TypeElem.AREA, 850));
            AreaElem area14 = (AreaElem) area11.add(new AreaElem(14, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':701}"));
            rootArea.add(new Element(15, TypeElem.GLASS));
            area8.add(new Element(16, TypeElem.GLASS));
            area13.add(new Element(17, TypeElem.GLASS));
            area14.add(new Element(18, TypeElem.GLASS));

        } else if (project == 605001) {
            rootArea = new AreaRoot(1, LayoutArea.VERT, TypeElem.TRAPEZE, 1300, 1200, 1500, 1009, 10009, 1009, "");
            rootArea.setParam(project.toString(), 387, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)");
            rootArea.add(new Element(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.add(new Element(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.add(new Element(4, TypeElem.FRAME_SIDE, LayoutArea.ARCH));
            rootArea.add(new Element(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootArea.add(new Element(6, TypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem(7, LayoutArea.HORIZ, TypeElem.AREA, 1200));
            rootArea.add(new Element(8, TypeElem.GLASS));
            area3.add(new Element(9, TypeElem.GLASS));

        } else {
            return null;
        }
        if (nuni != null) {
            rootArea.setParam(project.toString(), nuni);
        }
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        return builder.create().toJson(rootArea);
    }

    public static String testPs3(Integer project, Integer nuni) {

        if (project == 601001) {
            rootArea = new AreaRoot(1, LayoutArea.VERT, TypeElem.RECTANGL, 900, 600, 600, 25, 25, 25, "");
            rootArea.setParam(project.toString(), 23, "СИАЛ\\КП40\\Окна");
            rootArea.add(new Element(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.add(new Element(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.add(new Element(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootArea.add(new Element(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            AreaElem area2 = (AreaElem) rootArea.add(new AreaElem(6, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':2}"));
            area2.add(new Element(7, TypeElem.GLASS, "{'artglasID':285}"));

        } else if (project == 601002) {
            rootArea = new AreaRoot(1, LayoutArea.HORIZ, TypeElem.RECTANGL, 1200, 1600, 1600, 25, 25, 25, "");
            rootArea.setParam(project.toString(), 23, "СИАЛ\\КП40\\Окна");
            rootArea.add(new Element(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.add(new Element(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.add(new Element(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootArea.add(new Element(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            AreaElem area2 = (AreaElem) rootArea.add(new AreaElem(6, LayoutArea.HORIZ, TypeElem.AREA, 1200 / 2));
            rootArea.add(new Element(7, TypeElem.IMPOST, "{'sysprofID':357}"));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem(8, LayoutArea.HORIZ, TypeElem.AREA, 1200 / 2));
            AreaElem area4 = (AreaElem) area2.add(new AreaElem(9, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':3, 'sysfurnID':143}"));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem(10, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':143}"));
            area4.add(new Element(11, TypeElem.GLASS, "{'artglasID':285}"));
            area5.add(new Element(12, TypeElem.GLASS, "{'artglasID':285}"));

        } else if (project == 601006) {
            rootArea = new AreaRoot(1, LayoutArea.HORIZ, TypeElem.RECTANGL, 800, 1200, 1200, 25, 25, 25, "");
            rootArea.setParam(project.toString(), 24, "СИАЛ\\КП45\\Окна");
            rootArea.add(new Element(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.add(new Element(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.add(new Element(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootArea.add(new Element(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootArea.add(new Element(6, TypeElem.GLASS, "{'artglasID':285}"));
        } else {
            return null;
        }
        if (nuni != null) {
            rootArea.setParam(project.toString(), nuni);
        }
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        return builder.create().toJson(rootArea);
    }
}
