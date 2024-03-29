package builder.script.test;

import builder.script.GsonElem;
import builder.script.GsonRoot;
import static builder.script.GsonScript.rootGson;
import enums.Form;
import enums.Layout;
import enums.Type;

public final class Bimax {

    public static String script(Integer prj) {
// <editor-fold defaultstate="collapsed" desc="RECTANGL">
        if (prj == 508807) { //PUNIC = 427595
            rootGson = new GsonRoot("1.0", prj, 2, 8, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)",
                    Layout.VERT, Type.RECTANGL, 900, 1400, 1009, 1009, 1009, "{ioknaParam: [-9504]}");  //маскитка Р400
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen: 4}"))
                    .addElem(new GsonElem(Type.MOSKITKA, "{artiklID: 2700, elementID: 84}"))
                    .addElem(new GsonElem(Type.GLASS, "{artglasID: 4267}"));

        } else if (prj == 508809) { //PUNIC = 427597
            rootGson = new GsonRoot("1.0", prj, 1, 291, "Teplowin 100\\Classic\\1 ОКНА",
                    Layout.VERT, Type.RECTANGL, 1000, 1520, 1009, 1009, 1009, "{ioknaParam: [-9504]}");  //маскитка Р400
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen: 4}"))
                    .addElem(new GsonElem(Type.MOSKITKA, "{artiklID: 2700, elementID: 84}"))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 508966) { //PUNIC = 427761
            rootGson = new GsonRoot("1.0", prj, 1, 291, "DarrioColor\\Односторонняя\\Isotech 530\\1 ОКНА",
                    Layout.VERT, Type.RECTANGL, 900, 1400, 1009, 1009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen: 4}"))
                    .addElem(new GsonElem(Type.MOSKITKA, "{artiklID: 2700, elementID: 1123}"))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 601001) { //PUNIC = 427817
            rootGson = new GsonRoot("1.0", prj, 1, 8, "KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)",
                    Layout.VERT, Type.RECTANGL, 900, 1300, 1009, 10009, 1009, "{ioknaParam: [-9504]}");  //маскитка Р400
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen:1, sysfurnID:1634}"))
                    //.addElem(new GsonElem(Type.MOSKITKA, "{artiklID: 2700, elementID: 1287}"))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 601002) { //PUNIC = 427818
            rootGson = new GsonRoot("1.0", prj, 1, 29, "Montblanc\\Nord\\1 ОКНА",
                    Layout.HORIZ, Type.RECTANGL, 1300, 1400, 1009, 10009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1300 / 2))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen:3, sysfurnID:860}"))
                    .addElem(new GsonElem(Type.GLASS));
            rootGson.addElem(new GsonElem(Type.IMPOST))
                    .addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1300 / 2))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen:4, sysfurnID:860}"))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 601003) { //PUNIC = 427819
            rootGson = new GsonRoot("1.0", prj, 1, 81, "Darrio\\DARRIO 200\\1 ОКНА",
                    Layout.VERT, Type.RECTANGL, 1440, 1700, 1009, 1009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addArea(new GsonElem(Layout.HORIZ, Type.AREA, 400))
                    .addElem(new GsonElem(Type.GLASS));
            rootGson.addElem(new GsonElem(Type.IMPOST));
            GsonElem area = rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1300));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 720))
                    .addArea(new GsonElem(Layout.HORIZ, Type.STVORKA, "{typeOpen:1, sysfurnID:389}"))
                    .addElem(new GsonElem(Type.GLASS));
            area.addElem(new GsonElem(Type.IMPOST))
                    .addArea(new GsonElem(Layout.VERT, Type.AREA, 720))
                    .addArea(new GsonElem(Layout.HORIZ, Type.STVORKA, "{typeOpen:3, sysfurnID:819}"))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 601004) { //PUNIC = 427820
            rootGson = new GsonRoot("1.0", prj, 1, 8, "KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)",
                    Layout.VERT, Type.RECTANGL, 1440, 1700, 1009, 1009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT));
            rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 400))
                    .addElem(new GsonElem(Type.GLASS));
            rootGson.addElem(new GsonElem(Type.IMPOST));
            GsonElem area = rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1300));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 720))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen:1, sysfurnID:1634}"))
                    .addElem(new GsonElem(Type.GLASS));
            area.addElem(new GsonElem(Type.IMPOST))
                    .addArea(new GsonElem(Layout.VERT, Type.AREA, 720))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen:4, sysfurnID:1633}"))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 601005) { //PUNIC = 427840
            rootGson = new GsonRoot("1.0", prj, 1, 8, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)",
                    Layout.HORIZ, Type.RECTANGL, 1600, 1700, 1009, 1009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT));
            rootGson.addArea(new GsonElem(Layout.VERT, Type.AREA, 800))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen:1, sysfurnID:1634}"))
                    .addElem(new GsonElem(Type.GLASS));
            rootGson.addElem(new GsonElem(Type.IMPOST))
                    .addArea(new GsonElem(Layout.VERT, Type.AREA, 800))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen:4, sysfurnID:1633}"))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 601006) { //PUNIC = 427838
            rootGson = new GsonRoot("1.0", prj, 1, 110, "RAZIO\\RAZIO 58 N\\1 ОКНА",
                    Layout.HORIZ, Type.RECTANGL, 900, 1400, 1009, 1009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addElem(new GsonElem(Type.GLASS, "{artglasID:5746}")); //или R4x10x4x10x4

        } else if (prj == 601007) { //PUNIC = 427842
            rootGson = new GsonRoot("1.0", prj, 1, 87, "NOVOTEX\\Techno 58\\1 ОКНА",
                    Layout.VERT, Type.RECTANGL, 1100, 1400, 1009, 10018, 10018);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addArea(new GsonElem(Layout.HORIZ, Type.AREA, 300))
                    .addElem(new GsonElem(Type.GLASS));
            rootGson.addElem(new GsonElem(Type.IMPOST));
            GsonElem area = rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1100));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 550))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen:1, sysfurnID:1537}"))
                    .addElem(new GsonElem(Type.GLASS));
            area.addElem(new GsonElem(Type.IMPOST));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 550))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen:4, sysfurnID:1536}"))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 601008) { //PUNIC = 427848
            rootGson = new GsonRoot("1.0", prj, 1, 99, "Rehau\\Blitz new\\1 ОКНА",
                    Layout.HORIZ, Type.RECTANGL, 1200, 1700, 1009, 28014, 21057);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addArea(new GsonElem(Layout.HORIZ, Type.AREA, 600))
                    .addElem(new GsonElem(Type.GLASS));
            rootGson.addElem(new GsonElem(Type.IMPOST));
            GsonElem area = rootGson.addArea(new GsonElem(Layout.VERT, Type.AREA, 600));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 550))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen:4, sysfurnID:534}"))
                    .addElem(new GsonElem(Type.GLASS));
            area.addElem(new GsonElem(Type.IMPOST));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 1150))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 601009) { //PUNIC = 427851
            rootGson = new GsonRoot("1.0", prj, 1, 54, "KBE\\KBE Эксперт\\1 ОКНА\\Открывание внутрь (ств. Z77)",
                    Layout.HORIZ, Type.RECTANGL, 700, 1400, 1009, 1009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addElem(new GsonElem(Type.GLASS, "{artglasID:4663}")); //или 4x12x4x12x4
        } else if (prj == 999) { //PUNIC =
            rootGson = new GsonRoot("1.0", prj, 1, 54, "KBE\\KBE Эксперт\\1 ОКНА\\Открывание внутрь (ств. Z77)",
                    Layout.VERT, Type.RECTANGL, 700, 1400, 1009, 1009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addArea(new GsonElem(Layout.VERT, Type.AREA, 650))
                    .addElem(new GsonElem(Type.GLASS, "{artglasID:4663}"));
            rootGson.addElem(new GsonElem(Type.IMPOST));
            rootGson.addArea(new GsonElem(Layout.VERT, Type.AREA, 650))
                    .addElem(new GsonElem(Type.GLASS, "{artglasID:4663}"));

            //Тут просочилась ручка не подходящая по параметру. Возможно ошибка ПрофСтроя4
        } else if (prj == 601010) { //PUNIC = 427852 
            rootGson = new GsonRoot("1.0", prj, 1, 54, "KBE\\KBE Эксперт\\1 ОКНА\\Открывание внутрь (ств. Z77)",
                    Layout.HORIZ, Type.RECTANGL, 1300, 1400, 1009, 1009, 1009, "{ioknaParam:[-8558]}"); //параметр недействительный, подогнал для ps спецификации
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addArea(new GsonElem(Layout.VERT, Type.AREA, 650))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{sysprofID:1121, typeOpen:1, sysfurnID:2335}")) //,artiklHandl:2159,colorHandl:1009}"))
                    .addElem(new GsonElem(Type.GLASS, "{artglasID:4663}"));
            rootGson.addElem(new GsonElem(Type.IMPOST));
            rootGson.addArea(new GsonElem(Layout.VERT, Type.AREA, 650))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{sysprofID:1121, typeOpen:4, sysfurnID:2916}")) //,artiklHandl:5058,colorHandl:1009}"))
                    .addElem(new GsonElem(Type.GLASS, "{artglasID:4663}"));

        } else if (prj == 700027) {  //punic = 427872 штульповое
            rootGson = new GsonRoot("1.0", prj, 1, 198, "Montblanc / Eco / 1 ОКНА (штульп)",
                    Layout.HORIZ, Type.RECTANGL, 1300, 1400, 1009, 1009, 1009, "{ioknaParam:[-8252]}"); //параметр недействительный, подогнал для ps спецификации
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addArea(new GsonElem(Layout.VERT, Type.AREA, 450))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen:1, sysfurnID:2915}"))
                    .addElem(new GsonElem(Type.GLASS, "{artglasID:3293}"));
            rootGson.addElem(new GsonElem(Type.SHTULP))
                    .addArea(new GsonElem(Layout.VERT, Type.AREA, 850))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen:4, sysfurnID:2913}"))
                    .addElem(new GsonElem(Type.GLASS, "{artglasID:4267}"));

        } else if (prj == 508634) { //PUNIC = 427422
            rootGson = new GsonRoot("1.0", prj, 1, 303, "Rehau\\Delight\\4 ОКНА(ФИГУРНАЯ СТВОРКА)",
                    Layout.HORIZ, Type.RECTANGL, 900, 1400, 1009, 1009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 508777) {  //PUNIC = 427565  Стойка - ригель, надо тестировать
            rootGson = new GsonRoot("1.0", prj, 1, 303, "Rehau\\Delight\\4 ОКНА(ФИГУРНАЯ СТВОРКА)",
                    Layout.HORIZ, Type.RECTANGL, 900, 1400, 1009, 1009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addElem(new GsonElem(Type.GLASS));
            // </editor-fold>

// <editor-fold defaultstate="collapsed" desc="ARCH"> 
            //Нерешённая проблема со штапиком
        } else if (prj == 604004) { //PUNIC = 427858
            rootGson = new GsonRoot("1.0", prj, 1, 37, "Rehau\\Delight\\1 ОКНА",
                    Layout.VERT, Type.ARCH, 1300, 1700, 1050, 1009, 1009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addArea(new GsonElem(Layout.HORIZ, Type.AREA, 650, Form.TOP))
                    .addElem(new GsonElem(Type.GLASS));
            rootGson.addElem(new GsonElem(Type.IMPOST, "{sysprofID:3246}"));
            GsonElem area = rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1050));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 650))
                    .addElem(new GsonElem(Type.GLASS));
            area.addElem(new GsonElem(Type.IMPOST, "{sysprofID:3246}"));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 650))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen:4, sysfurnID:91}"))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 604005) { //PUNIC = 427833
            rootGson = new GsonRoot("1.0", prj, 1, 135, "Wintech\\Termotech 742\\1 ОКНА",
                    Layout.VERT, Type.ARCH, 1300, 1500, 1200, 1009, 10009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addArea(new GsonElem(Layout.HORIZ, Type.AREA, 300, Form.TOP))
                    .addElem(new GsonElem(Type.GLASS));
            rootGson.addElem(new GsonElem(Type.IMPOST));
            GsonElem area = rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1200));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 650))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen:1, sysfurnID:2745}"))
                    .addElem(new GsonElem(Type.GLASS));
            area.addElem(new GsonElem(Type.IMPOST));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 650))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen:4, sysfurnID:2744}"))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 604006) { //PUNIC = 427832
            rootGson = new GsonRoot("1.0", prj, 1, 135, "Wintech\\Termotech 742\\1 ОКНА",
                    Layout.VERT, Type.ARCH, 1100, 1600, 1220, 1009, 1009, 10012);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addArea(new GsonElem(Layout.HORIZ, Type.AREA, 380, Form.TOP))
                    .addElem(new GsonElem(Type.GLASS));
            rootGson.addElem(new GsonElem(Type.IMPOST));
            GsonElem area = rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1220));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 550))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen:1, sysfurnID:2745}"))
                    .addElem(new GsonElem(Type.GLASS));
            area.addElem(new GsonElem(Type.IMPOST));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 550))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen:4, sysfurnID:2744}"))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 604007) { //PUNIC = 427831
            rootGson = new GsonRoot("1.0", prj, 1, 99, "Rehau\\Blitz new\\1 ОКНА",
                    Layout.VERT, Type.ARCH, 1400, 1700, 1300, 1009, 1009, 10001);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addArea(new GsonElem(Layout.HORIZ, Type.AREA, 400, Form.TOP))
                    .addElem(new GsonElem(Type.GLASS));
            rootGson.addElem(new GsonElem(Type.IMPOST));
            GsonElem area = (GsonElem) rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1300));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 700))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen:1, sysfurnID:535}"))
                    .addElem(new GsonElem(Type.GLASS));
            area.addElem(new GsonElem(Type.IMPOST));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 700))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen:3, sysfurnID:534}"))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 604008) { //PUNIC = 427830
            rootGson = new GsonRoot("1.0", prj, 1, 8, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)",
                    Layout.VERT, Type.ARCH, 1300, 1500, 1200, 1009, 10009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addArea(new GsonElem(Layout.HORIZ, Type.AREA, 300, Form.TOP))
                    .addElem(new GsonElem(Type.GLASS));
            rootGson.addElem(new GsonElem(Type.IMPOST));
            GsonElem area = (GsonElem) rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1200));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 650))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen:3, sysfurnID:-1}"))
                    .addElem(new GsonElem(Type.GLASS));
            area.addElem(new GsonElem(Type.IMPOST));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 650))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen:3, sysfurnID:-1}"))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 604009) { //PUNIC = 427825
            rootGson = new GsonRoot("1.0", prj, 1, 8, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)",
                    Layout.VERT, Type.ARCH, 1300, 1500, 1200, 1009, 10009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addArea(new GsonElem(Layout.HORIZ, Type.AREA, 300, Form.TOP))
                    .addElem(new GsonElem(Type.GLASS));
            rootGson.addElem(new GsonElem(Type.IMPOST));
            rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1200))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 604010) { //PUNIC = 427826
            rootGson = new GsonRoot("1.0", prj, 1, 29, "Montblanc\\Nord\\1 ОКНА",
                    Layout.VERT, Type.ARCH, 1300, 1700, 1400, 1009, 10009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addArea(new GsonElem(Layout.HORIZ, Type.AREA, 300, Form.TOP))
                    .addElem(new GsonElem(Type.GLASS));
            rootGson.addElem(new GsonElem(Type.IMPOST));
            GsonElem area = (GsonElem) rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1400));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 650))
                    .addElem(new GsonElem(Type.GLASS));
            area.addElem(new GsonElem(Type.IMPOST));
            GsonElem area2 = (GsonElem) area.addArea(new GsonElem(Layout.VERT, Type.AREA, 650));
            area2.addArea(new GsonElem(Layout.VERT, Type.AREA, 557))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen:4, sysfurnID:701}"))
                    .addElem(new GsonElem(Type.GLASS));
            area2.addElem(new GsonElem(Type.IMPOST));
            area2.addArea(new GsonElem(Layout.VERT, Type.AREA, 843))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 508983) { //PUNIC = 427779
            rootGson = new GsonRoot("1.0", prj, 1, 17, "KBE\\KBE 58\\3 НЕПРЯМОУГОЛЬНЫЕ ОКНА/ДВЕРИ\\АРКИ",
                    Layout.VERT, Type.ARCH, 1300, 1300, 1000, 1010, 10000, 10000);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addArea(new GsonElem(Layout.HORIZ, Type.AREA, 300, Form.TOP))
                    .addElem(new GsonElem(Type.GLASS));
            rootGson.addElem(new GsonElem(Type.IMPOST));
            GsonElem area = rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1000));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 650))
                    .addElem(new GsonElem(Type.GLASS));
            area.addElem(new GsonElem(Type.IMPOST));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 650))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen:4, sysfurnID:316}"))
                    .addElem(new GsonElem(Type.GLASS));
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="TRAPEZE"> 
        } else if (prj == 486451) { //PUNIC = 426696 onumb=3  Трапеции без импоста
            rootGson = new GsonRoot("1.0", prj, 3, 8, "KBE\\KBE 58\\1 ОКНА\\*Открывание внутрь (ств. Z77)",
                    Layout.VERT, Type.TRAPEZE, Form.RIGHT, 820, 1360, 825, 1009, 10009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 506642) { //PUNIC = 425392 onumb=1  Трапеции без импоста
            rootGson = new GsonRoot("1.0", prj, 1, 54, "KBE / KBE Эксперт / 1 ОКНА / Открывание внутрь (ств. Z 77)",
                    Layout.VERT, Type.TRAPEZE, Form.RIGHT, 1000, 1300, 950, 1009, 10009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addElem(new GsonElem(Type.GLASS, "{artglasID: 5241}"));

        } else if (prj == -506642) { //PUNIC = 425392 onumb=1  Трапеции без импоста
            rootGson = new GsonRoot("1.0", prj, 1, 54, "KBE / KBE Эксперт / 1 ОКНА / Открывание внутрь (ств. Z 77)",
                    Layout.VERT, Type.TRAPEZE, Form.LEFT, 1000, 950, 1300, 1009, 10009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addElem(new GsonElem(Type.GLASS, "{artglasID: 5241}"));

        } else if (prj == 605001) { //PUNIC = 427850  Трапеции
            rootGson = new GsonRoot("1.0", prj, 1, 8, "KBE\\KBE 58\\1 ОКНА\\*Открывание внутрь (ств. Z77)",
                    Layout.VERT, Type.TRAPEZE, 1300, 1500, 1200, 1009, 10009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addArea(new GsonElem(Layout.VERT, Type.AREA, 300, Form.RIGHT))
                    .addElem(new GsonElem(Type.GLASS));
            rootGson.addElem(new GsonElem(Type.IMPOST))
                    .addArea(new GsonElem(Layout.VERT, Type.AREA, 1200))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == -605001) { // Трапеции
            rootGson = new GsonRoot("1.0", prj, 1, 8, "KBE\\KBE 58\\1 ОКНА\\*Открывание внутрь (ств. Z77)",
                    Layout.VERT, Type.TRAPEZE, 1300, 1200, 1500, 1009, 10009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addArea(new GsonElem(Layout.VERT, Type.AREA, 300, Form.LEFT))
                    .addElem(new GsonElem(Type.GLASS));
            rootGson.addElem(new GsonElem(Type.IMPOST))
                    .addArea(new GsonElem(Layout.VERT, Type.AREA, 1200))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 508916) { //PUNIC = 427708  Трапеции
            rootGson = new GsonRoot("1.0", prj, 2, 8, "KBE\\KBE 58\\1 ОКНА\\*Открывание внутрь (ств. Z77)",
                    Layout.VERT, Type.TRAPEZE, 900, 1400, 1000, 1009, 10005, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addArea(new GsonElem(Layout.VERT, Type.AREA, 400, Form.RIGHT))
                    .addElem(new GsonElem(Type.GLASS));
            rootGson.addElem(new GsonElem(Type.IMPOST))
                    .addArea(new GsonElem(Layout.VERT, Type.AREA, 1000))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen:4, sysfurnID:1633}"))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == -508916) { //PUNIC = 427708  Трапеции
            rootGson = new GsonRoot("1.0", prj, 2, 8, "KBE\\KBE 58\\1 ОКНА\\*Открывание внутрь (ств. Z77)",
                    Layout.VERT, Type.TRAPEZE, 900, 1000, 1400, 1009, 10005, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addArea(new GsonElem(Layout.VERT, Type.AREA, 400, Form.LEFT))
                    .addElem(new GsonElem(Type.GLASS));
            rootGson.addElem(new GsonElem(Type.IMPOST))
                    .addArea(new GsonElem(Layout.VERT, Type.AREA, 1000))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen:4, sysfurnID:1633}"))
                    .addElem(new GsonElem(Type.GLASS));

//        } else if (prj == 508945) { //PUNIC = 427737  Трапеции
//            rootGson = new GsonRoot("1.0", prj, 2, 8, "KBE\\KBE 58\\1 ОКНА\\*Открывание внутрь (ств. Z77)",
//                    Layout.VERT, Type.TRAPEZE, 600, 600, 1400, 1009, 1009, 1009);
//            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
//                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
//                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
//                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
//                    .addArea(new GsonElem(Layout.VERT, Type.AREA, 800, Form.LEFT))
//                    .addElem(new GsonElem(Type.GLASS));
//            rootGson.addElem(new GsonElem(Type.IMPOST))
//                    .addArea(new GsonElem(Layout.VERT, Type.AREA, 600))
//                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen:4, sysfurnID:1633}"))
//                    .addElem(new GsonElem(Type.GLASS));
            //425179   506436
            //426309   507550 
            //790 = 175 + 175 + 440, 440 + 175 = 615,  h = 790,  w = 790
        } else if (prj == 506929) { //PUNIC = 425688 onumb=1
            rootGson = new GsonRoot("1.0", prj, 1, 202, "Montblanc\\Eco\\НЕПРЯМОУГОЛЬНЫЕ ОКНА",
                    Layout.VERT, Type.TRAPEZE, Form.SYMM, 790, 440, 615, 615, 1009, 1009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 508945) { //PUNIC = 427737 onumb=1
            rootGson = new GsonRoot("1.0", prj, 6, 8, "KBE\\KBE 58\\ОКНА\\Открывание внутрь (ств. Z77)",
                    Layout.HORIZ, Type.TRAPEZE, Form.SYMM, 2000, 1000, 1400, 1000, 1009, 1009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOPR))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOPL))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addArea(new GsonElem(Layout.HORIZ, Type.AREA, 600))
                    .addElem(new GsonElem(Type.GLASS));
            rootGson.addElem(new GsonElem(Type.IMPOST))
                    .addArea(new GsonElem(Layout.HORIZ, Type.AREA, 800))
                    .addElem(new GsonElem(Type.GLASS));
            rootGson.addElem(new GsonElem(Type.IMPOST))
                    .addArea(new GsonElem(Layout.HORIZ, Type.AREA, 600))
                    .addElem(new GsonElem(Type.GLASS));
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="DOOR"> 
        } else if (prj == 508841) { //427629 Двери
            rootGson = new GsonRoot("1.0", prj, 2, 8, "KBE / KBE Эксперт / 6 ВХОДНЫЕ ДВЕРИ / Дверь наружу",
                    Layout.VERT, Type.DOOR, 900, 2100, 1009, 1009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT));
            GsonElem stv = rootGson.addArea(new GsonElem(Layout.VERT, Type.STVORKA));
            stv.addArea(new GsonElem(Layout.VERT, Type.AREA, 1300))
                    .addElem(new GsonElem(Type.GLASS));
            stv.addElem(new GsonElem(Type.IMPOST))
                    .addArea(new GsonElem(Layout.VERT, Type.AREA, 800))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 700009) { //PUNIC = 427847 Двери
            rootGson = new GsonRoot("1.0", prj, 2, 330, "Darrio\\Двери DARRIO\\Дверь внутрь",
                    Layout.VERT, Type.DOOR, 900, 2000, 1009, 10004, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT));
            GsonElem stv = rootGson.addArea(new GsonElem(Layout.VERT, Type.STVORKA));
            stv.addArea(new GsonElem(Layout.VERT, Type.AREA, 1400))
                    .addElem(new GsonElem(Type.GLASS));
            stv.addElem(new GsonElem(Type.IMPOST))
                    .addArea(new GsonElem(Layout.VERT, Type.AREA, 600))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 700014) { //PUNIC = 427856  Двери
            rootGson = new GsonRoot("1.0", prj, 1, 66, "Rehau\\Brilliant\\4 ДВЕРИ ВХОДНЫЕ\\Дверь наружу",
                    Layout.VERT, Type.DOOR, 900, 2100, 1009, 1009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT));
            GsonElem stv = rootGson.addArea(new GsonElem(Layout.VERT, Type.STVORKA));
            stv.addArea(new GsonElem(Layout.VERT, Type.AREA, 600))
                    .addElem(new GsonElem(Type.GLASS));
            stv.addElem(new GsonElem(Type.IMPOST))
                    .addArea(new GsonElem(Layout.VERT, Type.AREA, 1500))
                    .addElem(new GsonElem(Type.GLASS));
        } // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="ХОЛОДНЫЙ  ТЕСТ, спецификации в базе нет">         
        else if (prj == 1043598818) { //Прямоугольное сложное
            rootGson = new GsonRoot("1.0", prj, 1, 8, "KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)",
                    Layout.VERT, Type.RECTANGL, 1440, 1700, 1009, 1009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT));
            GsonElem areaT = rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 400));
            areaT.addArea(new GsonElem(Layout.VERT, Type.AREA, 420))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen:1, sysfurnID:1634}"))
                    .addElem(new GsonElem(Type.GLASS));
            areaT.addElem(new GsonElem(Type.IMPOST))
                    .addArea(new GsonElem(Layout.VERT, Type.AREA, 1020))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen:4, sysfurnID:1633}"))
                    .addElem(new GsonElem(Type.GLASS));
            rootGson.addElem(new GsonElem(Type.IMPOST));
            GsonElem area = rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1300));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 720))
                    .addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen:1, sysfurnID:1634}"))
                    .addElem(new GsonElem(Type.GLASS));
            area.addElem(new GsonElem(Type.IMPOST));
            GsonElem area2 = area.addArea(new GsonElem(Layout.VERT, Type.AREA, 720));
            area2.addArea(new GsonElem(Layout.VERT, Type.AREA, 200))
                    .addElem(new GsonElem(Type.GLASS));
            area2.addElem(new GsonElem(Type.IMPOST))
                    .addArea(new GsonElem(Layout.VERT, Type.AREA, 400))
                    .addElem(new GsonElem(Type.GLASS));
            area2.addElem(new GsonElem(Type.IMPOST));
            GsonElem area3 = area2.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 700));
            area3.addArea(new GsonElem(Layout.VERT, Type.AREA, 220))
                    .addElem(new GsonElem(Type.GLASS));
            area3.addElem(new GsonElem(Type.IMPOST))
                    .addArea(new GsonElem(Layout.VERT, Type.AREA, 300))
                    .addElem(new GsonElem(Type.GLASS));
            area3.addElem(new GsonElem(Type.IMPOST))
                    .addArea(new GsonElem(Layout.VERT, Type.AREA, 200))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 1489528103) { //Прямоугольное сложное без створок
            rootGson = new GsonRoot("1.0", prj, 1, 8, "KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)",
                    Layout.VERT, Type.RECTANGL, 1440, 1700, 1009, 1009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT));
            GsonElem areaT = rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 400));
            areaT.addArea(new GsonElem(Layout.VERT, Type.AREA, 420))
                    //.addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen:1, sysfurnID:1634}"))
                    .addElem(new GsonElem(Type.GLASS));
            areaT.addElem(new GsonElem(Type.IMPOST))
                    .addArea(new GsonElem(Layout.VERT, Type.AREA, 1020))
                    //.addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen:4, sysfurnID:1633}"))
                    .addElem(new GsonElem(Type.GLASS));
            rootGson.addElem(new GsonElem(Type.IMPOST));
            GsonElem area = rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 1300));
            area.addArea(new GsonElem(Layout.VERT, Type.AREA, 720))
                    //.addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen:1, sysfurnID:1634}"))
                    .addElem(new GsonElem(Type.GLASS));
            area.addElem(new GsonElem(Type.IMPOST))
                    .addArea(new GsonElem(Layout.VERT, Type.AREA, 720))
                    //.addArea(new GsonElem(Layout.VERT, Type.STVORKA, "{typeOpen:4, sysfurnID:1633}"))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 1620870217) { //Трапеции
            rootGson = new GsonRoot("1.0", prj, 1, 8, "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)",
                    Layout.VERT, Type.TRAPEZE, Form.RIGHT, 1300, 1500, 1200, 1009, 10009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 905754876) { //Трапеции
            rootGson = new GsonRoot("1.0", prj, 1, 8, "KBE\\KBE 58\\1 ОКНА\\*Открывание внутрь (ств. Z77)",
                    Layout.VERT, Type.TRAPEZE, Form.RIGHT, 1300, 1200, 1500, 1009, 10009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 1413114169) { //Трапеции
            rootGson = new GsonRoot("1.0", prj, 1, 8, "KBE\\KBE 58\\1 ОКНА\\*Открывание внутрь (ств. Z77)",
                    Layout.VERT, Type.TRAPEZE, 1300, 1200, 1500, 1009, 10009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT))
                    .addArea(new GsonElem(Layout.VERT, Type.AREA, 400, Form.LEFT))
                    .addElem(new GsonElem(Type.GLASS));
            rootGson.addElem(new GsonElem(Type.IMPOST))
                    .addArea(new GsonElem(Layout.VERT, Type.AREA, 1100))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 770802872) { //TEST
            rootGson = new GsonRoot("1.0", prj, 1, 8, "KBE\\KBE 58\\1 ОКНА\\*Открывание внутрь (ств. Z77)",
                    Layout.VERT, Type.RECTANGL, 1200, 1400, 1009, 1009, 1009);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT));
            GsonElem area1 = rootGson.addArea(new GsonElem(Layout.VERT, Type.AREA, 600));
            area1.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 200))
                    .addElem(new GsonElem(Type.GLASS));
            area1.addElem(new GsonElem(Type.IMPOST));
            area1.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 400))
                    .addElem(new GsonElem(Type.GLASS));
            rootGson.addElem(new GsonElem(Type.IMPOST));
            GsonElem area2 = rootGson.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 800));
            area2.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 200))
                    .addElem(new GsonElem(Type.GLASS));
            area2.addElem(new GsonElem(Type.IMPOST));

            GsonElem area3 = area2.addArea(new GsonElem(Layout.VERT, Type.AREA, 300));
            area3.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 400))
                    .addElem(new GsonElem(Type.GLASS));
            area3.addElem(new GsonElem(Type.IMPOST))
                    .addArea(new GsonElem(Layout.HORIZ, Type.AREA, 400))
                    .addElem(new GsonElem(Type.GLASS));

            area2.addElem(new GsonElem(Type.IMPOST));
            area2.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 400))
                    .addElem(new GsonElem(Type.GLASS));
            area2.addElem(new GsonElem(Type.IMPOST));
            area2.addArea(new GsonElem(Layout.HORIZ, Type.AREA, 300))
                    .addElem(new GsonElem(Type.GLASS));

        } else if (prj == 912042749) { //PUNIC = 427856  Двери (пока в тесте)
            rootGson = new GsonRoot("1.0", prj, 1, 10, "ALUTECH\\ALT.W62\\Двери\\Внутрь(1)",
                    Layout.VERT, Type.DOOR, 900, 2100, 0, 0, 0);
            rootGson.addElem(new GsonElem(Type.FRAME_SIDE, Layout.BOTT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.RIGHT))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.TOP))
                    .addElem(new GsonElem(Type.FRAME_SIDE, Layout.LEFT));
            rootGson.addArea(new GsonElem(Layout.VERT, Type.STVORKA))
                    .addElem(new GsonElem(Type.GLASS));
        } // </editor-fold>          
        else {
            return null;
        }
        return rootGson.toJson();
    }
}
