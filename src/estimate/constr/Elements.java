package estimate.constr;

import dataset.Record;
import domain.eArtdet;
import domain.eArtikl;
import domain.eElemdet;
import domain.eElement;
import domain.eElempar1;
import domain.eElempar2;
import domain.eJoining;
import domain.eSysprof;
import enums.TypeElem;
import enums.UseArtiklTo;
import enums.UseSide;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import estimate.Wincalc;
import estimate.constr.param.ElementDet;
import estimate.constr.param.ElementVar;
import estimate.model.Com5t;
import estimate.model.ElemFrame;
import estimate.model.ElemSimple;

/**
 * Составы.
 */
public class Elements extends Cal5e {

    private ElementVar elementVar = null;
    private ElementDet elementDet = null;

    public Elements(Wincalc iwin) {
        super(iwin);
        elementVar = new ElementVar(iwin);
        elementDet = new ElementDet(iwin);
    }

    //Идем по списку профилей, смотрю есть аналог работаю с ним.
    //Но при проверке параметров использую оригин. мат. ценность. (Непонятно!!!)
    public void calc() {
        listVariants.clear();
        LinkedList<ElemSimple> listElem = iwin().rootArea.listElem(TypeElem.FRAME_SIDE, TypeElem.STVORKA_SIDE, TypeElem.IMPOST);
        try {
            //Цикл по списку элементов конструкции
            for (ElemSimple elem5e : listElem) {                
                
                elem5e.setSpecific();
                
                //Варианты состава для серии профилей
                int series_id = elem5e.artiklRec.getInt(eArtikl.series_id);
                List<Record> elementList2 = eElement.find(series_id);
                detail(elementList2, elem5e);

                //Варианты состава для артикула профиля
                int artikl_id = (elem5e.artiklRec.getInt(eArtikl.analog_id) != -1) ? elem5e.artiklRec.getInt(eArtikl.analog_id) : elem5e.artiklRec.getInt(eArtikl.id);
                List<Record> elementList3 = eElement.find2(artikl_id);
                detail(elementList3, elem5e);
                
                elem5e.setSpecific();
            }                       
        } catch (Exception e) {
            System.err.println("estimate.constr.Elements.calc() " + e);
        }       
    }

    protected void detail(List<Record> elementList, ElemSimple elem5e) {
        try {
            //Цикл по вариантам
            for (Record elementRec : elementList) {
                int element_id = elementRec.getInt(eElement.id);
                List<Record> elempar1List = eElempar1.find3(element_id); //список параметров вариантов использования
                listVariants.add(elementRec.getInt(eElement.id)); //сделано для запуска формы Elements из формы Systree

                //ФИЛЬТР вариантов, параметры накапливаются в спецификации элемента
                if (elementVar.check(elem5e, elempar1List) == true) {
                    List<Record> elemdetList = eElemdet.find(element_id);

                    //Цикл по детализации
                    for (Record elemdetRec : elemdetList) {
                        HashMap<Integer, String> mapParam = new HashMap(); //тут накапливаются параметры детализации
                        int elemdet_id = elemdetRec.getInt(eElemdet.id);
                        List<Record> elempar2List = eElempar2.find3(elemdet_id); //список параметров детализации 

                        //ФИЛЬТР детализации, параметры накапливаются в mapParam
                        if (elementDet.check(mapParam, elem5e, elempar2List) == true) {

                            Record artiklRec = eArtikl.find(elemdetRec.getInt(eElemdet.artikl_id), false);
                            Specification specif = new Specification(artiklRec, elem5e, mapParam);
                            specif.setColor(elem5e, elemdetRec);
                            specif.place = "СОСТ";
                            elem5e.addSpecific(specif); //добавим спецификацию в элемент
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка wincalc.constr.Сomposition.nested() " + e);
        }
    }
}
