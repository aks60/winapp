package builder.param;

import dataset.Record;
import domain.eElemdet;
import domain.eElempar2;
import domain.eSetting;
import java.util.HashMap;
import java.util.List;
import builder.Wincalc;
import builder.model.ElemGlass;
import builder.model.ElemSimple;
import common.Util;
import domain.eArtikl;
import domain.eElement;
import domain.eSyssize;
import enums.TypeElem;

//Составы 33000, 34000, 38000, 39000, 40000
public class ElementDet extends Par5s {

    public ElementDet(Wincalc iwin) {
        super(iwin);
    }

    public boolean filter(HashMap<Integer, String> mapParam, ElemSimple elem5e, Record elemdetRec, Record elementRec) {

        List<Record> paramList = eElempar2.find3(elemdetRec.getInt(eElemdet.id)); //список параметров детализации 
        if (filterParamDef(paramList) == false) {
            return false;
        }
        //Цикл по параметрам составов
        for (Record rec : paramList) {
            if (ElementDet.this.check(mapParam, elem5e, rec, elementRec) == false) {
                return false;
            }
        }
        return true;
    }

    public boolean check(HashMap<Integer, String> mapParam, ElemSimple elem5e, Record rec, Record rec2) {
        int grup = rec.getInt(GRUP);
        try {
            switch (grup) {

                case 33000: //Для технологического кода контейнера 
                case 34000: //Для технологического кода контейнера 
                    if (!Uti5.check_STRING_XX000(rec.getStr(TEXT), elem5e)) {
                        return false;
                    }
                    break;
                case 33001:  //Если признак состава 
                case 34001:  //Если признак состава 
                    if (rec.getStr(TEXT).equals(rec2.getStr(eElement.signset)) == false) {
                        return false;
                    }
                    break;
                case 33002:  //Расчет пролетов соединений 
                    message(grup);
                    break;
                case 33003:  //Расчет пролетов заполнений, мм 
                    message(grup);
                    break;
                case 33004:  //Расчет от длины профиля стойки 
                case 34004:  //Расчет от длины профиля стойки                   
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
                case 33008: //Эффективное заполнение изд., мм 
                {
                    float depth = 0;
                    for (ElemSimple elem : iwin.listElem) {
                        if (elem.type() == TypeElem.GLASS) {
                            depth = (elem.artiklRecAn.getFloat(eArtikl.depth) > depth) ? elem.artiklRecAn.getFloat(eArtikl.depth) : depth;
                        }
                    }
                    if (rec.getFloat(TEXT) != depth) {
                        return false;
                    }
                }
                break;
                case 33011: //Толщина внешнего/внутреннего заполнения, мм
                case 34011: //Толщина внешнего/внутреннего заполнения, мм
                    List<ElemGlass> glassList = Uti5.getGlassDepth(elem5e);
                    if (glassList.get(0) instanceof ElemGlass && glassList.get(1) instanceof ElemGlass) {
                        if ("ps3".equals(eSetting.find(2))) { //Толщина заполнения, мм
                            if (Util.containsNumbAny(rec.getStr(TEXT),
                                    glassList.get(0).artiklRec.getFloat(eArtikl.depth),
                                    glassList.get(1).artiklRec.getFloat(eArtikl.depth)) == false) {
                                return false;
                            }
                        } else if (Util.containsNumb(rec.getStr(TEXT),
                                glassList.get(0).artiklRec.getFloat(eArtikl.depth),
                                glassList.get(1).artiklRec.getFloat(eArtikl.depth)) == false) {
                            return false;
                        }
                    }
                    break;
                case 33017: //Код системы содержит строку 
                case 34017: //Код системы содержит строку 
                {
                    Record record = eSyssize.find(elem5e.artiklRec.getInt(eArtikl.syssize_id));
                    if (rec.getStr(TEXT).equals(record.getStr(eSyssize.name)) == false) {
                        return false;
                    }
                }
                break;
                case 33030:  //Количество 
                case 38030:  //Количество   
                    mapParam.put(grup, rec.getStr(TEXT));
                    break;
                case 33031:  //Расчет количества 
                case 34061:  //Расчет количества                 
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
                case 33040:  //Порог расчета, мм 
                case 38040:  //Порог расчета, мм     
                    mapParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                    break;
                case 33050:  //Шаг, мм 
                case 38050:  //Шаг, мм     
                    mapParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                    break;
                case 33060:  //Количество на шаг 
                    mapParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
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
                    mapParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                    break;
                case 34002:  //Расчет пролетов соединений 
                    message(grup);
                    break;
                case 34003:  //Расчет пролетов заполнений, мм 
                    message(grup);
                    break;
                case 34010:  //Расчет армирования 
                    mapParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
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
                case 34030:  //[ * коэф-т ] 
                case 39030:  //[ * коэф-т ]     
                    mapParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                    break;
                case 34049:  //Поправка по нормали от начала/конца, мм 
                    message(grup);
                    break;
                case 34050:  //Поправка, мм 
                    mapParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                    break;
                case 34051:  //Поправка, мм 
                    if (elem5e.spcRec.getParam("0", 31052).equalsIgnoreCase(rec.getStr(TEXT)) == false) {
                        mapParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                    }
                    break;
                case 34052:  //Поправка не прямого угла импоста, мм 
                    message(grup);
                    break;
                case 34060:  //Количество
                case 39060:  //Количество
                    mapParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
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
                    mapParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
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
                    mapParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                    break;
                case 34077:  //Задать Угол_реза_1/Угол_реза_2, ° 
                    mapParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                    break;
                case 34078:  //Ставить однократно 
                    if ("ps3".equals(eSetting.find(2))) {
                        mapParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
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
                    mapParam.put(grup, rec.getStr(TEXT));
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
                    mapParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
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
        return true;
    }

    public boolean check(ElemSimple elem5e, Record rec) {
        HashMap<Integer, String> mapParam = new HashMap();
        return ElementDet.this.check(mapParam, elem5e, rec, null);
    }
}
