package startup;

import common.*;
import dataset.*;
import builder.param.test.ElementTest;
import builder.param.test.FillingTest;
import builder.param.test.FurnitureTest;
import builder.param.test.JoiningTest;
import builder.script.GsonScript;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import domain.eElement;
import frames.DBCompare;
import frames.Profstroy;
import java.sql.Connection;
import javax.swing.UIManager;
import java.util.List;
import java.util.UUID;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class Test {

    public static Integer numDb = Integer.valueOf(eProp.base_num.read());

    // <editor-fold defaultstate="collapsed" desc="Connection[] connect(int numDb)">
    public static Connection connect1() {
        try {
            String db = (numDb == 1) ? eProp.base1.read() : (numDb == 2) ? eProp.base2.read() : eProp.base3.read();
            if (db.toUpperCase().contains("BIMAX")) {
                return java.sql.DriverManager.getConnection("jdbc:firebirdsql:localhost/3050:D:\\Okna\\Database\\ps4\\ITEST.FDB?encoding=win1251", "sysdba", "masterkey");
            } else if (db.toUpperCase().contains("SIAL3")) {
                return java.sql.DriverManager.getConnection("jdbc:firebirdsql:localhost/3055:D:\\Okna\\Database\\ps3\\sial3b.fdb?encoding=win1251", "sysdba", "masterkey");
            } else if (db.toUpperCase().contains("ALUTEX3")) {
                return java.sql.DriverManager.getConnection("jdbc:firebirdsql:localhost/3055:D:\\Okna\\Database\\ps3\\alutex3.fdb?encoding=win1251", "sysdba", "masterkey");
            } else if (db.toUpperCase().contains("ALUTECH3")) {
                return java.sql.DriverManager.getConnection("jdbc:firebirdsql:localhost/3055:D:\\Okna\\Database\\ps3\\alutech3.fdb?encoding=win1251", "sysdba", "masterkey");
            } else if (db.toUpperCase().contains("KRAUSS")) {
                return java.sql.DriverManager.getConnection("jdbc:firebirdsql:localhost/3050:D:\\Okna\\Database\\ps4\\krauss.fdb?encoding=win1251", "sysdba", "masterkey");
            } else if (db.toUpperCase().contains("VIDNAL")) {
                return java.sql.DriverManager.getConnection("jdbc:firebirdsql:localhost/3050:D:\\Okna\\Database\\ps4\\vidnal.fdb?encoding=win1251", "sysdba", "masterkey");
            } else if (db.toUpperCase().contains("SOKOL")) {
                return java.sql.DriverManager.getConnection("jdbc:firebirdsql:localhost/3050:D:\\Okna\\Database\\ps4\\sokol.fdb?encoding=win1251", "sysdba", "masterkey");
            }
            return null;
        } catch (Exception e) {
            System.err.println("Ошибка:Test.connect() " + e);
            return null;
        }
    }

    public static Connection connect2() {
        try {
            eProp.user.write("sysdba");
            eProp.password = String.valueOf("masterkey");
            Conn.connection(eProp.server(numDb.toString()), eProp.port(numDb.toString()), eProp.base(numDb.toString()), eProp.user.read(), eProp.password.toCharArray(), null);
            return Conn.connection();
        } catch (Exception e) {
            System.err.println("Ошибка:Test.connect() " + e);
            return null;
        }
    }

    // </editor-fold>     
    //
    public static void main(String[] args) throws Exception { //java -jar C:\\Okna\\winapp\\dist\\winapp.jar dev loc

        eProp.dev = true;
        try {
            Profstroy.exec();
            //wincalc();
            //param();
            //query();
            //frame();
            //json();
            //parse();
            //uid();
            //script(); 
//            String json = builder.script.WinScript.test(601006, true);
//            JsonParser parser = new JsonParser();
//            JsonElement node = parser.parse(json);
//            JsonObject obj = node.getAsJsonObject();
//            obj.addProperty("prjTest", "777");
//            JsonElement nameNode = obj.get("prjTest");
//            System.out.println("name: " + nameNode.getAsString());

        } catch (Exception e) {
            System.err.println("TEST-MAIN: " + e);
        }
    }

    private static void wincalc() throws Exception {

        Conn.connection(Test.connect2());
        builder.Wincalc winc = new builder.Wincalc();
        String _case = "min";

        if (_case.equals("one")) {
            winc.build(GsonScript.makeJson(601001));
            winc.constructiv(true);

//            winc.bufferImg = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
//            winc.gc2d = winc.bufferImg.createGraphics();
//            winc.rootArea.draw(); //рисую конструкцию
            //DBCompare.iwinXls(winc, true);
            DBCompare.iwinPs4(winc, true);
            //winc.mapJoin.entrySet().forEach(it -> System.out.println(it.getValue() + ", (" + it.getKey() + ")" + " " + it.getValue().elem1.type));           

        } else if (_case.equals("min")) {
            List<Integer> prjList = GsonScript.models(_case);
            for (int prj : prjList) {
                String script = GsonScript.makeJson(prj);
                if (script != null) {
                    winc.build(script);
                    winc.constructiv(true);
                    //DBCompare.iwinXls(winc, false);
                    DBCompare.iwinPs4(winc, false);
                }
            }

        } else if (_case.equals("max")) {
            List<Integer> prjList = GsonScript.models(_case);
            for (int prj : prjList) {
                String script = GsonScript.makeJson(prj);
                if (script != null) {
                    winc.build(script);
                    winc.constructiv(true);
                    //DBCompare.iwinXls(winc, false);
                    DBCompare.iwinPs4(winc, false);
                }
            }
        }
    }

    private static void param() {

        Conn.connection(Test.connect2());

        ElementTest et = new ElementTest();
        et.elementVar();
        et.elementDet();
        JoiningTest jt = new JoiningTest();
        jt.joiningVar();
        jt.joiningDet();
        FillingTest gt = new FillingTest();
        gt.fillingVar();
        gt.fillingDet();
        FurnitureTest ft = new FurnitureTest();
        ft.furnitureVar();
        ft.furnitureDet();

//        Query.connection = Test.connect2();
//        Set set = new HashSet();
//        Map<String, Set> map = new HashMap();
//        for (Enam en : ParamList.values()) {
//            Set set2 = map.getOrDefault(en.text(), new HashSet());
//            set2.add(en.numb());
//            map.put(en.text(), set2);
//        }
//        for (Map.Entry<String, Set> entry : map.entrySet()) {
//            String key = entry.getKey();
//            Set value = entry.getValue();
//            System.out.println(key + " " + value);
//        }
    }

    private static void frame() throws Exception {
        Main.main(new String[]{"tex"});
//        while (App.Top.frame == null) {
//            Thread.yield();
//        }
        Thread.sleep(1300);
        App.Order.createFrame(App.Top.frame);
    }

    private static void query() {
        try {
            Conn.connection(Test.connect2());
            Object obj = eElement.find3(1386, 33);
            System.out.println(obj);

        } catch (Exception e) {
            System.out.println("main.Test.query()");
        }
    }

    private static void parse() {

    }

    private static void json() {
        Gson gson = new Gson();
        JsonParser parse = new JsonParser();

        String str1 = "{\"developers\": [{ \"firstName\":777 , \"lastName\":\"Torvalds\" }, "
                + "{ \"firstName\":\"John\" , \"lastName\":\"von Neumann\"}]}";

        String str2 = "\"developers\": [ \"firstName\":\"Linus\" , \"lastName\":\"Torvalds\" }, "
                + "{ \"firstName\":\"John\" , \"lastName\":\"von Neumann\" } ]}";

        String str3 = null; //"{typeOpen:1, \"sysfurnID\":1634}";

        JsonObject obj = gson.fromJson(str3, JsonObject.class);
        Object out = obj.get("sysfurnID");
        System.out.println(obj);

        //boolean firstStringValid = JSONUtils.isJSONValid(str1); //true
        //boolean secondStringValid = JSONUtils.isJSONValid(str2); //false
        //Object obj = gson.fromJson(str1, Object.class);
        //System.out.println(new GsonBuilder().create().toJson(parse.parse(str1)));
//        JSONObject output = new JSONObj;
//        Object obj = gson.toJson(parse.parse(str3));
//        System.out.println(obj);    
        //System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(new com.google.gson.JsonParser().parse(str1)));
        //
        //String p = gson.fromJson(str3, String.class);
        //System.out.println(gson.fromJson(str3, String.class)); 
//        Conn.instanc().connection(Test.connect2());
//        builder.Wincalc winc = new builder.Wincalc();
//        String script = Winscript.test(601004, false);
//        winc.build(script);
//
//        GsonBuilder builder = new GsonBuilder();
//        //builder.registerTypeAdapter(Element.class, new GsonDeserializer<Element>());
//        //builder.setPrettyPrinting();
//        GsonRoot root = builder.create().fromJson(script, GsonRoot.class);
    }

    private static void lookAndFeel() {
        try {
            Main.runRussifier();
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(laf.getName())) { //"Windows Classic", "Windows", "CDE/Motif", "Metal", "Nimbus"
                    UIManager.setLookAndFeel(laf.getClassName());
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private static void uid() {

        UUID idOne = UUID.randomUUID();
        String str = "" + idOne;
        int uid = str.hashCode();
        String filterStr = "" + uid;
        str = filterStr.replaceAll("-", "");
        System.out.println(Integer.parseInt(str));
    }

    //https://spec-zone.ru/RU/Java/Docs/7/technotes/guides/scripting/programmer_guide/index.html#top
    private static void script() throws Exception {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("nashorn"); //factory.getEngineByName("JavaScript");
        Bindings scope = engine.createBindings();
//        
//        File f = new File("test.js");
//        engine.put("file", f);
//        engine.eval("print(file.getAbsolutePath())");

        //https://developer.mozilla.org/de/docs/Web/JavaScript/Reference/Global_Objects/Math/ceil
        //Переменные сценария
//        float B = 3;
//        float L = 1200;
//        float H = 56;
//        engine.put("B", B);
//        engine.put("L", L);
//        engine.put("H", H);
//        engine.eval("print(Math.ceil(L + 33.3));");
//
        //Вызов Функций Сценария и Методов
//        String script = "function hello(name) { print('Hello, ' + name); }";
//        engine.eval(script);
//        Invocable inv = (Invocable) engine;
//        inv.invokeFunction("hello", "Аксёнов!!" );
//
        //Основанным на объектах сценария
//        String script = "var obj = new Object(); obj.hello = function(name) { print('Hello, ' + name); }";
//        engine.eval(script);
//        Invocable inv = (Invocable) engine;
//        Object obj = engine.get("obj");
//        inv.invokeMethod(obj, "hello", "Аксёнов!!" );
//
        //Реализация Интерфейсов Java Сценариями
//        String script = "function run() { print('run called Аксёнов'); }";
//        engine.eval(script);
//        Invocable inv = (Invocable) engine;
//        Runnable r = inv.getInterface(Runnable.class);
//        Thread th = new Thread(r);
//        th.start();
//
        //На объектах или объектно-ориентирован
//        String script = "var obj = new Object(); obj.run = function() { print('run method called Аксёнов'); }";
//        engine.eval(script);
//        Object obj = engine.get("obj");
//        Invocable inv = (Invocable) engine;
//        Runnable r = inv.getInterface(obj, Runnable.class);
//        Thread th = new Thread(r);
//        th.start(); 
//        float Q = 3;
//        float L = 1200;
//        float H = 56;
//        engine.put("Q", Q);
//        engine.put("L", L);
//        engine.put("H", H);
//    
//        String script ="Math.ceil((Q * 2 * L) + 3.3)";
//        Object result = engine.eval(script);
//        System.out.println(result);
    }

    public static void random() {
        int a = 0; // Начальное значение диапазона - "от"
        int b = 10; // Конечное значение диапазона - "до"

        int random_number1 = a + (int) (Math.random() * b); // Генерация 1-го числа
        System.out.println("1-ое случайное число: " + random_number1);

        int random_number2 = a + (int) (Math.random() * b); // Генерация 2-го числа
        System.out.println("2-ое случайное число: " + random_number2);

        int random_number3 = a + (int) (Math.random() * b); // Генерация 3-го числа
        System.out.println("3-е случайное число: " + random_number3);
    }
}
/*
{
  "version": "1.0",
  "name": "KBE\\KBE 58\\1 ОКНА\\Открывание внутрь (ств. Z77)",
  "prj": 604008,
  "ord": 1,
  "nuni": 8,
  "width2": 1300.0,
  "height1": 1500.0,
  "height2": 1200.0002,
  "color1": 1009,
  "color2": 10009,
  "color3": 1009,
  "id": 0.0,
  "childs": [
    {
      "id": 1.0,
      "childs": [],
      "layout": "LEFT",
      "type": "FRAME_SIDE",
      "param": {}
    },
    {
      "id": 2.0,
      "childs": [],
      "layout": "RIGHT",
      "type": "FRAME_SIDE",
      "param": {}
    },
    {
      "id": 3.0,
      "childs": [],
      "layout": "TOP",
      "type": "FRAME_SIDE",
      "param": {}
    },
    {
      "id": 4.0,
      "childs": [],
      "layout": "BOTT",
      "type": "FRAME_SIDE",
      "param": {}
    },
    {
      "id": 5.0,
      "childs": [
        {
          "id": 6.0,
          "childs": [],
          "type": "GLASS",
          "param": {}
        }
      ],
      "layout": "HORIZ",
      "type": "AREA",
      "form": "TOP",
      "param": {},
      "length": 300.00006
    },
    {
      "id": 7.0,
      "childs": [],
      "type": "IMPOST",
      "param": {}
    },
    {
      "id": 8.0,
      "childs": [
        {
          "id": 9.0,
          "childs": [
            {
              "id": 10.0,
              "childs": [
                {
                  "id": 11.0,
                  "childs": [],
                  "type": "GLASS",
                  "param": {}
                }
              ],
              "layout": "VERT",
              "type": "STVORKA",
              "param": {
                "typeOpen": 3,
                "sysfurnID": -1
              },
              "length": 650.0
            }
          ],
          "layout": "VERT",
          "type": "AREA",
          "param": {},
          "length": 650.0
        },
        {
          "id": 12.0,
          "childs": [],
          "type": "IMPOST",
          "param": {}
        },
        {
          "id": 13.0,
          "childs": [
            {
              "id": 14.0,
              "childs": [
                {
                  "id": 15.0,
                  "childs": [],
                  "type": "GLASS",
                  "param": {}
                }
              ],
              "layout": "VERT",
              "type": "STVORKA",
              "param": {
                "typeOpen": 3,
                "sysfurnID": -1
              },
              "length": 650.0
            }
          ],
          "layout": "VERT",
          "type": "AREA",
          "param": {},
          "length": 650.0
        }
      ],
      "layout": "HORIZ",
      "type": "AREA",
      "param": {},
      "length": 1200.0002
    }
  ],
  "layout": "VERT",
  "type": "ARCH",
  "param": {}
}
 */
