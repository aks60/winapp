package startup;

import common.*;
import dataset.*;
import enums.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import domain.eArtdet;
import domain.eParams;
import builder.specif.Specification;
import java.sql.Connection;
import java.util.HashMap;
import javax.swing.UIManager;
import java.util.Arrays;

public class Test {

    public static void main(String[] args) { //java -jar C:\\Okna\\winapp\\dist\\winapp.jar dev loc
        Main.dev = true;
        try {
            convert.Profstroy.exec2();
            //wincalc();
            //query();
            //frame();
            //parse();
        } catch (Exception e) {
            System.err.println("TEST-MAIN: " + e);
        }
    }

    private static void wincalc() throws Exception {

        Query.connection = connection();
        builder.Wincalc iwin = new builder.Wincalc();
        String _case = "max";

        if (_case.equals("one")) {
            iwin.prj = 601003;
            iwin.build(builder.script.Winscript.test(iwin.prj, null));
            iwin.constructiv();
            //Specification.write_txt1(iwin.listSpec);
            Specification.compareIWin(iwin.listSpec, iwin.prj, true);
            //iwin.mapJoin.entrySet().forEach(it -> System.out.println("id=" + it.getValue().id + "  JOIN=" + it.getValue().typeJoin + "  POINT:" + it.getKey() + " (" + it.getValue().joinElement1.specificationRec.artikl + ":" + it.getValue().joinElement2.specificationRec.artikl + ") -" + it.getValue().layoutJoin.name));           
        } else {
            if (_case.equals("min")) {
                for (int i : Arrays.asList(601001, 601002, 601007)) {
                    iwin.prj = i;
                    String script = builder.script.Winscript.test(iwin.prj, null);
                    if (script != null) {
                        iwin.build(script);
                        iwin.constructiv();
                        Specification.compareIWin(iwin.listSpec, iwin.prj, false);
                    }
                }
            } else if (_case.equals("max")) {
                for (int i : Arrays.asList(601001, 601002, 601003, 601004, 601005, 601006, 601007,
                        601008, 601009, 601010, 604004, 604005, 604006, 604007, 604008, 604009, 604010)) {
                    iwin.prj = i;
                    String script = builder.script.Winscript.test(iwin.prj, null);
                    if (script != null) {
                        iwin.build(script);
                        iwin.constructiv();
                        Specification.compareIWin(iwin.listSpec, iwin.prj, false);
                    }
                }
            }
        }
    }

    private static void frame() throws Exception {

        Query.connection = connection();
        lookAndFeel();
        App1 app = new App1();
        app.setVisible(true);
        frames.Joining frm = new frames.Joining();
        FrameToFile.setFrameSize(frm);
        //frm.iwin.build(Winscript.test(Winscript.prj, null));
        frm.setVisible(true);
    }

    private static void query() {
        try {
            Query.connection = connection();
            Query qArtdet = new Query(eArtdet.values()).select(eArtdet.up, "where", eArtdet.id, "=", 19143);
            Record artdetRec = qArtdet.get(0);
            int side = 1;
            if ((side == 1 && "1".equals(artdetRec.getStr(eArtdet.mark_c1)))
                    || (side == 2 && ("1".equals(artdetRec.getStr(eArtdet.mark_c2)) || "1".equals(artdetRec.getStr(eArtdet.mark_c1))))
                    || (side == 3 && ("1".equals(artdetRec.getStr(eArtdet.mark_c3))) || "1".equals(artdetRec.getStr(eArtdet.mark_c1)))) {

                System.out.println("++++++");
            } else {
                System.out.println("------");
            }
        } catch (Exception e) {
            System.out.println("main.Test.query()");
        }
    }

    private static void parse() {

        System.out.println("11 = " + (11 + (11 << 4) + (11 << 8)));
        System.out.println("21 = " + (15 + (15 << 4) + (15 << 8)));
        System.out.println("31 = " + (1 + (1 << 4) + (1 << 8)));
        System.out.println("32 = " + (2 + (2 << 4) + (2 << 8)));
        System.out.println("33 = " + (3 + (3 << 4) + (3 << 8)));
        System.out.println("41 = " + (6 + (6 << 4) + (6 << 8)));
        System.out.println("42 = " + (7 + (7 << 4) + (7 << 8)));
        System.out.println("43 = " + (8 + (8 << 4) + (8 << 8)));
        System.out.println("50 = " + (11 + (11 << 4) + (11 << 8)));
        System.out.println("60 = " + (11 + (11 << 4) + (11 << 8)));

//        HashMap<Integer, Record> mapParamUse = new HashMap();
//        String paramJson = "{'typeOpen':1,'nuni':23, 'ioknaParam': [[-862107,826],[-862106,830]]}";
//        String str = paramJson.replace("'", "\"");
//
//        JsonObject jsonObj = new Gson().fromJson(str, JsonObject.class);
//        JsonArray jsonArr = jsonObj.getAsJsonArray(ParamJson.ioknaParam.name());
//        if (!jsonArr.isJsonNull() && jsonArr.isJsonArray()) {
//            jsonArr.forEach(it -> {
//                Record paramRec = eParams.find(it.getAsJsonArray().get(0).getAsInt(), it.getAsJsonArray().get(1).getAsInt());
//                mapParamUse.put(paramRec.getInt(eParams.grup), paramRec);
//            });
//            System.out.println(mapParamUse);
//
        //System.out.println(list.get(0));
//            mapParam.put(ParamJson.ioknaParam, jsonObj.get(ParamJson.ioknaParam.name()));
//            HashMap<Integer, Object[]> hmValue = new HashMap();
//            for (int index = 0; index < jsonArr.size(); index++) {
//                JsonArray jsonRec = (JsonArray) jsonArr.get(index);
//                int pnumb = jsonRec.getAsInt();
////                        Parlist rec = Parlist.get(root.getConst(), jsonRec.get(0), jsonRec.get(1));
////                        if (pnumb < 0 && rec != null)
////                            hmValue.put(pnumb, new Object[]{rec.pname, rec.znumb, 0});
//                int mm = 0;
//            }
//            mapParam.put(ParamJson.ioknaParam2, hmValue); //второй вариант                
//        }
    }

    private static void classToJson() {

//        AreaRoot rootArea = new AreaRoot("1", LayoutArea.VERT, TypeElem.SQUARE, 900, 1300, 1300, 1009, 10009, 1009, "");
//        rootArea.setParam("1", 8);
//        rootArea.add(new Element("2", TypeElem.FRAME_BOX, LayoutArea.LEFT));
//        rootArea.add(new Element("3", TypeElem.FRAME_BOX, LayoutArea.RIGHT));
//        rootArea.add(new Element("4", TypeElem.FRAME_BOX, LayoutArea.TOP));
//        rootArea.add(new Element("5", TypeElem.FRAME_BOX, LayoutArea.BOTTOM));
//        AreaElem area2 = (AreaElem) rootArea.add(new AreaElem("6", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':1, 'sysfurnID':23}"));
//        area2.add(new Element("7", TypeElem.GLASS));
    }

    private static void lookAndFeel() {
        try {
            Main.runRussifier();
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(laf.getName())) { //"Windows Classic", "Windows", "CDE/Motif", "Metal", "Nimbus"
                    UIManager.setLookAndFeel(laf.getClassName());
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private static Connection connection() {
        eProperty.user.write("sysdba");
        eProperty.password = String.valueOf("masterkey");
        int num_base = Integer.valueOf(eProperty.base_num.read());
        ConnApp con = ConnApp.initConnect();
        con.createConnection(num_base);
        return con.getConnection();
    }
}
