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
    //Но при проверке параметров использую оригин. мат. ценность. (Непонятно)
    public void build() {
        try {
            //Arrays.asList(UseArtiklTo.values()).forEach(useArtiklTo -> useArtiklTo.sysprofRec = eSysprof.find2(iwin().nuni, useArtiklTo)); //профили по приоритету, до ручного выбора

            for (UseArtiklTo useArtiklTo : UseArtiklTo.values()) { //цыкл по списку применений артикулов
                for (ElemSimple elemSimp : iwin().listElem) { //цыкл по списку элементов конструкции
                    if (elemSimp.useArtiklTo() == useArtiklTo) {

                        int artikl_id = elemSimp.sysprofRec.getInt(eSysprof.artikl_id); //ищем текстуры не на аналоге 
                        List<Record> artdetList = eArtdet.find(artikl_id); //список текстур артикула             
                        elemSimp.artdetRec = artdet(artdetList); //текстура артикула

                        int series_id = elemSimp.artiklRec.getInt(eArtikl.series_id);
                        List<Record> elementList2 = eElement.find(series_id); //варианты состава для серии профилей
                        detail(elementList2, elemSimp);                        
                        
                        if(elemSimp.id() == 6.1f) {
                            System.out.println("ТЕСТОВАЯ ЗАПЛАТКА");
                        }
                        
                        if (elemSimp.artiklRec.getInt(eArtikl.analog_id) != -1) { 
                            artikl_id = elemSimp.artiklRec.getInt(eArtikl.analog_id);
                        } else {
                            artikl_id = elemSimp.artiklRec.getInt(eArtikl.id);
                        }
                        List<Record> elementList3 = eElement.find2(artikl_id); //варианты состава для артикула профиля
                        detail(elementList3, elemSimp);
                    }
                }
            }
            
            
        } catch (Exception e) {
            System.err.println("Ошибка wincalc.constr.Сomposition.build()");
        }
    }

    protected void detail(List<Record> elementList, ElemSimple elemSimple) {
        try {
            for (Record elementRec : elementList) { //цыкл по вариантам

                int element_id = elementRec.getInt(eElement.id);
                List<Record> elempar1List = eElempar1.find3(element_id); //список параметров вариантов использования
//                if(elempar1List.get(0).getInt(eElempar1.grup) == 31000 || elempar1List.get(0).getInt(eElempar1.grup) == 31002) {
//                    int m= 0;
//                }
                    
                boolean out = elementVar.check(elemSimple, elempar1List); //ФИЛЬТР вариантов
                if (out == true) {
                    List<Record> elemdetList = eElemdet.find(element_id);                   
                    for (Record elemdetRec : elemdetList) { //цыкл по детализации

                        HashMap<Integer, String> hmParam = new HashMap(); //тут накапливаются параметры детализации
                        int elemdet_id = elemdetRec.getInt(eElemdet.id);
                        List<Record> elempar2List = eElempar2.find3(elemdet_id); //список параметров детализации
                        boolean out2 = elementDet.check(hmParam, elemSimple, elempar2List);//ФИЛЬТР детализации
                        if (out2 == true) {

                            Record artiklRec = eArtikl.find(elemdetRec.getInt(eElemdet.artikl_id), false);
                            Specification specif = new Specification(artiklRec, elemSimple, hmParam);
                            specif.setColor(elemSimple, elemdetRec);
                            specif.layout = "СОСТ";
                            ((ElemSimple) elemSimple).addSpecifSubelem(specif); //добавим спецификацию в элемент
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
