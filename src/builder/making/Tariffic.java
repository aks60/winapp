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
import builder.IElem5e;
import common.ArrayList2;
import common.UCom;
import dataset.Query;
import domain.ePrjkit;
import domain.ePrjprod;
import domain.eProject;
import enums.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Расчёт стоимости элементов окна алгоритм см. в UML
 */
public class Tariffic extends Cal5e {

    private static boolean norm_otx = true;
    private static int precision = Math.round(new Query(eGroups.values()).select(eGroups.up).get(0).getFloat(eGroups.val)); //округление длины профилей

    public Tariffic(Wincalc winc, boolean norm_otx) {
        super(winc);
        this.norm_otx = norm_otx;
    }

    //Тарификация конструкции
    public void calc() {
        try {
            super.calc();
            float percentMarkup = percentMarkup(winc); //процентная надбавка на изделия сложной формы

            //Расчёт себес-сти за ед.изм. и колич. материала
            //Цикл по эдементам конструкции
            for (IElem5e elem5e : winc.listElem) {
                if (filter(elem5e)) {

                    elem5e.spcRec().costpric1 += artdetPrice(elem5e.spcRec()); //себест. за ед. без отхода по табл. ARTDET с коэф. и надб.
                    elem5e.spcRec().quant1 = formatAmount(elem5e.spcRec()); //количество без отхода
                    elem5e.spcRec().quant2 = elem5e.spcRec().quant1;  //базовое количество с отходом
                    if (norm_otx == true) {
                        float otx = elem5e.spcRec().artiklRec.getFloat(eArtikl.otx_norm);
                        elem5e.spcRec().quant2 = elem5e.spcRec().quant2 + (elem5e.spcRec().quant1 * otx / 100); //количество с отходом
                    }
                    //Вложенная спецификация
                    //Цикл по детализации эдемента
                    for (Specific specificationRec2 : elem5e.spcRec().spcList) {
                        specificationRec2.costpric1 += artdetPrice(specificationRec2); //себест. за ед. без отхода
                        specificationRec2.quant1 = formatAmount(specificationRec2); //количество без отхода
                        specificationRec2.quant2 = specificationRec2.quant1; //базовое количество с отходом
                        if (norm_otx == true) {
                            float otx = specificationRec2.artiklRec.getFloat(eArtikl.otx_norm);
                            specificationRec2.quant2 = specificationRec2.quant2 + (specificationRec2.quant1 * otx / 100); //количество с отходом
                        }
                    }
                }
            }

            //Расчёт стоимости
            //Цикл по эдементам конструкции
            for (IElem5e elem5e : winc.listElem) {
                if (filter(elem5e)) {

                    Record systreeRec = eSystree.find(winc.nuni());
                    //Цикл по правилам расчёта.                 
                    for (Record rulecalcRec : eRulecalc.list()) {
                        //Всё обнуляется и рассчитывается по таблице правил расчёта
                        //Увеличение себестоимости в coeff раз и на incr величину наценки.

                        //Фильтр по полю 'форма профиля', в заполнениях. В БиМакс используюеся только 1, 4, 10, 12 параметры
                        int form = (rulecalcRec.getInt(eRulecalc.form) == 0) ? 1 : rulecalcRec.getInt(eRulecalc.form);
                        if (Type.GLASS == elem5e.type()) {//фильтр для стеклопакета

                            if (form == TypeForm.P00.id) {//не проверять форму
                                rulecalcPrise(winc, rulecalcRec, elem5e.spcRec());

                            } else if (form == TypeForm.P10.id && Type.TRAPEZE == elem5e.owner().type()) { //не прямоугольное, не арочное заполнение
                                rulecalcPrise(winc, rulecalcRec, elem5e.spcRec());

                            } else if (form == TypeForm.P12.id && Type.ARCH == elem5e.owner().type()) {//не прямоугольное заполнение с арками
                                rulecalcPrise(winc, rulecalcRec, elem5e.spcRec());
                            }
                        } else if (form == TypeForm.P04.id && elem5e.type() == Type.FRAME_SIDE
                                && elem5e.owner().type() == Type.ARCH && elem5e.layout() == Layout.TOP) {  //профиль с радиусом  (фильтр для арки профиля AYPC.W62.0101)
                            rulecalcPrise(winc, rulecalcRec, elem5e.spcRec()); //профиль с радиусом

                        } else {
                            if (form == TypeForm.P00.id) {  //не проверять форму
                                rulecalcPrise(winc, rulecalcRec, elem5e.spcRec()); //всё остальное не проверять форму
                            }
                        }
                    }

                    elem5e.spcRec().costpric2 = elem5e.spcRec().costpric1 * elem5e.spcRec().quant2; //себест. за ед. с отходом 
                    Record artgrp1Rec = eGroups.find(elem5e.spcRec().artiklRec.getInt(eArtikl.groups1_id));
                    Record artgrp2Rec = eGroups.find(elem5e.spcRec().artiklRec.getInt(eArtikl.groups2_id));
                    float k1 = artgrp1Rec.getFloat(eGroups.val, 1);  //наценка группы мат.ценностей
                    float k2 = artgrp2Rec.getFloat(eGroups.val, 0);  //скидки группы мат.ценностей
                    float k3 = systreeRec.getFloat(eSystree.coef, 1); //коэф. рентабельности
                    elem5e.spcRec().price = elem5e.spcRec().costpric2 * k1 * k3;
                    elem5e.spcRec().price = elem5e.spcRec().price + (elem5e.spcRec().price / 100) * percentMarkup; //стоимость без скидки                     
                    elem5e.spcRec().cost2 = elem5e.spcRec().price - (elem5e.spcRec().price / 100) * k2; //стоимость со скидкой 

                    //Правила расчёта вложенные
                    for (Specific spc : elem5e.spcRec().spcList) {

                        //Цикл по правилам расчёта.
                        for (Record rulecalcRec : eRulecalc.list()) {
                            int form = (rulecalcRec.getInt(eRulecalc.form) == 0) ? 1 : rulecalcRec.getInt(eRulecalc.form);
                            if (form == TypeForm.P00.id) { //не проверять форму 
                                rulecalcPrise(winc, rulecalcRec, spc);
                            }
                        }
                        spc.costpric2 = spc.costpric1 * spc.quant2; //себест. за ед. с отходом  
                        Record artgrp1bRec = eGroups.find(spc.artiklRec.getInt(eArtikl.groups1_id));
                        Record artgrp2bRec = eGroups.find(spc.artiklRec.getInt(eArtikl.groups2_id));
                        float m1 = artgrp1bRec.getFloat(eGroups.val, 1);  //наценка группы мат.ценностей
                        float m2 = artgrp2bRec.getFloat(eGroups.val, 0);  //скидки группы мат.ценностей
                        float m3 = systreeRec.getFloat(eSystree.coef); //коэф. рентабельности
                        spc.price = spc.costpric2 * m1 * m3;
                        spc.price = spc.price + (spc.price / 100) * percentMarkup; //стоимость без скидки                         
                        spc.cost2 = spc.price - (spc.price / 100) * m2; //стоимость со скидкой 
                    }
                }
            }

            //Расчёт веса элемента конструкции
            for (IElem5e elem5e : winc.listElem) {
                if (filter(elem5e)) {
                    elem5e.spcRec().weight = elem5e.spcRec().quant1 * elem5e.spcRec().artiklRec.getFloat(eArtikl.density);

                    for (Specific spec : elem5e.spcRec().spcList) {
                        spec.weight = spec.quant1 * spec.artiklRec.getFloat(eArtikl.density);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка:Tariffic.calc() " + e);
        } finally {
            Query.conf = conf;
        }
    }

    //Комплекты конструкции    
    public static ArrayList2<Specific> kits(Record prjprodRec, Wincalc winc, boolean norm_otx) {
        ArrayList2<Specific> kitList = new ArrayList2();
        try {
            Record systreeRec = eSystree.find(winc.nuni()); //для нахожд. коэф. рентабельности
            float percentMarkup = percentMarkup(winc); //процентная надбавка на изделия сложной формы
            if (prjprodRec != null) {
                List<Record> prjkitList = ePrjkit.find3(prjprodRec.getInt(ePrjprod.id));

                //Цикл по комплектам
                for (Record prjkitRec : prjkitList) {
                    Record artiklRec = eArtikl.find(prjkitRec.getInt(ePrjkit.artikl_id), true);
                    if (artiklRec != null) {
                        Specific spc = new Specific(winc.genId(), prjkitRec, artiklRec, null);
                        spc.place = "КОМП";
                        spc.width = prjkitRec.getFloat(ePrjkit.width);
                        spc.height = prjkitRec.getFloat(ePrjkit.height);
                        spc.count = prjkitRec.getFloat(ePrjkit.numb);
                        spc.colorID1 = prjkitRec.getInt(ePrjkit.color1_id);
                        spc.colorID2 = prjkitRec.getInt(ePrjkit.color2_id);
                        spc.colorID3 = prjkitRec.getInt(ePrjkit.color3_id);
                        spc.anglCut1 = prjkitRec.getFloat(ePrjkit.angl1);
                        spc.anglCut2 = prjkitRec.getFloat(ePrjkit.angl2);
                        spc.quant1 = formatAmount(spc); //количество без отхода
                        spc.quant2 = spc.quant1; //базовое количество с отходом
                        if (norm_otx == true) {
                            float otx = spc.artiklRec.getFloat(eArtikl.otx_norm);
                            spc.quant2 = spc.quant2 + (spc.quant1 * otx / 100); //количество с отходом
                        }
                        spc.costpric1 += artdetPrice(spc); //себест. за ед. без отхода по табл. ARTDET с коэф. и надб.
                        spc.costpric2 = spc.costpric1 * spc.quant2; //себест. за ед. с отходом 
                        Record artgrp1Rec = eGroups.find(spc.artiklRec.getInt(eArtikl.groups1_id));
                        Record artgrp2Rec = eGroups.find(spc.artiklRec.getInt(eArtikl.groups2_id));
                        float k1 = artgrp1Rec.getFloat(eGroups.val, 1);  //наценка группы мат.ценностей
                        float k2 = artgrp2Rec.getFloat(eGroups.val, 0);  //скидки группы мат.ценностей
                        float k3 = systreeRec.getFloat(eSystree.coef, 1); //коэф. рентабельности
                        spc.price = spc.costpric2 * k1 * k3;
                        spc.price = spc.price + (spc.price / 100) * percentMarkup; //стоимость без скидки                     
                        spc.cost2 = spc.price - (spc.price / 100) * k2; //стоимость со скидкой 
                        kitList.add(spc);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка:Tariffic.kits() " + e);
        }
        return kitList;
    }

    //Себес-сть за ед. изм. Рассчёт тарифа для заданного артикула заданных цветов по таблице eArtdet
    private static float artdetPrice(Specific specificRec) {

        float inPrice = 0;
        Record color1Rec = eColor.find(specificRec.colorID1);  //основная
        Record color2Rec = eColor.find(specificRec.colorID2);  //внутренняя
        Record color3Rec = eColor.find(specificRec.colorID3);  //внешняя

        Record kursBaseRec = eCurrenc.find(specificRec.artiklRec.getInt(eArtikl.currenc1_id));    // кросс-курс валюты для основной текстуры
        Record kursNoBaseRec = eCurrenc.find(specificRec.artiklRec.getInt(eArtikl.currenc2_id));  // кросс-курс валюты для неосновных текстур (внутренняя, внешняя, двухсторонняя)

        //Цикл по тарификационной таблице ARTDET мат. ценностей
        for (Record artdetRec : eArtdet.find(specificRec.artiklRec.getInt(eArtikl.id))) {

            float artdetPrice = 0;
            boolean artdetUsed = false;
            float k0 = specificRec.artiklRec.getFloat(eArtikl.coeff); //Ценовой коэффициент артикула

            //Если тариф двухсторонней текстуры не равен 0, и если
            //текстура1 равна текстура2 и заданный тариф применим
            if (artdetRec.getFloat(eArtdet.cost_c4) != 0
                    && color2Rec.getInt(eColor.id) == color3Rec.getInt(eColor.id)
                    && isTariff(artdetRec, color2Rec)) {

                float k1 = artdetRec.getFloat(eArtdet.cost_c4); //тариф двухсторонней текстуры
                float k2 = color2Rec.getFloat(eColor.coef2); //ценовой коэф.внутренний текстуры
                float k3 = color3Rec.getFloat(eColor.coef3); //ценовой коэф.внешний текстуры
                float k5 = kursNoBaseRec.getFloat(eCurrenc.cross_cour); //кросс курс            
                artdetPrice += (k0 * k1 * Math.max(k2, k3) / k5);

                if (isTariff(artdetRec, color1Rec)) { //подбираем тариф основной текстуры
                    float m1 = artdetRec.getFloat(eArtdet.cost_unit); //тариф единица измерения
                    float m2 = (specificRec.elem5e == null) ? 0 : specificRec.elem5e.artiklRec().getFloat(eArtikl.density); //удельный вес                    
                    if (m1 > 0 && m2 > 0) {
                        artdetPrice += m1 * m2;
                    } else {
                        Record colgrpRec = eGroups.find(color1Rec.getInt(eColor.colgrp_id));
                        float z1 = artdetRec.getFloat(eArtdet.cost_c1); //тариф основной текстуры"
                        float z2 = color1Rec.getFloat(eColor.coef1); //ценовой коэф.основной текст.
                        float z3 = colgrpRec.getFloat(eGroups.val); //коэф. группы текстур
                        float z5 = kursBaseRec.getFloat(eCurrenc.cross_cour); //кросс курс
                        artdetPrice += (k0 * z1 * z2 * z3 * z5) / z5;
                    }
                }
                artdetUsed = true;

                //Сложение цен по трём текстурам
            } else {
                //Подбираем тариф основной текстуры
                if (isTariff(artdetRec, color1Rec)) {
                    Record colgrpRec = eGroups.find(color1Rec.getInt(eColor.colgrp_id));
                    float k1 = artdetRec.getFloat(eArtdet.cost_c1); //тариф основной текстуры
                    float k2 = color1Rec.getFloat(eColor.coef1); //ценовой коэф.основной текстуры
                    float k3 = colgrpRec.getFloat(eGroups.val); //коэф. группы текстур
                    float k5 = kursBaseRec.getFloat(eCurrenc.cross_cour); //кросс курс
                    artdetPrice += (k0 * k1 * k2 * k3) / k5;
                    artdetUsed = true;
                }
                //Подбираем тариф внутренней текстуры
                if (isTariff(artdetRec, color2Rec)) {
                    Record colgrpRec = eGroups.find(color2Rec.getInt(eColor.colgrp_id));
                    float d1 = artdetRec.getFloat(eArtdet.cost_c2); //тариф внутренний текстуры
                    float d2 = color2Rec.getFloat(eColor.coef2); //ценовой коэф.внутренний текстуры
                    float d3 = colgrpRec.getFloat(eGroups.val); //коэф. группы текстур
                    float d5 = kursNoBaseRec.getFloat(eCurrenc.cross_cour); //кросс курс
                    artdetPrice += (k0 * d1 * d2 * d3) / d5;
                    artdetUsed = true;
                }
                //Подбираем тариф внешней текстуры
                if (isTariff(artdetRec, color3Rec)) {
                    Record colgrpRec = eGroups.find(color3Rec.getInt(eColor.colgrp_id));
                    float w1 = artdetRec.getFloat(eArtdet.cost_c3); //Тариф внешний текстуры
                    float w2 = color3Rec.getFloat(eColor.coef3); //Ценовой коэф.внешний текстуры
                    float w3 = colgrpRec.getFloat(eGroups.val); //коэф. группы текстур
                    float w5 = kursNoBaseRec.getFloat(eCurrenc.cross_cour); //кросс курс
                    artdetPrice += (k0 * w1 * w2 * w3) / w5;
                    artdetUsed = true;
                }
            }
            //Проверка минимального тарифа (Непонятная проверка)
            /*if (artdetUsed && artdetRec.getFloat(eArtdet.cost_min) != 0
                    && specificRec.quant1 != 0 && artdetPrice
                    * specificRec.quant1 < artdetRec.getFloat(eArtdet.cost_min)) {

                artdetPrice = artdetRec.getFloat(eArtdet.cost_min) / specificRec.quant1;
            }*/
            if (artdetUsed) { //если было попадание
                inPrice = inPrice + (artdetPrice
                        * artdetRec.getFloat(eArtdet.coef)); //kоэф. текстуры уникальн. арт.
            }
        }
        return inPrice;
    }

    //Правила расчёта. Фильтр по полю form, color(1,2,3) таблицы RULECALC
    private static void rulecalcPrise(Wincalc winc, Record rulecalcRec, Specific spcRec) {

        try {
            //Если артикул ИЛИ тип ИЛИ подтип совпали
            if (spcRec.artiklRec.get(eArtikl.id) != null
                    && (spcRec.artiklRec.get(eArtikl.id).equals(rulecalcRec.get(eRulecalc.artikl_id)) == true || rulecalcRec.get(eRulecalc.artikl_id) == null)) {

                if ((spcRec.artiklRec.getInt(eArtikl.level1) * 100 + spcRec.artiklRec.getInt(eArtikl.level2)) == rulecalcRec.getInt(eRulecalc.type)) {
                    if (UCom.containsNumbJust(rulecalcRec.getStr(eRulecalc.color1), spcRec.colorID1) == true
                            && UCom.containsNumbJust(rulecalcRec.getStr(eRulecalc.color2), spcRec.colorID2) == true
                            && UCom.containsNumbJust(rulecalcRec.getStr(eRulecalc.color3), spcRec.colorID3) == true) {

                        if (rulecalcRec.getInt(eRulecalc.common) == 0) {
                            if (UCom.containsNumbJust(rulecalcRec.getStr(eRulecalc.quant), spcRec.quant2) == true) {
                                spcRec.costpric1 = spcRec.costpric1 * rulecalcRec.getFloat(eRulecalc.coeff) + rulecalcRec.getFloat(eRulecalc.incr);  //увеличение себестоимости в coegg раз и на incr величину надбавки
                            }

                        } else if (rulecalcRec.getInt(eRulecalc.common) == 1) { //по использованию c расчётом общего количества по артикулу, подтипу, типу
                            LinkedList<IElem5e> elemList = winc.listElem;
                            float quantity3 = 0;
                            if (rulecalcRec.get(eRulecalc.artikl_id) != null) { //по артикулу
                                for (IElem5e elem5e : elemList) { //суммирую колич. всех элементов (например штапиков)
                                    if (filter(elem5e)) {
                                        if (elem5e.spcRec().artikl.equals(spcRec.artikl)) {
                                            quantity3 = quantity3 + elem5e.spcRec().quant1;
                                        }
                                        for (Specific specifRec2 : elem5e.spcRec().spcList) {
                                            if (specifRec2.artikl.equals(spcRec.artikl)) {
                                                quantity3 = quantity3 + specifRec2.quant1;
                                            }
                                        }
                                    }
                                }
                            } else { //по подтипу, типу
                                for (IElem5e elem5e : elemList) { //суммирую колич. всех элементов (например штапиков)
                                    if (filter(elem5e)) {
                                        Specific specifRec2 = elem5e.spcRec();
                                        if (specifRec2.artiklRec.getInt(eArtikl.level1) * 100 + specifRec2.artiklRec.getInt(eArtikl.level2) == rulecalcRec.getInt(eRulecalc.type)) {
                                            quantity3 = quantity3 + elem5e.spcRec().quant1;
                                        }
                                        for (Specific specifRec3 : specifRec2.spcList) {
                                            if (specifRec3.artiklRec.getInt(eArtikl.level1) * 100 + specifRec3.artiklRec.getInt(eArtikl.level2) == rulecalcRec.getInt(eRulecalc.type)) {
                                                quantity3 = quantity3 + specifRec3.quant1;
                                            }
                                        }
                                    }
                                }
                            }
                            if (UCom.containsNumbJust(rulecalcRec.getStr(eRulecalc.quant), quantity3) == true) {
                                spcRec.costpric1 = spcRec.costpric1 * rulecalcRec.getFloat(eRulecalc.coeff) + rulecalcRec.getFloat(eRulecalc.incr); //увеличение себестоимости в coeff раз и на incr величину надбавки                      
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка:Tariffic.rulecalcPrise() " + e);
        }
    }

    //В зав. от единицы изм. форматируется количество
    private static float formatAmount(Specific spcRec) {
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
    private static float percentMarkup(Wincalc winc) {
        if (Type.ARCH == winc.rootArea.type()) {
            return eGroups.find(2101).getFloat(eGroups.val);

        } else if (Type.RECTANGL != winc.rootArea.type()) {
            return eGroups.find(2104).getFloat(eGroups.val);
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

    //Фильтр на фантомы, грязные фичи...
    private static boolean filter(IElem5e elem5e) {
        if (elem5e.artiklRec() == null) {
            return false;
        } else if (elem5e.artiklRec().getInt(eArtikl.id) == -3) {
            return false;
        } else if (elem5e.spcRec().artiklRec == null) {
            return false;
        } else if (elem5e.spcRec().artiklRec.getInt(eArtikl.id) == -3) {
            return false;
        }
        return true;
    }
}
