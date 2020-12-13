package estimate.constr;

import dataset.Record;
import domain.eArtdet;
import domain.eArtikl;
import domain.eColor;
import domain.eColpar1;
import domain.eParams;
import enums.UseColor;
import java.util.Comparator;
import java.util.List;
import static java.util.stream.Collectors.toList;
import javax.swing.JOptionPane;

public class Color {

    private static final int TYPES = 2;
    private static final int COLOR_FK = 3;
    private static final int ARTIKL_ID = 4;

    public static boolean colorFromProduct(Specification spc) {  //см. http://help.profsegment.ru/?id=1107        

        int colorFk = spc.detailRec.getInt(COLOR_FK);
        int types = spc.detailRec.getInt(TYPES);

        //Цыкл по сторонам текстур элемента
        for (int side = 1; side < 4; ++side) {
            try {
                int artdetColorFK = -1;
                int colorType = (side == 1) ? types & 0x0000000f : (side == 2) ? (types & 0x000000f0) >> 4 : (types & 0x00000f00) >> 8; //тип подбора                
                int elemColorID = colorFromTypes(spc, colorType, side); //цвет из варианта подбора 

                //Поиск по стороне текстуры в серии артикулов
                if (colorType == UseColor.C1SER.id || colorType == UseColor.C2SER.id || colorType == UseColor.C3SER.id) {
                    artdetColorFK = colorFromSeries(spc.artiklRec, side, elemColorID, colorFk);

                    //Поиск по стороне текстуры в артикуле
                } else if (colorType == UseColor.COL1.id || colorType == UseColor.COL2.id || colorType == UseColor.COL3.id
                        || colorType == UseColor.PROF.id || colorType == UseColor.GLAS.id) {
                    artdetColorFK = colorFromArtikl(spc.artiklRec, side, elemColorID, colorFk);
                }

                //Указана вручную
                if (colorFk > 0 && colorFk != 100000) {
                    if (colorType == UseColor.MANUAL.id || artdetColorFK == -1) { //явное указание текстуры или неудача поиска
                        spc.setColor(side, colorFk);
                    } else {
                        spc.setColor(side, artdetColorFK);
                    }

                    //Автоподбор текстуры
                } else if (colorFk == 0) {
                    if (artdetColorFK != -1) {
                        spc.setColor(side, artdetColorFK);

                    } else { //если неудача подбора то первая в списке запись цвета
                        Record artdetRec = eArtdet.find2(spc.detailRec.getInt(ARTIKL_ID));
                        if (artdetRec != null) {
                            int colorFK2 = artdetRec.getInt(eArtdet.color_fk);
                            if (colorFK2 > 0) { //если это не группа цветов                               
                                spc.setColor(side, colorFK2);

                            } else if (colorFK2 < 0 && colorFK2 != -1) { //это группа
                                List<Record> colorList = eColor.find2(colorFK2 * -1);
                                if (colorList.isEmpty() == false) {
                                    spc.setColor(side, colorList.get(0).getInt(eColor.id));
                                }
                            }
                        }
                    }

                    //Точный подбор
                } else if (colorFk == 100000) {
                    if (artdetColorFK != -1) {
                        spc.setColor(side, artdetColorFK);

                    } else {
                        //В спецификпцию не попадёт. См. HELP "Конструктив=>Подбор текстур"
                        return false;
                    }

                    //Текстура задана через параметр
                } else if (colorFk < 0) {
                    if (artdetColorFK != -1) {
                        spc.setColor(side, elemColorID);

                    } else {//В спецификпцию не попадёт. См. HELP "Конструктив=>Подбор текстур"
                        return false;
                    }
                }
            } catch (Exception e) {
                System.err.println("Ошибка:Color.setting() " + e);
            }
        }

        return true;
    }

    //Поиск текстуры в серии артикулов
    private static int colorFromSeries(Record artiklRec, int side, int colorFromTypesID, int colorFk) {

        List<Record> artseriList = eArtikl.find3(artiklRec.getInt(eArtikl.series_id));
        for (Record artseriRec : artseriList) {

            int color_id = colorFromArtikl(artseriRec, side, colorFromTypesID, colorFk);
            if (color_id != -1) {
                return color_id;
            }
        }
        return -1;
    }

    //Поиск текстуры в артикуле
    private static int colorFromArtikl(Record artiklRec, int side, int colorFromTypesID, int colorFk) {
        try {
            List<Record> artdetList = eArtdet.find(artiklRec.getInt(eArtikl.id));
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
//                            if (compare(colorFk, colorRec.getInt(eColor.id), colorFromTypesID) != -1) {
//                                return colorFromTypesID;
//                            }
                            if (colorRec.getInt(eColor.id) == colorFromTypesID) {
                                return colorFromTypesID;
                            }
                        }

                        //Одна текстура
                    } else {
//                        if (compare(colorFk, artdetRec.getInt(eArtdet.color_fk), colorFromTypesID) != -1) {
//                            return colorFromTypesID;
//                        }
                        if (artdetRec.getInt(eArtdet.color_fk) == colorFromTypesID) { //если есть такая текстура в ARTDET
                            return colorFromTypesID;
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
    private static int colorFromTypes(Specification spc, int colorType, int side) {
        try {
            switch (colorType) {
                case 0:
                    return spc.detailRec.getInt(COLOR_FK);  //указана вручную
                case 11:
                    if (side == 1) {
                        return spc.elem5e.colorID1; //по основе текстуры профиля
                    } else if (side == 2) {
                        return spc.elem5e.colorID2; //по внутр. текстуры профиля 
                    } else if (side == 3) {
                        return spc.elem5e.colorID3; //по внешн. текстуры профиля  
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

    //функции сравнения по параметру текстур или по id текстур
    private static int compare(int colorFk, int p1, int p2) {
        if (colorFk < 0) { //по параметру текстур
            List<Record> list1 = eColpar1.find(p1);
            List<Record> list2 = eColpar1.find(p2);

            for (Record record1 : list1) {
                for (Record record2 : list2) {
                    if (record1.getInt(eColpar1.grup) == record2.getInt(eColpar1.grup)
                            && record1.getInt(eColpar1.numb) == record2.getInt(eColpar1.numb)) {
                        return p2;
                    }
                }
            }
            return -1;
        } else { //по id текстур
            return (p1 == p2) ? p2 : -1;
        }
    }
}
