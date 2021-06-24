package builder.param;

import dataset.Record;
import domain.eArtikl;
import domain.eElement;
import domain.eJoinpar1;
import domain.eJoinvar;
import domain.eSetting;
import domain.eSystree;
import enums.TypeJoin;
import java.util.List;
import builder.Wincalc;
import builder.model.ElemJoining;
import builder.model.ElemSimple;
import common.Util;
import java.util.Dictionary;
import java.util.HashMap;

//Соединения
public class JoiningVar extends Par5s {

    //Соединения
    public JoiningVar(Wincalc iwin) {
        super(iwin);
    }

    //1000 - прилегающее соединение, 2000 - угловое на ус, 3000 - угловое (левое, правое), 4000 - Т образное соединение
    public boolean filter(ElemJoining elemJoin, Record joinvarRec) {

        List<Record> paramList = eJoinpar1.find(joinvarRec.getInt(eJoinvar.id));
        if (filterParamDef(paramList) == false) {
            return false;
        }
        //цикл по параметрам элементов соединения
        for (Record rec : paramList) {
            if (check(elemJoin, rec) == false) {
                return false;
            }
        }
        return true;
    }

    public boolean check(ElemJoining elemJoin, Record rec) {

        int grup = rec.getInt(GRUP);
        try {
            switch (grup) {
                case 1005:  //Контейнер имеет тип Артикула1/Артикула2
                case 2005:  //Контейнер имеет тип Артикула1/Артикула2
                case 3005:  //Контейнер имеет тип Артикула1/Артикула2
                case 4005:  //Контейнер имеет тип Артикула1/Артикула2
                {
                    if ("ps3".equals(eSetting.find(2))) { //Контейнер Артикула 1 имеет тип
                        String[] arr = {"коробка", "створка", "импост", "стойка", "эркер"};
                        int[] index = {1, 2, 3, 5, 19};
                        for (int i = 0; i < arr.length; i++) {
                            if (arr.equals(rec.getStr(TEXT)) && Util.containsNumb(String.valueOf(index[i]), elemJoin.elem1.type().id) == false) {
                                return false;
                            }
                        }
                    } else {
                        if (Util.containsNumb(rec.getStr(TEXT), elemJoin.elem1.type().id, elemJoin.elem2.type().id) == false) {
                            return false;
                        }
                    }
                }
                break;
                case 1006:  //Контейнер Артикула 2 имеет тип
                case 2006:  //Контейнер Артикула 2 имеет тип
                case 3006:  //Контейнер Артикула 2 имеет тип
                case 4006:  //Контейнер Артикула 2 имеет тип
                    if ("ps3".equals(eSetting.find(2))) {
                        String[] arr = {"коробка", "створка", "импост", "стойка", "эркер"};
                        for (int i = 1; i < arr.length; i++) {
                            if (arr.equals(rec.getStr(TEXT)) && Util.containsNumb(String.valueOf(i), elemJoin.elem1.type().id) == false) {
                                return false;
                            }
                        }
                    }
                    break;
                case 1008:  //Эффективное заполнение изд., мм
                    if (Uti4.is_1008_31008_34008(rec.getFloat(TEXT), iwin) == false) {
                        return false;
                    }
                    break;
                case 1010:  //Внешнее соединение 
                case 4010:  //Внешнее соединение                     
                    //message(rec.getInt(GRUP)); //У SA всегда внутреннее
                    break;
                case 1011: //Для Артикула 1 указан состав 
                case 4011: //Для Артикула 1 указан состав     
                {
                    boolean substr = false;
                    List<Record> elementList = eElement.find3(elemJoin.elem1.artiklRec.getInt(eArtikl.code), elemJoin.elem1.artiklRec.getInt(eArtikl.series_id));
                    for (Record elementRec : elementList) {
                        if (elementRec.getStr(eElement.name).contains(rec.getStr(TEXT))) {
                            substr = (new ElementVar(iwin)).filter(elemJoin.elem1, elementRec);
                            break;
                        }
                    }
                    if (substr == false) {
                        return false;
                    }
                }
                break;
                case 1012: //Для Артикула 2 указан состав                  
                case 4012: //Для Артикула 2 указан состав     
                {
                    boolean substr = false;
                    List<Record> elementList = eElement.find3(elemJoin.elem2.artiklRec.getInt(eArtikl.code), elemJoin.elem2.artiklRec.getInt(eArtikl.series_id));
                    for (Record elementRec : elementList) {
                        if (elementRec.getStr(eElement.name).contains(rec.getStr(TEXT))) {
                            substr = (new ElementVar(iwin)).filter(elemJoin.elem2, elementRec);
                            break;
                        }
                    }
                    if (substr == false) {
                        return false;
                    }
                }
                break;
                case 1013:  //Для Артикулов не указан состав
                case 2013:  //Для Артикулов не указан состав 
                case 3013:  //Для Артикулов не указан состав
                case 4013:  //Для Артикулов не указан состав  
                {
                    List<Record> elementList1 = eElement.find3(elemJoin.elem1.artiklRec.getInt(eArtikl.code), elemJoin.elem1.artiklRec.getInt(eArtikl.series_id));
                    boolean substr1 = false;
                    ElementVar elementVar = new ElementVar(iwin);
                    for (Record elementRec : elementList1) {
                        if (elementRec.getStr(eElement.name).contains(rec.getStr(TEXT))) {
                            substr1 = elementVar.filter(elemJoin.elem1, elementRec);
                            break;
                        }
                    }
                    boolean substr2 = false;
                    List<Record> elementList2 = eElement.find3(elemJoin.elem2.artiklRec.getInt(eArtikl.code), elemJoin.elem2.artiklRec.getInt(eArtikl.series_id));
                    for (Record elementRec : elementList2) {
                        if (elementRec.getStr(eElement.name).contains(rec.getStr(TEXT))) {
                            substr1 = elementVar.filter(elemJoin.elem1, elementRec);
                            break;
                        }
                    }
                    if (substr1 == true || substr2 == true) {
                        return false;
                    }
                }
                break;
                case 1020:  //Ограничение угла к горизонту, °
                    if ("ps3".equals(eSetting.find(2))) { //Угол к горизонту минимальный
                        if (elemJoin.elem1.anglHoriz < rec.getFloat(TEXT)) {
                            return false;
                        }
                    } else if (Util.containsNumb(rec.getStr(TEXT), elemJoin.elem1.anglHoriz) == false) {
                        return false;
                    }
                    break;
                case 1030:  //Угол к горизонту максимальный
                    if ("ps3".equals(eSetting.find(2))) {
                        if (elemJoin.elem1.anglHoriz > rec.getFloat(TEXT)) {
                            return false;
                        }
                    }
                    break;
                case 1031:  //Точный угол к горизонту
                    if ("ps3".equals(eSetting.find(2))) {
                        if (elemJoin.elem1.anglHoriz == rec.getFloat(TEXT)) {
                            return false;
                        }
                    }
                    break;
                case 1032:  //Исключить угол к горизонту, °
                    if ("ps3".equals(eSetting.find(2))) {
                        if (elemJoin.elem1.anglHoriz == rec.getFloat(TEXT)) {
                            return false;
                        }
                    }
                    break;
                case 1035:  //Уровень створки 
                    message(rec.getInt(GRUP));
                    break;
                case 1039:  //Для типа открывания 
                    message(rec.getInt(GRUP));
                    break;
                case 1040:  //Размер, мм (Смещение осей рамы и створки. Наследие ps3)
                    //Параметр вычисляктся на раннем этапе см. конструктор AreaStvorka()
                    //Применяется если сист. константы отсутствуют
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
                    elemJoin.costsJoin = rec.getStr(TEXT);
                    break;
                case 1097:  //Трудозатраты по длине 
                    message(rec.getInt(GRUP));
                    break;
                case 1085:  //Надпись на элементе 
                case 2085:  //Надпись на элементе 
                case 3085:  //Надпись на элементе  
                case 4085:  //Надпись на элементе     
                    elemJoin.elem1.iwin().labelSketch = rec.getStr(TEXT);
                    break;
                case 2003:  //Угол варианта 
                    message(rec.getInt(GRUP));
                    break;
                case 2010:  //Угол минимальный, °
                case 3010:  //Угол минимальный, °
                case 4020:  //Ограничение угла, ° или Угол минимальный, ° для ps3 
                    if ("ps3".equals(eSetting.find(2))) {
                        if (rec.getFloat(TEXT) < elemJoin.anglProf) {
                            return false;
                        }
                    } else if (Util.containsNumb(rec.getStr(TEXT), elemJoin.anglProf) == false) {
                        return false;
                    }
                    break;
                case 2012: //Для Артикулов указан состав
                case 3012: //Для Артикулов указан состав 
                {
                    List<Record> elementList1 = eElement.find3(elemJoin.elem1.artiklRec.getInt(eArtikl.code), elemJoin.elem1.artiklRec.getInt(eArtikl.series_id));
                    boolean substr1 = false;
                    ElementVar elementVar = new ElementVar(iwin);
                    for (Record elementRec : elementList1) {
                        if (elementRec.getStr(eElement.name).contains(rec.getStr(TEXT))) {
                            substr1 = elementVar.filter(elemJoin.elem1, elementRec);
                            break;
                        }
                    }
                    boolean substr2 = false;
                    List<Record> elementList2 = eElement.find3(elemJoin.elem2.artiklRec.getInt(eArtikl.code), elemJoin.elem2.artiklRec.getInt(eArtikl.series_id));
                    for (Record elementRec : elementList2) {
                        if (elementRec.getStr(eElement.name).contains(rec.getStr(TEXT))) {
                            substr1 = elementVar.filter(elemJoin.elem1, elementRec);
                            break;
                        }
                    }
                    if (substr1 == true || substr2 == true) {
                        return false;
                    }
                }
                break;
                case 2015:  //Ориентация Артикула1/Артикула2, ° 
                case 3015:  //Ориентация Артикула1/Артикула2, °
                case 4015:  //Ориентация Артикула1/Артикула2, °
                    if ("ps3".equals(eSetting.find(2))) { //Ориентация Артикула 1
                        List<String> list = ParamList.find(grup).dict();
                        if ("горизонтально".equals(rec.getStr(TEXT)) && (elemJoin.elem1.anglHoriz == 0 || elemJoin.elem1.anglHoriz == 180) == false) { //горизонтально
                            return false;
                        } else if ("вертикально".equals(rec.getStr(TEXT)) && (elemJoin.elem1.anglHoriz == 90 || elemJoin.elem1.anglHoriz == 270) == false) { //горизонтально
                            return false;
                        }
                    } else {
                        if (Util.containsNumb(rec.getStr(TEXT), elemJoin.elem1.anglHoriz, elemJoin.elem2.anglHoriz) == false) {
                            return false;
                        }
                    }
                    break;
                case 2016:  //Ориентация Артикула 2 
                case 3016:
                case 4016:
                    if ("ps3".equals(eSetting.find(2))) {
                        List<String> list = ParamList.find(grup).dict();
                        if ("горизонтально".equals(rec.getStr(TEXT)) && (elemJoin.elem2.anglHoriz == 0 || elemJoin.elem2.anglHoriz == 180) == false) { //горизонтально
                            return false;
                        } else if ("вертикально".equals(rec.getStr(TEXT)) && (elemJoin.elem2.anglHoriz == 90 || elemJoin.elem2.anglHoriz == 270) == false) { //горизонтально
                            return false;
                        }
                    }
                    break;
                case 2020:  //Ограничение угла, °  или Угол максимальный, ° для ps3 
                case 3020:  //Ограничение угла, °  или Угол максимальный, ° для ps3 
                case 4030: // Угол максимальный, °                      
                    if ("ps3".equals(eSetting.find(2))) {
                        if (elemJoin.anglProf > rec.getFloat(TEXT)) {
                            return false;
                        }
                    } else if (Util.containsNumb(rec.getStr(TEXT), elemJoin.anglProf) == false) {
                        return false;
                    }
                    break;
                case 2021: //Точный угол, °
                case 3021:
                case 4031:
                    if ("ps3".equals(eSetting.find(2))) {
                        if (rec.getFloat(TEXT) != elemJoin.anglProf) {
                            return false;
                        }
                    }
                    break;
                case 2022: //Исключить угол, °
                case 3022:
                case 4032:
                    if ("ps3".equals(eSetting.find(2))) {
                        if (rec.getFloat(TEXT) == elemJoin.anglProf) {
                            return false;
                        }
                    }
                    break;
                case 2030: //Припуск Артикула1/Артикула2 , мм 
                {
                    String strTxt = rec.getStr(TEXT);
                    char symmetry = strTxt.charAt(strTxt.length() - 1);
                    if (symmetry == '@') {
                        strTxt = strTxt.substring(0, strTxt.length() - 1);
                    }
                    String arr2[] = strTxt.split("/");
                    elemJoin.elem1.spcRec.mapParam.put(2030, arr2[0]);
                    elemJoin.elem2.spcRec.mapParam.put(2030, arr2[1]);
                }
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
                    if (elemJoin.typeJoin == TypeJoin.VAR40 && "Простое Т-обр.".equalsIgnoreCase(rec.getStr(TEXT)) == false) {
                        return false;
                    }
                    break;
                case 3003:  //Угол варианта 
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
                case 4018:  //От ручки не менее, мм 
                    message(rec.getInt(GRUP));
                    break;
                case 4040: //Размер от оси профиля, мм. или заход импоста
                    elemJoin.elem1.spcRec.width += -rec.getFloat(TEXT);
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
                case 4095: //Если признак системы конструкции 
                {
                    Record record = eSystree.find(iwin.nuni);
                    if (Util.containsNumb(rec.getStr(TEXT), record.getInt(eSystree.types)) == false) {
                        return false;
                    }
                }
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
                    assert !(grup > 0 && grup < 50000) : "Код " + grup + "  не обработан!!!";
            }
        } catch (Exception e) {
            System.err.println("Ошибка:JoiningVar.check()  parametr=" + grup + "    " + e);
            return false;
        }
        return true;
    }
}
