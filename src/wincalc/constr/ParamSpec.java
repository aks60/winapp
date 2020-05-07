package wincalc.constr;

import dataset.Record;
import domain.eArtikl;
import domain.eSysprof;
import domain.eSystree;
import enums.LayoutArea;
import enums.ParamJson;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import wincalc.Wincalc;
import static wincalc.constr.ParamSpecific.PAR1;
import static wincalc.constr.ParamSpecific.PAR3;
import wincalc.model.Com5t;
import wincalc.model.ElemSimple;

/**
 * Перечень параметров спецификаций (составов, заполнений...) Параметры нижней
 * части формы конструктива (составов, заполнений...)
 */
public class ParamSpec {

    protected Wincalc iwin = null;
    public String sideCheck = ""; //TODO Эту переменную надо вынести в map параметров!!!
    public static final int PAR1 = 3;   //Ключ 1  
    public static final int PAR2 = 4;   //Ключ 2   
    public static final int PAR3 = 5;   //Значение 
    protected Constructiv calcConstr = null;

    public int pass = 1; //pass=1 ищем тех что попали, pass=2 основной цикл, pass=3 находим доступные параметры

    public ParamSpec(Wincalc iwin, Constructiv calcConstr) {
        this.iwin = iwin;
        this.calcConstr = calcConstr;
    }

    //Фильтр параметров по умолчанию и i-okna
    protected boolean filterParamJson(Com5t com5t, List<Record> paramList) {

        HashMap<Integer, Object[]> paramJson = new HashMap();
        HashMap<Integer, Object[]> paramTotal = new HashMap();
        paramTotal.putAll(iwin.mapParamDef); //добавим параметры по умолчанию

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
            HashMap<Integer, Object[]> pJson = (HashMap) el.mapParam.get(ParamJson.pro4Params2);
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
        }
        for (Record paramRec : paramList) {
            if (paramRec.getInt(PAR1) < 0) {

                if (paramTotal.get(paramRec.getInt(PAR1)) == null) {
                    return false; //усли в базе парам. нет, сразу выход
                }
                //В данной ветке есть попадание в paramRec.getInt(PAR1)
                Object[] totalVal = paramTotal.get(paramRec.getInt(PAR1));
                if (totalVal[1].equals(paramRec.getInt(PAR2)) == false) { //если в param.znumb() попадания нет

                    //на третьей итерации дополняю ...
                    return false;

                } else if (paramJson != null && paramJson.isEmpty() == false && paramJson.get(paramRec.getInt(PAR1)) != null) {
                    totalVal[2] = 1; //если попадание было, то записываю 1 в третий элемент массива
                }
            }
        }
        return true;
    }

    //Составы
    protected boolean elements(HashMap<Integer, String> hmParam, ElemSimple elemSimple, List<Record> tableList) {

        //Цикл по параметрам составов
        for (Record paramRec : tableList) {
            switch (paramRec.getInt(PAR1)) {

                case 33000:  //Для технологического кода контейнера 
                case 34000:  //Для технологического кода контейнера    
                    Record sysprofRec = elemSimple.sysprofRec;
                    Record artiklVRec = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), false);
                    if (artiklVRec.get(eArtikl.tech_code) == null) {
                        return false;
                    }
                    String[] strList = paramRec.getStr(PAR3).split(";");
                    String[] strList2 = artiklVRec.getStr(eArtikl.tech_code).split(";");
                    boolean ret2 = false;
                    for (String str : strList) {
                        for (String str2 : strList2) {
                            if (str.equals(str2)) {
                                ret2 = true;
                            }
                        }
                    }
                    if (ret2 == false) {
                        return false;
                    }
                    break;
                case 33001:  //Если признак состава 
                    message(paramRec.getInt(PAR1));
                    break;
                case 33002:  //Расчет пролетов соединений 
                    message(paramRec.getInt(PAR1));
                    break;
                case 33003:  //Расчет пролетов заполнений, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 33004:  //Расчет от длины профиля стойки 
                    message(paramRec.getInt(PAR1));
                    break;
                case 33005:  //Коды основной текстуры контейнера 
                case 34005:  //Коды основной текстуры контейнера
                    int m1 = elemSimple.iwin().color1;
                    if (Constructiv.compareInt(paramRec.getStr(PAR3), m1) == false) {
                        return false;
                    }
                    break;
                case 33006:  //Коды внутр. текстуры контейнера
                case 34006:  //Коды внутр. текстуры контейнера 
                    int m2 = elemSimple.iwin().color2;
                    if (Constructiv.compareInt(paramRec.getStr(PAR3), m2) == false) {
                        return false;
                    }
                    break;
                case 33007:  //Коды внешн. текстуры контейнера 
                case 34007:  //Коды внешн. текстуры контейнера     
                    int m3 = elemSimple.iwin().color3;
                    if (Constructiv.compareInt(paramRec.getStr(PAR3), m3) == false) {
                        return false;
                    }
                    break;
                case 33008:  //Эффективное заполнение изд., мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 33011:  //Толщина внешнего/внутреннего заполнения, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 33017:  //Код системы содержит строку 
                    message(paramRec.getInt(PAR1));
                    break;
                case 33030:  //Количество 
                    message(paramRec.getInt(PAR1));
                    break;
                case 33031:  //Расчет количества 
                    message(paramRec.getInt(PAR1));
                    break;
                case 33032:  //Периметр покраски по периметру 
                    message(paramRec.getInt(PAR1));
                    break;
                case 33033:  //Расход по профилю 
                    message(paramRec.getInt(PAR1));
                    break;
                case 33034:  //Периметр покраски, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 33035:  //Расход по поверхности на кв.м. 
                    message(paramRec.getInt(PAR1));
                    break;
                case 33036:  //Коэффициент_расхода 
                    message(paramRec.getInt(PAR1));
                    break;
                case 33040:  //Порог расчета, мм 
                    hmParam.put(paramRec.getInt(PAR1), paramRec.getStr(PAR3));
                    break;
                case 33050:  //Шаг, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 33060:  //Количество на шаг 
                    message(paramRec.getInt(PAR1));
                    break;
                case 33062:  //Если стойка удлинена 
                    message(paramRec.getInt(PAR1));
                    break;
                case 33063:  //Диапазон веса створки, кг 
                    message(paramRec.getInt(PAR1));
                    break;
                case 33066:  //Если номер стороны в контуре 
                    message(paramRec.getInt(PAR1));
                    break;
                case 33067:  //Коды основной текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 33068:  //Коды внутр. текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 33069:  //Коды внешн. текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 33071:  //Контейнер типа 
                    message(paramRec.getInt(PAR1));
                    break;
                case 33074:  //На прилегающей створке 
                    message(paramRec.getInt(PAR1));
                    break;
                case 33078:  //Ставить однократно 
                    message(paramRec.getInt(PAR1));
                    break;
                case 33081:  //Для внешнего/внутреннего угла плоскости, ° 
                    message(paramRec.getInt(PAR1));
                    break;
                case 33073:  //Отправочная марка фасада 
                    message(paramRec.getInt(PAR1));
                    break;
                case 33095:  //Если признак системы конструкции 
                    message(paramRec.getInt(PAR1));
                    break;
                case 33099:  //Трудозатраты, ч/ч. 
                    message(paramRec.getInt(PAR1));
                    break;
//                case 34000:  //Для технологического кода контейнера 
//                    message(paramRec.getInt(PAR1));
//                    break;
                case 34001:  //Если признак состава 
                    message(paramRec.getInt(PAR1));
                    break;
                case 34002:  //Расчет пролетов соединений 
                    message(paramRec.getInt(PAR1));
                    break;
                case 34003:  //Расчет пролетов заполнений, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 34004:  //Расчет от длины профиля стойки 
                    message(paramRec.getInt(PAR1));
                    break;
//                case 34005:  //Коды основной текстуры контейнера 
//                    message(paramRec.getInt(PAR1));
//                    break;
//                case 34006:  //Коды внутр. текстуры контейнера 
//                    message(paramRec.getInt(PAR1));
//                    break;
//                case 34007:  //Коды внешн. текстуры контейнера 
//                    message(paramRec.getInt(PAR1));
//                    break;
                case 34010:  //Расчет армирования 
                    hmParam.put(paramRec.getInt(PAR1), paramRec.getStr(PAR3));
                    break;
                case 34012:  //Для варианта соединения Т*
                    message(paramRec.getInt(PAR1));
                    break;
                case 34009:  //Если два присоединенных артикула 
                    message(paramRec.getInt(PAR1));
                    break;
                case 34008:  //Эффективное заполнение изделия, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 34011:  //Толщина внешнего/внутреннего заполнения, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 34013:  //Подбор дистанционных вставок по пролетам 
                    message(paramRec.getInt(PAR1));
                    break;
                case 34015:  //Расчет длины по 
                    message(paramRec.getInt(PAR1));
                    break;
                case 34016:  //Прилегание контура створки 
                    message(paramRec.getInt(PAR1));
                    break;
                case 34017:  //Код системы содержит строку 
                    message(paramRec.getInt(PAR1));
                    break;
                case 34030:  //[ * коэф-т ] 
                    hmParam.put(paramRec.getInt(PAR1), paramRec.getStr(PAR3));
                    break;
                case 34049:  //Поправка по нормали от начала/конца, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 34050:  //Поправка, мм 
                    hmParam.put(paramRec.getInt(PAR1), paramRec.getStr(PAR3));
                    break;
                case 34051:  //Поправка, мм 
                    if (elemSimple.specificationRec.getParam("0", 31052).equals(paramRec.getStr(PAR3)) == false) {
                        hmParam.put(paramRec.getInt(PAR1), paramRec.getStr(PAR3));
                    }
                    break;
                case 34052:  //Поправка не прямого угла импоста, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 34060:  //Количество 
                    hmParam.put(paramRec.getInt(PAR1), paramRec.getStr(PAR3));
                    break;
                case 34061:  //Расчет количества 
                    message(paramRec.getInt(PAR1));
                    break;
                case 34062:  //Если стойка удлинена 
                    message(paramRec.getInt(PAR1));
                    break;
                case 34063:  //Диапазон веса створки, кг 
                    message(paramRec.getInt(PAR1));
                    break;
                case 34064:  //Учёт поправок соединений для составов 
                    message(paramRec.getInt(PAR1));
                    break;
                case 34066:  //Если номер стороны в контуре 
                    if ("1".equals(paramRec.getStr(PAR3)) == true && LayoutArea.BOTTOM != elemSimple.layout()) {
                        return false;
                    } else if ("2".equals(paramRec.getStr(PAR3)) == true && LayoutArea.RIGHT != elemSimple.layout()) {
                        return false;
                    } else if ("3".equals(paramRec.getStr(PAR3)) == true && LayoutArea.TOP != elemSimple.layout()) {
                        return false;
                    } else if ("4".equals(paramRec.getStr(PAR3)) == true && LayoutArea.LEFT != elemSimple.layout()) {
                        return false;
                    }
                    break;
                case 34067:  //Коды основной текстуры изделия 
                    int c1 = elemSimple.iwin().color1;
                    if (Constructiv.compareInt(paramRec.getStr(PAR3), c1) == false) {
                        return false;
                    }
                    break;
                case 34068:  //Коды внутр. текстуры изделия 
                    int c2 = elemSimple.iwin().color2;
                    if (Constructiv.compareInt(paramRec.getStr(PAR3), c2) == false) {
                        return false;
                    }
                    break;
                case 34069:  //Коды внешн. текстуры изделия 
                    int c3 = elemSimple.iwin().color3;
                    if (Constructiv.compareInt(paramRec.getStr(PAR3), c3) == false) {
                        return false;
                    }
                    break;
                case 34070:  //Длина, мм 
                    hmParam.put(paramRec.getInt(PAR1), paramRec.getStr(PAR3));
                    break;
                case 34071:  //Контейнер типа 
                    message(paramRec.getInt(PAR1));
                    break;
                case 34072:  //Смещение от уровня деления, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 34073:  //Отправочная марка фасада 
                    message(paramRec.getInt(PAR1));
                    break;
                case 34074:  //На прилегающей створке 
                    message(paramRec.getInt(PAR1));
                    break;
                case 34075:  //Углы реза 
                    message(paramRec.getInt(PAR1));
                    break;
                case 34077:  //Задать Угол_реза_1/Угол_реза_2, ° 
                    message(paramRec.getInt(PAR1));
                    break;
                case 34078:  //Ставить однократно 
                    message(paramRec.getInt(PAR1));
                    break;
                case 34079:  //Длина подбирается из списка, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 34080:  //Длина округляется с шагом, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 34081:  //Для внешнего/внутреннего угла плоскости, ° 
                    message(paramRec.getInt(PAR1));
                    break;
                case 34095:  //Если признак системы конструкции 
                    Record systreefRec = eSystree.find(iwin.nuni);
                    String[] arr = paramRec.getStr(PAR3).split(";");
                    List<String> arrList = Arrays.asList(arr);
                    boolean ret = false;
                    for (String str : arrList) {
                        if (systreefRec.getInt(eSystree.types) == Integer.valueOf(str) == true) {
                            ret = true;
                        }
                    }
                    if (ret == false) {
                        return false;
                    }
                    break;
                case 34097:  //Трудозатраты по длине 
                    message(paramRec.getInt(PAR1));
                    break;
                case 34099:  //Трудозатраты, ч/ч. 
                    hmParam.put(paramRec.getInt(PAR1), paramRec.getStr(PAR3));
                    break;
                case 38004:  //Расчет 
                    message(paramRec.getInt(PAR1));
                    break;
                case 38010:  //Номер стороны 
                    message(paramRec.getInt(PAR1));
                    break;
                case 38017:  //Код системы содержит строку 
                    message(paramRec.getInt(PAR1));
                    break;
                case 38030:  //Количество 
                    message(paramRec.getInt(PAR1));
                    break;
                case 38037:  //Название фурнитуры содержит 
                    message(paramRec.getInt(PAR1));
                    break;
                case 38039:  //Для типа открывания 
                    message(paramRec.getInt(PAR1));
                    break;
                case 38040:  //Порог расчета, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 38050:  //Шаг, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 38060:  //Количество на шаг 
                    message(paramRec.getInt(PAR1));
                    break;
                case 38067:  //Коды основной текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 38068:  //Коды внутр. текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 38069:  //Коды внешн. текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 38081:  //Если артикул профиля контура 
                    message(paramRec.getInt(PAR1));
                    break;
                case 38095:  //Если признак системы конструкции 
                    message(paramRec.getInt(PAR1));
                    break;
                case 38099:  //Трудозатраты, ч/ч. 
                    message(paramRec.getInt(PAR1));
                    break;
                case 38108:  //Применять коэффициенты АКЦИИ для МЦ 
                    message(paramRec.getInt(PAR1));
                    break;
                case 38109:  //Возможное управление жалюзи 
                    message(paramRec.getInt(PAR1));
                    break;
                case 38113:  //Установить текстуру по 
                    message(paramRec.getInt(PAR1));
                    break;
                case 39002:  //Номер стороны 
                    message(paramRec.getInt(PAR1));
                    break;
                case 39005:  //Расчет 
                    message(paramRec.getInt(PAR1));
                    break;
                case 39017:  //Код системы содержит строку 
                    message(paramRec.getInt(PAR1));
                    break;
                case 39020:  //Поправка, мм 
                    hmParam.put(paramRec.getInt(PAR1), paramRec.getStr(PAR3));
                    break;
                case 39030:  //[ * коэф-т ] 
                    message(paramRec.getInt(PAR1));
                    break;
                case 39037:  //Название фурнитуры содержит 
                    message(paramRec.getInt(PAR1));
                    break;
                case 39039:  //Для типа открывания 
                    message(paramRec.getInt(PAR1));
                    break;
                case 39060:  //Количество 
                    message(paramRec.getInt(PAR1));
                    break;
                case 39063:  //Округлять количество до ближайшего 
                    message(paramRec.getInt(PAR1));
                    break;
                case 39067:  //Коды основной текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 39068:  //Коды внутр. текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 39069:  //Коды внешн. текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 39070:  //Длина, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 39075:  //Углы реза 
                    message(paramRec.getInt(PAR1));
                    break;
                case 39077:  //Задать Угол_реза_1/Угол_реза_2, ° 
                    message(paramRec.getInt(PAR1));
                    break;
                case 39080:  //Шаг вагонки ламели, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 39081:  //Если артикул профиля контура 
                    message(paramRec.getInt(PAR1));
                    break;
                case 39093:  //Поперечину ставить : 
                    message(paramRec.getInt(PAR1));
                    break;
                case 39095:  //Если признак системы конструкции 
                    message(paramRec.getInt(PAR1));
                    break;
                case 39097:  //Трудозатраты по периметру 
                    message(paramRec.getInt(PAR1));
                    break;
                case 39099:  //Трудозатраты, ч/ч. 
                    message(paramRec.getInt(PAR1));
                    break;
                case 39108:  //Применять коэффициенты АКЦИИ для МЦ 
                    message(paramRec.getInt(PAR1));
                    break;
                case 39109:  //Возможное управление жалюзи 
                    message(paramRec.getInt(PAR1));
                    break;
                case 39113:  //Установить текстуру по 
                    message(paramRec.getInt(PAR1));
                    break;
                case 40004:  //Ширина заполнения, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 40005:  //Поправка ширины/высоты, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 40006:  //Высота заполнения, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 40007:  //Высоту сделать длиной 
                    message(paramRec.getInt(PAR1));
                    break;
                case 40008:  //Эффективное заполнение изд., мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 40010:  //Поправка на стороны четные/нечетные, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 40017:  //Код системы содержит строку 
                    message(paramRec.getInt(PAR1));
                    break;
                case 40037:  //Название фурнитуры содержит 
                    message(paramRec.getInt(PAR1));
                    break;
                case 40067:  //Коды основной текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 40068:  //Коды внутр. текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 40069:  //Коды внешн. текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 40095:  //Если признак системы конструкции 
                    message(paramRec.getInt(PAR1));
                    break;
                case 40108:  //Применять коэффициенты АКЦИИ для МЦ 
                    message(paramRec.getInt(PAR1));
                    break;
                case 40109:  //Возможное управление жалюзи 
                    message(paramRec.getInt(PAR1));
                    break;
                case 40113:  //Установить текстуру по 
                    message(paramRec.getInt(PAR1));
                    break;
            }
        }
        if (filterParamJson(elemSimple, tableList) == false) {
            return false; //параметры по умолчанию и I-OKNA
        }
        return true;
    }

    //Cоединения
    protected boolean joinng(HashMap<Integer, String> hmParam, ElemSimple elemSimple, List<Record> tableList) {

        //Цикл по параметрам соединения
        for (Record paramRec : tableList) {

            switch (paramRec.getInt(PAR1)) {
                case 11000:  //Для технологического кода контейнера 1/2
                    message(paramRec.getInt(PAR1));
                    break;
                case 11001:  //Если признак состава Арт.1 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11002:  //Если признак состава Арт.2 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11008:  //Эффективное заполнение изд., мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11005:  //Контейнер типа 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11009:  //Внешнее соединение 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11010:  //Рассчитывать с Артикулом 1 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11020:  //Рассчитывать с Артикулом 2 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11028:  //Диапозон веса заполнения, кг 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11029:  //Расстояние узла от ручки, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11030:  //Количество 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11040:  //Порог расчета, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11050:  //Шаг, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11060:  //Количество на шаг 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11066:  //Если текстура профиля Арт.1 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11067:  //Коды основной текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11068:  //Коды внутр. текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11069:  //Коды внешн. текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11070:  //Ставить однократно 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11072:  //Расчет по стороне 
                    message(paramRec.getInt(PAR1));
                    break;
                case 11095:  //Если признак системы конструкции 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12000:  //Для технологического кода контейнера 1/2
                    message(paramRec.getInt(PAR1));
                    break;
                case 12001:  //Если признак состава Арт.1 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12002:  //Если признак состава Арт.2 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12005:  //Контейнер типа 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12008:  //Эффективное заполнение изд., мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12009:  //Внешнее соединение 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12010:  //Рассчитывать с Артикулом 1 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12020:  //Рассчитывать с Артикулом 2 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12027:  //Рассчитывать для профилей 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12028:  //Диапозон веса заполнения, кг 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12030:  //[ * коэф-т ] 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12050:  //Поправка, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12060:  //Количество 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12063:  //Углы реза по плоскости ригеля 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12064:  //Учёт в длине углов плоскостей 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12065:  //Длина, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12067:  //Коды основной текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12068:  //Коды внутр. текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12069:  //Коды внешн. текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12070:  //Ставить однократно 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12072:  //Расчет по стороне 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12075:  //Углы реза 
                    message(paramRec.getInt(PAR1));
                    break;
                case 12095:  //Если признак системы конструкции 
                    message(paramRec.getInt(PAR1));
                    break;
                default:
                    message(paramRec.getInt(PAR1));
                    break;
            }
        }
        if (filterParamJson(elemSimple, tableList) == false) {
            return false; //параметры по умолчанию и I-OKNA
        }
        return true;
    }

    //Заполнения
    //public static int[]  parGlas = {14000 ,14030 ,14040 ,14050 ,14060 ,14065 ,14068 ,15000 ,15005 ,15011 ,15013 ,15027 ,15030 ,15040,15045 ,15050 ,15055 ,15068 ,15069};        
    protected boolean filling(HashMap<Integer, String> hmParam, ElemSimple ElemSimple, List<Record> tableList) {

        //Цикл по параметрам заполнения
        for (Record paramRec : tableList) {

            switch (paramRec.getInt(PAR1)) {
                case 14000:  //Для технологического кода контейнера 
                    message(paramRec.getInt(PAR1));
                    break;
                case 14001:  //Если признак состава 
                    message(paramRec.getInt(PAR1));
                    break;
                case 14005:  //Тип проема 
                    message(paramRec.getInt(PAR1));
                    break;
                case 14008:  //Эффективное заполнение изд., мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 14009:  //Арочное заполнение 
                    message(paramRec.getInt(PAR1));
                    break;
                case 14017:  //Код системы содержит строку 
                    message(paramRec.getInt(PAR1));
                    break;
                case 14030:  //Количество 
                    message(paramRec.getInt(PAR1));
                    break;
                case 14040:  //Порог расчета, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 14050:  //Шаг, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 14060:  //Количество на шаг 
                    message(paramRec.getInt(PAR1));
                    break;
                case 14065:  //Ограничение угла, ° 
                    message(paramRec.getInt(PAR1));
                    break;
                case 14067:  //Коды основной текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 14068:  //Коды внутр. текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 14069:  //Коды внешн. текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 14081:  //Если артикул профиля контура 
                    message(paramRec.getInt(PAR1));
                    break;
                case 14095:  //Если признак системы конструкции 
                    message(paramRec.getInt(PAR1));
                    break;
                case 15000:  //Для технологического кода контейнера 
                    message(paramRec.getInt(PAR1));
                    break;
                case 15001:  //Если признак состава 
                    message(paramRec.getInt(PAR1));
                    break;
                case 15005:  //Тип проема 
                    message(paramRec.getInt(PAR1));
                    break;
                case 15008:  //Эффективное заполнение изд., мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 15009:  //Арочное заполнение 
                    message(paramRec.getInt(PAR1));
                    break;
                case 15010:  //Расчет реза штапика 
                    message(paramRec.getInt(PAR1));
                    break;
                case 15011:  //Расчет реза штапика 
                    message(paramRec.getInt(PAR1));
                    break;
                case 15017:  //Код системы содержит строку 
                    message(paramRec.getInt(PAR1));
                    break;
                case 15013:  //Подбор дистанционных вставок пролета 
                    message(paramRec.getInt(PAR1));
                    break;
                case 15027:  //Рассчитывать для профиля 
                    message(paramRec.getInt(PAR1));
                    break;
                case 15030:  //[ * коэф-т ] 
                    message(paramRec.getInt(PAR1));
                    break;
                case 15040:  //Количество 
                    message(paramRec.getInt(PAR1));
                    break;
                case 15045:  //Длина, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 15050:  //Поправка, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 15051:  //Удлинение на один пог.м., мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 15055:  //Ограничение угла, ° 
                    message(paramRec.getInt(PAR1));
                    break;
                case 15067:  //Коды основной текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 15068:  //Коды внутр. текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 15069:  //Коды внешн. текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 15081:  //Если артикул профиля контура 
                    message(paramRec.getInt(PAR1));
                    break;
                case 15095:  //Если признак системы конструкции 
                    message(paramRec.getInt(PAR1));
                    break;
                default:
                    message(paramRec.getInt(PAR1));
                    break;
            }
        }
        if (filterParamJson(ElemSimple, tableList) == false) {
            return false; //параметры по умолчанию и I-OKNA
        }
        return true;
    }

    //Фурнитура
    protected boolean furniture(HashMap<Integer, String> hmParam, ElemSimple elemSimple, List<Record> tableList) {

        //Цикл по параметрам фурнитуры
        for (Record paramRec : tableList) {
            switch (paramRec.getInt(PAR1)) {

                case 24001:  //Форма контура 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24002:  //Если артикул створки 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24003:  //Если артикул цоколя 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24004:  //Если створка прилегает к артикулу 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24005:  //Коды текстуры створки 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24006:  //Установить текстуру 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24007:  //Коды текстуры ручки 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24009:  //Коды текстуры подвеса 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24008:  //Если серия створки 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24010:  //Номер стороны 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24011:  //Расчет по общей арке 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24012:  //Направление открывания 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24013:  //Выбран авто расчет подвеса 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24017:  //Код системы содержит строку 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24030:  //Количество 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24032:  //Правильная полуарка 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24033:  //Фурнитура штульповая 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24036:  //Номер Стороны_X/Стороны_Y набора 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24037:  //Номер стороны по параметру набора 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24038:  //Проверять Cторону_L)/Cторону_W) 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24039:  //Створка заднего плана 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24040:  //Порог расчета, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24050:  //Шаг, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24060:  //Количество на шаг 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24063:  //Диапазон веса, кг 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24064:  //Ограничение высоты ручки, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24067:  //Коды основной текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24068:  //Коды внутр. текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24069:  //Коды внешн. текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24070:  //Если высота ручки 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24072:  //Ручка от низа створки, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24073:  //Петля от низа створки, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24074:  //Петля по центру стороны 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24075:  //Петля от верха створки, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24077:  //Смещение замка от ручки, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24078:  //Замок от края профиля, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24095:  //Если признак системы конструкции 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24098:  //Бригада, участок) 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24099:  //Трудозатраты, ч/ч. 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24800:  //Код основной обработки) 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24801:  //Доп.основная обработка
                    message(paramRec.getInt(PAR1));
                    break;
                case 24802:  //Код симметр. обработки 
                    message(paramRec.getInt(PAR1));
                    break;
                case 24803:  //Доп.симметр. обработка
                    message(paramRec.getInt(PAR1));
                    break;
                case 25001:  //Форма контура 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25002:  //Если артикул створки 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25003:  //Если артикул цоколя 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25005:  //Коды текстуры створки 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25007:  //Коды текстуры ручки 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25009:  //Коды текстуры подвеса 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25008:  //Если серия створки 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25010:  //Номер стороны 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25011:  //Расчет по общей арке 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25012:  //Направление открывания 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25013:  //Укорочение от 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25017:  //Код системы содержит строку 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25030:  //Укорочение, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25032:  //Правильная полуарка 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25033:  //Фурнитура штульповая 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25035:  //[ * коэф-т ] 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25036:  //Номер Стороны_ X/Стороны_ Y набора 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25037:  //Номер стороны по параметру набора 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25038:  //Проверять Cторону_ L/Cторону_ W 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25040:  //Длина, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25060:  //Количество 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25063:  //Диапазон веса, кг 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25064:  //Ограничение высоты ручки, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25067:  //Коды основной текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25068:  //Коды внутр. текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25069:  //Коды внешн. текстуры изделия 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25070:  //Если высота ручки 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25072:  //Ручка от низа створки, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25095:  //Если признак системы конструкции 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25098:  //Бригада, участок) 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25099:  //Трудозатраты, ч/ч. 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25800:  //Код основной обработки
                    message(paramRec.getInt(PAR1));
                    break;
                case 25801:  //Доп.основная обработка
                    message(paramRec.getInt(PAR1));
                    break;
                case 25802:  //Код симметр. обработки 
                    message(paramRec.getInt(PAR1));
                    break;
                case 25803:  //Доп.симметр. обработка
                    message(paramRec.getInt(PAR1));
                    break;
                default:
                    message(paramRec.getInt(PAR1));
                    break;
            }
        }
        if (filterParamJson(elemSimple, tableList) == false) {
            return false; //параметры по умолчанию и I-OKNA
        }
        return true;
    }

    private void message(int code) {
        System.err.println("Parametr ОШИБКА! КОД " + code + " НЕ ОБРАБОТАН.");
    }
}
