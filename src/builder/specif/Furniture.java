package builder.specif;

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
import enums.TypeArtikl;
import enums.TypeElem;
import java.util.LinkedList;
import java.util.List;
import builder.Wincalc;
import builder.param.FurnitureDet;
import builder.param.FurnitureVar;
import builder.model.AreaStvorka;
import builder.model.ElemFrame;
import builder.param.Processing;
import dataset.Query;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Фурнитура
 */
public class Furniture extends Cal5e {

    private FurnitureVar furnitureVar = null;
    private FurnitureDet furnitureDet = null;
    private HashSet<Record> setFurndet = new HashSet();
    public boolean handle = false;

    public Furniture(Wincalc iwin) {
        this(iwin, false);
    }

    public Furniture(Wincalc iwin, boolean handle) {
        super(iwin);
        furnitureVar = new FurnitureVar(iwin);
        furnitureDet = new FurnitureDet(iwin);
        this.handle = handle;
    }

    public void calc() {
        super.calc();
        LinkedList<AreaStvorka> stvorkaList = root().listElem(TypeElem.STVORKA);
        try {
            //Цикл по створкам      
            for (AreaStvorka areaStv : stvorkaList) {

                setFurndet.clear();
                //Подбор фурнитуры по параметрам
                List<Record> sysfurnList = eSysfurn.find(iwin().nuni);
                Record sysfurnRec = sysfurnList.get(0);
                sysfurnRec = sysfurnList.stream().filter(rec -> rec.getInt(eSysfurn.id) == areaStv.sysfurnRec.getInt(eSysfurn.id)).findFirst().orElse(sysfurnRec);//теперь sysfurnRec соответствует параметру полученному из i-okna                                                             
                Record furnityreRec = eFurniture.find(sysfurnRec.getInt(eSysfurn.furniture_id));

                middle(areaStv, furnityreRec, 1); //основная фурнитура
            }
        } catch (Exception e) {
            System.err.println("Ошибка:Furniture.calc() " + e);
        } finally {
            Query.conf = conf;
        }
    }

    protected void middle(AreaStvorka areaStv, Record furnitureRec, int count) {
        try {
            List<Record> furndetList = eFurndet.find(furnitureRec.getInt(eFurniture.id));
            List<Record> furnside1List = eFurnside1.find(furnitureRec.getInt(eFurniture.id));
            listVariants.add(furnitureRec.getInt(eFurniture.id)); //сделано для запуска формы Furniture на ветке Systree

            //Цикл по описанию сторон фурнитуры
            for (Record furnside1Rec : furnside1List) {
                ElemFrame sideFrame = areaStv.mapFrame.get((LayoutArea) LayoutArea.ANY.find(furnside1Rec.getInt(eFurnside1.side_num)));

                //ФИЛЬТР вариантов
                if (furnitureVar.check(sideFrame, furnside1Rec) == false) {
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
            System.err.println("Ошибка:Furniture.middle() " + e);
        }
    }

    protected boolean detail(AreaStvorka areaStv, Record furndetRec, int count) {
        try {
            Record artiklRec = eArtikl.find(furndetRec.getInt(eFurndet.artikl_id), false);
            if (handle == true) { //если пишем ручку в створку то всё остальное отсеиваем
                if (furndetRec.getInt(eFurndet.furndet_id) == furndetRec.getInt(eFurndet.id)) {
                    if (artiklRec.getInt(eArtikl.level1) != 2) {
                        return false;
                    }
                }
            }
            HashMap<Integer, String> mapParam = new HashMap(); //тут накапливаются параметры element и specific
            Record furnitureRec = eFurniture.find(furndetRec.getInt(eFurndet.furniture_id1));

            //Подбор текстуры ручки
            if (furndetRec.get(eFurndet.furniture_id2) == null) {
                if (artiklRec != null && TypeArtikl.FURNRUCHKA.isType(artiklRec)) {
                    if (furndetRec.getInt(eFurndet.color_fk) > 0) {
                        boolean empty = true;
                        List<Record> artdetList = eArtdet.find(furndetRec.getInt(eFurndet.artikl_id));
                        for (Record artdetRec : artdetList) {
                            if (artdetRec.getInt(eArtdet.color_fk) == areaStv.handlColor) {
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
            furnitureDet.detailRec = furndetRec; //для тестирования
            if (furnitureDet.check(mapParam, areaStv, furndetRec) == false) {
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
                    el = areaStv.mapFrame.get(LayoutArea.BOTTOM);
                    float size_falz = (el.artiklRec.getFloat(eArtikl.size_falz) == 0) ? 21 : el.artiklRec.getFloat(eArtikl.size_falz);
                    width = el.specificationRec.width - 2 * size_falz;
                } else if (side == 2) {
                    el = areaStv.mapFrame.get(LayoutArea.RIGHT);
                    float size_falz = (el.artiklRec.getFloat(eArtikl.size_falz) == 0) ? 21 : el.artiklRec.getFloat(eArtikl.size_falz);
                    width = el.specificationRec.width - 2 * size_falz;
                } else if (side == 3) {
                    el = areaStv.mapFrame.get(LayoutArea.TOP);
                    float size_falz = (el.artiklRec.getFloat(eArtikl.size_falz) == 0) ? 21 : el.artiklRec.getFloat(eArtikl.size_falz);
                    width = el.specificationRec.width - 2 * size_falz;
                } else if (side == 4) {
                    el = areaStv.mapFrame.get(LayoutArea.LEFT);
                    float size_falz = (el.artiklRec.getFloat(eArtikl.size_falz) == 0) ? 21 : el.artiklRec.getFloat(eArtikl.size_falz);
                    width = el.specificationRec.width - 2 * size_falz;
                }
                if (furnside2Rec.getFloat(eFurnside2.len_max) < width || (furnside2Rec.getFloat(eFurnside2.len_min) > width)) {
                    return false;
                }
            }

            //Если это элемент из мат. ценности (не набор)
            if (furndetRec.get(eFurndet.furniture_id2) == null) {                
                if (artiklRec.getInt(eArtikl.id) != -1 && artiklRec.getStr(eArtikl.code).charAt(0) != '@') {

                    ElemFrame sideStv = Processing.determOfSide(mapParam, areaStv);
                    Specification specif = new Specification(furndetRec, artiklRec, sideStv, mapParam);
                    if (Color.colorFromProduct(specif, 1)) { //попадает или нет в спецификацию по цвету

                        //Пишем ручку в створку
                        if (handle == true && artiklRec.getInt(eArtikl.level1) == 2 && (artiklRec.getInt(eArtikl.level2) == 11 || artiklRec.getInt(eArtikl.level2) == 13)) {
                            if (artiklRec.getStr(eArtikl.name).toLowerCase().contains("ручк")) {

                                areaStv.handlRec = artiklRec;
                                areaStv.handlColor = specif.colorID1;
                            }
                        }
                        specif.count = Integer.valueOf(specif.getParam(specif.count, 24030));
                        specif.count = specif.count * count;
                        specif.place = "ФУРН";
                        sideStv.addSpecific(specif); //добавим спецификацию в элемент
                    }
                }

                //Если это нобор   
            } else {
                int count2 = (mapParam.get(24030) == null) ? 1 : Integer.valueOf((mapParam.get(24030)));
                Record furnitureRec2 = eFurniture.find(furndetRec.getInt(eFurndet.furniture_id2));
                try {
                    middle(areaStv, furnitureRec2, count2); //рекурсия обработки наборов
                } catch (Exception e) {
                    System.err.println("Ошибка:Furniture.middle() " + e);
                }
            }
            return true;
        } catch (Exception e) {
            System.err.println("estimate.constr.Furniture.detail() " + e);
            return false;
        }
    }
}
