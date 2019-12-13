package winapp;

import winapp.App1;
import common.FrameAdapter;
import common.FrameToFile;
import common.eProfile;
import common.eProp;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * <p>
 * Модуль главного меню </p>
 */
public class mMainapp extends FrameAdapter {

    public static Dimension screenSize;
    public static Dimension frameSize;
    public static java.awt.Frame frame;

    public static void logoToDb() {
        LogoToDb frame = new LogoToDb(null);
        FrameToFile.setFrameSize(frame);
        frame.setVisible(true);
    }

    public static void pathToDb(java.awt.Window owner) {
        PathToDb  pathToDb = new PathToDb(owner);
        //window.setName(this.name());
        FrameToFile.setFrameSize(pathToDb);
        pathToDb.setVisible(true);
    }

    public static <T> T createApp(eProfile profile) {
        
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if (profile.equals(eProfile.P16)) {
            eProp.open_dict = false;
            eProfile.role_user = "user";
            frame = new App1();
            frame.setName(eProfile.P16.name());
        }
        frame.setLocation(0, 0);
        frame.setSize(screenSize.width, frame.getHeight());
        frame.setVisible(true);
        return (T) frame;
    }
}
