package startup;

import builder.script.GsonRoot;
import builder.script.Winscript;
import common.*;
import dataset.*;
import domain.eArtdet;
import builder.specif.Specification;
import com.google.gson.GsonBuilder;
import java.sql.Connection;
import javax.swing.UIManager;
import java.util.Arrays;
import java.util.UUID;

public class Test {

    public static void main(String[] args) { //java -jar C:\\Okna\\winapp\\dist\\winapp.jar dev loc
        Main.dev = true;
        try {
            //convert.Profstroy.exec2();
            wincalc();
            //query();
            //frame();
            //json();
            //parse();
            //uid();
        } catch (Exception e) {
            System.err.println("TEST-MAIN: " + e);
        }
    }

    private static void wincalc() throws Exception {

        Query.connection = connection();
        builder.Wincalc iwin = new builder.Wincalc();
        String _case = "one";

        if (_case.equals("one")) {
            iwin.prj = 601001;
            iwin.build(builder.script.Winscript.test(iwin.prj, false));
            iwin.constructiv();
            //Specification.write_txt1(iwin.listSpec);
            Specification.compareIWin(iwin.listSpec, iwin.prj, true);
            //iwin.mapJoin.entrySet().forEach(it -> System.out.println("id=" + it.getValue().id + "  JOIN=" + it.getValue().typeJoin + "  POINT:" + it.getKey() + " (" + it.getValue().joinElement1.specificationRec.artikl + ":" + it.getValue().joinElement2.specificationRec.artikl + ") -" + it.getValue().layoutJoin.name));           
        } else {
            if (_case.equals("min")) {
                for (int i : Arrays.asList(601001, 601002, 601007)) {
                    iwin.prj = i;
                    String script = builder.script.Winscript.test(iwin.prj, false);
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
                    String script = builder.script.Winscript.test(iwin.prj, false);
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
        Tex app = new Tex();
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
//
//        JsonObject jsonObj = new Gson().fromJson(paramJson, JsonObject.class);
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

    private static void json() {

        Query.connection = connection();
        builder.Wincalc iwin = new builder.Wincalc();
        String script = Winscript.test(601004, false);
        iwin.build(script);

        GsonBuilder builder = new GsonBuilder();
        //builder.registerTypeAdapter(Element.class, new GsonDeserializer<Element>());
        //builder.setPrettyPrinting();
        GsonRoot root = builder.create().fromJson(script, GsonRoot.class);

        System.out.println(root.color(1));
//        
//        for (Element el : root.getElements()) {
//            System.out.println(el.getElemType());
//        }
//        for (AreaElem ar : root.getAreas()) {
//            System.out.println(ar.getElemType());
//            for (Element el : ar.getElements()) {
//                System.out.println(el.getElemType());
//            }
//            for (AreaElem ar2 : ar.getAreas()) {
//                System.out.println(ar2.getElemType());
//                for (Element el2 : ar2.getElements()) {
//                    System.out.println(el2.getElemType());
//                }
//                for (AreaElem ar3 : ar2.getAreas()) {
//                    System.out.println(ar3.getElemType());
//                    for (Element el3 : ar3.getElements()) {
//                        System.out.println(el3.getElemType());
//                    }
//
//                }
//
//            }
//        }
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
        String num_base = eProperty.base_num.read();
        Conn con = Conn.initConnect();
        con.createConnection(eProperty.server(num_base), eProperty.port(num_base), eProperty.base(num_base), eProperty.user.read(), eProperty.password.toCharArray(), null);;
        return con.getConnection();
    }

    private static void uid() {

        UUID idOne = UUID.randomUUID();
        String str = "" + idOne;
        int uid = str.hashCode();
        String filterStr = "" + uid;
        str = filterStr.replaceAll("-", "");
        System.out.println(Integer.parseInt(str));
    }

}
