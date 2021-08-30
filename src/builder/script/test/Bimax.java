package builder.script.test;

import builder.script.GsonElem;
import builder.script.GsonRoot;
import static builder.script.Winscript.rootGson;
import com.google.gson.GsonBuilder;
import enums.Layout;
import enums.Type;

public final class Bimax {

    /**
     * PUNIC:427779 - KBE / KBE 58 / 3 НЕПРЯМОУГОЛЬНЫЕ ОКНА/ДВЕРИ / АРКИ
     * PUNIC:416791 - Rehau / Delight / 3 ТРАПЕЦИИ
     */
    public static String script(Integer prj, boolean model) {

        if (prj == 601001) {
            rootGson = new GsonRoot(prj, 1, 8, "KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)",
                    Layout.VERT, Type.RECTANGL, 900, 1300, 1009, 10009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT));
            rootGson.addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{'typeOpen':1, 'sysfurnID':1634}"))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 601002) {
            rootGson = new GsonRoot(prj, 1, 29, "Montblanc\\Nord\\1 ОКНА",
                    Layout.HORIZ, Type.RECTANGL, 1300, 1400, 1009, 10009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT));
            rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1300 / 2))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{'typeOpen':3, 'sysfurnID':860}"))
                    .addElem(new GsonElem(Type.GLASS));
            rootGson.addElem(new GsonElem(Type.IMPOST));
            rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1300 / 2))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{'typeOpen':4, 'sysfurnID':860}"))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 601003) {
            rootGson = new GsonRoot(prj, 1, 81, "Darrio\\DARRIO 200\\1 ОКНА",
                    Layout.VERT, Type.RECTANGL, 1440, 1700, 1009, 1009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT));
            rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 400))
                    .addElem(new GsonElem(Type.GLASS));
            rootGson.addElem(new GsonElem(Type.IMPOST));
            GsonElem area = rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1300));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 720))
                    .addArea(new GsonElem(Layout.HORIZ, Type.STVORKA, "{'typeOpen':1, 'sysfurnID':389}"))
                    .addElem(new GsonElem(Type.GLASS));
            area.addElem(new GsonElem(Type.IMPOST));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 720))
                    .addArea(new GsonElem(Layout.HORIZ, Type.STVORKA, "{'typeOpen':3, 'sysfurnID':819}"))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 601004) {
            rootGson = new GsonRoot(prj, 1, 8, "KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)",
                    Layout.VERT, Type.RECTANGL, 1440, 1700, 1009, 1009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT));
            rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 400))
                    .addElem(new GsonElem(Type.GLASS));
            rootGson.addElem(new GsonElem(Type.IMPOST));
            GsonElem area = rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1300));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 720))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{'typeOpen':1, 'sysfurnID':1634}"))
                    .addElem(new GsonElem(Type.GLASS));
            area.addElem(new GsonElem(Type.IMPOST));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 720))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{'typeOpen':4, 'sysfurnID':1633}"))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 601005) {
            rootGson = new GsonRoot(prj, 1, 8, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)",
                    Layout.HORIZ, Type.RECTANGL, 1600, 1700, 1009, 1009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT));
            rootGson.addArea(new GsonElem(Layout.VERT, Type.AREA, 800))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{'typeOpen':1, 'sysfurnID':1634}"))
                    .addElem(new GsonElem(Type.GLASS));
            rootGson.addElem(new GsonElem(Type.IMPOST));
            rootGson.addArea(new GsonElem(Layout.VERT, Type.AREA, 800))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{'typeOpen':4, 'sysfurnID':1633}"))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 601006) {
            rootGson = new GsonRoot(prj, 1, 110, "RAZIO\\RAZIO 58 N\\1 ОКНА",
                    Layout.HORIZ, Type.RECTANGL, 900, 1400, 1009, 1009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT));
            rootGson.addElem(new GsonElem(Type.GLASS, "{'artglasID':5746}")); //или 'R4x10x4x10x4'

        } else if (prj == 601007) {
            rootGson = new GsonRoot(prj, 1, 87, "NOVOTEX\\Techno 58\\1 ОКНА",
                    Layout.VERT, Type.RECTANGL, 1100, 1400, 1009, 10018, 10018);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT));
            rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 300))
                    .addElem(new GsonElem(Type.GLASS));
            rootGson.addElem(new GsonElem(Type.IMPOST));
            GsonElem area = rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1100));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 550))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{'typeOpen':1, 'sysfurnID':1537}"))
                    .addElem(new GsonElem(Type.GLASS));
            area.addElem(new GsonElem(Type.IMPOST));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 550))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{'typeOpen':4, 'sysfurnID':1536}"))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 601008) {
            rootGson = new GsonRoot(prj, 1, 99, "Rehau\\Blitz new\\1 ОКНА",
                    Layout.HORIZ, Type.RECTANGL, 1200, 1700, 1009, 28014, 21057);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT));
            rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 600))
                    .addElem(new GsonElem(Type.GLASS));
            rootGson.addElem(new GsonElem(Type.IMPOST));
            GsonElem area = rootGson.addArea(new GsonElem(Layout.VERT, Type.AREA, 600));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 550))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{'typeOpen':4, 'sysfurnID':534}"))
                    .addElem(new GsonElem(Type.GLASS));
            area.addElem(new GsonElem(Type.IMPOST));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 1150))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 601009) {
            rootGson = new GsonRoot(prj, 1, 54, "KBE\\KBE Эксперт\\1 ОКНА\\Открывание внутрь (ств. Z 77)",
                    Layout.HORIZ, Type.RECTANGL, 700, 1400, 1009, 1009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT));
            rootGson.addElem(new GsonElem(Type.GLASS, "{'artglasID':4663}")); //или '4x12x4x12x4'

        } else if (prj == 601010) {
            rootGson = new GsonRoot(prj, 1, 54, "KBE\\KBE Эксперт\\1 ОКНА\\Открывание внутрь (ств. Z 77)",
                    Layout.HORIZ, Type.RECTANGL, 1300, 1400, 1009, 1009, 1009, "{'sysprofID':1120}");
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT));
            rootGson.addArea(new GsonElem(Layout.VERT, Type.AREA, 650))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{'sysprofID':1121, 'typeOpen':1,'sysfurnID':2335}")) //, 'artiklHandl':2159}"))
                    .addElem(new GsonElem(Type.GLASS, "{'artglasID':4663}"));
            rootGson.addElem(new GsonElem(Type.IMPOST));
            rootGson.addArea(new GsonElem(Layout.VERT, Type.AREA, 650))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{'sysprofID':1121, 'typeOpen':4,'sysfurnID':2916}")) //, 'artiklHandl':2159}"))
                    .addElem(new GsonElem(Type.GLASS, "{'artglasID':4663}"));

        } else if (prj == 604004) {
            rootGson = new GsonRoot(prj, 1, 37, "Rehau\\Delight\\1 ОКНА",
                    Layout.VERT, Type.ARCH, 1300, 1050, 1700, 1009, 1009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT));
            rootGson.addElem(new GsonElem(Type.IMPOST));
            rootGson.addElem(new GsonElem(Type.GLASS));
            GsonElem area = rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1050));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 650))
                    .addElem(new GsonElem(Type.GLASS));
            area.addElem(new GsonElem(Type.IMPOST));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 650))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{'typeOpen':4, 'sysfurnID':91}"))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 700027) {  //punic = 427872 штульповое
            rootGson = new GsonRoot(prj, 1, 198, "Montblanc / Eco / 1 ОКНА (штульп)",
                    Layout.HORIZ, Type.RECTANGL, 1300, 1400, 1009, 1009, 1009, "{'ioknaParam':[-8252]}");
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT));
            rootGson.addArea(new GsonElem(Layout.VERT, Type.AREA, 450))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{'typeOpen':1, 'sysfurnID':2915}"))
                    .addElem(new GsonElem(Type.GLASS, "{'artglasID':3293}"));
            rootGson.addElem(new GsonElem(Type.SHTULP));
            rootGson.addArea(new GsonElem(Layout.VERT, Type.AREA, 850))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{'typeOpen':4, 'sysfurnID':2913}"))
                    .addElem(new GsonElem(Type.GLASS, "{'artglasID':4267}"));

        } else if (prj == 508634) {
            rootGson = new GsonRoot(prj, 1, 303, "Rehau\\Delight\\4 ОКНА(ФИГУРНАЯ СТВОРКА)",
                    Layout.HORIZ, Type.RECTANGL, 900, 1400, 1009, 1009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT));
            rootGson.addElem(new GsonElem(Type.GLASS));  
            
        } else if (prj == 508777) { //Стойка - ригель, надо тестировать
            rootGson = new GsonRoot(prj, 1, 303, "Rehau\\Delight\\4 ОКНА(ФИГУРНАЯ СТВОРКА)",
                    Layout.HORIZ, Type.RECTANGL, 900, 1400, 1009, 1009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT));
            rootGson.addElem(new GsonElem(Type.GLASS));  
                        
//        } else if (prj == 495647) { // PUNIC = 414087
//            //Арт.554008   Коробка 60мм  Белая, коричневая и карамель.
//            //Арт.554018  Створка Z57
//            //Арт.560607 615   Штапик 14.7
//            //Арт.865530 Уплотнитель притвора
//            rootGson = new GsonRoot(prj, 1, 303, "Rehau\\Delight\\3 ТРАПЕЦИИ",
//                    Layout.HORIZ, Type.RECTANGL, 900, 1400, 1009, 1009, 1009);
//            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT));
//            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT));
//            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP));
//            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT));
//            rootGson.addElem(new GsonElem(Type.GLASS));  
            
        } else if (prj == 604005) {
            rootGson = new GsonRoot(prj, 1, 135, "Wintech\\Termotech 742\\1 ОКНА",
                    Layout.VERT, Type.ARCH, 1300, 1200, 1500, 1009, 10009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT));
            rootGson.addElem(new GsonElem(Type.IMPOST));
            rootGson.addElem(new GsonElem(Type.GLASS));
            GsonElem area = rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1200));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 650))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{'typeOpen':1, 'sysfurnID':2745}"))
                    .addElem(new GsonElem(Type.GLASS));
            area.addElem(new GsonElem(Type.IMPOST));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 650))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{'typeOpen':4, 'sysfurnID':2744}"))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 604006) {
            rootGson = new GsonRoot(prj, 1, 135, "Wintech\\Termotech 742\\1 ОКНА",
                    Layout.VERT, Type.ARCH, 1100, 1220, 1600, 1009, 1009, 10012);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT));
            rootGson.addElem(new GsonElem(Type.IMPOST));
            rootGson.addElem(new GsonElem(Type.GLASS));
            GsonElem area = rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1220));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 550))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{'typeOpen':1, 'sysfurnID':2745}"))
                    .addElem(new GsonElem(Type.GLASS));
            area.addElem(new GsonElem(Type.IMPOST));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 550))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{'typeOpen':4, 'sysfurnID':2744}"))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 604007) {
            rootGson = new GsonRoot(prj, 1, 99, "Rehau\\Blitz new\\1 ОКНА",
                    Layout.VERT, Type.ARCH, 1400, 1300, 1700, 1009, 1009, 10001);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT));
            rootGson.addElem(new GsonElem(Type.IMPOST));
            rootGson.addElem(new GsonElem(Type.GLASS));
            GsonElem area = (GsonElem) rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1300));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 700))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{'typeOpen':1, 'sysfurnID':535}"))
                    .addElem(new GsonElem(Type.GLASS));
            area.addElem(new GsonElem(Type.IMPOST));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 700))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{'typeOpen':3, 'sysfurnID':534}"))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 604008) {
            rootGson = new GsonRoot(prj, 1, 8, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)",
                    Layout.VERT, Type.ARCH, 1300, 1200, 1500, 1009, 10009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT));
            rootGson.addElem(new GsonElem(Type.IMPOST));
            rootGson.addElem(new GsonElem(Type.GLASS));
            GsonElem area = (GsonElem) rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1200));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 650))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{'typeOpen':3, 'sysfurnID':-1}"))
                    .addElem(new GsonElem(Type.GLASS));
            area.addElem(new GsonElem(Type.IMPOST));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 650))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{'typeOpen':3, 'sysfurnID':-1}"))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 604009) {
            rootGson = new GsonRoot(prj, 1, 8, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)",
                    Layout.VERT, Type.ARCH, 1300, 1200, 1500, 1009, 10009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT));
            rootGson.addElem(new GsonElem(Type.IMPOST));
            rootGson.addElem(new GsonElem(Type.GLASS));
            rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1200))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 604010) {
            rootGson = new GsonRoot(prj, 1, 29, "Montblanc\\Nord\\1 ОКНА",
                    Layout.VERT, Type.ARCH, 1300, 1400, 1700, 1009, 10009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT));
            rootGson.addElem(new GsonElem(Type.IMPOST));
            rootGson.addElem(new GsonElem(Type.GLASS));
            GsonElem area = (GsonElem) rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1400));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 650))
                    .addElem(new GsonElem(Type.GLASS));
            area.addElem(new GsonElem(Type.IMPOST));
            GsonElem area2 = (GsonElem) area.addArea(new GsonElem(Layout.VERT, Type.AREA, 650));
            area2.addArea(new GsonElem(Layout.VERT, Type.AREA, 557))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{'typeOpen':4, 'sysfurnID':701}"))
                    .addElem(new GsonElem(Type.GLASS));
            area2.addElem(new GsonElem(Type.IMPOST));
            area2.addArea(new GsonElem(Layout.VERT, Type.AREA, 843))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 508983) { //PUNIC = 427779
            rootGson = new GsonRoot(prj, 1, 17, "KBE\\KBE 58\\3 НЕПРЯМОУГОЛЬНЫЕ ОКНА/ДВЕРИ\\АРКИ",
                    Layout.VERT, Type.ARCH, 1300, 1000, 1300, 1010, 10000, 10000);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT));
            rootGson.addElem(new GsonElem(Type.IMPOST));
            rootGson.addElem(new GsonElem(Type.GLASS));
            GsonElem area = rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1000));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 650))
                    .addElem(new GsonElem(Type.GLASS));
            area.addElem(new GsonElem(Type.IMPOST));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 650))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{'typeOpen':4, 'sysfurnID':316}"))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 605001) { //PUNIC = 427850  Трапеции
            rootGson = new GsonRoot(prj, 1, 8, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)",
                    Layout.VERT, Type.TRAPEZE, 1300, 1500, 1200, 1009, 10009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP));
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT));
            rootGson.addElem(new GsonElem(Type.GLASS));
//            rootGson.addElem(new GsonElem(Type.IMPOST));
//            rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1200))
//                    .addElem(new GsonElem(Type.GLASS));

        } else {
            return null;
        }
        if (model == true) {
            rootGson.propery(prj.toString(), -3, null);
        }
        return new GsonBuilder().create().toJson(rootGson);
    }
}
/*
Prj=601001 Ord=1
Prj=601001        PS=10894,13       SA=10893,66       dx=0,47     

Prj=601002 Ord=1
Prj=601002        PS=19009,51       SA=19006,40       dx=3,11     

Prj=601003 Ord=1
Prj=601003        PS=12100,05       SA=12311,85       dx=211,81   

Prj=601004 Ord=1
Prj=601004        PS=18145,02       SA=18155,85       dx=10,83    

Prj=601005 Ord=1
Prj=601005        PS=18888,66       SA=18893,19       dx=4,54     

Prj=601006 Ord=1
Prj=601006        PS=4750,56        SA=4794,70        dx=44,14    

Prj=601007 Ord=1
Prj=601007        PS=17261,23       SA=17259,94       dx=1,29     

Prj=601008 Ord=1
Prj=601008        PS=24201,85       SA=24203,10       dx=1,25     

Prj=601009 Ord=1
Prj=601009        PS=4787,42        SA=4786,49        dx=0,93     

Prj=601010 Ord=1
Prj=601010        PS=16905,07       SA=16609,98       dx=295,09   

Prj=700027 Ord=1
Prj=700027        PS=13296,18       SA=13314,79       dx=18,61    

Prj=604004 Ord=1
Prj=604004        PS=20410,69       SA=20201,16       dx=209,53   

Prj=604005 Ord=1
Prj=604005        PS=21819,14       SA=21821,70       dx=2,55     

Prj=604006 Ord=1
Prj=604006        PS=20081,09       SA=20082,36       dx=1,27     

Prj=604007 Ord=1
Prj=604007        PS=25234,63       SA=25232,09       dx=2,54     

Prj=604008 Ord=1
Prj=604008        PS=24105,55       SA=24108,99       dx=3,44     

Prj=604009 Ord=1
Prj=604009        PS=13256,22       SA=13257,24       dx=1,02     

Prj=604010 Ord=1
Prj=604010        PS=20823,77       SA=20825,69       dx=1,93 
 */
