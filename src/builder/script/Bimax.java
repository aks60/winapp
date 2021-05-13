package builder.script;

import static builder.script.Winscript.rootGson;
import com.google.gson.GsonBuilder;
import enums.LayoutArea;
import enums.TypeElem;

public final class Bimax {

    public static String script(Integer prj, boolean model) {

        if (prj == 601001) {
            rootGson = new GsonRoot(prj, 1, 8, "KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)",
                    LayoutArea.VERT, TypeElem.RECTANGL, 900, 1300, 1009, 10009, 1009);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1, 'sysfurnID':1634}"))
                    .addElem(new GsonElem(TypeElem.GLASS));

        } else if (prj == 601002) {
            rootGson = new GsonRoot(prj, 1, 29, "Montblanc\\Nord\\1 ОКНА",
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

        } else if (prj == 601003) {
            rootGson = new GsonRoot(prj, 1, 81, "Darrio\\DARRIO 200\\1 ОКНА",
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

        } else if (prj == 601004) {
            rootGson = new GsonRoot(prj, 1, 8, "KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)",
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

        } else if (prj == 601005) {
            rootGson = new GsonRoot(prj, 1, 8, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)",
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

        } else if (prj == 601006) {
            rootGson = new GsonRoot(prj, 1, 110, "RAZIO\\RAZIO 58 N\\1 ОКНА",
                    LayoutArea.HORIZ, TypeElem.RECTANGL, 900, 1400, 1009, 1009, 1009);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':5746}")); //или 'R4x10x4x10x4'

        } else if (prj == 601007) {
            rootGson = new GsonRoot(prj, 1, 87, "NOVOTEX\\Techno 58\\1 ОКНА",
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

        } else if (prj == 601008) {
            rootGson = new GsonRoot(prj, 1, 99, "Rehau\\Blitz new\\1 ОКНА",
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

        } else if (prj == 601009) {
            rootGson = new GsonRoot(prj, 1, 54, "KBE\\KBE Эксперт\\1 ОКНА\\Открывание внутрь (ств. Z 77)",
                    LayoutArea.HORIZ, TypeElem.RECTANGL, 700, 1400, 1009, 1009, 1009);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':4663}")); //или '4x12x4x12x4'

        } else if (prj == 601010) {
            rootGson = new GsonRoot(prj, 1, 54, "KBE\\KBE Эксперт\\1 ОКНА\\Открывание внутрь (ств. Z 77)",
                    LayoutArea.HORIZ, TypeElem.RECTANGL, 1300, 1400, 1009, 1009, 1009, "{'sysprofID':1120, 'ioknaParam':[-2147482183,-2147482226, -2147482193]}");
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 650))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'sysprofID':1121, 'typeOpen':1,'sysfurnID':2335}")) //, 'artiklHandl':2159}"))
                    .addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':4663}"));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));
            rootGson.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 650))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'sysprofID':1121, 'typeOpen':4,'sysfurnID':2916}")) //, 'artiklHandl':2159}"))
                    .addElem(new GsonElem(TypeElem.GLASS, "{'artglasID':4663}"));
        
        } else if (prj == 700027) {  //punic = 427872
            rootGson = new GsonRoot(prj, 1, 198, "Montblanc / Eco / 1 ОКНА",
                    LayoutArea.HORIZ, TypeElem.RECTANGL, 1300, 1400, 1009, 1009, 1009);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.TOP));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 450))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':1}"))
                    .addElem(new GsonElem(TypeElem.GLASS));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));
            rootGson.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 850))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':2}"))
                    .addElem(new GsonElem(TypeElem.GLASS));
            

        } else if (prj == 604004) {
            rootGson = new GsonRoot(prj, 1, 37, "Rehau\\Delight\\1 ОКНА",
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

        } else if (prj == 604005) {
            rootGson = new GsonRoot(prj, 1, 135, "Wintech\\Termotech 742\\1 ОКНА",
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

        } else if (prj == 604006) {
            rootGson = new GsonRoot(prj, 1, 135, "Wintech\\Termotech 742\\1 ОКНА",
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

        } else if (prj == 604007) {
            rootGson = new GsonRoot(prj, 1, 99, "Rehau\\Blitz new\\1 ОКНА",
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

        } else if (prj == 604008) {
            rootGson = new GsonRoot(prj, 1, 8, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)",
                    LayoutArea.VERT, TypeElem.ARCH, 1300, 1200, 1500, 1009, 10009, 1009);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.ARCH));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));
            rootGson.addElem(new GsonElem(TypeElem.GLASS));
            GsonElem area = (GsonElem) rootGson.addArea(new GsonElem(LayoutArea.HORIZ, TypeElem.AREA, 1200));
            area.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 650))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':3, 'sysfurnID':-1}"))
                    .addElem(new GsonElem(TypeElem.GLASS));
            area.addElem(new GsonElem(TypeElem.IMPOST));
            area.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 650))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':3, 'sysfurnID':-1}"))
                    .addElem(new GsonElem(TypeElem.GLASS));

        } else if (prj == 604009) {
            rootGson = new GsonRoot(prj, 1, 8, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)",
                    LayoutArea.VERT, TypeElem.ARCH, 1300, 1200, 1500, 1009, 10009, 1009);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.ARCH));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));
            rootGson.addElem(new GsonElem(TypeElem.GLASS));
            rootGson.addArea(new GsonElem(LayoutArea.HORIZ, TypeElem.AREA, 1200))
                    .addElem(new GsonElem(TypeElem.GLASS));

        } else if (prj == 604010) {
            rootGson = new GsonRoot(prj, 1, 29, "Montblanc\\Nord\\1 ОКНА",
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
            area2.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 557))
                    .addArea(new GsonElem(LayoutArea.VERT, TypeElem.STVORKA, "{'typeOpen':4, 'sysfurnID':701}"))
                    .addElem(new GsonElem(TypeElem.GLASS));
            area2.addElem(new GsonElem(TypeElem.IMPOST));
            area2.addArea(new GsonElem(LayoutArea.VERT, TypeElem.AREA, 843))
                    .addElem(new GsonElem(TypeElem.GLASS));

        } else if (prj == 605001) {
            rootGson = new GsonRoot(prj, 1, 8, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)",
                    LayoutArea.VERT, TypeElem.TRAPEZE, 1300, 1200, 1500, 1009, 10009, 1009);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.ARCH));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
            rootGson.addElem(new GsonElem(TypeElem.IMPOST));
            rootGson.addElem(new GsonElem(TypeElem.GLASS));
            rootGson.addArea(new GsonElem(LayoutArea.HORIZ, TypeElem.AREA, 1200))
                    .addElem(new GsonElem(TypeElem.GLASS));

        } else if (prj == 416791) {
            rootGson = new GsonRoot(prj, 1, 8, "Rehau\\Delight\\3 ТРАПЕЦИИ",
                    LayoutArea.VERT, TypeElem.TRAPEZE, 1300, 1200, 1500, 1009, 10009, 1009);
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.LEFT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.RIGHT));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.ARCH));
            rootGson.addElem(new GsonElem(TypeElem.FRAME_SIDE, LayoutArea.BOTTOM));
                
        } else {
            return null;
        }
        if (model == true) {
            rootGson.propery(prj.toString(), -3, null);
        }
        return new GsonBuilder().create().toJson(rootGson);
    }
}
