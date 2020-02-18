package wincalc.constr;

import dataset.Record;
import domain.eArtdet;
import static domain.eArtdet.artikl_id;
import static domain.eArtdet.query;
import domain.eArtikl;
import domain.eColor;
import domain.eSysprof;
import enums.TypeElem;
import enums.TypeProfile;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import static java.util.stream.Collectors.toList;
import wincalc.Wincalc;
import wincalc.model.ElemFrame;

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
                    List<Record> artdetList = eArtdet.query.select().stream().filter(rec -> rec.getInt(eArtdet.artikl_id) == id).collect(toList());
                    //Цыкл по цветам артикулов
                    for (Record artdetRec : artdetList) {

                        int color_id = artdetRec.getInt(eArtdet.color_id);
                        Record colorRec = eColor.query.select().stream().filter(rec -> rec.getInt(eColor.id) == color_id).findFirst().orElse(null);
                        int color_code = colorRec.getInt(eColor.code);
                        if (color_code == iwin.color1) {
                            is = true;
                            LinkedList<ElemFrame> listFrame = iwin.rootArea.listElem(TypeElem.FRAME_BOX, TypeElem.FRAME_STV);
                            for (ElemFrame recordFrame : listFrame) {

//                                recordFrame.setSpecifElement(sysprofRec);
//                                ArrayList<Vstalst> vstalstList2 = Vstalst.find2(constr, recordFrame.getArticlesRec().aseri); //состав для серии профилей
//                                compositionSecond(vstalstList2, recordFrame);
//                                ArrayList<Vstalst> vstalstList = Vstalst.find(constr, recordFrame.getArticlesRec().anumb); //состав для артикула профиля
//                                compositionSecond(vstalstList, recordFrame);
//
//                                recordFrame.getSpecificationRec().width = recordFrame.getSpecificationRec().width + Float.valueOf(recordFrame.getHmParam(0, 31052));
                            }
                        }
                    }
                }
                if (is == true) {
                    break;
                }
            }
            /*        for (Sysproa sysproaRec : sysproaList) {
            boolean is = false;
            if (TypeProfile.IMPOST.value == sysproaRec.atype) {
                ArrayList<Artsvst> svstList = Artsvst.find(constr, sysproaRec.anumb); //подбор текстуры, ищем не на аналоге
                for (Artsvst svst : svstList) {
                    if (svst.clcod == iwin.getColorProfile(1)) {
                        is = true;
                        LinkedList<ElemImpost> impostList = root.getElemList(TypeElem.IMPOST);
                        for (ElemImpost elemInpost : impostList) {
                            elemInpost.setSpecifElement(sysproaRec);
                            ArrayList<Vstalst> vstalstList2 = Vstalst.find2(constr, elemInpost.getArticlesRec().aseri); //состав для серии профилей
                            compositionSecond(vstalstList2, elemInpost);
                            ArrayList<Vstalst> vstalstList = Vstalst.find(constr, elemInpost.getArticlesRec().anumb); //состав для артикула профиля
                            compositionSecond(vstalstList, elemInpost);
                        }
                    }
                }
            }
            if (is == true) {
                break;
            }
        }
        for (Sysproa sysproaRec : sysproaList) {
            boolean is = false;
            if (TypeProfile.STVORKA.value == sysproaRec.atype) {
                ArrayList<Artsvst> svstList = Artsvst.find(constr, sysproaRec.anumb); //подбор текстуры, ищем не на аналоге
                for (Artsvst svst : svstList) {
                    if (svst.clcod == iwin.getColorProfile(1)) {
                        is = true;
                        LinkedList<ElemFrame> elemStvorkaList = root.getElemList(TypeElem.STVORKA);
                        for (ElemFrame elemStvorka : elemStvorkaList) {
                            elemStvorka.setSpecifElement(sysproaRec);
                            ArrayList<Vstalst> vstalstList2 = Vstalst.find2(constr, elemStvorka.getArticlesRec().aseri); //состав для серии профилей
                            compositionSecond(vstalstList2, elemStvorka);
                            ArrayList<Vstalst> vstalstList = Vstalst.find(constr, elemStvorka.getArticlesRec().anumb); //состав для артикула профиля
                            compositionSecond(vstalstList, elemStvorka);
                        }
                    }
                }
            }
            if (is == true) {
                break;
            }*/
            //}
        } catch (Exception e) {
            System.out.println("Ошибка wincalc.constr.Сomposition.compositionFirst()");
        }
    }
//
//    /**
//     * Соcтавы
//     */
//    protected boolean compositionSecond(ArrayList<Vstalst> vstalstList, ElemBase elemBase) {
//
//        //цикл по составам
//        for (Vstalst vstalstRec : vstalstList) {
//
//            ArrayList<ITParam> parvstmList = Parvstm.find(constr, vstalstRec.vnumb);
//            boolean out = paramVariant.checkParvstm(elemBase, parvstmList); //ФИЛЬТР вариантов
//            if (out == true) {
//                //artiklTech = elemBase.getArticlesRec(); //Artikls.get(constr, vstalstRec.anumb, false); //запишем технологический код контейнера
//                ArrayList<Vstaspc> vstaspcList = Vstaspc.find(constr, vstalstRec.vnumb);
//                //Цикл по спецификации
//                for (Vstaspc vstaspcRec : vstaspcList) {
//
//                    HashMap<Integer, String> hmParam = new HashMap(); //тут накапливаются параметры
//                    ArrayList<ITParam> parvstsList = Parvsts.find(constr, vstaspcRec.aunic);
//                    boolean out2 = paramSpecific.checkSpecific(hmParam, elemBase, parvstsList);//ФИЛЬТР спецификаций
//                    if (out2 == true) {
//
//                        Artikls artikl = Artikls.get(constr, vstaspcRec.anumb, false);
//                        Specification specif = new Specification(artikl, elemBase, hmParam);
//                        specif.setColor(this, elemBase, vstaspcRec);
//                        specif.element = "СОСТ";
//                        elemBase.addSpecifSubelem(specif); //добавим спецификацию в элемент
//                    }
//                }
//            }
//        }
//        return false;
//    }
}
