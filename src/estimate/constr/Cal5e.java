package estimate.constr;

import dataset.Record;
import domain.eColor;
import java.util.*;
import estimate.Wincalc;
import estimate.model.AreaSimple;
import estimate.model.Com5t;

public abstract class Cal5e {

    private Wincalc iwin = null;
    public Set listVariants = new HashSet();

    public Cal5e(Wincalc iwin) {
        this.iwin = iwin;
    }

    public abstract void calc();

    public Wincalc iwin() {
        return iwin;
    }

    public AreaSimple root() {
        return iwin.rootArea;
    }

    public boolean DblNotZero(Object p) {
        float p2 = (float) p;
        return p2 > 0.00005;
    }

    public Integer[] parserInt(String str) {

        ArrayList<Integer> arrList = new ArrayList();
        char symmetry = str.charAt(str.length() - 1);
        if (symmetry == '@') {
            str = str.substring(0, str.length() - 1);
        }
        String[] arr = str.split(";");
        if (arr.length == 1) {
            arr = arr[0].split("-");
            if (arr.length == 1) {
                return new Integer[]{Integer.valueOf(arr[0])};
            } else {
                arrList.add(Integer.valueOf(arr[0]));
                arrList.add(Integer.valueOf(arr[1]));
            }
        } else {
            for (int index = 0; index < arr.length; index++) {
                String[] arr2 = arr[index].split("-");
                if (arr2.length == 2) {
                    arrList.add(Integer.valueOf(arr2[0]));
                    arrList.add(Integer.valueOf(arr2[1]));
                }
            }
        }
        return arrList.stream().toArray(Integer[]::new);
    }

    public Float[] parserFloat(String str) {

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

    public Float[] parserFloat2(String str) {
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

    public static boolean containsFloat(String ptext, float value) {

        if (ptext == null) {
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

    public boolean compareColor(Integer[] arr, Integer color) {
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

    public boolean checkSize(float par, float... arr) {
        return true;
    }

    public static void test_param(int[] paramArr) {

        HashMap<String, ArrayList> hm = new HashMap();
        for (int index = 0; index < paramArr.length; ++index) {
            Integer param = paramArr[index];
            String code = (String.valueOf(param).length() == 4) ? String.valueOf(param).substring(1, 4) : String.valueOf(param).substring(2, 5);
            if (hm.get(code) == null) {
                ArrayList<Integer> value = new ArrayList();
                value.add(Integer.valueOf(code));
                value.add(param);
                hm.put(code, value);
            } else {
                ArrayList arr = hm.get(code);
                arr.add(param);
            }
        }
        ArrayList<ArrayList<Integer>> arr = new ArrayList();
        for (Map.Entry<String, ArrayList> el : hm.entrySet()) {
            arr.add(el.getValue());
        }
        arr.sort(new Comparator<ArrayList<Integer>>() {

            public int compare(ArrayList a, ArrayList b) {
                return (Integer) a.get(0) - (Integer) b.get(0);
            }
        });
        for (ArrayList el : arr) {
            System.out.println(el);
        }
    }
}
