package estimate.constr;

import dataset.Record;
import domain.eArtdet;
import domain.eArtikl;
import domain.eFurndet;
import domain.eFurniture;
import domain.eFurnpar1;
import domain.eFurnpar2;
import domain.eFurnside1;
import domain.eFurnside2;
import domain.eSysfurn;
import enums.LayoutArea;
import enums.LayoutFurn1;
import enums.LayoutHandle;
import enums.ParamJson;
import enums.TypeArtikl;
import enums.TypeElem;
import java.util.LinkedList;
import java.util.List;
import estimate.Wincalc;
import estimate.constr.param.FurnitureDet;
import estimate.constr.param.FurnitureVar;
import estimate.model.AreaStvorka;
import estimate.model.ElemFrame;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Фурнитура
 */
public class Furniture extends Cal5e {

    private FurnitureVar furnitureVar = null;
    private FurnitureDet furnitureDet = null;
    private HashSet<Record> setFurndet = new HashSet();

    public Furniture(Wincalc iwin) {
        super(iwin);
        furnitureVar = new FurnitureVar(iwin);
        furnitureDet = new FurnitureDet(iwin);
    }

    public void calc() {
        listVariants.clear();
        LinkedList<AreaStvorka> stvorkaList = root().listElem(TypeElem.STVORKA);
        try {
            //Цикл по створкам      
            for (AreaStvorka areaStvorka : stvorkaList) {
                setFurndet.clear();
                //Подбор фурнитуры по параметрам
                List<Record> sysfurnList = eSysfurn.find(iwin().nuni);
                Record sysfurnRec = sysfurnList.get(0);
                sysfurnRec = sysfurnList.stream().filter(rec -> rec.getInt(eSysfurn.id) == areaStvorka.sysfurnID).findFirst().orElse(sysfurnRec);//теперь sysfurnRec соответствует параметру полученному из i-okna             
                Record furnityreRec = eFurniture.find(sysfurnRec.getInt(eSysfurn.furniture_id));
                //Сторона открывания
                areaStvorka.handleSide = Arrays.stream(LayoutFurn1.values()).filter(el -> el.id == furnityreRec.getInt(eFurniture.hand_side)).findFirst().get();
                //Подбор текстуры ручки створки
                if (areaStvorka.handleColor == -1) { //если цвет не установлен подбираю по основной текстуре
                    areaStvorka.handleColor = iwin().colorID1;
                }
                //Ручка по умолчанию
                if (sysfurnRec.getInt(eSysfurn.hand_pos) == LayoutHandle.MIDDL.id) {
                    areaStvorka.handleHeight = LayoutHandle.MIDDL.name;
                } else if (sysfurnRec.getInt(eSysfurn.hand_pos) == LayoutHandle.CONST.id) {
                    areaStvorka.handleHeight = LayoutHandle.CONST.name;
                } else if (sysfurnRec.getStr(eSysfurn.hand_pos).equalsIgnoreCase("вариационная")) {
                    areaStvorka.handleHeight = "установлена";
                }

                middle(areaStvorka, furnityreRec, 1); //основная фурнитура

            }
        } catch (Exception e) {
            System.err.println("estimate.constr.Furniture.calc() " + e);
        }
    }

    protected void middle(AreaStvorka areaStv, Record furnitureRec, int count) {
        try {
            List<Record> furndetList = eFurndet.find(furnitureRec.getInt(eFurniture.id));
            List<Record> furnside1List = eFurnside1.find(furnitureRec.getInt(eFurniture.id));
            listVariants.add(furnitureRec.getInt(eFurniture.id)); //сделано для запуска формы Furniture на ветке Systree

            //Цикл по описанию сторон фурнитуры
            for (Record furnside1Rec : furnside1List) {
                List<Record> furnpar1List = eFurnpar1.find(furnside1Rec.getInt(eFurnside1.id));
                ElemFrame sideFrame = areaStv.mapFrame.get((LayoutArea) LayoutArea.ANY.find(furnside1Rec.getInt(eFurnside1.side_num)));

                //ФИЛЬТР вариантов
                if (furnitureVar.check(sideFrame, furnpar1List) == false) {
                    return;
                }
            }
            //Цикл по детализации (уровень 1)        
            for (Record furndetRec1 : furndetList) {
                if (furndetRec1.getInt(eFurndet.furndet_id) == furndetRec1.getInt(eFurndet.id)) {
                    if (detail(areaStv, furndetRec1, count) == true) {

                        //Цикл по детализации (уровень 2)
                        for (Record furndetRec2 : furndetList) {
                            if (furndetRec2.getInt(eFurndet.furndet_id) == furndetRec1.getInt(eFurndet.id)
                                    && furndetRec2.getInt(eFurndet.furndet_id) != furndetRec2.getInt(eFurndet.id)) {
                                if (detail(areaStv, furndetRec2, count) == true) {

                                    //Цикл по детализации (уровень 3)
                                    for (Record furndetRec3 : furndetList) {
                                        if (furndetRec3.getInt(eFurndet.furndet_id) == furndetRec2.getInt(eFurndet.id)
                                                && furndetRec3.getInt(eFurndet.furndet_id) != furndetRec3.getInt(eFurndet.id)) {
                                            detail(areaStv, furndetRec3, count);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("estimate.constr.Furniture.middle() " + e);
        }
    }

    protected boolean detail(AreaStvorka areaStvorka, Record furndetRec, int count) {
        try {
            HashMap<Integer, String> mapParam = new HashMap(); //тут накапливаются параметры element и specific
            Record furnitureRec = eFurniture.find(furndetRec.getInt(eFurndet.furniture_id1));
            ElemFrame handlFrame = areaStvorka.mapFrame.get((LayoutArea) LayoutArea.ANY.find(furnitureRec.getInt(eFurniture.hand_side))); //Крепится ручка

            //Подбор текстуры ручки
            if (furndetRec.get(eFurndet.furniture_id2) == null) {
                Record artiklRec = eArtikl.find(furndetRec.getInt(eFurndet.artikl_id), false);
                if (artiklRec != null && TypeArtikl.FURNRUCHKA.isType(artiklRec)) {
                    if (furndetRec.getInt(eFurndet.color_fk) > 0) {
                        boolean empty = true;
                        List<Record> artdetList = eArtdet.find(furndetRec.getInt(eFurndet.artikl_id));
                        for (Record artdetRec : artdetList) {
                            if (artdetRec.getInt(eArtdet.color_fk) == areaStvorka.handleColor) {
                                empty = false;
                            }
                        }
                        if (empty == true) {
                            return false;
                        }
                    }
                }
            }
            //ФИЛЬТР детализации
            List<Record> furnpar2List = eFurnpar2.find(furndetRec.getInt(eFurndet.id));
            if (furnitureDet.check(mapParam, handlFrame, furnpar2List) == false) {
                return false; //параметры детализации
            }
            List<Record> furnside2List = eFurnside2.find(furndetRec.getInt(eFurndet.id));

            //Цикл по ограничению сторон фурнитуры
            for (Record furnside2Rec : furnside2List) {
                ElemFrame el = null;
                float width = 0;
                int side = furnside2Rec.getInt(eFurnside2.side_num);

                if (side < 0) {
                    String txt = (furnitureDet.mapParamTmp.get(24038) == null) ? furnitureDet.mapParamTmp.get(25038) : furnitureDet.mapParamTmp.get(24038);
                    String[] par = txt.split("/");
                    if (side == -1) {
                        side = (par[0].equals("*") == true) ? 99 : Integer.valueOf(par[0]);
                    } else if (side == -2) {
                        side = (par[1].equals("*") == true) ? 99 : Integer.valueOf(par[1]);
                    }
                }
                if (side == 1) {
                    el = areaStvorka.mapFrame.get(LayoutArea.BOTTOM);
                    float size_falz = (el.artiklRec.getFloat(eArtikl.size_falz) == 0) ? 21 : el.artiklRec.getFloat(eArtikl.size_falz);
                    width = el.specificationRec.width - 2 * size_falz;
                } else if (side == 2) {
                    el = areaStvorka.mapFrame.get(LayoutArea.RIGHT);
                    float size_falz = (el.artiklRec.getFloat(eArtikl.size_falz) == 0) ? 21 : el.artiklRec.getFloat(eArtikl.size_falz);
                    width = el.specificationRec.width - 2 * size_falz;
                } else if (side == 3) {
                    el = areaStvorka.mapFrame.get(LayoutArea.TOP);
                    float size_falz = (el.artiklRec.getFloat(eArtikl.size_falz) == 0) ? 21 : el.artiklRec.getFloat(eArtikl.size_falz);
                    width = el.specificationRec.width - 2 * size_falz;
                } else if (side == 4) {
                    el = areaStvorka.mapFrame.get(LayoutArea.LEFT);
                    float size_falz = (el.artiklRec.getFloat(eArtikl.size_falz) == 0) ? 21 : el.artiklRec.getFloat(eArtikl.size_falz);
                    width = el.specificationRec.width - 2 * size_falz;
                }
                if (furnside2Rec.getFloat(eFurnside2.len_max) < width || (furnside2Rec.getFloat(eFurnside2.len_min) > width)) {
                    return false;
                }
            }

            //Если это элемент из мат. ценность (не набор)
            if (furndetRec.get(eFurndet.furniture_id2) == null) {
                Record artiklRec = eArtikl.find(furndetRec.getInt(eFurndet.artikl_id), false);
                if (artiklRec.getInt(eArtikl.id) != -1 && artiklRec.getStr(eArtikl.code).charAt(0) != '@') {

                    Specification specif = new Specification(furndetRec, artiklRec, handlFrame, mapParam);
                    specif.count = Integer.valueOf(specif.getParam(specif.count, 24030));
                    specif.count = specif.count * count;
                    Color.setting(specif, furndetRec);
                    specif.place = "ФУРН";
                    handlFrame.addSpecific(specif); //добавим спецификацию в элемент
                }

                //Если это нобор   
            } else {
                int count2 = (mapParam.get(24030) == null) ? 1 : Integer.valueOf((mapParam.get(24030)));
                Record furnitureRec2 = eFurniture.find(furndetRec.getInt(eFurndet.furniture_id2));
                try {
                    middle(areaStvorka, furnitureRec2, count2); //рекурсия обработки наборов
                } catch (Exception e) {
                    System.err.println("Ошибка recursion, estimate.constr.Furniture.middle() " + e);
                }
            }
            return true;
        } catch (Exception e) {
            System.err.println("estimate.constr.Furniture.detail() " + e);
            return false;
        }
    }
}
