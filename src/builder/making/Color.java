package builder.making;

import dataset.Record;
import domain.eArtdet;
import domain.eArtikl;
import domain.eColor;
import domain.eColmap;
import domain.eParams;
import domain.eSetting;
import enums.UseColor;
import java.util.Comparator;
import java.util.List;
import static java.util.stream.Collectors.toList;
import javax.swing.JOptionPane;

public class Color {

    private static final int TYPES = 2;
    private static final int COLOR_FK = 3;
    private static final int ARTIKL_ID = 4;

    public static boolean colorFromProduct(SpecificRec spc, int side) {  //см. http://help.profsegment.ru/?id=1107        

        int colorFk = spc.detailRec.getInt(COLOR_FK);
        int types = spc.detailRec.getInt(TYPES);

        try {
            int artdetColorFK = -1;
            int colorType = (side == 1) ? types & 0x0000000f : (side == 2) ? (types & 0x000000f0) >> 4 : (types & 0x00000f00) >> 8; //тип подбора                
            int elemColorID = colorFromTypes(spc, colorType, side); //цвет из варианта подбора 

            //Указана вручную
            if (colorFk > 0 && colorFk != 100000) {
                if (colorType == UseColor.MANUAL.id) {
                    artdetColorFK = colorFromArtikl(spc.artiklRec.getInt(eArtikl.id), side, colorFk);
                    if (artdetColorFK != -1) {
                        spc.setColor(side, artdetColorFK);

                    } else {
                        if ("ps3".equals(eSetting.find(2))) {
                            return false;
                        }
                        if (spc.artiklRec.getInt(eArtikl.level1) == 2 && (spc.artiklRec.getInt(eArtikl.level2) == 11 || spc.artiklRec.getInt(eArtikl.level2) == 13)) {
                            return false;
                        }
                        spc.setColor(side, colorFromFirst(spc)); //первая в списке
                    }
                } else if (colorType == UseColor.PROF.id || colorType == UseColor.GLAS.id || colorType == UseColor.COL1.id || colorType == UseColor.COL2.id || colorType == UseColor.COL3.id) {
                    artdetColorFK = colorFromArtikl(spc.artiklRec.getInt(eArtikl.id), side, elemColorID);
                    if (artdetColorFK != -1) {
                        spc.setColor(side, artdetColorFK);

                    } else {
                        artdetColorFK = colorFromArtikl(spc.artiklRec.getInt(eArtikl.id), side, colorFk);
                        if (artdetColorFK != -1) {
                            spc.setColor(side, artdetColorFK);
                        } else {
                            spc.setColor(side, colorFromFirst(spc)); //первая в списке запись цвета
                        }
                    }
                } else if (colorType == UseColor.C1SER.id || colorType == UseColor.C2SER.id || colorType == UseColor.C3SER.id) {
                    artdetColorFK = colorFromSeries(spc.artiklRec.getInt(eArtikl.id), side, elemColorID);
                    if (artdetColorFK != -1) {
                        spc.setColor(side, artdetColorFK);

                    } else {
                        artdetColorFK = colorFromArtikl(spc.artiklRec.getInt(eArtikl.id), side, colorFk);
                        if (artdetColorFK != -1) {
                            spc.setColor(side, artdetColorFK);
                        } else {
                            spc.setColor(side, colorFromFirst(spc)); //первая в списке запись цвета
                        }
                    }
                }

                //Автоподбор текстуры
            } else if (colorFk == 0) {
                if (colorType == UseColor.PROF.id || colorType == UseColor.GLAS.id
                        || colorType == UseColor.COL1.id || colorType == UseColor.COL2.id || colorType == UseColor.COL3.id) {
                    artdetColorFK = colorFromArtikl(spc.artiklRec.getInt(eArtikl.id), side, elemColorID);
                    if (artdetColorFK != -1) {
                        spc.setColor(side, artdetColorFK);

                    } else { //если неудача подбора то первая в списке запись цвета
                        spc.setColor(side, colorFromFirst(spc));
                    }
                } else if (colorType == UseColor.C1SER.id || colorType == UseColor.C2SER.id || colorType == UseColor.C3SER.id) {
                    artdetColorFK = colorFromSeries(spc.artiklRec.getInt(eArtikl.series_id), side, elemColorID);
                    if (artdetColorFK != -1) {
                        spc.setColor(side, artdetColorFK);

                    } else { //если неудача подбора то первая в списке запись цвета
                        spc.setColor(side, colorFromFirst(spc));
                    }
                }

                //Точный подбор
            } else if (colorFk == 100000) {
                if (colorType == UseColor.PROF.id || colorType == UseColor.GLAS.id
                        || colorType == UseColor.COL1.id || colorType == UseColor.COL2.id || colorType == UseColor.COL3.id) {
                    artdetColorFK = colorFromArtikl(spc.artiklRec.getInt(eArtikl.id), side, elemColorID);
                    if (artdetColorFK != -1) {
                        spc.setColor(side, artdetColorFK);

                    } else { //В спецификпцию не попадёт. См. HELP "Конструктив=>Подбор текстур" 
                        return false;
                    }
                } else if (colorType == UseColor.C1SER.id || colorType == UseColor.C2SER.id || colorType == UseColor.C3SER.id) {
                    artdetColorFK = colorFromSeries(spc.artiklRec.getInt(eArtikl.series_id), side, elemColorID);
                    if (artdetColorFK != -1) {
                        spc.setColor(side, artdetColorFK);

                    } else { //В спецификпцию не попадёт. См. HELP "Конструктив=>Подбор текстур" 
                        return false;
                    }
                }

                //Текстура задана через параметр
            } else if (colorFk < 0) {
//                if (spc.detailRec.getInt(1) == 148) {
//                    System.out.println("TEST");
//                }
                if (colorType == UseColor.PROF.id || colorType == UseColor.GLAS.id
                        || colorType == UseColor.COL1.id || colorType == UseColor.COL2.id || colorType == UseColor.COL3.id) {

                    artdetColorFK = colorFromArtiklParam(spc.artiklRec.getInt(eArtikl.id), side, elemColorID, colorFk);
                    if (artdetColorFK != -1) {

                        spc.setColor(side, artdetColorFK);
                    } else { //В спецификпцию не попадёт. См. HELP "Конструктив=>Подбор текстур" 
                        return false;
                    }
                } else if (colorType == UseColor.C1SER.id || colorType == UseColor.C2SER.id || colorType == UseColor.C3SER.id) {
                    artdetColorFK = colorFromSeriesParam(spc.artiklRec.getInt(eArtikl.series_id), side, elemColorID, colorFk);
                    if (artdetColorFK != -1) {

                        spc.setColor(side, artdetColorFK);
                    } else { //В спецификпцию не попадёт. См. HELP "Конструктив=>Подбор текстур" 
                        return false;
                    }
                } else {
                    artdetColorFK = colorFromArtiklParam(spc.artiklRec.getInt(eArtikl.series_id), side, elemColorID, colorFk);
                    if (artdetColorFK != -1) {

                        spc.setColor(side, artdetColorFK);
                    } else { //В спецификпцию не попадёт. См. HELP "Конструктив=>Подбор текстур" 
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка:Color.setting() " + e);
        }
        return true;
    }

    //Первая в списке запись цвета
    private static int colorFromFirst(SpecificRec spc) {
        Record artdetRec = eArtdet.find2(spc.detailRec.getInt(ARTIKL_ID));
        if (artdetRec != null) {
            int colorFK2 = artdetRec.getInt(eArtdet.color_fk);
            if (colorFK2 > 0) { //если это не группа цветов                               
                return colorFK2;

            } else if (colorFK2 < 0 && colorFK2 != -1) { //это группа
                List<Record> colorList = eColor.find2(colorFK2 * -1);
                if (colorList.isEmpty() == false) {
                    return colorList.get(0).getInt(eColor.id);
                }
            }
        }
        JOptionPane.showMessageDialog(null, "Для артикуда  " + spc.artikl + " не определена цена.", "ВНИМАНИЕ!", 1);
        return 1; //такого случая не должно быть
    }

    //Первая в списке запись цвета (Не в группе)
    private static int colorFromFirst2(SpecificRec spc) {
        List<Record> artdetList = eArtdet.find(spc.artiklRec.getInt(ARTIKL_ID));
        for (Record record : artdetList) {
            if (record.getInt(eArtdet.color_fk) >= 0) {
                return record.getInt(eArtdet.color_fk);
            }
        }
        return -1;
    }

    //Поиск текстуры в серии артикулов
    private static int colorFromSeries(int seriesID, int side, int elemColorID) {

        List<Record> artseriList = eArtikl.find3(seriesID);
        for (Record artseriRec : artseriList) {

            int color_id1 = colorFromArtikl(artseriRec.getInt(eArtikl.id), side, elemColorID);
            if (color_id1 != -1) {
                return color_id1;
            }
        }
        return -1;
    }

    //Поиск текстуры в серии артикулов по параметру
    private static int colorFromSeriesParam(int seriesID, int side, int elemColorID, int colorFk) {

        List<Record> artseriList = eArtikl.find3(seriesID);
        for (Record artseriRec : artseriList) {

            int color_id1 = colorFromArtiklParam(artseriRec.getInt(eArtikl.id), side, elemColorID, colorFk);
            if (color_id1 != -1) {
                return color_id1;
            }
        }
        return -1;
    }

    //Поиск текстуры в артикуле
    private static int colorFromArtikl(int artiklID, int side, int elemColorID) {
        try {
            List<Record> artdetList = eArtdet.find(artiklID);
            //Цикл по ARTDET определённого артикула
            for (Record artdetRec : artdetList) {
                //Сторона подлежит рассмотрению?
                if ((side == 1 && "1".equals(artdetRec.getStr(eArtdet.mark_c1)))
                        || (side == 2 && ("1".equals(artdetRec.getStr(eArtdet.mark_c2)) || "1".equals(artdetRec.getStr(eArtdet.mark_c1))))
                        || (side == 3 && ("1".equals(artdetRec.getStr(eArtdet.mark_c3))) || "1".equals(artdetRec.getStr(eArtdet.mark_c1)))) {

                    //Группа текстур
                    if (artdetRec.getInt(eArtdet.color_fk) < 0) {
                        List<Record> colorList = eColor.find2(artdetRec.getInt(eArtdet.color_fk) * -1); //фильтр списка определённой группы
                        //Цикл по COLOR определённой группы
                        for (Record colorRec : colorList) {
                            if (colorRec.getInt(eColor.id) == elemColorID) {
                                return elemColorID;
                            }
                        }

                        //Одна текстура
                    } else {
                        if (artdetRec.getInt(eArtdet.color_fk) == elemColorID) { //если есть такая текстура в ARTDET
                            return elemColorID;
                        }
                    }
                }
            }
            return -1;

        } catch (Exception e) {
            System.err.println("Ошибка Color.colorFromArtdet() " + e);
            return -1;
        }
    }

    //Поиск текстуры в артикуле по параметру.
    private static int colorFromArtiklParam(int artiklID, int side, int elemColorID, int paramFk) {
        try {
//            if (artiklID == 84) {
//                System.out.println("TEST");
//            }
            List<Record> colmapList = eColmap.find3(elemColorID, paramFk);
            List<Record> artdetList = eArtdet.find(artiklID);

            //Цикл по ARTDET определённого артикула
            for (Record artdetRec : artdetList) {
                //Сторона подлежит рассмотрению?
                if ((side == 1 && "1".equals(artdetRec.getStr(eArtdet.mark_c1)))
                        || (side == 2 && ("1".equals(artdetRec.getStr(eArtdet.mark_c2)) || "1".equals(artdetRec.getStr(eArtdet.mark_c1))))
                        || (side == 3 && ("1".equals(artdetRec.getStr(eArtdet.mark_c3))) || "1".equals(artdetRec.getStr(eArtdet.mark_c1)))) {

                    //Группа текстур
                    if (artdetRec.getInt(eArtdet.color_fk) < 0) {
                        List<Record> colorList = eColor.find2(artdetRec.getInt(eArtdet.color_fk) * -1); //фильтр списка определённой группы
                        //Цикл по COLOR определённой группы
                        for (Record colorRec : colorList) {

                            List<Record> colmapList2 = eColmap.find(colorRec.getInt(eColor.id));

                            for (Record colmapRec : colmapList) {
                                for (Record colmapRec2 : colmapList2) {
                                    if (colmapRec.getInt(eColmap.colgrp_id) == colmapRec2.getInt(eColmap.colgrp_id)
                                            && colmapRec.getInt(eColmap.color_id1) == colmapRec2.getInt(eColmap.color_id1)) {

                                        return colmapRec2.getInt(eColmap.color_id2);
                                    }
                                }
                            }
                        }

                        //Одна текстура
                    } else {
                        for (Record colmapRec : colmapList) {
                            if (artdetRec.getInt(eArtdet.color_fk) == colmapRec.getInt(eColmap.color_id1)) {
                                return colmapRec.getInt(eColmap.color_id2);
                            }
                        }
                    }
                }
            }
            return -1;

        } catch (Exception e) {
            System.err.println("Ошибка Color.colorFromArtdet() " + e);
            return -1;
        }
    }

    // Выдает цвет из текущего изделия в соответствии с заданным вариантом подбора текстуры   
    private static int colorFromTypes(SpecificRec spc, int colorType, int side) {
        try {
            switch (colorType) {
                case 0:
                    return spc.detailRec.getInt(COLOR_FK);  //указана вручную
                case 11: //Профиль
                    if ("ps3".equals(eSetting.find(2))) {
                        return spc.elem5e.colorID1; //по основе текстуры профиля
                    } else {
                        if (side == 1) {
                            return spc.elem5e.colorID1; //по основе текстуры профиля
                        } else if (side == 2) {
                            return spc.elem5e.colorID2; //по внутр. текстуры профиля 
                        } else if (side == 3) {
                            return spc.elem5e.colorID3; //по внешн. текстуры профиля  
                        }
                    }
                case 15:
                    return spc.elem5e.colorID1; //по основе текстуры заполнения                 
                case 1:
                    return spc.elem5e.iwin().colorID1; //по основе изделия
                case 2:
                    return spc.elem5e.iwin().colorID2; //по внутр.изделия
                case 3:
                    return spc.elem5e.iwin().colorID3; //по внешн.изделия
                case 6:
                    return spc.elem5e.iwin().colorID1; //по основе в серии
                case 7:
                    return spc.elem5e.iwin().colorID2; //по внутр. в серии
                case 8:
                    return spc.elem5e.iwin().colorID3; //по внешн. в серии
                default:
                    return -1;
            }
        } catch (Exception e) {
            System.err.println("Ошибка: Color.colorFromTypes() " + e);
            return -1;
        }
    }

    //Текстура профиля или текстура заполнения изделия (неокрашенные)
    public static int colorFromArtikl(int artiklId) {
        try {
            List<Record> artdetList = eArtdet.find(artiklId);
            //Цикл по ARTDET определённого артикула
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
