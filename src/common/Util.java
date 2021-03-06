package common;

import builder.model.ElemSimple;
import enums.LayoutArea;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Util {

    public static Float getFloat(String str) {

        if (str != null && str.isEmpty() == false) {
            str = str.replace(",", ".");
            try {
                return Float.valueOf(str);
            } catch (java.lang.NumberFormatException e) {
                System.err.println("Ошибка:Util.getFloat() " + e);
            }
        }
        return -1f;
    }

    public static Double getDbl(String str) {
        if (str != null && str.isEmpty() == false) {
            str = str.replace(",", ".");
            try {
                return Double.valueOf(str);
            } catch (java.lang.NumberFormatException e) {
                System.err.println("Ошибка:Util.getFloat() " + e);
            }
        }
        return -1.0;
    }

    //1;79-10;0-10 => [1,1,79,10,0,10]
    public static Integer[] parserInt(String txt) {
        if (txt.isEmpty()) {
            return new Integer[]{};
        }
        ArrayList<Object> arrList = new ArrayList();
        txt = (txt.charAt(txt.length() - 1) == '@') ? txt.substring(0, txt.length() - 1) : txt;
        String[] arr = txt.split(";");
        if (arr.length == 1) {
            arr = arr[0].split("-");
            if (arr.length == 1) {
                arrList.add(Integer.valueOf(arr[0]));
                arrList.add(Integer.valueOf(arr[0]));
            } else {
                arrList.add(Integer.valueOf(arr[0]));
                arrList.add(Integer.valueOf(arr[1]));
            }
        } else {
            for (int index = 0; index < arr.length; index++) {
                String[] arr2 = arr[index].split("-");
                if (arr2.length == 1) {
                    arrList.add(Integer.valueOf(arr2[0]));
                    arrList.add(Integer.valueOf(arr2[0]));
                } else {
                    arrList.add(Integer.valueOf(arr2[0]));
                    arrList.add(Integer.valueOf(arr2[1]));
                }
            }
        }
        return arrList.stream().toArray(Integer[]::new);
    }

    //0.55;79,01-10;0-10 => [0.55,0.55,79.01,10.0,0.0,10.0]
    public static Float[] parserFloat(String str) {
        if (str.isEmpty()) {
            return new Float[]{};
        }
        ArrayList<Object> arrList = new ArrayList();
        str = str.replace(",", ".");
        String[] arr = str.split(";");
        if (arr.length == 1) {
            arr = arr[0].split("-");
            if (arr.length == 1) {
                arrList.add(Float.valueOf(arr[0]));
                arrList.add(Float.valueOf(arr[0]));
            } else {
                arrList.add(Float.valueOf(arr[0]));
                arrList.add(Float.valueOf(arr[1]));
            }
        } else {
            for (int index = 0; index < arr.length; index++) {
                String[] arr2 = arr[index].split("-");
                if (arr2.length == 1) {
                    arrList.add(Float.valueOf(arr2[0]));
                    arrList.add(Float.valueOf(arr2[0]));
                } else {
                    arrList.add(Float.valueOf(arr2[0]));
                    arrList.add(Float.valueOf(arr2[1]));
                }
            }
        }
        return arrList.stream().toArray(Float[]::new);
    }

    //"180",  "30-179",  "0-89,99;90, 01-150;180, 01-269, 99;270, 01-359, 99"
    public static boolean containsNumb(String txt, Number value) {
        if (txt == null || txt.isEmpty() || txt.equals("*")) {
            return true;
        }
        ArrayList<Float> arrList = new ArrayList();
        txt = txt.replace(",", ".");
        String[] arr = txt.split(";");
        if (arr.length == 1) {
            arr = arr[0].split("-");
            if (arr.length == 1) {
                arrList.add(Float.valueOf(arr[0]));
                arrList.add(Float.valueOf(arr[0]));
            } else {
                arrList.add(Float.valueOf(arr[0]));
                arrList.add(Float.valueOf(arr[1]));
            }
        } else {
            for (int index = 0; index < arr.length; index++) {
                String[] arr2 = arr[index].split("-");
                if (arr2.length == 1) {
                    arrList.add(Float.valueOf(arr2[0]));
                    arrList.add(Float.valueOf(arr2[0]));
                } else {
                    arrList.add(Float.valueOf(arr2[0]));
                    arrList.add(Float.valueOf(arr2[1]));
                }
            }
        }
        for (int index = 0; index < arrList.size(); ++index) {
            float v1 = arrList.get(index);
            float v2 = arrList.get(++index);
            float v3 = Float.valueOf(value.toString());
            if (v1 <= v3 && v3 <= v2) {
                return true;
            }
        }
        return false;
    }

    //"180",  "30-179",  "0-89,99;90, 01-150;180, 01-269, 99;270, 01-359, 99"
    public static boolean containsNumb2(String txt, Number value) {
        if (txt == null || txt.isEmpty() || txt.equals("*")) {
            return true;
        }
        ArrayList<Float> arrList = new ArrayList();
        txt = txt.replace(",", ".");
        String[] arr = txt.split(";");
        if (arr.length == 1) {
            arr = arr[0].split("-");
            if (arr.length == 1) {
                arrList.add(0f);
                arrList.add(Float.valueOf(arr[0]));
            } else {
                arrList.add(Float.valueOf(arr[0]));
                arrList.add(Float.valueOf(arr[1]));
            }
        } else {
            for (int index = 0; index < arr.length; index++) {
                String[] arr2 = arr[index].split("-");
                if (arr2.length == 1) {
                    arrList.add(Float.valueOf(arr2[0]));
                    arrList.add(Float.valueOf(arr2[0]));
                } else {
                    arrList.add(Float.valueOf(arr2[0]));
                    arrList.add(Float.valueOf(arr2[1]));
                }
            }
        }
        for (int index = 0; index < arrList.size(); ++index) {
            float v1 = arrList.get(index);
            float v2 = arrList.get(++index);
            float v3 = Float.valueOf(value.toString());
            if (v1 <= v3 && v3 <= v2) {
                return true;
            }
        }
        return false;
    }

    //"288-488/1028,01-1128", "2000,2-3000/0-1250@", "55;/*"
    //TODO необходимо учесть такой вариант -27,5/-27,5 см. 34049
    public static boolean containsNumb(String txt, Number val1, Number val2) {
        if (txt == null || txt.isEmpty()) {
            return true;
        }
        char symmetry = txt.charAt(txt.length() - 1);
        if (symmetry == '@') {
            txt = txt.substring(0, txt.length() - 1);
        }
        String[] arr = txt.split("/");
        if (symmetry == '@') {
            if (containsNumb(arr[0], val1) == true || containsNumb(arr[1], val2) == true) {
                return true;
            }
            if (containsNumb(arr[1], val1) == true || containsNumb(arr[0], val2) == true) {
                return true;
            }
            return false;
        } else {
            if (containsNumb(arr[0], val1) == true && containsNumb(arr[1], val2) == true) {
                return true;
            }
            return false;
        }
    }

    //"288-488/1028,01-1128", "2000,2-3000/0-1250"
    public static boolean containsNumbAny(String txt, Number val1, Number val2) {
        if (txt == null || txt.isEmpty()) {
            return true;
        }
        String[] arr = txt.split("/");
        if (containsNumb(arr[0], val1) == true || containsNumb(arr[1], val2) == true) {
            return true;
        }
        return false;
    }

    //"Стойка 172;Стойка 240;
    public static boolean containsStr(String str, String val) {
        String[] arr = str.split(";");
        for (String str2 : arr) {
            if (str2.equals(val)) {
                return true;
            }
        }
        return false;
    }

    //"Стойка 100;Стойка 200/*", "Slidors 60;@/Slidors 60;@"
    public static boolean containsStr(String str, String val1, String val2) {
        if (str == null || str.isEmpty()) {
            return true;
        }
        String[] arr = str.split("/");
        if ((arr[0].equals("*") || containsStr(arr[0], val1) == true)
                && (arr[1].equals("*") || containsStr(arr[1], val2) == true)) {
            return true;
        }
        return false;
    }
}
