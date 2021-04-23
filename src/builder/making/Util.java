/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package builder.making;

import dataset.Record;
import domain.eArtdet;
import domain.eColor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author aks
 */
public class Util {

    public boolean DblNotZero(Object p) {
        float p2 = (float) p;
        return p2 > 0.00005;
    }

    public static Float getFloat(String str) {
        if (str != null && str.isEmpty() == false) {
            str = str.replace(",", ".");
            return Float.valueOf(str);
        }
        return 0f;
    }

    //1;79-10;0-10=>[1,1,79,10,0,10]
    public static Integer[] parserInt(String str) {
        if (str.isEmpty()) {
            return new Integer[]{};
        }
        ArrayList<Object> arrList = new ArrayList();
        String[] arr = str.split(";");
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

    //0.55;79,01-10;0-10=>[0.55,0.55,79.01,10.0,0.0,10.0]
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

    //"30-89,99;90,01-150;180,01-269,99;270,01-359,99"
    public static boolean containsInt(String str, int value) {
        if (str == null || str.isEmpty()) {
            return true;
        }
        ArrayList<Integer> arrList = new ArrayList();
        str = str.replace(",", ".");
        String[] arr = str.split(";");
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
        for (int index = 0; index < arrList.size(); ++index) {
            float v1 = arrList.get(index);
            float v2 = arrList.get(++index);
            if (v1 <= value && value <= v2) {
                return true;
            }
        }
        return false;
    }

    //"180",  "30-179",  "0-89,99;90,01-150;180,01-269,99;270,01-359,99"
    public static boolean containsFloat(String str, float value) {
        if (str == null || str.isEmpty() || str.equals("*")) {
            return true;
        }
        ArrayList<Float> arrList = new ArrayList();
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
        for (int index = 0; index < arrList.size(); ++index) {
            float v1 = arrList.get(index);
            float v2 = arrList.get(++index);
            if (v1 <= value && value <= v2) {
                return true;
            }
        }
        return false;
    }

    //"288-488/1028,01-1128", "2000,2-3000/0-1250@", "55;/*"
    public static boolean containsFloat(String str, float val1, float val2) {
        if (str == null || str.isEmpty()) {
            return true;
        }
        char symmetry = str.charAt(str.length() - 1);
        if (symmetry == '@') {
            str = str.substring(0, str.length() - 1);
        }
        String[] arr = str.split("/");
        if (symmetry == '@') {
            if (containsFloat(arr[0], val1) == true || containsFloat(arr[1], val2) == true) {
                return true;
            }
            if (containsFloat(arr[1], val1) == true || containsFloat(arr[0], val2) == true) {
                return true;
            }
            return false;
        } else {
            if (containsFloat(arr[0], val1) == true && containsFloat(arr[1], val2) == true) {
                return true;
            }
            return false;
        }
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

    //"Стойка 100;Стойка 200;/*", "Slidors 60;@/Slidors 60;@"
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
/*
 ==  Примеры параметров см. в базе ==
select grup, text from elempar1 where grup > 0 union
select grup, text from elempar2 where grup > 0 union
select grup, text from furnpar1 where grup > 0 union
select grup, text from furnpar2 where grup > 0 union
select grup, text from glaspar1 where grup > 0 union
select grup, text from glaspar2 where grup > 0 union
select grup, text from joinpar1 where grup > 0 union
select grup, text from joinpar2 where grup > 0 order by 1
 */
