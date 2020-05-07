package wincalc.constr;

import dataset.Record;
import domain.eArtikl;
import domain.eElement;
import domain.eElempar1;
import domain.eElempar2;
import domain.eSysprof;
import domain.eSystree;
import enums.TypeJoin;
import enums.LayoutArea;
import enums.TypeElem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import wincalc.Wincalc;
import wincalc.model.Com5t;
import wincalc.model.ElemJoining;
import wincalc.model.ElemSimple;

//Перечень параметров конструктива (составов, заполнений...)
//Параметры верхней части формы конструктива (составов, заполнений...)
public class ParamVariant {

    private ParamVariant paramVariant = null;
    private ParamSpecific paramSpecific = null;
    
    public static final int PAR1 = 3;   //Ключ 1  
    public static final int PAR2 = 4;   //Ключ 2   
    public static final int PAR3 = 5;   //Значение     

    protected Wincalc iwin = null;
    private Constructiv calcConstr = null;

    public ParamVariant(Wincalc iwin, Constructiv calcConstr) {
        this.iwin = iwin;
        this.calcConstr = calcConstr;
    }

    private boolean filterParamDef(Record paramRec) {
        if (paramRec.getInt(PAR1) < 0) {
            if (iwin.mapParamDef.get(paramRec.getInt(PAR1)) == null) {
                return false;
            }
            int id1 = Integer.valueOf(iwin.mapParamDef.get(paramRec.getInt(PAR1))[1].toString());
            int id2 = paramRec.getInt(PAR2);
            if ((id1 == id2) == false) {
                return false;
            }
        }
        return true;
    }

    //Составы
    //int[] parVstm = {31000, 31001, 31002, 31003, 31004, 31005, 31006, 31007, 31008, 31015, 31016, 31020, 31033, 31034, 31037, 31041, 31050, 31052, 31055, 31056, 31080, 31085, 31090, 31095, 31097, 31099, 37001, 37002, 37009, 37010, 37030, 37042, 37056, 37080, 37085, 37099};
    protected boolean element(ElemSimple elemSimple, ArrayList<Record> tableList) {

        //Цикл по параметрам состава
        for (Record paramRec : tableList) {

            if (filterParamDef(paramRec) == false) return false;
            switch (paramRec.getInt(PAR1)) {
                
                case 31000: //Для технологического кода контейнера
                    Record sysprofRec = elemSimple.sysprofRec;
                    Record artiklVRec = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), false);
                    if (artiklVRec.get(eArtikl.tech_code) == null) return false;
                    String[] strList = paramRec.getStr(PAR3).split(";");
                    String[] strList2 = artiklVRec.getStr(PAR3).split(";");
                    boolean ret2 = false;
                    for (String str : strList) {
                        for (String str2 : strList2) {
                            if (str.equals(str2)) {
                                ret2 = true;
                            }
                        }
                    }
                    if (ret2 == false) return false;
                    break;
                case 31002://Если профиль прямой, арочный
                case 37002:
                    Object obj = elemSimple.layout();
                    if (LayoutArea.ARCH == elemSimple.layout() && "арочный".equals(paramRec.getStr(PAR3)) == false) {
                        return false;
                    } else if (LayoutArea.ARCH != elemSimple.layout() && "прямой".equals(paramRec.getStr(PAR3)) == false) {
                        return false;
                    }
                    break;
                case 31004: //Если прилегающий артикул
                    HashMap<String, ElemJoining> mapJoin = elemSimple.iwin().mapJoin;
                    boolean ret = false;
                    for (Map.Entry<String, ElemJoining> elemJoin : mapJoin.entrySet()) {
                        ElemJoining el = elemJoin.getValue();
                        if (TypeJoin.VAR4 == el.varJoin &&
                                el.joinElement1.artiklRec.equals(elemSimple.artiklRec) &&
                                el.joinElement2.artiklRec.equals(paramRec.getStr(PAR3))) {
                            ret = true;
                        }
                    }
                    if (ret == false) return false;
                    break;
                case 31005: //Коды основной текстуры контейнера
                    if (Constructiv.compareInt(paramRec.getStr(PAR3), elemSimple.color1) == false) return false;
                    break;
                case 31006: //Коды внутр. текстуры контейнера
                    if (Constructiv.compareInt(paramRec.getStr(PAR3), elemSimple.color2) == false) return false;
                    break;
                case 31007://Коды внешн. текстуры контейнера
                    if (Constructiv.compareInt(paramRec.getStr(PAR3), elemSimple.color3) == false) return false;
                    break;
                case 31015: //Форма заполнения
                    if (paramRec.getStr(PAR3).equals(elemSimple.specificationRec.getParam("empty", 13015)) == false) return false;
                    break;
                case 31020: //Ограничение угла к горизонту
                    if (Constructiv.compareFloat(paramRec.getStr(PAR3), ((ElemSimple)elemSimple).anglHoriz) == false) return false;
                    break;
                case 31037:  //Название фурнитуры содержит
                    if (TypeElem.FULLSTVORKA == elemSimple.owner().type()) {
                        return paramRec.getStr(PAR3).contains(elemSimple.artiklRec.getStr(eArtikl.name));
                    } else return false;
                case 31041: //Ограничение длины профиля, мм
                    if (Constructiv.compareFloat(paramRec.getStr(PAR3), elemSimple.width()) == false) return false;
                    break;
                case 31050: //Контейнер имеет тип
                    TypeElem type = elemSimple.type();
                    if (type.value != Integer.valueOf(paramRec.getStr(PAR3))) return false;
                    break;
                case 31052:
                    if(elemSimple.layout() == LayoutArea.ARCH) {
                        elemSimple.specificationRec.putParam(paramRec.getInt(PAR1), paramRec.getStr(PAR3));
                    }
                    break;
                case 31055: //Коды внутр. и внешн. текстуры изд
                    if ((Constructiv.compareInt(paramRec.getStr(PAR3), elemSimple.color2) == true &&
                            Constructiv.compareInt(paramRec.getStr(PAR3), elemSimple.color3) == true) == false)
                        return false;
                    break;
                case 31056: //Коды внутр. или внеш. текстуры изд
                case 37056:
                    if ((Constructiv.compareInt(paramRec.getStr(PAR3), elemSimple.color2) == true ||
                            Constructiv.compareInt(paramRec.getStr(PAR3), elemSimple.color3) == true) == false)
                        return false;
                    break;
                case 31099:  //Трудозатраты, ч/ч.
                case 37099:
                    break;
                default:
                    message(paramRec.getInt(PAR1));
                    break;
            }
        }
        return true;
    }

    // Заполнения
    //int[] parGrup = {13015, 13017, 13081, 13099};
    protected boolean filling(ElemSimple elemBase, List<Record> tableList) {

        //Цикл по параметрам состава
        for (Record paramRec : tableList) {

            if (filterParamDef(paramRec) == false) return false;
            switch (paramRec.getInt(PAR1)) {
                case 13015: //Форма заполнения
                    Object objx = elemBase.specificationRec.getParam("empty", 13015);
                    if (paramRec.getStr(PAR3).equals(elemBase.specificationRec.getParam("empty", 13015)) == false) return false;
                    break;
                case 13017: //Код системы содержит строку
                    Record sysprofRec = eSystree.find(iwin.nuni);
                    if (sysprofRec.getStr(eSystree.pref).contains(paramRec.getStr(PAR3)) == false)
                        return false;
                    break;
                case 13099: //Трудозатраты, ч/ч.
                    break;
                default:
                    message(paramRec.getInt(PAR1));
                    break;
            }
        }
        return true;
    }

    // Фурнитура
    //int[] parFurl = {2101, 2104, 2140, 2185};
    protected boolean furniture(ElemSimple elemSimple, List<Record> tableList) {

        //Цикл по параметрам фурнитуры
        for (Record paramRec : tableList) {

            if (filterParamDef(paramRec) == false) return false;

            switch (paramRec.getInt(PAR1)) {
                case 21001: //Форма контура прямоугольная трапециевидная
                    if (TypeElem.FULLSTVORKA == elemSimple.type() && "прямоугольная".equals(paramRec.getStr(PAR3)) == false) {
                        return false;
                    }
                    break;
                case 21004: //Артикул створки
                    if (elemSimple.artiklRec.getStr(eArtikl.code).equals(paramRec.getStr(PAR3)) == false) {
                        return false;
                    }
                    break;
                default:
                    message(paramRec.getInt(PAR1));
                    break;
            }
        }
        return true;
    }

    /**
     * 1000 - прилегающее соединение
     * 2000 - угловое на ус
     * 3000 - угловое (левое, правое)
     * 4000 - Т образное соединение
     */
    //Соединения
    //  int[] parConv = {1005, 1008, 1010, 1011, 1012, 1013, 1020, 1040, 1085, 1099, 2005, 2012, 2013, 2020, 2030, 2061, 2099, 3002, 3003, 3005, 3015,
    //  3020, 3031, 3050, 3099, 4002, 4005, 4011, 4012, 4013, 4015, 4018, 4020, 4040, 4044, 4085, 4095, 4099};
    protected boolean joining(ElemJoining elemJoin, List<Record> parconvList) {
        
        float angl = (ElemSimple.SIDE_START == ElemJoining.FIRST_SIDE) ? elemJoin.joinAngl(1) : elemJoin.joinAngl(2);
        ElemSimple joinElement1 = elemJoin.joinElement1;
        ElemSimple joinElement2 = elemJoin.joinElement2;
        boolean result = true;
        String strTxt = "";
        //цикл по параметрам элементов соединения
        for (Record paramRec : parconvList) {

            String code = (paramRec.getStr(PAR1).length() == 4) ? paramRec.getStr(PAR1).substring(1, 4) : paramRec.getStr(PAR1).substring(2, 5);
            switch (code) {
                case "002": //Вид Т-образного варианта (простое Т-обр. крестовое Т-обр. сложное Y-обр.)
                    if (elemJoin.varJoin == TypeJoin.VAR4 && "Простое Т-обр.".equals(paramRec.getStr(PAR3)) == false)
                        return false;
                    break;
                case "005": //Контейнер имеет тип Артикула1/Артикула2
                    try {
                        strTxt = paramRec.getStr(PAR3);
                        int type1 = joinElement1.type().value;
                        int type2 = joinElement2.type().value;

                        char symmetry = strTxt.charAt(strTxt.length() - 1);
                        if (symmetry == '@') {
                            strTxt = strTxt.substring(0, strTxt.length() - 1);
                        }
                        String arr2[] = strTxt.split("/");//парсинг параметра
                        int[] arr3 = Arrays.asList(arr2).stream().mapToInt(Integer::parseInt).toArray();
                        if (arr2.length == 1) {
                            if (!(arr3[0] == type1 || arr3[0] == type2)) {
                                return false;
                            }
                        } else if (arr2.length == 2) {
                            if (symmetry == '@') {
                                if (!((arr3[0] == type1 && arr3[1] == type2) || (arr3[0] == type2 && arr3[1] == type1))) {
                                    return false;
                                }
                            } else {
                                if (!((arr3[0] == type1 && arr3[1] == type2))) {
                                    return false;
                                }
                            }
                        } else {
                            if (symmetry == '@') {
                                if (!((type1 >= arr3[0] && type1 < arr3[1]) && (type2 >= arr3[2] && type2 < arr3[3]) ||
                                        (type2 >= arr3[0] && type2 < arr3[1]) && (type1 >= arr3[2] && type1 < arr3[3]))) {
                                    return false;
                                }
                            } else {
                                if (!((type1 >= arr3[0] && type1 < arr3[1]) && (type2 >= arr3[2] && type2 < arr3[3]))) {
                                    return false;
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("Ошибка ParamVariant.checkParconv() " + e);
                        return false;
                    }
                    break;
                case "011": //Для Артикула 1 указан состав
                    strTxt = paramRec.getStr(PAR3);
                    List<Record> elementList1 = eElement.find3(joinElement1.artiklRec.getInt(eArtikl.code), joinElement1.artiklRec.getInt(eArtikl.series_id));
                    boolean substr1 = false;
                    for (Record elementRec1 : elementList1) {
                        if (elementRec1.getStr(eElement.name).contains(strTxt)) {
                            ArrayList<Record> elempar1List = eElempar1.find2(elementRec1.getInt(PAR1));
                            substr1 = element(joinElement1, elempar1List);
                            break;
                        }
                    }
                    if (substr1 == false) {
                        return false;
                    }
                    break;
                case "012": //Для Артикула 2 указан состав
                    strTxt = paramRec.getStr(PAR3);
                    boolean substr2 = false;
                    List<Record> elementList2 = eElement.find3(joinElement2.artiklRec.getInt(eArtikl.code), joinElement2.artiklRec.getInt(eArtikl.series_id));
                    for (Record elementRec2 : elementList2) {
                        if (elementRec2.getStr(eElement.name).contains(strTxt)) {
                            ArrayList<Record> elempar2List = eElempar2.find2(elementRec2.getInt(PAR1));
                            substr2 = element(joinElement2, elempar2List);
                            break;
                        }
                    }
                    if (substr2 == false) {
                        return false;
                    }
                    break;
                case "013": //Для Артикулов не указан состав
                    strTxt = paramRec.getStr(PAR3);
                    List<Record> elementList1a = eElement.find3(joinElement1.artiklRec.getInt(eArtikl.code), joinElement1.artiklRec.getInt(eArtikl.series_id));
                    boolean substr1a = false;
                    for (Record elementRec : elementList1a) {
                        if (elementRec.getStr(eElement.name).contains(strTxt)) {
                            ArrayList<Record> elempar1List = eElempar1.find(elementRec.getInt(PAR1));
                            substr1a = element(joinElement1, elempar1List);
                            break;
                        }
                    }
                    boolean substr2a = false;
                    List<Record> elementList2a = eElement.find3(joinElement2.artiklRec.getInt(eArtikl.code), joinElement2.artiklRec.getInt(eArtikl.series_id));
                    for (Record elementRec : elementList2a) {
                        if (elementRec.getStr(eElement.name).contains(strTxt)) {
                            ArrayList<Record> elempar1List = eElempar1.find(elementRec.getInt(PAR1));
                            substr1a = element(joinElement1, elempar1List);
                            break;
                        }
                    }
                    if (substr1a == true || substr2a == true) {
                        return false;
                    }
                    break;
                case "020":  //Ограничение угла
                    if (Constructiv.compareFloat(paramRec.getStr(PAR3), angl) == false) return false;
                    break;
                case "030":  //Припуск Артикула1/Артикула2 , мм
                    strTxt = paramRec.getStr(PAR3);
                    char symmetry = strTxt.charAt(strTxt.length() - 1);
                    if (symmetry == '@') {
                        strTxt = strTxt.substring(0, strTxt.length() - 1);
                    }
                    String arr2[] = strTxt.split("/");
                    joinElement1.specificationRec.putParam(2030, arr2[0]);
                    joinElement2.specificationRec.putParam(2030, arr2[1]);
                    break;
                case "031":
                case "032":
                case "033":
                case "034":
                case "035":
                case "036":
                case "037":
                case "038":
                case "039":
                case "040":
                    return false; //Т. к. есть системные константы
                case "085":
                    System.out.println("надпись на элем. записать");
                    elemJoin.joinElement1.iwin().labelSketch = paramRec.getStr(PAR3);
                    break;
                case "095": //Если признак системы конструкции
                    Record systreeRec = eSystree.find(iwin.nuni);
                    String[] arrPar3 = paramRec.getStr(PAR3).split(";");
                    boolean empty = true;
                    for (int i = 0; i < arrPar3.length; i++) {
                        if (systreeRec.getInt(eSystree.types) == Integer.valueOf(arrPar3[i])) {
                            empty = false;
                        }
                    }
                    if (empty == true) {
                        return false;
                    }
                case "099":  //Трудозатраты, ч/ч.
                    elemJoin.costsJoin = paramRec.getStr(PAR3);
                    break;
                default:
                    message(paramRec.getInt(PAR1));
                    break;
            }
        }
        return result;
    }

    private void message(int code) {
        System.err.println("ParamVariant ОШИБКА! КОД " + code + " НЕ ОБРАБОТАН.");
    }

    /* Все варианты вместе
    public static int[] paramSum = {13015, 13017, 13081, 13099, 31000, 31001, 31002, 31003, 31004, 31005, 31006, 31007, 31008, 31015, 31016, 31020,
            31033, 31034, 31037, 31041, 31050, 31052, 31055, 31056, 31080, 31085, 31090, 31095, 31097, 31099, 37001, 37002, 37009, 37010, 37030,
            37042, 37056, 37080, 37085, 37099};*/
}
