package builder;

import builder.model.ElemGlass;
import builder.model.AreaStvorka;
import builder.model.AreaArch;
import builder.model.AreaTriangl;
import builder.model.ElemJoining;
import builder.model.AreaSimple;
import builder.model.AreaRectangl;
import builder.model.AreaTrapeze;
import builder.model.ElemCross;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataset.Record;
import domain.eArtikl;
import domain.eSyssize;
import domain.eSyspar1;
import domain.eSysprof;
import enums.UseArtiklTo;
import builder.making.Cal5e;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.util.Collections;
import java.util.HashMap;
import builder.making.Specific;
import builder.making.Tariffic;
import builder.making.Joining;
import builder.making.Elements;
import builder.making.Filling;
import builder.making.Furniture;
import builder.model.AreaDoor;
import builder.model.Com5t;
import builder.model.ElemFrame;
import builder.model.ElemSimple;
import builder.script.GsonRoot;
import builder.script.GsonElem;
import common.ArrayList2;
import common.LinkedList2;
import common.UCom;
import enums.Form;
import enums.Type;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import static java.util.stream.Collectors.toList;
import javax.swing.ImageIcon;

public class Wincalc {

    public Connection conn;
    public Integer nuni = 0;
    public Record artiklRec = null; //главный артикул системы профилей   
    public Record syssizeRec = null; //константы    
    public float genId = 0; //генерация ключа в спецификации

    private String script = null;
    private float width1 = 0; //ширина окна верхняя    
    private float width2 = 0; //ширина окна нижняя
    private float height1 = 0; //высота окна левая
    private float height2 = 0; //высота окна правая
    public int colorID1 = -1; //базовый цвет
    public int colorID2 = -1; //внутренний цвет
    public int colorID3 = -1; //внещний цвет
    private float costpric1 = 0; //себест. за ед. без отхода     
    private float costpric2 = 0; //себест. за ед. с отходом
    private float price = 0; //стоимость без скидки
    private float cost2 = 0; //стоимость с технологической скидкой
    private float cost3 = 0; //стоимость со скидкой менеджера/диллера
    private float weight = 0; //масса конструкции 

    public BufferedImage bufferImg = null;  //образ рисунка
    public ImageIcon imageIcon = null; //рисунок конструкции
    public Graphics2D gc2d = null; //графический котекст рисунка  
    public double scale = 1; //коэффициент сжатия

    public AreaSimple rootArea = null; //главное окно кострукции
    public GsonRoot rootGson = null; //главное окно кострукции в формате gson
    public Form form = null; //форма контура (параметр в развитии) 

    public HashMap<Integer, Record> mapPardef = new HashMap(); //пар. по умолчанию + наложенные пар. клиента
    public LinkedList2<AreaSimple> listArea = new LinkedList2(); //список AreaSimple
    public LinkedList2<ElemSimple> listElem = new LinkedList2(); //список ElemSimple
    public LinkedList2<Com5t> listAll = new LinkedList2(); //список всех компонентов
    public HashMap<String, ElemJoining> mapJoin = new HashMap(); //список соединений рам и створок 
    public ArrayList2<Specific> listSpec = new ArrayList2(); //спецификация
    public Cal5e calcJoining, calcElements, calcFilling, calcFurniture, calcTariffication; //объекты калькуляции конструктива

    public Wincalc() {
    }

    public Wincalc(String script) {
        this.script = script;
        build(script);
    }

    public AreaSimple build(String script) {
        try {
            genId = 0;
            height2 = 0.f;
            //List.of().forEach(el -> el=0);
            List.of((List) listArea, (List) listElem, (List) listSpec, (List) listAll).forEach(el -> el.clear());
            List.of(mapPardef, mapJoin).forEach(el -> el.clear());

            //Парсинг входного скрипта
            parsing(script);

            rootArea.joining(); //соединения ареа
            listArea.stream().filter(el -> el.type() == Type.STVORKA).collect(toList()).forEach(el -> el.joining()); //соединения створок
            listElem.forEach(it -> it.setSpecific()); //спецификация профилей

        } catch (Exception e) {
            System.err.println("Ошибка:Wincalc.build() " + e);
        }
        return rootArea;
    }

    // Парсим входное json окно и строим объектную модель окна
    private void parsing(String script) {
        try {
            //System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(new com.google.gson.JsonParser().parse(script))); //для тестирования
            //System.out.println(new GsonBuilder().create().toJson(new com.google.gson.JsonParser().parse(script))); //для тестирования
            Gson gson = new GsonBuilder().create();
            rootGson = gson.fromJson(script, GsonRoot.class);

            //Назначить родителей всем детям,  и
            //для быстрого доступа поднять elem.form до Wincalc.form
            rootGson.parent(this);

            //Инит конструктива
            this.nuni = rootGson.nuni();
            this.width1 = (rootGson.width1() == null)
                    ? rootGson.width() : rootGson.width1();
            this.width2 = rootGson.width2();
            this.height1 = rootGson.height1();
            this.height2 = (rootGson.height2() == null)
                    ? rootGson.height() : rootGson.height2();
            this.colorID1 = rootGson.color1();
            this.colorID2 = rootGson.color2();
            this.colorID3 = rootGson.color3();
            this.artiklRec = eArtikl.find(eSysprof.find2(nuni, UseArtiklTo.FRAME).getInt(eSysprof.artikl_id), true);
            this.syssizeRec = eSyssize.find(artiklRec);
            eSyspar1.find(nuni).stream().forEach(syspar1Rec -> mapPardef.put(syspar1Rec.getInt(eSyspar1.params_id), syspar1Rec)); //загрузим параметры по умолчанию

            //Главное окно
            if (Type.RECTANGL == rootGson.type()) {
                rootArea = new AreaRectangl(this); //простое
            } else if (Type.DOOR == rootGson.type()) {
                rootArea = new AreaDoor(this); //дверь                
            } else if (Type.TRAPEZE == rootGson.type()) {
                rootArea = new AreaTrapeze(this); //трапеция
            } else if (Type.TRIANGL == rootGson.type()) {
                rootArea = new AreaTriangl(this); //треугольник
            } else if (Type.ARCH == rootGson.type()) {
                rootArea = new AreaArch(this); //арка
            }

            //Создадим элементы конструкции
            elements(rootArea, rootGson);

        } catch (Exception e) {
            System.err.println("Ошибка:Wincalc.parsing() " + e);
        }
    }

    private void elements(AreaSimple owner, GsonElem gson) {
        try {
            LinkedHashMap<AreaSimple, GsonElem> hm = new LinkedHashMap();
            for (GsonElem el : gson.childs()) {

                if (Type.STVORKA == el.type()) {
                    AreaSimple area5e = new AreaStvorka(Wincalc.this, owner, el);
                    owner.childs.add(area5e);
                    hm.put(area5e, el);

                } else if (Type.AREA == el.type() || Type.ARCH == el.type() || Type.TRAPEZE == el.type()) {
                    AreaSimple area5e = (el.form() == null)
                            ? new AreaSimple(Wincalc.this, owner, el, el.width(), el.height())
                            : new AreaSimple(Wincalc.this, owner, el, el.width(), el.height(), el.form());
                    owner.childs.add(area5e);
                    hm.put(area5e, el);

                } else if (Type.FRAME_SIDE == el.type()) {
                    rootArea.frames.put(el.layout(), new ElemFrame(rootArea, el));

                } else if (Type.IMPOST == el.type() || Type.SHTULP == el.type() || Type.STOIKA == el.type()) {
                    owner.childs.add(new ElemCross(owner, el));

                } else if (Type.GLASS == el.type()) {
                    owner.childs.add(new ElemGlass(owner, el));
                }
            }

            //Теперь вложенные элементы
            for (Map.Entry<AreaSimple, GsonElem> entry : hm.entrySet()) {
                elements(entry.getKey(), entry.getValue());
            }

        } catch (Exception e) {
            System.err.println("Ошибка:Wincalc.elements() " + e);
        }
    }

    //Конструктив и тарификация 
    public void constructiv(boolean norm_otx) {
        try {
            calcJoining = new Joining(this); //соединения
            calcJoining.calc();
            calcElements = new Elements(this); //составы
            calcElements.calc();
            calcFilling = new Filling(this); //заполнения
            calcFilling.calc();
            calcFurniture = new Furniture(this); //фурнитура 
            calcFurniture.calc();
            calcTariffication = new Tariffic(this, norm_otx); //тарификация
            calcTariffication.calc();

            for (ElemSimple elemRec : listElem) {
                if (elemRec.spcRec.artikl.trim().charAt(0) != '@') {
                    listSpec.add(elemRec.spcRec);
                }
                for (Specific specific : elemRec.spcRec.spcList) {
                    if (specific.artikl.trim().charAt(0) != '@') {
                        listSpec.add(specific);
                    }
                }
            }
            Collections.sort(listSpec, (o1, o2) -> (o1.place.subSequence(0, 3) + o1.name + o1.width).compareTo(o2.place.subSequence(0, 3) + o2.name + o2.width));

        } catch (Exception e) {
            System.err.println("Ошибка:Wincalc.constructiv(" + e);
        }
    }

    public String script() {
        return this.script;
    }

    public float width() {
        return (width1 > width2) ? width1 : width2;
    }

    public float height() {
        return (height1 > height2) ? height1 : height2;
    }

    public float width1() {
        return width1;
    }

    public float width2() {
        return width2;
    }

    public float height1() {
        return height1;
    }

    public float height2() {
        return height2;
    }
    
    public float weight() {
        return weight;
    }

    public void weight(float weight) {
        this.weight = weight;
    }
    
    public float cost(int index) {
        return (index == 1) ? price : (index == 2) ? cost2 : cost3;
    }

    public void cost1(float cost1) {
        this.price = cost1;
    }

    public void cost2(float cost2) {
        this.cost2 = cost2;
    }

    public void totalCalc() {
        for (Specific spc : listSpec) {
            costpric1 += spc.costpric1;
            costpric2 += spc.costpric2;
            price += spc.price;
            cost2 += spc.costs;
        }
    }

    public float cost3() {
        return cost3;
    }

    public float getSquare() {
        return width() * height() / 1000000;
    }

    public float getWeight() {
        LinkedList<ElemGlass> glassList = UCom.listSortObj(listElem, Type.GLASS);
        float weight = 0;
        for (ElemGlass el : glassList) {
            weight += el.artiklRecAn.getFloat(eArtikl.density) * getSquare();
        }
        return weight;
    }
}
