package estimate.constr;

import dataset.Record;
import domain.eArtdet;
import domain.eArtikl;
import domain.eColor;
import enums.UseUnit;
import enums.TypeArtikl;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import estimate.model.Com5t;
import estimate.model.ElemSimple;
import java.util.LinkedHashMap;
import jxl.Sheet;
import jxl.Workbook;

/**
 * Спецификация элемента окна
 */
public class Specification {

    public ArrayList<Specification> specificationList = new ArrayList(); //список составов, фарнитур и т.д.
    private HashMap<Integer, String> mapParam = null; //параметры спецификации
    public ElemSimple elem5e = null; //элемент пораждающий спецификацию
    public Record artiklRec = null; //профиль в спецификации

    public float id = -1; //ID
    public String place = "-"; //Место расмешения
    public String name = "-"; //Наименование
    public String artikl = "-";  //Артикул
    public int colorID1 = 1005;  //Осн.текстура
    public int colorID2 = 1005;  //Внутр.текстура
    public int colorID3 = 1005;  //Внешн.текстура
    public float width = 0;  //Длина
    public float height = 0;  //Ширина
    public float weight = 0;  //Масса
    public float anglCut1 = 0;  //Угол1
    public float anglCut2 = 0;  //Угол2
    public float anglHoriz = 0; // Угол к горизонту    
    public int count = 1;  //Кол. единиц
    public int unit = 0; //Ед.изм
    public float quantity = 0; //Количество без отхода
    public float wastePrc = 0;  //Процент отхода
    public float quantity2 = 0;  //Количество с отходом
    public float inPrice = 0;  //Себес-сть за ед. изм.
    public float outPrice = 0;  //Себес-сть за злемент с отходом
    public float inCost = 0; //Стоимость без скидки
    public float outCost = 0; //Стоимость со скидкой
    public float discount = 0;  //Скидка

    /**
     * Конструктор для видимых эдементов окна
     *
     * @param id
     * @param elem5e
     */
    public Specification(float id, ElemSimple elem5e) {
        this.id = id;
        this.elem5e = elem5e;
        this.mapParam = new HashMap();
    }

    //Конструктор для элементов спецификации окна
    public Specification(Record artiklRec, ElemSimple elem5e, HashMap<Integer, String> mapParam) {
        this.id = ++elem5e.iwin().genId;
        this.elem5e = elem5e;
        this.mapParam = mapParam;
        setArtiklRec(artiklRec);
    }

    public Specification(Specification spec) {
        this.id = spec.id; //++spec.elem5e.iwin().genId;
        this.place = spec.place;
        this.artikl = spec.artikl;
        this.name = spec.name;
        this.colorID1 = spec.colorID1;
        this.colorID2 = spec.colorID2;
        this.colorID3 = spec.colorID3;
        this.width = spec.width;
        this.height = spec.height;
        this.anglCut2 = spec.anglCut2;
        this.anglCut1 = spec.anglCut1;
        this.count = spec.count;
        this.unit = spec.unit;
        this.quantity = spec.quantity;
        this.wastePrc = spec.wastePrc;
        this.quantity2 = spec.quantity2;
        this.inPrice = spec.inPrice;
        this.outPrice = spec.outPrice;
        this.discount = spec.discount;
        this.anglHoriz = spec.anglHoriz;
        this.mapParam = spec.mapParam;
        this.elem5e = spec.elem5e;
        this.artiklRec = spec.artiklRec;
    }

    public Vector getVector(int npp) {
        List list = Arrays.asList(npp, id, place, artikl, name, eColor.find(colorID1).getStr(eColor.name), eColor.find(colorID2).getStr(eColor.name),
                eColor.find(colorID3).getStr(eColor.name), (width == 0) ? "" : width, (height == 0) ? "" : height, (weight == 0) ? "" : weight,
                (anglCut1 == 0) ? "" : anglCut1, (anglCut2 == 0) ? "" : anglCut2, (anglHoriz == 0) ? "" : anglHoriz,
                count, UseUnit.getName(unit), quantity, wastePrc, quantity2, inPrice, outPrice, inCost, outCost, discount
        );
        return new Vector(list);
    }

    public void setArtiklRec(Record artiklRec) {
        this.artikl = artiklRec.getStr(eArtikl.code);
        this.name = artiklRec.getStr(eArtikl.name);
        this.wastePrc = artiklRec.getFloat(eArtikl.otx_norm);
        this.unit = artiklRec.getInt(eArtikl.unit); //atypi;
        this.artiklRec = artiklRec;
        setAnglCut();
        //this.height = artiklRec.aheig; //TODO парадокс добавления ширины, надо разобраться
    }

    public void setColor(int side, int colorID) {
        if (side == 1) {
            colorID1 = colorID;
        } else if (side == 2) {
            colorID2 = colorID;
        } else if (side == 3) {
            colorID3 = colorID;
        }
    }

    //TODO ВАЖНО !!! необходимо по умолчанию устонавливать colorNone = 1005 - без цвета
    public void setColor(Com5t com5t, Record record) {  //см. http://help.profsegment.ru/?id=1107        
        int constr_types = record.getInt(2);
        int constr_colorFk = record.getInt(3);
        
        //Цыкл по 3 сторонам текстур
        for (int index = 1; index < 4; ++index) {

            int colorID = getColorFromProduct(com5t, index, record); //подбор цвета для сторон
            Record colorRec = eColor.find(colorID);

            //Автоподбор текстуры
            if (constr_colorFk == 0) {

                Record artiklRec = eArtikl.find2(artikl);
                List<Record> artdetList = eArtdet.find2(artiklRec.getInt(eArtikl.id));
                //Цыкл по ARTDET определённого артикула
                for (Record artdetRec : artdetList) {
                    
                    if (canBeUsedColor(index, artdetRec) == true) {
                        if(artdetRec.getInt(eArtdet.color_fk) < 0) { //группа текстур
                            List<Record> list = eColor.find2(artdetRec.getInt(eArtdet.color_fk));
                           for(Record colorRec2: list) {
                               
                           }
                        } else if(artdetRec.getInt(eArtdet.color_fk) == colorID) {
                          setColor(index, colorID);  
                        }
                    }
                }
                //указана
                //} else if (vstaspcRec.clnum() == 1) {
                //    return colorCode;
            }
//  else if (paramRec.clnum() == 100000) {
//            return colorCode;
//
//
//            //Текстура задана через параметр
//        } else if (paramRec.clnum() < 0) {
//            if (colorCode == root.getIwin().getColorNone()) {
//                for (Parcols parcolsRec : constr.parcolsList) {
//                    if (parcolsRec.pnumb == paramRec.clnum()) {
//                        int code = Colslst.get3(constr, parcolsRec.ptext).ccode;
//                        if (code != -1) return code;
//                    }
//                }
//            }
//            return colorCode;
//
//            //В clnum указан цвет.
//        } else {
//            if (colorCode == root.getIwin().getColorNone()) {    //видимо во всех текстурах стоит значение "Указана"
//                // Подбираем цвет так (придумала Л.Цветкова):
//                // Если основная текстура изделия может быть основной текстурой для данного артикула (по тарифу мат.ценностей), то используем ее.
//                // Иначе - используем CLNUM как основную текстуру. Аналогично для внутренней и внешней текстур.
//
//                int colorNumber = Colslst.get2(constr, root.getIwin().getColorProfile(index)).cnumb;
//                boolean colorFound = false;
//                for (Artsvst artsvst : constr.artsvstMap.get(specif.artikl)) {
//
//                    if ((index == 1 && CanBeUsedAsBaseColor(artsvst)) ||
//                            (index == 2 && CanBeUsedAsInsideColor(artsvst)) ||
//                            (index == 3 && CanBeUsedAsOutsideColor(artsvst))) {
//
//                        if (IsArtTariffAppliesForColor(artsvst, clslstRecr) == true) {
//                            for (Parcols parcolsRec : constr.parcolsList) {
//                                if (parcolsRec.pnumb == paramRec.clnum()) {
//                                    return Integer.valueOf(parcolsRec.ptext);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
            //return colorCode;
        }
    }

    //Cторона подлежит рассмотрению ?
    public boolean canBeUsedColor(int side, Record artdetRec) {
        if ((side == 1 && (artdetRec.getInt(eArtdet.prefe) & 4) != 0)
                || (side == 2 && (artdetRec.getInt(eArtdet.prefe) & 4) != 0 || (artdetRec.getInt(eArtdet.prefe) & 1) != 0)
                || (side == 3 && (artdetRec.getInt(eArtdet.prefe) & 4) != 0 || (artdetRec.getInt(eArtdet.prefe) & 2) != 0)) {
            return true;
        }
        return false;
    }

    // Выдает цвет из текущего изделия в соответствии с заданным вариантом подбора текстуры
    //public int getColorFromProduct(Com5t com5t, int colorSide, int type, int colorId) {
    public int getColorFromProduct(Com5t com5t, int colorSide, Record record) {
        int types = record.getInt(2);
        int colorFk = record.getInt(3);
        int colorType = (colorSide == 1) ? types & 0x0000000f : (colorSide == 2) ? types & 0x000000f0 >> 4 : types & 0x00000f00 >> 8;
        switch (colorType) {
            case 0:  //указана
                return colorFk;
            case 1:        // по основе изделия
                return elem5e.iwin().colorID1;
            case 2:        // по внутр.изделия
                return elem5e.iwin().colorID2;
            case 3:        // по внешн.изделия
                return elem5e.iwin().colorID3; //elem.getColor(3); 
            case 4:        // по параметру (внутр.)
                return -1;
            case 6:        // по основе в серии
                return elem5e.iwin().colorID1;
            case 7:        // по внутр. в серии
                return elem5e.iwin().colorID2;
            case 8:        // по внешн. в серии
                return elem5e.iwin().colorID3; //elem.getColor(3);
            case 9:        // по параметру (основа)
                return -1;
            case 11:       // по профилю
                return elem5e.iwin().colorID1;
            case 12:       // по параметру (внешн.)    
                return -1;
            case 15:       // по заполнению
                return com5t.colorID1;
            default:    // без цвета
                return elem5e.iwin().colorNone;
        }
    }

    protected void setAnglCut() {
        //TODO Тут логическая ошибка
        if (TypeArtikl.FURNITURA.isType(artiklRec)
                || TypeArtikl.KONZEVPROF.isType(artiklRec)
                || TypeArtikl.MONTPROF.isType(artiklRec)
                || TypeArtikl.FIKSPROF.isType(artiklRec)) {
            anglCut2 = 90;
            anglCut1 = 90;

        } else if (TypeArtikl.FURNITURA.isType(artiklRec)) {
            anglCut2 = 0;
            anglCut1 = 0;
        }
    }

    public void putParam(Integer key, String val) {
        mapParam.put(key, val);
    }

    public String getParam(Object def, int... p) {

        if (mapParam == null) {
            System.err.println("ОШИБКА getHmParam() hmParamJson = null");
        }
        for (int index = 0; index < p.length; ++index) {
            int key = p[index];
            String str = mapParam.get(Integer.valueOf(key));
            if (str != null) {
                return str;
            }
        }
        return String.valueOf(def);
    }

    public static void write_csv(ArrayList<Specification> spcList) {
        Writer writer = null;
        try {
            File file = new File("C:\\Java\\IWinCalc\\out\\Specification.csv.");
            writer = new BufferedWriter(new FileWriter(file));

            writer.write(new String(("TEST Изделие, Элемент, Артикул, Наименование, Текстура, Внутренняя, Внешняя, Длина. мм, "
                    + "Ширина. мм, Угол1, Угол2, Количество, Погонаж, Ед.изм, Ед.изм, Скидка, Скидка").getBytes("windows-1251"), "UTF-8"));
            for (Specification spc : spcList) {

                String str = spc.id + "," + spc.place + "," + spc.artikl + "," + spc.name + "," + spc.colorID1 + "," + spc.colorID2 + "," + spc.colorID3
                        + "," + String.format("%.1f", spc.width) + String.format("%.1f", spc.height) + String.format("%.2f", spc.anglCut2)
                        + String.format("%.2f", spc.anglCut1) + spc.count + spc.width + spc.unit + spc.discount + "\n";

                str = new String(str.getBytes(), "windows-1251");

                writer.write(str);
            }
        } catch (Exception ex) {
            System.err.println("Ошибка Specification.write_csv() " + ex);
        } finally {
            try {
                writer.flush();
                writer.close();
            } catch (Exception ex2) {
                System.err.println("Ошибка Specification.write_csv() " + ex2);
            }
        }
    }

    public static void write_txt1(ArrayList<Specification> specList) {
        int npp = 0;
        String format = "%-6s%-46s%-32s%-32s%-32s%-32s%-16s%-16s%-16s%-16s%-16s%-16s%-16s%-16s%-16s%-16s"
                + "%-16s%-16s%-16s%-16s%-16s%-16s%-16s%-16s%-16s%-16s%-16s%-16s%-16s%-16s%-16s %n";
        Object str[] = {"Code", "Name", "Art", "BaseColor", "InsideColor", "OutsideColor", "Count", "Quantity",
            "UM", "InPrice", "CostPrice", "OutPrice", "OutTotal", "Width", "Height", "Weight",
            "Angle", "ComplType", "ElemID", "elemType", "ObjectID", "ObjectType", "AreaID", "AreaType",
            "AccessoryID", "PriceGRP", "PrintGroup", "CutAngle1", "CutAngle2", "Composite", "Усл.окна"};
        String str3 = new String(("Спецификация (" + specList.size() + " строк):").getBytes());
        System.out.printf(format, str);
        for (Specification s : specList) {

            Object str2[] = {String.valueOf(++npp), s.name, s.artikl,
                eColor.find(s.colorID1).getInt(eColor.name),
                eColor.find(s.colorID2).getInt(eColor.name),
                eColor.find(s.colorID3).getInt(eColor.name),
                String.valueOf(s.count), String.valueOf(s.quantity),
                UseUnit.getName(s.unit), "0", String.valueOf(s.inPrice), String.valueOf(s.outPrice), String.valueOf(s.inCost),
                String.valueOf(s.width), String.valueOf(s.height), "0", "0", "0", String.valueOf(s.id), "0", "0", "0", "0", "0",
                "0", "0", "0", String.valueOf(s.anglCut2), String.valueOf(s.anglCut1), "0", "0"};
            System.out.printf(format, str2);
        }
        float totalVal = 0;
        for (Specification s : specList) {
            totalVal = totalVal + s.outCost;
        }
        String str4 = new String(("Суммарная цена = " + totalVal).getBytes());
        System.out.println(str4);
    }

    public static void write_txt2(ArrayList<Specification> specList) {
        try {
            int npp = 0;
            String format = "%-6s%-16s%-60s%-26s%-12s%-12s%-12s%-12s";
            Object str[] = {"Npp", "Place", "Name", "Code", "areaId", "elemId", "owner", "xxx"};
            System.out.printf(format, str);
            System.out.println();
            float total = 0;
            for (Specification s : specList) {
                Object str2[] = {String.valueOf(++npp), s.place, s.name, s.artikl,
                    s.elem5e.owner().id(), s.elem5e.id(), s.elem5e.specificationRec.artiklRec.get(eArtikl.code), s.inPrice};
                total = total + s.weight;
                System.out.printf(format, str2);
                System.out.println();
            }
            System.out.println("Масса окна " + total + " кг.");
        } catch (Exception e) {
            System.err.println("Ошибка estimate.constr.write_txt2() " + e);
        }
    }

    //сравнение спецификации с профстроем
    public static void compareIWin(ArrayList<Specification> spcList, int prj, boolean frame) {

        //TODO нужна синхронизация функции
        Float iwinTotal = 0f, jarTotal = 0f;
        String path = "src\\resource\\xls\\p" + prj + ".xls";
        //Specification.sort(spcList);
        Map<String, Float> hmDll = new LinkedHashMap();
        Map<String, Float> hmJar = new LinkedHashMap();
        Map<String, String> hmJarArt = new LinkedHashMap();
        for (Specification spc : spcList) {

            String key = spc.name.trim().replaceAll("[\\s]{1,}", " ");
            Float val = (hmJar.get(key) == null) ? 0.f : hmJar.get(key);
            hmJar.put(key, val + spc.inCost);
            hmJarArt.put(key, spc.artikl);
        }
        Workbook w;
        File inputWorkbook = new File(path);
        try {
            w = Workbook.getWorkbook(inputWorkbook);
            Sheet sheet = w.getSheet(0);
            for (int i = 5; i < sheet.getRows(); i++) {

                String art = sheet.getCell(1, i).getContents().trim();
                String key = sheet.getCell(2, i).getContents().trim().replaceAll("[\\s]{1,}", " ");
                String val = sheet.getCell(10, i).getContents();
                if (key.isEmpty() || art.isEmpty() || val.isEmpty()) {
                    continue;
                }

                val = val.replaceAll("[\\s|\\u00A0]+", "");
                val = val.replace(",", ".");
                Float val2 = (hmDll.get(key) == null) ? 0.f : hmDll.get(key);
                try {
                    Float val3 = Float.valueOf(val) + val2;
                    hmDll.put(key, val3);
                    hmJarArt.put(key, art);
                } catch (Exception e) {
                    System.err.println("Ошибка Main.compareIWin " + e);
                    continue;
                }
            }
            if (frame == true) {
                System.out.printf("%-64s%-24s%-16s%-16s%-16s", new Object[]{"Name", "Artikl", "Dll", "Jar", "Delta"});
                System.out.println();
                for (Map.Entry<String, Float> entry : hmDll.entrySet()) {
                    String key = entry.getKey();
                    Float val1 = entry.getValue();
                    Float val2 = (hmJar.get(key) == null) ? 0.f : hmJar.get(key);
                    hmJar.remove(key);
                    System.out.printf("%-64s%-24s%-16.2f%-16.2f%-16.2f", new Object[]{key, hmJarArt.get(key), val1, val2, Math.abs(val1 - val2)});
                    System.out.println();
                    jarTotal = jarTotal + val2;
                    iwinTotal = iwinTotal + val1;
                }
                System.out.println();
                if (hmJar.isEmpty() == false) {
                    System.out.printf("%-72s%-24s%-20s", new Object[]{"Name", "Artikl", "Value"});
                }
                System.out.println();
                for (Map.Entry<String, Float> entry : hmJar.entrySet()) {
                    String key = entry.getKey();
                    Float value3 = entry.getValue();
                    System.out.printf("%-72s%-24s%-16.2f", "Лишние: " + key, hmJarArt.get(key), value3);
                    System.out.println();
                    jarTotal = jarTotal + value3;
                }
            } else {
                for (Map.Entry<String, Float> entry : hmDll.entrySet()) {
                    String key = entry.getKey();
                    Float val1 = entry.getValue();
                    Float val2 = (hmJar.get(key) == null) ? 0.f : hmJar.get(key);
                    hmJar.remove(key);
                    jarTotal = jarTotal + val2;
                    iwinTotal = iwinTotal + val1;
                }
                for (Map.Entry<String, Float> entry : hmJar.entrySet()) {
                    String key = entry.getKey();
                    Float value3 = entry.getValue();
                    jarTotal = jarTotal + value3;
                }
            }
            System.out.printf("%-18s%-18s%-18s%-12s", "Prj=" + prj, "iwin=" + String.format("%.2f", iwinTotal), "jar="
                    + String.format("%.2f", jarTotal), "dx=" + String.format("%.2f", Math.abs(iwinTotal - jarTotal)));
            System.out.println();

        } catch (Exception e2) {
            System.err.println("Ошибка Main.compareIWin " + e2);
        }
    }
}
