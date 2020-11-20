package startup;

import common.*;
import dataset.*;
import enums.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import domain.eParams;
import estimate.constr.Specification;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.HashMap;
import javax.swing.UIManager;
import java.util.Arrays;

public class Test {

    public static void main(String[] args) { //java -jar C:\\Okna\\winapp\\dist\\winapp.jar dev loc
        Main.dev = true;
        try {
            //convert.Profstroy.script();
            wincalc();
            //query();
            //frame();
            //parse();
            //test();
        } catch (Exception e) {
            System.err.println("TEST-MAIN: " + e);
        }
    }

    static Connection connection() {
        eProperty.user.write("sysdba");
        eProperty.password = String.valueOf("masterkey");
        int num_base = Integer.valueOf(eProperty.base_num.read());
        ConnApp con = ConnApp.initConnect();
        con.createConnection(num_base);
        return con.getConnection();
    }

    static void wincalc() throws Exception {

        Query.connection = connection();
        estimate.Wincalc iwin = new estimate.Wincalc();
        String _case = "one";

        if (_case.equals("one")) {
            iwin.prj = 601001;
            iwin.build(estimate.script.Winscript.test(iwin.prj, null));
            iwin.constructiv();
            Specification.write_txt1(iwin.listSpec);
            //Specification.compareIWin(iwin.listSpec, iwin.prj, true);
            //iwin.mapJoin.entrySet().forEach(it -> System.out.println("id=" + it.getValue().id + "  JOIN=" + it.getValue().typeJoin + "  POINT:" + it.getKey() + " (" + it.getValue().joinElement1.specificationRec.artikl + ":" + it.getValue().joinElement2.specificationRec.artikl + ") -" + it.getValue().layoutJoin.name));           

        } else {
            if (_case.equals("min")) {
                for (int i : Arrays.asList(601008, 601009, 601010)) {
                    iwin.prj = i;
                    String script = estimate.script.Winscript.test(iwin.prj, null);
                    iwin.build(script);
                    iwin.constructiv();
                    Specification.compareIWin(iwin.listSpec, iwin.prj, true);
                }
            } else if (_case.equals("max")) {
                for (int i : Arrays.asList(601001, 601002, 601003, 601004, 601005, 601006, 601007,
                        601008, 601009, 601010, 604004, 604005, 604006, 604007, 604008, 604009, 604010)) {
                    iwin.prj = i;
                    String script = estimate.script.Winscript.test(iwin.prj, null);
                    iwin.build(script);
                    iwin.constructiv();
                    Specification.compareIWin(iwin.listSpec, iwin.prj, false);
                }
            }
        }
    }

    static void frame() throws Exception {

        Query.connection = connection();
        lookAndFeel();
        App1 app = new App1();
        app.setVisible(true);
        frames.Joining frm = new frames.Joining();
        FrameToFile.setFrameSize(frm);
        //frm.iwin.build(Winscript.test(Winscript.prj, null));
        frm.setVisible(true);
    }

    static void query() {
        try {
            Query.connection = connection();
            DatabaseMetaData dmd = Query.connection.getMetaData();
            String name = dmd.getURL().substring(dmd.getURL().lastIndexOf("\\") + 1);
            System.out.println(dmd.getURL().toCharArray());
            System.out.println(Integer.valueOf(eProperty.base_num.read()));

            //"jdbc:firebirdsql:localhost/3050:D:\\Okna\\Database\\Profstroy4\\IBASE.FDB?encoding=win1251", "sysdba", "masterkey");
            //"jdbc:firebirdsql:localhost/3055:D:\\Okna\\Database\\Sialbase2\\base2.GDB?encoding=win1251", "sysdba", "masterkey");
            //Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //ResultSet recordset = statement.executeQuery("select first 1 * from ARTDET where artikl_id = 693");
            //Query qArtdet = new Query(eArtdet.values()).select(eArtdet.up);
//            int count = 3;
//            System.out.println(count);
//            int dx = temp(count);
//            System.out.println(dx);
        } catch (Exception e) {
            System.out.println("main.Test.query()");
        }
    }

    static void parse() {

        HashMap<Integer, Record> mapParamUse = new HashMap();
        String paramJson = "{'typeOpen':1,'nuni':23, 'ioknaParam': [[-862107,826],[-862106,830]]}";
        String str = paramJson.replace("'", "\"");

        JsonObject jsonObj = new Gson().fromJson(str, JsonObject.class);
        JsonArray jsonArr = jsonObj.getAsJsonArray(ParamJson.ioknaParam.name());
        if (!jsonArr.isJsonNull() && jsonArr.isJsonArray()) {
            jsonArr.forEach(it -> {
                Record paramRec = eParams.find(it.getAsJsonArray().get(0).getAsInt(), it.getAsJsonArray().get(1).getAsInt());
                mapParamUse.put(paramRec.getInt(eParams.grup), paramRec);
            });
            System.out.println(mapParamUse);

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
        }
    }

    static void classToJson() {

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
            AppRus.runRussifier();
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

    private static void test() {
        //Integer arr[] = estimate.constr.Util.parserInt("33;10-99;44;17-21;23-28");
        boolean obj = estimate.constr.Util.containsStr("Стойка 100;Стойка 200;/sel", "Стойка 200", "sel");
//        String[] obj = "55;".split(";");
//        boolean list = estimate.constr.Util.containsFloat("90,01-150;180,01-269,99;270,01-359,99/0-89,99;90,01-150;180,01-269,99;270,01-359,99", 0, 100);
//        List list = Arrays.asList(arr);
        System.out.println(obj);

    }
}
