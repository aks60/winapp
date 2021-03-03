package common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import javax.swing.JTextField;
import startup.Main;

/**
 * 
 * Параметры программы 
 */
public enum eProperty {

    lookandfeel("Metal", "Windows"),
    url_src("http://aks.acron.ru:8080"),
    web_port("8080"),
    web_start("false"),
    typedb("fb"),
    port("3050"),
    user("sysdba"),
    server1("localhost"),
    server2("localhost"),
    server3("localhost"),
    sysprodID("-1"),
    ordprodID("-1"),
    orderID("-1"),
    base_num("1"),
    base1("C:\\Okna\\fbase\\BASE.FDB?encoding=win1251", "C:\\Okna\\fbase\\BASE.FDB?encoding=win1251"),
    base2("C:\\Okna\\fbase\\BASE.FDB?encoding=win1251", "C:\\Okna\\fbase\\BASE2.FDB?encoding=win1251"),
    base3("C:\\Okna\\fbase\\BASE.FDB?encoding=win1251", "C:\\Okna\\fbase\\BASE3.FDB?encoding=win1251"),
    path_app(System.getProperty("user.home") + "/Acron/Okno", "C:\\Users\\aksenov\\Desktop\\winapp.jar"),
    path_prop(System.getProperty("user.home") + "/Acron/Okno", "C:\\Documents and Settings\\All Users\\Application Data\\Acron\\Okno"),     
    cmd_word("libreoffice -writer ", "cmd /c start winword.exe "), 
    cmd_excel("libreoffice -calc ", "cmd /c start excel.exe "),
    cmd_html("firefox ", "cmd /c start iexplore.exe "),
    fontname("Dialog"),    
    fontsize("11");
    private static Properties prop = null;
    public final static String filename = "v9.properties"; //имя файла properties
    
    //Значения по умолчанию
    private String value;
    
    public static String password = "******";
    public static FileWriter logconv = null;
    public static String fb = "fb";
    public static String pg = "pg";
    
    //Значение по умолчанию
    eProperty(String value) {
        this.value = value;
    }

    //Значение по умолчанию для конкретной OS
    eProperty(String value1, String value2) {
        String os = System.getProperty("os.name");
        this.value = os.equals("Linux") ? value1 : value2;
    }

    //Возвращает конкретное значение от выбранного экземпляра enum
    public String read() {
        load();
        if (prop.getProperty(this.name()) != null && prop.getProperty(this.name()).equals("")) { //свойство не записано           
            return this.value; //по умолчанию
        } else {            
            return prop.getProperty(this.name(), this.value); //читаем с диска
        }
    }

    //Запись str в Property
    public void write(String str) {
        load();
        prop.setProperty(this.name(), str.trim());
    }

    //Чтение property из файла
    public static Properties load() {
        if (prop == null) {
            prop = new Properties();
            try {
                File file = new File(System.getProperty("user.dir"), filename);
                if (file.exists() == true) {
                    
                    path_prop.value = System.getProperty("user.dir"); //сохраним путь к файлу в path_prop
                } else {                    
                    file = new File(path_prop.value, filename); //если файла нет, создадим его
                }
                if (file.exists() == false) {                   
                    File mydir = new File(path_prop.value); //если файл создать так и не удалось
                    mydir.mkdirs();
                    file.createNewFile();
                    
                } else {                    
                    FileInputStream inStream = new FileInputStream(file); //теперь можно грузить файл
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
            File file = new File(path_prop.value, filename);
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
