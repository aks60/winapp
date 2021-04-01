package builder.specif;

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
import builder.model.Com5t;
import builder.model.ElemSimple;
import domain.eSetting;
import java.util.LinkedHashMap;
import jxl.Sheet;
import jxl.Workbook;

/**
 * Спецификация элемента окна
 */
public class Specification {

    public ArrayList<Specification> spcList = new ArrayList(); //список составов, фарнитур и т.д.
    public HashMap<Integer, String> mapParam = null; //параметры спецификации
    public ElemSimple elem5e = null; //элемент пораждающий спецификацию
    public Record artiklRec = null; //профиль в спецификации
    public Record detailRec = null; //профиль в спецификации

    public float id = -1; //ID
    public String place = "---"; //Место расмешения
    public String name = "-"; //Наименование
    public String artikl = "-";  //Артикул
    public int colorID1 = -1;  //Осн.текстура
    public int colorID2 = -1;  //Внутр.текстура
    public int colorID3 = -1;  //Внешн.текстура
    public float width = 0;  //Длина
    public float height = 0;  //Ширина
    public float weight = 0;  //Масса
    public float anglCut1 = 0;  //Угол1
    public float anglCut2 = 0;  //Угол2
    public float anglHoriz = 0; // Угол к горизонту    
    public int count = 1;  //Кол. единиц
    public int unit = 0; //Ед.изм
    public float wastePrc = 0;  //Процент отхода
    
    public float quant1 = 0;     //Количество без отхода
    public float quant2 = 0;     //Количество с отходом
    public float price1 = 0;     //Себест. за ед. без отхода     
    public float price2 = 0;     //Себест. за ед с отходом
    public float cost1 = 0;      //Стоимость без скидки
    public float cost2 = 0;      //Стоимость со скидкой


    public Specification(float id, ElemSimple elem5e) {
        this.id = id;
        this.elem5e = elem5e;
        this.mapParam = new HashMap();
    }

    public Specification(Record artiklRec, ElemSimple elem5e, HashMap<Integer, String> mapParam) {
        this(null, artiklRec, elem5e, mapParam);
    }

    public Specification(Record deteilRec, Record artiklRec, ElemSimple elem5e, HashMap<Integer, String> mapParam) {
        this.id = ++elem5e.iwin().genId;
        this.elem5e = elem5e;
        this.mapParam = mapParam;
        this.detailRec = deteilRec;
        setArtiklRec(artiklRec);
    }

    public Specification(Specification spec) {
        this.id = spec.id; //++spec.elem5e.iwin().genId;
        this.place = spec.place;
        this.artikl = spec.artikl;
        this.detailRec = spec.detailRec;
        this.name = spec.name;
        this.colorID1 = spec.colorID1;
        this.colorID2 = spec.colorID2;
        this.colorID3 = spec.colorID3;
        this.width = spec.width;
        this.height = spec.height;
        this.weight = spec.weight;
        this.anglCut2 = spec.anglCut2;
        this.anglCut1 = spec.anglCut1;
        this.count = spec.count;
        this.unit = spec.unit;
        this.quant1 = spec.quant1;
        this.wastePrc = spec.wastePrc;
        this.quant2 = spec.quant2;
        this.price1 = spec.price1;
        this.price2 = spec.price2;
        this.cost1 = spec.cost1;
        this.cost2 = spec.cost2;
        this.anglHoriz = spec.anglHoriz;
        this.mapParam = spec.mapParam;
        this.elem5e = spec.elem5e;
        this.artiklRec = spec.artiklRec;
    }

    public Vector getVector(int npp) {
        List list = Arrays.asList(npp, id, place, artikl, name, eColor.find(colorID1).getStr(eColor.name), eColor.find(colorID2).getStr(eColor.name),
                eColor.find(colorID3).getStr(eColor.name), width, height, weight, anglCut1, anglCut2, anglHoriz,
                count, UseUnit.getName(unit), wastePrc, quant1, quant2, price1, price2, cost1, cost2
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

    protected void setAnglCut() {
        //TODO Тут логическая ошибка
        if (TypeArtikl.X109.isType(artiklRec)
                || TypeArtikl.X135.isType(artiklRec)
                || TypeArtikl.X117.isType(artiklRec)
                || TypeArtikl.X136.isType(artiklRec)) {
            anglCut2 = 90;
            anglCut1 = 90;

        } else if (TypeArtikl.X109.isType(artiklRec)) {
            anglCut2 = 0;
            anglCut1 = 0;
        }
    }

    public void putParam(Integer key, String val) {
        mapParam.put(key, val);
    }

    public int getParam(int key) {

        if (mapParam != null) {
            String str = mapParam.get(Integer.valueOf(key));
            if (str != null || str.isEmpty() == false) {
                return Integer.valueOf(str);
            }
        }
        return 0;
    }

    public String getParam(Object def, int... p) {

        if (mapParam != null) {
            for (int index = 0; index < p.length; ++index) {
                int key = p[index];
                String str = mapParam.get(Integer.valueOf(key));
                if (str != null) {
                    return str;
                }
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
                        + String.format("%.2f", spc.anglCut1) + spc.count + spc.width + spc.unit + "\n";

                str = new String(str.getBytes(), "windows-1251");

                writer.write(str);
            }
        } catch (Exception ex) {
            System.err.println("Ошибка:Specification.write_csv() " + ex);
        } finally {
            try {
                writer.flush();
                writer.close();
            } catch (Exception ex2) {
                System.err.println("Ошибка:Specification.write_csv() " + ex2);
            }
        }
    }

    public static void write_txt1(ArrayList<Specification> specList) {
        int npp = 0;
        String format = "%-6s%-42s%-24s%-18s%-18s%-18s%-8s%-8s%-16s%-16s%-16s%-16s%-16s%-16s%-16s%-16s"
                + "%-16s%-16s%-16s%-16s%-16s%-16s%-16s%-16s%-16s%-16s%-16s%-16s%-16s%-16s%-16s %n";
        Object str[] = {"Code", "Name", "Art", "Color1", "Color2", "Color3", "Count", "Quantity",
            "UM", "InPrice", "CostPrice", "OutPrice", "OutTotal", "Width", "Height", "Weight",
            "Angle", "ComplType", "ElemID", "elemType", "ObjectID", "ObjectType", "AreaID", "AreaType",
            "AccessoryID", "PriceGRP", "PrintGroup", "CutAngle1", "CutAngle2", "Composite", "Усл.окна"};
        String str3 = new String(("Спецификация (" + specList.size() + " строк):").getBytes());
        System.out.printf(format, str);
        for (Specification s : specList) {

            Object str2[] = {String.valueOf(++npp), s.name, s.artikl,
                eColor.find(s.colorID1).getStr(eColor.name),
                eColor.find(s.colorID2).getStr(eColor.name),
                eColor.find(s.colorID3).getStr(eColor.name),
                String.valueOf(s.count), String.valueOf(s.quant1),
                UseUnit.getName(s.unit), "0", String.valueOf(s.price1), String.valueOf(s.price2), String.valueOf(s.cost1),
                String.valueOf(s.width), String.valueOf(s.height), "0", "0", "0", String.valueOf(s.id), "0", "0", "0", "0", "0",
                "0", "0", "0", String.valueOf(s.anglCut2), String.valueOf(s.anglCut1), "0", "0"};
            System.out.printf(format, str2);
        }
        float totalVal = 0;
        for (Specification s : specList) {
            totalVal = totalVal + s.cost2;
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
                    s.elem5e.owner().id(), s.elem5e.id(), s.elem5e.spcRec.artiklRec.get(eArtikl.code), s.price1};
                total = total + s.weight;
                System.out.printf(format, str2);
                System.out.println();
            }
            System.out.println("Масса окна " + total + " кг.");
        } catch (Exception e) {
            System.err.println("Ошибка:Specification.write_txt2() " + e);
        }
    }
}
