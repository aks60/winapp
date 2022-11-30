package builder.model;

import builder.IArea5e;
import builder.IElem5e;
import builder.ICom5t;
import builder.IStvorka;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dataset.Record;
import domain.eParams;
import domain.eSysprof;
import enums.Layout;
import enums.LayoutJoin;
import enums.TypeJoin;
import java.io.File;
import java.util.EnumMap;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import builder.Wincalc;
import builder.making.Specific;
import builder.script.GsonElem;
import builder.script.GsonRoot;
import common.UCom;
import common.eProp;
import enums.Form;
import enums.PKjson;
import enums.Type;
import java.util.HashMap;
import common.interfac.IDrawing;
import common.interfac.ILocation;

public class AreaSimple extends Com5t implements IArea5e {

    protected Form form = null; //форма контура (параметр в развитии)
    protected EnumMap<Layout, IElem5e> frames = new EnumMap<>(Layout.class); //список рам в окне     
    protected LinkedList<ICom5t> childs = new LinkedList(); //дети

    public AreaSimple(Wincalc winc) {
        super(winc.rootGson.id(), winc, null, winc.rootGson);
        this.layout = winc.rootGson.layout();
        this.colorID1 = winc.rootGson.color1();
        this.colorID2 = winc.rootGson.color2();
        this.colorID3 = winc.rootGson.color3();

        initСonstructiv(winc.rootGson.param());
        setLocation(winc.rootGson.width(), winc.rootGson.height());
        initParametr(winc.rootGson.param());
    }

    public AreaSimple(Wincalc winc, IArea5e owner, GsonElem gson, float width, float height) {
        super(gson.id(), winc, owner, gson);
        this.layout = gson.layout();
        this.colorID1 = winc.rootGson.color1();
        this.colorID2 = winc.rootGson.color2();
        this.colorID3 = winc.rootGson.color3();

        initСonstructiv(gson.param());
        setLocation(width, height);
        initParametr(gson.param());
    }

    public AreaSimple(Wincalc winc, IArea5e owner, GsonElem gson, float width, float height, Form form) {
        super(gson.id(), winc, owner, gson);
        this.form = form;
        this.layout = gson.layout();
        this.colorID1 = winc.rootGson.color1();
        this.colorID2 = winc.rootGson.color2();
        this.colorID3 = winc.rootGson.color3();

        initСonstructiv(gson.param());
        setLocation(width, height);
        initParametr(gson.param());
    }

    @Override
    public void initСonstructiv(JsonObject param) {
        //if(eProperty.dev)
        if (isJson(param, PKjson.sysprofID)) {//профили через параметр
            sysprofRec = eSysprof.find3(param.get(PKjson.sysprofID).getAsInt());
        }
        winc.listArea.add(this);
        winc.listAll.add(this);
    }

    /**
     * Вычисление координат ареа в котором будут находится элементы окна
     * ограниченные этим ареа и формой контура.
     */
    protected void setLocation(float width, float height) {
        //Происходит при подкдадке дополнительной ареа в арке
        //или сужении area створки при нахлёсте профилей
        if (owner != null) {
            if (owner.childs().isEmpty() == true) { //если childs.isEmpty то prevArea искать нет смысла

                if (Layout.VERT.equals(owner.layout())) { //сверху вниз
                    float Y2 = (owner.y1() + height > owner.y2()) ? owner.y2() : owner.y1() + height;
                    setDimension(owner.x1(), owner.y1(), owner.x2(), Y2);

                } else if (Layout.HORIZ.equals(owner.layout())) { //слева направо
                    float X2 = (owner.x1() + width > owner.x2()) ? owner.x2() : owner.x1() + width;
                    setDimension(owner.x1(), owner.y1(), X2, owner.y2());
                }

            } else {
                for (int index = owner.childs().size() - 1; index >= 0; --index) { //т.к. this area ёщё не создана начнём с конца
                    if (owner.childs().get(index) instanceof IArea5e) {
                        IArea5e prevArea = (IArea5e) owner.childs().get(index);

                        if (Layout.VERT.equals(owner.layout())) { //сверху вниз                            
                            float Y2 = (prevArea.y2() + height > owner.y2()) ? owner.y2() : prevArea.y2() + height;
                            setDimension(owner.x1(), prevArea.y2(), owner.x2(), Y2);

                        } else if (Layout.HORIZ.equals(owner.layout())) { //слева направо
                            float X2 = (prevArea.x2() + width > owner.x2()) ? owner.x2() : prevArea.x2() + width;
                            setDimension(prevArea.x2(), owner.y1(), X2, owner.y2());
                        }
                        break;
                    }
                }
            }
        }
    }

    protected void initParametr(JsonObject param) {
        try {
            if (isJson(param)) {
                HashMap defMap = new HashMap();
                //Добавим к параметрам системы конструкции параметры конкретной конструкции
                JsonArray ioknaParamArr = param.getAsJsonArray(PKjson.ioknaParam);
                if (ioknaParamArr != null && !ioknaParamArr.isJsonNull() && ioknaParamArr.isJsonArray()) {
                    ioknaParamArr.forEach(grup -> {
                        Record paramsRec = eParams.find(grup.getAsInt()); //параметр менеджера  
                        Record syspar1Rec = winc.mapPardef.get(paramsRec.getInt(eParams.params_id));
                        if (syspar1Rec != null) { //ситуация если конструкция с nuni = -3, т.е. модели
                            syspar1Rec.setNo(eParams.text, paramsRec.getStr(eParams.text));
                        }
                    });
                }
            }
        } catch (Exception e) {
            System.out.println(winc.rootGson.prj);
            System.err.println("Ошибка:ICom5t.parsingParam() " + e);
        }
    }

    @Override
    public void addFilling(IElem5e glass, Specific spcAdd) {
    }

    @Override
    public EnumMap<Layout, IElem5e> frames() {
        return frames;
    }

    @Override
    public LinkedList<ICom5t> childs() {
        return childs;
    }

    @Override
    public void resizeX(float v) {
        GsonRoot rootGson = winc.rootGson;
        try {
            if (id() == 0) {
                float k = v / gson.width(); //коэффициент
                if (k != 1) {
                    if (UCom.getFloat(rootGson.width1(), 0f) > UCom.getFloat(rootGson.width2(), 0f)) {
                        rootGson.width1(v);
                        if (rootGson.width2() != null) {
                            rootGson.width2(k * rootGson.width2());
                        }
                    }
                    if (UCom.getFloat(rootGson.width2(), 0f) > UCom.getFloat(rootGson.width1(), 0f)) {
                        rootGson.width2(v);
                        if (rootGson.width1() != null) {
                            rootGson.width1(k * rootGson.width1());
                        }
                    }
                    for (IArea5e e : winc.listArea) { //перебор всех гориз. area
                        if (e.layout() == Layout.HORIZ) {
                            for (ICom5t e2 : e.childs()) { //изменение детей по ширине
                                if (e2 instanceof IArea5e) {
                                    e2.gson().length(k * e2.gson().length());
                                }
                            }
                        }
                    }
                }
            } else {
                float k = v / lengthX(); //коэффициент 
                if (k != 1) {
                    gson.length(v);
                    if (type() == Type.ARCH) {
                        rootGson.width2(rootGson.width() - v);
                    } else if (type() == Type.TRAPEZE && form == Form.RIGHT) {
                        rootGson.width2(rootGson.width() - v);
                    } else if (type() == Type.TRAPEZE && form == Form.LEFT) {
                        rootGson.width1(rootGson.width() - v);
                    }

                    for (ICom5t e : childs) { //изменение детей по высоте
                        if (e.owner().layout() == Layout.HORIZ && (e.type() == Type.AREA || e.type() == Type.STVORKA)) {
                            ((IArea5e) e).resizeX(k * e.lengthX()); //рекурсия изменения детей

                        } else {
                            if (e instanceof IArea5e) {
                                for (ICom5t e2 : ((IArea5e) e).childs()) {
                                    if (e2.owner().layout() == Layout.HORIZ && (e2.type() == Type.AREA || e2.type() == Type.STVORKA)) {
                                        ((IArea5e) e2).resizeX(k * e2.lengthX()); //рекурсия изменения детей
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка:AreaSimple.resizeX()");
        }
    }

    @Override
    public void resizeY(float v) {
        GsonRoot rootGson = winc.rootGson;
        try {
            if (id() == 0) {
                float k = v / gson.height(); //коэффициент
                if (k != 1) {
                    if (UCom.getFloat(rootGson.height1(), 0f) > UCom.getFloat(rootGson.height2(), 0f)) {
                        rootGson.height1(v);
                        if (rootGson.height2() != null) {
                            rootGson.height2(k * rootGson.height2());
                        }
                    }
                    if (UCom.getFloat(rootGson.height2(), 0f) > UCom.getFloat(rootGson.height1(), 0f)) {
                        rootGson.height2(v);
                        if (rootGson.height1() != null) {
                            rootGson.height1(k * rootGson.height1());
                        }
                    }
                    for (IArea5e e : winc.listArea) { //перебор всех вертик. area
                        if (e.layout() == Layout.VERT) {
                            for (ICom5t e2 : e.childs()) { //изменение детей по высоте
                                if (e2 instanceof IArea5e) {
                                    e2.gson().length(k * e2.gson().length());
                                }
                            }
                        }
                    }
                }
            } else {
                float k = v / lengthY(); //коэффициент 
                if (k != 1) {
                    gson.length(v);
                    if (type() == Type.ARCH) {
                        rootGson.height2(rootGson.height() - v);
                    } else if (type() == Type.TRAPEZE && form == Form.RIGHT) {
                        rootGson.height2(rootGson.height() - v);
                    } else if (type() == Type.TRAPEZE && form == Form.LEFT) {
                        rootGson.height1(rootGson.height() - v);
                    }

                    for (ICom5t e : childs) { //изменение детей по высоте
                        if (e.owner().layout() == Layout.VERT && (e.type() == Type.AREA || e.type() == Type.STVORKA)) {
                            ((IArea5e) e).resizeY(k * e.lengthY()); //рекурсия изменения детей

                        } else {
                            if (e instanceof IArea5e) {
                                for (ICom5t e2 : ((IArea5e) e).childs()) {
                                    if (e2.owner().layout() == Layout.VERT && (e2.type() == Type.AREA || e2.type() == Type.STVORKA)) {
                                        ((IArea5e) e2).resizeY(k * e2.lengthY()); //рекурсия изменения детей
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка:AreaSimple.resizeY()");
        }
    }

    /**
     * Форма контура Если ареа элемента окна(напр.стекло) имеет form!=null, то
     * эта ареа принимает форму root.type()
     */
    @Override
    public Type type() {
        if (this != root && form != null) {
            return root.type();
        }
        return super.type();
    }

    /**
     * T - соединения area. Все поперечены(cross) в area имеют Т-соединения.
     * Т-соед. записываются в map, см. winc.mapJoin.put(point, cross). За
     * угловые соединени отвечает конечнй наследник например
     * AreaRectangl.joining(). Прилегающие см. IElem5e.joinFlat()
     */
    @Override
    public void joining() {

        LinkedList<IElem5e> crosList = UCom.listSortObj(winc.listElem, Type.IMPOST, Type.SHTULP, Type.STOIKA);
        LinkedList<IElem5e> elemList = UCom.listSortObj(winc.listElem, Type.FRAME_SIDE, Type.STVORKA_SIDE, Type.IMPOST, Type.SHTULP, Type.STOIKA);

        //T - соединения
        //Цикл по кросс элементам
        for (IElem5e crosEl : crosList) {
            //Цикл по сторонам рамы и импостам (т.к. в створке Т-обр. соединений нет)
            for (IElem5e elem5e : elemList) {
                if ((elem5e.owner().type() == Type.ARCH && elem5e.layout() == Layout.TOP) == false) { //для арки inside() не работает
                    crosEl.anglCut()[0] = 90;
                    crosEl.anglCut()[1] = 90;

                    if (crosEl.owner().layout() == Layout.HORIZ) { //Импосты(штульпы...)  расположены по горизонтали слева на право                     
                        if (elem5e.inside(crosEl.x2(), crosEl.y2()) == true && elem5e != crosEl) { //T - соединение нижнее                              
                            winc.mapJoin.put(crosEl.joinPoint(0), ElemJoining.create(winc, TypeJoin.VAR40, LayoutJoin.TBOT, crosEl, elem5e, 90));
                        } else if (elem5e.inside(crosEl.x1(), crosEl.y1()) == true && elem5e != crosEl) { //T - соединение верхнее                            
                            winc.mapJoin.put(crosEl.joinPoint(1), ElemJoining.create(winc, TypeJoin.VAR40, LayoutJoin.TTOP, crosEl, elem5e, 90));
                        }

                    } else {//Импосты(штульпы...) расположены по вертикали снизу вверх
                        if (elem5e.inside(crosEl.x1(), crosEl.y1()) == true && elem5e != crosEl) { //T - соединение левое                             
                            winc.mapJoin.put(crosEl.joinPoint(0), ElemJoining.create(winc, TypeJoin.VAR40, LayoutJoin.TLEFT, crosEl, elem5e, 90));
                        } else if (elem5e.inside(crosEl.x2(), crosEl.y2()) == true && elem5e != crosEl) { //T - соединение правое                              
                            winc.mapJoin.put(crosEl.joinPoint(1), ElemJoining.create(winc, TypeJoin.VAR40, LayoutJoin.TRIGH, crosEl, elem5e, 90));
                        }
                    }
                }
            }
        }
    }

    //Рисуем конструкцию
    @Override
    public void draw() {
        if (eProp.old.read().equals("1")) {
            drawing.draw();
        } else {
            try {
                //Прорисовка стеклопакетов
                LinkedList<IElem5e> elemGlassList = UCom.listSortObj(winc.listElem, Type.GLASS);
                elemGlassList.stream().forEach(el -> el.paint());

                //Прорисовка импостов
                LinkedList<IElem5e> elemImpostList = UCom.listSortObj(winc.listElem, Type.IMPOST);
                elemImpostList.stream().forEach(el -> el.paint());

                //Прорисовка штульпов
                LinkedList<IElem5e> elemShtulpList = UCom.listSortObj(winc.listElem, Type.SHTULP);
                elemShtulpList.stream().forEach(el -> el.paint());

                //Прорисовка стоек
                LinkedList<IElem5e> elemStoikaList = UCom.listSortObj(winc.listElem, Type.STOIKA);
                elemStoikaList.stream().forEach(el -> el.paint());

                //Прорисовка рам
                frames.get(Layout.TOP).paint();
                frames.get(Layout.BOTT).paint();
                frames.get(Layout.LEFT).paint();
                frames.get(Layout.RIGHT).paint();

                //Прорисовка створок
                LinkedList<IArea5e> elemStvorkaList = UCom.listSortObj(winc.listArea, Type.STVORKA);
                elemStvorkaList.stream().forEach(el -> el.paint());

                //Рисунок в память
                if (winc.bufferImg != null) {
                    ByteArrayOutputStream byteArrOutStream = new ByteArrayOutputStream();
                    ImageIO.write(winc.bufferImg, "png", byteArrOutStream);
                    if (eProp.dev == true) {
                        File outputfile = new File("CanvasImage.png");
                        ImageIO.write(winc.bufferImg, "png", outputfile);
                    }
                }
            } catch (Exception s) {
                System.err.println("Ошибка:IArea5e.drawWin() " + s);
            }
        }
    }

    public EnumMap<Layout, IElem5e> frame() {
        return frames;
    }

// <editor-fold defaultstate="collapsed" desc="Version"> 
    private IDrawing drawing = () -> {
        try {
            //Прорисовка стеклопакетов
            LinkedList<IElem5e> elemGlassList = UCom.listSortObj(winc.listElem, Type.GLASS);
            elemGlassList.stream().forEach(el -> el.paint());

            //Прорисовка импостов
            LinkedList<IElem5e> elemImpostList = UCom.listSortObj(winc.listElem, Type.IMPOST);
            elemImpostList.stream().forEach(el -> el.paint());

            //Прорисовка штульпов
            LinkedList<IElem5e> elemShtulpList = UCom.listSortObj(winc.listElem, Type.SHTULP);
            elemShtulpList.stream().forEach(el -> el.paint());

            //Прорисовка стоек
            LinkedList<IElem5e> elemStoikaList = UCom.listSortObj(winc.listElem, Type.STOIKA);
            elemStoikaList.stream().forEach(el -> el.paint());

            //Прорисовка рам
            frames.get(Layout.TOP).paint();
            frames.get(Layout.BOTT).paint();
            frames.get(Layout.LEFT).paint();
            frames.get(Layout.RIGHT).paint();

            //Прорисовка створок
            LinkedList<IArea5e> elemStvorkaList = UCom.listSortObj(winc.listArea, Type.STVORKA);
            elemStvorkaList.stream().forEach(el -> el.paint());

            //Рисунок в память
            if (winc.bufferImg != null) {
                ByteArrayOutputStream byteArrOutStream = new ByteArrayOutputStream();
                ImageIO.write(winc.bufferImg, "png", byteArrOutStream);
                if (eProp.dev == true) {
                    File outputfile = new File("CanvasImage.png");
                    ImageIO.write(winc.bufferImg, "png", outputfile);
                }
            }
        } catch (Exception s) {
            System.err.println("Ошибка:IArea5e.drawWin() " + s);
        }
    };

    private ILocation localion = (float width, float heigh) -> {
    };
// </editor-fold>  
}
