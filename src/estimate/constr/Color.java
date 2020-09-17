package estimate.constr;

import dataset.Record;
import domain.eArtdet;
import domain.eArtikl;
import domain.eColor;
import domain.eGlasdet;
import estimate.model.Com5t;
import java.util.List;

public class Color {

    private static int TYPES = 2;
    private static int COLOR_FK = 3;
    private static int ARTIKL_ID = 4;

    //TODO ВАЖНО !!! необходимо по умолчанию устанавливать colorNone = 1005 - без цвета
    public static void setting(Specification spc, Record spcdetRec) {  //см. http://help.profsegment.ru/?id=1107        
        int colorFk = spcdetRec.getInt(COLOR_FK);

        //Цыкл по сторонам текстур элемента
        for (int side = 1; side < 4; ++side) {
            try {
                int colorID = colorFromProduct(spc.elem5e, side, spcdetRec); //цвет из варианта подбора           
                if (colorFk == 0) { //автоподбор текстуры
                    int color_id = location(spc.artikl, side, colorID);
                    if (color_id != -1) {
                        spc.setColor(side, color_id);

                    } else { //если неудача подбора
                        Record artdetRec = eArtdet.find2(spcdetRec.getInt(ARTIKL_ID)); //первая в списке запись цвета
                        if (artdetRec != null) {
                            int colorFK2 = artdetRec.getInt(eArtdet.color_fk);
                            if (colorFK2 > 0) { //если это не группа цветов
                                spc.setColor(side, colorFK2);

                            } else if (colorFK2 < 0 && colorFK2 != -1) { //это группа
                                artdetRec = eColor.find2(colorFK2 * -1).get(0); //первая в списке запись цвета
                                spc.setColor(side, artdetRec.getInt(eColor.id));
                            }
                        }
                    }
                } else if (colorFk == 100000) { //точный подбор 
                    int colorId = location(spc.artikl, side, colorID);
                    if (colorId != -1) {
                        spc.setColor(side, colorId);
                    }

                } else if (colorFk > 0 && colorFk != 100000) { //указана
                    spc.setColor(side, colorID);

                } else if (colorFk < 0) { //текстура задана через параметр
                    int colorFK2 = parametr();
                    spc.setColor(side, colorFK2);
                }
            } catch (Exception e) {
                System.out.println("Ошибка estimate.constr.Color.setting() " + e);
            }
        }
    }

    //Автоподбор или точный подбор текстуры
    private static int location(String artikl, int side, int colorID) {
        try {
            Record artiklRec = eArtikl.find2(artikl);
            List<Record> artdetList = eArtdet.find(artiklRec.getInt(eArtikl.id));
            //Цыкл по ARTDET определённого артикула
            for (Record artdetRec : artdetList) {
                if (canBeUsed(side, artdetRec) == true) {
                    if (artdetRec.getInt(eArtdet.color_fk) < 0) { //группа текстур

                        List<Record> colorList = eColor.find2(artdetRec.getInt(eArtdet.color_fk));
                        for (Record colorRec : colorList) {
                            if (colorRec.getInt(eColor.id) == colorID) {
                                return colorID;
                            }
                        }
                    } else if (artdetRec.getInt(eArtdet.color_fk) == colorID) {
                        return colorID;
                    }
                }
            } 
            return -1;
            
        } catch (Exception e) {
            System.out.println("Ошибна estimate.constr.Color.location() " + e);
            return -1;
        }
    }
    //Текстура указана в настройках конструктива

    private static int indicated(String artikl, int side, int colorID) {
        Record artiklRec = eArtikl.find2(artikl);
        List<Record> artdetList = eArtdet.find(artiklRec.getInt(eArtikl.id));
        //Цыкл по ARTDET определённого артикула
        for (Record artdetRec : artdetList) {
            if (canBeUsed(side, artdetRec) == true) {
                if (artdetRec.getInt(eArtdet.color_fk) == colorID) { //если есть такая текстура в ARTDET
                    return colorID;
                }
            }
        }
        return -1; //иначе виртуальный цвет
    }

    //TODO - Текстура задана через параметр
    private static int parametr() {
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

    //Cторона подлежит рассмотрению ?
    public static boolean canBeUsed(int side, Record artdetRec) {
        if ((side == 1 && (artdetRec.getInt(eArtdet.prefe) & 4) != 0)
                || (side == 2 && (artdetRec.getInt(eArtdet.prefe) & 4) != 0 || (artdetRec.getInt(eArtdet.prefe) & 1) != 0)
                || (side == 3 && (artdetRec.getInt(eArtdet.prefe) & 4) != 0 || (artdetRec.getInt(eArtdet.prefe) & 2) != 0)) {
            return true;
        }
        return false;
    }

    // Выдает цвет из текущего изделия в соответствии с заданным вариантом подбора текстуры
    public static int colorFromProduct(Com5t com5t, int colorSide, Record detailRec) {
        int types = detailRec.getInt(TYPES);
        try {
            int colorType = (colorSide == 1) ? types & 0x0000000f : (colorSide == 2) ? (types & 0x000000f0) >> 4 : (types & 0x00000f00) >> 8;
            switch (colorType) {
                case 0:  //указана
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
                    return com5t.iwin().colorID1;
                case 12:       // по параметру (внешн.)    
                    return -1;
                case 15:       // по заполнению
                    return com5t.colorID1;
                default:    // без цвета
                    return com5t.iwin().colorNone;
            }
        } catch (Exception e) {
            System.out.println("Ошибка estimate.constr.Color.colorFromProduct() " + e);
            return -1;
        }
    }

}
