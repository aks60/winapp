package wincalc.constr;

import dataset.Record;
import domain.eArtdet;
import domain.eArtikl;
import domain.eElemdet;
import domain.eElement;
import domain.eElempar1;
import domain.eElempar2;
import domain.eSysprof;
import enums.TypeElem;
import enums.UseArtiklTo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import wincalc.Wincalc;
import wincalc.constr.param.ElementDet;
import wincalc.constr.param.ElementVar;
import wincalc.model.ElemFrame;
import wincalc.model.ElemSimple;

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
    public void build() {
        try {
            //Цыкл по списку элементов конструкции
            for (ElemSimple elem5e : iwin().listElem) {
                
                //Ищем текстуры не на аналоге 
                int artikl_id = elem5e.sysprofRec.getInt(eSysprof.artikl_id);
                List<Record> artdetList = eArtdet.find(artikl_id); //список текстур артикула             
                elem5e.artdetRec = artdet(artdetList); //текстура артикула, нужен подбор текстуры!!!
                //elem5e.set

                //Варианты состава для серии профилей
                int series_id = elem5e.artiklRec.getInt(eArtikl.series_id);
                List<Record> elementList2 = eElement.find(series_id);
                detail(elementList2, elem5e);

//                if (elem5e.artiklRec.getInt(eArtikl.id) == 4474) {
//                    System.out.println("ТЕСТОВАЯ ЗАПЛАТКА");
//                } 
                //Варианты состава для артикула профиля
                if (elem5e.artiklRec.getInt(eArtikl.analog_id) != -1) {
                    artikl_id = elem5e.artiklRec.getInt(eArtikl.analog_id);
                } else {
                    artikl_id = elem5e.artiklRec.getInt(eArtikl.id);
                }
                List<Record> elementList3 = eElement.find2(artikl_id);
                detail(elementList3, elem5e);
            }
        } catch (Exception e) {
            System.err.println("Ошибка wincalc.constr.Сomposition.build()");
        }
    }

    protected void detail(List<Record> elementList, ElemSimple elem5e) {
        try {
            for (Record elementRec : elementList) { //цыкл по вариантам

                int element_id = elementRec.getInt(eElement.id);
                List<Record> elempar1List = eElempar1.find3(element_id); //список параметров вариантов использования
                boolean out = elementVar.check(elem5e, elempar1List); //ФИЛЬТР вариантов
                if (out == true) {

                    List<Record> elemdetList = eElemdet.find(element_id);
                    for (Record elemdetRec : elemdetList) { //цыкл по детализации

                        HashMap<Integer, String> hmParam = new HashMap(); //тут накапливаются параметры детализации
                        int elemdet_id = elemdetRec.getInt(eElemdet.id);
                        List<Record> elempar2List = eElempar2.find3(elemdet_id); //список параметров детализации                       
                        boolean out2 = elementDet.check(hmParam, elem5e, elempar2List);//ФИЛЬТР детализации
                        if (out2 == true) {

                            Record artiklRec = eArtikl.find(elemdetRec.getInt(eElemdet.artikl_id), false);
                            Specification specif = new Specification(artiklRec, elem5e, hmParam);
                            specif.setColor(elem5e, elemdetRec);
                            specif.section = "СОСТ";
                            elem5e.addSpecification(specif); //добавим спецификацию в элемент
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка wincalc.constr.Сomposition.nested()");
        }
    }

    private Record artdet(List<Record> artdetList) {
        return artdetList.stream().filter(rec -> rec.getInt(eArtdet.color_fk) == iwin().color1).findFirst().orElse(eArtdet.record());
    }
}
