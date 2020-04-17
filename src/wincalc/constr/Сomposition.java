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
import enums.UserArtikl;
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
public class Сomposition extends Cal5e {
      
    public Сomposition(Wincalc iwin, CalcConstructiv calc) {
        super(iwin, calc);        
    }

    //Идем по списку профилей, смотрю есть аналог работаю с ним.
    //Но при проверке параметров использую вирт. мат. ценность.
    public void build() {
        try {
            ArrayList<Record> sysprofList = eSysprof.find(iwin().nuni);

            //Цыкл по профилям системы
            for (Record sysprofRec : sysprofList) {
                boolean is = false;
                int types = sysprofRec.getInt(eSysprof.use_type);
                if (UserArtikl.FRAME.id == types) {

                    int id = sysprofRec.getInt(eSysprof.artikl_id); //подбор текстуры, ищем не на аналоге                
                    List<Record> artdetList = eArtdet.find(id);
                    //Цыкл по цветам артикулов
                    for (Record artdetRec : artdetList) {

                        int color_fk = artdetRec.getInt(eArtdet.color_fk);
                        //TODO Нужна проверка для color_fk < 0
                        if (color_fk == iwin().color1) {
                            is = true;
                            LinkedList<ElemFrame> listFrame = iwin().rootArea.listElem(TypeElem.FRAME_BOX, TypeElem.FRAME_STV);
                            for (ElemFrame recordFrame : listFrame) {

                                recordFrame.setSpecifElement(sysprofRec);
                                String series = recordFrame.artiklRec.getStr(eArtikl.series);
                                List<Record> elementList2 = eElement.find(series); //состав для серии профилей
                                nested(elementList2, recordFrame);
                                int artikl_id = recordFrame.artiklRec.getInt(eArtikl.id);
                                List<Record> elementList = eElement.find2(artikl_id); //состав для артикула профиля
                                nested(elementList, recordFrame);

                                recordFrame.specificationRec.width = recordFrame.specificationRec.width
                                        + Float.valueOf(String.valueOf(recordFrame.specificationRec.getParam(0, 31052)));
                            }
                        }
                    }
                }
                if (is == true) {
                    break;
                }
            }
            for (Record record : sysprofList) {
                boolean is = false;
                if (UserArtikl.IMPOST.id == record.getInt(eSysprof.use_type)) {
                    List<Record> artdetList = eArtdet.find(record.getInt(eArtdet.id)); //подбор текстуры, ищем не на аналоге
                    for (Record artdetRec : artdetList) {

                        int color_fk = artdetRec.getInt(eArtdet.color_fk);
                        //TODO Нужна проверка для color_fk < 0
                        if (color_fk == iwin().color1) {
                            is = true;
                            LinkedList<ElemImpost> impostList = iwin().rootArea.listElem(TypeElem.IMPOST);
                            for (ElemImpost elemInpost : impostList) {

                                String series = elemInpost.artiklRec.getStr(eArtikl.series);
                                elemInpost.setSpecifElement(record);
                                List<Record> elementList2 = eElement.find(series); //состав для серии профилей
                                nested(elementList2, elemInpost);
                                List<Record> elementList = eElement.find(series); //.anumb); //состав для артикула профиля
                                nested(elementList, elemInpost);
                            }
                        }
                    }
                }
                if (is == true) {
                    break;
                }
            }
            for (Record record : sysprofList) {
                boolean is = false;
                if (UserArtikl.STVORKA.id == record.getInt(eSysprof.use_type)) {
                    List<Record> artdetList = eArtdet.find(record.getInt(eArtdet.id)); //подбор текстуры, ищем не на аналоге
                    for (Record artdetRec : artdetList) {

                        int color_fk = artdetRec.getInt(eArtdet.color_fk);
                        //TODO Нужна проверка для color_fk < 0
                        if (color_fk == iwin().color1) {
                            is = true;
                            LinkedList<ElemImpost> impostList = iwin().rootArea.listElem(TypeElem.IMPOST);
                            for (ElemImpost elemInpost : impostList) {

                                elemInpost.setSpecifElement(record);
                                List<Record> elementList2 = eElement.find(elemInpost.artiklRec.getStr(eArtikl.series)); //состав для серии профилей
                                nested(elementList2, elemInpost);
                                List<Record> elementList = eElement.find(elemInpost.artiklRec.getStr(eArtikl.code)); //.anumb); //состав для артикула профиля
                                nested(elementList, elemInpost);
                            }
                        }
                    }
                }
                if (is == true) {
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка wincalc.constr.Сomposition.compositionFirst()");
        }
    }

    protected boolean nested(List<Record> elementList, Com5t com5t) {

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
        return false;
    }
}
