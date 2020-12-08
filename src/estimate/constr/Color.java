package estimate.constr;

import dataset.Record;
import domain.eArtdet;
import domain.eArtikl;
import domain.eColor;
import enums.UseColor;
import estimate.model.Com5t;
import estimate.model.ElemSimple;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

public class Color {

    private static final int TYPES = 2;
    private static final int COLOR_FK = 3;
    private static final int ARTIKL_ID = 4;

    public static Map colorFromProduct(ElemSimple elem5e, Record artiklRec, Record detailRec) {  //см. http://help.profsegment.ru/?id=1107        
        int colorFk = detailRec.getInt(COLOR_FK);
        int types = detailRec.getInt(TYPES);
        Map<Integer, Integer> returnMap = new HashMap();

        //Цыкл по сторонам текстур элемента
        for (int side = 1; side < 4; ++side) {
            try {
                int colorFromTypesID = colorFromTypes(elem5e, side, detailRec); //цвет из варианта подбора 
                int colorType = (side == 1) ? types & 0x0000000f : (side == 2) ? (types & 0x000000f0) >> 4 : (types & 0x00000f00) >> 8;
                int colorFromArtdetID = -1;

                if (colorType == UseColor.C1SER.id || colorType == UseColor.C2SER.id || colorType == UseColor.C3SER.id) { //поиск в серии
                    //colorFromArtdetID = colorFromSeridet(artiklRec.getInt(eArtikl.id), side, colorFromTypesID);
                    JOptionPane.showMessageDialog(null, "Подбор текстуры по серии не реализован", "ВНИМАНИЕ!", 1);
                } else if (colorType == UseColor.C1PAR.id || colorType == UseColor.C2PAR.id || colorType == UseColor.C3PAR.id) { //поиск в параметре
                    colorFromArtdetID = colorFromParam();
                } else { //всё остальное
                    colorFromArtdetID = colorFromArtdet(artiklRec.getInt(eArtikl.id), side, colorFromTypesID);
                }

                //Автоподбор текстуры
                if (colorFk == 0) {
                    if (colorFromArtdetID != -1) {
                        returnMap.put(side, colorFromArtdetID);

                    } else { //если неудача подбора то первая в списке запись цвета
                        Record artdetRec = eArtdet.find2(detailRec.getInt(ARTIKL_ID));
                        if (artdetRec != null) {
                            int colorFK2 = artdetRec.getInt(eArtdet.color_fk);
                            if (colorFK2 > 0) { //если это не группа цветов                               
                                returnMap.put(side, colorFK2);

                            } else if (colorFK2 < 0 && colorFK2 != -1) { //это группа
                                List<Record> colorList = eColor.find2(colorFK2 * -1);
                                if (colorList.isEmpty() == false) {
                                    returnMap.put(side, colorList.get(0).getInt(eColor.id));
                                }
                            }
                        }
                    }

                    //Точный подбор
                } else if (colorFk == 100000) {
                    if (colorFromArtdetID != -1) {
                        returnMap.put(side, colorFromArtdetID);

                    } else {
                        //В спецификпцию не попадёт. См. HELP "Конструктив=>Подбор текстур"
                        return null;
                    }

                    //Указана вручную
                } else if (colorFk > 0 && colorFk != 100000) {
                    if (colorFromArtdetID != -1) {
                        returnMap.put(side, colorFromTypesID);

                    } else {  //дордом профстроя
                        List<Record> artdetList = eArtdet.find(detailRec.getInt(ARTIKL_ID));
                        for (Record record : artdetList) {
                            if (record.getInt(eArtdet.color_fk) >= 0) {
                                returnMap.put(side, record.getInt(eArtdet.color_fk));
                                break;
                            }
                        }
                    }

                    //Текстура задана через параметр
                } else if (colorFk < 0) {
                    int colorFK2 = colorFromParam();
                    returnMap.put(side, colorFK2);
                }

            } catch (Exception e) {
                System.err.println("Ошибка:Color.setting() " + e);
            }
        }
        return returnMap;
    }

    //Поиск текстуры в Artdet
    private static int colorFromArtdet(int artiklID, int side, int colorID) {
        try {
            List<Record> artdetList = eArtdet.find(artiklID);
            //Цыкл по ARTDET определённого артикула
            for (Record artdetRec : artdetList) {
                //Сторона подлежит рассмотрению?
                if ((side == 1 && "1".equals(artdetRec.getStr(eArtdet.mark_c1)))
                        || (side == 2 && ("1".equals(artdetRec.getStr(eArtdet.mark_c2)) || "1".equals(artdetRec.getStr(eArtdet.mark_c1))))
                        || (side == 3 && ("1".equals(artdetRec.getStr(eArtdet.mark_c3))) || "1".equals(artdetRec.getStr(eArtdet.mark_c1)))) {

                    //Группа текстур
                    if (artdetRec.getInt(eArtdet.color_fk) < 0) {
                        List<Record> colorList = eColor.find2(artdetRec.getInt(eArtdet.color_fk) * -1); //фильтр списка определённой группы
                        //Цыкл по COLOR определённой группы
                        for (Record colorRec : colorList) {
                            if (colorRec.getInt(eColor.id) == colorID) {
                                return colorID;
                            }
                        }

                        //Одна текстура
                    } else if (artdetRec.getInt(eArtdet.color_fk) == colorID) { //если есть такая текстура в ARTDET
                        return colorID;
                    }
                }
            }
            return -1;

        } catch (Exception e) {
            System.err.println("Ошибка Color.colorFromArtdet() " + e);
            return -1;
        }
    }

    //Поиск текстуры в серии Artdet
    private static int colorFromSeridet(int seriesID, int side, int colorID) {
        try {
            for (Record artiklRec : eArtikl.find3(seriesID)) {
                List<Record> artdetList = eArtdet.find2(artiklRec.getInt(eArtikl.id));

                //Цыкл по ARTDET определённого артикула
                for (Record artdetRec : artdetList) {
                    //Сторона подлежит рассмотрению?
                    if ((side == 1 && "1".equals(artdetRec.getStr(eArtdet.mark_c1)))
                            || (side == 2 && ("1".equals(artdetRec.getStr(eArtdet.mark_c2)) || "1".equals(artdetRec.getStr(eArtdet.mark_c1))))
                            || (side == 3 && ("1".equals(artdetRec.getStr(eArtdet.mark_c3))) || "1".equals(artdetRec.getStr(eArtdet.mark_c1)))) {

                        //Группа текстур
                        if (artdetRec.getInt(eArtdet.color_fk) < 0) {
                            List<Record> colorList = eColor.find2(artdetRec.getInt(eArtdet.color_fk) * -1); //фильтр списка определённой группы
                            //Цыкл по COLOR определённой группы
                            for (Record colorRec : colorList) {
                                if (colorRec.getInt(eColor.id) == colorID) {
                                    return colorID;
                                }
                            }

                            //Одна текстура
                        } else if (artdetRec.getInt(eArtdet.color_fk) == colorID) { //если есть такая текстура в ARTDET
                            return colorID;
                        }
                    }
                }
            }
            return -1;

        } catch (Exception e) {
            System.err.println("Ошибка Color.colorFromSeridet() " + e);
            return -1;
        }
    }

    //Поиск текстуры в заданном параметре
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

    // Выдает цвет из текущего изделия в соответствии с заданным вариантом подбора текстуры   
    private static int colorFromTypes(Com5t com5t, int side, Record detailRec) {
        try {
            int types = detailRec.getInt(TYPES);
            int colorType = (side == 1) ? types & 0x0000000f : (side == 2) ? (types & 0x000000f0) >> 4 : (types & 0x00000f00) >> 8;
            switch (colorType) {
                case 0:
                    return detailRec.getInt(COLOR_FK);  //указана вручную
                case 11:
                    return ((ElemSimple) com5t).colorElem; //по профилю 
                case 15:
                    return ((ElemSimple) com5t).colorElem; //по заполнению                   
                case 1:
                    return com5t.iwin().colorID1; //по основе изделия
                case 2:
                    return com5t.iwin().colorID2; //по внутр.изделия
                case 3:
                    return com5t.iwin().colorID3; //по внешн.изделия
                case 6:
                    return com5t.iwin().colorID1; //по основе в серии
                case 7:
                    return com5t.iwin().colorID2; //по внутр. в серии
                case 8:
                    return com5t.iwin().colorID3; //по внешн. в серии
                case 9:
                    return -1;  //по параметру (основа)
                case 4:
                    return -1;  //по параметру (внутр.)                    
                case 12:
                    return -1;  //по параметру (внешн.) 
                default:
                    return com5t.iwin().colorNone; //без цвета
            }
        } catch (Exception e) {
            System.err.println("Ошибка: Color.colorFromTypes() " + e);
            return -1;
        }
    }

    //Текстура профиля или текстура заполнения изделия
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
