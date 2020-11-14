/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package estimate.constr;

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

    //0.55;79,01-10;0-10=>[0.55,0.55,79.01,10.0,0.0,10.0]
    public static Float[] parserFloat(String str) {

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
        if (str == null || str.isEmpty()) {
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

    //"288-488/1028,01-1128", "2000,2-3000/0-1250@", "554192;/*"
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

//==============================================================================    
    public boolean checkSize(float par, float... arr) {
        return true;
    }

    //Проверяет, должен ли применяться заданный тариф мат-ценности для заданной текстуры
    public static boolean IsArtTariffAppliesForColor(Record artdetRec, Record colorRec) {
        if (artdetRec.getInt(eArtdet.color_fk) < 0) {    //этот тариф задан для группы текстур

            if ((-1 * colorRec.getInt(eColor.colgrp_id)) == artdetRec.getInt(eArtdet.color_fk)) {
                return true;
            }
        } else {  //проверяем не только colorCode, а еще и colorNumber
            if (colorRec.getInt(eColor.id) == artdetRec.getInt(eArtdet.color_fk)) {
                return true;

            }
//            else if (colorRec.cnumb == artdetRec.getInt(eArtdet.color_fk)) {
//                return true;
//            }
        }
        return false;
    }
}
//  ==  Примеры параметров  ==
//  GW58;Без упллотнения;@/GW58;Без упллотнения;@
//  Slidors 60;@/Slidors 60;@
//
//  30-89,99;90,01-150;180,01-269,99;270,01-359,99
//  1009;10000-10999;17000-29999;42003-42999;46000-46999
//
//  288-488/1028,01-1128
//  1-2/1-2@
//  -10-10/-10-10@
//  */1@
//  S;/*
//  554192;/*
/*
select grup, text from elempar1 where grup > 0 union
select grup, text from elempar2 where grup > 0 union
select grup, text from furnpar1 where grup > 0 union
select grup, text from furnpar2 where grup > 0 union
select grup, text from glaspar1 where grup > 0 union
select grup, text from glaspar2 where grup > 0 union
select grup, text from joinpar1 where grup > 0 union
select grup, text from joinpar2 where grup > 0
order by 1
 */
