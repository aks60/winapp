package builder;

import builder.model.ElemJoining;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataset.Record;
import domain.eArtikl;
import domain.eSyspar1;
import builder.making.Cal5e;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashMap;
import builder.making.Specific;
import builder.making.Tariffic;
import builder.making.Joining;
import builder.making.Elements;
import builder.making.Filling;
import builder.making.Furniture;
import builder.script.GsonRoot;
import builder.script.GsonElem;
import com.google.gson.JsonSyntaxException;
import common.ArrayList2;
import common.LinkedList2;
import common.eProp;
import enums.Form;
import enums.Type;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toList;
import javax.swing.ImageIcon;

public class Wincalc {

    private Integer nuni = 0;
    private Record syssizeRec = null; //системные константы    
    private float genId = 0; //для генерация ключа в спецификации

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
    private float weight = 0; //масса конструкции 

    public BufferedImage bufferImg = null;  //образ рисунка
    public ImageIcon imageIcon = null; //рисунок конструкции
    public Graphics2D gc2d = null; //графический котекст рисунка  
    public double scale = 1; //коэффициент сжатия

    public IArea5e rootArea = null; //главное окно кострукции
    public GsonRoot rootGson = null; //главное окно кострукции в формате gson
    public Form form = null; //форма контура (параметр в развитии) 

    public HashMap<Integer, Record> mapPardef = new HashMap(); //пар. по умолчанию + наложенные пар. клиента
    public LinkedList2<IArea5e> listArea = new LinkedList2(); //список ареа
    public LinkedList2<IElem5e> listElem = new LinkedList2(); //список элем.
    public LinkedList2<ICom5t> listAll = new LinkedList2(); //список всех компонентов (area + elem)
    public HashMap<String, ElemJoining> mapJoin = new HashMap(); //список соединений рам и створок 
    public ArrayList2<Specific> listSpec = new ArrayList2(); //спецификация
    public ArrayList2<Specific> kitsSpec = new ArrayList2(); //не использую
    public Cal5e calcJoining, calcElements, calcFilling, calcFurniture, calcTariffication; //объекты калькуляции конструктива

    public Wincalc() {
    }

    public Wincalc(String script) {
        this.script = script;
        build(script);
    }

    public IArea5e build(String script) {
        try {
            this.script = script;
            //Обнуление
            genId = 0;
            width1 = 0;
            width2 = 0;
            height1 = 0;
            height2 = 0;
            syssizeRec = null;
            List.of((List) listArea, (List) listElem, (List) listSpec, (List) kitsSpec, (List) listAll).forEach(el -> el.clear());
            List.of(mapPardef, mapJoin).forEach(el -> el.clear());

            //Парсинг входного скрипта
            parsing(script);

            //Все соединения вычисляются в классах AreaRoot.joining()=> AreaSimple.joining() и AreaStvorka.joining()
            rootArea.joining(); //соединения ареа
            listArea.stream().filter(area -> area.type() == Type.STVORKA).collect(toList()).forEach(elem -> elem.joining()); //соединения створок
            //Каждый элемент конструкции попадает в спецификацию через функцию setSpecific()            
            listElem.forEach(elem -> elem.setSpecific()); //спецификация ведущих элементов конструкции

        } catch (Exception e) {
            System.err.println("Ошибка:Wincalc.build() " + e);
        }
        return rootArea;
    }

    // Парсим входное json окно и строим объектную модель окна
    private void parsing(String script) {
        try {
            //******************************************************************
            System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(new com.google.gson.JsonParser().parse(script)));  //для тестирования
            //******************************************************************

            Gson gson = new GsonBuilder().create();
            rootGson = gson.fromJson(script, GsonRoot.class);

            //Назначить родителей т.к. gson.fromJson() это делать не будет,
            //для быстрого доступа поднять elem.form до Wincalc.form
            rootGson.setOwner(this);

            //Инит конструктива
            this.nuni = rootGson.nuni();
            this.width1 = (rootGson.width1() == null)
                    ? rootGson.width() : rootGson.width1();
            this.width2 = rootGson.width2(); //ширина в основании всегда задана
            this.height1 = rootGson.height1(); //высота слева всегда задана
            this.height2 = (rootGson.height2() == null)
                    ? rootGson.height() : rootGson.height2();
            this.colorID1 = rootGson.color1();
            this.colorID2 = rootGson.color2();
            this.colorID3 = rootGson.color3();
            eSyspar1.find(nuni).stream().forEach(syspar1Rec -> mapPardef.put(syspar1Rec.getInt(eSyspar1.params_id), syspar1Rec)); //загрузим параметры по умолчанию

            //Главное окно
            if (Type.RECTANGL == rootGson.type()) {
                rootArea = (eProp.old.read().equals("0"))
                        ? new builder.model.AreaRectangl(this)
                        : new builder.model.AreaRectangl(this); //простое
            } else if (Type.DOOR == rootGson.type()) {
                rootArea = (eProp.old.read().equals("0"))
                        ? new builder.model.AreaDoor(this)
                        : new builder.model.AreaDoor(this); //дверь                
            } else if (Type.TRAPEZE == rootGson.type()) {
                rootArea = (eProp.old.read().equals("0"))
                        ? new builder.model.AreaTrapeze(this)
                        : new builder.model.AreaTrapeze(this); //трапеция
            } else if (Type.TRIANGL == rootGson.type()) {
                rootArea = (eProp.old.read().equals("0"))
                        ? new builder.model.AreaTriangl(this)
                        : new builder.model.AreaTriangl(this); //треугольник
            } else if (Type.ARCH == rootGson.type()) {
                rootArea = (eProp.old.read().equals("0"))
                        ? new builder.model.AreaArch(this)
                        : new builder.model.AreaArch(this); //арка
            }

            //Создадим элементы конструкции
            elements(rootArea, rootGson);

        } catch (JsonSyntaxException e) {
            System.err.println("Ошибка:Wincalc.parsing() " + e);
        }
    }

    private void elements(IArea5e owner, GsonElem gson) {
        try {
            LinkedHashMap<IArea5e, GsonElem> hm = new LinkedHashMap();
            for (GsonElem js : gson.childs()) {

                if (Type.STVORKA == js.type()) {
                    IArea5e area5e = (eProp.old.read().equals("0"))
                            ? new builder.model.AreaStvorka(Wincalc.this, owner, js)
                            : new builder.model.AreaStvorka(Wincalc.this, owner, js);
                    owner.childs().add(area5e);
                    hm.put(area5e, js);

                    //AreaSimple может принимать форму арки, трапеции. см. AreaSimple.type(). 
                    //Элементы окна ограничены этим ареа и формой контура.
                } else if (Type.AREA == js.type() || Type.ARCH == js.type() || Type.TRAPEZE == js.type()) {
                    IArea5e area5e = null;
                    if (js.form() == null) {
                        area5e = (eProp.old.read().equals("0"))
                                ? new builder.model.AreaSimple(Wincalc.this, owner, js, js.width(), js.height())
                                : new builder.model.AreaSimple(Wincalc.this, owner, js, js.width(), js.height());
                    } else {
                        area5e = (eProp.old.read().equals("0"))
                                ? new builder.model.AreaSimple(Wincalc.this, owner, js, js.width(), js.height(), js.form())
                                : new builder.model.AreaSimple(Wincalc.this, owner, js, js.width(), js.height(), js.form());
                    }
                    owner.childs().add(area5e);
                    hm.put(area5e, js);

                } else if (Type.FRAME_SIDE == js.type()) {
                    IElem5e elem5e = (eProp.old.read().equals("0"))
                            ? new builder.model.ElemFrame(rootArea, js)
                            : new builder.model.ElemFrame(rootArea, js);
                    rootArea.frames().put(js.layout(), elem5e);

                } else if (Type.IMPOST == js.type() || Type.SHTULP == js.type() || Type.STOIKA == js.type()) {
                    IElem5e elem5e = (eProp.old.read().equals("0"))
                            ? new builder.model.ElemCross(owner, js)
                            : new builder.model.ElemCross(owner, js);
                    owner.childs().add(elem5e);

                } else if (Type.GLASS == js.type()) {
                    IElem5e elem5e = (eProp.old.read().equals("0"))
                            ? new builder.model.ElemGlass(owner, js)
                            : new builder.model.ElemGlass(owner, js);
                    owner.childs().add(elem5e);

                }
            }

            //Теперь вложенные элементы
            for (Map.Entry<IArea5e, GsonElem> entry : hm.entrySet()) {
                elements(entry.getKey(), entry.getValue());
            }

        } catch (Exception e) {
            System.err.println("Ошибка:Wincalc.elements() " + e);
        }
    }

    //Конструктив и тарификация 
    public void constructiv(boolean norm_otx) {
        weight = 0;
        price = 0;
        cost2 = 0;
        //cost3 = 0;
        try {
            //Детали элемента через конструктив попадают в спецификацию через функцию addSpecific();
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

            //Построим список спецификаций
            for (IElem5e elem5e : listElem) {
                if (elem5e.spcRec().artikl.isEmpty() || elem5e.spcRec().artikl.trim().charAt(0) != '@') {
                    listSpec.add(elem5e.spcRec());
                    elem5e.spcRec().spcList.forEach(spc -> listSpec.add(spc));
                }
            }

            //Итоговая стоимость
            for (Specific spc : listSpec) {
                this.price(this.price() + spc.price); //общая стоимость без скидки
                this.cost2(this.cost2() + spc.cost2); //общая стоимость со скидкой             
            }

            //Вес изделия
            LinkedList<IElem5e> glassList = listElem.filter(Type.GLASS);
            for (IElem5e el : glassList) {
                this.weight += el.artiklRecAn().getFloat(eArtikl.density) * el.width() * el.height() / 1000000; //уд.вес * площадь = вес
            }

            Collections.sort(listSpec, (o1, o2) -> (o1.place.subSequence(0, 3) + o1.name + o1.width).compareTo(o2.place.subSequence(0, 3) + o2.name + o2.width));

        } catch (Exception e) {
            System.err.println("Ошибка:Wincalc.constructiv() " + e);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="GET AND SET"> 
    public float genId() {
        return ++genId;
    }

    public Integer nuni() {
        return nuni;
    }

    public void syssizeRec(Record syssize) {
        this.syssizeRec = syssize;
    }

    public Record syssizeRec() {
        return syssizeRec;
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

    public float price() {
        return this.price;
    }

    public void price(float price) {
        this.price = price;
    }

    public float cost2() {
        return this.cost2;
    }

    public void cost2(float cost2) {
        this.cost2 = cost2;
    }

    public float square() {
        return width() * height() / 1000000;
    }
    // </editor-fold>     
}
