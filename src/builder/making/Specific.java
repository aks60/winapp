package builder.making;

import builder.Wincalc;
import dataset.Record;
import domain.eArtikl;
import domain.eColor;
import enums.UseUnit;
import enums.TypeArtikl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import builder.model.ElemSimple;
import java.util.List;

/**
 * Спецификация элемента окна
 */
public class Specific {

    public ArrayList<Specific> spcList = new ArrayList();  //список составов, фурнитур и т.д.
    public HashMap<Integer, String> mapParam = null;  //параметры спецификации
    public ElemSimple elem5e = null;  //элемент пораждающий спецификацию (контейнер)
    public Record variantRec = null;  //вариант в конструктиве
    public Record detailRec = null;  //детализация в конструктиве
    public Record artiklRec = null;  //артикул в детализации конструктива

    public float id = -1; //ID
    public String place = "---";  //Место размешения
    public String name = "-";  //Наименование
    public String artikl = "-";  //Артикул
    public int colorID1 = -3;  //Осн.текстура
    public int colorID2 = -3;  //Внутр.текстура
    public int colorID3 = -3;  //Внешн.текстура
    public float width = 0;  //Длина
    public float height = 0;  //Ширина
    public float weight = 0;  //Масса
    public float anglCut1 = 0;  //Угол1
    public float anglCut2 = 0;  //Угол2
    public float anglHoriz = 0; // Угол к горизонту    
    public float count = 1;  //Кол. единиц

    public int unit = 0;  //Ед.изм   
    public float wastePrc = 0;  //Процент отхода    
    public float quant1 = 0;  //Количество без отхода
    public float quant2 = 0;  //Количество с отходом
    public float costpric1 = 0;  //Себест. за ед. без отхода     
    public float costpric2 = 0;  //Себест. за ед. с отходом
    public float price = 0;  //Стоимость без скидки
    public float cost2 = 0;  //Стоимость с технологической скидкой

    public Specific() {        
    }
    
    public Specific(float id, ElemSimple elem5e) {
        ++elem5e.winc.genId;
        this.id = id;
        this.elem5e = elem5e;
        this.mapParam = new HashMap();
    }

    public Specific(float id, Record detailRec, Record artiklRec, HashMap<Integer, String> mapParam) {
        this.id = id;
        this.mapParam = mapParam;
        this.detailRec = detailRec;
        setArtiklRec(artiklRec);
    }

    public Specific(Record detailRec, Record artiklRec, ElemSimple elem5e, HashMap<Integer, String> mapParam) {
        this.id = ++elem5e.winc.genId;
        this.elem5e = elem5e;
        this.mapParam = mapParam;
        this.detailRec = detailRec;
        setArtiklRec(artiklRec);
    }

    public Specific(Specific spec) {
        this.id = spec.id; //++spec.elem5e.winc.genId;
        this.place = spec.place;
        this.artikl = spec.artikl;
        this.artiklRec = spec.artiklRec;
        this.detailRec = spec.detailRec;
        this.name = spec.name;
        this.colorID1 = spec.colorID1;
        this.colorID2 = spec.colorID2;
        this.colorID3 = spec.colorID3;
        this.width = spec.width;
        this.height = spec.height;
        this.weight = spec.weight;
        this.anglCut1 = spec.anglCut1;
        this.anglCut2 = spec.anglCut2;
        this.count = spec.count;
        this.unit = spec.unit;
        this.quant1 = spec.quant1;
        this.wastePrc = spec.wastePrc;
        this.quant2 = spec.quant2;
        this.costpric1 = spec.costpric1;
        this.costpric2 = spec.costpric2;
        this.price = spec.price;
        this.cost2 = spec.cost2;
        this.anglHoriz = spec.anglHoriz;
        this.mapParam = spec.mapParam;
        this.elem5e = spec.elem5e;
    }

    public Vector getVector(int npp) {
        return new Vector(List.of(npp, id, elem5e.id(), place, artikl, name, eColor.find(colorID1).getStr(eColor.name), eColor.find(colorID2).getStr(eColor.name),
                eColor.find(colorID3).getStr(eColor.name), width, height, weight, anglCut1, anglCut2, anglHoriz,
                count, UseUnit.getName(unit), wastePrc, quant1, quant2, costpric1, costpric2, price, cost2));
    }

    public void setArtiklRec(Record artiklRec) {
        this.artikl = artiklRec.getStr(eArtikl.code);
        this.name = artiklRec.getStr(eArtikl.name);
        this.wastePrc = artiklRec.getFloat(eArtikl.otx_norm);
        this.unit = artiklRec.getInt(eArtikl.unit); //atypi;
        this.artiklRec = artiklRec;
        setAnglCut();
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

    public static void write_csv(ArrayList<Specific> spcList) {
        //См. историю
    }

    public static void write_txt(ArrayList<Specific> specList) {
        //См. историю
    }

    public String toString() {
        return artikl + " - " + name;
    }
}
