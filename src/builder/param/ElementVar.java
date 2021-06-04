package builder.param;

import dataset.Record;
import domain.eArtikl;
import domain.eElement;
import domain.eElempar1;
import domain.eSetting;
import domain.eSysprof;
import enums.LayoutArea;
import enums.TypeElem;
import java.util.List;
import builder.Wincalc;
import builder.model.ElemGlass;
import builder.model.ElemJoining;
import builder.model.ElemSimple;
import common.Util;
import enums.TypeJoin;

//Составы
public class ElementVar extends Par5s {

    public ElementVar(Wincalc iwin) {
        super(iwin);
    }

    public boolean filter(ElemSimple elem5e, Record elementRec) {

        List<Record> paramList = eElempar1.find3(elementRec.getInt(eElement.id)); //список параметров вариантов использования
        if (filterParamDef(paramList) == false) {
            return false;
        }
        //Цикл по параметрам состава
        for (Record rec : paramList) {
            if (check(elem5e, rec) == false) {
                return false;
            }
        }
        return true;
    }

    public boolean check(ElemSimple elem5e, Record rec) {
        int grup = rec.getInt(GRUP);
        try {
            switch (grup) {

                case 31000: //Для технологического кода контейнера 
                {
                    Record sysprofRec2 = elem5e.sysprofRec;
                    Record artiklVRec = eArtikl.find(sysprofRec2.getInt(eSysprof.artikl_id), false);
                    if (artiklVRec.get(eArtikl.tech_code) == null) {
                        return false;
                    }
                    String[] strList = rec.getStr(TEXT).split(";");
                    String[] strList2 = artiklVRec.getStr(eArtikl.tech_code).split(";");
                    boolean ret2 = false;
                    for (String str : strList) {
                        for (String str2 : strList2) {
                            if (str.equalsIgnoreCase(str2)) {
                                ret2 = true;
                            }
                        }
                    }
                    if (ret2 == false) {
                        return false;
                    }
                }
                break;
                case 31001: //Максимальное заполнение изделия, мм 
                {
                    List<ElemGlass> glassList = elem5e.iwin().rootArea.listElem(TypeElem.GLASS);
                    float depth = 0;
                    for (ElemGlass glass : glassList) {
                        if (glass.artiklRecAn.getFloat(eArtikl.depth) > depth) {
                            depth = (glass.artiklRecAn.getFloat(eArtikl.depth));
                        }
                    }
                    if (Util.compareBetween(rec.getStr(TEXT), depth) == false) {
                        return false;
                    }
                }
                break;
                case 31002:  //Если профиль 
                    if ("арочный".equalsIgnoreCase(rec.getStr(TEXT)) == false && LayoutArea.ARCH == elem5e.layout() == true) {
                        return false;
                    } else if ("прямой".equalsIgnoreCase(rec.getStr(TEXT)) == false && LayoutArea.ARCH != elem5e.layout() == true) {
                        return false;
                    }
                    break;
                case 31003:  //Если соединенный артикул  T-обр.
                    if (rec.getStr(TEXT).equals(elem5e.joinElem(0).artiklRecAn.getStr(eArtikl.code)) == true) {
                        if (iwin.mapJoin.get(elem5e.joinPoint(0)).typeJoin != TypeJoin.VAR40 && iwin.mapJoin.get(elem5e.joinPoint(0)).typeJoin != TypeJoin.VAR41) {
                            return false;
                        }
                    } else if (rec.getStr(TEXT).equals(elem5e.joinElem(1).artiklRecAn.getStr(eArtikl.code))) {
                        if (iwin.mapJoin.get(elem5e.joinPoint(1)).typeJoin != TypeJoin.VAR40 && iwin.mapJoin.get(elem5e.joinPoint(1)).typeJoin != TypeJoin.VAR41) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                    break;
                case 31004: //Если прилегающий артикул 
                    if (elem5e.joinElem(2) == null) {
                        return false;
                    }
                    if (rec.getStr(TEXT).equals(elem5e.joinElem(2).artiklRecAn.getStr(eArtikl.code)) == false) {
                        return false;
                    }
                    /*if (LayoutArea.HORIZ == elem5e.layout()) {
                            boolean b1 = (elem5e.joinFlat(LayoutArea.BOTT) != null) ? rec.getStr(TEXT).equals(elem5e.joinFlat(LayoutArea.BOTT).artiklRec.getStr(eArtikl.code)) : false;
                            boolean b2 = (elem5e.joinFlat(LayoutArea.TOP) != null) ? rec.getStr(TEXT).equals(elem5e.joinFlat(LayoutArea.TOP).artiklRec.getStr(eArtikl.code)) : false;
                            if (b1 == false && b2 == false) {
                                return false;
                            }
                        } else if (LayoutArea.VERT == elem5e.layout()) {
                            boolean b1 = (elem5e.joinFlat(LayoutArea.LEFT) != null) ? rec.getStr(TEXT).equals(elem5e.joinFlat(LayoutArea.LEFT).artiklRec.getStr(eArtikl.code)) : false;
                            boolean b2 = (elem5e.joinFlat(LayoutArea.RIGHT) != null) ? rec.getStr(TEXT).equals(elem5e.joinFlat(LayoutArea.RIGHT).artiklRec.getStr(eArtikl.code)) : false;
                            if (b1 == false && b2 == false) {
                                return false;
                            }
                        } else if (elem5e.joinFlat(elem5e.layout()) != null) {
                            if (rec.getStr(TEXT).equals(elem5e.joinFlat(elem5e.layout()).artiklRec.getStr(eArtikl.code)) == false) {
                                return false;
                            }
                        }*/
                    break;
                case 31005:  //Коды основной текстуры контейнера 
                case 37005:  //Коды основной текстуры контейнера 
                    if (Util.containsNumb(rec.getStr(TEXT), elem5e.colorID1) == false) {
                        return false;
                    }
                    break;
                case 31006:  //Коды внутр. текстуры контейнера 
                case 37006:  //Коды внутр. текстуры контейнера  
                    if (Util.containsNumb(rec.getStr(TEXT), elem5e.colorID2) == false) {
                        return false;
                    }
                    break;
                case 31007:  //Коды внешн. текстуры контейнера 
                case 37007:  //Коды внешн. текстуры контейнера  
                    if (Util.containsNumb(rec.getStr(TEXT), elem5e.colorID3) == false) {
                        return false;
                    }
                    break;
                case 31008:  //Эффективное заполнение изделия, мм 
                    message(grup);
                    break;
                case 31011:  //Толщина внешнего/внутреннего заполнения, мм 
                    if ("ps3".equals(eSetting.find(2))) {

                    } else {
                        if (elem5e.layout() == LayoutArea.HORIZ) {
                            ElemJoining el = iwin.mapJoin.get((elem5e.x1 + elem5e.width() / 2) + ":" + elem5e.y1);
                            //System.out.println(el);
                            if (el != null) {
                            }
                        } else {
                            ElemJoining el = iwin.mapJoin.get((elem5e.x1 + elem5e.width() / 2) + ":" + elem5e.y1);
                        }
                    }
                    message(grup);
                    break;
                case 31017:  //Код системы содержит строку 
                    message(grup);
                    break;
                case 31014:  //Заполнения одинаковой толщины 
                    message(grup);
                    break;
                case 31015:  //Разбиение профиля по уровням 
                    if (rec.getStr(TEXT).equalsIgnoreCase(elem5e.spcRec.getParam("empty", 13015)) == false) {
                        return false;
                    }
                    break;
                case 31016:  //Зазор_на_метр,_мм/Размер_,мм терморазрыва 
                    message(grup);
                    break;
                case 31019:  //Правило подбора текстур 
                    message(grup);
                    break;
                case 31020:  //Ограничение угла к горизонту, ° или Угол к горизонту минимальный, °
                    if ("ps3".equals(eSetting.find(2))) {
                        if (elem5e.anglHoriz < rec.getFloat(TEXT)) {
                            return false;
                        }
                    } else {
                        if (Util.containsNumb(rec.getStr(TEXT), elem5e.anglHoriz) == false) {
                            return false;
                        }
                    }
                    break;
                case 31030:  //Угол к горизонту максимальный, °
                    if ("ps3".equals(eSetting.find(2))) {
                        if (rec.getFloat(TEXT) < elem5e.anglHoriz) {
                            return false;
                        }
                    }
                    break;
                case 31031:  //Точный угол к горизонту
                    if ("ps3".equals(eSetting.find(2))) {
                        if (rec.getFloat(TEXT) != elem5e.anglHoriz) {
                            return false;
                        }
                    }
                    break;
                case 31033: //Если предыдущий артикул 
                    if (rec.getStr(TEXT).equalsIgnoreCase(elem5e.joinElem(0).artiklRecAn.getStr(eArtikl.code)) == false) {
                        return false;
                    }
                    break;
                case 31034:  //Если следующий артикул 
                    if (rec.getStr(TEXT).equalsIgnoreCase(elem5e.joinElem(1).artiklRecAn.getStr(eArtikl.code)) == false) {
                        return false;
                    }
                    break;
                case 31035:  //Уровень створки 
                    message(grup);
                    break;
                case 31037:  //Название фурнитуры содержит 
//                    if (TypeElem.STVORKA == elem5e.owner().type()) {
//                        if (elem5e.artiklRec.getStr(eArtikl.name).contains(rec.getStr(TEXT)) == false) {
//                            return false;
//                        }
//                    }
//                    break;
                    if (TypeElem.STVORKA == elem5e.owner().type()) {
                        return rec.getStr(TEXT).contains(elem5e.artiklRec.getStr(eArtikl.name));
                    }
                    return false;
                //break;
                case 31040:  //Поправка габарита накладки, мм 
                    message(grup);
                    break;
                case 31041:  //Ограничение длины профиля, мм 
                    if (Util.containsNumb(rec.getStr(TEXT), elem5e.length()) == false) {
                        return false;
                    }
                    break;
                case 31050:  //Контейнер имеет тип 
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
                case 31051:  //Если створка фурнитуры 
                    message(grup);
                    break;
                case 31052:  //Поправка в спецификацию, мм 
                    if (elem5e.layout() == LayoutArea.ARCH) {
                        elem5e.spcRec.width = elem5e.spcRec.width + rec.getFloat(TEXT);
                    }
                    break;
                case 31054:  //Коды основной текстуры изделия 
                    message(grup);
                    break;
                case 31055:  //Коды внутр. и внешн. текстуры изд.
                case 37055:  //Коды внутр. и внешн. текстуры изд. 
                    if ((Util.containsNumb(rec.getStr(TEXT), elem5e.colorID2) == true
                            && Util.containsNumb(rec.getStr(TEXT), elem5e.colorID3) == true) == false) {
                        return false;
                    }
                    break;
                case 31056:  //Коды внутр. или внеш. текстуры изд. 
                case 37056:  //Коды внут. или внеш. текстуры изд. 
                    if ((Util.containsNumb(rec.getStr(TEXT), elem5e.colorID2) == true
                            || Util.containsNumb(rec.getStr(TEXT), elem5e.colorID3) == true) == false) {
                        return false;
                    }
                    break;
                case 31057:  //Внутренняя текстура равна внешней 
                    message(grup);
                    break;
                case 31060:  //Допустимый угол между плоскостями, ° 
                    message(grup);
                    break;
                case 31073:  //Отправочная марка фасада 
                    message(grup);
                    break;
                case 31074:  //На прилегающей створке 
                    message(grup);
                    break;
                case 31080:  //Сообщение-предупреждение 
                    message(grup);
                    break;
                case 31081:  //Для внешнего/внутреннего угла плоскости, ° 
                    message(grup);
                    break;
                case 31085:  //Надпись на элементе 
                case 37085:  //Надпись на элементе   
                    elem5e.spcRec.mapParam.put(grup, rec.getStr(TEXT));
                    break;
                case 31090:  //Изменение сторон покраски 
                    if (rec.getStr(TEXT).equalsIgnoreCase(elem5e.spcRec.getParam("empty", 31090)) == false) {
                        return false;
                    }
                    break;
                case 31095:  //Если признак системы конструкции 
                    message(grup);
                    break;
                case 31098:  //Бригада, участок) 
                    message(grup);
                    break;
                case 31099:  //Трудозатраты, ч/ч. 
                case 37099:  //Трудозатраты, ч/ч.  
                    elem5e.spcRec.mapParam.put(grup, rec.getStr(TEXT));
                    break;
                case 31097:  //Трудозатраты по длине 
                    message(grup);
                    break;
                case 31800:  //Код обработки 
                    message(grup);
                    break;
                case 31801:  //Доп.обработки
                    message(grup);
                    break;
                case 37001:  //Установка жалюзи 
                    message(grup);
                    break;
                case 37002:  //Если артикул профиля контура 
                    message(grup);
                    break;
                case 37008:  //Тип проема 
                    if (!Uti5.dic_13003_14005_15005_37008(rec.getStr(TEXT), elem5e)) {
                        return false;
                    }
                    break;
                case 37009:  //Тип заполнения 
                    if (!Uti5.dic_37009(rec.getStr(TEXT), elem5e)) {
                        return false;
                    }
                    break;
                case 37010:  //Ограничение ширины/высоты листа, мм 
                    if (Util.containsNumb(rec.getStr(TEXT), elem5e.width(), elem5e.height()) == false) {
                        return false;
                    }
                    break;
                case 37017:  //Код системы содержит строку 
                    message(grup);
                    break;
                case 37030:  //Минимальная площадь или Ограничение площади, кв.м. для Ps4                        
                    if ("ps4".equals(versionDb)) {
                        if (Util.containsNumb(rec.getStr(TEXT), elem5e.width() / 1000 * elem5e.height() / 1000) == false) {
                            return false;
                        }
                    } else if ("ps3".equals(versionDb)) {
                        if (elem5e.width() / 1000 * elem5e.height() / 1000 < rec.getFloat(TEXT)) {
                            return false;
                        }
                    }
                    break;
                case 37031:  //Максимальная площадь 
                    message(grup);
                    if ("ps3".equals(versionDb)) {
                        if (elem5e.width() / 1000 * elem5e.height() / 1000 > rec.getFloat(TEXT)) {
                            return false;
                        }
                    }
                    break;
                case 37042:  //Мин. соотношение габаритов (б/м) или Допустимое соотношение габаритов б/м для Ps4
                    elem5e.spcRec.mapParam.put(grup, rec.getStr(TEXT));
                    break;
                case 37043:  //Макс. соотношение габаритов (б/м)
                    elem5e.spcRec.mapParam.put(grup, rec.getStr(TEXT));
                    break;
                case 37054:  //Коды основной текстуры изделия 
                    message(grup);
                    break;
                case 37080:  //Сообщение-предупреждение 
                    message(grup);
                    break;
                case 37095:  //Если признак системы конструкции 
                    message(grup);
                    break;
                case 37098:  //Бригада участок
                    message(grup);
                    break;
                case 37097:  //Трудозатраты по 
                    message(grup);
                    break;
                case 37108:  //Коэффициенты АКЦИИ 
                    message(grup);
                    break;
                case 37310:  //Сопротивление теплопередаче, м2*°С/Вт 
                    message(grup);
                    break;
                case 37320:  //Воздухопроницаемость, м3/ ч*м2
                    message(grup);
                    break;
                case 37330:  //Звукоизоляция, дБА 
                    message(grup);
                    break;
                case 37340:  //Коэффициент пропускания света 
                    message(grup);
                    break;
                case 37350:  //Сопротивление ветровым нагрузкам, Па 
                    message(grup);
                    break;
                case 37351:  //Номер поверхности 
                    message(grup);
                    break;
                default:
                    assert !(grup > 0 && grup < 50000) : "Код " + grup + "  не обработан!!!";
            }
        } catch (Exception e) {
            System.err.println("Ошибка:param.ElementVar.check()  parametr=" + grup + "    " + e);
            return false;
        }
        return true;
    }
}
