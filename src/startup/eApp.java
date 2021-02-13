package startup;

import common.FrameToFile;
import common.eProfile;
import convert.Convert;
import dataset.Record;
import frames.AboutBox;
import frames.Artikles;
import frames.Color;
import frames.Currenc;
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
import java.awt.Toolkit;
import java.util.Set;
import javax.swing.JFrame;

    public enum eApp {

        App1, Convert, Groups, DicCurrenc, Color, Artikles, Joining, Element, Param,
        Filling, Furniture, Kits, Systree, Partner, Order, AboutBox, Models,
        Specific, DicSyssize, TestFrame, RuleCalc;
        public javax.swing.JFrame frame;

        public void createFrame(java.awt.Window parent, Object... param) {
            if (frame != null) {
                frame.dispose();
            }
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
                    frame = new Groups();
                    break;
                case Convert:
                    frame = new Convert();
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
                case DicCurrenc:
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
                case DicSyssize:
                    frame = new Syssize();
                    break;
            }
            eProfile.appframe = frame;
            frame.setName(this.name());
            FrameToFile.setFrameSize(frame);
            frame.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowDeiconified(java.awt.event.WindowEvent evt) {
                    App1.frame.setExtendedState(JFrame.NORMAL);
                }
            });
            frame.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage());
            frame.setVisible(true);
        }

        public static void createApp(eProfile profile) {

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            if (profile.equals(eProfile.P16)) {
                eProfile.role_user = "user";
                App1.frame = new App1();
                App1.frame.setName(eProfile.P16.name());
                eProfile.appframe = App1.frame;
            }
            App1.frame.setLocation(0, 0);
            App1.frame.setSize(screenSize.width, App1.frame.getHeight());
            App1.frame.setVisible(true);
        }
    }
