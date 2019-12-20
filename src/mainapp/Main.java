package mainapp;

import common.eProfile;
import common.eProp;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static boolean dev = false;
    //Конструктор
    public Main() {
        LogoToDb.logoToDb();
        eProp.save();
    }

    public static void main(String[] args) {

        for (int index = 0; index < args.length; index++) {
            if (index == 0 && args[0].equals("dev")) {
                //режим разработки и тестирования
                Main.dev = true;
            }
        }
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                //"Windows Classic", "Windows", "CDE/Motif", "Metal", "Nimbus"
                String lafName = "Windows"; //eProp.lookandfeel.read();
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
                        if (lafName.equals(laf.getName())) {
                            UIManager.setLookAndFeel(laf.getClassName());
                        }
                    }
                } catch (Exception e) {
                    System.err.println(e);
                }
                new Main();
                //закрываю и сохраняю все изменения в базе данных
                Thread thread = new Thread() {

                    public void run() {
                        try {
                            //Query.autoexecSql();

                        } catch (Exception e) {
                            System.err.println(e);
                        }
                    }
                };
                Runtime.getRuntime().addShutdownHook(thread);
            }
        });
    }
}
