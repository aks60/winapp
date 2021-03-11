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
import com.google.gson.JsonElement;
import dataset.Record;
import domain.eArtikl;
import domain.eSyssize;
import domain.eSyspar1;
import domain.eSysprof;
import enums.LayoutArea;
import enums.TypeElem;
import enums.UseArtiklTo;
import builder.specif.Cal5e;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import builder.specif.Specification;
import builder.specif.Tariffication;
import builder.specif.Joining;
import builder.specif.Elements;
import builder.specif.Filling;
import builder.specif.Furniture;
import builder.model.ElemFrame;
import builder.model.ElemSimple;
import builder.script.JsonArea;
import builder.script.JsonRoot;
import builder.script.JsonElem;
import builder.script.Mediate;
import com.google.gson.JsonParser;
import frames.swing.Draw;

public class Wincalc {

    public Connection conn;
    public Integer nuni = 0;
    public int prj = -1;
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
    public String labelSketch = "empty"; //надпись на эскизе

    public AreaSimple rootArea = null;
    public JsonRoot rootJson = null;
    public HashMap<Integer, Record> mapParamDef = new HashMap(); //пар. по умолчанию + наложенные пар. клиента
    public LinkedList<ElemSimple> listElem; //список ElemSimple
    public HashMap<String, ElemJoining> mapJoin = new HashMap(); //список соединений рам и створок 
    public ArrayList<Specification> listSpec = new ArrayList(); //спецификация
    public Cal5e calcJoining, calcElements, calcFilling, calcFurniture, calTariffication; //объекты калькуляции конструктива

    public AreaSimple build(String productJson) {

        mapParamDef.clear();
        mapJoin.clear();
        listSpec.clear();
        genId = 100;

        //Парсинг входного скрипта
        parsingScript(productJson);

        //Соединения 
        rootArea.joinFrame(); //соединения рамы
        rootArea.joinElem(); //T-соединения рамы 
        LinkedList<AreaStvorka> listAreaStv = rootArea.listElem(TypeElem.STVORKA); //список створок
        listAreaStv.stream().forEach(area5e -> area5e.joinFrame());  //соединения створок

        //Список элементов, (важно! получаем после построения створки)
        listElem = rootArea.listElem(TypeElem.FRAME_SIDE, TypeElem.STVORKA_SIDE, TypeElem.IMPOST, TypeElem.GLASS);
        Collections.sort(listElem, Collections.reverseOrder((a, b) -> Float.compare(a.id(), b.id())));
        return rootArea;
    }

    //Конструктив и тарификация 
    public void constructiv() {
        try {            
            calcJoining = new Joining(this); //соединения
            calcJoining.calc();
            calcElements = new Elements(this); //составы
            calcElements.calc();
            calcFilling = new Filling(this); //заполнения
            calcFilling.calc();
            calcFurniture = new Furniture(this); //фурнитура 
            calcFurniture.calc();
            calTariffication = new Tariffication(this); //тарификация
            calTariffication.calc();
            for (ElemSimple elemRec : listElem) {
                listSpec.add(elemRec.specificationRec);
                listSpec.addAll(elemRec.specificationRec.specificationList);
            }
            Collections.sort(listSpec, (o1, o2) -> (o1.place.subSequence(0, 3) + o1.name + o1.width).compareTo(o2.place.subSequence(0, 3) + o2.name + o2.width));

        } catch (Exception e) {
            System.err.println("Ошибка:Wincalc.constructiv(" + e);
        }
    }

    // Парсим входное json окно и строим объектную модель окна
    private void parsingScript(String json) {
        try {
            //Для тестирования
            //Gson gs = new GsonBuilder().setPrettyPrinting().create();
            //JsonElement je = new JsonParser().parse(json);
            //System.out.println(gs.toJson(je));

            Gson gson = new GsonBuilder().create();
            rootJson = gson.fromJson(json, JsonRoot.class);

            this.nuni = rootJson.nuni();
            float id = rootJson.id();
            this.width = rootJson.width();
            this.height = rootJson.height();
            this.heightAdd = rootJson.heightAdd();
            this.colorID1 = rootJson.color(1);
            this.colorID2 = rootJson.color(2);
            this.colorID3 = rootJson.color(3);

            //Инит конструктив
            Record sysprofRec = eSysprof.find2(nuni, UseArtiklTo.FRAME);
            artiklRec = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), true);
            syssizeRec = eSyssize.find(artiklRec.getInt(eArtikl.syssize_id));
            eSyspar1.find(nuni).stream().forEach(rec -> mapParamDef.put(rec.getInt(eSyspar1.params_id), rec)); //загрузим параметры по умолчанию

            //Главное окно
            LinkedList<Mediate> fromjsonList = new LinkedList(); //промежуточная конструкция
            Mediate mediateRoot = new Mediate(null, id, rootJson.type().name(), rootJson.layout().name(), width, height, rootJson.param());
            fromjsonList.add(mediateRoot);

            //Добавим рамы         
            for (builder.script.JsonElem elem : rootJson.elements()) {
                if (TypeElem.FRAME_SIDE.equals(elem.type())) {
                    fromjsonList.add(new Mediate(mediateRoot, elem.id(), TypeElem.FRAME_SIDE.name(), elem.layout().name(), elem.param()));
                }
            }
            //Добавим все остальные Mediate, через рекурсию
            recursionArea(rootJson, mediateRoot, fromjsonList);
            
            //Упорядочим порядок построения окна
            Collections.sort(fromjsonList, (o1, o2) -> Float.compare(o1.id(), o2.id())); 

            //Строим конструкцию из промежуточного списка
            windowsBuild(fromjsonList);

        } catch (Exception e) {
            System.err.println("Ошибка:Wincalc.parsingScript() " + e);
        }
    }

    //Промежуточный список окна (для ранжирования элементов построения)
    private void recursionArea(JsonArea el, Mediate med, LinkedList<Mediate> mediateList) {
        try {
            for (JsonArea area : el.areas()) {
                float width = (med.layout() == LayoutArea.VERT) ? med.width() : area.width();
                float height = (med.layout() == LayoutArea.VERT) ? area.height() : med.height();
                Mediate med2 = new Mediate(med, area.id(), area.type().name(), area.layout().name(), width, height, area.param());
                mediateList.add(med2);

                recursionArea(area, med2, mediateList); //рекурсия

            }
            for (JsonElem elem : el.elements()) {
                if (TypeElem.IMPOST.equals(elem.type())) {
                    mediateList.add(new Mediate(med, elem.id(), elem.type().name(), LayoutArea.ANY.name(), elem.param()));
                } else if (TypeElem.GLASS.equals(elem.type())) {
                    mediateList.add(new Mediate(med, elem.id(), elem.type().name(), LayoutArea.ANY.name(), elem.param()));
                }
            }

        } catch (Exception e) {
            System.err.println("wincalc.Wincalc.intermBuild() " + e);
        }
    }

    //Строим конструкцию из промежуточного списка
    private void windowsBuild(LinkedList<Mediate> mediateList) {
        try {
            //Главное окно        
            Mediate mdtRoot = mediateList.getFirst();
            if (TypeElem.RECTANGL == mdtRoot.type()) {
                rootArea = new AreaRectangl(this, null, mdtRoot.id(), TypeElem.RECTANGL, mdtRoot.layout(), mdtRoot.width(), mdtRoot.height(), colorID1, colorID2, colorID3, mdtRoot.param()); //простое
            } else if (TypeElem.TRAPEZE == mdtRoot.type()) {
                rootArea = new AreaTrapeze(this, null, mdtRoot.id(), TypeElem.TRAPEZE, mdtRoot.layout(), mdtRoot.width(), mdtRoot.height(), colorID1, colorID2, colorID3, mdtRoot.param()); //трапеция
            } else if (TypeElem.TRIANGL == mdtRoot.type()) {
                rootArea = new AreaTriangl(this, null, mdtRoot.id(), TypeElem.TRIANGL, mdtRoot.layout(), mdtRoot.width(), mdtRoot.height(), colorID1, colorID2, colorID3, mdtRoot.param()); //треугольник
            } else if (TypeElem.ARCH == mdtRoot.type()) {
                rootArea = new AreaArch(this, null, mdtRoot.id(), TypeElem.ARCH, mdtRoot.layout(), mdtRoot.width(), mdtRoot.height(), colorID1, colorID2, colorID3, mdtRoot.param()); //арка
            }
            mdtRoot.area5e = rootArea;

            //Цикл по элементам конструкции ранж. по ключам.
            for (Mediate mdt : mediateList) {

                //Добавим рамы в гпавное окно
                if (TypeElem.FRAME_SIDE == mdt.type()) {
                    ElemFrame elemFrame = new ElemFrame(rootArea, mdt.id(), mdt.layout(), mdt.param());
                    rootArea.mapFrame.put(elemFrame.layout(), elemFrame);
                    continue;
                }

                if (TypeElem.STVORKA == mdt.type()) {
                    mdt.addArea(new AreaStvorka(this, mdt.owner.area5e, mdt.id(), mdt.param()));
                } else if (TypeElem.AREA == mdt.type()) {
                    mdt.addArea(new AreaSimple(this, mdt.owner.area5e, mdt.id(), mdt.type(), mdt.layout(), mdt.width(), mdt.height(), -1, -1, -1, null)); //простое
                } else if (TypeElem.IMPOST == mdt.type()) {
                    mdt.addElem(new ElemImpost(mdt.owner.area5e, mdt.id(), mdt.param()));
                } else if (TypeElem.GLASS == mdt.type()) {
                    mdt.addElem(new ElemGlass(mdt.owner.area5e, mdt.id(), mdt.param()));
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка:wincalc.Wincalc.windowsBuild() " + e);
        }
    }
}
