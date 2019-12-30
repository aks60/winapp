package main;

import convdb.Script;
import dataset.Entity;
import dataset.Query;
import dataset.Table;
import domain.eArtikls;
import frames.Artikls;
import frames.Rate;
import frames.Color;

public class Test {

    public static void main(String[] args) {
        Main.dev = true;

        try {

            Script.script();
            //query();

        } catch (Exception e) {
            System.err.println("TEST-MAIN: " + e);
        }
    }

    private static void frames() {
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

    private static void query() {
        try {
            Query.connection = java.sql.DriverManager.getConnection(
                    //"jdbc:firebirdsql:localhost/3050:C:\\Okna\\winbase\\BASE.FDB?encoding=win1251", "sysdba", "masterkey");
                    "jdbc:firebirdsql:localhost/3050:D:\\Okna\\Database\\Profstroy4\\IBASE.FDB?encoding=win1251", "sysdba", "masterkey");
            Entity.firebird(Query.connection, "KOMPSPC");

            //Query q1 = new Query(eArtikls.values()).select("$t where $f = $v order by $f", eArtikls.up, eArtikls.id, eArtikls.code, 2);
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
}
