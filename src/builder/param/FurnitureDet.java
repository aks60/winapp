package builder.param;

import dataset.Record;
import domain.eArtikl;
import domain.eFurndet;
import domain.eFurnpar2;
import enums.LayoutArea;
import enums.LayoutHandle;
import enums.TypeElem;
import enums.UseArtiklTo;
import java.util.HashMap;
import java.util.List;
import builder.Wincalc;
import builder.specif.Util;
import builder.model.AreaSimple;
import builder.model.AreaStvorka;
import builder.model.ElemFrame;
import builder.model.ElemSimple;

//Фурнитура
public class FurnitureDet extends Par5s {

    private int[] par = {24001, 24002, 25002, 24004, 24006, 24010, 25010, 24012, 24030, 25030, 24033, 24038, 24063, 24067, 25067, 24068, 24069, 24070, 24072, 24073, 24074, 24075, 24095, 24099, 25013, 25035, 25040, 25060, 25067};

    public FurnitureDet(Wincalc iwin) {
        super(iwin);
    }

    public boolean check(HashMap<Integer, String> mapParam, AreaStvorka areaStv, Record furndetRec) {

        List<Record> tableList = eFurnpar2.find(furndetRec.getInt(eFurndet.id));
        if (filterParamDef(tableList) == false) {
            return false; //параметры по умолчанию
        }
        //Цикл по параметрам фурнитуры
        for (Record rec : tableList) {

            int grup = rec.getInt(GRUP);
            try {
                switch (grup) {

                    case 24001:  //Форма контура 
                    case 25001: {
                        //"Прямоугольное", "Не прямоугольное", "Не арочное", "Арочное" (TypeElem.AREA - глухарь)
                        if ("прямоугольная".equalsIgnoreCase(rec.getStr(TEXT)) && TypeElem.RECTANGL.equals(areaStv.type()) == false
                                && TypeElem.AREA.equals(areaStv.type()) == false && TypeElem.STVORKA.equals(areaStv.type()) == false) {
                            return false;
                        } else if ("трапециевидная".equalsIgnoreCase(rec.getStr(TEXT)) && TypeElem.TRAPEZE.equals(areaStv.type()) == false) {
                            return false;
                        } else if ("арочная".equalsIgnoreCase(rec.getStr(TEXT)) && TypeElem.ARCH.equals(areaStv.type()) == false) {
                            return false;
                        } else if ("не арочная".equalsIgnoreCase(rec.getStr(TEXT)) && TypeElem.ARCH.equals(areaStv.type()) == true) {
                            return false;
                        }
                        break;
                    }
                    case 24002:  //Если артикул створки 
                    case 25002:  //Если артикул створки 
                        if (areaStv.mapFrame.entrySet().stream().filter(el -> el.getValue().artiklRec.getStr(eArtikl.code).equals(rec.getStr(TEXT))).findFirst().isEmpty()) {
                            return false;
                        }
                        break;
                    case 24003:  //Если артикул цоколя 
                        message(rec.getInt(GRUP));
                        break;
                    case 24004:  //Если створка прилегает к артикулу 
                        message(rec.getInt(GRUP));
                        break;
                    case 24005:  //Коды текстуры створки 
                        message(rec.getInt(GRUP));
                        break;
                    case 24006:  //Установить текстуру 
                        mapParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                        break;
                    case 24007:  //Коды текстуры ручки 
                        message(rec.getInt(GRUP));
                        break;
                    case 24009:  //Коды текстуры подвеса 
                        message(rec.getInt(GRUP));
                        break;
                    case 24008:  //Если серия створки 
                        message(rec.getInt(GRUP));
                        break;
                    case 24010:  //Номер стороны 
                        message(rec.getInt(GRUP));
                        break;
                    case 24011:  //Расчет по общей арке 
                        message(rec.getInt(GRUP));
                        break;
                    case 24012:  //Направление открывания
                        if (areaStv.typeOpen.name.equalsIgnoreCase(rec.getStr(TEXT)) == false) {
                            return false;
                        }
                        break;
                    case 24013:  //Выбран авто расчет подвеса 
                        message(rec.getInt(GRUP));
                        break;
                    case 24017:  //Код системы содержит строку 
                        message(rec.getInt(GRUP));
                        break;
                    case 24030:  //Количество 
                    case 25060:  //Количество     
                        mapParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                        break;
                    case 24032:  //Правильная полуарка 
                        message(rec.getInt(GRUP));
                        break;
                    case 24033:   //Фурнитура штульповая 
                    case 25033: {
                        if (areaStv.typeOpen.name.equalsIgnoreCase("Левое")) {
                            ElemFrame el = areaStv.mapFrame.get(LayoutArea.LEFT);
                            if (rec.getStr(TEXT).equalsIgnoreCase("Да") && el.useArtiklTo() != UseArtiklTo.SHTULP) {
                                return false;
                            } else if (rec.getStr(TEXT).equalsIgnoreCase("Нет") && el.useArtiklTo() == UseArtiklTo.SHTULP) {
                                return false;
                            }
                        } else if (areaStv.typeOpen.name.equalsIgnoreCase("Правое")) {
                            ElemFrame el = areaStv.mapFrame.get(LayoutArea.RIGHT);
                            if (rec.getStr(TEXT).equalsIgnoreCase("Да") && el.useArtiklTo() != UseArtiklTo.SHTULP) {
                                return false;
                            }
                            if (el.useArtiklTo() == UseArtiklTo.SHTULP && rec.getStr(TEXT).equalsIgnoreCase("Нет")) {
                                return false;
                            }
                        }
                    }
                    break;
                    case 24036:  //Номер Стороны_X/Стороны_Y набора 
                        message(rec.getInt(GRUP));
                        break;
                    case 24037:  //Номер стороны по параметру набора 
                        message(rec.getInt(GRUP));
                        break;
                    case 24038:  //Проверять Cторону_(L))/Cторону_(W) 
                    case 25038:  //Проверять Cторону_(L)/Cторону_(W)     
                        //Тут полные непонятки. Возможно сторона проверки назначается для всего набора
                        mapParamTmp.put(grup, rec.getStr(TEXT));
                        break;
                    case 24039:  //Створка заднего плана 
                        message(rec.getInt(GRUP));
                        break;
                    case 24040:  //Порог расчета, мм 
                        message(rec.getInt(GRUP));
                        break;
                    case 24050:  //Шаг, мм 
                        message(rec.getInt(GRUP));
                        break;
                    case 24060:  //Количество на шаг 
                        message(rec.getInt(GRUP));
                        break;
                    case 24063:  //Диапазон веса, кг 
                        message(rec.getInt(GRUP));
                        break;
                    case 24064:  //Ограничение высоты ручки, мм 
                        message(rec.getInt(GRUP));
                        break;
                    case 24067:  //Коды основной текстуры изделия 
                        message(rec.getInt(GRUP));
                        break;
                    case 24068:  //Коды внутр. текстуры изделия 
                        int c2 = areaStv.iwin().colorID2;
                        if (Util.containsInt(rec.getStr(TEXT), c2) == false) {
                            return false;
                        }
                        break;
                    case 24069:  //Коды внешн. текстуры изделия 
                    case 25069:  //Коды внешн. текстуры изделия     
                        int c3 = areaStv.iwin().colorID3;
                        if (Util.containsInt(rec.getStr(TEXT), c3) == false) {
                            return false;
                        }
                        break;
                    case 24070:  //Если высота ручки "по середине", "константная", "не константная", "установлена"
                    case 25070: {
                        if (LayoutHandle.CONST != areaStv.handleHeight && rec.getStr(TEXT).equalsIgnoreCase("константная")) {
                            return false;
                        } else if (LayoutHandle.CONST == areaStv.handleHeight && rec.getStr(TEXT).equalsIgnoreCase("не константная")) {
                            return false;
                        } else if (LayoutHandle.MIDL != areaStv.handleHeight && rec.getStr(TEXT).equalsIgnoreCase("по середине")) {
                            return false;
                        } else if (LayoutHandle.SET != areaStv.handleHeight && rec.getStr(TEXT).equalsIgnoreCase("установлена")) {
                            return false;
                        }
                        break;
                    }
                    case 24072:  //Ручка от низа створки, мм 
                    case 25072:  //Ручка от низа створки, мм     
                        mapParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                        break;
                    case 24073:  //Петля от низа створки, мм 
                        mapParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                        break;
                    case 24074:  //Петля по центру стороны 
                        mapParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                        break;
                    case 24075:  //Петля от верха створки, мм 
                        mapParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                        break;
                    case 24077:  //Смещение замка от ручки, мм 
                        message(rec.getInt(GRUP));
                        break;
                    case 24078:  //Замок от края профиля, мм 
                        message(rec.getInt(GRUP));
                        break;
                    case 24095:  //Если признак системы конструкции 
                        message(rec.getInt(GRUP));
                        break;
                    case 24098:  //Бригада, участок) 
                        message(rec.getInt(GRUP));
                        break;
                    case 24099:  //Трудозатраты, ч/ч. 
                    case 25099:  //Трудозатраты, ч/ч.                    
                        mapParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                        break;
                    case 24800:  //Код основной обработки) 
                    case 24801:  //Доп.основная обработка
                        message(rec.getInt(GRUP));
                        break;
                    case 24802:  //Код симметр. обработки 
                        message(rec.getInt(GRUP));
                        break;
                    case 24803:  //Доп.симметр. обработка
                        message(rec.getInt(GRUP));
                        break;
                    case 25003:  //Если артикул цоколя 
                        message(rec.getInt(GRUP));
                        break;
                    case 25005:  //Коды текстуры створки 
                        message(rec.getInt(GRUP));
                        break;
                    case 25007:  //Коды текстуры ручки 
                        message(rec.getInt(GRUP));
                        break;
                    case 25009:  //Коды текстуры подвеса 
                        message(rec.getInt(GRUP));
                        break;
                    case 25008:  //Если серия створки 
                        message(rec.getInt(GRUP));
                        break;
                    case 25010:  //Номер стороны 
                        message(rec.getInt(GRUP));
                        //System.out.println("- " + detailRec.getInt(1) + " - " + areaStv.id() + " - " + rec.getStr(TEXT));
                        mapParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                        break;
                    case 25011:  //Расчет по общей арке 
                        message(rec.getInt(GRUP));
                        break;
                    case 25013:  //Укорочение от 
                    case 25030:  //Укорочение, мм 
                        mapParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                        break;
                    case 25017:  //Код системы содержит строку 
                        message(rec.getInt(GRUP));
                        break;
                    case 25032:  //Правильная полуарка 
                        message(rec.getInt(GRUP));
                        break;
                    case 25035:  //[ * коэф-т ] 
                        message(rec.getInt(GRUP));
                        break;
                    case 25036:  //Номер Стороны_ X/Стороны_ Y набора 
                        message(rec.getInt(GRUP));
                        break;
                    case 25037:  //Номер стороны по параметру набора 
                        message(rec.getInt(GRUP));
                        break;
                    case 25040:  //Длина, мм 
                        mapParam.put(rec.getInt(GRUP), rec.getStr(TEXT));
                        break;
                    case 25063:  //Диапазон веса, кг 
                        message(rec.getInt(GRUP));
                        break;
                    case 25064:  //Ограничение высоты ручки, мм 
                        message(rec.getInt(GRUP));
                        break;
                    case 25067:  //Коды основной текстуры изделия 
                        message(rec.getInt(GRUP));
                        break;
                    case 25068:  //Коды внутр. текстуры изделия 
                        message(rec.getInt(GRUP));
                        break;
                    case 25095:  //Если признак системы конструкции 
                        message(rec.getInt(GRUP));
                        break;
                    case 25098:  //Бригада, участок) 
                        message(rec.getInt(GRUP));
                        break;
                    case 25800:  //Код основной обработки
                        message(rec.getInt(GRUP));
                        break;
                    case 25801:  //Доп.основная обработка
                        message(rec.getInt(GRUP));
                        break;
                    case 25802:  //Код симметр. обработки 
                        message(rec.getInt(GRUP));
                        break;
                    case 25803:  //Доп.симметр. обработка
                        message(rec.getInt(GRUP));
                        break;
                    default:
                        message(rec.getInt(GRUP));
                        break;
                }
            } catch (Exception e) {
                System.err.println("Ошибка:param.FurnitureDet.check()  parametr=" + grup + "    " + e);
                return false;
            }
        }

        return true;
    }
}
