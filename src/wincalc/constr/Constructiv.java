package wincalc.constr;

import dataset.Record;
import domain.eColor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import wincalc.Wincalc;
import wincalc.model.Com5t;

/**
 * Расчёт конструктива окна.
 */
public class Constructiv {

    protected Wincalc iwin = null;
    
    public Constructiv(Wincalc iwin) {
        this.iwin = iwin;
    }

    public int determineColorCodeForArt(Com5t elem, int color_side, Record paramRec, Specification specif) {

//        //int colorCode = getColorFromProduct(elem, color_side, paramRec);
//            Colslst clslstRecr = Colslst.get2(constr, colorCode);
//        //Фвтоподбор текстуры
//        if (paramRec.clnum() == 0) {
//            // Если этот цвет не подходит для данного артикула, то берем первую имеющуюся текстуру в тарифе
//            // материальных ценностей указанного артикула (см. "Автоподбор" на http://help.profsegment.ru/?id=1107).
//            boolean colorOK = false;
//            int firstFoundColorNumber = 0;
//            for (Artsvst artsvstRec : constr.artsvstMap.get(specif.artikl)) {
//                if ((color_side == 1 && CanBeUsedAsBaseColor(artsvstRec))
//                        || (color_side == 2 && CanBeUsedAsInsideColor(artsvstRec))
//                        || (color_side == 3 && CanBeUsedAsOutsideColor(artsvstRec))) {
//
//                    if (IsArtTariffAppliesForColor(artsvstRec, clslstRecr)) {
//                        colorOK = true;
//                        break;
//                    } else if (firstFoundColorNumber == 0)
//                        firstFoundColorNumber = artsvstRec.clnum;
//                }
//            }
//            if (colorOK) return colorCode;
//            else if (firstFoundColorNumber > 0) return Colslst.get(constr, firstFoundColorNumber).ccode;
//            else return root.getIwin().getColorNone();
//
//            //указана
//            //} else if (vstaspcRec.clnum() == 1) {
//            //    return colorCode;
//
//        } else if (paramRec.clnum() == 100000) {
//            return colorCode;
//
//
//            //Текстура задана через параметр
//        } else if (paramRec.clnum() < 0) {
//            if (colorCode == root.getIwin().getColorNone()) {
//                for (Parcols parcolsRec : constr.parcolsList) {
//                    if (parcolsRec.pnumb == paramRec.clnum()) {
//                        int code = Colslst.get3(constr, parcolsRec.ptext).ccode;
//                        if (code != -1) return code;
//                    }
//                }
//            }
//            return colorCode;
//
//            //В clnum указан цвет.
//        } else {
//            if (colorCode == root.getIwin().getColorNone()) {    //видимо во всех текстурах стоит значение "Указана"
//                // Подбираем цвет так (придумала Л.Цветкова):
//                // Если основная текстура изделия может быть основной текстурой для данного артикула (по тарифу мат.ценностей), то используем ее.
//                // Иначе - используем CLNUM как основную текстуру. Аналогично для внутренней и внешней текстур.
//
//                int colorNumber = Colslst.get2(constr, root.getIwin().getColorProfile(color_side)).cnumb;
//                boolean colorFound = false;
//                for (Artsvst artsvst : constr.artsvstMap.get(specif.artikl)) {
//
//                    if ((color_side == 1 && CanBeUsedAsBaseColor(artsvst)) ||
//                            (color_side == 2 && CanBeUsedAsInsideColor(artsvst)) ||
//                            (color_side == 3 && CanBeUsedAsOutsideColor(artsvst))) {
//
//                        if (IsArtTariffAppliesForColor(artsvst, clslstRecr) == true) {
//                            for (Parcols parcolsRec : constr.parcolsList) {
//                                if (parcolsRec.pnumb == paramRec.clnum()) {
//                                    return Integer.valueOf(parcolsRec.ptext);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
        //return colorCode;
        return -1;
    }

    // Выдает цвет из текущего изделия в соответствии с заданным вариантом подбора текстуры (dll->GetColorCodeFromProduct() - см. "SVN\iWin_Doc\trunk\Профстрой\Значения GLASART.CTYPE.xlsx".
    public int getColorFromProduct(Com5t com5t, int colorSide, int type, int colorId) {
        // Получаем код варианта подбора текстуры (см. "SVN\iWin_Doc\trunk\Профстрой\Значения GLASART.CTYPE.xlsx").
        int colorType = 0;
        if (colorSide == 1) {
            colorType = type % 16;
        } else if (colorSide == 2) {
            colorType = (type / 16) % 16;
        } else if (colorSide == 3) {
            colorType = (type / (16 * 16)) % 16;
        }
        switch (colorType) {
            case 0:  //указана
                return colorId;
            case 1:        // по основе изделия
            case 6:        // по основе в серии
                return iwin.color1; //elem.getColor(1);
            case 2:        // по внутр.изделия
            case 7:        // по внутр. в серии
                return iwin.color2; //elem.getColor(2);
            case 3:        // по внешн.изделия
            case 8:        // по внешн. в серии
                return iwin.color3; //elem.getColor(3);
            case 11:    // по профилю
                return iwin.color1;
            case 15:    // по заполнению
                return com5t.color1;
            default:    // без цвета
                return iwin.colorNone;
        }
    }

    //Проверяет, должен ли применяться заданный тариф мат-ценности для заданной текстуры
//    protected boolean IsArtTariffAppliesForColor(Artsvst artsvstRec, Colslst colslstRec) {
//        if (artsvstRec.clnum < 0) {    //этот тариф задан для группы текстур
//
//            if ((-1 * colslstRec.cgrup) == artsvstRec.clnum) {
//                return true;
//            }
//        } else {  //проверяем не только colorCode, а еще и colorNumber
//            if (colslstRec.ccode == artsvstRec.clcod) {
//                return true;
//
//            } else if (colslstRec.cnumb == artsvstRec.clnum) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    protected boolean CanBeUsedAsOutsideColor(Artsvst artsvst) { //cways = 1-внутр. 2-внешн. 4-основн.
//        return (artsvst.cways & 2) != 0 || CanBeUsedAsBaseColor(artsvst);
//    }
//
//    protected boolean CanBeUsedAsInsideColor(Artsvst artsvst) { //cways = 1-внутр. 2-внешн. 4-основн.
//        return (artsvst.cways & 1) != 0 || CanBeUsedAsBaseColor(artsvst);
//    }
//
//    protected boolean CanBeUsedAsBaseColor(Artsvst artsvst) { //cways = 1-внутр. 2-внешн. 4-основн.
//        return (artsvst.cways & 4) != 0;
//    }
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
