package builder.script;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import domain.eSetting;
import enums.LayoutArea;
import enums.TypeElem;

public class Winscript {

    public static GsonRoot rootArea;

    public static String test(Integer project, boolean model) {
        if ("ps4".equals(eSetting.find(2).get(eSetting.val)) == true) {
            return builder.script.Winscript.testPs4(project, model);
        } else {
            return builder.script.Winscript.testPs3(project, model);
        }
    }

    public static String testPs4(Integer project, boolean model) {

        if (project == 601001) {
            rootArea = new GsonRoot(1, LayoutArea.VERT, TypeElem.RECTANGL, 900, 1300, 1009, 10009, 1009);
            rootArea.propery(project.toString(), 8, "KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)");
            rootArea.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootArea.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            GsonElem area2 = (GsonElem) rootArea.addArea(new GsonElem(6, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':1634}"));
            area2.addElem(new GsonElem(7, TypeElem.GLASS));

        } else if (project == 601002) {
            rootArea = new GsonRoot(1, LayoutArea.HORIZ, TypeElem.RECTANGL, 1300, 1400, 1009, 10009, 1009);
            rootArea.propery(project.toString(), 29, "Montblanc\\Nord\\1 ОКНА");
            rootArea.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootArea.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            GsonElem area2 = (GsonElem) rootArea.addArea(new GsonElem(6, LayoutArea.HORIZ, TypeElem.AREA, 1300 / 2));
            rootArea.addElem(new GsonElem(7, TypeElem.IMPOST));
            GsonElem area3 = (GsonElem) rootArea.addArea(new GsonElem(8, LayoutArea.HORIZ, TypeElem.AREA, 1300 / 2));
            GsonElem area4 = (GsonElem) area2.addArea(new GsonElem(9, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':3, 'sysfurnID':860}"));
            GsonElem area5 = (GsonElem) area3.addArea(new GsonElem(10, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':860}"));
            area4.addElem(new GsonElem(11, TypeElem.GLASS));
            area5.addElem(new GsonElem(12, TypeElem.GLASS));

        } else if (project == 601003) {
            rootArea = new GsonRoot(1, LayoutArea.VERT, TypeElem.RECTANGL, 1440, 1700, 1009, 1009, 1009);
            rootArea.propery(project.toString(), 81, "Darrio\\DARRIO 200\\1 ОКНА");
            rootArea.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootArea.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            GsonElem area2 = (GsonElem) rootArea.addArea(new GsonElem(6, LayoutArea.HORIZ, TypeElem.AREA, 400));
            rootArea.addElem(new GsonElem(7, TypeElem.IMPOST));
            GsonElem area4 = (GsonElem) rootArea.addArea(new GsonElem(8, LayoutArea.HORIZ, TypeElem.AREA, 1300));
            GsonElem area5 = (GsonElem) area4.addArea(new GsonElem(9, LayoutArea.VERT, TypeElem.AREA, 720));
            area4.addElem(new GsonElem(10, TypeElem.IMPOST));
            GsonElem area6 = (GsonElem) area4.addArea(new GsonElem(11, LayoutArea.VERT, TypeElem.AREA, 720));
            GsonElem area8 = (GsonElem) area5.addArea(new GsonElem(12, LayoutArea.HORIZ, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':389}"));
            GsonElem area9 = (GsonElem) area6.addArea(new GsonElem(13, LayoutArea.HORIZ, TypeElem.STVORKA, "{'typeOpen':3, 'sysfurnID':819}"));
            area2.addElem(new GsonElem(14, TypeElem.GLASS));
            area8.addElem(new GsonElem(15, TypeElem.GLASS));
            area9.addElem(new GsonElem(16, TypeElem.GLASS));

        } else if (project == 601004) {
            rootArea = new GsonRoot(1, LayoutArea.VERT, TypeElem.RECTANGL, 1440, 1700, 1009, 1009, 1009);
            rootArea.propery(project.toString(), 8, "KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)");
            rootArea.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootArea.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            GsonElem area2 = (GsonElem) rootArea.addArea(new GsonElem(6, LayoutArea.HORIZ, TypeElem.AREA, 400));
            rootArea.addElem(new GsonElem(7, TypeElem.IMPOST));
            GsonElem area4 = (GsonElem) rootArea.addArea(new GsonElem(8, LayoutArea.HORIZ, TypeElem.AREA, 1300));
            GsonElem area5 = (GsonElem) area4.addArea(new GsonElem(9, LayoutArea.VERT, TypeElem.AREA, 720));
            area4.addElem(new GsonElem(10, TypeElem.IMPOST));
            GsonElem area6 = (GsonElem) area4.addArea(new GsonElem(11, LayoutArea.VERT, TypeElem.AREA, 720));
            GsonElem area8 = (GsonElem) area5.addArea(new GsonElem(12, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':1634}"));
            GsonElem area9 = (GsonElem) area6.addArea(new GsonElem(13, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':1633}"));
            area2.addElem(new GsonElem(14, TypeElem.GLASS));
            area8.addElem(new GsonElem(15, TypeElem.GLASS));
            area9.addElem(new GsonElem(16, TypeElem.GLASS));

        } else if (project == 601005) {
            rootArea = new GsonRoot(1, LayoutArea.HORIZ, TypeElem.RECTANGL, 1600, 1700, 1009, 1009, 1009);
            rootArea.propery(project.toString(), 8, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)");
            rootArea.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootArea.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            GsonElem area2 = (GsonElem) rootArea.addArea(new GsonElem(6, LayoutArea.VERT, TypeElem.AREA, 800));
            rootArea.addElem(new GsonElem(7, TypeElem.IMPOST));
            GsonElem area3 = (GsonElem) rootArea.addArea(new GsonElem(8, LayoutArea.VERT, TypeElem.AREA, 800));
            GsonElem area4 = (GsonElem) area2.addArea(new GsonElem(9, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':1634}"));
            GsonElem area5 = (GsonElem) area3.addArea(new GsonElem(10, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':1633}"));
            area4.addElem(new GsonElem(11, TypeElem.GLASS));
            area5.addElem(new GsonElem(12, TypeElem.GLASS));

        } else if (project == 601006) {
            rootArea = new GsonRoot(1, LayoutArea.HORIZ, TypeElem.RECTANGL, 900, 1400, 1009, 1009, 1009);
            rootArea.propery(project.toString(), 110, "RAZIO\\RAZIO 58 N\\1 ОКНА");
            rootArea.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootArea.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootArea.addElem(new GsonElem(6, TypeElem.GLASS, "{'artglasID':5746}")); //или 'R4x10x4x10x4'

        } else if (project == 601007) {
            rootArea = new GsonRoot(1, LayoutArea.VERT, TypeElem.RECTANGL, 1100, 1400, 1009, 10018, 10018);
            rootArea.propery(project.toString(), 87, "NOVOTEX\\Techno 58\\1 ОКНА");
            rootArea.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootArea.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            GsonElem area2 = (GsonElem) rootArea.addArea(new GsonElem(6, LayoutArea.HORIZ, TypeElem.AREA, 300));
            rootArea.addElem(new GsonElem(7, TypeElem.IMPOST));
            GsonElem area4 = (GsonElem) rootArea.addArea(new GsonElem(8, LayoutArea.HORIZ, TypeElem.AREA, 1100));
            GsonElem area5 = (GsonElem) area4.addArea(new GsonElem(9, LayoutArea.VERT, TypeElem.AREA, 550));
            area4.addElem(new GsonElem(10, TypeElem.IMPOST));
            GsonElem area6 = (GsonElem) area4.addArea(new GsonElem(11, LayoutArea.VERT, TypeElem.AREA, 550));
            GsonElem area8 = (GsonElem) area5.addArea(new GsonElem(12, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':1537}"));
            GsonElem area9 = (GsonElem) area6.addArea(new GsonElem(13, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':1536}"));
            area2.addElem(new GsonElem(14, TypeElem.GLASS));
            area8.addElem(new GsonElem(15, TypeElem.GLASS));
            area9.addElem(new GsonElem(16, TypeElem.GLASS));

        } else if (project == 601008) {
            rootArea = new GsonRoot(1, LayoutArea.HORIZ, TypeElem.RECTANGL, 1200, 1700, 1009, 28014, 21057);
            rootArea.propery(project.toString(), 99, "Rehau\\Blitz new\\1 ОКНА");
            rootArea.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootArea.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            GsonElem area2 = (GsonElem) rootArea.addArea(new GsonElem(6, LayoutArea.HORIZ, TypeElem.AREA, 600));
            rootArea.addElem(new GsonElem(7, TypeElem.IMPOST));
            GsonElem area3 = (GsonElem) rootArea.addArea(new GsonElem(8, LayoutArea.VERT, TypeElem.AREA, 600));
            GsonElem area4 = (GsonElem) area3.addArea(new GsonElem(9, LayoutArea.VERT, TypeElem.AREA, 550));
            area3.addElem(new GsonElem(10, TypeElem.IMPOST));
            GsonElem area5 = (GsonElem) area3.addArea(new GsonElem(11, LayoutArea.VERT, TypeElem.AREA, 1150));
            GsonElem area6 = (GsonElem) area4.addArea(new GsonElem(12, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':534}"));
            area2.addElem(new GsonElem(13, TypeElem.GLASS));
            area6.addElem(new GsonElem(14, TypeElem.GLASS));
            area5.addElem(new GsonElem(15, TypeElem.GLASS));

        } else if (project == 601009) {
            rootArea = new GsonRoot(1, LayoutArea.HORIZ, TypeElem.RECTANGL, 700, 1400, 1009, 1009, 1009);
            rootArea.propery(project.toString(), 54, "KBE\\KBE Эксперт\\1 ОКНА\\Открывание внутрь (ств. Z 77)");
            rootArea.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootArea.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootArea.addElem(new GsonElem(6, TypeElem.GLASS, "{'artglasID':4663}")); //или '4x12x4x12x4'

        } else if (project == 601010) {
            rootArea = new GsonRoot(1, LayoutArea.HORIZ, TypeElem.RECTANGL, 1300, 1400, 1009, 1009, 1009, "{'sysprofID':1120, 'ioknaParam':[-2147482183,-2147482226, -2147482193]}");
            rootArea.propery(project.toString(), 54, "KBE\\KBE Эксперт\\1 ОКНА\\Открывание внутрь (ств. Z 77)");
            rootArea.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootArea.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            GsonElem area2 = (GsonElem) rootArea.addArea(new GsonElem(6, LayoutArea.VERT, TypeElem.AREA, 650));
            rootArea.addElem(new GsonElem(7, TypeElem.IMPOST));
            GsonElem area3 = (GsonElem) rootArea.addArea(new GsonElem(8, LayoutArea.VERT, TypeElem.AREA, 650));
            GsonElem area4 = (GsonElem) area2.addArea(new GsonElem(9, LayoutArea.VERT, TypeElem.STVORKA, "{'sysprofID':1121, 'typeOpen':1,'sysfurnID':2335}"));
            GsonElem area5 = (GsonElem) area3.addArea(new GsonElem(10, LayoutArea.VERT, TypeElem.STVORKA, "{'sysprofID':1121, 'typeOpen':4,'sysfurnID':2916}"));
            area4.addElem(new GsonElem(11, TypeElem.GLASS, "{'artglasID':4663}"));
            area5.addElem(new GsonElem(12, TypeElem.GLASS, "{'artglasID':4663}"));

        } else if (project == 604004) {
            rootArea = new GsonRoot(1, LayoutArea.VERT, TypeElem.ARCH, 1300, 1050, 1700, 1009, 1009, 1009);
            rootArea.propery(project.toString(), 37, "Rehau\\Delight\\1 ОКНА");
            rootArea.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.ARCH));
            rootArea.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootArea.addElem(new GsonElem(6, TypeElem.IMPOST));
            GsonElem area3 = (GsonElem) rootArea.addArea(new GsonElem(7, LayoutArea.HORIZ, TypeElem.AREA, 1050));
            GsonElem area4 = (GsonElem) area3.addArea(new GsonElem(8, LayoutArea.VERT, TypeElem.AREA, 650));
            area3.addElem(new GsonElem(9, TypeElem.IMPOST));
            GsonElem area5 = (GsonElem) area3.addArea(new GsonElem(10, LayoutArea.VERT, TypeElem.AREA, 650));
            GsonElem area6 = (GsonElem) area5.addArea(new GsonElem(11, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':91}"));
            rootArea.addElem(new GsonElem(12, TypeElem.GLASS));
            area4.addElem(new GsonElem(13, TypeElem.GLASS));
            area6.addElem(new GsonElem(14, TypeElem.GLASS));

        } else if (project == 604005) {
            rootArea = new GsonRoot(1, LayoutArea.VERT, TypeElem.ARCH, 1300, 1200, 1500, 1009, 10009, 1009);
            rootArea.propery(project.toString(), 135, "Wintech\\Termotech 742\\1 ОКНА");
            rootArea.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.ARCH));
            rootArea.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootArea.addElem(new GsonElem(6, TypeElem.IMPOST));
            GsonElem area3 = (GsonElem) rootArea.addArea(new GsonElem(7, LayoutArea.HORIZ, TypeElem.AREA, 1200));
            GsonElem area4 = (GsonElem) area3.addArea(new GsonElem(8, LayoutArea.VERT, TypeElem.AREA, 650));
            area3.addElem(new GsonElem(9, TypeElem.IMPOST));
            GsonElem area5 = (GsonElem) area3.addArea(new GsonElem(10, LayoutArea.VERT, TypeElem.AREA, 650));
            GsonElem area6 = (GsonElem) area4.addArea(new GsonElem(11, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':2745}"));
            GsonElem area7 = (GsonElem) area5.addArea(new GsonElem(12, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':2744}"));
            rootArea.addElem(new GsonElem(13, TypeElem.GLASS));
            area6.addElem(new GsonElem(14, TypeElem.GLASS));
            area7.addElem(new GsonElem(15, TypeElem.GLASS));

        } else if (project == 604006) {
            rootArea = new GsonRoot(1, LayoutArea.VERT, TypeElem.ARCH, 1100, 1220, 1600, 1009, 1009, 10012);
            rootArea.propery(project.toString(), 135, "Wintech\\Termotech 742\\1 ОКНА");
            rootArea.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.ARCH));
            rootArea.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootArea.addElem(new GsonElem(6, TypeElem.IMPOST));
            GsonElem area3 = (GsonElem) rootArea.addArea(new GsonElem(7, LayoutArea.HORIZ, TypeElem.AREA, 1220));
            GsonElem area4 = (GsonElem) area3.addArea(new GsonElem(8, LayoutArea.VERT, TypeElem.AREA, 550));
            area3.addElem(new GsonElem(9, TypeElem.IMPOST));
            GsonElem area5 = (GsonElem) area3.addArea(new GsonElem(10, LayoutArea.VERT, TypeElem.AREA, 550));
            GsonElem area6 = (GsonElem) area4.addArea(new GsonElem(11, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':2745}"));
            GsonElem area7 = (GsonElem) area5.addArea(new GsonElem(12, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':2744}"));
            rootArea.addElem(new GsonElem(13, TypeElem.GLASS));
            area6.addElem(new GsonElem(14, TypeElem.GLASS));
            area7.addElem(new GsonElem(15, TypeElem.GLASS));

        } else if (project == 604007) {
            rootArea = new GsonRoot(1, LayoutArea.VERT, TypeElem.ARCH, 1400, 1300, 1700, 1009, 1009, 10001);
            rootArea.propery(project.toString(), 99, "Rehau\\Blitz new\\1 ОКНА");
            rootArea.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.ARCH));
            rootArea.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootArea.addElem(new GsonElem(6, TypeElem.IMPOST));
            GsonElem area3 = (GsonElem) rootArea.addArea(new GsonElem(7, LayoutArea.HORIZ, TypeElem.AREA, 1300));
            GsonElem area4 = (GsonElem) area3.addArea(new GsonElem(8, LayoutArea.VERT, TypeElem.AREA, 700));
            area3.addElem(new GsonElem(9, TypeElem.IMPOST));
            GsonElem area5 = (GsonElem) area3.addArea(new GsonElem(10, LayoutArea.VERT, TypeElem.AREA, 700));
            GsonElem area6 = (GsonElem) area4.addArea(new GsonElem(11, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':535}"));
            GsonElem area7 = (GsonElem) area5.addArea(new GsonElem(12, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':3, 'sysfurnID':534}"));
            rootArea.addElem(new GsonElem(13, TypeElem.GLASS));
            area6.addElem(new GsonElem(14, TypeElem.GLASS));
            area7.addElem(new GsonElem(15, TypeElem.GLASS));

        } else if (project == 604008) {
            rootArea = new GsonRoot(1, LayoutArea.VERT, TypeElem.ARCH, 1300, 1200, 1500, 1009, 10009, 1009);
            rootArea.propery(project.toString(), 8, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)");
            rootArea.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.ARCH));
            rootArea.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootArea.addElem(new GsonElem(6, TypeElem.IMPOST));
            GsonElem area3 = (GsonElem) rootArea.addArea(new GsonElem(7, LayoutArea.HORIZ, TypeElem.AREA, 1200));
            GsonElem area4 = (GsonElem) area3.addArea(new GsonElem(8, LayoutArea.VERT, TypeElem.AREA, 650));
            area3.addElem(new GsonElem(9, TypeElem.IMPOST));
            GsonElem area5 = (GsonElem) area3.addArea(new GsonElem(10, LayoutArea.VERT, TypeElem.AREA, 650));
            GsonElem area6 = (GsonElem) area4.addArea(new GsonElem(11, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':3, 'sysfurnID':-1}"));
            GsonElem area7 = (GsonElem) area5.addArea(new GsonElem(12, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':3, 'sysfurnID':-1}"));
            rootArea.addElem(new GsonElem(13, TypeElem.GLASS));
            area6.addElem(new GsonElem(14, TypeElem.GLASS));
            area7.addElem(new GsonElem(15, TypeElem.GLASS));

        } else if (project == 604009) {
            rootArea = new GsonRoot(1, LayoutArea.VERT, TypeElem.ARCH, 1300, 1200, 1500, 1009, 10009, 1009);
            rootArea.propery(project.toString(), 8, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)");
            rootArea.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.ARCH));
            rootArea.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootArea.addElem(new GsonElem(6, TypeElem.IMPOST));
            GsonElem area3 = (GsonElem) rootArea.addArea(new GsonElem(7, LayoutArea.HORIZ, TypeElem.AREA, 1200));
            rootArea.addElem(new GsonElem(8, TypeElem.GLASS));
            area3.addElem(new GsonElem(9, TypeElem.GLASS));

        } else if (project == 604010) {
            rootArea = new GsonRoot(1, LayoutArea.VERT, TypeElem.ARCH, 1300, 1400, 1700, 1009, 10009, 1009);
            rootArea.propery(project.toString(), 29, "Montblanc\\Nord\\1 ОКНА");
            rootArea.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.ARCH));
            rootArea.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootArea.addElem(new GsonElem(6, TypeElem.IMPOST));
            GsonElem area7 = (GsonElem) rootArea.addArea(new GsonElem(7, LayoutArea.HORIZ, TypeElem.AREA, 1400));
            GsonElem area8 = (GsonElem) area7.addArea(new GsonElem(8, LayoutArea.VERT, TypeElem.AREA, 650));
            area7.addElem(new GsonElem(9, TypeElem.IMPOST));
            GsonElem area10 = (GsonElem) area7.addArea(new GsonElem(10, LayoutArea.VERT, TypeElem.AREA, 650));
            GsonElem area11 = (GsonElem) area10.addArea(new GsonElem(11, LayoutArea.VERT, TypeElem.AREA, 550));
            area10.addElem(new GsonElem(12, TypeElem.IMPOST));
            GsonElem area13 = (GsonElem) area10.addArea(new GsonElem(13, LayoutArea.VERT, TypeElem.AREA, 850));
            GsonElem area14 = (GsonElem) area11.addArea(new GsonElem(14, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':701}"));
            rootArea.addElem(new GsonElem(15, TypeElem.GLASS));
            area8.addElem(new GsonElem(16, TypeElem.GLASS));
            area13.addElem(new GsonElem(17, TypeElem.GLASS));
            area14.addElem(new GsonElem(18, TypeElem.GLASS));

        } else if (project == 605001) {
            rootArea = new GsonRoot(1, LayoutArea.VERT, TypeElem.TRAPEZE, 1300, 1200, 1500, 1009, 10009, 1009);
            rootArea.propery(project.toString(), 8, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)");
            rootArea.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.ARCH));
            rootArea.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootArea.addElem(new GsonElem(6, TypeElem.IMPOST));
            GsonElem area3 = (GsonElem) rootArea.addArea(new GsonElem(7, LayoutArea.HORIZ, TypeElem.AREA, 1200));
            rootArea.addElem(new GsonElem(8, TypeElem.GLASS));
            area3.addElem(new GsonElem(9, TypeElem.GLASS));

        } else {
            return null;
        }
        if (model == true) {
            rootArea.propery(project.toString(), -3, null);
        }
        return new GsonBuilder().create().toJson(rootArea);
    }

    public static String testPs3(Integer project, boolean model) {

        if (project == 601001) {
            rootArea = new GsonRoot(1, LayoutArea.VERT, TypeElem.RECTANGL, 800, 1200, 25, 25, 25);
            rootArea.propery(project.toString(), 12, "СИАЛ\\КП45\\Окна");
            rootArea.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootArea.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootArea.addElem(new GsonElem(6, TypeElem.GLASS, "{'artglasID':285}"));

        } else if (project == 601002) {
            rootArea = new GsonRoot(1, LayoutArea.VERT, TypeElem.RECTANGL, 900, 600, 25, 25, 25);
            rootArea.propery(project.toString(), 32, "СИАЛ\\КП40\\Окна");
            rootArea.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootArea.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            GsonElem area2 = (GsonElem) rootArea.addArea(new GsonElem(6, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':2}"));
            area2.addElem(new GsonElem(7, TypeElem.GLASS, "{'artglasID':285}"));

        } else if (project == 601003) {
            rootArea = new GsonRoot(1, LayoutArea.HORIZ, TypeElem.RECTANGL, 1200, 1600, 30, 30, 30);
            rootArea.propery(project.toString(), 32, "СИАЛ\\КП40\\Окна");
            rootArea.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootArea.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            GsonElem area2 = (GsonElem) rootArea.addArea(new GsonElem(6, LayoutArea.HORIZ, TypeElem.AREA, 1200 / 2));
            rootArea.addElem(new GsonElem(7, TypeElem.IMPOST, "{'sysprofID':357}"));
            GsonElem area3 = (GsonElem) rootArea.addArea(new GsonElem(8, LayoutArea.HORIZ, TypeElem.AREA, 1200 / 2));
            GsonElem area4 = (GsonElem) area2.addArea(new GsonElem(9, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':3, 'sysfurnID':143}"));
            GsonElem area5 = (GsonElem) area3.addArea(new GsonElem(10, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':143}"));
            area4.addElem(new GsonElem(11, TypeElem.GLASS, "{'artglasID':285}"));
            area5.addElem(new GsonElem(12, TypeElem.GLASS, "{'artglasID':285}"));

        } else if (project == 601004) {
            rootArea = new GsonRoot(1, LayoutArea.HORIZ, TypeElem.RECTANGL, 2010, 1600, 27, 27, 27);
            rootArea.propery(project.toString(), 12, "СИАЛ\\КП45\\Окна\\");
            rootArea.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootArea.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            GsonElem area2 = (GsonElem) rootArea.addArea(new GsonElem(6, LayoutArea.HORIZ, TypeElem.AREA, 670));
            rootArea.addElem(new GsonElem(7, TypeElem.IMPOST));
            GsonElem area3 = (GsonElem) rootArea.addArea(new GsonElem(8, LayoutArea.HORIZ, TypeElem.AREA, 670));
            rootArea.addElem(new GsonElem(9, TypeElem.IMPOST));
            GsonElem area4 = rootArea.addArea(new GsonElem(10, LayoutArea.HORIZ, TypeElem.AREA, 670));
            GsonElem area5 = area2.addArea(new GsonElem(11, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':143}"));
            GsonElem area6 = area4.addArea(new GsonElem(12, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':2, 'sysfurnID':143}"));
            area5.addElem(new GsonElem(13, TypeElem.GLASS, "{'artglasID':283}"));
            area6.addElem(new GsonElem(14, TypeElem.GLASS, "{'artglasID':283}"));
            area3.addElem(new GsonElem(15, TypeElem.GLASS, "{'artglasID':283}"));

        } else if (project == 601005) {
            rootArea = new GsonRoot(1, LayoutArea.HORIZ, TypeElem.RECTANGL, 1800, 2020, 30, 30, 30);
            rootArea.propery(project.toString(), 12, "СИАЛ\\КП45\\Окна\\");
            rootArea.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootArea.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootArea.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootArea.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            
            GsonElem area2 = rootArea.addArea(new GsonElem(6, LayoutArea.VERT, TypeElem.AREA, 900));
            rootArea.addElem(new GsonElem(7, TypeElem.IMPOST));
            GsonElem area3 = rootArea.addArea(new GsonElem(8, LayoutArea.VERT, TypeElem.AREA, 900));
            
            GsonElem area4 = area3.addArea(new GsonElem(9, LayoutArea.VERT, TypeElem.AREA, 800));
            area3.addElem(new GsonElem(10, TypeElem.IMPOST));
            GsonElem area5 = area3.addArea(new GsonElem(11, LayoutArea.VERT, TypeElem.AREA, 1220));
            
            GsonElem area6 = area4.addArea(new GsonElem(12, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':2, 'sysfurnID':147}"));
            area2.addElem(new GsonElem(13, TypeElem.GLASS, "{'artglasID':286}"));
            area6.addElem(new GsonElem(14, TypeElem.GLASS, "{'artglasID':286}"));
            area5.addElem(new GsonElem(15, TypeElem.GLASS, "{'artglasID':286}"));
            
        } else {
            return null;
        }
        if (model == true) {
            rootArea.propery(project.toString(), -3, null);
        }

        return new GsonBuilder().create().toJson(rootArea);
    }
}
