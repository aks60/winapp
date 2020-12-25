package estimate;

import estimate.model.ElemGlass;
import estimate.model.AreaStvorka;
import estimate.model.AreaArch;
import estimate.model.AreaTriangl;
import estimate.model.ElemJoining;
import estimate.model.AreaSimple;
import estimate.model.AreaRectangl;
import estimate.model.AreaTrapeze;
import estimate.model.ElemImpost;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dataset.Query;
import dataset.Record;
import domain.eArtikl;
import domain.eSyssize;
import domain.eSyspar1;
import domain.eSysprof;
import enums.LayoutArea;
import enums.TypeElem;
import enums.UseArtiklTo;
import estimate.constr.Cal5e;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import estimate.constr.Specification;
import estimate.constr.Tariffication;
import estimate.constr.Joining;
import estimate.constr.Elements;
import estimate.constr.Filling;
import estimate.constr.Furniture;
import estimate.model.Com5t;
import estimate.model.ElemFrame;
import estimate.model.ElemSimple;
import estimate.script.Mediate;
import frames.swing.Draw;
import java.util.Arrays;
import startup.App1;

public class Wincalc {

    public Connection conn;
    public Integer nuni = 0;
    public int prj = -1;
    public Record artiklRec = null; //главный артикл системы профилей   
    public Record syssizeRec = null; //константы    
    public float genId = 100; //генерация ключа спецификации

    public float width = 0.f;     //ширина окна
    public float height = 0.f;    //высота окна
    public float heightAdd = 0.f; //арка, трапеция, треугольник
    public final int colorNone = 1005;  //без цвета (возвращаемое значение по умолчанию)
    public int colorID1 = -1;  //базовый цвет
    public int colorID2 = -1;  //внутренний цвет
    public int colorID3 = -1;  //внещний цвет

    public Draw draw = new Draw(this);
    public byte[] bufferByte = null; //буфер рисунка
    public BufferedImage bufferImg = null;  //образ рисунка
    public Graphics2D gc2d = null; //графический котекст рисунка  
    public float scale1 = 1, scale2 = 20; //коэффициент сжатия
    public String labelSketch = "empty"; //надпись на эскизе

    public AreaSimple rootArea = null;
    public HashMap<Integer, Record> mapParamDef = new HashMap(); //параметры по умолчанию 
    public LinkedList<Com5t> listCom5t; //список всех Component
    public LinkedList<ElemSimple> listElem; //список ElemSimple
    public HashMap<String, ElemJoining> mapJoin = new HashMap(); //список соединений рам и створок 
    public ArrayList<Specification> listSpec = new ArrayList(); //спецификация
    public Cal5e calcJoining, calcElements, calcFilling, calcFurniture, calTariffication; //объекты калькуляции конструктива
    public LinkedList<Mediate> mediateList = new LinkedList();

    public AreaSimple build(String productJson) {
        //System.out.println(productJson);
        mediateList.clear();
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
            Query.conf = "calc";
            Query.listOpenTable.forEach(q -> q.clear());
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
            Collections.sort(listSpec, (o1, o2) -> (o1.place.subSequence(0, 3) + o1.name + o1.width).compareTo(o2.place.subSequence(0, 3) + o2.name + o2.width)
            );

        } catch (Exception e) {
            System.err.println("Ошибка:Wincalc.constructiv(" + e);
        } finally {
            Query.conf = "app";
        }
    }

    // Парсим входное json окно и строим объектную модель окна
    private void parsingScript(String json) {
        try {
            //Для тестирования
            Gson gs = new GsonBuilder().setPrettyPrinting().create();
            JsonElement je = new JsonParser().parse(json);
            //System.out.println(gs.toJson(je));

            Gson gson = new Gson(); //библиотека jso
            JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
            JsonObject jsonObj = jsonElement.getAsJsonObject();

            int id = jsonObj.get("id").getAsInt();
            String paramJson = jsonObj.get("paramJson").getAsString();
            nuni = jsonObj.get("nuni").getAsInt();                       
            
            width = jsonObj.get("width").getAsFloat();
            height = jsonObj.get("height").getAsFloat();
            heightAdd = jsonObj.get("heightAdd").getAsFloat();

            Record sysprofRec = eSysprof.find2(nuni, UseArtiklTo.FRAME);
            artiklRec = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), true);
            syssizeRec = eSyssize.find(artiklRec.getInt(eArtikl.syssize_id));
            eSyspar1.find(nuni).stream().forEach(rec -> mapParamDef.put(rec.getInt(eSyspar1.grup), rec)); //загрузим параметры по умолчанию

            colorID1 = jsonObj.get("color1").getAsInt();
            colorID2 = jsonObj.get("color2").getAsInt();
            colorID3 = jsonObj.get("color3").getAsInt();

            //Главное окно
            String layoutObj = jsonObj.get("layoutArea").getAsString();
            String elemType = jsonObj.get("elemType").getAsString();
            Mediate mediateRoot = new Mediate(null, id, elemType, layoutObj, width, height, paramJson);
            mediateList.add(mediateRoot);

            //Добавим рамы         
            for (Object elemFrame : jsonObj.get("elements").getAsJsonArray()) {
                JsonObject jsonFrame = (JsonObject) elemFrame;
                if (TypeElem.FRAME_SIDE.name().equals(jsonFrame.get("elemType").getAsString())) {
                    String layourFrame = jsonFrame.get("layoutFrame").getAsString();
                    mediateList.add(new Mediate(mediateRoot, jsonFrame.get("id").getAsInt(), TypeElem.FRAME_SIDE.name(), layourFrame, null));
                }
            }
            //Добавим все остальные Mediate, через рекурсию
            intermBuild(jsonObj, mediateRoot, mediateList);
            Collections.sort(mediateList, (o1, o2) -> Float.compare(o1.id, o2.id)); //упорядочим порядок построения окна

            //Строим конструкцию из промежуточного списка
            windowsBuild(mediateList);

        } catch (Exception e) {
            System.err.println("Ошибка:Wincalc.parsingScript() " + e);
        }
    }

    //Промежуточный список окна (для последовательности построения)
    private void intermBuild(JsonObject jso, Mediate owner, LinkedList<Mediate> mediateList) {
        try {
            for (Object obj : jso.get("elements").getAsJsonArray()) {
                JsonObject objArea = (JsonObject) obj;
                int id = objArea.get("id").getAsInt();
                String type = objArea.get("elemType").getAsString();
                String param = (objArea.get("paramJson") != null) ? objArea.get("paramJson").getAsString() : null;
                if (TypeElem.AREA.name().equals(type) || TypeElem.STVORKA.name().equals(type)) {

                    float width = (owner.layout == LayoutArea.VERT) ? owner.width : objArea.get("width").getAsFloat();
                    float height = (owner.layout == LayoutArea.VERT) ? objArea.get("height").getAsFloat() : owner.height;
                    String layout = objArea.get("layoutArea").getAsString();
                    Mediate mediateBox = new Mediate(owner, id, type, layout, width, height, param);
                    mediateList.add(mediateBox);

                    intermBuild(objArea, mediateBox, mediateList); //рекурсия

                } else {
                    if (TypeElem.IMPOST.name().equals(type)) {
                        mediateList.add(new Mediate(owner, id, type, LayoutArea.ANY.name(), param));
                    } else if (TypeElem.GLASS.name().equals(type)) {
                        mediateList.add(new Mediate(owner, id, type, LayoutArea.ANY.name(), param));
                    }
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
            if (TypeElem.RECTANGL == mdtRoot.type) {
                rootArea = new AreaRectangl(this, null, mdtRoot.id, TypeElem.RECTANGL, mdtRoot.layout, mdtRoot.width, mdtRoot.height, colorID1, colorID2, colorID3, mdtRoot.param); //простое
            } else if (TypeElem.TRAPEZE == mdtRoot.type) {
                rootArea = new AreaTrapeze(this, null, mdtRoot.id, TypeElem.TRAPEZE, mdtRoot.layout, mdtRoot.width, mdtRoot.height, colorID1, colorID2, colorID3, mdtRoot.param); //трапеция
            } else if (TypeElem.TRIANGL == mdtRoot.type) {
                rootArea = new AreaTriangl(this, null, mdtRoot.id, TypeElem.TRIANGL, mdtRoot.layout, mdtRoot.width, mdtRoot.height, colorID1, colorID2, colorID3, mdtRoot.param); //треугольник
            } else if (TypeElem.ARCH == mdtRoot.type) {
                rootArea = new AreaArch(this, null, mdtRoot.id, TypeElem.ARCH, mdtRoot.layout, mdtRoot.width, mdtRoot.height, colorID1, colorID2, colorID3, mdtRoot.param); //арка
            }
            mdtRoot.area5e = rootArea;

            //Цикл по элементам конструкции ранж. по ключам.
            for (Mediate mdt : mediateList) {

                //Добавим рамы в гпавное окно
                if (TypeElem.FRAME_SIDE == mdt.type) {
                    ElemFrame elemFrame = new ElemFrame(rootArea, mdt.id, mdt.layout, mdt.param);
                    rootArea.mapFrame.put(elemFrame.layout(), elemFrame);
                    continue;
                }

                if (TypeElem.STVORKA == mdt.type) {
                    mdt.addArea(new AreaStvorka(this, mdt.owner.area5e, mdt.id, mdt.param));
                } else if (TypeElem.AREA == mdt.type) {
                    mdt.addArea(new AreaSimple(this, mdt.owner.area5e, mdt.id, mdt.type, mdt.layout, mdt.width, mdt.height, -1, -1, -1, null)); //простое
                } else if (TypeElem.IMPOST == mdt.type) {
                    mdt.addElem(new ElemImpost(mdt.owner.area5e, mdt.id, mdt.param));
                } else if (TypeElem.GLASS == mdt.type) {
                    mdt.addElem(new ElemGlass(mdt.owner.area5e, mdt.id, mdt.param));
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка:wincalc.Wincalc.windowsBuild() " + e);
        }
    }
}
