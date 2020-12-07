package estimate.constr;

import dataset.Record;
import domain.eArtdet;
import domain.eArtikl;
import domain.eColor;
import estimate.model.Com5t;
import estimate.model.ElemSimple;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Color {

    private static final int TYPES = 2;
    private static final int COLOR_FK = 3;
    private static final int ARTIKL_ID = 4;
  
    public static Map colorFromProduct(ElemSimple elem5e, Record artiklRec, Record detailRec) {  //см. http://help.profsegment.ru/?id=1107        
        int colorFk = detailRec.getInt(COLOR_FK);
        Map<Integer, Integer> mapRet = new HashMap();

        //Цыкл по сторонам текстур элемента
        for (int side = 1; side < 4; ++side) {
            try {
                int colorID = colorFromTypes(elem5e, side, detailRec); //цвет из варианта подбора  
                
                if (colorFk == 0) { //автоподбор текстуры
                    int color_id = colorFromArtdet(artiklRec, side, colorID);
                    if (color_id != -1) {
                        mapRet.put(side, color_id);
                        //spc.setColor(side, color_id);

                    } else { //если неудача подбора то первая в списке запись цвета
                        Record artdetRec = eArtdet.find2(detailRec.getInt(ARTIKL_ID));
                        if (artdetRec != null) {
                            int colorFK2 = artdetRec.getInt(eArtdet.color_fk);
                            if (colorFK2 > 0) { //если это не группа цветов                               
                                mapRet.put(side, colorFK2);
                                //spc.setColor(side, colorFK2);

                            } else if (colorFK2 < 0 && colorFK2 != -1) { //это группа
                                List<Record> colorList = eColor.find2(colorFK2 * -1);
                                if (colorList.isEmpty() == false) {                                    
                                    mapRet.put(side, colorList.get(0).getInt(eColor.id));
                                    //spc.setColor(side, colorList.get(0).getInt(eColor.id));
                                }
                            }
                        }
                    }
                } else if (colorFk == 100000) { //точный подбор 
                    int colorId = colorFromArtdet(artiklRec, side, colorID);
                    if (colorId != -1) {
                        mapRet.put(side, colorId);
                        //spc.setColor(side, colorId);
                    } else {
                        //TODO в спецификпцию не попадёт. См. HELP.
                        return null;
                    }

                } else if (colorFk > 0 && colorFk != 100000) { //указана
                    int color_id = colorFromArtdet(artiklRec, side, colorID);
                    if (color_id != -1) {
                        mapRet.put(side, colorID);
                        //spc.setColor(side, colorID);
                    } else {  //дордом профстроя
                        List<Record> artdetList = eArtdet.find(detailRec.getInt(ARTIKL_ID));
                        for (Record record : artdetList) {
                            if (record.getInt(eArtdet.color_fk) >= 0) {
                                mapRet.put(side, record.getInt(eArtdet.color_fk));
                                //spc.setColor(side, record.getInt(eArtdet.color_fk));
                                break;
                            }
                        }
                    }

                } else if (colorFk < 0) { //текстура задана через параметр
                    int colorFK2 = colorFromParam();
                    mapRet.put(side, colorFK2);
                    //spc.setColor(side, colorFK2);
                }
            } catch (Exception e) {
                System.err.println("Ошибка:Color.setting() " + e);
            }
        }
        return mapRet;
    }

    //Поиск текстуры в Artdet
    private static int colorFromArtdet(Record artiklRec, int side, int colorID) {
        try {
            List<Record> artdetList = eArtdet.find(artiklRec.getInt(eArtikl.id));
            //Цыкл по ARTDET определённого артикула
            for (Record artdetRec : artdetList) {

                if (artdetRec.getInt(eArtdet.color_fk) < 0) { //группа текстур

                    List<Record> colorList = eColor.find2(artdetRec.getInt(eArtdet.color_fk) * -1);
                    for (Record colorRec : colorList) {
                        if (colorRec.getInt(eColor.id) == colorID) {
                            return colorID;
                        }
                    }
                } else if (artdetRec.getInt(eArtdet.color_fk) == colorID) { //если есть такая текстура в ARTDET
                    //Сторона подлежит рассмотрению?
                    if ((side == 1 && "1".equals(artdetRec.getStr(eArtdet.mark_c1)))
                            || (side == 2 && ("1".equals(artdetRec.getStr(eArtdet.mark_c2)) || "1".equals(artdetRec.getStr(eArtdet.mark_c1))))
                            || (side == 3 && ("1".equals(artdetRec.getStr(eArtdet.mark_c3))) || "1".equals(artdetRec.getStr(eArtdet.mark_c1)))) {

                        return colorID;
                    }
                }
            }
            return -1;

        } catch (Exception e) {
            System.err.println("Ошибна Color.colorFromArt() " + e);
            return -1;
        }
    }

    // Выдает цвет из текущего изделия в соответствии с заданным вариантом подбора текстуры
    private static int colorFromTypes(Com5t com5t, int colorSide, Record detailRec) {
        int types = detailRec.getInt(TYPES);
        try {
            int colorType = (colorSide == 1) ? types & 0x0000000f : (colorSide == 2) ? (types & 0x000000f0) >> 4 : (types & 0x00000f00) >> 8;
            switch (colorType) {
                case 0:        //указана вручную
                    return detailRec.getInt(COLOR_FK);
                case 1:        // по основе изделия
                    return com5t.iwin().colorID1;
                case 2:        // по внутр.изделия
                    return com5t.iwin().colorID2;
                case 3:        // по внешн.изделия
                    return com5t.iwin().colorID3;
                case 4:        // по параметру (внутр.)
                    return -1;
                case 6:        // по основе в серии
                    return com5t.iwin().colorID1;
                case 7:        // по внутр. в серии
                    return com5t.iwin().colorID2;
                case 8:        // по внешн. в серии
                    return com5t.iwin().colorID3;
                case 9:        // по параметру (основа)
                    return -1;
                case 11:       // по профилю
                    return ((ElemSimple) com5t).colorElem;
                case 12:       // по параметру (внешн.)    
                    return -1;
                case 15:       // по заполнению
                    return ((ElemSimple) com5t).colorElem;
                default:      // без цвета
                    return com5t.iwin().colorNone;
            }
        } catch (Exception e) {
            System.err.println("Ошибка:Color.colorFromProduct() " + e);
            return -1;
        }
    }
    
    //TODO - Текстура задана через параметр
    private static int colorFromParam() {
//            if (colorCode == root.getIwin().getColorNone()) {
//                for (Parcols parcolsRec : constr.parcolsList) {
//                    if (parcolsRec.pnumb == paramRec.clnum()) {
//                        
//                        int code = Colslst.get3(constr, parcolsRec.ptext).ccode;
//                        if (code != -1) return code;
//                    }
//                }
//            }
//            return colorCode;
        return -1; //иначе виртуальный цвет
    }  
    
    //Текстура профиля или текстура заполнения
    public static int colorFromArtikl(int artiklId) {
        try {
            List<Record> artdetList = eArtdet.find(artiklId);
            //Цыкл по ARTDET определённого артикула
            for (Record artdetRec : artdetList) {
                if (artdetRec.getInt(eArtdet.color_fk) >= 0) {
                    if ("1".equals(artdetRec.getStr(eArtdet.mark_c1))
                            && ("1".equals(artdetRec.getStr(eArtdet.mark_c2)) || "1".equals(artdetRec.getStr(eArtdet.mark_c1)))
                            && ("1".equals(artdetRec.getStr(eArtdet.mark_c3))) || "1".equals(artdetRec.getStr(eArtdet.mark_c1))) {

                        return artdetRec.getInt(eArtdet.color_fk);
                    }
                }
            }
            return -1;

        } catch (Exception e) {
            System.err.println("Ошибна Color.colorFromArt() " + e);
            return -1;
        }
    }    
}
