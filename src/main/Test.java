package main;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import common.FrameToFile;
import dataset.Field;
import dataset.Query;
import domain.*;
import enums.ParamJson;
import forms.BoxTypical;
import java.sql.SQLException;
import java.util.HashMap;
import wincalc.script.Winscript;

public class Test {

    public static void main(String[] args) {
        Main.dev = true;

        try {
            //convert.Profstroy.script();
            query();
            //wincalc();
            //frame();

        } catch (Exception e) {
            System.err.println("TEST-MAIN: " + e);
        }
    }

    static void wincalc() throws Exception {

        Query.connection = java.sql.DriverManager.getConnection(
                "jdbc:firebirdsql:localhost/3050:C:\\Okna\\winbase\\BASE.FDB?encoding=win1251", "sysdba", "masterkey");
        wincalc.Wincalc iwin = new wincalc.Wincalc();
        iwin.create(wincalc.script.Winscript.test(Winscript.prj, null));
//        iwin.constructiv();
//        iwin.bufferImg = new BufferedImage((int) (iwin.width + 260), (int) (iwin.heightAdd + 260), BufferedImage.TYPE_INT_RGB);
//        iwin.graphics2D = (Graphics2D) iwin.bufferImg.getGraphics();
//        iwin.rootArea.drawWin(iwin.bufferImg.getWidth(), iwin.bufferImg.getHeight());           
    }

    static void frame() throws Exception {
        Query.connection = java.sql.DriverManager.getConnection(
                "jdbc:firebirdsql:localhost/3050:C:\\Okna\\winbase\\BASE.FDB?encoding=win1251", "sysdba", "masterkey");
        App1 app = new App1();
        app.setVisible(true);
        BoxTypical frm = new BoxTypical();
        FrameToFile.setFrameSize(frm);
        //frm.iwin.create(Winscript.test(Winscript.prj, null));
        frm.setVisible(true);
    }

    static void query() {
        try {
            Query.connection = java.sql.DriverManager.getConnection(
                    "jdbc:firebirdsql:localhost/3050:C:\\Okna\\winbase\\BASE.FDB?encoding=win1251", "sysdba", "masterkey");

            //"jdbc:firebirdsql:localhost/3050:D:\\Okna\\Database\\Profstroy4\\IBASE.FDB?encoding=win1251", "sysdba", "masterkey");
            //"jdbc:firebirdsql:localhost/3055:D:\\Okna\\Database\\Sialbase2\\base2.GDB?encoding=win1251", "sysdba", "masterkey");
            //Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //ResultSet recordset = statement.executeQuery("select first 1 * from ARTDET where artikl_id = 693");
            //int _nuni, TypeProfile _type, ProfileSide _side
            
            for (Field field : new Field[] {eSyscons.up, eArtikl.up, eSystree.up, eSysprof.up}) {
                field.virtualRec();
            }

//        Query q2 = q.select(eSysprof.up, "left join", eSystree.up, "on", eSysprof.systree_id, "=", eSystree.id);
//        
//        Query q3 = q2.table(eSysprof.up.tname());
//        Query q4 = q2.table(eSystree.up.tname());
//        
//        int id = q2.getAs(1, eSystree.id, -1);
//        int id2 = q2.getAs(1, eSysprof.id, -1);

        } catch (SQLException e) {
            System.out.println("main.Test.query()");
        }
    }

    static void parseJson() {

        HashMap<ParamJson, Object> mapParam = new HashMap();
        String paramJson = "{'typeOpen':1,'funic':23, 'pro4Params': [[-862107,826],[-862106,830]]}";
        Gson gson = new Gson();
        String str = paramJson.replace("'", "\"");

        JsonElement jsonElem = gson.fromJson(str, JsonElement.class);
        JsonObject jsonObj = jsonElem.getAsJsonObject();
        JsonArray jsonArr = jsonObj.getAsJsonArray(ParamJson.pro4Params.name());

        if (!jsonArr.isJsonNull() && jsonArr.isJsonArray()) {
            mapParam.put(ParamJson.pro4Params, jsonObj.get(ParamJson.pro4Params.name()));
            HashMap<Integer, Object[]> hmValue = new HashMap();
            for (int index = 0; index < jsonArr.size(); index++) {
                JsonArray jsonRec = (JsonArray) jsonArr.get(index);
                int pnumb = jsonRec.getAsInt();
//                        Parlist rec = Parlist.get(root.getConst(), jsonRec.get(0), jsonRec.get(1));
//                        if (pnumb < 0 && rec != null)
//                            hmValue.put(pnumb, new Object[]{rec.pname, rec.znumb, 0});
                int mm = 0;
            }
            mapParam.put(ParamJson.pro4Params2, hmValue); //второй вариант                
        }
    }

    static void classToJson() {

//        AreaRoot rootArea = new AreaRoot("1", LayoutArea.VERT, TypeElem.SQUARE, 900, 1300, 1300, 1009, 10009, 1009, "");
//        rootArea.setParam("1", 8);
//        rootArea.add(new Element("2", TypeElem.FRAME_BOX, LayoutArea.LEFT));
//        rootArea.add(new Element("3", TypeElem.FRAME_BOX, LayoutArea.RIGHT));
//        rootArea.add(new Element("4", TypeElem.FRAME_BOX, LayoutArea.TOP));
//        rootArea.add(new Element("5", TypeElem.FRAME_BOX, LayoutArea.BOTTOM));
//        AreaElem area2 = (AreaElem) rootArea.add(new AreaElem("6", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':1, 'funic':23}"));
//        area2.add(new Element("7", TypeElem.GLASS));
    }
}
