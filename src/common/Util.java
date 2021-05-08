package common;

public class Util {

    public static Float getFloat(String str) {
        if (str != null && str.isEmpty() == false) {
            str = str.replace(",", ".");
            return Float.valueOf(str);
        }
        return 0f;
    }

    public static Double getDbl(String str) {
        if (str != null && str.isEmpty() == false) {
            str = str.replace(",", ".");
            return Double.valueOf(str);
        }
        return .0;
    }
}
