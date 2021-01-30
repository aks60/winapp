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
import builder.script.AreaElem;
import builder.script.AreaRoot;
import builder.script.Element;
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
    public AreaRoot fromJson = null;
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
            fromJson = gson.fromJson(json, AreaRoot.class);

            this.nuni = fromJson.nuni();
            float id = fromJson.id();
            this.width = fromJson.width();
            this.height = fromJson.height();
            this.heightAdd = fromJson.heightAdd();
            this.colorID1 = fromJson.color(1);
            this.colorID2 = fromJson.color(2);
            this.colorID3 = fromJson.color(3);

            //Инит конструктив
            Record sysprofRec = eSysprof.find2(nuni, UseArtiklTo.FRAME);
            artiklRec = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), true);
            syssizeRec = eSyssize.find(artiklRec.getInt(eArtikl.syssize_id));
            eSyspar1.find(nuni).stream().forEach(rec -> mapParamDef.put(rec.getInt(eSyspar1.params_id), rec)); //загрузим параметры по умолчанию

            //Главное окно
            LinkedList<Mediate> fromjsonList = new LinkedList(); //промежуточная конструкция
            Mediate mediateRoot = new Mediate(null, id, fromJson.elemType().name(), fromJson.layoutArea().name(), width, height, fromJson.paramJson());
            fromjsonList.add(mediateRoot);

            //Добавим рамы         
            for (builder.script.Element elem : fromJson.elems()) {
                if (TypeElem.FRAME_SIDE.equals(elem.elemType())) {
                    fromjsonList.add(new Mediate(mediateRoot, elem.id(), TypeElem.FRAME_SIDE.name(), elem.layoutFrame().name(), ""));
                }
            }
            //Добавим все остальные Mediate, через рекурсию
            recursionArea(fromJson, mediateRoot, fromjsonList);
            
            //Упорядочим порядок построения окна
            Collections.sort(fromjsonList, (o1, o2) -> Float.compare(o1.id(), o2.id())); 

            //Строим конструкцию из промежуточного списка
            windowsBuild(fromjsonList);

        } catch (Exception e) {
            System.err.println("Ошибка:Wincalc.parsingScript() " + e);
        }
    }

    //Промежуточный список окна (для ранжирования элементов построения)
    private void recursionArea(AreaElem el, Mediate med, LinkedList<Mediate> mediateList) {
        try {
            for (AreaElem area : el.areas()) {
                float width = (med.layoutArea() == LayoutArea.VERT) ? med.width() : area.width();
                float height = (med.layoutArea() == LayoutArea.VERT) ? area.height() : med.height();
                Mediate med2 = new Mediate(med, area.id(), area.elemType().name(), area.layoutArea().name(), width, height, area.paramJson());
                mediateList.add(med2);

                recursionArea(area, med2, mediateList); //рекурсия

            }
            for (Element elem : el.elems()) {
                if (TypeElem.IMPOST.equals(elem.elemType())) {
                    mediateList.add(new Mediate(med, elem.id(), elem.elemType().name(), LayoutArea.ANY.name(), elem.paramJson()));
                } else if (TypeElem.GLASS.equals(elem.elemType())) {
                    mediateList.add(new Mediate(med, elem.id(), elem.elemType().name(), LayoutArea.ANY.name(), elem.paramJson()));
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
            if (TypeElem.RECTANGL == mdtRoot.elemType()) {
                rootArea = new AreaRectangl(this, null, mdtRoot.id(), TypeElem.RECTANGL, mdtRoot.layoutArea(), mdtRoot.width(), mdtRoot.height(), colorID1, colorID2, colorID3, mdtRoot.paramJson()); //простое
            } else if (TypeElem.TRAPEZE == mdtRoot.elemType()) {
                rootArea = new AreaTrapeze(this, null, mdtRoot.id(), TypeElem.TRAPEZE, mdtRoot.layoutArea(), mdtRoot.width(), mdtRoot.height(), colorID1, colorID2, colorID3, mdtRoot.paramJson()); //трапеция
            } else if (TypeElem.TRIANGL == mdtRoot.elemType()) {
                rootArea = new AreaTriangl(this, null, mdtRoot.id(), TypeElem.TRIANGL, mdtRoot.layoutArea(), mdtRoot.width(), mdtRoot.height(), colorID1, colorID2, colorID3, mdtRoot.paramJson()); //треугольник
            } else if (TypeElem.ARCH == mdtRoot.elemType()) {
                rootArea = new AreaArch(this, null, mdtRoot.id(), TypeElem.ARCH, mdtRoot.layoutArea(), mdtRoot.width(), mdtRoot.height(), colorID1, colorID2, colorID3, mdtRoot.paramJson()); //арка
            }
            mdtRoot.area5e = rootArea;

            //Цикл по элементам конструкции ранж. по ключам.
            for (Mediate mdt : mediateList) {

                //Добавим рамы в гпавное окно
                if (TypeElem.FRAME_SIDE == mdt.elemType()) {
                    ElemFrame elemFrame = new ElemFrame(rootArea, mdt.id(), mdt.layoutArea(), mdt.paramJson());
                    rootArea.mapFrame.put(elemFrame.layout(), elemFrame);
                    continue;
                }

                if (TypeElem.STVORKA == mdt.elemType()) {
                    mdt.addArea(new AreaStvorka(this, mdt.owner.area5e, mdt.id(), mdt.paramJson()));
                } else if (TypeElem.AREA == mdt.elemType()) {
                    mdt.addArea(new AreaSimple(this, mdt.owner.area5e, mdt.id(), mdt.elemType(), mdt.layoutArea(), mdt.width(), mdt.height(), -1, -1, -1, null)); //простое
                } else if (TypeElem.IMPOST == mdt.elemType()) {
                    mdt.addElem(new ElemImpost(mdt.owner.area5e, mdt.id(), mdt.paramJson()));
                } else if (TypeElem.GLASS == mdt.elemType()) {
                    mdt.addElem(new ElemGlass(mdt.owner.area5e, mdt.id(), mdt.paramJson()));
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка:wincalc.Wincalc.windowsBuild() " + e);
        }
    }
}
