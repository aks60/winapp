package builder;

import builder.geoms.Area2Polygon;
import builder.geoms.Area2Simple;
import builder.geoms.Area2Stvorka;
import builder.geoms.Comp;
import builder.geoms.Elem2Cross;
import builder.geoms.Elem2Frame;
import builder.geoms.Elem2Glass;
import builder.geoms.Elem2Simple;
import builder.making.Specific;
import builder.script.GeoElem;
import builder.script.GeoRoot;
import com.google.gson.GsonBuilder;
import common.ArraySpc;
import common.listener.ListenerMouse;
import dataset.Record;
import domain.eSyspar1;
import domain.eSysprof;
import enums.Type;
import enums.UseArtiklTo;
import java.awt.Graphics2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Wingeo {

    public Integer nuni = 0;
    public Record syssizeRec = null; //системные константы     
    public double genId = 0; //для генерация ключа в спецификации
    public int colorID1 = -1; //базовый цвет
    public int colorID2 = -1; //внутренний цвет
    public int colorID3 = -1; //внещний цвет   

    public Graphics2D gc2D = null; //графический котекст рисунка  
    public double scale = .6;
    public ArrayList<ListenerMouse> mousePressed = new ArrayList();
    public ArrayList<ListenerMouse> mouseReleased = new ArrayList();
    public ArrayList<ListenerMouse> mouseDragged = new ArrayList();

    public HashMap<Integer, Record> mapPardef = new HashMap(); //пар. по умолчанию + наложенные пар. клиента
    public List<Area2Simple> listArea = new ArrayList(); //список ареа.
    public List<Elem2Simple> listLine = new ArrayList(); //список элем.
    public List<Elem2Frame> listFrame = new ArrayList();
    public List<Elem2Cross> listCross = new ArrayList();
    public ArraySpc<Specific> listSpec = new ArraySpc(); //спецификация

    public GeoRoot gson = null; //объектная модель конструкции 1-го уровня
    public Area2Polygon root = null; //объектная модель конструкции 2-го уровня

    public Wingeo() {
    }

    public Wingeo(String script) {
        build(script);
    }

    /**
     * Построение окна из json скрипта
     *
     * @param script - json скрипт построения окна
     * @return rootArea - главное окно
     */
    public void build(String script) {
        try {
            //Инит свойств окна
            init();

            //Парсинг входного скрипта
            //Создание элементов конструкции
            parsing(script);

            //построение полигонов
            root.build();

            //Каждый элемент конструкции попадает в спецификацию через функцию setSpecific()            
            //listFrame.forEach(elem -> elem.setSpecific()); //спецификация ведущих элементов конструкции
            //listCross.forEach(elem -> elem.setSpecific()); //спецификация ведущих элементов конструкции
        } catch (Exception e) {
            System.err.println("Ошибка:Wincalc.build() " + e);
        }
    }

    private void parsing(String script) {
        //Для тестирования
        //System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(new com.google.gson.JsonParser().parse(script)));

        gson = new GsonBuilder().create().fromJson(script, GeoRoot.class);
        gson.setOwner(this);

        //Инит конструктива
        this.nuni = gson.nuni;
        Record sysprofRec = eSysprof.find2(nuni, UseArtiklTo.FRAME);
        eSyspar1.find(nuni).forEach(syspar1Rec -> mapPardef.put(syspar1Rec.getInt(eSyspar1.groups_id), syspar1Rec)); //загрузим параметры по умолчанию

        root = new Area2Polygon(this, gson);

        elements(root, gson);
    }

    private void elements(Comp owner, GeoElem gson) {
        try {
            LinkedHashMap<Comp, GeoElem> hm = new LinkedHashMap();
            for (GeoElem js : gson.childs) {

                if (Type.STVORKA == js.type) {
                    Area2Simple area5e = new Area2Stvorka(this, gson, owner);
                    owner.childs().add(area5e); //добавим ребёна родителю
                    hm.put(area5e, js);

                } else if (Type.AREA == js.type) {
                    Area2Simple area5e = new Area2Simple(this, js, owner);
                    owner.childs().add(area5e); //добавим ребёна родителю
                    listArea.add(area5e);
                    hm.put(area5e, js);

                } else if (Type.FRAME_SIDE == js.type) {
                    Elem2Frame elem5e = new Elem2Frame(this, js, owner);
                    listLine.add(elem5e);
                    listFrame.add(elem5e);

                } else if (Type.IMPOST == js.type || Type.SHTULP == js.type || Type.STOIKA == js.type) {
                    Elem2Cross elem5e = new Elem2Cross(this, js, owner);
                    owner.childs().add(elem5e); //добавим ребёна родителю
                    listLine.add(elem5e);
                    listCross.add(elem5e);

                } else if (Type.GLASS == js.type) {
                    Elem2Glass elem5e = new Elem2Glass(this, js, owner);
                    owner.childs().add(elem5e); //добавим ребёна родителю

                } else if (Type.MOSKITKA == js.type) {
                    //Elem2Mosquit elem5e = new Elem2Mosquit(this, js, owner);
                    //owner.childs().add(elem5e); //добавим ребёна родителю

                }
            }

            //Теперь вложенные элементы
            for (Map.Entry<Comp, GeoElem> entry : hm.entrySet()) {
                elements(entry.getKey(), entry.getValue());
            }

        } catch (Exception e) {
            System.err.println("Ошибка:Wincalc.elements(*) " + e);
        }
    }

    public void draw() {
        try {
            root.build();
            root.paint();
            listArea.forEach(e -> e.build());
            listArea.forEach(e -> e.paint());
            listCross.forEach(e -> e.build());
            listCross.forEach(e -> e.paint());

        } catch (Exception e) {
            System.err.println("Ошибка:Area2Simple.draw() " + e);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="GET AND SET"> 
    private void init() {
        genId = 0;
        syssizeRec = null;
        mapPardef.clear();
        List.of((List) listArea, (List) listFrame, (List) listCross, (List) listSpec).forEach(el -> el.clear());
    }

    public double width() {
        double[] s = {0, 0, 0, 0, 0, 0, 10000, 0};
        PathIterator it = root.area.getPathIterator(null);
        while (!it.isDone()) {
            it.currentSegment(s);
            s[6] = (s[0] < s[6]) ? s[0] : s[6];
            s[7] = (s[0] > s[7]) ? s[0] : s[7];
            it.next();
        }
        return s[7] - s[6];
    }

    public double height() {
        double[] s = {0, 0, 0, 0, 0, 0, 10000, 0};
        PathIterator it = root.area.getPathIterator(null);
        while (!it.isDone()) {
            it.currentSegment(s);
            s[6] = (s[1] < s[6]) ? s[1] : s[6];
            s[7] = (s[1] > s[7]) ? s[1] : s[7];
            it.next();
        }
        return s[7] - s[6];
    }
    // </editor-fold>  
}
