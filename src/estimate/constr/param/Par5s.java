package estimate.constr.param;

import dataset.Record;
import domain.eSetting;
import enums.ParamJson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import satrtup.Main;
import estimate.Wincalc;
import estimate.constr.Specification;
import estimate.model.Com5t;

public class Par5s {

    protected final int GRUP = 2;   //Ключ 1  
    protected final int NUMB = 3;   //Ключ 2   
    protected final int TEXT = 4;   //Текст 
    protected Object obj, obj2, obj3, obj4; //Объекты калькуляции
    protected Float[] arr, arr2, arr3, arr4;
    protected String str, str2, str3, str4;
    protected Float num, num2, num3, num4;
    protected Integer val, val2, val3, val4;
    protected Wincalc iwin = null;
    protected String versionDb = eSetting.find(2).getStr(eSetting.val);
    protected int pass = 1; //проверка на попадание либо pass=1 ищем тех что попали, pass=2 основной цикл, pass=3 находим доступные параметры
    protected String sideCheck = ""; //TODO Эту переменную надо вынести в map параметров!!!

    public Par5s(Wincalc iwin) {
        this.iwin = iwin;
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

    public boolean compareFloat(String ptext, float value) {

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

    public boolean compareInt(String ptext, int value) {

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

    public void test_param(int[] paramArr) {

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

    //Фильтр параметров по умолчанию
    protected boolean filterParamDef(Record paramRec) {
        if (paramRec.getInt(GRUP) < 0) {
            if (iwin.mapParamDef.get(paramRec.getInt(GRUP)) == null) {
                return false;
            }
            int id1 = iwin.mapParamDef.get(paramRec.getInt(GRUP)).getInt(NUMB);
            int id2 = paramRec.getInt(NUMB);
            if ((id1 == id2) == false) {
                return false;
            }
        }
        return true;
    }

    //Фильтр параметров
    protected boolean filterParamJson(Com5t com5t, List<Record> paramList) {

        //HashMap<Integer, Object[]> paramJson = new HashMap();
        HashMap<Integer, Record> paramTotal = new HashMap();
        paramTotal.putAll(iwin.mapParamDef); //добавим параметры по умолчанию
/*
        //Все владельцы этого элемента
        LinkedList<Com5t> ownerList = new LinkedList();
        Com5t el = com5t;
        ownerList.add(el);
        do {
            el = el.owner();
            ownerList.add(el);
        } while (el != iwin.rootArea);

        //Цикл по владельцам этого элемента
        for (int index = ownerList.size() - 1; index >= 0; index--) {

            el = ownerList.get(index);
            HashMap<Integer, Object[]> pJson = (HashMap) el.mapParamUse.get(ParamJson.pro4Params2);
            if (pJson != null && pJson.isEmpty() == false) {  // если параметры от i-okna есть
                if (pass == 1) {
                    paramTotal.putAll(pJson); //к пар. по умолч. наложим парам. от i-win
                } else {
                    for (Map.Entry<Integer, Object[]> entry : pJson.entrySet()) {
                        Object[] val = entry.getValue();
                        if (val[2].equals(1)) { //
                            paramTotal.put(entry.getKey(), entry.getValue()); //по умолчанию и i-win
                        }
                    }
                }
                paramJson.putAll(pJson); //к парам. i-win верхнего уровня наложим парам. i-win нижнего уровня
            }
        }*/
        for (Record paramRec : paramList) {
            if (paramRec.getInt(GRUP) < 0) {

                if (paramTotal.get(paramRec.getInt(GRUP)) == null) {
                    return false; //усли в базе парам. нет, сразу выход
                }
                //В данной ветке есть попадание в paramRec.getInt(PAR1)
                Record record = paramTotal.get(paramRec.getInt(GRUP));
                if ((record.getInt(NUMB) == paramRec.getInt(NUMB)) == false) { //если в param.znumb() попадания нет

                    //на третьей итерации дополняю ...
                    return false;

                }
                //else if (paramJson != null && paramJson.isEmpty() == false && paramJson.get(paramRec.getInt(GRUP)) != null) {
                //    totalVal[2] = 1; //если попадание было, то записываю 1 в третий элемент массива
                //}
            }
        }
        return true;
    }

    //Не обработанные параметры
    protected void message(int code) {
        if (code >= 0) {
            System.err.println("ОШИБКА! КОД " + code + " НЕ ОБРАБОТАН.");
        }
    }

    //Обработанные параметры
    protected void message(int code, Object... obj) {
        if (Main.dev == true) {
            if (code == -13015) {
                if (obj.length == 1) {
                    System.out.println("\u001B[34mПАРАМЕТР code=" + code + " ЗНАЧЕНИЯ " + obj[0] + "\u001B[0m");
                } else if (obj.length == 2) {
                    System.out.println("\u001B[34mПАРАМЕТР code=" + code + " ЗНАЧЕНИЯ " + obj[0] + " " + obj[1] + "\u001B[0m");
                } else if (obj.length == 3) {
                    System.out.println("\u001B[34mПАРАМЕТР code=" + code + " ЗНАЧЕНИЯ " + obj[0] + " " + obj[1] + " " + obj[2] + "\u001B[0m");
                } else if (obj.length == 4) {
                    System.out.println("\u001B[34mПАРАМЕТР code=" + code + " ЗНАЧЕНИЯ " + obj[0] + " " + obj[1] + " " + obj[2] + " " + obj[3] + "\u001B[0m");
                }
            }
        }
    }

    public int determineColorCodeForArt(Com5t com5t, int color_side, Record paramRec, Specification specif) {

//        //int colorCode = getColorFromProduct(com5t, color_side, paramRec);
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

}

/*
    //Проверяет, должен ли применяться заданный тариф мат-ценности для заданной текстуры
    protected boolean IsArtTariffAppliesForColor(Artsvst artsvstRec, Colslst colslstRec) {
        if (artsvstRec.clnum < 0) {    //этот тариф задан для группы текстур

            if ((-1 * colslstRec.cgrup) == artsvstRec.clnum) {
                return true;
            }
        } else {  //проверяем не только colorCode, а еще и colorNumber
            if (colslstRec.ccode == artsvstRec.clcod) {
                return true;

            } else if (colslstRec.cnumb == artsvstRec.clnum) {
                return true;
            }
        }
        return false;
    }

    protected boolean CanBeUsedAsOutsideColor(Artsvst artsvst) { //cways = 1-внутр. 2-внешн. 4-основн.
        return (artsvst.cways & 2) != 0 || CanBeUsedAsBaseColor(artsvst);
    }

    protected boolean CanBeUsedAsInsideColor(Artsvst artsvst) { //cways = 1-внутр. 2-внешн. 4-основн.
        return (artsvst.cways & 1) != 0 || CanBeUsedAsBaseColor(artsvst);
    }

    protected boolean CanBeUsedAsBaseColor(Artsvst artsvst) { //cways = 1-внутр. 2-внешн. 4-основн.
        return (artsvst.cways & 4) != 0;
    } 

    //Все параметры БиМакс
    public static int[] paramSum = {
            11000, 11009, 11010, 11020, 11030, 11040, 11050, 11060, 11067, 11068, 11069, 11070, 11072, 11095, 12000, 12008, 12010, 12020, 12030, 12050, 12060,
            12063, 12065, 12068, 12069, 12070, 12072, 12075, 14000, 14030, 14040, 14050, 14060, 14065, 14068, 15000, 15005, 15011, 15013, 15027, 15030, 15040,
            15045, 15050, 15055, 15068, 15069, 24001, 24002, 24004, 24006, 24010, 24012, 24030, 24033, 24038, 24063, 24067, 24068, 24069, 24070, 24072, 24073, 24074, 24075, 24095,
            24099, 25002, 25010, 25013, 25030, 25035, 25040, 25060, 25067, 33000, 33005, 33008, 33030, 33040, 33050, 33060, 33066, 33067, 33069, 33078, 33095, 34000, 34004, 34005, 34006,
            34007, 34008, 34010, 34011, 34015, 34030, 34051, 34060, 34066, 34067, 34068, 34069, 34070, 34071, 34075, 34095, 34099, 38004, 38010, 38030, 38050,
            38060, 38067, 38068, 38069, 39002, 39005, 39020, 39060, 39068, 39069, 39075, 39077, 39093, 40010, 40067, 40068, 40069};
    //Соединения
    public static int[] parCons = {11000, 11009, 11010, 11020, 11030, 11040, 11050, 11060, 11067, 11068, 11069, 11070, 11072, 11095,
            12000, 12008, 12010, 12020, 12030, 12050, 12060, 12063, 12065, 12068, 12069, 12070, 12072, 12075};
    //Составы
    public static int[] parVsts = {33000, 33005, 33008, 33030, 33040, 33050, 33060, 33066, 33067, 33069, 33078, 33095, 34000, 34004, 34005,
            34006, 34007, 34008, 34010, 34011, 34015, 34030, 34051, 34060, 34066, 34067, 34068, 34069, 34070, 34071, 34075, 34095, 34099,
            38004, 38010, 38030, 38050, 38060, 38067, 38068, 38069, 39002, 39005, 39020, 39060, 39068, 39069, 39075, 39077, 39093,
            40010, 40067, 40068, 40069};
    //Заполнения
    public static int[] parGlas = {14000, 14030, 14040, 14050, 14060, 14065, 14068, 15000, 15005, 15011, 15013, 15027, 15030, 15040,
            15045, 15050, 15055, 15068, 15069};
    //фурнитура
    public static int[] parFurs = {24001, 24002, 25002, 24004, 24006, 24010, 25010, 24012, 24030, 25030, 24033, 24038, 24063, 24067, 25067, 24068, 24069, 24070, 24072, 24073, 24074, 24075,
            24095, 24099, 25013, 25035, 25040, 25060, 25067};
 */
