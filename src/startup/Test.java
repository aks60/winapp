package startup;

import frames.FrameToFile;
import builder.script.GsonRoot;
import builder.script.Winscript;
import common.*;
import dataset.*;
import com.google.gson.GsonBuilder;
import builder.param.test.ElementTest;
import builder.param.test.FillingTest;
import builder.param.test.JoiningTest;
import domain.eElement;
import frames.DBCompare;
import java.sql.Connection;
import javax.swing.UIManager;
import java.util.List;
import java.util.UUID;

public class Test {

    public static Integer numDb = Integer.valueOf(eProperty.base_num.read());

    // <editor-fold defaultstate="collapsed" desc="Connection[] connect(int numDb)">
    public static Connection connect1() {
        try {
            String db = (numDb == 1) ? eProperty.base1.read() : (numDb == 2) ? eProperty.base2.read() : eProperty.base3.read();
            if (db.toUpperCase().contains("BIMAX.FDB")) {
                return java.sql.DriverManager.getConnection("jdbc:firebirdsql:localhost/3050:D:\\Okna\\Database\\ps4\\ITEST.FDB?encoding=win1251", "sysdba", "masterkey");
            } else if (db.toUpperCase().contains("SIAL3.FDB")) {
                return java.sql.DriverManager.getConnection("jdbc:firebirdsql:localhost/3055:D:\\Okna\\Database\\ps3\\sial3.fdb?encoding=win1251", "sysdba", "masterkey");
            } else if (db.toUpperCase().contains("ALUTECH3.FDB")) {
                return java.sql.DriverManager.getConnection("jdbc:firebirdsql:localhost/3055:D:\\Okna\\Database\\ps3\\alutech3x.fdb?encoding=win1251", "sysdba", "masterkey");
            } else if (db.toUpperCase().contains("KRAUSS.FDB")) {
                return java.sql.DriverManager.getConnection("jdbc:firebirdsql:localhost/3050:D:\\Okna\\Database\\ps4\\krauss.fdb?encoding=win1251", "sysdba", "masterkey");
            } else if (db.toUpperCase().contains("VIDNAL.FDB")) {
                return java.sql.DriverManager.getConnection("jdbc:firebirdsql:localhost/3050:D:\\Okna\\Database\\ps4\\vidnal.fdb?encoding=win1251", "sysdba", "masterkey");
            } else if (db.toUpperCase().contains("SOKOL.FDB")) {
                return java.sql.DriverManager.getConnection("jdbc:firebirdsql:localhost/3055:D:\\Okna\\Database\\ps4\\sokol.fdb?encoding=win1251", "sysdba", "masterkey");
            }
            return null;
        } catch (Exception e) {
            System.err.println("Ошибка:Test.connect() " + e);
            return null;
        }
    }

    public static Connection connect2() {
        try {
            eProperty.user.write("sysdba");
            eProperty.password = String.valueOf("masterkey");
            Conn con = Conn.initConnect();
            con.createConnection(eProperty.server(numDb.toString()), eProperty.port(numDb.toString()), eProperty.base(numDb.toString()), eProperty.user.read(), eProperty.password.toCharArray(), null);;
            return con.getConnection();
        } catch (Exception e) {
            System.err.println("Ошибка:Test.connect() " + e);
            return null;
        }
    }

    // </editor-fold>     
    //
    public static void main(String[] args) { //java -jar C:\\Okna\\winapp\\dist\\winapp.jar dev loc

        Main.dev = true;
        try {
            //convert.Profstroy.exec();
            wincalc();
            //param();
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

        Query.connection = Test.connect2();
        builder.Wincalc iwin = new builder.Wincalc();
        String _case = "max";

        if (_case.equals("one")) {
            iwin.build(builder.script.Winscript.test(601008, false));
            iwin.constructiv(true);
            //Specific.write_txt1(iwin.listSpec);
            //DBCompare.iwinXls(iwin, true);
            DBCompare.iwinRec(iwin, true);
            //iwin.mapJoin.entrySet().forEach(it -> System.out.println("id=" + it.getValue().id + "  JOIN=" + it.getValue().type + "  POINT:" + it.getKey() + " (" + it.getValue().elem1.spcRec.artikl + ":" + it.getValue().elem2.spcRec.artikl + ") -" + it.getValue().layout.name));           

        } else if (_case.equals("min")) {
            List<Integer> prjList = Winscript.models();
            for (int prj : prjList) {
                String script = builder.script.Winscript.test(prj, false);
                if (script != null) {
                    iwin.build(script);
                    iwin.constructiv(true);
                    //DBCompare.iwinXls(iwin, false);
                    DBCompare.iwinRec(iwin, false);
                }
            }

        } else if (_case.equals("max")) {
            List<Integer> prjList = Winscript.models();
            for (int prj : prjList) {
                String script = builder.script.Winscript.test(prj, false);
                if (script != null) {
                    iwin.build(script);
                    iwin.constructiv(true);
                    //DBCompare.iwinXls(iwin, false);
                    DBCompare.iwinRec(iwin, false);
                }
            }
        }
    }

    private static void param() {

        Query.connection = Test.connect2();
        ElementTest et = new ElementTest();
        JoiningTest jt = new JoiningTest();
        FillingTest gt = new FillingTest();
//        et.elementVar();
//        et.elementDet();
//        jt.joiningVar();
//        jt.joiningDet();
//        gt.fillingVar();
        gt.fillingDet();
        
//        Query.connection = Test.connect2();
//        Set set = new HashSet();
//        Map<String, Set> map = new HashMap();
//        for (Enam en : ParamList.values()) {
//            Set set2 = map.getOrDefault(en.text(), new HashSet());
//            set2.add(en.numb());
//            map.put(en.text(), set2);
//        }
//        for (Map.Entry<String, Set> entry : map.entrySet()) {
//            String key = entry.getKey();
//            Set value = entry.getValue();
//            System.out.println(key + " " + value);
//        }
    }

    private static void frame() throws Exception {

        Query.connection = Test.connect2();
        lookAndFeel();
        Tex app = new Tex();
        app.setVisible(true);
        frames.Joining frm = new frames.Joining();
        FrameToFile.setFrameSize(frm);
        //frm.iwin.build(Winscript.test(Winscript.rootGson.prj, null));
        frm.setVisible(true);
    }

    private static void query() {
        try {
            Query.connection = Test.connect2();
            Object obj = eElement.find3(1386, 33);
            System.out.println(obj);

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

        Query.connection = Test.connect2();
        builder.Wincalc iwin = new builder.Wincalc();
        String script = Winscript.test(601004, false);
        iwin.build(script);

        GsonBuilder builder = new GsonBuilder();
        //builder.registerTypeAdapter(Element.class, new GsonDeserializer<Element>());
        //builder.setPrettyPrinting();
        GsonRoot root = builder.create().fromJson(script, GsonRoot.class);

        System.out.println(root.color1);
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

    private static void uid() {

        UUID idOne = UUID.randomUUID();
        String str = "" + idOne;
        int uid = str.hashCode();
        String filterStr = "" + uid;
        str = filterStr.replaceAll("-", "");
        System.out.println(Integer.parseInt(str));
    }
}
