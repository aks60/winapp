package estimate.constr.param;

import dataset.Record;
import domain.eArtikl;
import domain.eSysprof;
import enums.LayoutArea;
import enums.TypeElem;
import enums.TypeJoin;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import estimate.Wincalc;
import estimate.constr.Util;
import estimate.model.ElemJoining;
import estimate.model.ElemSimple;

//Составы
public class ElementVar extends Par5s {

    private int[] par = {31000, 31001, 31002, 31003, 31004, 31005, 31006, 31007, 31008, 31015, 31016, 31020, 31033, 31034, 31037, 31041, 31050, 31052, 31055, 31056, 31080, 31085, 31090, 31095, 31097, 31099, 37001, 37002, 37009, 37010, 37030, 37042, 37056, 37080, 37085, 37099};

    public ElementVar(Wincalc iwin) {
        super(iwin);
    }

    public boolean check(ElemSimple elem5e, List<Record> paramList) {

        //Цикл по параметрам состава
        for (Record rec : paramList) {
            if (filterParamDef(rec) == false) {
                return false;
            }
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
                                if (str.equals(str2)) {
                                    ret2 = true;
                                }
                            }
                        }
                        if (ret2 == false) {
                            return false;
                        }
                    }
                    break;
                    case 31001:  //Максимальное заполнение изделия, мм 
                        message(grup);
                        break;
                    case 31002:  //Если профиль 
                        if ("арочный".equals(rec.getStr(TEXT)) == false && LayoutArea.ARCH == elem5e.layout() == true) {
                            return false;
                        } else if ("прямой".equals(rec.getStr(TEXT)) == false && LayoutArea.ARCH != elem5e.layout() == true) {
                            return false;
                        }
                        break;
                    case 31003:  //Если соединенный артикул  T-обр.
                        message(grup);
                        break;
                    case 31004: //Если прилегающий артикул 
                    {
                        HashMap<String, ElemJoining> mapJoin = elem5e.iwin().mapJoin;
                        pass = 0;

                        for (Map.Entry<String, ElemJoining> entry : mapJoin.entrySet()) {
                            ElemJoining el = entry.getValue();
                        }

                        for (Map.Entry<String, ElemJoining> entry : mapJoin.entrySet()) {
                            ElemJoining el = entry.getValue();
                            if (TypeJoin.VAR10 == el.typeJoin
                                    && el.joinElement1.artiklRec.getStr(eArtikl.code).equals(elem5e.artiklRec.getStr(eArtikl.code))
                                    && el.joinElement2.artiklRec.getStr(eArtikl.code).equals(rec.getStr(TEXT))) {
                                pass = 1;
                            }
                        }
                        if (pass == 0) {
                            return false;
                        }
                    }
                    break;
                    case 31005:  //Коды основной текстуры контейнера 
                    case 37005:  //Коды основной текстуры контейнера 
                        if (compareInt(rec.getStr(TEXT), elem5e.colorID1) == false) {
                            return false;
                        }
                        break;
                    case 31006:  //Коды внутр. текстуры контейнера 
                    case 37006:  //Коды внутр. текстуры контейнера  
                        if (compareInt(rec.getStr(TEXT), elem5e.colorID2) == false) {
                            return false;
                        }
                        break;
                    case 31007:  //Коды внешн. текстуры контейнера 
                    case 37007:  //Коды внешн. текстуры контейнера  
                        if (compareInt(rec.getStr(TEXT), elem5e.colorID3) == false) {
                            return false;
                        }
                        break;
                    case 31008:  //Эффективное заполнение изделия, мм 
                        message(grup);
                        break;
                    case 31011:  //Толщина внешнего/внутреннего заполнения, мм 
                        message(grup);
                        break;
                    case 31017:  //Код системы содержит строку 
                        message(grup);
                        break;
                    case 31014:  //Заполнения одинаковой толщины 
                        message(grup);
                        break;
                    case 31015:  //Разбиение профиля по уровням 
                        if (rec.getStr(TEXT).equals(elem5e.specificationRec.getParam("empty", 13015)) == false) {
                            return false;
                        }
                        break;
                    case 31016:  //Зазор_на_метр,_мм/Размер_,мм терморазрыва 
                        message(grup);
                        break;
                    case 31019:  //Правило подбора текстур 
                        message(grup);
                        break;
                    case 31020:  //Ограничение угла к горизонту, ° 
                        if (compareFloat(rec.getStr(TEXT), ((ElemSimple) elem5e).anglHoriz) == false) {
                            return false;
                        }
                        break;
                    case 31033:  //Если предыдущий артикул 
                        message(grup);
                        break;
                    case 31034:  //Если следующий артикул 
                        message(grup);
                        break;
                    case 31035:  //Уровень створки 
                        message(grup);
                        break;
                    case 31037:  //Название фурнитуры содержит 
                        if (TypeElem.STVORKA == elem5e.owner().type()) {
                            return rec.getStr(TEXT).contains(elem5e.artiklRec.getStr(eArtikl.name));
                        }
                        return false;
                    case 31040:  //Поправка габарита накладки, мм 
                        message(grup);
                        break;
                    case 31041:  //Ограничение длины профиля, мм 
                        Object obj = elem5e.length();
                        if (compareFloat(rec.getStr(TEXT), elem5e.length()) == false) {
                            return false;
                        }
                        break;
                    case 31050:  //Контейнер имеет тип 
                        TypeElem type = elem5e.type();
                        if (type.id != Integer.valueOf(rec.getStr(TEXT))) {
                            return false;
                        }
                        break;
                    case 31051:  //Если створка фурнитуры 
                        message(grup);
                        break;
                    case 31052:  //Поправка в спецификацию, мм 
                        if (elem5e.layout() == LayoutArea.ARCH) {
                            elem5e.specificationRec.width = elem5e.specificationRec.width + Float.valueOf(rec.getFloat(TEXT));
                        }
                        break;
                    case 31054:  //Коды основной текстуры изделия 
                        message(grup);
                        break;
                    case 31055:  //Коды внутр. и внешн. текстуры изд.
                    case 37055:  //Коды внутр. и внешн. текстуры изд. 
                        if ((compareInt(rec.getStr(TEXT), elem5e.colorID2) == true
                                && compareInt(rec.getStr(TEXT), elem5e.colorID3) == true) == false) {
                            return false;
                        }
                        break;
                    case 31056:  //Коды внутр. или внеш. текстуры изд. 
                    case 37056:  //Коды внут. или внеш. текстуры изд. 
                        if ((compareInt(rec.getStr(TEXT), elem5e.colorID2) == true
                                || compareInt(rec.getStr(TEXT), elem5e.colorID3) == true) == false) {
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
                        elem5e.specificationRec.putParam(grup, rec.getStr(TEXT));
                        break;
                    case 31090:  //Изменение сторон покраски 
                        if (rec.getStr(TEXT).equals(elem5e.specificationRec.getParam("empty", 31090)) == false) {
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
                        elem5e.specificationRec.putParam(grup, rec.getStr(TEXT));
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
                        message(grup);
                        break;
                    case 37009:  //Тип заполнения 
                        //Все, Произвольное, Прямоугольное, Арочное                                            
                        if ("Прямоугольное".equals(rec.getStr(TEXT)) && (TypeElem.RECTANGL.equals(elem5e.owner().type()) || TypeElem.AREA.equals(elem5e.owner().type()) == false)) {
                            return false;
                        } else if ("Арочное".equals(rec.getStr(TEXT)) && TypeElem.ARCH.equals(elem5e.owner().type()) == false) {
                            return false;
                        }
                        break;
                    case 37010:  //Ограничение ширины/высоты листа, мм 
                        Float[] arr1 = parserFloat2(rec.getStr(TEXT));
                        if (((arr1[0] > elem5e.width() && arr1[1] < elem5e.width()) || (arr1[0] > elem5e.height() && arr1[1] < elem5e.height())) == false) {
                            return false;
                        }
                        if (((arr1[2] > elem5e.width() && arr1[2] < elem5e.width()) || (arr1[2] > elem5e.height() && arr1[3] < elem5e.height())) == false) {
                            return false;
                        }
                        break;
                    case 37017:  //Код системы содержит строку 
                        message(grup);
                        break;
                    case 37030:  //Минимальная площадь или Ограничение площади, кв.м. для Ps4                        
                        if ("ps4".equals(versionDb)) {
                            Float[] arr2 = parserFloat(rec.getStr(TEXT));
                            if (elem5e.width() / 1000 * elem5e.height() / 1000 < arr2[0]) {
                                return false;
                            }
                            if (elem5e.width() / 1000 * elem5e.height() / 1000 > arr2[1]) {
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
                        elem5e.specificationRec.putParam(grup, rec.getStr(TEXT));
                        break;
                    case 37043:  //Макс. соотношение габаритов (б/м)
                        elem5e.specificationRec.putParam(grup, rec.getStr(TEXT));
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
                        if (grup > 0) {
                            message(grup);
                        }
                        break;
                }
            } catch (Exception e) {
                System.err.println("wincalc.constr.param.ElementVar.check()  parametr=" + grup + "    " + e);
                return false;
            }
        }
        return true;
    }
}
