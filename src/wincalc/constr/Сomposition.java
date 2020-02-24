package wincalc.constr;

import dataset.Record;
import domain.eArtdet;
import domain.eArtikl;
import domain.eColor;
import domain.eElement;
import domain.eElempar1;
import domain.eSysprof;
import enums.TypeElem;
import enums.TypeProfile;
import forms.Artikls;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import static main.App1.eApp1.Artikls;
import wincalc.Wincalc;
import wincalc.model.Com5t;
import wincalc.model.ElemFrame;
import wincalc.model.ElemImpost;

public class Сomposition extends Cal5e {

    public Сomposition(Wincalc iwin) {
        super(iwin);
    }

    //Составы.
    //Идем по списку профилей, смотрю есть аналог работаю с ним.
    //Но при проверке параметров использую вирт. мат. ценность.
    public void compositionFirst() {
        try {
            ArrayList<Record> sysprofList = eSysprof.find(iwin.nuni);

            //Цыкл по профилям системы
            for (Record sysprofRec : sysprofList) {
                boolean is = false;
                int types = sysprofRec.getInt(eSysprof.types);
                if (TypeProfile.FRAME.value == types) {

                    int id = sysprofRec.getInt(eSysprof.artikl_id); //подбор текстуры, ищем не на аналоге                
                    List<Record> artdetList = eArtdet.find(id);
                    //Цыкл по цветам артикулов
                    for (Record artdetRec : artdetList) {

                        int color_id = artdetRec.getInt(eArtdet.color_id);
                        Record colorRec = eColor.find(color_id);
                        int color_code = colorRec.getInt(eColor.code);
                        if (color_code == iwin.color1) {
                            is = true;
                            LinkedList<ElemFrame> listFrame = iwin.rootArea.listElem(TypeElem.FRAME_BOX, TypeElem.FRAME_STV);
                            for (ElemFrame recordFrame : listFrame) {

                                recordFrame.setSpecifElement(sysprofRec);
                                String series = recordFrame.artiklRec.getStr(eArtikl.series);
                                List<Record> elementList2 = eElement.find(series); //состав для серии профилей
                                compositionSecond(elementList2, recordFrame);
                                String code = recordFrame.artiklRec.getStr(eArtikl.code);
                                List<Record> elementList = eElement.find2(code); //состав для артикула профиля
                                compositionSecond(elementList, recordFrame);

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
                if (TypeProfile.IMPOST.value == record.getInt(eSysprof.types)) {
                    List<Record> artdetList = eArtdet.find(record.getInt(eArtdet.id)); //подбор текстуры, ищем не на аналоге
                    for (Record artdetRec : artdetList) {

                        Record artiklCol = eColor.find(artdetRec.getInt(eColor.id));
                        if (artiklCol.getInt(eColor.code) == iwin.color1) {
                            is = true;
                            LinkedList<ElemImpost> impostList = iwin.rootArea.listElem(TypeElem.IMPOST);
                            for (ElemImpost elemInpost : impostList) {

                                String series = elemInpost.artiklRec.getStr(eArtikl.series);
                                elemInpost.setSpecifElement(record);
                                List<Record> elementList2 = eElement.find(series); //состав для серии профилей
                                compositionSecond(elementList2, elemInpost);
                                List<Record> elementList = eElement.find(series); //.anumb); //состав для артикула профиля
                                compositionSecond(elementList, elemInpost);
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
                if (TypeProfile.STVORKA.value == record.getInt(eSysprof.types)) {
                    List<Record> artdetList = eArtdet.find(record.getInt(eArtdet.id)); //подбор текстуры, ищем не на аналоге
                    for (Record artdetRec : artdetList) {

                        Record artiklCol = eColor.find(artdetRec.getInt(eColor.id));
                        if (artiklCol.getInt(eColor.code) == iwin.color1) {
                            is = true;
                            LinkedList<ElemImpost> impostList = iwin.rootArea.listElem(TypeElem.IMPOST);
                            for (ElemImpost elemInpost : impostList) {

                                elemInpost.setSpecifElement(record);
                                List<Record> elementList2 = eElement.find(elemInpost.artiklRec.getStr(eArtikl.series)); //состав для серии профилей
                                compositionSecond(elementList2, elemInpost);
                                List<Record> elementList = eElement.find(elemInpost.artiklRec.getStr(eArtikl.code)); //.anumb); //состав для артикула профиля
                                compositionSecond(elementList, elemInpost);
                            }
                        }
                    }
                }
                if (is == true) {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка wincalc.constr.Сomposition.compositionFirst()");
        }
    }

    //Соcтавы
    protected boolean compositionSecond(List<Record> elementList, Com5t elemBase) {

        //цикл по составам
     /*   for (Record elementRec : elementList) {

            ArrayList<Record> elempar1List = eElempar1.find(elementRec.getInt(eElement.id));
            boolean out = paramVariant.checkParvstm(elemBase, elempar1List); //ФИЛЬТР вариантов
            if (out == true) {
                //artiklTech = elemBase.getArticlesRec(); //Artikls.get(constr, vstalstRec.anumb, false); //запишем технологический код контейнера
                ArrayList<Vstaspc> vstaspcList = Vstaspc.find(constr, elementRec.vnumb);
                //Цикл по спецификации
                for (Vstaspc vstaspcRec : vstaspcList) {

                    HashMap<Integer, String> hmParam = new HashMap(); //тут накапливаются параметры
                    ArrayList<ITParam> parvstsList = Parvsts.find(constr, vstaspcRec.aunic);
                    boolean out2 = paramSpecific.checkSpecific(hmParam, elemBase, parvstsList);//ФИЛЬТР спецификаций
                    if (out2 == true) {

                        Artikls artikl = Artikls.get(constr, vstaspcRec.anumb, false);
                        Specification specif = new Specification(artikl, elemBase, hmParam);
                        specif.setColor(this, elemBase, vstaspcRec);
                        specif.element = "СОСТ";
                        elemBase.addSpecifSubelem(specif); //добавим спецификацию в элемент
                    }
                }
            }
        }*/
        return false;
    }
}
