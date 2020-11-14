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

    //33333;10000-10999;444444;17000-21999;23000-28999 => 
    //[33333, 33333, 10000, 10999, 444444, 444444, 17000, 21999, 23000, 28999]
    public static Integer[] parserInt(String str) {

        ArrayList<Integer> arrList = new ArrayList();
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

    
    public static Float[] parserFloat(String str) {

        ArrayList<Float> arrList = new ArrayList();
        str = str.replace(",", ".");
        char symmetry = str.charAt(str.length() - 1);
        if (symmetry == '@') {
            str = str.substring(0, str.length() - 1);
        }
        String[] arr = str.split(";");
        if (arr.length == 1) {
            arr = arr[0].split("-");
            if (arr.length == 1) {
                return new Float[]{Float.valueOf(arr[0])};
            } else {
                arrList.add(Float.valueOf(arr[0]));
                arrList.add(Float.valueOf(arr[1]));
            }
        } else {
            for (int index = 0; index < arr.length; index++) {
                String[] arr2 = arr[index].split("-");
                if (arr2.length == 2) {
                    arrList.add(Float.valueOf(arr2[0]));
                    arrList.add(Float.valueOf(arr2[1]));
                }
            }
        }
        return arrList.stream().toArray(Float[]::new);
    }

    public static Float[] parserFloat2(String str) {
        Float[] arr = {0f, 6000f, 0f, 6000f};
        str = str.replace(",", ".");
        char symmetry = str.charAt(str.length() - 1);
        if (symmetry == '@') {
            str = str.substring(0, str.length() - 1);
        }
        String[] arr2 = str.split("/");
        if (arr2.length == 2) {
            String[] arr3 = arr2[0].split("-");
            String[] arr4 = arr2[1].split("-");
            if (arr3.length == 2) {
                arr[0] = Float.valueOf(arr3[0]);
                arr[1] = Float.valueOf(arr3[1]);
            }
            if (arr4.length == 2) {
                arr[2] = Float.valueOf(arr4[0]);
                arr[3] = Float.valueOf(arr4[1]);
            }
        }
        return arr;
    }

    public static boolean compareInt(String ptext, int value) {

        if (ptext == null) {
            return true;
        }
        ptext = ptext.replace(",", "."); //парсинг параметра
        char symmetry = ptext.charAt(ptext.length() - 1);
        if (symmetry == '@') {
            ptext = ptext.substring(0, ptext.length() - 1);
        }
        String[] arr = ptext.split(";");
        List<String> arrList = Arrays.asList(arr);

        for (String str : arrList) {
            String[] p = str.split("-");
            if (p.length == 1) {
                Integer valueOne = Integer.valueOf(p[0]);
                if (value == valueOne) {
                    return true;
                }
            } else if (p.length == 2) {
                Integer valueMin = Integer.valueOf(p[0]);
                Integer valueMax = Integer.valueOf(p[1]);
                if (valueMin <= value && valueMax >= value) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean compareFloat(String ptext, float value) {

        if (ptext == null) {
            return true;
        }
        ptext = ptext.replace(",", "."); //парсинг параметра
        char symmetry = ptext.charAt(ptext.length() - 1);
        if (symmetry == '@') {
            ptext = ptext.substring(0, ptext.length() - 1);
        }
        String[] arr = ptext.split(";");
        List<String> arrList = Arrays.asList(arr);
        for (String str : arrList) {

            String[] p = str.split("-");
            if (p.length == 1) {
                Float valueOne = Float.valueOf(p[0]);
                if (value == valueOne) {
                    return true;
                }

            } else if (p.length == 2) {
                Float valueMin = Float.valueOf(p[0]);
                Float valueMax = Float.valueOf(p[1]);
                if (valueMin <= value && valueMax >= value) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean compareColor(Integer[] arr, Integer color) {
        if (arr.length == 1) {
            int arr1 = arr[0];
            return (arr1 == color);
        } else {
            for (int index1 = 0; index1 < arr.length; index1 = index1 + 2) {
                int arr1 = arr[index1];
                int arr2 = arr[index1 + 1];
                if (arr1 <= color && color <= arr2) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean containsFloat(String ptext, float value) {

        if (ptext == null || ptext.isEmpty()) {
            return true;
        }
        ptext = ptext.replace(",", "."); //парсинг параметра
        String[] arr = ptext.split(";");
        for (String el : arr) {

            Float valueOne = Float.valueOf(el);
            if (value == valueOne) {
                return true;
            }
        }
        return false;
    }

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
//  */1@
//  S;/*
//  554192;/*
//
//  30-89,99;90,01-150;180,01-269,99;270,01-359,99
//  10000-10999;17000-29999;42000-42999;46000-46999
//  1009;10000-10999;17000-22999
//  1009;10000-10999;17000-29999;42003-42999;46000-46999
//
//  790,01-10000/0-1000000
//  288-488/1028,01-1128
//  -10-10/-10-10@
//  400,1-450

