package wincalc.constr;

import dataset.Record;
import domain.eArtdet;
import domain.eArtikl;
import domain.eColor;
import domain.eElemdet;
import domain.eElement;
import domain.eElempar1;
import domain.eElempar2;
import domain.eSysprof;
import enums.TypeElem;
import enums.UseArtikl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import wincalc.Wincalc;
import wincalc.model.Com5t;
import wincalc.model.ElemFrame;
import wincalc.model.ElemImpost;
import wincalc.model.ElemSimple;

/**
 * Составы.
 */
public class Elements extends Cal5e {

    public Elements(Wincalc iwin, CalcConstructiv calc) {
        super(iwin, calc);
    }

    //Идем по списку профилей, смотрю есть аналог работаю с ним.
    //Но при проверке параметров использую вирт. мат. ценность.
    public void build() {
        try {
            LinkedList<ElemFrame> listFrameBox = iwin().rootArea.listElem(TypeElem.FRAME_BOX); //список рам конструкции  
            LinkedList<ElemFrame> listFrameStv = iwin().rootArea.listElem(TypeElem.FRAME_BOX); //список створок конструкции
            
            Record sysprofRec = eSysprof.find2(iwin().nuni, UseArtikl.FRAME); //находим первую по приоритету запись профиля в системе 
            int artikl_id = sysprofRec.getInt(eSysprof.artikl_id); //ищем не на аналоге                
            List<Record> artdetList = eArtdet.find(artikl_id); //список спецификаций артикула профиля в системе  
            
            boolean is = false;
            //Цыкл по спецификациям артикула профиля в системе
            for (Record artdetRec : artdetList) { 

                int color_fk = artdetRec.getInt(eArtdet.color_fk); //TODO Нужна проверка для color_fk < 0
                //Подбор текстуры
                if (color_fk == iwin().color1) {
                    is = true;
                    //Цыкл по рамам
                    for (ElemFrame elemFrame : listFrameBox) {

                        elemFrame.setSpecifElement(sysprofRec);
                        
                        int series_id = elemFrame.artiklRec.getInt(eArtikl.series_id);
                        List<Record> elementList2 = eElement.find(series_id); //состав для серии профилей
                        nested(elementList2, elemFrame);
                        
                        //int artikl_id = elemFrame.artiklRec.getInt(eArtikl.id);
                        //List<Record> elementList = eElement.find2(artikl_id); //состав для артикула профиля
                        //nested(elementList, recordFrame);

                        elemFrame.specificationRec.width = elemFrame.specificationRec.width
                                + Float.valueOf(String.valueOf(elemFrame.specificationRec.getParam(0, 31052)));
                    }
                }
                if (is == true) {
                    break;
                }
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

    protected void nested(List<Record> elementList, Com5t com5t) {
        try {
            System.out.println(elementList);
            //цикл по составам
            for (Record elementRec : elementList) {

                ArrayList<Record> elempar1List = eElempar1.find(elementRec.getInt(eElement.id));
                boolean out = calc().paramVariant.checkParvstm(com5t, elempar1List); //ФИЛЬТР вариантов
                if (out == true) {
                    //artiklTech = elemBase.getArticlesRec(); //Artikls.get(constr, vstalstRec.anumb, false); //запишем технологический код контейнера
                    List<Record> elemdetList = eElemdet.find(elementRec.getInt(eElemdet.element_id));
                    //Цикл по спецификации
                    for (Record elendetRec : elemdetList) {

                        HashMap<Integer, String> hmParam = new HashMap(); //тут накапливаются параметры
                        ArrayList<Record> parvstsList = eElempar2.find(elendetRec.getInt(eElemdet.element_id));
                        boolean out2 = calc().paramSpecific.checkSpecific(hmParam, com5t, parvstsList);//ФИЛЬТР спецификаций
                        if (out2 == true) {

                            Record artikl = eArtikl.find(elendetRec.getInt(eElemdet.artikl_id), false);
                            Specification specif = new Specification(artikl, com5t, hmParam);
                            specif.setColor(com5t, elendetRec);
                            specif.element = "СОСТ";
                            ((ElemSimple) com5t).addSpecifSubelem(specif); //добавим спецификацию в элемент
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка wincalc.constr.Сomposition.nested()");
        }
    }
}
