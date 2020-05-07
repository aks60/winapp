package wincalc.constr.param;

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
import wincalc.constr.Constructiv;
import wincalc.model.Com5t;
import wincalc.model.ElemSimple;

//Составы
public class ElementDet extends Par5s {

    public ElementDet(Wincalc iwin, Constructiv calcConstr) {
        super(iwin, calcConstr);
    }

    protected boolean check(HashMap<Integer, String> hmParam, ElemSimple elemSimple, List<Record> tableList) {

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
                case 38040:  //Порог расчета, мм     
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
                case 34095:  //Если признак системы конструкции
                case 38095:  //Если признак системы конструкции
                case 39095:  //Если признак системы конструкции
                case 40095:  //Если признак системы конструкции 
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
                case 33099:  //Трудозатраты, ч/ч. 
                case 34099:  //Трудозатраты, ч/ч.
                case 38099:  //Трудозатраты, ч/ч. 
                case 39099:  //Трудозатраты, ч/ч. 
                    hmParam.put(paramRec.getInt(PAR1), paramRec.getStr(PAR3));
                    break;
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
                case 39030:  //[ * коэф-т ]     
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
                case 39060:  //Количество
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
                case 33066:  //Если номер стороны в контуре
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
                case 33067:  //Коды основной текстуры изделия    
                case 34067:  //Коды основной текстуры изделия 
                case 38067:  //Коды основной текстуры изделия    
                case 39067:  //Коды основной текстуры изделия
                case 40067:  //Коды основной текстуры изделия                     
                    int c1 = elemSimple.iwin().color1;
                    if (Constructiv.compareInt(paramRec.getStr(PAR3), c1) == false) {
                        return false;
                    }
                    break;
                case 33068:  //Коды внутр. текстуры изделия    
                case 34068:  //Коды внутр. текстуры изделия 
                case 38068:  //Коды внутр. текстуры изделия 
                case 39068:  //Коды внутр. текстуры изделия
                case 40068:  //Коды внутр. текстуры изделия    
                    int c2 = elemSimple.iwin().color2;
                    if (Constructiv.compareInt(paramRec.getStr(PAR3), c2) == false) {
                        return false;
                    }
                    break;
                case 33069:  //Коды внешн. текстуры изделия    
                case 34069:  //Коды внешн. текстуры изделия 
                case 38069:  //Коды внешн. текстуры изделия 
                case 39069:  //Коды внешн. текстуры изделия 
                case 40069:  //Коды внешн. текстуры изделия                      
                    int c3 = elemSimple.iwin().color3;
                    if (Constructiv.compareInt(paramRec.getStr(PAR3), c3) == false) {
                        return false;
                    }
                    break;
                case 34070:  //Длина, мм 
                case 39070:  //Длина, мм
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
                case 34097:  //Трудозатраты по длине 
                    message(paramRec.getInt(PAR1));
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
                case 38050:  //Шаг, мм 
                    message(paramRec.getInt(PAR1));
                    break;
                case 38060:  //Количество на шаг 
                    message(paramRec.getInt(PAR1));
                    break;                                 
                case 38081:  //Если артикул профиля контура 
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
                case 39037:  //Название фурнитуры содержит 
                    message(paramRec.getInt(PAR1));
                    break;
                case 39039:  //Для типа открывания 
                    message(paramRec.getInt(PAR1));
                    break;                 
                case 39063:  //Округлять количество до ближайшего 
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
                case 39097:  //Трудозатраты по периметру 
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
}
