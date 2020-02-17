package wincalc.constr;

import dataset.Record;
import domain.eColor;
import wincalc.Wincalc;
import wincalc.model.Com5t;

/**
 * Расчёт конструктива окна.
 */
public class Constructiv extends Cal5e {

    private ParamVariant paramVariant = null;
    private ParamSpecific paramSpecific = null;
//
//    public String sideCheck = ""; //TODO Эту переменную надо вынести в map параметров!!!
//

    public Constructiv(Wincalc iwin) {

        super(iwin);
        paramVariant = new ParamVariant(iwin, this);
        paramSpecific = new ParamSpecific(iwin, this);
    }

    public void calculate(Wincalc iwin) {

        Сomposition composition = new Сomposition(iwin);
        composition.compositionFirst();                //составы
//            constructiv.joiningFirst();                    //соединения
//            constructiv.fillingFirst();                    //заполнения
//            constructiv.fittingFirst();                    //фурнитура
//            constructiv.kitsFirst();                       //комплекты    
    }

    public void kitsFirst() {

    }

    protected int determineColorCodeForArt(Com5t elem, int color_side, Record paramRec, Specification specif) {

        //int colorCode = getColorFromProduct(elem, color_side, paramRec);
        /*    Colslst clslstRecr = Colslst.get2(constr, colorCode);
        //Фвтоподбор текстуры
        if (paramRec.clnum() == 0) {
            // Если этот цвет не подходит для данного артикула, то берем первую имеющуюся текстуру в тарифе
            // материальных ценностей указанного артикула (см. "Автоподбор" на http://help.profsegment.ru/?id=1107).
            boolean colorOK = false;
            int firstFoundColorNumber = 0;
            for (Artsvst artsvstRec : constr.artsvstMap.get(specif.artikl)) {
                if ((color_side == 1 && CanBeUsedAsBaseColor(artsvstRec))
                        || (color_side == 2 && CanBeUsedAsInsideColor(artsvstRec))
                        || (color_side == 3 && CanBeUsedAsOutsideColor(artsvstRec))) {

                    if (IsArtTariffAppliesForColor(artsvstRec, clslstRecr)) {
                        colorOK = true;
                        break;
                    } else if (firstFoundColorNumber == 0)
                        firstFoundColorNumber = artsvstRec.clnum;
                }
            }
            if (colorOK) return colorCode;
            else if (firstFoundColorNumber > 0) return Colslst.get(constr, firstFoundColorNumber).ccode;
            else return root.getIwin().getColorNone();

            //указана
            //} else if (vstaspcRec.clnum() == 1) {
            //    return colorCode;

        } else if (paramRec.clnum() == 100000) {
            return colorCode;


            //Текстура задана через параметр
        } else if (paramRec.clnum() < 0) {
            if (colorCode == root.getIwin().getColorNone()) {
                for (Parcols parcolsRec : constr.parcolsList) {
                    if (parcolsRec.pnumb == paramRec.clnum()) {
                        int code = Colslst.get3(constr, parcolsRec.ptext).ccode;
                        if (code != -1) return code;
                    }
                }
            }
            return colorCode;

            //В clnum указан цвет.
        } else {
            if (colorCode == root.getIwin().getColorNone()) {    //видимо во всех текстурах стоит значение "Указана"
                // Подбираем цвет так (придумала Л.Цветкова):
                // Если основная текстура изделия может быть основной текстурой для данного артикула (по тарифу мат.ценностей), то используем ее.
                // Иначе - используем CLNUM как основную текстуру. Аналогично для внутренней и внешней текстур.

                int colorNumber = Colslst.get2(constr, root.getIwin().getColorProfile(color_side)).cnumb;
                boolean colorFound = false;
                for (Artsvst artsvst : constr.artsvstMap.get(specif.artikl)) {

                    if ((color_side == 1 && CanBeUsedAsBaseColor(artsvst)) ||
                            (color_side == 2 && CanBeUsedAsInsideColor(artsvst)) ||
                            (color_side == 3 && CanBeUsedAsOutsideColor(artsvst))) {

                        if (IsArtTariffAppliesForColor(artsvst, clslstRecr) == true) {
                            for (Parcols parcolsRec : constr.parcolsList) {
                                if (parcolsRec.pnumb == paramRec.clnum()) {
                                    return Integer.valueOf(parcolsRec.ptext);
                                }
                            }
                        }
                    }
                }
            }
        }*/
        //return colorCode;
        return -1;
    }

    // Выдает цвет из текущего изделия в соответствии с заданным вариантом подбора текстуры (dll->GetColorCodeFromProduct() - см. "SVN\iWin_Doc\trunk\Профстрой\Значения GLASART.CTYPE.xlsx".
    private int getColorFromProduct(Com5t com5t, int colorSide, int type, int clnum) {
        // Получаем код варианта подбора текстуры (см. "SVN\iWin_Doc\trunk\Профстрой\Значения GLASART.CTYPE.xlsx").
        int colorType = 0;
        if (colorSide == 1) colorType = type % 16;
        else if (colorSide == 2) colorType = (type / 16) % 16;
        else if (colorSide == 3) colorType = (type / (16 * 16)) % 16;
        switch (colorType) {
            case 0:  //указана
                return eColor.find2(clnum).getInt(eColor.code);
            case 1:        // по основе изделия
            case 6:        // по основе в серии
                return iwin.color1; //elem.getColor(1);
            case 2:        // по внутр.изделия
            case 7:        // по внутр. в серии
                return iwin.color2; //elem.getColor(2);
            case 3:        // по внешн.изделия
            case 8:        // по внешн. в серии
                return iwin.color3; //elem.getColor(3);
            case 11:    // по профилю
                return iwin.color1;
            case 15:    // по заполнению
                return com5t.color1;
            default:    // без цвета
                return iwin.colorNone;
        }
    }
}
