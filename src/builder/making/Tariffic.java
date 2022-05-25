package builder.making;

import dataset.Record;
import domain.eArtdet;
import domain.eArtikl;
import domain.eColor;
import domain.eCurrenc;
import domain.eGroups;
import domain.eRulecalc;
import domain.eSystree;
import enums.Layout;
import enums.TypeForm;
import enums.UseUnit;
import java.util.LinkedList;
import builder.Wincalc;
import builder.model.ElemSimple;
import common.UCom;
import dataset.Query;
import enums.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Расчёт стоимости элементов окна
 */
public class Tariffic extends Cal5e {

//    //Выведено для читабельности алгоритма
//    private float koef_col1 = 1; //ценовой коэф.основной текстуры
//    private float koef_col2 = 1; //ценовой коэф.внутренний текстуры
//    private float koef_col3 = 1; //ценовой коэф.внешний текстуры
    private boolean norm_otx = true;
    private int precision = Math.round(new Query(eGroups.values()).select(eGroups.up).get(0).getFloat(eGroups.val)); //округление длины профилей

    public Tariffic(Wincalc winc, boolean norm_otx) {
        super(winc);
        this.norm_otx = norm_otx;
    }

    public void calc() {
        try {
            super.calc();
            float percentMarkup = percentMarkup(); //процентная надбавка на изделия сложной формы

            //Расчёт  себес-сти за ед. изм. по таблице мат. ценностей
            for (ElemSimple elem5e : winc.listElem) {
                elem5e.spcRec.price1 += calcPrice(elem5e.spcRec); //себест. за ед. без отхода
                elem5e.spcRec.quant1 = formatAmount(elem5e.spcRec); //количество без отхода
                elem5e.spcRec.quant2 = elem5e.spcRec.quant1;
                if (norm_otx == true) {
                    elem5e.spcRec.quant2 = elem5e.spcRec.quant2 + (elem5e.spcRec.quant1 * elem5e.spcRec.artiklRec.getFloat(eArtikl.otx_norm) / 100); //количество с отходом
                }
                for (Specific specificationRec2 : elem5e.spcRec.spcList) {
                    specificationRec2.price1 += calcPrice(specificationRec2); //себест. за ед. без отхода
                    specificationRec2.quant1 = formatAmount(specificationRec2); //количество без отхода
                    specificationRec2.quant2 = specificationRec2.quant1;
                    if (norm_otx == true) {
                        specificationRec2.quant2 = specificationRec2.quant2 + (specificationRec2.quant1 * specificationRec2.artiklRec.getFloat(eArtikl.otx_norm) / 100); //количество с отходом
                    }
                }
            }

            //Цикл по элементам конструкции
            for (ElemSimple elem5e : winc.listElem) {

                Record systreeRec = eSystree.find(winc.nuni);
                //Цикл по правилам расчёта. Увеличение себестоимости в coeff раз и на incr величину наценки.
                for (Record rulecalcRec : eRulecalc.list()) {

                    //Фильтр по полю форма профиля, заполнения. В БиМакс используюеся только 1, 4, 10, 12 параметры
                    int form = (rulecalcRec.getInt(eRulecalc.form) == 0) ? 1 : rulecalcRec.getInt(eRulecalc.form);
                    if (Type.GLASS == elem5e.type()) {//фильтр для стеклопакета

                        if (form == TypeForm.P00.id) {//не проверять форму
                            rulePrise(rulecalcRec, elem5e.spcRec);

                        } else if (form == TypeForm.P10.id && Type.TRAPEZE == elem5e.owner.type()) { //не прямоугольное, не арочное заполнение
                            rulePrise(rulecalcRec, elem5e.spcRec);

                        } else if (form == TypeForm.P12.id && Type.ARCH == elem5e.owner.type()) {//не прямоугольное заполнение с арками
                            rulePrise(rulecalcRec, elem5e.spcRec);
                        }
                    } else if (form == TypeForm.P04.id && elem5e.type() == Type.FRAME_SIDE
                            && elem5e.owner.type() == Type.ARCH && elem5e.layout() == Layout.TOP) {  //профиль с радиусом  (фильтр для арки профиля AYPC.W62.0101)
                        rulePrise(rulecalcRec, elem5e.spcRec); //профиль с радиусом

                    } else {
                        if (form == TypeForm.P00.id) {  //не проверять форму
                            rulePrise(rulecalcRec, elem5e.spcRec); //всё остальное не проверять форму
                        }
                    }
                }

                elem5e.spcRec.price2 = elem5e.spcRec.price1 * elem5e.spcRec.quant2; //себест. за ед. с отходом 
                Record artgrp1Rec = eGroups.find(elem5e.spcRec.artiklRec.getInt(eArtikl.artgrp1_id));
                elem5e.spcRec.cost1 = elem5e.spcRec.price2 * artgrp1Rec.getFloat(eGroups.val, 1) * systreeRec.getFloat(eSystree.coef, 1);
                elem5e.spcRec.cost1 = elem5e.spcRec.cost1 + (elem5e.spcRec.cost1 / 100) * percentMarkup; //стоимость без скидки                     
                elem5e.spcRec.cost2 = elem5e.spcRec.cost1; //стоимость со скидкой 

                for (Specific specificationRec2 : elem5e.spcRec.spcList) {

                    //Цикл по правилам расчёта.
                    for (Record rulecalcRec : eRulecalc.list()) {
                        int form = (rulecalcRec.getInt(eRulecalc.form) == 0) ? 1 : rulecalcRec.getInt(eRulecalc.form);
                        if (form == TypeForm.P00.id) { //не проверять форму 
                            rulePrise(rulecalcRec, specificationRec2);
                        }
                    }
                    specificationRec2.price2 = specificationRec2.price1 * specificationRec2.quant2; //себест. за ед. с отходом  
                    Record artgrp1Rec2 = eGroups.find(specificationRec2.artiklRec.getInt(eArtikl.artgrp1_id));
                    specificationRec2.cost1 = specificationRec2.price2 * artgrp1Rec2.getFloat(eGroups.val, 1) * systreeRec.getFloat(eSystree.coef);
                    specificationRec2.cost1 = specificationRec2.cost1 + (specificationRec2.cost1 / 100) * percentMarkup; //стоимость без скидки                        
                    specificationRec2.cost2 = specificationRec2.cost1; //стоимость со скидкой 
                }
            }

            //Расчёт веса элемента конструкции
            for (ElemSimple elem5e : winc.listElem) {
                for (Specific spec : elem5e.spcRec.spcList) {
                    spec.weight = spec.quant1 * spec.artiklRec.getFloat(eArtikl.density);
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка:specif.Tariffication.calc() " + e);
        } finally {
            Query.conf = conf;
        }
    }

    //Себес-сть за ед. изм. Считает тариф для заданного артикула заданных цветов по таблице eArtdet
    public float calcPrice(Specific specificRec) {

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

            /**
             * Если тариф двухсторонний текстуры не равен 0, и если
             * двухсторонняя текстура и должен применяться заданный тариф
             */
            if (artdetRec.getFloat(eArtdet.cost_c4) != 0 && color2Rec.getInt(eColor.id) == color3Rec.getInt(eColor.id)
                    && isTariff(artdetRec, color2Rec)) {
                float k1 = artdetRec.getFloat(eArtdet.cost_c4); //тариф двухсторонний текстуры
                float k2 = color2Rec.getFloat(eColor.coef2); //ценовой коэф.внутренний
                float k3 = color2Rec.getFloat(eColor.coef3); //ценовой коэф.внешний
                float k4 = kursNoBaseRec.getFloat(eCurrenc.cross_cour); //кросс курс
                artdetTariff += (k1 * Math.max(k2, k3) / k4);

                if (isTariff(artdetRec, color1Rec)) { //подбираем тариф основной текстуры
                    float m1 = artdetRec.getFloat(eArtdet.cost_unit); //тариф единица измерения
                    float m2 = specificRec.elem5e.artiklRec.getFloat(eArtikl.density); //удельный вес                    
                    if (m1 > 0 && m2 > 0) {
                        artdetTariff += m1 * m2;
                    } else {
                        Record colgrpRec = eGroups.find(color1Rec.getInt(eColor.colgrp_id));
                        float z1 = artdetRec.getFloat(eArtdet.cost_c1); //тариф основной текстуры"
                        float z2 = color1Rec.getFloat(eColor.coef1); //ценовой коэф.основной текст.
                        float z3 = colgrpRec.getFloat(eGroups.val); //коэф. групп текстур
                        float z4 = kursBaseRec.getFloat(eCurrenc.cross_cour); ////кросс курс
                        artdetTariff += (z1 * z2 * z3) / z4;
                    }
                }
                artdetUsed = true;

                //Сложение цен по трём текстурам
            } else {

                if (isTariff(artdetRec, color1Rec)) { //подбираем тариф основной текстуры
                    Record colgrpRec = eGroups.find(color1Rec.getInt(eColor.colgrp_id));
                    float k1 = artdetRec.getFloat(eArtdet.cost_c1); //тариф основной текстуры
                    float k2 = color1Rec.getFloat(eColor.coef1); //ценовой коэф.основной текстуры
                    float k3 = colgrpRec.getFloat(eGroups.val); //коэф. групп текстур
                    float k4 = kursBaseRec.getFloat(eCurrenc.cross_cour); //кросс курс
                    artdetTariff += (k1 * k2 * k3) / k4;
                    artdetUsed = true;
                }
                if (isTariff(artdetRec, color2Rec)) {  //подбираем тариф внутренней текстуры
                    Record colgrpRec = eGroups.find(color2Rec.getInt(eColor.colgrp_id));
                    float d1 = artdetRec.getFloat(eArtdet.cost_c2); //тариф внутренний текстуры
                    float d2 = color2Rec.getFloat(eColor.coef2); //ценовой коэф.внутренний
                    float d3 = colgrpRec.getFloat(eGroups.val); //коэф. групп текстур
                    float d4 = kursNoBaseRec.getFloat(eCurrenc.cross_cour); //кросс курс
                    artdetTariff += (d1 * d2 * d3) / d4;
                    artdetUsed = true;
                }
                if (isTariff(artdetRec, color3Rec)) { //подбираем тариф внешней текстуры
                    Record colgrpRec = eGroups.find(color3Rec.getInt(eColor.colgrp_id));
                    float w1 = artdetRec.getFloat(eArtdet.cost_c3); //Тариф внешний текстуры
                    float w2 = color3Rec.getFloat(eColor.coef3); //Ценовой коэф.внешний
                    float w3 = colgrpRec.getFloat(eGroups.val); //коэф. групп текстур
                    float w4 = kursNoBaseRec.getFloat(eCurrenc.cross_cour); //кросс курс
                    artdetTariff += (w1 * w2 * w3) / w4;
                    artdetUsed = true;
                }
            }
            if (artdetUsed && artdetRec.getFloat(eArtdet.cost_min) != 0 && 
                    specificRec.quant1 != 0 && artdetTariff * 
                    specificRec.quant1 < artdetRec.getFloat(eArtdet.cost_min)) {
                artdetTariff = artdetRec.getFloat(eArtdet.cost_min) / specificRec.quant1; //используем минимальный тариф 
            }
            if (artdetUsed) {
                inPrice = inPrice + (artdetTariff * artdetRec.getFloat(eArtdet.price_coeff));
            }
        }
        return inPrice;
    }

    //Правила расчёта. Фильтр по полю form, color(1,2,3) таблицы RULECALC
    private void rulePrise(Record rulecalcRec, Specific specifRec) {

        Object obj = rulecalcRec.getStr(eArtikl.code);

        //Если артикул ИЛИ тип ИЛИ подтип совпали
        if (specifRec.artiklRec.get(eArtikl.id).equals(rulecalcRec.get(eRulecalc.artikl_id)) == true || rulecalcRec.get(eRulecalc.artikl_id) == null) {
            if ((specifRec.artiklRec.getInt(eArtikl.level1) * 100 + specifRec.artiklRec.getInt(eArtikl.level2)) == rulecalcRec.getInt(eRulecalc.type)) {
                if (UCom.containsNumbJust(rulecalcRec.getStr(eRulecalc.color1), specifRec.colorID1) == true
                        && UCom.containsNumbJust(rulecalcRec.getStr(eRulecalc.color2), specifRec.colorID2) == true
                        && UCom.containsNumbJust(rulecalcRec.getStr(eRulecalc.color3), specifRec.colorID3) == true) {

                    if (rulecalcRec.getInt(eRulecalc.common) == 0) {
                        if (UCom.containsNumbJust(rulecalcRec.getStr(eRulecalc.quant), specifRec.quant2) == true) {
                            specifRec.price1 = specifRec.price1 * rulecalcRec.getFloat(eRulecalc.coeff) + rulecalcRec.getFloat(eRulecalc.incr);  //увеличение себестоимости в coegg раз и на incr величину надбавки
                        }

                    } else if (rulecalcRec.getInt(eRulecalc.common) == 1) { //по использованию c расчётом общего количества по артикулу, подтипу, типу
                        LinkedList<ElemSimple> elemList = winc.listElem;
                        float quantity3 = 0;
                        if (rulecalcRec.get(eRulecalc.artikl_id) != null) { //по артикулу
                            for (ElemSimple elem5e : elemList) { //суммирую колич. всех элементов (например штапиков)
                                if (elem5e.spcRec.artikl.equals(specifRec.artikl)) {
                                    quantity3 = quantity3 + elem5e.spcRec.quant1;
                                }
                                for (Specific specifRec2 : elem5e.spcRec.spcList) {
                                    if (specifRec2.artikl.equals(specifRec.artikl)) {
                                        quantity3 = quantity3 + specifRec2.quant1;
                                    }
                                }
                            }
                        } else { //по подтипу, типу
                            for (ElemSimple elem5e : elemList) { //суммирую колич. всех элементов (например штапиков)
                                Specific specifRec2 = elem5e.spcRec;
                                if (specifRec2.artiklRec.getInt(eArtikl.level1) * 100 + specifRec2.artiklRec.getInt(eArtikl.level2) == rulecalcRec.getInt(eRulecalc.type)) {
                                    quantity3 = quantity3 + elem5e.spcRec.quant1;
                                }
                                for (Specific specifRec3 : specifRec2.spcList) {
                                    if (specifRec3.artiklRec.getInt(eArtikl.level1) * 100 + specifRec3.artiklRec.getInt(eArtikl.level2) == rulecalcRec.getInt(eRulecalc.type)) {
                                        quantity3 = quantity3 + specifRec3.quant1;
                                    }
                                }
                            }
                        }
                        if (UCom.containsNumbJust(rulecalcRec.getStr(eRulecalc.quant), quantity3) == true) {
                            specifRec.price1 = specifRec.price1 * rulecalcRec.getFloat(eRulecalc.coeff) + rulecalcRec.getFloat(eRulecalc.incr); //увеличение себестоимости в coeff раз и на incr величину надбавки                      
                        }
                    }
                }
            }
        }
    }

    //В зав. от единицы изм. форматируется количество
    private float formatAmount(Specific spcRec) {
        //Нужна доработка для расчёта по минимальному тарифу. См. dll VirtualPro4::CalcArtTariff

        if (UseUnit.METR.id == spcRec.artiklRec.getInt(eArtikl.unit)) { //метры
            return spcRec.count * round(spcRec.width, precision) / 1000;

        } else if (UseUnit.METR2.id == spcRec.artiklRec.getInt(eArtikl.unit)) { //кв. метры
            return spcRec.count * round(spcRec.width, precision) * round(spcRec.height, precision) / 1000000;

        } else if (UseUnit.PIE.id == spcRec.artiklRec.getInt(eArtikl.unit)) { //шт.
            return spcRec.count;

        } else if (UseUnit.KIT.id == spcRec.artiklRec.getInt(eArtikl.unit)) { //комп.
            return spcRec.count;

        } else if (UseUnit.ML.id == spcRec.artiklRec.getInt(eArtikl.unit)) { //мл
            return spcRec.count;
        }
        return 0;
    }

    //Процентная надбавка на изделия сложной формы
    private float percentMarkup() {
        if (Type.ARCH == winc.rootArea.type()) {
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

    private static float round(float value, int places) {
        if (places == 0) {
            return value;
        }
        places = (places == 3) ? 1 : (places == 2) ? 2 : 3;
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.floatValue();
    }
}
