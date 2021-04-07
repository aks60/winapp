package common;

import dataset.Query;
import domain.eArtikl;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;

public class LambdaPar {
    
    //Default value
    // <editor-fold defaultstate="collapsed" desc="Generated Defparam">   
    public interface Defparam {

        public String def();
    }

    public static Defparam def_Default = () -> {

        return "";
    };

    public static Defparam def_NUMB = () -> {

        return "0";
    };

    public static Defparam def_LENGTH = () -> {

        return "0-1000;";
    };

    public static Defparam def_ANGL = () -> {

        return "0-360;";
    };
    // </editor-fold>  

    //Dictionary
    // <editor-fold defaultstate="collapsed" desc="Generated Dictionary"> 
    public interface Dictionary {

        public List<String> dict();
    }

    public static Dictionary dic_DEFAULT = () -> {
        return null;
    };

    public static Dictionary dic_21050_2015_3015_39093_4015 = () -> {
        return Arrays.asList("горизонтально", "вертикально");
    };

    public static Dictionary dic_38004_39005 = () -> {
        return Arrays.asList("по периметру", "по площади", "как крест", "как концевик",
                "длина по коробке", "для прямых углов", "для не прямых углов");
    };

    public static Dictionary dic_2003_3003 = () -> {
        return Arrays.asList("левый", "правый");
    };

    public static Dictionary dic_24012_25012 = () -> {
        return Arrays.asList("левое", "правое");
    };

    public static Dictionary dic_31002 = () -> {
        return Arrays.asList("прямой", "кривой");
    };

    public static Dictionary dic_OK_NO = () -> {
        return Arrays.asList("Да", "Нет");
    };

    public static Dictionary dic_15027_12027 = () -> {
        return Arrays.asList("с уплотнителем", "без уплотнителя");
    };

    public static Dictionary dic_4005_11005_12005_31050_33071_34071 = () -> {
        return Arrays.asList("коробка", "створка", "импост", "ригель/импост", "стойка", "стойка/коробка", "эркер", "грань");
    };

    public static Dictionary dic_24070_25070 = () -> {
        return Arrays.asList("по середине", "константная", "не константная", "установлена");
    };

    public static Dictionary dic_21001_24001_25001 = () -> {
        return Arrays.asList("прямоугольная", "трапециевидная", "не арочная", "арочная");
    };

    public static Dictionary dic_13015 = () -> {
        return Arrays.asList("Прямоугольное", "Не прямоугольное", "Не арочное", "Арочное");
    };

    public static Dictionary dic_37009 = () -> {
        return Arrays.asList("Все", "Произвольное", "Прямоугольное", "Арочное");
    };

    public static Dictionary dic_37005 = () -> {
        return Arrays.asList("Внутренний", "Раскладка", "Вагонка", "Жалюзи на профили", "Жалюзи на заполнение");
    };

    public static Dictionary dic_COLOR = () -> {
        return Arrays.asList("1цвет", "2цвет", "3цвет", "4цвет");
    };

    public static Dictionary dic_COLOR2 = () -> {
        return Arrays.asList("1цвет", "2цвет", "3цвет", "4цвет");
    };

    public static Dictionary dic_11072_12072 = () -> {
        return Arrays.asList("большей", "меньшей", "общей", "4");
    };

    public static Dictionary dic_3088 = () -> {
        return Arrays.asList("внешний", "внутренний");
    };

    public static Dictionary dic_37001 = () -> {
        return Arrays.asList("на профили", "на заполнения");
    };

    public static Dictionary dic_20066 = () -> {
        return Arrays.asList("по меньшей ширине");
    };

    public static Dictionary dic_33031_34061 = () -> {
        return Arrays.asList("по уровням деления");
    };

    public static Dictionary dic_13003_14005_15005_37008 = () -> {
        return Arrays.asList("глухой", "не глухой");
    };

    public static Dictionary dic_24006 = () -> {
        return Arrays.asList("по текстуре ручки", "по текстуре подвеса");
    };

    public static Dictionary dic_39063 = () -> {
        return Arrays.asList("меньшего целого числа", "большего целого числа", "большего чётного числа", "большего нечётного числа");
    };

    public static Dictionary dic_33062_34062 = () -> {
        return Arrays.asList("только вверх", "только вниз", "вверх и вниз", "нет удлинений");
    };

    public static Dictionary dic_13005 = () -> {
        return Arrays.asList("Стекло", "Стеклопакет", "Сендвич", "Вагонка", "Алюминевый лист", "Специальное стекло", "Конструктив", "Панель откоса");
    };

    public static Dictionary dic_25013 = () -> {
        return Arrays.asList("длины стороны", "высоты ручки", "сторона выс-ручки", "половины стороны");
    };

    public static Dictionary dic_33004_34004 = () -> {
        return Arrays.asList("вычет длин удлинений", "по нижнему удлинению", "по верхнему удлинению", "по обоим удлинениям");
    };

    public static Dictionary dic_12075_34075_39075 = () -> {
        return Arrays.asList("по контейнерам", "по биссектрисам", "установить (90° x 90°)", "установить (90° x 45°)", "установить (45° x 45°)");
    };

    public static Dictionary dic_34010 = () -> {
        return Arrays.asList("от конца  профиля", "от внутреннего угла", "от внутреннего фальца");
    };

    public static Dictionary dic_4002 = () -> {
        return Arrays.asList("Простое T-обр", "Крестовое †-обр", "Сложное Y-обр");
    };

    public static Dictionary dic_3002 = () -> {
        return Arrays.asList("Простое L-обр", "Крестовое †-обр");
    };

    public static Dictionary dic_SERIES = () -> {
        return Arrays.asList("sql к сериям", "sql к сериям", "sql к сериям");
    };

    public static Dictionary dic_37097 = () -> {
        return Arrays.asList("периметру", "площади");
    };

    public static Dictionary dic_15013 = () -> {
        return Arrays.asList("без/минимальный дистанционер", "с наибольшим дистанционером");
    };

    public static Dictionary dic_1039_38039_39039 = () -> {
        return Arrays.asList("фрамуга", "поворотное", "поворотно-откидное", "раздвижное");
    };

    public static Dictionary dic_34015 = () -> {
        return Arrays.asList("длине контейнера", "межосевому расстоянию");
    };

    public static Dictionary dic_TEAM = () -> {
        return Arrays.asList("sql запрос к таблице бригад", "sql запрос к таблице бригад");
    };

    public static Dictionary dic_31019 = () -> {
        return Arrays.asList("внутренняя по основной", "внешняя по основной", "внутрення по внешней", "внешняя по внутренней", "2 стороны по основной");
    };

    public static Dictionary dic_15011 = () -> {
        return Arrays.asList("по биссектрисе", "усекать нижний", "усекать боковой");
    };

    public static Dictionary dic_ARTIKL_CODE = () -> {
        List list = new ArrayList();
        new Query(eArtikl.code).select(eArtikl.up, "where", eArtikl.level1, "= 1", "order by", eArtikl.level2).forEach(rec -> list.add(rec.getStr(eArtikl.code)));
        return list;
    };
    // </editor-fold>

    //Check
    // <editor-fold defaultstate="collapsed" desc="Generated Checkparam">   
    public interface Checkparam {

        public boolean check(String c);
    }

    public static Checkparam check_STRING = (c) -> {

        return true;
    };

    public static Checkparam check_COUNT = (c) -> {
        return ("-0123456789".indexOf(c) != -1);
    };

    public static Checkparam check_INT = (c) -> {
        return ("-0123456789".indexOf(c) != -1);
    };

    public static Checkparam check_INT_MOD = (c) -> {
        return ("0123456789".indexOf(c) != -1);
    };

    public static Checkparam check_INT_LIST = (c) -> {
        return ("0123456789-;".indexOf(c) != -1);
    };

    public static Checkparam check_INT_LIST2 = (c) -> {
        return ("0123456789-/@".indexOf(c) != -1);
    };

    public static Checkparam check_FLOAT = (c) -> {
        return ("0123456789.-".indexOf(c) != -1);
    };

    public static Checkparam check_FLOAT_MOD = (c) -> {
        return ("0123456789.".indexOf(c) != -1);
    };

    public static Checkparam check_FLOAT_LIST = (c) -> {
        return ("0123456789.-;".indexOf(c) != -1);
    };

    public static Checkparam check_FLOAT_LIST2 = (c) -> {
        return ("0123456789.-/@".indexOf(c) != -1);
    };
    // </editor-fold> 

    //Formatter
    // <editor-fold defaultstate="collapsed" desc="Generated Code">   
    private static JFormattedTextField.AbstractFormatterFactory defaultFormatter = new DefaultFormatterFactory();
    
    private static JFormattedTextField.AbstractFormatterFactory numberFormatter = new DefaultFormatterFactory(new NumberFormatter());

    public static JFormattedTextField.AbstractFormatterFactory defaultFormatter() {
        return defaultFormatter;
    }

    public interface Formatter {

        public JFormattedTextField.AbstractFormatterFactory format();
    }

    public static Formatter frm_Default = () -> {
        return numberFormatter;
    };

    public static Formatter frm_Number = () -> {
        try {
            return new DefaultFormatterFactory(new MaskFormatter("###"));
        } catch (final ParseException e) {
            return null;
        }
    };

    public static Formatter frm_Mask = () -> {
        try {
            return new DefaultFormatterFactory(new MaskFormatter("###;###.###-##"));
        } catch (final ParseException e) {
            return null;
        }
    };
    // </editor-fold>      
}
