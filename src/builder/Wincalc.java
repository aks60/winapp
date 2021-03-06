package builder;

import builder.model.ElemGlass;
import builder.model.AreaStvorka;
import builder.model.AreaArch;
import builder.model.AreaTriangl;
import builder.model.ElemJoining;
import builder.model.AreaSimple;
import builder.model.AreaRectangl;
import builder.model.AreaTrapeze;
import builder.model.ElemImpost;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataset.Record;
import domain.eArtikl;
import domain.eSyssize;
import domain.eSyspar1;
import domain.eSysprof;
import enums.TypeElem;
import enums.UseArtiklTo;
import builder.making.Cal5e;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import builder.making.Specific;
import builder.making.Tariffic;
import builder.making.Joining;
import builder.making.Elements;
import builder.making.Filling;
import builder.making.Furniture;
import builder.model.ElemFrame;
import builder.model.ElemShtulp;
import builder.model.ElemSimple;
import builder.script.GsonRoot;
import builder.script.GsonElem;
import frames.swing.Draw;
import java.util.LinkedHashMap;
import java.util.Map;

public class Wincalc {

    public Connection conn;
    public Integer nuni = 0;
    public Record artiklRec = null; //главный артикл системы профилей   
    public Record syssizeRec = null; //константы    
    public float genId = 100; //генерация ключа в спецификации

    public float width = 0.f; //ширина окна
    public float height = 0.f; //высота окна
    public float heightAdd = 0.f; //арка, трапеция, треугольник
    public int colorID1 = -1;  //базовый цвет
    public int colorID2 = -1;  //внутренний цвет
    public int colorID3 = -1;  //внещний цвет

    public Draw draw = new Draw(this);
    public byte[] bufferByte = null; //буфер рисунка
    public BufferedImage bufferImg = null;  //образ рисунка
    public Graphics2D gc2d = null; //графический котекст рисунка  
    public double scale = 1; //коэффициент сжатия

    public AreaSimple rootArea = null; //главное окно кострукции
    public GsonRoot rootGson = null; //главное окно кострукции в формате gson

    public HashMap<Integer, Record> mapPardef = new HashMap(); //пар. по умолчанию + наложенные пар. клиента
    public LinkedList<ElemSimple> listElem; //список ElemSimple
    public HashMap<String, ElemJoining> mapJoin = new HashMap(); //список соединений рам и створок 
    public ArrayList<Specific> listSpec = new ArrayList(); //спецификация
    public Cal5e calcJoining, calcElements, calcFilling, calcFurniture, calTariffication; //объекты калькуляции конструктива

    public AreaSimple build(String productJson) {

        genId = 100;
        listSpec.clear();
        mapPardef.clear();
        mapJoin.clear();

        //Парсинг входного скрипта
        parsing(productJson);

        //Соединения 
        rootArea.joinFrame(); //соединения рамы
        rootArea.joinElem(); //T-соединения рамы 
        LinkedList<AreaStvorka> listAreaStv = rootArea.listElem(TypeElem.STVORKA); //список створок
        listAreaStv.stream().forEach(area5e -> area5e.joinFrame());  //соединения створок

        //Список элементов, (важно! получаем после построения створки)
        listElem = rootArea.listElem(TypeElem.FRAME_SIDE, TypeElem.STVORKA_SIDE, TypeElem.IMPOST, TypeElem.SHTULP, TypeElem.GLASS);
        //Важно! Не нарушаем последовательность построения окна
        Collections.sort(listElem, (a, b) -> Float.compare(a.id(), b.id()));  
        return rootArea;
    }

    // Парсим входное json окно и строим объектную модель окна
    private void parsing(String json) {
        try {
            //System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(new com.google.gson.JsonParser().parse(json))); //для тестирования
            //System.out.println(new GsonBuilder().create().toJson(new com.google.gson.JsonParser().parse(json))); //для тестирования
            Gson gson = new GsonBuilder().create();
            rootGson = gson.fromJson(json, GsonRoot.class);
            rootGson.setParent(rootGson);

            //Инит конструктива
            this.nuni = rootGson.nuni();
            this.width = rootGson.width();
            this.height = rootGson.height();
            this.heightAdd = (rootGson.heightAdd() == null) ? this.height : rootGson.heightAdd();
            this.colorID1 = rootGson.color1;
            this.colorID2 = rootGson.color2;
            this.colorID3 = rootGson.color3;
            this.artiklRec = eArtikl.find(eSysprof.find2(nuni, UseArtiklTo.FRAME).getInt(eSysprof.artikl_id), true);
            this.syssizeRec = eSyssize.find(artiklRec.getInt(eArtikl.syssize_id));
            eSyspar1.find(nuni).stream().forEach(rec -> mapPardef.put(rec.getInt(eSyspar1.params_id), rec)); //загрузим параметры по умолчанию

            //Главное окно
            if (TypeElem.RECTANGL == rootGson.type()) {
                rootArea = new AreaRectangl(this, null, rootGson.id(), TypeElem.RECTANGL, rootGson.layout(), rootGson.width(), rootGson.height(), colorID1, colorID2, colorID3, rootGson.param()); //простое
            } else if (TypeElem.TRAPEZE == rootGson.type()) {
                rootArea = new AreaTrapeze(this, null, rootGson.id(), TypeElem.TRAPEZE, rootGson.layout(), rootGson.width(), rootGson.height(), colorID1, colorID2, colorID3, rootGson.param()); //трапеция
            } else if (TypeElem.TRIANGL == rootGson.type()) {
                rootArea = new AreaTriangl(this, null, rootGson.id(), TypeElem.TRIANGL, rootGson.layout(), rootGson.width(), rootGson.height(), colorID1, colorID2, colorID3, rootGson.param()); //треугольник
            } else if (TypeElem.ARCH == rootGson.type()) {
                rootArea = new AreaArch(this, null, rootGson.id(), TypeElem.ARCH, rootGson.layout(), rootGson.width(), rootGson.height(), colorID1, colorID2, colorID3, rootGson.param()); //арка
            }

            //Добавим рамы
            for (GsonElem gsonElem : rootGson.childs()) {
                if (TypeElem.FRAME_SIDE == gsonElem.type()) {
                    rootArea.mapFrame.put(gsonElem.layout(), new ElemFrame(rootArea, gsonElem.id(), gsonElem.layout(), gsonElem.param()));
                }
            }

            //Всё остальное
            recursion(rootArea, rootGson);

        } catch (Exception e) {
            System.err.println("Ошибка:Wincalc.parsing() " + e);
        }
    }

    //Рекурсия дерева конструкции
    private void recursion(AreaSimple owner, GsonElem gsonElem) {
        try {
            LinkedHashMap<AreaSimple, GsonElem> hm = new LinkedHashMap();
            for (GsonElem el : gsonElem.childs()) {

                //Добавим Area
                if (TypeElem.STVORKA == el.type()) {
                    AreaSimple area5e = new AreaStvorka(Wincalc.this, owner, el.id(), el.param());
                    owner.listChild.add(area5e);
                    hm.put(area5e, el);
                } else if (TypeElem.AREA == el.type()) {
                    AreaSimple area5e = new AreaSimple(Wincalc.this, owner, el.id(), el.type(), el.layout(), el.width(), el.height(), -1, -1, -1, null);
                    owner.listChild.add(area5e);
                    hm.put(area5e, el);

                    //Добавим Element
                } else if (TypeElem.IMPOST == el.type()) {
                    owner.listChild.add(new ElemImpost(owner, el.type(), el.id(), el.param()));
                    
                } else if (TypeElem.SHTULP == el.type()) {
                    owner.listChild.add(new ElemShtulp(owner, el.type(), el.id(), el.param()));
                    
                } else if (TypeElem.GLASS == el.type()) {
                    owner.listChild.add(new ElemGlass(owner, el.id(), el.param()));
                }
            }

            //Теперь вложенные элементы
            for (Map.Entry<AreaSimple, GsonElem> entry : hm.entrySet()) {
                recursion(entry.getKey(), entry.getValue());
            }

        } catch (Exception e) {
            System.err.println("Ошибка:Wincalc.recursion() " + e);
        }
    }

    //Конструктив и тарификация 
    public void constructiv(boolean norm_otx) {
        try {
            calcElements = new Elements(this); //составы
            calcElements.calc();
            calcJoining = new Joining(this); //соединения
            calcJoining.calc();            
            calcFilling = new Filling(this); //заполнения
            calcFilling.calc();
            calcFurniture = new Furniture(this); //фурнитура 
            calcFurniture.calc();
            calTariffication = new Tariffic(this, norm_otx); //тарификация
            calTariffication.calc();

            for (ElemSimple elemRec : listElem) {
                listSpec.add(elemRec.spcRec);
                listSpec.addAll(elemRec.spcRec.spcList);
            }
            Collections.sort(listSpec, (o1, o2) -> (o1.place.subSequence(0, 3) + o1.name + o1.width).compareTo(o2.place.subSequence(0, 3) + o2.name + o2.width));

        } catch (Exception e) {
            System.err.println("Ошибка:Wincalc.constructiv(" + e);
        }
    }

}
