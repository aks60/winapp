package builder.making;

import dataset.Record;
import domain.eArtikl;
import domain.eFurndet;
import domain.eFurniture;
import domain.eFurnside1;
import domain.eFurnside2;
import domain.eSysfurn;
import enums.Layout;
import java.util.LinkedList;
import java.util.List;
import builder.Wincalc;
import builder.model.AreaSimple;
import builder.param.FurnitureDet;
import builder.param.FurnitureVar;
import builder.model.AreaStvorka;
import builder.model.ElemFrame;
import common.UCom;
import dataset.Query;
import enums.Type;
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
        this(iwin, false);
    }

    public Furniture(Wincalc iwin, boolean shortPass) {
        super(iwin);
        furnitureVar = new FurnitureVar(iwin);
        furnitureDet = new FurnitureDet(iwin);
        this.shortPass = shortPass;
    }

    public void calc() {
        super.calc();
        LinkedList<AreaStvorka> stvorkaList = rootArea().listElem(Type.STVORKA);
        try {
            //Цикл по створкам      
            for (AreaStvorka areaStv : stvorkaList) {

                //Если ручка выбрана вручную
                if (areaStv.handleRec.getInt(eArtikl.id) != -3) {
                    ElemFrame sideStv = determOfSide(areaStv);
                    Specific spcAdd = new Specific(null, areaStv.handleRec, sideStv, new HashMap());
                    spcAdd.colorID1 = areaStv.handleColor;
                    spcAdd.place = "ФУРН";
                    sideStv.addSpecific(spcAdd); //добавим спецификацию в элемент                    
                }

                setFurndet.clear();
                //Подбор фурнитуры по параметрам
                List<Record> sysfurnList = eSysfurn.find(iwin().nuni);
                Record sysfurnRec = sysfurnList.get(0); //значение по умолчанию, первая SYSFURN в ветке
                //Теперь найдём sysfurnRec если есть параметр фурнитуры 
                sysfurnRec = sysfurnList.stream().filter(rec -> rec.getInt(eSysfurn.id) == areaStv.sysfurnRec.getInt(eSysfurn.id)).findFirst().orElse(sysfurnRec);
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

            //Цикл по описанию сторон фурнитуры
            for (Record furnside1Rec : furnside1List) {
                ElemFrame elemFrame = areaStv.mapFrame.get((Layout) Layout.ANY.find(furnside1Rec.getInt(eFurnside1.side_num)));

                //ФИЛЬТР вариантов с учётом стороны
                if (furnitureVar.filter(elemFrame, furnside1Rec) == false) {
                    return;
                }
                listVariants.add(furnitureRec.getInt(eFurniture.id)); //сделано для запуска формы Furniture на ветке Systree
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

    protected boolean detail(AreaStvorka areaStv, Record furndetRec, int countKit) {
        try {
            Record artiklRec = eArtikl.find(furndetRec.getInt(eFurndet.artikl_id), false);

            //Сделано для убыстрения поиска ручки при конструировании окна. Если ручки нет (eArtikl.level1) != 2) то сразу выход.
            if (shortPass == true) {
                if (furndetRec.getInt(eFurndet.furndet_id) == furndetRec.getInt(eFurndet.id)
                        && furndetRec.get(eFurndet.furniture_id2) == null) {
                    if (artiklRec.getInt(eArtikl.level1) != 2) { //т.к. ручки на уровне 2
                        return false;
                    }
                }
            }

            //Если ручка выбрана вручную, не ищем её
            if (artiklRec.getInt(eArtikl.level1) == 2 && artiklRec.getInt(eArtikl.level2) == 11) {
                if (areaStv.handleRec.getInt(eArtikl.id) != -3) {
                    return false;
                }
            }
            HashMap<Integer, String> mapParam = new HashMap(); //тут накапливаются параметры element и specific

            //ФИЛЬТР детализации            
            furnitureDet.detailRec = furndetRec; //для тестирования
            if (furnitureDet.filter(mapParam, areaStv, furndetRec) == false) {
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
                    el = areaStv.mapFrame.get(Layout.BOTT);
                    float size_falz = (el.artiklRec.getFloat(eArtikl.size_falz) == 0) ? 21 : el.artiklRec.getFloat(eArtikl.size_falz);
                    width = el.spcRec.width - 2 * size_falz;
                } else if (side == 2) {
                    el = areaStv.mapFrame.get(Layout.RIGHT);
                    float size_falz = (el.artiklRec.getFloat(eArtikl.size_falz) == 0) ? 21 : el.artiklRec.getFloat(eArtikl.size_falz);
                    width = el.spcRec.width - 2 * size_falz;
                } else if (side == 3) {
                    el = areaStv.mapFrame.get(Layout.TOP);
                    float size_falz = (el.artiklRec.getFloat(eArtikl.size_falz) == 0) ? 21 : el.artiklRec.getFloat(eArtikl.size_falz);
                    width = el.spcRec.width - 2 * size_falz;
                } else if (side == 4) {
                    el = areaStv.mapFrame.get(Layout.LEFT);
                    float size_falz = (el.artiklRec.getFloat(eArtikl.size_falz) == 0) ? 21 : el.artiklRec.getFloat(eArtikl.size_falz);
                    width = el.spcRec.width - 2 * size_falz;
                }
                if (furnside2Rec.getFloat(eFurnside2.len_max) < width || (furnside2Rec.getFloat(eFurnside2.len_min) > width)) {
                    return false;
                }
            }

            //Если это элемент из мат. ценности (не набор)
            if (furndetRec.get(eFurndet.furniture_id2) == null) {
                if (artiklRec.getInt(eArtikl.id) != -1 && artiklRec.getStr(eArtikl.code).charAt(0) != '@') {

                    ElemFrame sideStv = determOfSide(mapParam, areaStv);
                    Specific spcAdd = new Specific(furndetRec, artiklRec, sideStv, mapParam);

                    //Пишем ручку в створку
                    if (artiklRec.getInt(eArtikl.level1) == 2 && (artiklRec.getInt(eArtikl.level2) == 11 || artiklRec.getInt(eArtikl.level2) == 13)) {                                                       
                        
                        if (areaStv.handleRec.getInt(eArtikl.id) == -3) {          
                            areaStv.handleRec = artiklRec;
                        } else {                          
                            spcAdd.setArtiklRec(areaStv.artiklRecAn); //если ручка выбрана через параметр
                        }
                        if (areaStv.handleColor == -3) {
                            UColor.colorFromProduct(spcAdd, 1);
                            areaStv.handleColor = spcAdd.colorID1;
                        } else {
                            spcAdd.setColor(1, areaStv.handleColor); //если цвет ручки выбран через параметр
                        }              
                    } else {
                        UColor.colorFromProduct(spcAdd, 1); //попадает или нет в спецификацию по цвету
                    }
                    spcAdd.count = UCom.getFloat(spcAdd.getParam(spcAdd.count, 24030));
                    spcAdd.count = spcAdd.count * countKit; //умножаю на количество комплектов
                    spcAdd.place = "ФУРН";
                    sideStv.addSpecific(spcAdd); //добавим спецификацию в элемент
                }

                //Если это нобор   
            } else {
                int countKi2 = (mapParam.get(24030) == null) ? 1 : Integer.valueOf((mapParam.get(24030)));
                Record furnitureRec2 = eFurniture.find(furndetRec.getInt(eFurndet.furniture_id2));
                try {
                    middle(areaStv, furnitureRec2, countKi2); //рекурсия обработки наборов
                } catch (Exception e) {
                    System.err.println("Ошибка:Furniture.middle() " + e);
                }
            }
            return true;
        } catch (Exception e) {
            System.err.println("Furniture.detail() " + e);
            return false;
        }
    }

    public ElemFrame determOfSide(HashMap<Integer, String> mapParam, AreaSimple area5e) {

        //Через параметр
        if ("1".equals(mapParam.get(25010))) {
            return area5e.mapFrame.get(Layout.BOTT);
        } else if ("2".equals(mapParam.get(25010))) {
            return area5e.mapFrame.get(Layout.RIGHT);
        } else if ("3".equals(mapParam.get(25010))) {
            return area5e.mapFrame.get(Layout.TOP);
        } else if ("4".equals(mapParam.get(25010))) {
            return area5e.mapFrame.get(Layout.LEFT);
        } else {
            //Там где крепится ручка
            return determOfSide(area5e);
        }
    }

    //Там где крепится ручка
    public static ElemFrame determOfSide(AreaSimple area5e) {
        if (area5e instanceof AreaStvorka) {
            int id = ((AreaStvorka) area5e).typeOpen.id;
            if (Arrays.asList(1, 3, 11).contains(id)) {
                return area5e.mapFrame.get(Layout.LEFT);
            } else if (Arrays.asList(2, 4, 12).contains(id)) {
                return area5e.mapFrame.get(Layout.RIGHT);
            } else {
                return area5e.mapFrame.get(Layout.BOTT);
            }
        }
        return area5e.mapFrame.values().stream().findFirst().get();  //первая попавшаяся        
    }
}
