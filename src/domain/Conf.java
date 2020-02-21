
package domain;

public class Conf {
    public static String app = "app";    //настольное приложение
    public static String calc = "calc";  //калькулятор в web   
    private static String env = "app";
    
    public static void setEnv(String _env) {
        env = _env;
    }
    public static boolean comp(String _env) {
        return env.equals(_env);
    }
}
