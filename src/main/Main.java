package main;

import common.FrameListener;
import common.FrameProgress;
import common.eProperty;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class Main {

    public static boolean dev = false;
    public static boolean locate = false;

    //Конструктор
    public Main() {
        LogoToDb.logoToDb();
        eProperty.save();
    }

    public static void main(String[] args) {

        for (int index = 0; index < args.length; index++) {
            if (index == 0 && args[0].equals("dev")) {
                Main.dev = true; //режим разработки и тестирования
            }
            if(index == 1 && args[1].equals("loc")) {
               Main.locate = true;
            }
        }
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                try {
                    AppRus.runRussifier();
                    String lafName = eProperty.lookandfeel.read();
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    for (LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
                        if (lafName.equals(laf.getName())) { //"Windows Classic", "Windows", "CDE/Motif", "Metal", "Nimbus"
                            UIManager.setLookAndFeel(laf.getClassName());
                        }
                    }
                } catch (Exception e) {
                    System.err.println(e);
                }
                new Main();
                //закрываю и сохраняю проперти
                Thread thread = new Thread() {

                    public void run() {
                        try {
                            eProperty.save();
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                };
                Runtime.getRuntime().addShutdownHook(thread);
            }
        });
    }
}
