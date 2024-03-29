package builder;

import builder.IArea5e;
import builder.ICom5t;
import builder.IElem5e;
import builder.model1.ElemJoining;
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
import builder.making.Joining;
import builder.making.UColor;
import builder.script.GsonRoot;
import builder.script.GsonElem;
import com.google.gson.JsonSyntaxException;
import common.ArraySpc;
import common.ArrayJoin;
import common.LinkedCom;
import common.eProp;
import domain.eSysprof;
import enums.Form;
import enums.Type;
import enums.UseArtiklTo;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toList;
import javax.swing.ImageIcon;

public class Wincalc {

    private Gson gson = new GsonBuilder().create();
    private Integer nuni = 0;
    private Record syssizeRec = null; //системные константы    
    private double genId = 0; //для генерация ключа в спецификации

    private String script = null;
    private double width1 = 0; //ширина окна нижняя
    private double width2 = 0; //ширина окна верхняя    
    private double height1 = 0; //высота окна левая
    private double height2 = 0; //высота окна правая
    public int colorID1 = -1; //базовый цвет
    public int colorID2 = -1; //внутренний цвет
    public int colorID3 = -1; //внещний цвет
    private double costpric1 = 0; //себест. за ед. без отхода     
    private double costpric2 = 0; //себест. за ед. с отходом
    private double price = 0; //стоимость без скидки
    private double cost2 = 0; //стоимость с технологической скидкой
    private double weight = 0; //масса конструкции 
    public Form form = null; //форма контура (параметр в развитии)

    public BufferedImage bufferImg = null;  //образ рисунка
    public ImageIcon imageIcon = null; //рисунок конструкции
    public Graphics2D gc2d = null; //графический котекст рисунка  
    public double scale = 1; //коэффициент сжатия

    public GsonRoot rootGson = null; //объектная модель конструкции 1-го уровня
    public IArea5e rootArea = null; //объектная модель конструкции 2-го уровня

    private HashMap<Integer, Record> mapPardef = new HashMap(); //пар. по умолчанию + наложенные пар. клиента
    public LinkedCom<IArea5e> listArea = new LinkedCom(); //список ареа инит происх. в констр AreaSimple, ElemSimple 
    public LinkedCom<IElem5e> listElem = new LinkedCom(); //список элем.
    public LinkedCom<ICom5t> listAll = new LinkedCom(); //список всех компонентов (area + elem)
    public ArrayJoin listJoin = new ArrayJoin(); //список соединений рам и створок 
    public ArraySpc<Specific> listSpec = new ArraySpc(); //спецификация
    public Cal5e calcJoining, calcElements, calcFilling, calcFurniture, calcTariffication; //объекты калькуляции конструктива

    public Wincalc() {
    }

    public Wincalc(String script) {
        this.script = script;
        build(script);
    }

    /**
     * Построение окна из json скрипта
     *
     * @param script - json скрипт построения окна
     * @return rootArea - главное окно
     */
    public IArea5e build(String script) {
        this.script = script;
        try {
            //Инит свойств окна
            initProperty();

            //Парсинг входного скрипта
            //Создание элементов конструкции
            parsing(script);

            //Cоединения ареа           
            rootArea.joining();

            //Каждый элемент конструкции попадает в спецификацию через функцию setSpecific()            
            listElem.forEach(elem -> elem.setSpecific()); //спецификация ведущих элементов конструкции

        } catch (Exception e) {
            System.err.println("Ошибка:Wincalc.build() " + e);
        }
        return rootArea;
    }

    /**
     * Парсим входное json окно и строим объектную модель окна
     */
    private void parsing(String script) {
        //Для тестирования
        //System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(new com.google.gson.JsonParser().parse(script)));         
        try {

            //Конвертирование json скрипта конструкции 
            //в объектную модель 1 уров. java классов 
            rootGson = gson.fromJson(script, GsonRoot.class);

            //Назначить родителей т.к. Gson.fromJson() это не делает,
            //для быстрого доступа elem.form вверх до Wincalc.form
            rootGson.setOwnerAndForm(this);

            //Инит конструктива
            this.nuni = rootGson.nuni();
            this.width2 = (rootGson.width2() == null) ? rootGson.width() : rootGson.width2();
            this.width1 = rootGson.width1(); //ширина в основании всегда задана
            this.height1 = rootGson.height1(); //высота слева всегда задана
            this.height2 = (rootGson.height2() == null) ? rootGson.height() : rootGson.height2();
            Record sysprofRec = eSysprof.find2(nuni, UseArtiklTo.FRAME);
            this.colorID1 = (rootGson.color1() == -3) ? UColor.colorFromArtikl(sysprofRec.getInt(eSysprof.artikl_id)) : rootGson.color1();
            this.colorID2 = (rootGson.color2() == -3) ? UColor.colorFromArtikl(sysprofRec.getInt(eSysprof.artikl_id)) : rootGson.color2();
            this.colorID3 = (rootGson.color3() == -3) ? UColor.colorFromArtikl(sysprofRec.getInt(eSysprof.artikl_id)) : rootGson.color3();
            eSyspar1.find(nuni).forEach(syspar1Rec -> mapPardef.put(syspar1Rec.getInt(eSyspar1.groups_id), syspar1Rec)); //загрузим параметры по умолчанию

            //Главное окно
            if (Type.RECTANGL == rootGson.type()) {
                rootArea = (eProp.old.read().equals("0"))
                        ? new builder.model1.AreaRectangl(this)
                        : new builder.model1.AreaRectangl(this); //простое
            } else if (Type.DOOR == rootGson.type()) {
                rootArea = (eProp.old.read().equals("0"))
                        ? new builder.model1.AreaDoor(this)
                        : new builder.model1.AreaDoor(this); //дверь                
            } else if (Type.TRAPEZE == rootGson.type()) {
                rootArea = (eProp.old.read().equals("0"))
                        ? new builder.model1.AreaTrapeze(this)
                        : new builder.model1.AreaTrapeze(this); //трапеция
            } else if (Type.TRIANGL == rootGson.type()) {
                rootArea = (eProp.old.read().equals("0"))
                        ? new builder.model1.AreaTriangl(this)
                        : new builder.model1.AreaTriangl(this); //треугольник
            } else if (Type.ARCH == rootGson.type()) {
                rootArea = (eProp.old.read().equals("0"))
                        ? new builder.model1.AreaArch(this)
                        : new builder.model1.AreaArch(this); //арка
            }

            //Создадим ареа областей и элементы конструкции
            elements(rootArea, rootGson);

        } catch (JsonSyntaxException e) {
            System.err.println("Ошибка:Wincalc.parsing() " + e);
        }
    }

    /**
     * Создание ареа областей и элементов конструкции
     *
     * @param owner - родитель
     * @param gson - объектная модель 1 уровня
     */
    private void elements(IArea5e owner, GsonElem gson) {
        try {
            LinkedHashMap<IArea5e, GsonElem> hm = new LinkedHashMap();
            for (GsonElem js : gson.childs()) {

                if (Type.STVORKA == js.type()) {
                    IArea5e area5e = (eProp.old.read().equals("0"))
                            ? new builder.model1.AreaStvorka(Wincalc.this, owner, js)
                            : new builder.model1.AreaStvorka(Wincalc.this, owner, js);
                    owner.childs().add(area5e); //добавим ребёна родителю
                    hm.put(area5e, js);

                    //AreaSimple может принимать форму арки, трапеции. см. AreaSimple.type(). 
                    //Элементы окна ограничены этим ареа и формой контура.
                } else if (Type.AREA == js.type() || Type.ARCH == js.type() || Type.TRAPEZE == js.type()) {
                    IArea5e area5e = null;
                    if (js.form() == null) {
                        area5e = (eProp.old.read().equals("0"))
                                ? new builder.model1.AreaSimple(Wincalc.this, owner, js, js.width(), js.height())
                                : new builder.model1.AreaSimple(Wincalc.this, owner, js, js.width(), js.height());
                    } else {
                        area5e = (eProp.old.read().equals("0"))
                                ? new builder.model1.AreaSimple(Wincalc.this, owner, js, js.width(), js.height(), js.form())
                                : new builder.model1.AreaSimple(Wincalc.this, owner, js, js.width(), js.height(), js.form());
                    }
                    owner.childs().add(area5e); //добавим ребёна родителю
                    hm.put(area5e, js);

                } else if (Type.FRAME_SIDE == js.type()) {
                    IElem5e elem5e = (eProp.old.read().equals("0"))
                            ? new builder.model1.ElemFrame(rootArea, js)
                            : new builder.model1.ElemFrame(rootArea, js);
                    rootArea.frames().put(js.layout(), elem5e);

                } else if (Type.IMPOST == js.type() || Type.SHTULP == js.type() || Type.STOIKA == js.type()) {
                    IElem5e elem5e = (eProp.old.read().equals("0"))
                            ? new builder.model1.ElemCross(owner, js)
                            : new builder.model1.ElemCross(owner, js);
                    owner.childs().add(elem5e); //добавим ребёна родителю

                } else if (Type.GLASS == js.type()) {
                    IElem5e elem5e = (eProp.old.read().equals("0"))
                            ? new builder.model1.ElemGlass(owner, js)
                            : new builder.model1.ElemGlass(owner, js);
                    owner.childs().add(elem5e); //добавим ребёна родителю

                } else if (Type.MOSKITKA == js.type()) {
                    IElem5e elem5e = (eProp.old.read().equals("0"))
                            ? new builder.model1.ElemMosquit(owner, js)
                            : new builder.model1.ElemMosquit(owner, js);
                    owner.childs().add(elem5e); //добавим ребёна родителю

                }
            }

            //Теперь вложенные элементы
            for (Map.Entry<IArea5e, GsonElem> entry : hm.entrySet()) {
                elements(entry.getKey(), entry.getValue());
            }

        } catch (Exception e) {
            System.err.println("Ошибка:Wincalc.elements(*) " + e);
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
            Cal5e calcElements = (eProp.old.read().equals("0")) //составы
                    ? new builder.making.Elements(this)
                    : new builder.making.Elements(this);
            calcElements.calc();
            calcFilling = (eProp.old.read().equals("0")) //заполнения
                    ? new builder.making.Filling(this)
                    : new builder.making.Filling(this);
            calcFilling.calc();
            calcFurniture = (eProp.old.read().equals("0")) //фурнитура 
                    ? new builder.making.Furniture(this)
                    : new builder.making.Furniture(this);
            calcFurniture.calc();
            calcTariffication = (eProp.old.read().equals("0")) //тарификация 
                    ? new builder.making.Tariffic(this, norm_otx)
                    : new builder.making.Tariffic(this, norm_otx);
            calcTariffication.calc();

            //Построим список спецификации
            for (IElem5e elem5e : listElem) {
                if (elem5e.spcRec().artikl.isEmpty() || elem5e.spcRec().artikl.trim().charAt(0) != '@') {
                    listSpec.add(elem5e.spcRec());
                }
                for (Specific spc : elem5e.spcRec().spcList) {
                    if (spc.artikl.isEmpty() || spc.artikl.trim().charAt(0) != '@') {
                        listSpec.add(spc);
                    }
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
                this.weight += el.artiklRecAn().getDbl(eArtikl.density) * el.width() * el.height() / 1000000; //уд.вес * площадь = вес
            }

            Collections.sort(listSpec, (o1, o2) -> (o1.place.subSequence(0, 3) + o1.name + o1.width).compareTo(o2.place.subSequence(0, 3) + o2.name + o2.width));

        } catch (Exception e) {
            System.err.println("Ошибка:Wincalc.constructiv() " + e);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="GET AND SET"> 
    private void initProperty() {
        genId = 0;
        width2 = 0;
        width1 = 0;
        height1 = 0;
        height2 = 0;
        syssizeRec = null;
        mapPardef.clear();
        List.of((List) listArea, (List) listElem, (List) listSpec, (List) listAll, (List) listJoin).forEach(el -> el.clear());
    }

    public double genId() {
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

    public HashMap<Integer, Record> mapPardef() {
        return mapPardef;
    }

    public double width() {
        return (width2 > width1) ? width2 : width1;
    }

    public double height() {
        return (height1 > height2) ? height1 : height2;
    }

    public double width1() {
        return width1;
    }

    public double width2() {
        return width2;
    }

    public double height1() {
        return height1;
    }

    public double height2() {
        return height2;
    }

    public double weight() {
        return weight;
    }

    public void weight(double weight) {
        this.weight = weight;
    }

    public double price() {
        return this.price;
    }

    public void price(double price) {
        this.price = price;
    }

    public double cost2() {
        return this.cost2;
    }

    public void cost2(double cost2) {
        this.cost2 = cost2;
    }

    public double square() {
        return width() * height() / 1000000;
    }
    // </editor-fold>     
}
