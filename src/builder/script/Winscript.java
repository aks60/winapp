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
            rootGson = new GsonRoot(project, 8, "KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)",
                    LayoutArea.VERT, TypeElem.RECTANGL, 900, 1300, 1009, 10009, 1009);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':1634}"))
                    .addElem(new GsonElem(TypeElem.GLASS));

        } else if (project == 601002) {
            rootGson = new GsonRoot(project, 29, "Montblanc\\Nord\\1 ОКНА",
                    LayoutArea.HORIZ, TypeElem.RECTANGL, 1300, 1400, 1009, 10009, 1009);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addArea(new GsonElem(LayoutArea.HORIZ, TypeElem.AREA, 1300 / 2))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':3, 'sysfurnID':860}"))
                    .addElem(new GsonElem(TypeElem.GLASS));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));
            rootGson.addArea(new GsonElem(LayoutArea.HORIZ, TypeElem.AREA, 1300 / 2))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':860}"))
                    .addElem(new GsonElem(TypeElem.GLASS));

        } else if (project == 601003) {
            rootGson = new GsonRoot(project, 81, "Darrio\\DARRIO 200\\1 ОКНА",
                    LayoutArea.VERT, TypeElem.RECTANGL, 1440, 1700, 1009, 1009, 1009);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addArea(new GsonElem(LayoutArea.HORIZ, TypeElem.AREA, 400))
                    .addElem(new GsonElem(TypeElem.GLASS));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));
            GsonElem area = rootGson.addArea(new GsonElem(LayoutArea.HORIZ, TypeElem.AREA, 1300));
            area.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 720))
                    .addArea(new GsonElem(LayoutArea.HORIZ, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':389}"))
                    .addElem(new GsonElem(TypeElem.GLASS));
            area.addElem(new GsonElem(TypeElem.IMPOST));
            area.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 720))
                    .addArea(new GsonElem(LayoutArea.HORIZ, TypeElem.STVORKA, "{'typeOpen':3, 'sysfurnID':819}"))
                    .addElem(new GsonElem(TypeElem.GLASS));

        } else if (project == 601004) {
            rootGson = new GsonRoot(project, 8, "KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)",
                    LayoutArea.VERT, TypeElem.RECTANGL, 1440, 1700, 1009, 1009, 1009);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addArea(new GsonElem(LayoutArea.HORIZ, TypeElem.AREA, 400))
                    .addElem(new GsonElem(TypeElem.GLASS));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));
            GsonElem area = rootGson.addArea(new GsonElem(LayoutArea.HORIZ, TypeElem.AREA, 1300));
            area.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 720))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':1634}"))
                    .addElem(new GsonElem(TypeElem.GLASS));
            area.addElem(new GsonElem(TypeElem.IMPOST));
            area.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 720))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':1633}"))
                    .addElem(new GsonElem(TypeElem.GLASS));

        } else if (project == 601005) {
            rootGson = new GsonRoot(project, 8, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)",
                    LayoutArea.HORIZ, TypeElem.RECTANGL, 1600, 1700, 1009, 1009, 1009);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 800))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':1634}"))
                    .addElem(new GsonElem(TypeElem.GLASS));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));
            rootGson.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 800))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':1633}"))
                    .addElem(new GsonElem(TypeElem.GLASS));

        } else if (project == 601006) {
            rootGson = new GsonRoot(project, 110, "RAZIO\\RAZIO 58 N\\1 ОКНА",
                    LayoutArea.HORIZ, TypeElem.RECTANGL, 900, 1400, 1009, 1009, 1009);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':5746}")); //или 'R4x10x4x10x4'

        } else if (project == 601007) {
            rootGson = new GsonRoot(project, 87, "NOVOTEX\\Techno 58\\1 ОКНА",
                    LayoutArea.VERT, TypeElem.RECTANGL, 1100, 1400, 1009, 10018, 10018);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addArea(new GsonElem(LayoutArea.HORIZ, TypeElem.AREA, 300))
                    .addElem(new GsonElem(TypeElem.GLASS));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));
            GsonElem area = rootGson.addArea(new GsonElem(LayoutArea.HORIZ, TypeElem.AREA, 1100));
            area.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 550))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':1537}"))
                    .addElem(new GsonElem(TypeElem.GLASS));
            area.addElem(new GsonElem(TypeElem.IMPOST));
            area.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 550))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':1536}"))
                    .addElem(new GsonElem(TypeElem.GLASS));

        } else if (project == 601008) {
            rootGson = new GsonRoot(project, 99, "Rehau\\Blitz new\\1 ОКНА",
                    LayoutArea.HORIZ, TypeElem.RECTANGL, 1200, 1700, 1009, 28014, 21057);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addArea(new GsonElem(LayoutArea.HORIZ, TypeElem.AREA, 600))
                    .addElem(new GsonElem(TypeElem.GLASS));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));
            GsonElem area = rootGson.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 600));
            area.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 550))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':534}"))
                    .addElem(new GsonElem(TypeElem.GLASS));
            area.addElem(new GsonElem(TypeElem.IMPOST));
            area.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 1150))
                    .addElem(new GsonElem(TypeElem.GLASS));

        } else if (project == 601009) {
            rootGson = new GsonRoot(project, 54, "KBE\\KBE Эксперт\\1 ОКНА\\Открывание внутрь (ств. Z 77)",
                    LayoutArea.HORIZ, TypeElem.RECTANGL, 700, 1400, 1009, 1009, 1009);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':4663}")); //или '4x12x4x12x4'

        } else if (project == 601010) {
            rootGson = new GsonRoot(project, 54, "KBE\\KBE Эксперт\\1 ОКНА\\Открывание внутрь (ств. Z 77)",
                    LayoutArea.HORIZ, TypeElem.RECTANGL, 1300, 1400, 1009, 1009, 1009, "{'sysprofID':1120, 'ioknaParam':[-2147482183,-2147482226, -2147482193]}");
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 650))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'sysprofID':1121, 'typeOpen':1,'sysfurnID':2335, 'artiklHandl':2159}"))
                    .addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':4663}"));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));
            rootGson.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 650))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'sysprofID':1121, 'typeOpen':4,'sysfurnID':2916, 'artiklHandl':2159}"))
                    .addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':4663}"));

        } else if (project == 604004) {
            rootGson = new GsonRoot(project, 37, "Rehau\\Delight\\1 ОКНА",
                    LayoutArea.VERT, TypeElem.ARCH, 1300, 1050, 1700, 1009, 1009, 1009);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.ARCH));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));
            rootGson.addElem(new GsonElem(TypeElem.GLASS));
            GsonElem area = rootGson.addArea(new GsonElem(LayoutArea.HORIZ, TypeElem.AREA, 1050));
            area.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 650))
                    .addElem(new GsonElem(TypeElem.GLASS));
            area.addElem(new GsonElem(TypeElem.IMPOST));
            area.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 650))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':91}"))
                    .addElem(new GsonElem(TypeElem.GLASS));

        } else if (project == 604005) {
            rootGson = new GsonRoot(project, 135, "Wintech\\Termotech 742\\1 ОКНА",
                    LayoutArea.VERT, TypeElem.ARCH, 1300, 1200, 1500, 1009, 10009, 1009);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.ARCH));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));
            rootGson.addElem(new GsonElem(TypeElem.GLASS));
            GsonElem area = rootGson.addArea(new GsonElem(LayoutArea.HORIZ, TypeElem.AREA, 1200));
            area.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 650))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':2745}"))
                    .addElem(new GsonElem(TypeElem.GLASS));
            area.addElem(new GsonElem(TypeElem.IMPOST));
            area.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 650))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':2744}"))
                    .addElem(new GsonElem(TypeElem.GLASS));

        } else if (project == 604006) {
            rootGson = new GsonRoot(project, 135, "Wintech\\Termotech 742\\1 ОКНА",
                    LayoutArea.VERT, TypeElem.ARCH, 1100, 1220, 1600, 1009, 1009, 10012);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.ARCH));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));
            rootGson.addElem(new GsonElem(TypeElem.GLASS));
            GsonElem area = rootGson.addArea(new GsonElem(LayoutArea.HORIZ, TypeElem.AREA, 1220));
            area.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 550))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':2745}"))
                    .addElem(new GsonElem(TypeElem.GLASS));
            area.addElem(new GsonElem(TypeElem.IMPOST));
            area.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 550))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':2744}"))
                    .addElem(new GsonElem(TypeElem.GLASS));

        } else if (project == 604007) {
            rootGson = new GsonRoot(project, 99, "Rehau\\Blitz new\\1 ОКНА",
                    LayoutArea.VERT, TypeElem.ARCH, 1400, 1300, 1700, 1009, 1009, 10001);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.ARCH));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));
            rootGson.addElem(new GsonElem(TypeElem.GLASS));
            GsonElem area = (GsonElem) rootGson.addArea(new GsonElem(LayoutArea.HORIZ, TypeElem.AREA, 1300));
            area.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 700))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':535}"))
                    .addElem(new GsonElem(TypeElem.GLASS));
            area.addElem(new GsonElem(TypeElem.IMPOST));
            area.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 700))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':3, 'sysfurnID':534}"))
                    .addElem(new GsonElem(TypeElem.GLASS));

        } else if (project == 604008) {
            rootGson = new GsonRoot(project, 8, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)",
                    LayoutArea.VERT, TypeElem.ARCH, 1300, 1200, 1500, 1009, 10009, 1009);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.ARCH));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));
            rootGson.addElem(new GsonElem(TypeElem.GLASS));
            GsonElem area = (GsonElem) rootGson.addArea(new GsonElem(7, LayoutArea.HORIZ, TypeElem.AREA, 1200));
            area.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 650))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':3, 'sysfurnID':-1}"))
                    .addElem(new GsonElem(TypeElem.GLASS));
            area.addElem(new GsonElem(TypeElem.IMPOST));
            area.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 650))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':3, 'sysfurnID':-1}"))
                    .addElem(new GsonElem(TypeElem.GLASS));

        } else if (project == 604009) {
            rootGson = new GsonRoot(project, 8, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)",
                    LayoutArea.VERT, TypeElem.ARCH, 1300, 1200, 1500, 1009, 10009, 1009);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.ARCH));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));
            rootGson.addElem(new GsonElem(TypeElem.GLASS));
            rootGson.addArea(new GsonElem(LayoutArea.HORIZ, TypeElem.AREA, 1200))
                    .addElem(new GsonElem(TypeElem.GLASS));

        } else if (project == 604010) {
            rootGson = new GsonRoot(project, 29, "Montblanc\\Nord\\1 ОКНА",
                    LayoutArea.VERT, TypeElem.ARCH, 1300, 1400, 1700, 1009, 10009, 1009);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.ARCH));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));
            rootGson.addElem(new GsonElem(TypeElem.GLASS));
            GsonElem area = (GsonElem) rootGson.addArea(new GsonElem(LayoutArea.HORIZ, TypeElem.AREA, 1400));
            area.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 650))
                    .addElem(new GsonElem(TypeElem.GLASS));
            area.addElem(new GsonElem(TypeElem.IMPOST));
            GsonElem area2 = (GsonElem) area.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 650));
            area2.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 550))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':701}"))
                    .addElem(new GsonElem(TypeElem.GLASS));
            area2.addElem(new GsonElem(TypeElem.IMPOST));
            area2.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 850))
                    .addElem(new GsonElem(TypeElem.GLASS));

        } else if (project == 605001) {
            rootGson = new GsonRoot(project, 8, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)",
                    LayoutArea.VERT, TypeElem.TRAPEZE, 1300, 1200, 1500, 1009, 10009, 1009);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.ARCH));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));
            rootGson.addElem(new GsonElem(TypeElem.GLASS));
            rootGson.addArea(new GsonElem(LayoutArea.HORIZ, TypeElem.AREA, 1200))
                    .addElem(new GsonElem(TypeElem.GLASS));

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
            rootGson = new GsonRoot(project, 12, "СИАЛ\\КП45\\Окна",
                    LayoutArea.VERT, TypeElem.RECTANGL, 800, 1200, 25, 25, 25);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':285}"));

        } else if (project == 601002) {
            rootGson = new GsonRoot(project, 32, "СИАЛ\\КП40\\Окна",
                    LayoutArea.VERT, TypeElem.RECTANGL, 900, 600, 25, 25, 25);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':2}"))
                    .addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':285}"));

        } else if (project == 601003) {
            rootGson = new GsonRoot(project, 32, "СИАЛ\\КП40\\Окна",
                    LayoutArea.HORIZ, TypeElem.RECTANGL, 1200, 1600, 30, 30, 30);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addArea(new GsonElem(LayoutArea.HORIZ, TypeElem.AREA, 1200 / 2))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':3, 'sysfurnID':143}"))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':143}"));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST, "{'sysprofID':357}"));
            rootGson.addArea(new GsonElem(LayoutArea.HORIZ, TypeElem.AREA, 1200 / 2))
                    .addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':285}"))
                    .addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':285}"));

        } else if (project == 601004) {
            rootGson = new GsonRoot(project, 12, "СИАЛ\\КП45\\Окна\\",
                    LayoutArea.HORIZ, TypeElem.RECTANGL, 2010, 1600, 27, 27, 27);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addArea(new GsonElem(LayoutArea.HORIZ, TypeElem.AREA, 670))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':143}"))
                    .addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':283}"));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));
            rootGson.addArea(new GsonElem(LayoutArea.HORIZ, TypeElem.AREA, 670))
                    .addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':283}"));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));
            rootGson.addArea(new GsonElem(LayoutArea.HORIZ, TypeElem.AREA, 670))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':2, 'sysfurnID':143}"))
                    .addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':283}"));

        } else if (project == 601005) {
            rootGson = new GsonRoot(project, 12, "СИАЛ\\КП45\\Окна\\",
                    LayoutArea.HORIZ, TypeElem.RECTANGL, 1800, 2020, 30, 30, 30);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            GsonElem area2 = rootGson.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 900))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':147}"))
                    .addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':286}"));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));
            GsonElem area3 = rootGson.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 900))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 800))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':2, 'sysfurnID':147}"))
                    .addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':286}"));
            area3.addElem(new GsonElem(TypeElem.IMPOST));
            area3.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 1220))
                    .addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':286}"));

        } else if (project == 601006) {
            rootGson = new GsonRoot(project, 12, "СИАЛ\\КП45\\Окна\\",
                    LayoutArea.HORIZ, TypeElem.RECTANGL, 3800, 1800, 37, 37, 37);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));

            rootGson.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 633.33f))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':147}"))
                    .addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':286}"));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));

            rootGson.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 633.33f))
                    .addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':286}"));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));

            rootGson.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 633.33f))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':2, 'sysfurnID':147}"))
                    .addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':286}"));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));

            rootGson.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 633.33f))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':147}"))
                    .addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':286}"));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));

            rootGson.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 633.33f))
                    .addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':286}"));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));

            rootGson.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 633.33f))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':2, 'sysfurnID':147}"))
                    .addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':286}"));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));
        } else {
            return null;
        }
        if (model == true) {
            rootGson.propery(project.toString(), -3, null);
        }

        return new GsonBuilder().create().toJson(rootGson);
    }
}
