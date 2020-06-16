package estimate.constr.param;

import dataset.Record;
import domain.eArtikl;
import domain.eElement;
import domain.eElempar1;
import domain.eElempar2;
import domain.eSetting;
import enums.TypeJoin;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import estimate.Wincalc;
import static estimate.constr.Cal5e.compareFloat;
import estimate.model.ElemJoining;
import estimate.model.ElemSimple;
import startup.Main;

//Соединения
public class JoiningVar extends Par5s {

    private int[] parConv = {1005, 1008, 1010, 1011, 1012, 1013, 1020, 1040, 1085, 1099, 2005, 2012, 2013, 2020, 2030, 2061, 2099, 3002, 3003, 3005, 3015, 3020, 3031, 3050, 3099, 4002, 4005, 4011, 4012, 4013, 4015, 4018, 4020, 4040, 4044, 4085, 4095, 4099};

    //Соединения
    public JoiningVar(Wincalc iwin) {
        super(iwin);
    }

    //1000 - прилегающее соединение, 2000 - угловое на ус, 3000 - угловое (левое, правое), 4000 - Т образное соединение
    public boolean check(ElemJoining elemJoin, List<Record> paramList) {

        float angl = (ElemSimple.SIDE_START == ElemJoining.FIRST_SIDE) ? elemJoin.joinAngl(1) : elemJoin.joinAngl(2);
        ElemSimple joinElement1 = elemJoin.joinElement1;
        ElemSimple joinElement2 = elemJoin.joinElement2;
        boolean result = true;
        String strTxt = "";
        //цикл по параметрам элементов соединения
        for (Record rec : paramList) {

            int grup = rec.getInt(GRUP);
            try {
                switch (grup) {
                    case 0:  //Зрачение по умолчанию 
                        message(rec.getInt(GRUP));
                        break;
                    case 1005:  //Контейнер имеет тип Артикула1/Артикула2
                    case 2005:  //Контейнер имеет тип Артикула1/Артикула2  
                    case 3005:  //Контейнер имеет тип Артикула1/Артикула2 
                    case 4005:  //Контейнер имеет тип Артикула1/Артикула2    
                    try {
                        strTxt = rec.getStr(TEXT);
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
                                if (!((type1 >= arr3[0] && type1 < arr3[1]) && (type2 >= arr3[2] && type2 < arr3[3])
                                        || (type2 >= arr3[0] && type2 < arr3[1]) && (type1 >= arr3[2] && type1 < arr3[3]))) {
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
                    case 1008:  //Эффективное заполнение изд., мм
                        message(rec.getInt(GRUP));
                        break;
                    case 1010:  //Внешнее соединение 
                        message(rec.getInt(GRUP));
                        break;
                    case 1011:  //Для Артикула 1 указан состав 
                    case 4011:  //Для Артикула 1 указан состав     
                        strTxt = rec.getStr(TEXT);
                        List<Record> elementList1 = eElement.find3(joinElement1.artiklRec.getInt(eArtikl.code), joinElement1.artiklRec.getInt(eArtikl.series_id));
                        boolean substr1 = false;
                        for (Record elementRec1 : elementList1) {
                            if (elementRec1.getStr(eElement.name).contains(strTxt)) {
                                ArrayList<Record> elempar1List = eElempar1.find2(elementRec1.getInt(GRUP));
                                substr1 = (new ElementVar(iwin)).check(joinElement1, elempar1List);
                                break;
                            }
                        }
                        if (substr1 == false) {
                            return false;
                        }
                        break;
                    case 1012:  //Для Артикула 2 указан состав                  
                    case 4012:  //Для Артикула 2 указан состав     
                        strTxt = rec.getStr(TEXT);
                        boolean substr2 = false;
                        List<Record> elementList2 = eElement.find3(joinElement2.artiklRec.getInt(eArtikl.code), joinElement2.artiklRec.getInt(eArtikl.series_id));
                        for (Record elementRec2 : elementList2) {
                            if (elementRec2.getStr(eElement.name).contains(strTxt)) {
                                ArrayList<Record> elempar2List = eElempar2.find2(elementRec2.getInt(GRUP));
                                substr2 = (new ElementVar(iwin)).check(joinElement2, elempar2List);
                                break;
                            }
                        }
                        if (substr2 == false) {
                            return false;
                        }
                        break;
                    case 1013:  //Для Артикулов не указан состав
                    case 2013:  //Для Артикулов не указан состав 
                    case 3013:  //Для Артикулов не указан состав
                    case 4013:  //Для Артикулов не указан состав  
                        strTxt = rec.getStr(TEXT);
                        List<Record> elementList1a = eElement.find3(joinElement1.artiklRec.getInt(eArtikl.code), joinElement1.artiklRec.getInt(eArtikl.series_id));
                        boolean substr1a = false;
                        ElementVar elementVar = new ElementVar(iwin);
                        for (Record elementRec : elementList1a) {
                            if (elementRec.getStr(eElement.name).contains(strTxt)) {
                                ArrayList<Record> elempar1List = eElempar1.find(elementRec.getInt(GRUP));
                                substr1a = elementVar.check(joinElement1, elempar1List);
                                break;
                            }
                        }
                        boolean substr2a = false;
                        List<Record> elementList2a = eElement.find3(joinElement2.artiklRec.getInt(eArtikl.code), joinElement2.artiklRec.getInt(eArtikl.series_id));
                        for (Record elementRec : elementList2a) {
                            if (elementRec.getStr(eElement.name).contains(strTxt)) {
                                ArrayList<Record> elempar1List = eElempar1.find(elementRec.getInt(GRUP));
                                substr1a = elementVar.check(joinElement1, elempar1List);
                                break;
                            }
                        }
                        if (substr1a == true || substr2a == true) {
                            return false;
                        }
                        break;
                    case 1020:  //Ограничение угла к горизонту, °
                        message(rec.getInt(GRUP));
                        break;
                    case 1035:  //Уровень створки 
                        message(rec.getInt(GRUP));
                        break;
                    case 1039:  //Для типа открывания 
                        message(rec.getInt(GRUP));
                        break;
                    case 1040:  //Размер, мм 
                        message(rec.getInt(GRUP));
                        //return false; //Т. к. есть системные константы
                        break;
                    case 1043:  //Ограничение габарита контура, мм 
                        message(rec.getInt(GRUP));
                        break;
                    case 1090:  //Смещение по толщине, мм 
                        message(rec.getInt(GRUP));
                        break;
                    case 1095:  //Если признак системы конструкции 
                        message(rec.getInt(GRUP));
                        break;
                    case 1098:  //Бригада, участок) 
                        message(rec.getInt(GRUP));
                        break;
                    case 1099:  //Трудозатраты, ч/ч. 
                    case 2099:  //Трудозатраты, ч/ч. 
                    case 3099:  //Трудозатраты, ч/ч.
                    case 4099:  //Трудозатраты, ч/ч. 
                        message(grup, rec.getStr(TEXT));
                        elemJoin.costsJoin = rec.getStr(TEXT);
                        break;
                    case 1097:  //Трудозатраты по длине 
                        message(rec.getInt(GRUP));
                        break;
                    case 1085:  //Надпись на элементе 
                    case 2085:  //Надпись на элементе 
                    case 3085:  //Надпись на элементе  
                    case 4085:  //Надпись на элементе     
                        elemJoin.joinElement1.iwin().labelSketch = rec.getStr(TEXT);
                        break;
                    case 2003:  //Угол варианта 
                        message(rec.getInt(GRUP));
                        break;
                    case 2012:  //Для Артикулов указан состав
                        message(rec.getInt(GRUP));
                        break;
                    case 2015:  //Ориентация Артикула1/Артикула2, ° 
                        message(rec.getInt(GRUP));
                        break;
                    case 2020:  //Ограничение угла, ° 
                    case 3020:  //Ограничение угла, ° 
                    case 4020:  //Ограничение угла, ° или Угол минимальный, ° для ps3 
                        if ("ps3".equals(eSetting.find(2).getStr(eSetting.val))) {
                            if (parserFloat(rec.getStr(TEXT))[0] < angl) {
                                return false;
                            }
                        }
                        if (compareBetween(rec.getStr(TEXT), (int) angl) == false) {
                            return false;
                        }
                        break;
                    case 2030:  //Припуск Артикула1/Артикула2 , мм 
                        strTxt = rec.getStr(TEXT);
                        char symmetry = strTxt.charAt(strTxt.length() - 1);
                        if (symmetry == '@') {
                            strTxt = strTxt.substring(0, strTxt.length() - 1);
                        }
                        String arr2[] = strTxt.split("/");
                        joinElement1.specificationRec.putParam(2030, arr2[0]);
                        joinElement2.specificationRec.putParam(2030, arr2[1]);
                        break;
                    case 2055:  //Продолжение общей арки 
                        message(rec.getInt(GRUP));
                        break;
                    case 2061:  //Отступ для Артикула1/Артикула2, мм 
                        message(rec.getInt(GRUP));
                        break;
                    case 2066:  //Расчет углов реза профилей 
                        message(rec.getInt(GRUP));
                        break;
                    case 2064:  //Поправка для состава Арт.1/Арт.2, мм 
                        message(rec.getInt(GRUP));
                        break;
                    case 2095:  //Если признак системы конструкции 
                        message(rec.getInt(GRUP));
                        break;
                    case 2098:  //Бригада, участок) 
                        message(rec.getInt(GRUP));
                        break;
                    case 2097:  //Трудозатраты по длине 
                        message(rec.getInt(GRUP));
                        break;
                    case 3002:  //Вид Т-образного варианта (простое Т-обр. крестовое Т-обр. сложное Y-обр.) 
                    case 4002:  //Вид Т-образного варианта (простое Т-обр. крестовое Т-обр. сложное Y-обр.)     
                        if (elemJoin.typeJoin == TypeJoin.VAR40 && "Простое Т-обр.".equals(rec.getStr(TEXT)) == false) {
                            return false;
                        }
                        break;
                    case 3003:  //Угол варианта 
                        message(rec.getInt(GRUP));
                        break;
                    case 3012:  //Для Артикулов указан состав 
                        message(rec.getInt(GRUP));
                        break;
                    case 3015:  //Ориентация Артикула1/Артикула2, ° 
                        message(rec.getInt(GRUP));
                        break;
                    case 3030:  //Усечение Артикула1/Артикула2, мм 
                        message(rec.getInt(GRUP));
                        break;
                    case 3031:  //Усечение Артикула1/Артикула2, мм 
                        message(rec.getInt(GRUP));
                        break;
                    case 3045:  //Расстояние от уровня деления, мм 
                        message(rec.getInt(GRUP));
                        break;
                    case 3050:  //Припуск Артикула1/Артикула2, мм 
                        message(rec.getInt(GRUP));
                        break;
                    case 3064:  //Поправка для состава Арт.1/Арт.2 , мм 
                        message(rec.getInt(GRUP));
                        break;
                    case 3083:  //Проходит уровень деления 
                        message(rec.getInt(GRUP));
                        break;
                    case 3088:  //Вариант соединения для стойки 
                        message(rec.getInt(GRUP));
                        break;
                    case 3095:  //Если признак системы конструкции 
                        message(rec.getInt(GRUP));
                        break;
                    case 3098:  //Бригада, участок) 
                        message(rec.getInt(GRUP));
                        break;
                    case 3097:  //Трудозатраты по длине 
                        message(rec.getInt(GRUP));
                        break;
                    case 4010:  //Внешнее соединение 
                        message(rec.getInt(GRUP));
                        break;
                    case 4015:  //Ориентация Артикула1/Артикула2, ° 
                        message(rec.getInt(GRUP));
                        break;
                    case 4018:  //От ручки не менее, мм 
                        message(rec.getInt(GRUP));
                        break;
                    case 4030: // Угол максимальный, °
                        if (parserFloat(rec.getStr(TEXT))[0] > angl) {
                            return false;
                        }
                        break;
                    case 4040:  //Размер от оси профиля, мм 
                        message(rec.getInt(GRUP));
                        break;
                    case 4044:  //Размер от края пакета, мм 
                        message(rec.getInt(GRUP));
                        break;
                    case 4045:  //Расстояние от уровня деления, мм 
                        message(rec.getInt(GRUP));
                        break;
                    case 4046:  //Длина Артикула 1, мм 
                        message(rec.getInt(GRUP));
                        break;
                    case 4050:  //Припуск Артикула 1, мм. 
                        message(rec.getInt(GRUP));
                        break;
                    case 4061:  //Максимальный размер шва, мм 
                        message(rec.getInt(GRUP));
                        break;
                    case 4064:  //Поправка для состава, мм 
                        message(rec.getInt(GRUP));
                        break;
                    case 4083:  //Проходит уровень деления 
                        message(rec.getInt(GRUP));
                        break;
                    case 4095:  //Если признак системы конструкции 
                        message(rec.getInt(GRUP));
                        break;
                    case 4097:  //Трудозатраты по длине 
                        message(rec.getInt(GRUP));
                        break;
                    case 4098:  //Бригада, участок) 
                        message(rec.getInt(GRUP));
                        break;
                    case 4800:  //Код обработки 
                        message(rec.getInt(GRUP));
                        break;
                    case 4801:  //Доп.обработки
                        message(rec.getInt(GRUP));
                        break;
                    default:
                        message(rec.getInt(GRUP));
                        break;
                }
            } catch (Exception e) {
                System.err.println("wincalc.constr.param.JoiningVar.check()  parametr=" + grup + "    " + e);
                return false;
            }
        }
        return result;
    }
}
