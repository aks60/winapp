package wincalc.constr.param;

import wincalc.constr.*;
import wincalc.constr.param.Par5s;
import dataset.Record;
import domain.eArtikl;
import domain.eSetting;
import domain.eSysprof;
import enums.LayoutArea;
import enums.TypeElem;
import enums.TypeGlass;
import enums.TypeJoin;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import wincalc.Wincalc;
import wincalc.model.ElemGlass;
import wincalc.model.ElemJoining;
import wincalc.model.ElemSimple;

//Составы
public class ElementVar extends Par5s {

    private int[] par = {31000, 31001, 31002, 31003, 31004, 31005, 31006, 31007, 31008, 31015, 31016, 31020, 31033, 31034, 31037, 31041, 31050, 31052, 31055, 31056, 31080, 31085, 31090, 31095, 31097, 31099, 37001, 37002, 37009, 37010, 37030, 37042, 37056, 37080, 37085, 37099};

    public ElementVar(Wincalc iwin) {
        super(iwin);
    }

    public boolean check(ElemSimple elem, List<Record> paramList) {

        //Цикл по параметрам состава
        for (Record rec : paramList) {
            if (filterParamDef(rec) == false) {
                return false;
            }
            int grup = rec.getInt(GRUP);
            try {
                switch (grup) {

                    case 31000:  //Для технологического кода контейнера 
                        Record sysprofRec2 = elem.sysprofRec;
                        Record artiklVRec = eArtikl.find(sysprofRec2.getInt(eSysprof.artikl_id), false);
                        if (artiklVRec.get(eArtikl.tech_code) == null) {
                            return false;
                        }
                        String[] strList = rec.getStr(TEXT).split(";");
                        String[] strList2 = artiklVRec.getStr(TEXT).split(";");
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
                    case 31001:  //Максимальное заполнение изделия, мм 
                        message(grup);
                        break;
                    case 31008:  //Эффективное заполнение изделия, мм 
                        message(rec.getInt(GRUP));
                        break;
                    case 31002:  //Если профиль 
                    case 37002:  //Если артикул профиля контура    
                        if (LayoutArea.ARCH == elem.layout() && "арочный".equals(rec.getStr(TEXT)) == false) {
                            return false;
                        } else if (LayoutArea.ARCH != elem.layout() && "прямой".equals(rec.getStr(TEXT)) == false) {
                            return false;
                        }
                        break;
                    case 31003:  //Если соединенный артикул  T-обр.
                        message(rec.getInt(GRUP));
                        break;
                    case 31004:  //Если прилегающий артикул 
                        HashMap<String, ElemJoining> mapJoin = elem.iwin().mapJoin;
                        pass = 0;
                        for (Map.Entry<String, ElemJoining> elemJoin : mapJoin.entrySet()) {
                            ElemJoining el = elemJoin.getValue();
                            if (TypeJoin.VAR4 == el.varJoin
                                    && el.joinElement1.artiklRec.equals(elem.artiklRec)
                                    && el.joinElement2.artiklRec.equals(rec.getStr(TEXT))) {
                                pass = 1;
                            }
                        }
                        if (pass == 0) {
                            return false;
                        }
                        break;
                    case 31005:  //Коды основной текстуры контейнера 
                    case 37005:  //Коды основной текстуры контейнера    
                        if (compareInt(rec.getStr(TEXT), elem.color1) == false) {
                            return false;
                        }
                        break;
                    case 31006:  //Коды внутр. текстуры контейнера 
                    case 37006:  //Коды внутр. текстуры контейнера    
                        if (compareInt(rec.getStr(TEXT), elem.color2) == false) {
                            return false;
                        }
                        break;
                    case 31007:  //Коды внешн. текстуры контейнера 
                    case 37007:  //Коды внешн. текстуры контейнера    
                        if (compareInt(rec.getStr(TEXT), elem.color3) == false) {
                            return false;
                        }
                        break;
                    case 31011:  //Толщина внешнего/внутреннего заполнения, мм 
                        message(rec.getInt(GRUP));
                        break;
                    case 31017:  //Код системы содержит строку 
                        message(rec.getInt(GRUP));
                        break;
                    case 31014:  //Заполнения одинаковой толщины 
                        message(rec.getInt(GRUP));
                        break;
                    case 31015:  //Разбиение профиля по уровням 
                        if (rec.getStr(TEXT).equals(elem.specificationRec.getParam("empty", 13015)) == false) {
                            return false;
                        }
                        break;
                    case 31016:  //Зазор_на_метр,_мм/Размер_,мм терморазрыва 
                        message(rec.getInt(GRUP));
                        break;
                    case 31019:  //Правило подбора текстур 
                        message(rec.getInt(GRUP));
                        break;
                    case 31020:  //Ограничение угла к горизонту, ° 
                        if (compareFloat(rec.getStr(TEXT), ((ElemSimple) elem).anglHoriz) == false) {
                            return false;
                        }
                        break;
                    case 31033:  //Если предыдущий артикул 
                        message(rec.getInt(GRUP));
                        break;
                    case 31034:  //Если следующий артикул 
                        message(rec.getInt(GRUP));
                        break;
                    case 31035:  //Уровень створки 
                        message(rec.getInt(GRUP));
                        break;
                    case 31037:  //Название фурнитуры содержит 
                        if (TypeElem.FULLSTVORKA == elem.owner().type()) {
                            if (rec.getStr(TEXT).contains(elem.artiklRec.getStr(eArtikl.name)) == false) {
                                return false;
                            }
                        }
                        break;
                    case 31040:  //Поправка габарита накладки, мм 
                        message(rec.getInt(GRUP));
                        break;
                    case 31041:  //Ограничение длины профиля, мм 
                        if (compareFloat(rec.getStr(TEXT), elem.width()) == false) {
                            return false;
                        }
                        break;
                    case 31050:  //Контейнер имеет тип 
                        TypeElem type = elem.type();
                        if (type.value != Integer.valueOf(rec.getStr(TEXT))) {
                            return false;
                        }
                        break;
                    case 31051:  //Если створка фурнитуры 
                        message(rec.getInt(GRUP));
                        break;
                    case 31052:  //Поправка в спецификацию, мм 
                        if (elem.layout() == LayoutArea.ARCH) {
                            elem.specificationRec.putParam(rec.getInt(GRUP), rec.getStr(TEXT));
                        }
                        break;
                    case 31054:  //Коды основной текстуры изделия 
                        message(rec.getInt(GRUP));
                        break;
                    case 31055:  //Коды внутр. и внешн. текстуры изд.
                    case 37055:  //Коды внутр. и внешн. текстуры изд.    
                        if ((compareInt(rec.getStr(TEXT), elem.color2) == true
                                && compareInt(rec.getStr(TEXT), elem.color3) == true) == false) {
                            return false;
                        }
                        break;
                    case 31056:  //Коды внутр. или внеш. текстуры изд. 
                    case 37056:  //Коды внут. или внеш. текстуры изд. 
                        if ((compareInt(rec.getStr(TEXT), elem.color2) == true
                                || compareInt(rec.getStr(TEXT), elem.color3) == true) == false) {
                            return false;
                        }
                        break;
                    case 31057:  //Внутренняя текстура равна внешней 
                        message(rec.getInt(GRUP));
                        break;
                    case 31060:  //Допустимый угол между плоскостями, ° 
                        message(rec.getInt(GRUP));
                        break;
                    case 31073:  //Отправочная марка фасада 
                        message(rec.getInt(GRUP));
                        break;
                    case 31074:  //На прилегающей створке 
                        message(rec.getInt(GRUP));
                        break;
                    case 31080:  //Сообщение-предупреждение 
                        message(rec.getInt(GRUP));
                        break;
                    case 31081:  //Для внешнего/внутреннего угла плоскости, ° 
                        message(rec.getInt(GRUP));
                        break;
                    case 31085:  //Надпись на элементе 
                    case 37085:  //Надпись на элементе     
                        elem.specificationRec.putParam(rec.getInt(GRUP), rec.getStr(TEXT));
                        break;
                    case 31090:  //Изменение сторон покраски 
                        if (rec.getStr(TEXT).equals(elem.specificationRec.getParam("empty", 31090)) == false) {
                            return false;
                        }
                        break;
                    case 31095:  //Если признак системы конструкции 
                        message(rec.getInt(GRUP));
                        break;
                    case 31098:  //Бригада, участок) 
                        message(rec.getInt(GRUP));
                        break;
                    case 31099:  //Трудозатраты, ч/ч. 
                    case 37099:  //Трудозатраты, ч/ч.    
                        elem.specificationRec.putParam(rec.getInt(GRUP), rec.getStr(TEXT));
                        break;
                    case 31097:  //Трудозатраты по длине 
                        message(rec.getInt(GRUP));
                        break;
                    case 31800:  //Код обработки 
                        message(rec.getInt(GRUP));
                        break;
                    case 31801:  //Доп.обработки
                        message(rec.getInt(GRUP));
                        break;
                    case 37001:  //Установка жалюзи 
                        message(rec.getInt(GRUP));
                        break;
                    case 37008:  //Тип проема 
                        message(rec.getInt(GRUP));
                        break;
                    case 37009:  //Тип заполнения 
                        //Все, Произвольное, Прямоугольное, Арочное                                            
                        message(rec.getInt(GRUP), elem.type(), ((ElemGlass) elem).typeGlass, rec.getStr(TEXT));
                        if ("Прямоугольное".equals(rec.getStr(TEXT)) && TypeGlass.RECTANGL.text().equals(((ElemGlass) elem).typeGlass.text()) == false) {
                            return false;
                        } else if ("Арочное".equals(rec.getStr(TEXT)) && TypeGlass.ARCH.text().equals(((ElemGlass) elem).typeGlass.text()) == false) {
                            return false;
                        }
                        break;
                    case 37010:  //Ограничение ширины/высоты листа, мм 
                        message(rec.getInt(GRUP), elem.type(), rec.getStr(TEXT));
                        arr = parserFloat2(rec.getStr(TEXT));
                        if (((arr[0] > elem.width() && arr[1] < elem.width()) || (arr[0] > elem.height() && arr[1] < elem.height())) == false) {
                            return false;
                        }
                        if (((arr[2] > elem.width() && arr[2] < elem.width()) || (arr[2] > elem.height() && arr[3] < elem.height())) == false) {
                            return false;
                        }
                        break;
                    case 37017:  //Код системы содержит строку 
                        message(rec.getInt(GRUP));
                        break;
                    case 37030:  //Минимальная площадь или Ограничение площади, кв.м. для Ps4
                        message(rec.getInt(GRUP));
                        if ("ps4".equals(eSetting.find(2).getStr(eSetting.val))) {
                            arr = parserFloat(rec.getStr(TEXT));
                            arr2 = arr;
                        } else if ("ps3".equals(eSetting.find(2).getStr(eSetting.val))) {
                            if (elem.width() / 1000 * elem.height() / 1000 < rec.getInt(GRUP)) {
                                return false;
                            }
                        }
                        break;
                    case 37031:  //Максимальная площадь 
                        message(rec.getInt(GRUP));
                        if ("ps3".equals(eSetting.find(2).getStr(eSetting.val))) {
                            if (elem.width() / 1000 * elem.height() / 1000 > rec.getInt(GRUP)) {
                                return false;
                            }
                        }
                        break;
                    case 37042:  //Допустимое соотношение габаритов б/м
                        message(rec.getInt(GRUP));
                        break;
                    case 37054:  //Коды основной текстуры изделия 
                        message(rec.getInt(GRUP));
                        break;
                    case 37080:  //Сообщение-предупреждение 
                        message(rec.getInt(GRUP));
                        break;
                    case 37095:  //Если признак системы конструкции 
                        message(rec.getInt(GRUP));
                        break;
                    case 37098:  //Бригада участок
                        message(rec.getInt(GRUP));
                        break;
                    case 37097:  //Трудозатраты по 
                        message(rec.getInt(GRUP));
                        break;
                    case 37108:  //Коэффициенты АКЦИИ 
                        message(rec.getInt(GRUP));
                        break;
                    case 37310:  //Сопротивление теплопередаче, м2*°С/Вт 
                        message(rec.getInt(GRUP));
                        break;
                    case 37320:  //Воздухопроницаемость, м3/ ч*м2
                        message(rec.getInt(GRUP));
                        break;
                    case 37330:  //Звукоизоляция, дБА 
                        message(rec.getInt(GRUP));
                        break;
                    case 37340:  //Коэффициент пропускания света 
                        message(rec.getInt(GRUP));
                        break;
                    case 37350:  //Сопротивление ветровым нагрузкам, Па 
                        message(rec.getInt(GRUP));
                        break;
                    case 37351:  //Номер поверхности 
                        message(rec.getInt(GRUP));
                        break;
                    default:
                        if (grup > 0) {
                            message(rec.getInt(GRUP));
                        }
                        break;
                }
            } catch (Exception e) {
                System.out.println("wincalc.constr.param.ElementVar.check()  parametr=" + grup + "    " + e);
                return false;
            }
        }
        return true;
    }
}
