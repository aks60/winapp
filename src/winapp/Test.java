package winapp;

import winapp.App1;
import dataset.Query;
import domain.eArtikls;
import domain.eArtsvst;
import domain.eColslst;
import domain.eConnlst;
import domain.eConnspc;
import domain.eConnvar;
import domain.eCorrenc;
import domain.eFurnlen;
import domain.eFurnles;
import domain.eFurnspc;
import domain.eGlasart;
import domain.eGlasgrp;
import domain.eGlaspro;
import domain.eGrupart;
import domain.eGrupcol;
import domain.eKomplst;
import domain.eKompspc;
import domain.eParcols;
import domain.eParcons;
import domain.eParconv;
import domain.eParfurl;
import domain.eParfurs;
import domain.eParglas;
import domain.eParlist;
import domain.eParsysp;
import domain.eParvstm;
import domain.eParvsts;
import domain.eRuleclk;
import domain.eSpecpau;
import domain.eSysdata;
import domain.eSysproa;
import domain.eSyspros;
import domain.eSyssize;
import domain.eVstalst;
import domain.eVstaspc;
import frames.Artikls;
import frames.Rate;
import frames.Texture;

public class Test {

    public static void main(String[] args) {
        Main.dev = true;
        
        try {
            //////////7777+888
            Test test = new Test();
            test.convert();
            //test.query();

        } catch (Exception e) {
            System.err.println("TEST-MAIN: " + e);
        }
    }

    private void convert() {
        convdb.Script.script(
                eArtikls.up
                , eArtsvst.up, eColslst.up, eConnlst.up, eConnspc.up, eConnvar.up,
                eCorrenc.up, eFurnlen.up, eFurnles.up, eFurnspc.up, eGlasart.up, eGlasgrp.up, eGlaspro.up,
                eGrupart.up, eGrupcol.up, eKomplst.up, eKompspc.up, eParcols.up, eParcons.up, eParconv.up,
                eParfurl.up, eParfurs.up, eParglas.up, eParlist.up, eParsysp.up, eParvstm.up, eParvsts.up,
                eRuleclk.up, eSysdata.up, eVstalst.up, eVstaspc.up, eSyssize.up, eSyspros.up, eSysproa.up, 
                eSpecpau.up
        );
    }

    private void frames() {
        try {
            Query.connection = java.sql.DriverManager.getConnection(
                    "jdbc:firebirdsql:localhost/3050:C:\\Okna\\winbase\\BASE.FDB?encoding=win1251", "sysdba", "masterkey");
            App1 app = new App1();
            Artikls art = new Artikls();
            art.treeMat.setSelectionRow(2);
            art.setVisible(true);
            new Rate().setVisible(true);
            new Texture().setVisible(true);
            app.setVisible(true);

        } catch (Exception e) {
            System.err.println("TEST-FRAMES: " + e);
        }
    }
   
    private void query() {
        try {
            Query.connection = java.sql.DriverManager.getConnection(
                   "jdbc:firebirdsql:localhost/3055:D:\\Okna\\Database\\Sialbase2\\base2.GDB?encoding=win1251", "sysdba", "masterkey");
            //Query qCorrenc = new Query(eCorrenc.values()).select("correnc order by correnc.cname");
            //Query qGrupcol = new Query(eGrupcol.id, eGrupcol.gnumb, eGrupcol.gunic, eGrupcol.gname, eGrupcol.gkoef).select("grupcol order by grupcol.gname");
            Query qColslst = new Query(eColslst.values());
            int cgrup = 1;
            qColslst.select("colslst where colslst.cgrup = " + cgrup + " order by colslst.cname");
            
        } catch (Exception e) {
            System.err.println("TEST-CONNECTION: " + e);
        }
    }    
}
