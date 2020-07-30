package estimate.constr;

import dataset.Record;
import domain.eArtgrp;
import domain.eArtikl;
import domain.eRulecalc;
import domain.eSysdata;
import domain.eSystree;
import enums.LayoutArea;
import enums.TypeElem;
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
 * удаётся попасть увеличиваю себестоимости в rkoef раз и на rpric величину
 * надбавки. После завершения цикла правил расчёта произвожу расчёт собес-сти с
 * отходом.
 * <p>
 * Запускаю цикл по списку детализаций элемента конструкции. Произвожу расчёт
 * собес-сти за ед. изм. , калькуляцию количества без отхода и количества с
 * отходом.
 * <p>
 * Снова цикл по правилам расчёта. Если удаётся попасть в правила расчёта
 * увеличиваю себестоимости в rkoef раз и на rpric величину надбавки. После
 * завершения цикла правил расчёта произвожу расчёт собес-сти с отходом При
 * завершении итерации перехожу к новому элементу конструкции и т.д.
 */
public class Tariffication {

    //В прфстрое используюеся только 0, 4, 10, 12 параметры
    protected static final int PAR0 = 0;   //не проверять форму
    protected static final int PAR4 = 4;   //профиль с радиусом
    protected static final int PAR10 = 10; //не прямоугольное, не арочное заполнение
    protected static final int PAR12 = 12; //не прямоугольное заполнение с арками
    protected Wincalc iwin = null;
    protected float percentMarkup = 0;  //процентная надбавка на изделия сложной формы
    protected List<Record> rulecalcList = eRulecalc.get();

    public Tariffication(Wincalc iwin) {
        this.iwin = iwin;
        percentMarkup();
    }

    /**
     * @param elemList - список элементов окна рамы, импосты, стеклопакеты...
     */
    public void calculate(LinkedList<Com5t> elemList) {

        //Расчёт  собес-сть за ед. изм. по таблице мат. ценностей
        for (ElemSimple elem5e : iwin.listElem) {
            calcCostPrice(elem5e.specificationRec);
            for (Specification specifSubelemRec : elem5e.specificationRec.specificationList) {

                calcCostPrice(specifSubelemRec);
            }
        }

        //Увеличение собестоимости в rkoef раз и на rpric величину надбавки
        for (ElemSimple elem5e : iwin.listElem) {

            Record systreeRec = eSystree.find(iwin.nuni);
            TypeElem type = elem5e.owner().type();
            //Цикл по правилам расчёта
            for (Record rulecalcRec : rulecalcList) {

                //Только эти параметры используются в БиМакс
                //фильтр по полю form, rused и colorXXX таблицы rulecls
                if (TypeElem.GLASS == elem5e.type()) {//фильтр для стеклопакета

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

            elem5e.specificationRec.outPrice = elem5e.specificationRec.inPrice * elem5e.specificationRec.quantity2; //собестоимость с отходом
            Record artgrpRec = eArtgrp.find(elem5e.artiklRec.getInt(eArtikl.artgrp_id));
            elem5e.specificationRec.inCost = elem5e.specificationRec.outPrice * artgrpRec.getFloat(eArtgrp.coef) * systreeRec.getFloat(eSystree.coef); //стоимость без скидки
            elem5e.specificationRec.inCost = elem5e.specificationRec.inCost + (elem5e.specificationRec.inCost / 100) * percentMarkup;
            elem5e.specificationRec.outCost = elem5e.specificationRec.inCost; //стоимость со скидкой

            for (Specification specifSubelemRec : elem5e.specificationRec.specificationList) {
                for (Record ruleclkRec : rulecalcList) { //цикл по правилам расчёта

                    if (ruleclkRec.getInt(eRulecalc.form) == PAR0) {  //не проверять форму
                        checkRuleColor(ruleclkRec, specifSubelemRec);
                    }
                }
                specifSubelemRec.outPrice = specifSubelemRec.inPrice * specifSubelemRec.quantity2; //расчёт собестоимости с отходом
                Record artgrpRec2 = eArtgrp.find(specifSubelemRec.artiklRec.getInt(eArtikl.artgrp_id));
                specifSubelemRec.inCost = specifSubelemRec.outPrice * artgrpRec2.getFloat(eArtgrp.coef) * systreeRec.getFloat(eSystree.coef);
                specifSubelemRec.inCost = specifSubelemRec.inCost + (specifSubelemRec.inCost / 100) * percentMarkup;
                specifSubelemRec.outCost = specifSubelemRec.inCost;
            }
        }

        //Расчёт веса элемента конструкции
        for (Com5t com5t : elemList) {
            for (Specification spec : ((ElemSimple) com5t).specificationRec.specificationList) {
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
        if (specifRec.artikl.equals(rulecalcRec.getStr(eArtikl.code)) || ((specifRec.artiklRec.getInt(eArtikl.level1) * 100 + specifRec.artiklRec.getInt(eArtikl.level1)) == rulecalcRec.getInt(e))) { //артикл ИЛИ тип ИЛИ подтип совпали
//            if (compareColor(arr1, specifRec.colorBase) == true && compareColor(arr2, specifRec.colorInternal) == true && compareColor(arr3, specifRec.colorExternal) == true) {
//                if (ruleclkRec.rallp == 0) {
//
//                    boolean ret = compareFloat(ruleclkRec.rleng, specifRec.quantity2);
//                    if (ret == true) {
//                        specifRec.inPrice = specifRec.inPrice * ruleclkRec.rkoef + ruleclkRec.rpric;  //увеличение собестоимости в rkoef раз и на rpric величину надбавки
//                    }
//
//                } else if (ruleclkRec.rallp == 1) { //по использованию c расчётом общего количества по артикулу, подтипу, типу
//
//                    LinkedList<ElemBase> elemList = root.getElemList(null);
//                    float quantity3 = 0;
//                    if (ruleclkRec.rused < 0) { //по артикулу
//                        for (ElemBase elemBase : elemList) {
//                            if (elemBase.getSpecificationRec().artikl.equals(specifRec.artikl)) {
//
//                                quantity3 = quantity3 + elemBase.getSpecificationRec().quantity;
//                            }
//                            for (Specification specifRec2 : elemBase.getSpecificationRec().getSpecificationList()) {
//                                if (specifRec2.artikl.equals(specifRec.artikl)) {
//
//                                    quantity3 = quantity3 + specifRec2.quantity;
//                                }
//                            }
//                        }
//                    } else { //по подтипу, типу
//
//                        for (ElemBase elemBase : elemList) {
//                            Specification specifRec2 = elemBase.getSpecificationRec();
//                            if (specifRec2.getArticRec().atypm * 100 + specifRec2.getArticRec().atypp == ruleclkRec.rused) {
//
//                                quantity3 = quantity3 + elemBase.getSpecificationRec().quantity;
//                            }
//                            for (Specification specifRec3 : specifRec2.getSpecificationList()) {
//                                if (specifRec3.getArticRec().atypm * 100 + specifRec3.getArticRec().atypp == ruleclkRec.rused) {
//
//                                    quantity3 = quantity3 + specifRec3.quantity;
//                                }
//                            }
//                        }
//                    }
//                    boolean ret = compareFloat(ruleclkRec.rleng, quantity3);
//                    if (ret == true) {
//                        specifRec.inPrice = specifRec.inPrice * ruleclkRec.rkoef + ruleclkRec.rpric;  //увеличение собестоимости в rkoef раз и на rpric величину надбавки
//                    }
//                }
//            }
        }
    }

    /**
     * Считает тариф для заданного артикула заданных цветов по таблице
     * PRO4_ARTSVST (Материальные ценности -> нижняя таблица)
     */
    public void calcCostPrice(Specification specificRec) {
//
//        Colslst baseColorRec = Colslst.get2(constr, specificRec.colorBase);        //
//        Colslst insideColorRec = Colslst.get2(constr, specificRec.colorInternal);  //описание текстур
//        Colslst outsideColorRec = Colslst.get2(constr, specificRec.colorExternal); //
//
//        Correnc kursBaseRec = Correnc.get(constr, specificRec.getArticRec().cnumb);    // кросс-курс валюты для основной текстуры
//        Correnc kursNoBaseRec = Correnc.get(constr, specificRec.getArticRec().cnumt);  // кросс-курс валюты для неосновных текстур (внутренняя, внешняя, двухсторонняя)
//
//        //Цикл по тарификационной таблице мат ценностей
//        for (Artsvst artsvst : Artsvst.get(constr, specificRec.getArticRec().anumb)) {
//
//            float artsvstRowTariff = 0;
//            boolean artsvstRowUsed = false;
//
//            if (artsvst.clpra != 0 && insideColorRec.cnumb == outsideColorRec.cnumb
//                    && CanBeUsedAsInsideColor(artsvst) && CanBeUsedAsOutsideColor(artsvst)
//                    && IsArtTariffAppliesForColor(artsvst, insideColorRec)) { //если двухсторонняя текстура
//
//                artsvstRowTariff += (artsvst.clpra * Math.max(insideColorRec.koef1, insideColorRec.koef2) / kursNoBaseRec.ckurs);
//                artsvstRowUsed = true;
//
//            } else {
//
//                Object ooo = CanBeUsedAsBaseColor(artsvst);
//                Object ooo2 = IsArtTariffAppliesForColor(artsvst, baseColorRec);
//
//                if (CanBeUsedAsBaseColor(artsvst) && IsArtTariffAppliesForColor(artsvst, baseColorRec)) { //подбираем тариф основной текстуры
//                    Grupcol grupcolRec = Grupcol.get(constr, baseColorRec.cgrup);
//                    artsvstRowTariff += (artsvst.clprc * baseColorRec.ckoef * grupcolRec.gkoef) / kursBaseRec.ckurs;
//                    artsvstRowUsed = true;
//                }
//                if (CanBeUsedAsInsideColor(artsvst) && IsArtTariffAppliesForColor(artsvst, insideColorRec)) { //подбираем тариф внутренней текстуры
//                    Grupcol grupcolRec = Grupcol.get(constr, insideColorRec.cgrup);
//                    artsvstRowTariff += (artsvst.clpr1 * insideColorRec.koef1 * grupcolRec.gkoef) / kursNoBaseRec.ckurs;
//                    artsvstRowUsed = true;
//                }
//                if (CanBeUsedAsOutsideColor(artsvst) && IsArtTariffAppliesForColor(artsvst, outsideColorRec)) { //подбираем тариф внешней текстуры
//                    Grupcol grupcolRec = Grupcol.get(constr, outsideColorRec.cgrup);
//                    artsvstRowTariff += (artsvst.clpr2 * outsideColorRec.koef2 * grupcolRec.gkoef) / kursNoBaseRec.ckurs;
//                    artsvstRowUsed = true;
//                }
//            }
//            if (artsvstRowUsed && artsvst.cminp != 0 && specificRec.quantity != 0 && artsvstRowTariff * specificRec.quantity < artsvst.cminp) {
//                artsvstRowTariff = artsvst.cminp / specificRec.quantity;    //используем минимальный тариф (CMINP)
//            }
//            if (artsvstRowUsed) {
//                specificRec.inPrice = specificRec.inPrice + (artsvstRowTariff * artsvst.knakl);
//            }
//        }
//        //TODO Нужна доработка для расчёта по минимальному тарифу. См. dll VirtualPro4::CalcArtTariff
//        if (MeasUnit.METR.value == specificRec.getArticRec().atypi) { //метры
//            specificRec.quantity = specificRec.width / 1000;
//
//        } else if (MeasUnit.METR2.value == specificRec.getArticRec().atypi) { //кв. метры
//            specificRec.quantity = specificRec.width * specificRec.height / 1000000;
//
//        } else if (MeasUnit.PIE.value == specificRec.getArticRec().atypi) { //шт.
//            specificRec.quantity = specificRec.count;
//        }
//        specificRec.quantity2 = specificRec.quantity + (specificRec.quantity * specificRec.getArticRec().aouts / 100);
    }

    private void percentMarkup() {
        if (TypeElem.ARCH == iwin.rootArea.type()) {
            percentMarkup = eSysdata.find(2101).getInt(eSysdata.val);
        }
    }
}
