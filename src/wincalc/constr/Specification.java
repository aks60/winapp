package wincalc.constr;

import dataset.Record;
import domain.eArtikl;
import domain.eColor;
import enums.UseUnit;
import enums.TypeArtikl1;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import wincalc.model.Com5t;

/**
 * Спецификация элемента окна
 */
public class Specification {

    protected Com5t owner = null; //элемент пораждающий спецификацию
    public ArrayList<Specification> specificationList = new ArrayList(); //список составов, фарнитур и т.д.
    private HashMap<Integer, String> mapParam = null; //параметры спецификации
    public Record artiklRec = null; //профиль в спецификации

    public float id = -1;  //ID
    public String areaId = "0"; //ID area
    public String elemId = "0"; //ID elem
    public String elemType = "0"; //TypeElem
    public String element = "-"; //Расположение
    public String artikl = "-";  //Артикул
    public String name = "-";  //Наименование
    public int color1 = 1005;  //Текстура
    public int color2 = 1005;  //Внутренняя
    public int color3 = 1005;  //Внешняя
    public float width = 0;  //Длина
    public float height = 0;  //Ширина
    public float weight = 0;  //масса
    public float anglCut1 = 0;  //Угол1
    public float anglCut2 = 0;  //Угол2
    public int count = 1;  //Кол. единиц
    public int unit = 0; //Ед.изм
    public float quantity = 0; //Количество без отхода
    public float wastePrc = 0;  //Процент отхода
    public float quantity2 = 0;  //Количество с отходом
    public float inPrice = 0;  //Собес-сть за ед. изм.
    public float outPrice = 0;  //Собес-сть за злемент с отходом
    public float inCost = 0; //Стоимость без скидки
    public float outCost = 0; //Стоимость со скидкой
    public float discount = 0;  //Скидка
    public float anglHoriz = 0; // Угол к горизонту

    /**
     * Конструктор для видимых эдементов окна
     *
     * @param id
     * @param com5t
     */
    public Specification(float id, Com5t com5t) {
        this.id = id;
        this.owner = com5t;
        this.mapParam = new HashMap();
    }

    //Конструктор для элементов спецификации окна
    public Specification(Record artiklRec, Com5t com5t, HashMap<Integer, String> hmParam) {
        this.id = com5t.iwin().specId;
        this.owner = com5t;
        this.mapParam = hmParam;
        setArtiklRec(artiklRec);
    }

    public Specification(Specification spec) {
        this.id = ++spec.owner.iwin().specId;
        this.element = spec.element;
        this.artikl = spec.artikl;
        this.name = spec.name;
        this.color1 = spec.color1;
        this.color2 = spec.color2;
        this.color3 = spec.color3;
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
        this.artiklRec = spec.artiklRec;
    }

    public Vector getVector() {

        List list = Arrays.asList(id, areaId, elemId, elemType, element, artikl, name, color1, color2,
                color3, width, height, weight, anglCut1, anglCut2, count, unit, quantity, wastePrc,
                quantity2, inPrice, outPrice, inCost, outCost, discount, anglHoriz);
        return new Vector(list);
    }

    public void setArtiklRec(Record artiklRec) {
        this.artikl = artiklRec.getStr(eArtikl.code);
        this.name = artiklRec.getStr(eArtikl.name);
        this.wastePrc = artiklRec.getFloat(eArtikl.otx_norm);
        this.unit = artiklRec.getInt(eArtikl.currenc_id); //atypi;
        this.artiklRec = artiklRec;
        setAnglCut();
        //this.height = artiklRec.aheig; //TODO парадокс добавления ширины, надо разобраться
    }

    public void setColor(int color1, int color2, int color3) {
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
    }

    //TODO ВАЖНО необходимо по умолчанию устонавливать colorNone = 1005 - без цвета
    public void setColor(Com5t com5t, Record paramRec) {
//        color1 = determineColorCodeForArt(com5t, 1, paramRec, this);
//        color2 = determineColorCodeForArt(com5t, 2, paramRec, this);
//        color3 = determineColorCodeForArt(com5t, 3, paramRec, this);
    }

    protected void setAnglCut() {
        //TODO Тут логическая ошибка
        if (TypeArtikl1.FURNITURA.isType(artiklRec)
                || TypeArtikl1.KONZEVPROF.isType(artiklRec)
                || TypeArtikl1.MONTPROF.isType(artiklRec)
                || TypeArtikl1.FIKSPROF.isType(artiklRec)) {
            anglCut2 = 90;
            anglCut1 = 90;

        } else if (TypeArtikl1.FURNITURA.isType(artiklRec)) {
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

    @Override
    public boolean equals(Object specification) {
        Specification spec = (Specification) specification;

        return (id == spec.id && element.equals(spec.element) && artikl.equals(spec.artikl) && name.equals(spec.name)
                && color1 == spec.color1 && color2 == spec.color2 && color3 == spec.color3
                && width == spec.width && height == spec.height && anglCut2 == spec.anglCut2 && anglCut1 == spec.anglCut1
                && quantity == spec.quantity && unit == spec.unit && wastePrc == spec.wastePrc && quantity2 == spec.quantity2
                && inPrice == spec.inPrice && outPrice == spec.outPrice && discount == spec.discount);
    }

    @Override
    public String toString() {
        Formatter f = new Formatter();
        return "Изделие=" + id + ", Расп...=" + element + ", Артикул=" + artikl + ", Наименование=" + name + ", Текстура=" + color1 + ", Внутренняя=" + color2
                + ", Внешняя=" + color3 + ", Длина. мм=" + f.format("%.1f", width) + ", Ширина. мм=" + f.format("%.1f", height) + ", Угол1=" + String.format("%.2f", anglCut2)
                + ", Угол2=" + String.format("%.2f", anglCut1) + ", Кол.шт=" + count + ", Кол.без.отх=" + quantity + ", Отход=" + wastePrc + ", Кол.с.отх=" + quantity2
                + ", Собест.за.ед" + inPrice + ", Собест.с.отх" + outPrice + ", Скидка=" + discount;
    }

    public static void write_csv(ArrayList<Specification> spcList) {
        Writer writer = null;
        try {
            File file = new File("C:\\Java\\IWinCalc\\out\\Specification.csv.");
            writer = new BufferedWriter(new FileWriter(file));

            writer.write(new String(("TEST Изделие, Элемент, Артикул, Наименование, Текстура, Внутренняя, Внешняя, Длина. мм, "
                    + "Ширина. мм, Угол1, Угол2, Количество, Погонаж, Ед.изм, Ед.изм, Скидка, Скидка").getBytes("windows-1251"), "UTF-8"));
            for (Specification spc : spcList) {

                String str = spc.id + "," + spc.element + "," + spc.artikl + "," + spc.name + "," + spc.color1 + "," + spc.color2 + "," + spc.color3
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

    public static void write_txt(ArrayList<Specification> specList) {
        int npp = 0;
        String format = "%-6s%-46s%-32s%-32s%-32s%-32s%-16s%-16s%-16s%-16s%-16s%-16s%-16s%-16s%-16s%-16s"
                + "%-16s%-16s%-16s%-16s%-16s%-16s%-16s%-16s%-16s%-16s%-16s%-16s%-16s%-16s%-16s %n";
        Object str[] = {"Code", "Name", "Art", "BaseColor", "InsideColor", "OutsideColor", "Count", "Quantity",
            "UM", "InPrice", "CostPrice", "OutPrice", "OutTotal", "Width", "Height", "Weight",
            "Angle", "ComplType", "ElemID", "ElemType", "ObjectID", "ObjectType", "AreaID", "AreaType",
            "AccessoryID", "PriceGRP", "PrintGroup", "CutAngle1", "CutAngle2", "Composite", "Усл.окна"};
        String str3 = new String(("Спецификация (" + specList.size() + " строк):").getBytes());
        System.out.printf(format, str);
        for (Specification s : specList) {

            Object str2[] = {String.valueOf(++npp), s.name, s.artikl,
                eColor.find(s.color1).getInt(eColor.name),
                eColor.find(s.color2).getInt(eColor.name),
                eColor.find(s.color3).getInt(eColor.name),
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

        Specification.sort(specList);
        int npp = 0;
        String format = "%-6s%-46s%-26s%-12s%-12s%-12s";
        Object str[] = {"Code", "Name", "Art", "areaId", "elemId", "elemType"};
        System.out.printf(format, str);
        System.out.println();
        float total = 0;
        for (Specification s : specList) {
            Object str2[] = {String.valueOf(++npp), s.name, s.artikl,
                s.areaId, s.elemId, s.elemType};
            total = total + s.weight;
            System.out.printf(format, str2);
            System.out.println();
        }
        System.out.println("Масса окна " + total + " кг.");
    }

    public static void sort(ArrayList<Specification> contacts) {
        Collections.sort(contacts, new Comparator<Specification>() {
            public int compare(Specification one, Specification other) {
                return one.name.compareTo(other.name);
            }
        });
    }

    public static void sort2(ArrayList<Specification> contacts) {
        Collections.sort(contacts, new Comparator<Specification>() {
            public int compare(Specification one, Specification other) {
                return (one.element + one.name).compareTo(other.element + other.name);
            }
        });
    }

    public static void sort3(ArrayList<Specification> contacts) {
        Collections.sort(contacts, new Comparator<Specification>() {
            public int compare(Specification one, Specification other) {
                return (one.artikl + one.element).compareTo(other.artikl + other.element);
            }
        });
    }

    public static ArrayList<Specification> group(ArrayList<Specification> specificationList1) {

        HashMap<String, Specification> hm = new HashMap();
        for (Specification spc : specificationList1) {

            String key = spc.id + spc.element + spc.artikl + spc.name + spc.color1 + spc.color2 + spc.color3
                    + spc.width + spc.height + spc.anglCut2 + spc.anglCut1 + spc.unit + spc.quantity + spc.wastePrc
                    + spc.wastePrc + spc.quantity2 + spc.inPrice + spc.outPrice + spc.discount;
            Specification spc2 = hm.put(key, spc);
            if (spc2 != null) {
                Specification spc3 = hm.get(key);
                spc3.count = spc3.count + spc2.count; //TODO тут надо увеличивать и стоимост за кол. элементов
            }

        }
        ArrayList<Specification> specificationList2 = new ArrayList();
        for (Map.Entry<String, Specification> entry : hm.entrySet()) {
            specificationList2.add(entry.getValue());
        }
        return specificationList2;
    }
}
