package startup;

import common.*;
import dataset.*;
import enums.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import domain.eParams;
import java.sql.SQLException;
import java.util.HashMap;
import javax.swing.UIManager;
import java.util.Arrays;
import java.util.List;

public class Test {

    public static void main(String[] args) { //java -jar C:\\Okna\\winapp\\dist\\winapp.jar dev loc
        Main.dev = true;
        try {
            //convert.Profstroy.script();
            wincalc();
            //query();            
            //frame();
            //parseJson();
            //test();
        } catch (Exception e) {
            System.err.println("TEST-MAIN: " + e);
        }
    }

    static void wincalc() throws Exception {

        Query.connection = java.sql.DriverManager.getConnection(
                "jdbc:firebirdsql:localhost/3050:C:\\Okna\\fbase\\BASE.FDB?encoding=win1251", "sysdba", "masterkey");
        estimate.Wincalc iwin = new estimate.Wincalc();
        System.out.println("okno=" + estimate.Wincalc.prj);
        //int nuni = Integer.valueOf(eProperty.systree_nuni.read());
        iwin.build(estimate.script.Winscript.test(estimate.Wincalc.prj, null));
        iwin.constructiv();
//        iwin.bufferImg = new BufferedImage((int) (iwin.width + 260), (int) (iwin.heightAdd + 260), BufferedImage.TYPE_INT_RGB);
//        iwin.graphics2D = (Graphics2D) iwin.bufferImg.getGraphics();
//        iwin.rootArea.drawWin(iwin.bufferImg.getWidth(), iwin.bufferImg.getHeight());           
    }

    static void frame() throws Exception {

        Query.connection = java.sql.DriverManager.getConnection(
                "jdbc:firebirdsql:localhost/3050:C:\\Okna\\fbase\\BASE.FDB?encoding=win1251", "sysdba", "masterkey");
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
            Query.connection = java.sql.DriverManager.getConnection(
                    "jdbc:firebirdsql:localhost/3050:C:\\Okna\\fbase\\BASE.FDB?encoding=win1251", "sysdba", "masterkey");
            //"jdbc:firebirdsql:localhost/3050:D:\\Okna\\Database\\Profstroy4\\IBASE.FDB?encoding=win1251", "sysdba", "masterkey");
            //"jdbc:firebirdsql:localhost/3055:D:\\Okna\\Database\\Sialbase2\\base2.GDB?encoding=win1251", "sysdba", "masterkey");
            //Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //ResultSet recordset = statement.executeQuery("select first 1 * from ARTDET where artikl_id = 693");
            //Query qArtdet = new Query(eArtdet.values()).select(eArtdet.up);

//            int count = 3;
//            System.out.println(count);
//            int dx = temp(count);
//            System.out.println(dx);
        } catch (SQLException e) {
            System.out.println("main.Test.query()");
        }
    }

    static int temp(int count) {
        return ++count;
    }

    static void parseJson() {

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
//0:вручную        
//11:по профилю   
//15:по заполнению
//1:по основе изделия, 2:по внутр.изделия, 3:по внешн.изделия
//6:по основе в серии, 7:по внутр. в серии, 8:по внешн. в серии

//3003 - по профилю
//799 - по заполнению (зависимая?), 4095 - по заполнению
//273 - на основе изделия, 801 - по основе изделия, 546 - по внутр. изделия, 819 - по внешн. изделия
//1638 - по основе в серии , 1911 - по внутр. в серии, 2184 - по внешн. в серии
//3145 - по параметру (основа), 1092 - по параметру (внутр.), 3276 - по параметру (внешн.)
        int CTYPE[] = {0, 32, 48, 256, 273, 512, 529, 545, 546, 560, 561, 768, 785,
            799, 800, 801, 811, 816, 817, 819, 1092, 1097, 2457, 2992,
            2993, 3003, 3145, 3147, 3273, 3275, 3276, 4095};

        for (int i = 0; i < CTYPE.length; i++) {
            int j = CTYPE[i];

            System.out.println(" " + j);
            System.out.println("1ур " + j % 16 + " - " + (j & 0x000f));
            System.out.println("2ур " + (j / 16) % 16 + " - " + (j & 0x00f0 >> 4));
            System.out.println("3ур " + (j / (16 * 16)) % 16 + " - " + (j & 0x0f00 >> 8));
            System.out.println("============================");

        }
    }
}
