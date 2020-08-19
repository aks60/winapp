package estimate.constr;

import dataset.Record;
import domain.eArtdet;
import domain.eArtgrp;
import domain.eArtikl;
import domain.eColgrp;
import domain.eColor;
import domain.eCurrenc;
import domain.eRulecalc;
import domain.eSysdata;
import domain.eSystree;
import enums.LayoutArea;
import enums.TypeElem;
import enums.UseUnit;
import java.util.LinkedList;
import estimate.Wincalc;
import estimate.model.Com5t;
import estimate.model.ElemSimple;
import java.util.List;

/**
 * Расчёт стоимости элементов окна
 * <p>
 * Запускаю цикл по элементам конструкции окна. Произвожу расчёт собес-сти за
 * ед. изм. , калькуляцию количества без отхода и количества с отходом.
 * <p>
 * Запускаю цикл по правилам расчёта и пытаюсь попасть в правила расчёта. Если
 * удаётся попасть увеличиваю себестоимости в rkoef раз и на incr величину
 * надбавки. После завершения цикла правил расчёта произвожу расчёт собес-сти с
 * отходом.
 * <p>
 * Запускаю цикл по списку детализаций элемента конструкции. Произвожу расчёт
 * собес-сти за ед. изм. , калькуляцию количества без отхода и количества с
 * отходом.
 * <p>
 * Снова цикл по правилам расчёта. Если удаётся попасть в правила расчёта
 * увеличиваю себестоимости в rkoef раз и на incr величину надбавки. После
 * завершения цикла правил расчёта произвожу расчёт собес-сти с отходом При
 * завершении итерации перехожу к новому элементу конструкции и т.д.
 */
public class Tariffication extends Cal5e {

    //В прфстрое используюеся только 0, 4, 10, 12 параметры
    protected static final int PAR0 = 0;   //не проверять форму
    protected static final int PAR4 = 4;   //профиль с радиусом
    protected static final int PAR10 = 10; //не прямоугольное, не арочное заполнение
    protected static final int PAR12 = 12; //не прямоугольное заполнение с арками

    public Tariffication(Wincalc iwin) {
        super(iwin);
    }

    /**
     * @param elemList - список элементов окна рамы, импосты, стеклопакеты...
     */
    public void calc() {

        float percentMarkup = percentMarkup(); //процентная надбавка на изделия сложной формы
        //Расчёт  собес-сть за ед. изм. по таблице мат. ценностей
        for (ElemSimple elem5e : iwin.listElem) {
            calcCostPrice(elem5e.specificationRec);

            for (Specification specificationRec : elem5e.specificationRec.specificationList) {
                calcCostPrice(specificationRec);
            }
        }

        //Увеличение себестоимости в rkoef раз и на incr величину надбавки
        for (ElemSimple elem5e : iwin.listElem) {

            Record systreeRec = eSystree.find(iwin.nuni);
            TypeElem type = elem5e.owner().type();
            //Цикл по правилам расчёта
            for (Record rulecalcRec : eRulecalc.get()) {

                //Только эти параметры используются в БиМакс
                //фильтр по полю form, rused и colorXXX таблицы rulecls
                if (TypeElem.GLASS == elem5e.type()) {  //фильтр для стеклопакета

                    if (rulecalcRec.getInt(eRulecalc.form) == PAR0) {//не проверять форму
                        checkRuleColor(rulecalcRec, elem5e.specificationRec);

                    } else if (rulecalcRec.getInt(eRulecalc.form) == PAR10 && TypeElem.TRAPEZE == type) { //не прямоугольное, не арочное заполнение
                        checkRuleColor(rulecalcRec, elem5e.specificationRec);

                    } else if (rulecalcRec.getInt(eRulecalc.form) == PAR12 && TypeElem.ARCH == type) {//не прямоугольное заполнение с арками
                        checkRuleColor(rulecalcRec, elem5e.specificationRec);
                    }
                } else if (rulecalcRec.getInt(eRulecalc.form) == PAR4 && TypeElem.FRAME_SIDE == type && LayoutArea.ARCH == elem5e.layout()) { //фильтр для арки профиля AYPC.W62.0101
                    checkRuleColor(rulecalcRec, elem5e.specificationRec); //профиль с радиусом

                } else {
                    if (rulecalcRec.getInt(eRulecalc.form) == PAR0) {  //не проверять форму
                        checkRuleColor(rulecalcRec, elem5e.specificationRec); //всё остальное не проверять форму
                    }
                }
            }

            elem5e.specificationRec.outPrice = elem5e.specificationRec.inPrice * elem5e.specificationRec.quantity2; //себестоимость с отходом
            Record artgrpRec = eArtgrp.find(elem5e.artiklRec.getInt(eArtikl.artgrp_id));
            elem5e.specificationRec.inCost = elem5e.specificationRec.outPrice * artgrpRec.getFloat(eArtgrp.coef) * systreeRec.getFloat(eSystree.coef); //стоимость без скидки
            elem5e.specificationRec.inCost = elem5e.specificationRec.inCost + (elem5e.specificationRec.inCost / 100) * percentMarkup;
            elem5e.specificationRec.outCost = elem5e.specificationRec.inCost; //стоимость со скидкой

            for (Specification specifSubelemRec : elem5e.specificationRec.specificationList) {
                for (Record ruleclkRec : eRulecalc.get()) { //цикл по правилам расчёта

                    if (ruleclkRec.getInt(eRulecalc.form) == PAR0) {  //не проверять форму
                        checkRuleColor(ruleclkRec, specifSubelemRec);
                    }
                }
                specifSubelemRec.outPrice = specifSubelemRec.inPrice * specifSubelemRec.quantity2; //расчёт себестоимости с отходом
                Record artgrpRec2 = eArtgrp.find(specifSubelemRec.artiklRec.getInt(eArtikl.artgrp_id));
                specifSubelemRec.inCost = specifSubelemRec.outPrice * artgrpRec2.getFloat(eArtgrp.coef) * systreeRec.getFloat(eSystree.coef);
                specifSubelemRec.inCost = specifSubelemRec.inCost + (specifSubelemRec.inCost / 100) * percentMarkup;
                specifSubelemRec.outCost = specifSubelemRec.inCost;
            }
        }

        //Расчёт веса элемента конструкции
        for (ElemSimple elem5e : iwin.listElem) {
            for (Specification spec : elem5e.specificationRec.specificationList) {
                spec.weight = spec.quantity * spec.artiklRec.getFloat(eArtikl.density);
            }
        }
    }

    /**
     * Фильтр по полю riskl, colorXXX таблицы rulecls
     */
    private void checkRuleColor(Record rulecalcRec, Specification specifRec) {

        Integer[] arr1 = Util.parserInt(rulecalcRec.getStr(eRulecalc.color1));
        Integer[] arr2 = Util.parserInt(rulecalcRec.getStr(eRulecalc.color2));
        Integer[] arr3 = Util.parserInt(rulecalcRec.getStr(eRulecalc.color3));
        if (specifRec.artikl.equals(rulecalcRec.getStr(eArtikl.code))
                || ((specifRec.artiklRec.getInt(eArtikl.level1) * 100
                + specifRec.artiklRec.getInt(eArtikl.level1)) == rulecalcRec.getInt(eRulecalc.type))) { //артикл ИЛИ тип ИЛИ подтип совпали
            if (Util.compareColor(arr1, specifRec.color1) == true
                    && Util.compareColor(arr2, specifRec.color2) == true
                    && Util.compareColor(arr3, specifRec.color3) == true) {
                if (rulecalcRec.getInt(eRulecalc.common) == 0) {

                    boolean ret = Util.compareFloat(rulecalcRec.getStr(eRulecalc.quant), specifRec.quantity2);
                    if (ret == true) {
                        specifRec.inPrice = specifRec.inPrice * rulecalcRec.getFloat(eRulecalc.coeff) + rulecalcRec.getFloat(eRulecalc.incr);  //увеличение себестоимости в coegg раз и на incr величину надбавки
                    }

                } else if (rulecalcRec.getInt(eRulecalc.common) == 1) { //по использованию c расчётом общего количества по артикулу, подтипу, типу

                    LinkedList<ElemSimple> elemList = iwin.listElem;
                    float quantity3 = 0;
                    if (rulecalcRec.getInt(eRulecalc.type) < 0) { //по артикулу
                        for (ElemSimple elemBase : elemList) {
                            if (elemBase.specificationRec.artikl.equals(specifRec.artikl)) {

                                quantity3 = quantity3 + elemBase.specificationRec.quantity;
                            }
                            for (Specification specifRec2 : elemBase.specificationRec.specificationList) {
                                if (specifRec2.artikl.equals(specifRec.artikl)) {

                                    quantity3 = quantity3 + specifRec2.quantity;
                                }
                            }
                        }
                    } else { //по подтипу, типу

                        for (ElemSimple elemBase : elemList) {
                            Specification specifRec2 = elemBase.specificationRec;
                            if (specifRec2.artiklRec.getInt(eArtikl.level1) * 100 + specifRec2.artiklRec.getInt(eArtikl.level2) == rulecalcRec.getInt(eRulecalc.type)) {

                                quantity3 = quantity3 + elemBase.specificationRec.quantity;
                            }
                            for (Specification specifRec3 : specifRec2.specificationList) {
                                if (specifRec3.artiklRec.getInt(eArtikl.level1) * 100 + specifRec3.artiklRec.getInt(eArtikl.level2) == rulecalcRec.getInt(eRulecalc.type)) {

                                    quantity3 = quantity3 + specifRec3.quantity;
                                }
                            }
                        }
                    }
                    boolean ret = Util.compareFloat(rulecalcRec.getStr(eRulecalc.quant), quantity3);
                    if (ret == true) {
                        specifRec.inPrice = specifRec.inPrice * rulecalcRec.getFloat(eRulecalc.coeff) + rulecalcRec.getFloat(eRulecalc.incr);  //увеличение себестоимости в rkoef раз и на incr величину надбавки
                    }
                }
            }
        }
    }

    /**
     * Считает тариф для заданного артикула заданных цветов по таблице eArtdet
     * (Материальные ценности -> нижняя таблица)
     */
    public void calcCostPrice(Specification specificRec) {

        Record color1Rec = eColor.find(specificRec.color1);  //
        Record color2Rec = eColor.find(specificRec.color2);  //описание текстур
        Record color3Rec = eColor.find(specificRec.color3);  //

        Record kursBaseRec = eCurrenc.find(specificRec.artiklRec.getInt(eArtikl.currenc1_id));    // кросс-курс валюты для основной текстуры
        Record kursNoBaseRec = eCurrenc.find(specificRec.artiklRec.getInt(eArtikl.currenc2_id));  // кросс-курс валюты для неосновных текстур (внутренняя, внешняя, двухсторонняя)

        //Цикл по тарификационной таблице мат ценностей
        for (Record artdetRec : eArtdet.find(specificRec.artiklRec.getInt(eArtikl.id))) {

            float artdetTariff = 0;
            boolean artsvstRowUsed = false;

            if (artdetRec.getFloat(eArtdet.cost_cl4) != 0 && color2Rec.getInt(eColor.id) == color3Rec.getInt(eColor.id)
                    && (artdetRec.getInt(eArtdet.prefe) & 1) != 0 && (artdetRec.getInt(eArtdet.prefe) & 2) != 0
                    && IsArtTariffAppliesForColor(artdetRec, color2Rec)) { //если двухсторонняя текстура

                artdetTariff += (artdetRec.getInt(eArtdet.cost_cl4) * Math.max(color2Rec.getFloat(eColor.coef2), color2Rec.getFloat(eColor.coef3)) / kursNoBaseRec.getFloat(eCurrenc.cross_cour));
                artsvstRowUsed = true;

            } else {

                if ((artdetRec.getInt(eArtdet.prefe) & 4) != 0) {  //подбираем тариф основной текстуры
                    if (IsArtTariffAppliesForColor(artdetRec, color1Rec)) {
                        Record colgrpRec = eColgrp.find(color1Rec.getInt(eColor.colgrp_id));
                        artdetTariff += (artdetRec.getFloat(eArtdet.cost_cl1) * color1Rec.getFloat(eColor.coef1) * colgrpRec.getFloat(eColgrp.coeff)) / kursBaseRec.getFloat(eCurrenc.cross_cour);
                        artsvstRowUsed = true;
                    }
                }
                if ((artdetRec.getInt(eArtdet.prefe) & 4) != 0 || (artdetRec.getInt(eArtdet.prefe) & 1) != 0) { //подбираем тариф внутренней текстуры
                    if (IsArtTariffAppliesForColor(artdetRec, color2Rec)) {
                        Record colgrpRec = eColgrp.find(color2Rec.getInt(eColor.colgrp_id));
                        artdetTariff += (artdetRec.getFloat(eArtdet.cost_cl2) * color2Rec.getFloat(eColor.coef2) * colgrpRec.getFloat(eColgrp.coeff)) / kursNoBaseRec.getFloat(eCurrenc.cross_cour);
                        artsvstRowUsed = true;
                    }
                }
                if ((artdetRec.getInt(eArtdet.prefe) & 4) != 0 || (artdetRec.getInt(eArtdet.prefe) & 2) != 0) { //подбираем тариф внешней текстуры
                    if (IsArtTariffAppliesForColor(artdetRec, color3Rec)) {
                        Record colgrpRec = eColgrp.find(color3Rec.getInt(eColor.colgrp_id));
                        artdetTariff += (artdetRec.getFloat(eArtdet.cost_cl3) * color3Rec.getFloat(eColor.coef3) * colgrpRec.getFloat(eColgrp.coeff)) / kursNoBaseRec.getFloat(eCurrenc.cross_cour);
                        artsvstRowUsed = true;
                    }
                }
            }
            if (artsvstRowUsed && artdetRec.getFloat(eArtdet.cost_min) != 0 && specificRec.quantity != 0 && artdetTariff * specificRec.quantity < artdetRec.getFloat(eArtdet.cost_min)) {
                artdetTariff = artdetRec.getFloat(eArtdet.cost_min) / specificRec.quantity;    //используем минимальный тариф 
            }
            if (artsvstRowUsed) {
                specificRec.inPrice = specificRec.inPrice + (artdetTariff * artdetRec.getFloat(eArtdet.coef_nakl));
            }
        }
        //TODO Нужна доработка для расчёта по минимальному тарифу. См. dll VirtualPro4::CalcArtTariff
        if (UseUnit.METR.id == specificRec.artiklRec.getInt(eArtikl.unit)) { //метры
            specificRec.quantity = specificRec.width / 1000;

        } else if (UseUnit.METR2.id == specificRec.artiklRec.getInt(eArtikl.unit)) { //кв. метры
            specificRec.quantity = specificRec.width * specificRec.height / 1000000;

        } else if (UseUnit.PIE.id == specificRec.artiklRec.getInt(eArtikl.unit)) { //шт.
            specificRec.quantity = specificRec.count;
        }
        specificRec.quantity2 = specificRec.quantity + (specificRec.quantity * specificRec.artiklRec.getFloat(eArtikl.otx_norm) / 100);
    }

    private float percentMarkup() {
        if (TypeElem.ARCH == iwin.rootArea.type()) {
            return eSysdata.find(2101).getInt(eSysdata.val);
        }
        return 0;
    }

    //Проверяет, должен ли применяться заданный тариф мат-ценности для заданной текстуры
    protected boolean IsArtTariffAppliesForColor(Record artdetRec, Record colorRec) {
        if (artdetRec.getInt(eArtdet.color_fk) < 0) {    //этот тариф задан для группы текстур

            if ((-1 * colorRec.getInt(eColor.colgrp_id)) == artdetRec.getInt(eArtdet.color_fk)) {
                return true;
            }
        } else {  //проверяем не только colorCode, а еще и colorNumber
            if (colorRec.getInt(eColor.id) == artdetRec.getInt(eArtdet.color_fk)) {
                return true;

            }
//            else if (colorRec.cnumb == artdetRec.getInt(eArtdet.color_fk)) {
//                return true;
//            }
        }
        return false;
    }
}
