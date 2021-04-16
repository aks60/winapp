package builder.script;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import domain.eSetting;
import enums.LayoutArea;
import enums.TypeElem;

public class Winscript {

    public static GsonRoot rootGson;

    public static String test(Integer project, boolean model) {
        if ("ps4".equals(eSetting.find(2).get(eSetting.val)) == true) {
            return builder.script.Winscript.testPs4(project, model);
        } else {
            return builder.script.Winscript.testPs3(project, model);
        }
    }

    public static String testPs4(Integer project, boolean model) {

        if (project == 601001) {
            rootGson = new GsonRoot(1, LayoutArea.VERT, TypeElem.RECTANGL, 900, 1300, 1009, 10009, 1009);
            rootGson.propery(project.toString(), 8, "KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)");
            rootGson.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            GsonElem area2 = (GsonElem) rootGson.addArea(new GsonElem(6, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':1634}"));
            area2.addElem(new GsonElem(7, TypeElem.GLASS));

        } else if (project == 601002) {
            rootGson = new GsonRoot(1, LayoutArea.HORIZ, TypeElem.RECTANGL, 1300, 1400, 1009, 10009, 1009);
            rootGson.propery(project.toString(), 29, "Montblanc\\Nord\\1 ОКНА");
            rootGson.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            GsonElem area2 = (GsonElem) rootGson.addArea(new GsonElem(6, LayoutArea.HORIZ, TypeElem.AREA, 1300 / 2));
            rootGson.addElem(new GsonElem(7, TypeElem.IMPOST));
            GsonElem area3 = (GsonElem) rootGson.addArea(new GsonElem(8, LayoutArea.HORIZ, TypeElem.AREA, 1300 / 2));
            GsonElem area4 = (GsonElem) area2.addArea(new GsonElem(9, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':3, 'sysfurnID':860}"));
            GsonElem area5 = (GsonElem) area3.addArea(new GsonElem(10, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':860}"));
            area4.addElem(new GsonElem(11, TypeElem.GLASS));
            area5.addElem(new GsonElem(12, TypeElem.GLASS));

        } else if (project == 601003) {
            rootGson = new GsonRoot(1, LayoutArea.VERT, TypeElem.RECTANGL, 1440, 1700, 1009, 1009, 1009);
            rootGson.propery(project.toString(), 81, "Darrio\\DARRIO 200\\1 ОКНА");
            rootGson.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            GsonElem area2 = (GsonElem) rootGson.addArea(new GsonElem(6, LayoutArea.HORIZ, TypeElem.AREA, 400));
            rootGson.addElem(new GsonElem(7, TypeElem.IMPOST));
            GsonElem area4 = (GsonElem) rootGson.addArea(new GsonElem(8, LayoutArea.HORIZ, TypeElem.AREA, 1300));
            GsonElem area5 = (GsonElem) area4.addArea(new GsonElem(9, LayoutArea.VERT, TypeElem.AREA, 720));
            area4.addElem(new GsonElem(10, TypeElem.IMPOST));
            GsonElem area6 = (GsonElem) area4.addArea(new GsonElem(11, LayoutArea.VERT, TypeElem.AREA, 720));
            GsonElem area8 = (GsonElem) area5.addArea(new GsonElem(12, LayoutArea.HORIZ, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':389}"));
            GsonElem area9 = (GsonElem) area6.addArea(new GsonElem(13, LayoutArea.HORIZ, TypeElem.STVORKA, "{'typeOpen':3, 'sysfurnID':819}"));
            area2.addElem(new GsonElem(14, TypeElem.GLASS));
            area8.addElem(new GsonElem(15, TypeElem.GLASS));
            area9.addElem(new GsonElem(16, TypeElem.GLASS));

        } else if (project == 601004) {
            rootGson = new GsonRoot(1, LayoutArea.VERT, TypeElem.RECTANGL, 1440, 1700, 1009, 1009, 1009);
            rootGson.propery(project.toString(), 8, "KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)");
            rootGson.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            GsonElem area2 = (GsonElem) rootGson.addArea(new GsonElem(6, LayoutArea.HORIZ, TypeElem.AREA, 400));
            rootGson.addElem(new GsonElem(7, TypeElem.IMPOST));
            GsonElem area4 = (GsonElem) rootGson.addArea(new GsonElem(8, LayoutArea.HORIZ, TypeElem.AREA, 1300));
            GsonElem area5 = (GsonElem) area4.addArea(new GsonElem(9, LayoutArea.VERT, TypeElem.AREA, 720));
            area4.addElem(new GsonElem(10, TypeElem.IMPOST));
            GsonElem area6 = (GsonElem) area4.addArea(new GsonElem(11, LayoutArea.VERT, TypeElem.AREA, 720));
            GsonElem area8 = (GsonElem) area5.addArea(new GsonElem(12, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':1634}"));
            GsonElem area9 = (GsonElem) area6.addArea(new GsonElem(13, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':1633}"));
            area2.addElem(new GsonElem(14, TypeElem.GLASS));
            area8.addElem(new GsonElem(15, TypeElem.GLASS));
            area9.addElem(new GsonElem(16, TypeElem.GLASS));

        } else if (project == 601005) {
            rootGson = new GsonRoot(1, LayoutArea.HORIZ, TypeElem.RECTANGL, 1600, 1700, 1009, 1009, 1009);
            rootGson.propery(project.toString(), 8, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)");
            rootGson.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            GsonElem area2 = (GsonElem) rootGson.addArea(new GsonElem(6, LayoutArea.VERT, TypeElem.AREA, 800));
            rootGson.addElem(new GsonElem(7, TypeElem.IMPOST));
            GsonElem area3 = (GsonElem) rootGson.addArea(new GsonElem(8, LayoutArea.VERT, TypeElem.AREA, 800));
            GsonElem area4 = (GsonElem) area2.addArea(new GsonElem(9, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':1634}"));
            GsonElem area5 = (GsonElem) area3.addArea(new GsonElem(10, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':1633}"));
            area4.addElem(new GsonElem(11, TypeElem.GLASS));
            area5.addElem(new GsonElem(12, TypeElem.GLASS));

        } else if (project == 601006) {
            rootGson = new GsonRoot(1, LayoutArea.HORIZ, TypeElem.RECTANGL, 900, 1400, 1009, 1009, 1009);
            rootGson.propery(project.toString(), 110, "RAZIO\\RAZIO 58 N\\1 ОКНА");
            rootGson.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addElem(new GsonElem(6, TypeElem.GLASS, "{'artglasID':5746}")); //или 'R4x10x4x10x4'

        } else if (project == 601007) {
            rootGson = new GsonRoot(1, LayoutArea.VERT, TypeElem.RECTANGL, 1100, 1400, 1009, 10018, 10018);
            rootGson.propery(project.toString(), 87, "NOVOTEX\\Techno 58\\1 ОКНА");
            rootGson.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            GsonElem area2 = (GsonElem) rootGson.addArea(new GsonElem(6, LayoutArea.HORIZ, TypeElem.AREA, 300));
            rootGson.addElem(new GsonElem(7, TypeElem.IMPOST));
            GsonElem area4 = (GsonElem) rootGson.addArea(new GsonElem(8, LayoutArea.HORIZ, TypeElem.AREA, 1100));
            GsonElem area5 = (GsonElem) area4.addArea(new GsonElem(9, LayoutArea.VERT, TypeElem.AREA, 550));
            area4.addElem(new GsonElem(10, TypeElem.IMPOST));
            GsonElem area6 = (GsonElem) area4.addArea(new GsonElem(11, LayoutArea.VERT, TypeElem.AREA, 550));
            GsonElem area8 = (GsonElem) area5.addArea(new GsonElem(12, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':1537}"));
            GsonElem area9 = (GsonElem) area6.addArea(new GsonElem(13, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':1536}"));
            area2.addElem(new GsonElem(14, TypeElem.GLASS));
            area8.addElem(new GsonElem(15, TypeElem.GLASS));
            area9.addElem(new GsonElem(16, TypeElem.GLASS));

        } else if (project == 601008) {
            rootGson = new GsonRoot(1, LayoutArea.HORIZ, TypeElem.RECTANGL, 1200, 1700, 1009, 28014, 21057);
            rootGson.propery(project.toString(), 99, "Rehau\\Blitz new\\1 ОКНА");
            rootGson.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            GsonElem area2 = (GsonElem) rootGson.addArea(new GsonElem(6, LayoutArea.HORIZ, TypeElem.AREA, 600));
            rootGson.addElem(new GsonElem(7, TypeElem.IMPOST));
            GsonElem area3 = (GsonElem) rootGson.addArea(new GsonElem(8, LayoutArea.VERT, TypeElem.AREA, 600));
            GsonElem area4 = (GsonElem) area3.addArea(new GsonElem(9, LayoutArea.VERT, TypeElem.AREA, 550));
            area3.addElem(new GsonElem(10, TypeElem.IMPOST));
            GsonElem area5 = (GsonElem) area3.addArea(new GsonElem(11, LayoutArea.VERT, TypeElem.AREA, 1150));
            GsonElem area6 = (GsonElem) area4.addArea(new GsonElem(12, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':534}"));
            area2.addElem(new GsonElem(13, TypeElem.GLASS));
            area6.addElem(new GsonElem(14, TypeElem.GLASS));
            area5.addElem(new GsonElem(15, TypeElem.GLASS));

        } else if (project == 601009) {
            rootGson = new GsonRoot(1, LayoutArea.HORIZ, TypeElem.RECTANGL, 700, 1400, 1009, 1009, 1009);
            rootGson.propery(project.toString(), 54, "KBE\\KBE Эксперт\\1 ОКНА\\Открывание внутрь (ств. Z 77)");
            rootGson.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addElem(new GsonElem(6, TypeElem.GLASS, "{'artglasID':4663}")); //или '4x12x4x12x4'

        } else if (project == 601010) {
            rootGson = new GsonRoot(1, LayoutArea.HORIZ, TypeElem.RECTANGL, 1300, 1400, 1009, 1009, 1009, "{'sysprofID':1120, 'ioknaParam':[-2147482183,-2147482226, -2147482193]}");
            rootGson.propery(project.toString(), 54, "KBE\\KBE Эксперт\\1 ОКНА\\Открывание внутрь (ств. Z 77)");
            rootGson.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            GsonElem area2 = (GsonElem) rootGson.addArea(new GsonElem(6, LayoutArea.VERT, TypeElem.AREA, 650));
            rootGson.addElem(new GsonElem(7, TypeElem.IMPOST));
            GsonElem area3 = (GsonElem) rootGson.addArea(new GsonElem(8, LayoutArea.VERT, TypeElem.AREA, 650));
            GsonElem area4 = (GsonElem) area2.addArea(new GsonElem(9, LayoutArea.VERT, TypeElem.STVORKA, "{'sysprofID':1121, 'typeOpen':1,'sysfurnID':2335}"));
            GsonElem area5 = (GsonElem) area3.addArea(new GsonElem(10, LayoutArea.VERT, TypeElem.STVORKA, "{'sysprofID':1121, 'typeOpen':4,'sysfurnID':2916}"));
            area4.addElem(new GsonElem(11, TypeElem.GLASS, "{'artglasID':4663}"));
            area5.addElem(new GsonElem(12, TypeElem.GLASS, "{'artglasID':4663}"));

        } else if (project == 604004) {
            rootGson = new GsonRoot(1, LayoutArea.VERT, TypeElem.ARCH, 1300, 1050, 1700, 1009, 1009, 1009);
            rootGson.propery(project.toString(), 37, "Rehau\\Delight\\1 ОКНА");
            rootGson.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.ARCH));
            rootGson.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addElem(new GsonElem(6, TypeElem.IMPOST));
            GsonElem area3 = (GsonElem) rootGson.addArea(new GsonElem(7, LayoutArea.HORIZ, TypeElem.AREA, 1050));
            GsonElem area4 = (GsonElem) area3.addArea(new GsonElem(8, LayoutArea.VERT, TypeElem.AREA, 650));
            area3.addElem(new GsonElem(9, TypeElem.IMPOST));
            GsonElem area5 = (GsonElem) area3.addArea(new GsonElem(10, LayoutArea.VERT, TypeElem.AREA, 650));
            GsonElem area6 = (GsonElem) area5.addArea(new GsonElem(11, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':91}"));
            rootGson.addElem(new GsonElem(12, TypeElem.GLASS));
            area4.addElem(new GsonElem(13, TypeElem.GLASS));
            area6.addElem(new GsonElem(14, TypeElem.GLASS));

        } else if (project == 604005) {
            rootGson = new GsonRoot(1, LayoutArea.VERT, TypeElem.ARCH, 1300, 1200, 1500, 1009, 10009, 1009);
            rootGson.propery(project.toString(), 135, "Wintech\\Termotech 742\\1 ОКНА");
            rootGson.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.ARCH));
            rootGson.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addElem(new GsonElem(6, TypeElem.IMPOST));
            GsonElem area3 = (GsonElem) rootGson.addArea(new GsonElem(7, LayoutArea.HORIZ, TypeElem.AREA, 1200));
            GsonElem area4 = (GsonElem) area3.addArea(new GsonElem(8, LayoutArea.VERT, TypeElem.AREA, 650));
            area3.addElem(new GsonElem(9, TypeElem.IMPOST));
            GsonElem area5 = (GsonElem) area3.addArea(new GsonElem(10, LayoutArea.VERT, TypeElem.AREA, 650));
            GsonElem area6 = (GsonElem) area4.addArea(new GsonElem(11, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':2745}"));
            GsonElem area7 = (GsonElem) area5.addArea(new GsonElem(12, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':2744}"));
            rootGson.addElem(new GsonElem(13, TypeElem.GLASS));
            area6.addElem(new GsonElem(14, TypeElem.GLASS));
            area7.addElem(new GsonElem(15, TypeElem.GLASS));

        } else if (project == 604006) {
            rootGson = new GsonRoot(1, LayoutArea.VERT, TypeElem.ARCH, 1100, 1220, 1600, 1009, 1009, 10012);
            rootGson.propery(project.toString(), 135, "Wintech\\Termotech 742\\1 ОКНА");
            rootGson.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.ARCH));
            rootGson.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addElem(new GsonElem(6, TypeElem.IMPOST));
            GsonElem area3 = (GsonElem) rootGson.addArea(new GsonElem(7, LayoutArea.HORIZ, TypeElem.AREA, 1220));
            GsonElem area4 = (GsonElem) area3.addArea(new GsonElem(8, LayoutArea.VERT, TypeElem.AREA, 550));
            area3.addElem(new GsonElem(9, TypeElem.IMPOST));
            GsonElem area5 = (GsonElem) area3.addArea(new GsonElem(10, LayoutArea.VERT, TypeElem.AREA, 550));
            GsonElem area6 = (GsonElem) area4.addArea(new GsonElem(11, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':2745}"));
            GsonElem area7 = (GsonElem) area5.addArea(new GsonElem(12, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':2744}"));
            rootGson.addElem(new GsonElem(13, TypeElem.GLASS));
            area6.addElem(new GsonElem(14, TypeElem.GLASS));
            area7.addElem(new GsonElem(15, TypeElem.GLASS));

        } else if (project == 604007) {
            rootGson = new GsonRoot(1, LayoutArea.VERT, TypeElem.ARCH, 1400, 1300, 1700, 1009, 1009, 10001);
            rootGson.propery(project.toString(), 99, "Rehau\\Blitz new\\1 ОКНА");
            rootGson.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.ARCH));
            rootGson.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addElem(new GsonElem(6, TypeElem.IMPOST));
            GsonElem area3 = (GsonElem) rootGson.addArea(new GsonElem(7, LayoutArea.HORIZ, TypeElem.AREA, 1300));
            GsonElem area4 = (GsonElem) area3.addArea(new GsonElem(8, LayoutArea.VERT, TypeElem.AREA, 700));
            area3.addElem(new GsonElem(9, TypeElem.IMPOST));
            GsonElem area5 = (GsonElem) area3.addArea(new GsonElem(10, LayoutArea.VERT, TypeElem.AREA, 700));
            GsonElem area6 = (GsonElem) area4.addArea(new GsonElem(11, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':535}"));
            GsonElem area7 = (GsonElem) area5.addArea(new GsonElem(12, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':3, 'sysfurnID':534}"));
            rootGson.addElem(new GsonElem(13, TypeElem.GLASS));
            area6.addElem(new GsonElem(14, TypeElem.GLASS));
            area7.addElem(new GsonElem(15, TypeElem.GLASS));

        } else if (project == 604008) {
            rootGson = new GsonRoot(1, LayoutArea.VERT, TypeElem.ARCH, 1300, 1200, 1500, 1009, 10009, 1009);
            rootGson.propery(project.toString(), 8, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)");
            rootGson.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.ARCH));
            rootGson.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addElem(new GsonElem(6, TypeElem.IMPOST));
            GsonElem area3 = (GsonElem) rootGson.addArea(new GsonElem(7, LayoutArea.HORIZ, TypeElem.AREA, 1200));
            GsonElem area4 = (GsonElem) area3.addArea(new GsonElem(8, LayoutArea.VERT, TypeElem.AREA, 650));
            area3.addElem(new GsonElem(9, TypeElem.IMPOST));
            GsonElem area5 = (GsonElem) area3.addArea(new GsonElem(10, LayoutArea.VERT, TypeElem.AREA, 650));
            GsonElem area6 = (GsonElem) area4.addArea(new GsonElem(11, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':3, 'sysfurnID':-1}"));
            GsonElem area7 = (GsonElem) area5.addArea(new GsonElem(12, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':3, 'sysfurnID':-1}"));
            rootGson.addElem(new GsonElem(13, TypeElem.GLASS));
            area6.addElem(new GsonElem(14, TypeElem.GLASS));
            area7.addElem(new GsonElem(15, TypeElem.GLASS));

        } else if (project == 604009) {
            rootGson = new GsonRoot(1, LayoutArea.VERT, TypeElem.ARCH, 1300, 1200, 1500, 1009, 10009, 1009);
            rootGson.propery(project.toString(), 8, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)");
            rootGson.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.ARCH));
            rootGson.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addElem(new GsonElem(6, TypeElem.IMPOST));
            GsonElem area3 = (GsonElem) rootGson.addArea(new GsonElem(7, LayoutArea.HORIZ, TypeElem.AREA, 1200));
            rootGson.addElem(new GsonElem(8, TypeElem.GLASS));
            area3.addElem(new GsonElem(9, TypeElem.GLASS));

        } else if (project == 604010) {
            rootGson = new GsonRoot(1, LayoutArea.VERT, TypeElem.ARCH, 1300, 1400, 1700, 1009, 10009, 1009);
            rootGson.propery(project.toString(), 29, "Montblanc\\Nord\\1 ОКНА");
            rootGson.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.ARCH));
            rootGson.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addElem(new GsonElem(6, TypeElem.IMPOST));
            GsonElem area7 = (GsonElem) rootGson.addArea(new GsonElem(7, LayoutArea.HORIZ, TypeElem.AREA, 1400));
            GsonElem area8 = (GsonElem) area7.addArea(new GsonElem(8, LayoutArea.VERT, TypeElem.AREA, 650));
            area7.addElem(new GsonElem(9, TypeElem.IMPOST));
            GsonElem area10 = (GsonElem) area7.addArea(new GsonElem(10, LayoutArea.VERT, TypeElem.AREA, 650));
            GsonElem area11 = (GsonElem) area10.addArea(new GsonElem(11, LayoutArea.VERT, TypeElem.AREA, 550));
            area10.addElem(new GsonElem(12, TypeElem.IMPOST));
            GsonElem area13 = (GsonElem) area10.addArea(new GsonElem(13, LayoutArea.VERT, TypeElem.AREA, 850));
            GsonElem area14 = (GsonElem) area11.addArea(new GsonElem(14, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':701}"));
            rootGson.addElem(new GsonElem(15, TypeElem.GLASS));
            area8.addElem(new GsonElem(16, TypeElem.GLASS));
            area13.addElem(new GsonElem(17, TypeElem.GLASS));
            area14.addElem(new GsonElem(18, TypeElem.GLASS));

        } else if (project == 605001) {
            rootGson = new GsonRoot(1, LayoutArea.VERT, TypeElem.TRAPEZE, 1300, 1200, 1500, 1009, 10009, 1009);
            rootGson.propery(project.toString(), 8, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)");
            rootGson.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.ARCH));
            rootGson.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addElem(new GsonElem(6, TypeElem.IMPOST));
            GsonElem area3 = (GsonElem) rootGson.addArea(new GsonElem(7, LayoutArea.HORIZ, TypeElem.AREA, 1200));
            rootGson.addElem(new GsonElem(8, TypeElem.GLASS));
            area3.addElem(new GsonElem(9, TypeElem.GLASS));

        } else {
            return null;
        }
        if (model == true) {
            rootGson.propery(project.toString(), -3, null);
        }
        return new GsonBuilder().create().toJson(rootGson);
    }

    public static String testPs3(Integer project, boolean model) {

        if (project == 601001) {
            rootGson = new GsonRoot(1, LayoutArea.VERT, TypeElem.RECTANGL, 800, 1200, 25, 25, 25);
            rootGson.propery(project.toString(), 12, "СИАЛ\\КП45\\Окна");
            rootGson.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addElem(new GsonElem(6, TypeElem.GLASS, "{'artglasID':285}"));

        } else if (project == 601002) {
            rootGson = new GsonRoot(1, LayoutArea.VERT, TypeElem.RECTANGL, 900, 600, 25, 25, 25);
            rootGson.propery(project.toString(), 32, "СИАЛ\\КП40\\Окна");
            rootGson.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            GsonElem area2 = (GsonElem) rootGson.addArea(new GsonElem(6, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':2}"));
            area2.addElem(new GsonElem(7, TypeElem.GLASS, "{'artglasID':285}"));

        } else if (project == 601003) {
            rootGson = new GsonRoot(1, LayoutArea.HORIZ, TypeElem.RECTANGL, 1200, 1600, 30, 30, 30);
            rootGson.propery(project.toString(), 32, "СИАЛ\\КП40\\Окна");
            rootGson.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            GsonElem area2 = (GsonElem) rootGson.addArea(new GsonElem(6, LayoutArea.HORIZ, TypeElem.AREA, 1200 / 2));
            rootGson.addElem(new GsonElem(7, TypeElem.IMPOST, "{'sysprofID':357}"));
            GsonElem area3 = (GsonElem) rootGson.addArea(new GsonElem(8, LayoutArea.HORIZ, TypeElem.AREA, 1200 / 2));
            GsonElem area4 = (GsonElem) area2.addArea(new GsonElem(9, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':3, 'sysfurnID':143}"));
            GsonElem area5 = (GsonElem) area3.addArea(new GsonElem(10, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':143}"));
            area4.addElem(new GsonElem(11, TypeElem.GLASS, "{'artglasID':285}"));
            area5.addElem(new GsonElem(12, TypeElem.GLASS, "{'artglasID':285}"));

        } else if (project == 601004) {
            rootGson = new GsonRoot(1, LayoutArea.HORIZ, TypeElem.RECTANGL, 2010, 1600, 27, 27, 27);
            rootGson.propery(project.toString(), 12, "СИАЛ\\КП45\\Окна\\");
            rootGson.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            GsonElem area2 = (GsonElem) rootGson.addArea(new GsonElem(6, LayoutArea.HORIZ, TypeElem.AREA, 670));
            rootGson.addElem(new GsonElem(7, TypeElem.IMPOST));
            GsonElem area3 = (GsonElem) rootGson.addArea(new GsonElem(8, LayoutArea.HORIZ, TypeElem.AREA, 670));
            rootGson.addElem(new GsonElem(9, TypeElem.IMPOST));
            GsonElem area4 = rootGson.addArea(new GsonElem(10, LayoutArea.HORIZ, TypeElem.AREA, 670));
            GsonElem area5 = area2.addArea(new GsonElem(11, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':143}"));
            GsonElem area6 = area4.addArea(new GsonElem(12, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':2, 'sysfurnID':143}"));
            area5.addElem(new GsonElem(13, TypeElem.GLASS, "{'artglasID':283}"));
            area6.addElem(new GsonElem(14, TypeElem.GLASS, "{'artglasID':283}"));
            area3.addElem(new GsonElem(15, TypeElem.GLASS, "{'artglasID':283}"));

        } else if (project == 601005) {
            rootGson = new GsonRoot(1, LayoutArea.HORIZ, TypeElem.RECTANGL, 1800, 2020, 30, 30, 30);
            rootGson.propery(project.toString(), 12, "СИАЛ\\КП45\\Окна\\");
            rootGson.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));

            GsonElem area2 = rootGson.addArea(new GsonElem(6, LayoutArea.VERT, TypeElem.AREA, 900));
            rootGson.addElem(new GsonElem(7, TypeElem.IMPOST));
            GsonElem area3 = rootGson.addArea(new GsonElem(8, LayoutArea.VERT, TypeElem.AREA, 900));

            GsonElem area4 = area3.addArea(new GsonElem(9, LayoutArea.VERT, TypeElem.AREA, 800));
            area3.addElem(new GsonElem(10, TypeElem.IMPOST));
            GsonElem area5 = area3.addArea(new GsonElem(11, LayoutArea.VERT, TypeElem.AREA, 1220));

            GsonElem area6 = area2.addArea(new GsonElem(12, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':147}"));
            GsonElem area7 = area4.addArea(new GsonElem(13, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':2, 'sysfurnID':147}"));
            area6.addElem(new GsonElem(14, TypeElem.GLASS, "{'artglasID':286}"));
            area7.addElem(new GsonElem(15, TypeElem.GLASS, "{'artglasID':286}"));
            area5.addElem(new GsonElem(16, TypeElem.GLASS, "{'artglasID':286}"));

        } else if (project == 601006) {
            rootGson = new GsonRoot(1, LayoutArea.HORIZ, TypeElem.RECTANGL, 3800, 1800, 37, 37, 37);
            rootGson.propery(project.toString(), 12, "СИАЛ\\КП45\\Окна\\");
            rootGson.addElem(new GsonElem(2, TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(3, TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(4, TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(5, TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));

            rootGson.addArea(new GsonElem(6, LayoutArea.VERT, TypeElem.AREA, 900))
                    .addArea(new GsonElem(12, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':147}"))
                    .addElem(new GsonElem(14, TypeElem.GLASS, "{'artglasID':286}"));
            rootGson.addElem(new GsonElem(7, TypeElem.IMPOST));

            rootGson.addArea(new GsonElem(8, LayoutArea.VERT, TypeElem.AREA, 900))
                    .addElem(new GsonElem(14, TypeElem.GLASS, "{'artglasID':286}"));
            rootGson.addElem(new GsonElem(7, TypeElem.IMPOST));

            rootGson.addArea(new GsonElem(6, LayoutArea.VERT, TypeElem.AREA, 900))
                    .addArea(new GsonElem(12, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':147}"))
                    .addElem(new GsonElem(14, TypeElem.GLASS, "{'artglasID':286}"));
            rootGson.addElem(new GsonElem(7, TypeElem.IMPOST));

            rootGson.addArea(new GsonElem(6, LayoutArea.VERT, TypeElem.AREA, 900))
                    .addArea(new GsonElem(12, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':147}"))
                    .addElem(new GsonElem(14, TypeElem.GLASS, "{'artglasID':286}"));
            rootGson.addElem(new GsonElem(7, TypeElem.IMPOST));

            rootGson.addArea(new GsonElem(8, LayoutArea.VERT, TypeElem.AREA, 900))
                    .addElem(new GsonElem(14, TypeElem.GLASS, "{'artglasID':286}"));
            rootGson.addElem(new GsonElem(7, TypeElem.IMPOST));

            rootGson.addArea(new GsonElem(6, LayoutArea.VERT, TypeElem.AREA, 900))
                    .addArea(new GsonElem(12, LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':147}"))
                    .addElem(new GsonElem(14, TypeElem.GLASS, "{'artglasID':286}"));
            rootGson.addElem(new GsonElem(7, TypeElem.IMPOST));
        } else {
            return null;
        }
        if (model == true) {
            rootGson.propery(project.toString(), -3, null);
        }

        return new GsonBuilder().create().toJson(rootGson);
    }
}
