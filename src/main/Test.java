package main;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dataset.Query;
import enums.LayoutArea;
import enums.ParamJson;
import enums.TypeElem;
import forms.Artikls;
import forms.Rate;
import forms.Color;
import java.util.HashMap;
import wincalc.model.PaintPanel;
import wincalc.script.AreaElem;
import wincalc.script.AreaRoot;
import wincalc.script.Element;

public class Test {

    public static void main(String[] args) {
        Main.dev = true;

        try {   
            //convert.Profstroy.script();
            //query();
        } catch (Exception e) {
            System.err.println("TEST-MAIN: " + e);
        }
    }

    static void classToJson() {

        AreaRoot rootArea = new AreaRoot("1", LayoutArea.VERT, TypeElem.SQUARE, 900, 1300, 1300, 1009, 10009, 1009, "");
        rootArea.setParam(8, "1");
        rootArea.add(new Element("2", TypeElem.FRAME_BOX, LayoutArea.LEFT));
        rootArea.add(new Element("3", TypeElem.FRAME_BOX, LayoutArea.RIGHT));
        rootArea.add(new Element("4", TypeElem.FRAME_BOX, LayoutArea.TOP));
        rootArea.add(new Element("5", TypeElem.FRAME_BOX, LayoutArea.BOTTOM));
        AreaElem area2 = (AreaElem) rootArea.add(new AreaElem("6", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':1, 'funic':23}"));
        area2.add(new Element("7", TypeElem.GLASS));

    }
    
    static void frame() {
        try {
            Query.connection = java.sql.DriverManager.getConnection(
                    "jdbc:firebirdsql:localhost/3050:C:\\Okna\\winbase\\BASE.FDB?encoding=win1251", "sysdba", "masterkey");
            App1 app = new App1();
            Artikls art = new Artikls();
            art.tree.setSelectionRow(2);
            art.setVisible(true);
            new Rate().setVisible(true);
            new Color().setVisible(true);
            app.setVisible(true);

        } catch (Exception e) {
            System.err.println("TEST-FRAMES: " + e);
        }
    }

    static void query() {
        try {
            Query.connection = java.sql.DriverManager.getConnection(
                    "jdbc:firebirdsql:localhost/3050:C:\\Okna\\winbase\\BASE.FDB?encoding=win1251", "sysdba", "masterkey");
            
            wincalc.Wincalc wc = new wincalc.Wincalc();
            wc.create(wincalc.script.Winscript.test(601002));
            
            //"jdbc:firebirdsql:localhost/3050:D:\\Okna\\Database\\Profstroy4\\IBASE.FDB?encoding=win1251", "sysdba", "masterkey");
            //"jdbc:firebirdsql:localhost/3055:D:\\Okna\\Database\\Sialbase2\\base2.GDB?encoding=win1251", "sysdba", "masterkey");
            //Entity.firebird(Query.connection, "LIS_ORD");
            //Query sysprofRec = eSysprof.query.select(eSysprof.up, "where", eSysprof.systree_id, "=", 22, "and", eSysprof.types, "=", 44).table(eSysprof.up.tname());
            //Quaery record = eSysprof.query.select().stream().filter(record -> record.get(eSysprof.id) != null);

            //Record recs = eSysprof.query.newRecord(Query.SEL);
            //Record record = eSysprof.query.select().stream()
            //        .filter(rec -> rec.getInt(eSysprof.systree_id) == 39 && rec.getInt(eSysprof.types) == 2).findFirst().orElse(null);
            //Query q1 = new Query(eArtikls.values()).table(eArtikls.up.tname());
            //Query q1 = eArtikls.query.select(eArtikls.up, "order by", eArtikls.id);
            //int id = q1.getInt(0, eArtikls.id);
            //q1.select(eArtikls.up, "where", eArtikls.id, "= 2");
            //int i2 = q1.getInt(0, eArtikls.id);
            //Query q2 = new Query(eArtikls.values()).select(eArtikls.up, "where", eArtikls.id, "=", 2, "order by", eArtikls.code);
            //Table yyy = q2.query(eArtikls.up.tname());
            //Query qGrupcol = new Query(eGrupcol.id, eGrupcol.gnumb, eGrupcol.gunic, eGrupcol.gname, eGrupcol.gkoef).select("grupcol order by grupcol.gname");
            //Query qColslst = new Query(eTexture.values());
            //int cgrup = 1;
            //qColslst.select("colslst where colslst.cgrup = " + cgrup + " order by colslst.cname");
        } catch (Exception e) {
            System.err.println("TEST-CONNECTION: " + e);
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
}
