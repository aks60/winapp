package common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import javax.swing.JTextField;
import main.Main;

/**
 * <p>
 * Параметры программы </p>
 */
public enum eProp {

    lookandfeel("Metal", "Windows"),
    web_port("8080"),
    web_start("false"),
    typedb("fb"),
    port("3050"),
    user("sysdba"),
    server("localhost"),
    base("C:\\Okna\\winbase\\BASE.FDB?encoding=win1251", "C:\\Okna\\winbase\\BASE.FDB?encoding=win1251"),
    //base("D:\\Okna\\Database\\Sialbase2\\base2.gdb?encoding=win1251", "D:\\Okna\\Database\\Sialbase2\\base2.gdb?encoding=win1251"),
    path_app(System.getProperty("user.home") + "/Acron/Okno", "C:\\Users\\aksenov\\Desktop\\winapp.jar"),
    path_prop(System.getProperty("user.home") + "/Acron/Okno", "C:\\Documents and Settings\\All Users\\Application Data\\Acron\\Okno"),
    path_bekap(System.getProperty("user.home") + "/Acron/Backup", "C:\\Acron\\Backup"),
    url_src("http://aks.acron.ru:8080"),   
    sys_nuni("-1"),
    cmd_word("libreoffice -writer ", "cmd /c start winword.exe "), 
    cmd_excel("libreoffice -calc ", "cmd /c start excel.exe "),
    cmd_html("firefox ", "cmd /c start iexplore.exe "),
    fontname("Dialog"),    
    fontsize("11");
    private static Properties prop = null;
    //это значение по умолчанию
    private String value;
    public static String password = "******";
    public static FileWriter logconv = null;
    public static String fb = "fb";
    public static String pg = "pg";

    public static boolean open_dict = true;

    //Значение по умолчанию
    eProp(String value) {
        this.value = value;
    }

    //Значение по умолчанию для конкретной OS
    eProp(String value1, String value2) {
        String os = System.getProperty("os.name");
        this.value = os.equals("Linux") ? value1 : value2;
    }

    //Возвращает конкретное значение от выбранного экземпляра enum
    public String read() {
        load();
        String prop2 = prop.getProperty(this.name());
        //если свойство не записано локально
        if (prop2 != null && prop.getProperty(this.name()).equals("") || Main.dev == true) {
            return this.value;
        } else {
            //иначе читаем с диска
            return prop.getProperty(this.name(), this.value);
        }
    }

    //Запись str в Property
    public void write(String str) {
        load();
        if (this.name().equals("user")) {
            str = str.toLowerCase();
        }
        prop.setProperty(this.name(), str.trim());
    }

    //Чтение property из файла
    public static Properties load() {
        if (prop == null) {
            prop = new Properties();
            try {
                File file = new File(System.getProperty("user.dir"), eProfile.filename);
                if (file.exists() == true) {
                    //сохраним путь к файлу в path_prop
                    path_prop.value = System.getProperty("user.dir");
                } else {
                    //если файла нет создадим его
                    file = new File(path_prop.value, eProfile.filename);
                }
                if (file.exists() == false) {
                    //если файл создать так и не удалось
                    File mydir = new File(path_prop.value);
                    mydir.mkdirs();
                    file.createNewFile();
                } else {
                    //теперь можно грузить файл
                    FileInputStream inStream = new FileInputStream(file);
                    prop.load(inStream);
                    inStream.close();
                }
            } catch (FileNotFoundException e) {
                System.err.println("Нет такой директории " + e);
            } catch (IOException e) {
                System.err.println("Ошибка создания файла property " + e);
            }
        }
        return prop;
    }

    //Сохранение property в файл
    public static void save() {
        try {
            File file = new File(path_prop.value, eProfile.filename);
            FileOutputStream outStream = new FileOutputStream(file);
            prop.store(outStream, "");
            outStream.close();
        } catch (IOException e) {
            System.err.println("Ошибка сохранения property в файле " + e);
        }
    }

    public static void logindef(boolean adm, JTextField... val) {
        if (typedb.read().equals(fb)) {
            if (adm == true) {
                val[0].setText("sysdba"); //user
                val[1].setText("masterkey"); //pass
            } else {
                val[0].setText("sysdba"); //user
                val[1].setText("masterkey"); //pass
            }

        } else if (typedb.read().equals(pg)) {
            if (adm == true) {
                val[0].setText("admin"); //user
                val[1].setText("Platina6"); //pass                
            } else if (val[0].getText().equals("postgres")) {
                val[0].setText("user99a"); //user
                val[1].setText("Platina6"); //pass
            }
        }
    }
}
