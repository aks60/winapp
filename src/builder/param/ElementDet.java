package builder.param;

import dataset.Record;
import domain.eElemdet;
import domain.eElempar2;
import domain.eSetting;
import java.util.HashMap;
import java.util.List;
import builder.Wincalc;
import builder.model.ElemSimple;
import common.Util;

//Составы
public class ElementDet extends Par5s {

    public ElementDet(Wincalc iwin) {
        super(iwin);
    }

    public boolean check(HashMap<Integer, String> hmParam, ElemSimple elem5e, Record elemdetRec) {

        List<Record> paramList = eElempar2.find3(elemdetRec.getInt(eElemdet.id)); //список параметров детализации 
        if (filterParamDef(paramList) == false) {
            return false;
        }
        //Цикл по параметрам составов
        for (Record rec : paramList) {

            int grup = rec.getInt(GRUP);
            try {
                switch (grup) {

                    case 33000: //Для технологического кода контейнера 
                    case 34000: //Для технологического кода контейнера 
                        if (!Uti5.check_STRING_33000_34000(rec.getStr(TEXT), elem5e)) {
                            return false;
                        }
                        break;
                    case 33001:  //Если признак состава 
                        message(grup);
                        break;
                    case 33002:  //Расчет пролетов соединений 
                        message(grup);
                        break;
                    case 33003:  //Расчет пролетов заполнений, мм 
                        message(grup);
                        break;
                    case 33004:  //Расчет от длины профиля стойки 
                        message(grup);
                        break;
                    case 33005:  //Коды основной текстуры контейнера 
                    case 34005:  //Коды основной текстуры контейнера
                        if (Util.containsNumb(rec.getStr(TEXT), elem5e.iwin().colorID1) == false) {
                            return false;
                        }
                        break;
                    case 33006:  //Коды внутр. текстуры контейнера
                    case 34006:  //Коды внутр. текстуры контейнера 
                        if (Util.containsNumb(rec.getStr(TEXT), elem5e.iwin().colorID2) == false) {
                            return false;
                        }
                        break;
                    case 33007:  //Коды внешн. текстуры контейнера 
                    case 34007:  //Коды внешн. текстуры контейнера     
                        if (Util.containsNumb(rec.getStr(TEXT), elem5e.iwin().colorID3) == false) {
                            return false;
                        }
                        break;
                    case 33008:  //Эффективное заполнение изд., мм 
                        message(grup);
                        break;
                    case 33011:
                    case 34011: //Толщина внешнего/внутреннего заполнения, мм ("Толщина заполнения, мм") 
                        if ("ps3".equals(eSetting.find(2))) {
//                            LinkedList<ElemSimple> e = elem5e.owner().listElem(TypeElem.GLASS);
//                            float depth = e.getFirst().artiklRec.getFloat(eArtikl.depth);
//                            if (Util.compareFloat(rec.getStr(TEXT), depth) == false) {
//                                return false;
//                            }
                            message(grup);
                        } else {
                            message(grup);
                        }
                        break;
                    case 33017:  //Код системы содержит строку 
                        message(grup);
                        break;
                    case 33030:  //Количество 
                    case 38030:  //Количество   
                        hmParam.put(grup, rec.getStr(TEXT));
                        break;
                    case 33031:  //Расчет количества 
                        message(grup);
                        break;
                    case 33032:  //Периметр покраски по периметру 
                        message(grup);
                        break;
                    case 33033:  //Расход по профилю 
                        message(grup);
                        break;
                    case 33034:  //Периметр покраски, мм 
                        message(grup);
                        break;
                    case 33035:  //Расход по поверхности на кв.м. 
                        message(grup);
                        break;
                    case 33036:  //Коэффициент_расхода 
                        message(grup);
                        break;
                    case 33040:  //Порог расчета, мм 
                    case 38040:  //Порог расчета, мм     
                        hmParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                        break;
                    case 33050:  //Шаг, мм 
                    case 38050:  //Шаг, мм     
                        hmParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                        break;
                    case 33060:  //Количество на шаг 
                        hmParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                        break;
                    case 33062:  //Если стойка удлинена 
                        message(grup);
                        break;
                    case 33063:  //Диапазон веса створки, кг 
                        message(grup);
                        break;
                    case 33071:  //Контейнер типа 
                    case 34071:
                        if ("ps3".equals(eSetting.find(2))) {
                            String[] arr = {"коробка", "створка", "импост", "стойка", "эркер"};
                            int[] index = {1, 2, 3, 5, 19};
                            for (int i = 0; i < arr.length; i++) {
                                if (arr.equals(rec.getStr(TEXT)) && Util.containsNumb(String.valueOf(index[i]), elem5e.type().id) == false) {
                                    return false;
                                }
                            }
                        } else {
                            if (Util.containsNumb(rec.getStr(TEXT), elem5e.type().id) == false) {
                                return false;
                            }
                        }
                        break;
                    case 33073:  //Отправочная марка фасада 
                        message(grup);
                        break;
                    case 33074:  //На прилегающей створке 
                        message(grup);
                        break;
                    case 33078:  //Ставить однократно 
                        message(grup);
                        break;
                    case 33081:  //Для внешнего/внутреннего угла плоскости, ° 
                    case 34081:  //Для внешнего/внутреннего угла плоскости, °                        
                        message(grup);
                        break;
                    case 33083:  //Точный внутр. угол плоскости, ° 
                        //нет такого парам. в ps3,4
                        break;
                    case 33088:  //Точный внешний угол плоскости, °
                    case 34088:  //Точный внешний угол плоскости, °
                        //нет такого парам. в ps3,4
                        break;
                    case 33095:  //Если признак системы конструкции 
                    case 34095:  //Если признак системы конструкции
                    case 38095:  //Если признак системы конструкции
                    case 39095:  //Если признак системы конструкции
                    case 40095:  //Если признак системы конструкции 
                        if (!Uti5.check_STRING_33095_34095_38095_39095_40095(rec.getStr(TEXT), elem5e, iwin.nuni)) {
                            return false;
                        }
                        break;
                    case 33099:  //Трудозатраты, ч/ч. 
                    case 34099:  //Трудозатраты, ч/ч.
                    case 38099:  //Трудозатраты, ч/ч. 
                    case 39099:  //Трудозатраты, ч/ч. 
                        hmParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                        break;
                    case 34001:  //Если признак состава 
                        message(grup);
                        break;
                    case 34002:  //Расчет пролетов соединений 
                        message(grup);
                        break;
                    case 34003:  //Расчет пролетов заполнений, мм 
                        message(grup);
                        break;
                    case 34004:  //Расчет от длины профиля стойки 
                        message(grup);
                        break;
                    case 34010:  //Расчет армирования 
                        hmParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                        break;
                    case 34012:  //Для варианта соединения Т*
                        message(grup);
                        break;
                    case 34009:  //Если два присоединенных артикула 
                        message(grup);
                        break;
                    case 34008:  //Эффективное заполнение изделия, мм 
                        message(grup);
                        break;
                    case 34013:  //Подбор дистанционных вставок по пролетам 
                        message(grup);
                        break;
                    case 34015:  //Расчет длины по 
                        message(grup);
                        break;
                    case 34016:  //Прилегание контура створки 
                        message(grup);
                        break;
                    case 34017:  //Код системы содержит строку 
                        message(grup);
                        break;
                    case 34030:  //[ * коэф-т ] 
                    case 39030:  //[ * коэф-т ]     
                        hmParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                        break;
                    case 34049:  //Поправка по нормали от начала/конца, мм 
                        message(grup);
                        break;
                    case 34050:  //Поправка, мм 
                        hmParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                        break;
                    case 34051:  //Поправка, мм 
                        if (elem5e.spcRec.getParam("0", 31052).equalsIgnoreCase(rec.getStr(TEXT)) == false) {
                            hmParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                        }
                        break;
                    case 34052:  //Поправка не прямого угла импоста, мм 
                        message(grup);
                        break;
                    case 34060:  //Количество
                    case 39060:  //Количество
                        hmParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                        break;
                    case 34061:  //Расчет количества 
                        message(grup);
                        break;
                    case 34062:  //Если стойка удлинена 
                        message(grup);
                        break;
                    case 34063:  //Диапазон веса створки, кг 
                        message(grup);
                        break;
                    case 34064:  //Учёт поправок соединений для составов 
                        message(grup);
                        break;
                    case 34066:  //Если номер стороны в контуре 
                    case 33066:  //Если номер стороны в контуре
                        if (!Uti5.check_INT_33066_34066(rec.getStr(TEXT), elem5e)) {
                            return false;
                        }
                        break;
                    case 33067:  //Коды основной текстуры изделия    
                    case 34067:  //Коды основной текстуры изделия 
                    case 38067:  //Коды основной текстуры изделия    
                    case 39067:  //Коды основной текстуры изделия
                    case 40067:  //Коды основной текстуры изделия                     
                        int c1 = elem5e.iwin().colorID1;
                        if (Util.containsNumb(rec.getStr(TEXT), c1) == false) {
                            return false;
                        }
                        break;
                    case 33068:  //Коды внутр. текстуры изделия    
                    case 34068:  //Коды внутр. текстуры изделия 
                    case 38068:  //Коды внутр. текстуры изделия 
                    case 39068:  //Коды внутр. текстуры изделия
                    case 40068:  //Коды внутр. текстуры изделия    
                        int c2 = elem5e.iwin().colorID2;
                        if (Util.containsNumb(rec.getStr(TEXT), c2) == false) {
                            return false;
                        }
                        break;
                    case 33069:  //Коды внешн. текстуры изделия    
                    case 34069:  //Коды внешн. текстуры изделия 
                    case 38069:  //Коды внешн. текстуры изделия 
                    case 39069:  //Коды внешн. текстуры изделия 
                    case 40069: //Коды внешн. текстуры изделия  
                    {
                        int c3 = elem5e.iwin().colorID3;
                        if (Util.containsNumb(rec.getStr(TEXT), c3) == false) {
                            return false;
                        }
                    }
                    break;
                    case 34070:  //Длина, мм 
                    case 39070:  //Длина, мм
                        hmParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                        break;
                    case 34072:  //Смещение от уровня деления, мм 
                        message(grup);
                        break;
                    case 34073:  //Отправочная марка фасада 
                        message(grup);
                        break;
                    case 34074:  //На прилегающей створке 
                        message(grup);
                        break;
                    case 34075:  //Углы реза 
                        hmParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                        break;
                    case 34077:  //Задать Угол_реза_1/Угол_реза_2, ° 
                        hmParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                        break;
                    case 34078:  //Ставить однократно 
                        if ("ps3".equals(eSetting.find(2))) {
                            hmParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                        } else {
                            message(grup);
                        }
                        break;
                    case 34079:  //Длина подбирается из списка, мм 
                        message(grup);
                        break;
                    case 34080:  //Длина округляется с шагом, мм 
                        message(grup);
                        break;
                    case 34097:  //Трудозатраты по длине 
                        message(grup);
                        break;
                    case 38004:  //Расчет 
                        message(grup);
                        break;
                    case 38010:  //Номер стороны 
                        message(grup);
                        break;
                    case 38017:  //Код системы содержит строку 
                        message(grup);
                        break;
                    case 38037:  //Название фурнитуры содержит 
                        message(grup);
                        break;
                    case 38039:  //Для типа открывания 
                        message(grup);
                        break;
                    case 38060:  //Количество на шаг 
                        message(grup);
                        break;
                    case 38081:  //Если артикул профиля контура 
                        message(grup);
                        break;
                    case 38108:  //Применять коэффициенты АКЦИИ для МЦ 
                        message(grup);
                        break;
                    case 38109:  //Возможное управление жалюзи 
                        message(grup);
                        break;
                    case 38113:  //Установить текстуру по 
                        message(grup);
                        break;
                    case 39002:  //Номер стороны 
                        message(grup);
                        break;
                    case 39005:  //Расчет 
                        message(grup);
                        break;
                    case 39017:  //Код системы содержит строку 
                        message(grup);
                        break;
                    case 39020:  //Поправка, мм 
                        hmParam.put(grup, rec.getStr(TEXT));
                        break;
                    case 39037:  //Название фурнитуры содержит 
                        message(grup);
                        break;
                    case 39039:  //Для типа открывания 
                        message(grup);
                        break;
                    case 39063:  //Округлять количество до ближайшего 
                        message(grup);
                        break;
                    case 39075:  //Углы реза 
                        message(grup);
                        break;
                    case 39077:  //Задать Угол_реза_1/Угол_реза_2, ° 
                        message(grup);
                        break;
                    case 39080:  //Шаг вагонки ламели, мм 
                        message(grup);
                        break;
                    case 39081:  //Если артикул профиля контура 
                        message(grup);
                        break;
                    case 39093:  //Поперечину ставить :
                        hmParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                        message(grup);
                        break;
                    case 39097:  //Трудозатраты по периметру 
                        message(grup);
                        break;
                    case 39108:  //Применять коэффициенты АКЦИИ для МЦ 
                        message(grup);
                        break;
                    case 39109:  //Возможное управление жалюзи 
                        message(grup);
                        break;
                    case 39113:  //Установить текстуру по 
                        message(grup);
                        break;
                    case 40004:  //Ширина заполнения, мм 
                        message(grup);
                        break;
                    case 40005:  //Поправка ширины/высоты, мм 
                        message(grup);
                        break;
                    case 40006:  //Высота заполнения, мм 
                        message(grup);
                        break;
                    case 40007:  //Высоту сделать длиной 
                        message(grup);
                        break;
                    case 40008:  //Эффективное заполнение изд., мм 
                        message(grup);
                        break;
                    case 40010:  //Поправка на стороны четные/нечетные, мм 
                        message(grup);
                        break;
                    case 40017:  //Код системы содержит строку 
                        message(grup);
                        break;
                    case 40037:  //Название фурнитуры содержит 
                        message(grup);
                        break;
                    case 40108:  //Применять коэффициенты АКЦИИ для МЦ 
                        message(grup);
                        break;
                    case 40109:  //Возможное управление жалюзи 
                        message(grup);
                        break;
                    case 40113:  //Установить текстуру по 
                        message(grup);
                        break;
                    default:
                        assert !(grup > 0 && grup < 50000) : "Код " + grup + "  не обработан!!!";
                }
            } catch (Exception e) {
                System.err.println("Ошибка:param.ElementDet.check()  parametr=" + grup + "    " + e);
                return false;
            }
        }
        return true;
    }
}
/*
    public static int[] par = {33000, 33005, 33008, 33030, 33040, 33050, 33060, 33066, 33067, 33069, 33078, 33095, 34000, 34004, 34005,
        34006, 34007, 34008, 34010, 34011, 34015, 34030, 34051, 34060, 34066, 34067, 34068, 34069, 34070, 34071, 34075, 34095, 34099,
        38004, 38010, 38030, 38050, 38060, 38067, 38068, 38069, 39002, 39005, 39020, 39060, 39068, 39069, 39075, 39077, 39093, 40010, 40067, 40068, 40069};
 */
