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
            
    public Elements(Wincalc iwin, Constructiv calc) {
        super(iwin, calc);
        elementVar = new ElementVar(iwin, calc);
        elementDet = new ElementDet(iwin, calc);
    }

    //Идем по списку профилей, смотрю есть аналог работаю с ним.
    //Но при проверке параметров использую вирт. мат. ценность.
    public void build() {
        try {
            Arrays.asList(UseArtiklTo.values()).forEach(useArtiklTo -> useArtiklTo.sysprofRec = eSysprof.find2(iwin().nuni, useArtiklTo)); //профили по приоритету, до ручного выбора
            
            for (UseArtiklTo useArtiklTo : UseArtiklTo.values()) { //цыкл по списку применений артикулов
                for (ElemSimple elemSimp : iwin().listElem) { //цыкл по списку элементов конструкции
                    if (elemSimp.useArtiklTo() == useArtiklTo) {

                        int artikl_id = elemSimp.sysprofRec.getInt(eSysprof.artikl_id); //ищем не на аналоге 
                        List<Record> artdetList = eArtdet.find(artikl_id); //список текстур артикула             
                        elemSimp.artdetRec = artdet(artdetList); //текстура артикула

                        int series_id = elemSimp.artiklRec.getInt(eArtikl.series_id);
                        List<Record> elementList2 = eElement.find(series_id); //варианты состава для серии профилей
                        nested(elementList2, elemSimp);

                        artikl_id = elemSimp.artiklRec.getInt(eArtikl.id);
                        List<Record> elementList3 = eElement.find2(artikl_id); //варианты состава для артикула профиля
                        nested(elementList3, elemSimp);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка wincalc.constr.Сomposition.build()");
        }
    }
    
    protected void nested(List<Record> elementList, ElemSimple ElemSimple) {
        try {           
            for (Record elementRec : elementList) { //цыкл по вариантам состава

                int element_id = elementRec.getInt(eElement.id);
                List<Record> elempar1List = eElempar1.find3(element_id); //список параметров вариантов использования
                boolean out = elementVar.check(ElemSimple, elempar1List); //ФИЛЬТР вариантов
//                if (out == true) {
//                    //artiklTech = elemBase.getArticlesRec(); 
//                    //Artikls.get(constr, vstalstRec.anumb, false); //запишем технологический код контейнера
//
//                    List<Record> elemdetList = eElemdet.find(element_id);                   
//                    for (Record elendetRec : elemdetList) { //цыкл по вариантам спецификаций
//
//                        HashMap<Integer, String> hmParam = new HashMap(); //тут накапливаются параметры
//                        int elemdet_id = elendetRec.getInt(eElemdet.id);
//                        ArrayList<Record> elempar2List = eElempar2.find(elemdet_id); //список параметров спецификации
//                        boolean out2 = elementDet.check(hmParam, ElemSimple, elempar2List);//ФИЛЬТР спецификаций
//                        if (out2 == true) {
//
//                            Record artiklRec = eArtikl.find(elendetRec.getInt(eElemdet.artikl_id), false);
//                            Specification specif = new Specification(artiklRec, ElemSimple, hmParam);
//                            specif.setColor(ElemSimple, elendetRec);
//                            specif.element = "СОСТ";
//                            ((ElemSimple) ElemSimple).addSpecifSubelem(specif); //добавим спецификацию в элемент
//                        }
//                    }
 //               }
            }
        } catch (Exception e) {
            System.err.println("Ошибка wincalc.constr.Сomposition.nested()");
        }
    }

    private Record artdet(List<Record> artdetList) {
        return artdetList.stream().filter(rec -> rec.getInt(eArtdet.color_fk) == iwin().color1).findFirst().orElse(eArtdet.record());
    }

    public void build2() {
        try {
            LinkedList<ElemFrame> listFrameBox = iwin().rootArea.listElem(TypeElem.FRAME_BOX); //список рам конструкции  
            LinkedList<ElemFrame> listFrameStv = iwin().rootArea.listElem(TypeElem.FRAME_BOX); //список рам створок конструкции

            Record sysprofRec = eSysprof.find2(iwin().nuni, UseArtiklTo.FRAME); //первая по приоритету рама в системе 
            int artikl_id = sysprofRec.getInt(eSysprof.artikl_id); //ищем не на аналоге                
            List<Record> artdetList = eArtdet.find(artikl_id); //список спецификаций рамы в системе              
            Record artdetRec = artdet(artdetList); //спецификация рамы в системе (подбор текстуры)

            //Цыкл по рамам
            for (ElemFrame elemFrame : listFrameBox) {

                elemFrame.setSpecifElement(sysprofRec);

//                int series_id = elemFrame.artiklRec.getInt(eArtikl.series_id);
//                List<Record> elementList2 = eElement.find(series_id); //состав для серии профилей
//                nested(elementList2, elemFrame);
                //int artikl_id = elemFrame.artiklRec.getInt(eArtikl.id);
                //List<Record> elementList = eElement.find2(artikl_id); //состав для артикула профиля
                //nested(elementList, recordFrame);
                elemFrame.specificationRec.width = elemFrame.specificationRec.width
                        + Float.valueOf(String.valueOf(elemFrame.specificationRec.getParam(0, 31052)));
            }
//            for (Record record : sysprofList) {
//                boolean is = false;
//                if (UserArtikl.IMPOST.id == record.getInt(eSysprof.use_type)) {
//                    List<Record> artdetList = eArtdet.find(record.getInt(eArtdet.id)); //подбор текстуры, ищем не на аналоге
//                    for (Record artdetRec : artdetList) {
//
//                        int color_fk = artdetRec.getInt(eArtdet.color_fk);
//                        //TODO Нужна проверка для color_fk < 0
//                        if (color_fk == iwin().color1) {
//                            is = true;
//                            LinkedList<ElemImpost> impostList = iwin().rootArea.listElem(TypeElem.IMPOST);
//                            for (ElemImpost elemInpost : impostList) {
//
//                                int series_id = elemInpost.artiklRec.getInt(eArtikl.series_id);
//                                elemInpost.setSpecifElement(record);
//                                List<Record> elementList2 = eElement.find(series_id); //состав для серии профилей
//                                nested(elementList2, elemInpost);
//                                List<Record> elementList = eElement.find(series_id); //.anumb); //состав для артикула профиля
//                                nested(elementList, elemInpost);
//                            }
//                        }
//                    }
//                }
//                if (is == true) {
//                    break;
//                }
//            }
//            for (Record record : sysprofList) {
//                boolean is = false;
//                if (UserArtikl.STVORKA.id == record.getInt(eSysprof.use_type)) {
//                    List<Record> artdetList = eArtdet.find(record.getInt(eArtdet.id)); //подбор текстуры, ищем не на аналоге
//                    for (Record artdetRec : artdetList) {
//
//                        int color_fk = artdetRec.getInt(eArtdet.color_fk);
//                        //TODO Нужна проверка для color_fk < 0
//                        if (color_fk == iwin().color1) {
//                            is = true;
//                            LinkedList<ElemImpost> impostList = iwin().rootArea.listElem(TypeElem.IMPOST);
//                            for (ElemImpost elemInpost : impostList) {
//
//                                elemInpost.setSpecifElement(record);
//                                List<Record> elementList2 = eElement.find(elemInpost.artiklRec.getInt(eArtikl.series_id)); //состав для серии профилей
//                                nested(elementList2, elemInpost);
//                                List<Record> elementList = eElement.find(elemInpost.artiklRec.getInt(eArtikl.code)); //.anumb); //состав для артикула профиля
//                                nested(elementList, elemInpost);
//                            }
//                        }
//                    }
//                }
//                if (is == true) {
//                    break;
//                }
//            }
        } catch (Exception e) {
            System.err.println("Ошибка wincalc.constr.Сomposition.build()");
        }
    }    
}

