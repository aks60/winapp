package builder.specif;

import dataset.Record;
import domain.eArtdet;
import domain.eArtikl;
import domain.eColor;
import domain.eCurrenc;
import domain.eGroups;
import domain.eRulecalc;
import domain.eSystree;
import enums.LayoutArea;
import enums.TypeElem;
import enums.TypeForm;
import enums.UseUnit;
import java.util.LinkedList;
import builder.Wincalc;
import builder.model.ElemSimple;
import dataset.Query;

/**
 * Расчёт стоимости элементов окна
 */
public class Tariffication extends Cal5e {

    public Tariffication(Wincalc iwin) {
        super(iwin);
    }

    public void calc() {
        try {
            super.calc();
            float percentMarkup = percentMarkup(); //процентная надбавка на изделия сложной формы

            //Расчёт  собес-сть за ед. изм. по таблице мат. ценностей
            for (ElemSimple elem5e : iwin.listElem) {
                elem5e.specificationRec.price1 += calcPrice(elem5e.specificationRec); //себес-сть за ед. изм.
                elem5e.specificationRec.quant1 = formatAmount(elem5e.specificationRec); //количество без отхода
                elem5e.specificationRec.quant2 = elem5e.specificationRec.quant1
                        + (elem5e.specificationRec.quant1 * elem5e.specificationRec.artiklRec.getFloat(eArtikl.otx_norm) / 100); //количество с отходом

                for (Specification specificationRec2 : elem5e.specificationRec.specificationList) {
                    specificationRec2.price1 += calcPrice(specificationRec2); //себес-сть за ед. изм.
                    specificationRec2.quant1 = formatAmount(specificationRec2); //количество без отхода
                    specificationRec2.quant2 = specificationRec2.quant1 + (specificationRec2.quant1 * specificationRec2.artiklRec.getFloat(eArtikl.otx_norm) / 100); //количество с отходом
                }
            }

            //Цикл по элементам конструкции
            for (ElemSimple elem5e : iwin.listElem) {

                Record systreeRec = eSystree.find(iwin.nuni);
                //Цикл по правилам расчёта. Увеличение себестоимости в coeff раз и на incr величину наценки.
                for (Record rulecalcRec : eRulecalc.get()) {

                    //Фильтр по полю форма профиля, заполнения. В БиМакс используюеся только 1, 4, 10, 12 параметры
                    int form = (rulecalcRec.getInt(eRulecalc.form) == 0) ? 1 : rulecalcRec.getInt(eRulecalc.form);
                    if (TypeElem.GLASS == elem5e.type()) {//фильтр для стеклопакета

                        if (form == TypeForm.P00.id) {//не проверять форму
                            rulePrise(rulecalcRec, elem5e.specificationRec);

                        } else if (form == TypeForm.P10.id && TypeElem.TRAPEZE == elem5e.owner().type()) { //не прямоугольное, не арочное заполнение
                            rulePrise(rulecalcRec, elem5e.specificationRec);

                        } else if (form == TypeForm.P12.id && TypeElem.ARCH == elem5e.owner().type()) {//не прямоугольное заполнение с арками
                            rulePrise(rulecalcRec, elem5e.specificationRec);
                        }
                    } else if (form == TypeForm.P04.id && TypeElem.FRAME_SIDE == elem5e.owner().type() && LayoutArea.ARCH == elem5e.layout()) { //фильтр для арки профиля AYPC.W62.0101
                        rulePrise(rulecalcRec, elem5e.specificationRec); //профиль с радиусом

                    } else {
                        if (form == TypeForm.P00.id) {  //не проверять форму
                            rulePrise(rulecalcRec, elem5e.specificationRec); //всё остальное не проверять форму
                        }
                    }
                }

                elem5e.specificationRec.price2 = elem5e.specificationRec.price1 * elem5e.specificationRec.quant2; //себестоимость с отходом
                Record artgrp1Rec = eGroups.find(elem5e.specificationRec.artiklRec.getInt(eArtikl.artgrp1_id));
                elem5e.specificationRec.cost1 = elem5e.specificationRec.price2 * artgrp1Rec.getFloat(eGroups.val, 1) * systreeRec.getFloat(eSystree.coef, 1);
                elem5e.specificationRec.cost1 = elem5e.specificationRec.cost1 + (elem5e.specificationRec.cost1 / 100) * percentMarkup; //стоимость без скидки                     
                elem5e.specificationRec.cost2 = elem5e.specificationRec.cost1; //стоимость со скидкой 

                for (Specification specificationRec2 : elem5e.specificationRec.specificationList) {

                    //Цикл по правилам расчёта.
                    for (Record rulecalcRec : eRulecalc.get()) {
                        int form = (rulecalcRec.getInt(eRulecalc.form) == 0) ? 1 : rulecalcRec.getInt(eRulecalc.form);
                        if (form == TypeForm.P00.id) { //не проверять форму 
                            rulePrise(rulecalcRec, specificationRec2);
                        }
                    }
                    specificationRec2.price2 = specificationRec2.price1 * specificationRec2.quant2; //себестоимости с отходом
                    Record artgrp1Rec2 = eGroups.find(specificationRec2.artiklRec.getInt(eArtikl.artgrp1_id));
                    specificationRec2.cost1 = specificationRec2.price2 * artgrp1Rec2.getFloat(eGroups.val, 1) * systreeRec.getFloat(eSystree.coef);
                    specificationRec2.cost1 = specificationRec2.cost1 + (specificationRec2.cost1 / 100) * percentMarkup; //стоимость без скидки                        
                    specificationRec2.cost2 = specificationRec2.cost1; //стоимость со скидкой 
                }
            }

            //Расчёт веса элемента конструкции
            for (ElemSimple elem5e : iwin.listElem) {
                for (Specification spec : elem5e.specificationRec.specificationList) {
                    spec.weight = spec.quant1 * spec.artiklRec.getFloat(eArtikl.density);
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка: specif.Tariffication.calc() " + e);
        } finally {
            Query.conf = conf;
        }
    }

    //Себес-сть за ед. изм. Считает тариф для заданного артикула заданных цветов по таблице eArtdet
    public float calcPrice(Specification specificRec) {

        float inPrice = 0;
        Record color1Rec = eColor.find(specificRec.colorID1);  //основная
        Record color2Rec = eColor.find(specificRec.colorID2);  //внутренняя
        Record color3Rec = eColor.find(specificRec.colorID3);  //внешняя

        Record kursBaseRec = eCurrenc.find(specificRec.artiklRec.getInt(eArtikl.currenc1_id));    // кросс-курс валюты для основной текстуры
        Record kursNoBaseRec = eCurrenc.find(specificRec.artiklRec.getInt(eArtikl.currenc2_id));  // кросс-курс валюты для неосновных текстур (внутренняя, внешняя, двухсторонняя)

        //Цикл по тарификационной таблице ARTDET мат. ценностей
        for (Record artdetRec : eArtdet.find(specificRec.artiklRec.getInt(eArtikl.id))) {

            float artdetTariff = 0;
            boolean artdetUsed = false;

            if (artdetRec.getFloat(eArtdet.cost_c4) != 0 && color2Rec.getInt(eColor.id) == color3Rec.getInt(eColor.id)
                    && isTariff(artdetRec, color2Rec)) { //если двухсторонняя текстура

                artdetTariff += (artdetRec.getInt(eArtdet.cost_c4) * Math.max(color2Rec.getFloat(eColor.coef2), color2Rec.getFloat(eColor.coef3)) / kursNoBaseRec.getFloat(eCurrenc.cross_cour));
                artdetUsed = true;

            } else {

                if (isTariff(artdetRec, color1Rec)) { //подбираем тариф основной текстуры
                    Record colgrpRec = eGroups.find(color1Rec.getInt(eColor.colgrp_id));                   
                    artdetTariff += (artdetRec.getFloat(eArtdet.cost_c1) * color1Rec.getFloat(eColor.coef1) * colgrpRec.getFloat(eGroups.val)) / kursBaseRec.getFloat(eCurrenc.cross_cour);
                    artdetUsed = true;
                }
                if (isTariff(artdetRec, color2Rec)) {  //подбираем тариф внутренней текстуры
                    Record colgrpRec = eGroups.find(color2Rec.getInt(eColor.colgrp_id));
                    Object ooo = (artdetRec.getFloat(eArtdet.cost_c2) * color2Rec.getFloat(eColor.coef2) * colgrpRec.getFloat(eGroups.val)) / kursNoBaseRec.getFloat(eCurrenc.cross_cour);
                    Object o1 = artdetRec.getFloat(eArtdet.cost_c2);
                    Object o2 = color2Rec.getFloat(eColor.coef2);
                    Object o3 = colgrpRec.getFloat(eGroups.val);
                    Object o4 =  kursNoBaseRec.getFloat(eCurrenc.cross_cour);
                    
                    artdetTariff += (artdetRec.getFloat(eArtdet.cost_c2) * color2Rec.getFloat(eColor.coef2) * colgrpRec.getFloat(eGroups.val)) / kursNoBaseRec.getFloat(eCurrenc.cross_cour);
                    artdetUsed = true;
                }
                if (isTariff(artdetRec, color3Rec)) { //подбираем тариф внешней текстуры
                    Record colgrpRec = eGroups.find(color3Rec.getInt(eColor.colgrp_id));
                    artdetTariff += (artdetRec.getFloat(eArtdet.cost_c3) * color3Rec.getFloat(eColor.coef3) * colgrpRec.getFloat(eGroups.val)) / kursNoBaseRec.getFloat(eCurrenc.cross_cour);
                    artdetUsed = true;
                }
            }
            if (artdetUsed && artdetRec.getFloat(eArtdet.cost_min) != 0 && specificRec.quant1 != 0 && artdetTariff * specificRec.quant1 < artdetRec.getFloat(eArtdet.cost_min)) {
                artdetTariff = artdetRec.getFloat(eArtdet.cost_min) / specificRec.quant1;    //используем минимальный тариф 
            }
            if (artdetUsed) {
                inPrice = inPrice + (artdetTariff * artdetRec.getFloat(eArtdet.price_coeff));
            }
        }
        return inPrice;
    }

    //Себес-сть за ед. изм. Считает тариф для заданного артикула заданных цветов по таблице eArtdet
    public float calcPrice2(Specification specificRec) {

        float inPrice = 0;
        Record color1Rec = eColor.find(specificRec.colorID1);  //основная
        Record color2Rec = eColor.find(specificRec.colorID2);  //внутренняя
        Record color3Rec = eColor.find(specificRec.colorID3);  //внешняя

        Record kursBaseRec = eCurrenc.find(specificRec.artiklRec.getInt(eArtikl.currenc1_id));    // кросс-курс валюты для основной текстуры
        Record kursNoBaseRec = eCurrenc.find(specificRec.artiklRec.getInt(eArtikl.currenc2_id));  // кросс-курс валюты для неосновных текстур (внутренняя, внешняя, двухсторонняя)

        //Цикл по тарификационной таблице ARTDET мат. ценностей
        for (Record artdetRec : eArtdet.find(specificRec.artiklRec.getInt(eArtikl.id))) {

            float artdetTariff = 0;
            boolean artdetUsed = false;

            if (artdetRec.getFloat(eArtdet.cost_c4) != 0 && color2Rec.getInt(eColor.id) == color3Rec.getInt(eColor.id)
                    && artdetRec.getInt(eArtdet.mark_c2) == 1 && artdetRec.getInt(eArtdet.mark_c3) == 1
                    && isTariff(artdetRec, color2Rec)) { //если двухсторонняя текстура

                artdetTariff += (artdetRec.getInt(eArtdet.cost_c4) * Math.max(color2Rec.getFloat(eColor.coef2), color2Rec.getFloat(eColor.coef3)) / kursNoBaseRec.getFloat(eCurrenc.cross_cour));
                artdetUsed = true;

            } else {

                if ("1".equals(artdetRec.getStr(eArtdet.mark_c1))) {  //подбираем тариф основной текстуры
                    if (isTariff(artdetRec, color1Rec)) {
                        Record colgrpRec = eGroups.find(color1Rec.getInt(eColor.colgrp_id));
                        artdetTariff += (artdetRec.getFloat(eArtdet.cost_c1) * color1Rec.getFloat(eColor.coef1) * colgrpRec.getFloat(eGroups.val)) / kursBaseRec.getFloat(eCurrenc.cross_cour);
                        artdetUsed = true;
                    }
                }
                if ("1".equals(artdetRec.getStr(eArtdet.mark_c2)) || "1".equals(artdetRec.getStr(eArtdet.mark_c1))) { //подбираем тариф внутренней текстуры
                    if (isTariff(artdetRec, color2Rec)) {
                        Record colgrpRec = eGroups.find(color2Rec.getInt(eColor.colgrp_id));
                        artdetTariff += (artdetRec.getFloat(eArtdet.cost_c2) * color2Rec.getFloat(eColor.coef2) * colgrpRec.getFloat(eGroups.val)) / kursNoBaseRec.getFloat(eCurrenc.cross_cour);
                        artdetUsed = true;
                    }
                }
                if ("1".equals(artdetRec.getStr(eArtdet.mark_c3)) || "1".equals(artdetRec.getStr(eArtdet.mark_c1))) { //подбираем тариф внешней текстуры
                    if (isTariff(artdetRec, color3Rec)) {
                        Record colgrpRec = eGroups.find(color3Rec.getInt(eColor.colgrp_id));
                        artdetTariff += (artdetRec.getFloat(eArtdet.cost_c3) * color3Rec.getFloat(eColor.coef3) * colgrpRec.getFloat(eGroups.val)) / kursNoBaseRec.getFloat(eCurrenc.cross_cour);
                        artdetUsed = true;
                    }
                }
            }
            if (artdetUsed && artdetRec.getFloat(eArtdet.cost_min) != 0 && specificRec.quant1 != 0 && artdetTariff * specificRec.quant1 < artdetRec.getFloat(eArtdet.cost_min)) {
                artdetTariff = artdetRec.getFloat(eArtdet.cost_min) / specificRec.quant1;    //используем минимальный тариф 
            }
            if (artdetUsed) {
                inPrice = inPrice + (artdetTariff * artdetRec.getFloat(eArtdet.price_coeff));
            }
        }
        return inPrice;
    }

    //Правила расчёта. Фильтр по полю form, color(1,2,3) таблицы RULECALC
    private void rulePrise(Record rulecalcRec, Specification specifRec) {

        //Если артикл ИЛИ тип ИЛИ подтип совпали
        if (specifRec.artikl.equals(rulecalcRec.getStr(eArtikl.code))
                || ((specifRec.artiklRec.getInt(eArtikl.level1) * 100
                + specifRec.artiklRec.getInt(eArtikl.level2)) == rulecalcRec.getInt(eRulecalc.type))) {

            if (Util.containsInt(rulecalcRec.getStr(eRulecalc.color1), specifRec.colorID1) == true
                    && Util.containsInt(rulecalcRec.getStr(eRulecalc.color2), specifRec.colorID2) == true
                    && Util.containsInt(rulecalcRec.getStr(eRulecalc.color3), specifRec.colorID3) == true) {

                if (rulecalcRec.getInt(eRulecalc.common) == 0) {
                    if (Util.containsFloat(rulecalcRec.getStr(eRulecalc.quant), specifRec.quant2) == true) {
                        specifRec.price1 = specifRec.price1 * rulecalcRec.getFloat(eRulecalc.coeff) + rulecalcRec.getFloat(eRulecalc.incr);  //увеличение себестоимости в coegg раз и на incr величину надбавки
                    }

                } else if (rulecalcRec.getInt(eRulecalc.common) == 1) { //по использованию c расчётом общего количества по артикулу, подтипу, типу
                    LinkedList<ElemSimple> elemList = iwin.listElem;
                    float quantity3 = 0;
                    if (rulecalcRec.get(eRulecalc.artikl_id) != null) { //по артикулу
                        for (ElemSimple elem5e : elemList) { //суммирую колич. всех элементов (например штапиков)
                            if (elem5e.specificationRec.artikl.equals(specifRec.artikl)) {
                                quantity3 = quantity3 + elem5e.specificationRec.quant1;
                            }
                            for (Specification specifRec2 : elem5e.specificationRec.specificationList) {
                                if (specifRec2.artikl.equals(specifRec.artikl)) {
                                    quantity3 = quantity3 + specifRec2.quant1;
                                }
                            }
                        }
                    } else { //по подтипу, типу
                        for (ElemSimple elem5e : elemList) { //суммирую колич. всех элементов (например штапиков)
                            Specification specifRec2 = elem5e.specificationRec;
                            if (specifRec2.artiklRec.getInt(eArtikl.level1) * 100 + specifRec2.artiklRec.getInt(eArtikl.level2) == rulecalcRec.getInt(eRulecalc.type)) {
                                quantity3 = quantity3 + elem5e.specificationRec.quant1;
                            }
                            for (Specification specifRec3 : specifRec2.specificationList) {
                                if (specifRec3.artiklRec.getInt(eArtikl.level1) * 100 + specifRec3.artiklRec.getInt(eArtikl.level2) == rulecalcRec.getInt(eRulecalc.type)) {
                                    quantity3 = quantity3 + specifRec3.quant1;
                                }
                            }
                        }
                    }
                    if (Util.containsFloat(rulecalcRec.getStr(eRulecalc.quant), quantity3) == true) {
                        specifRec.price1 = specifRec.price1 * rulecalcRec.getFloat(eRulecalc.coeff) + rulecalcRec.getFloat(eRulecalc.incr); //увеличение себестоимости в coeff раз и на incr величину надбавки                      
                    }
                }
            }
        }
    }

    //В зав. от единицы изм. форматируется количество
    private float formatAmount(Specification specificRec) {
        //TODO Нужна доработка для расчёта по минимальному тарифу. См. dll VirtualPro4::CalcArtTariff
        if (UseUnit.METR.id == specificRec.artiklRec.getInt(eArtikl.unit)) { //метры
            return specificRec.width / 1000;

        } else if (UseUnit.METR2.id == specificRec.artiklRec.getInt(eArtikl.unit)) { //кв. метры
            return specificRec.width * specificRec.height / 1000000;

        } else if (UseUnit.PIE.id == specificRec.artiklRec.getInt(eArtikl.unit)) { //шт.
            return specificRec.count;

        } else if (UseUnit.ML.id == specificRec.artiklRec.getInt(eArtikl.unit)) { //мл
            return specificRec.quant1;
        }
        return 0;
    }

    //Процентная надбавка на изделия сложной формы
    private float percentMarkup() {
        if (TypeElem.ARCH == iwin.rootArea.type()) {
            return eGroups.find(2101).getFloat(eGroups.val);
        }
        return 0;
    }

    //Проверяет, должен ли применяться заданный тариф мат-ценности для заданной текстуры
    public static boolean isTariff(Record artdetRec, Record colorRec) {

        if (artdetRec.getInt(eArtdet.color_fk) < 0) { //этот тариф задан для группы текстур
            if ((-1 * colorRec.getInt(eColor.colgrp_id)) == artdetRec.getInt(eArtdet.color_fk)) {
                return true; //текстура принадлежит группе
            }
        } else if (colorRec.getInt(eColor.id) == artdetRec.getInt(eArtdet.color_fk)) {
            return true; //текстуры совпали
        }
        return false;
    }
}
