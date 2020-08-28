package estimate.constr.param;

import dataset.Record;
import domain.eSetting;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import startup.Main;
import estimate.Wincalc;
import estimate.constr.Specification;
import estimate.model.Com5t;

public class Par5s {

    protected final int ID = 1;   //Ключ 0  
    protected final int GRUP = 2;   //Ключ 1  
    protected final int NUMB = 3;   //Ключ 2   
    protected final int TEXT = 4;   //Текст 
    protected Wincalc iwin = null;
    protected String versionDb = eSetting.find(2).getStr(eSetting.val);
    protected int pass = 1; //проверка на попадание либо pass=1 ищем тех что попали, pass=2 основной цикл, pass=3 находим доступные параметры
    public HashMap<Integer, String> mapParamTmp = new HashMap();

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

    public boolean compareBetween(String ptext, float value) {
        if (ptext == null) {
            return true;
        }
        ptext = ptext.replace(",", "."); //парсинг параметра
        char symmetry = ptext.charAt(ptext.length() - 1);
        if (symmetry == '@') {
            ptext = ptext.substring(0, ptext.length() - 1);
        }
        String[] arr = ptext.split(";");
        for (String str : Arrays.asList(arr)) {

            String[] p = str.split("-");
            if (p.length == 1) {
                Float valueOne = Float.valueOf(p[0]);
                if (value <= valueOne) {
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

    //Фильтр параметров выбранных клиентом
    protected boolean filterParamUse(Com5t com5t, List<Record> paramList) {

        HashMap<Integer, Record> paramTotal = new HashMap();
        paramTotal.putAll(iwin.mapParamDef); //добавим параметры по умолчанию
        paramTotal.putAll(com5t.mapParamUse);

        Com5t el = com5t;
        do { //все владельцы этого элемента
            el = el.owner();
            paramTotal.putAll(el.mapParamUse);
        } while (el != iwin.rootArea);

        for (Record paramRec : paramList) {
            if (paramRec.getInt(GRUP) < 0) {
                if (paramTotal.get(paramRec.getInt(GRUP)) == null) {
                    return false;
                }
                Record rec = paramTotal.get(paramRec.getInt(GRUP));
                if (rec.getInt(NUMB) != paramRec.getInt(NUMB)) {
                    return false;
                }
            }
        }
        return true;
    }

    //Необработанные параметры
    protected void message(int code) {
        if (code >= 0) {
            System.err.println("ОШИБКА! КОД " + code + " НЕ ОБРАБОТАН.");
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
