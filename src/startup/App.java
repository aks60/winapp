package startup;

import builder.Wincalc;
import common.FrameToFile;
import common.eProfile;
import dataset.Field;
import dataset.Query;
import dataset.Record;
import domain.eArtdet;
import domain.eArtikl;
import domain.eColmap;
import domain.eColor;
import domain.eCurrenc;
import domain.eElemdet;
import domain.eElement;
import domain.eElempar1;
import domain.eElempar2;
import domain.eFurndet;
import domain.eFurniture;
import domain.eFurnpar1;
import domain.eFurnpar2;
import domain.eFurnside1;
import domain.eFurnside2;
import domain.eGlasdet;
import domain.eGlasgrp;
import domain.eGlaspar1;
import domain.eGlaspar2;
import domain.eGlasprof;
import domain.eGroups;
import domain.eJoindet;
import domain.eJoining;
import domain.eJoinpar1;
import domain.eJoinpar2;
import domain.eJoinvar;
import domain.eKitdet;
import domain.eKitpar1;
import domain.eKits;
import domain.eParams;
import domain.ePrjpart;
import domain.ePrjprod;
import domain.eProject;
import domain.eRulecalc;
import domain.eSetting;
import domain.eSysfurn;
import domain.eSysmodel;
import domain.eSyspar1;
import domain.eSysprod;
import domain.eSysprof;
import domain.eSyssize;
import domain.eSystree;
import frames.AboutBox;
import frames.Artikles;
import frames.Color;
import frames.Currenc;
import frames.DBCompare;
import frames.Element;
import frames.Filling;
import frames.Furniture;
import frames.Groups;
import frames.Joining;
import frames.Kits;
import frames.Models;
import frames.Order;
import frames.Param;
import frames.Partner;
import frames.Rulecalc;
import frames.Specific;
import frames.Syssize;
import frames.Systree;
import frames.TestFrame;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.util.Set;
import javax.swing.JFrame;

public enum App {

    Top, Groups, Currenc, Color, Artikles, Joining, Element, Param,
    Filling, Furniture, Kits, Systree, Partner, Order, AboutBox, Models,
    Specific, Syssize, TestFrame, RuleCalc, DBCompare;
    public javax.swing.JFrame frame;

    public void createFrame(java.awt.Window parent, Object... param) {
        if (frame != null) {
            frame.dispose();
        }
        try {
            switch (this) {

                case TestFrame:
                    frame = new TestFrame();
                    break;
                case RuleCalc:
                    frame = new Rulecalc();
                    break;
                case AboutBox:
                    frame = new AboutBox();
                    break;
                case Artikles:
                    if (param.length == 1) {
                        frame = new Artikles(parent, (Record) param[0]);
                    } else {
                        frame = new Artikles();
                    }
                    break;
                case Groups:
                    frame = new Groups(param);
                    break;
                case Color:
                    frame = new Color();
                    break;
                case Joining:
                    if (param.length == 0) {
                        frame = new Joining();
                    } else if (param.length == 1) {
                        frame = new Joining((Set) param[0]);
                    } else {
                        frame = new Joining((Set) param[0], (int) param[1]);
                    }
                    break;
                case Currenc:
                    frame = new Currenc();
                    break;
                case Element:
                    if (param.length == 0) {
                        frame = new Element();
                    } else if (param.length == 1) {
                        frame = new Element((Set) param[0]);
                    } else {
                        frame = new Element((Set) param[0], (int) param[1]);
                    }
                    break;
                case Param:
                    frame = new Param();
                    break;
                case Filling:
                    if (param.length == 0) {
                        frame = new Filling();
                    } else if (param.length == 1) {
                        frame = new Filling((Set) param[0]);
                    } else {
                        frame = new Filling((Set) param[0], (int) param[1]);
                    }
                    break;
                case Furniture:
                    if (param.length == 0) {
                        frame = new Furniture();
                    } else if (param.length == 1) {
                        frame = new Furniture((Set) param[0]);
                    } else {
                        frame = new Furniture((Set) param[0], (int) param[1]);
                    }
                    break;
                case Kits:
                    frame = new Kits();
                    break;
                case Systree:
                    if (param.length == 0) {
                        frame = new Systree();
                    } else {
                        frame = new Systree((int) param[0]);
                    }
                    break;
                case Partner:
                    frame = new Partner();
                    break;
                case Order:
                    frame = new Order();
                    break;
                case Models:
                    frame = new Models();
                    break;
                case Specific:
                    frame = new Specific();
                    break;
                case Syssize:
                    frame = new Syssize();
                    break;
                case DBCompare:
                    if (param.length == 0) {
                        frame = new DBCompare();
                    } else {
                        frame = new DBCompare((Wincalc) param[0]);
                    }
                    break;
            }
        } catch (Exception e) {
            System.err.println("startup.App.createFrame() " + e);
        }
        //eProfile.appframe = frame;
        frame.setName(this.name());
        FrameToFile.setFrameSize(frame); //размеры окна
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowDeiconified(java.awt.event.WindowEvent evt) {
                Top.frame.setExtendedState(JFrame.NORMAL);
            }
        });
        frame.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage());
        frame.setVisible(true);
    }

    public static void createApp(eProfile profile) {

        try {
            ResultSet rs = Query.connection.createStatement().executeQuery("select current_user from rdb$database");
            rs.next();
            eProfile.user = rs.getString(1);
            eProfile.profile = profile; //профиль приложения
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

            if (profile.equals(eProfile.P01)) {
                Top.frame = new Adm();

            } else if (profile.equals(eProfile.P02)) {
                Top.frame = new Tex();

            } else {
                Top.frame = new Man();
            }
            Top.frame.setName(profile.name());
            if (profile.equals(eProfile.P01)) {
                FrameToFile.setFrameSize(Top.frame); //размеры окна
            } else {
                Top.frame.setLocation(0, 0);
                Top.frame.setSize(screenSize.width, Top.frame.getHeight()); //размеры гл. окна
            }

            Top.frame.setVisible(true);
            //Top.frame.setExtendedState(JFrame.ICONIFIED);
            //Top.frame.setState(Frame.ICONIFIED);

        } catch (Exception e) {
            System.out.println("Ошибка: App.createApp()");
        }
    }

    //Список таблиц базы данных
    public static Field[] db = { //в порядке удаления при конвертирования из базы приёмника
        eSetting.up, eSyspar1.up, eSysprof.up, eSysfurn.up, eSysprod.up, eSysmodel.up,
        eKitpar1.up, eKitdet.up, eKits.up,
        eJoinpar2.up, eJoinpar1.up, eJoindet.up, eJoinvar.up, eJoining.up,
        eElempar1.up, eElempar2.up, eElemdet.up, eElement.up,
        eGlaspar1.up, eGlaspar2.up, eGlasdet.up, eGlasprof.up, eGlasgrp.up,
        eFurnpar1.up, eFurnpar2.up, eFurnside1.up, eFurnside2.up, eFurndet.up, eFurniture.up,
        eColmap.up, eColor.up,
        ePrjprod.up, eProject.up, ePrjpart.up,
        eRulecalc.up, eSystree.up,
        eArtdet.up, eArtikl.up,
        eSyssize.up, eGroups.up, eCurrenc.up, eParams.up,};
}
