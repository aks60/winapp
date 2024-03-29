package builder.model1;

import builder.Wincalc;
import builder.IArea5e;
import builder.IElem5e;
import builder.ICom5t;
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
import builder.making.Specific;
import builder.script.GsonElem;
import builder.script.GsonRoot;
import common.LinkedCom;
import common.UCom;
import common.eProp;
import domain.eColor;
import domain.eParmap;
import enums.Form;
import enums.PKjson;
import enums.Type;

/**
 * Базовый класс для всех ареа
 */
public class AreaSimple extends Com5t implements IArea5e {

    protected Form form = null; //форма контура (параметр в развитии)
    protected EnumMap<Layout, IElem5e> frames = new EnumMap<>(Layout.class); //список рам в окне     
    protected LinkedCom<ICom5t> childs = new LinkedCom(); //дети

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

    public AreaSimple(Wincalc winc, IArea5e owner, GsonElem gson, double width, double height) {
        super(gson.id(), winc, owner, gson);
        this.layout = gson.layout();
        this.colorID1 = winc.rootGson.color1();
        this.colorID2 = winc.rootGson.color2();
        this.colorID3 = winc.rootGson.color3();

        initСonstructiv(gson.param());
        setLocation(width, height);
        initParametr(gson.param());
    }

    public AreaSimple(Wincalc winc, IArea5e owner, GsonElem gson, double width, double height, Form form) {
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

    /**
     * Профиль через параметр PKjson_sysprofID пример створки:sysprofID:1121,
     * typeOpen:4, sysfurnID:2916} Этого параметра нет в интерфейсе программы,
     * он сделан для тестирования с ps4. Делегируется детьми см. класс ElemFrame
     */
    @Override
    public void initСonstructiv(JsonObject param) {
        if (isJson(param, PKjson.sysprofID)) {//профили через параметр
            sysprofRec = eSysprof.find3(param.get(PKjson.sysprofID).getAsInt());
        }
        winc.listArea.add(this);
        winc.listAll.add(this);
    }

    /**
     * Вычисление координат ареа в котором будут находится элементы окна
     * ограниченные ареа и формой контура. Если ареа вложенный (owner != null)
     * значить есть импост у которого центр по середине и надо смещать ареа на
     * его ширину для центровки см. ElemCross.setLocation()
     */
    protected void setLocation(double width, double height) {

        if (owner != null) {  //происходит для импостов у которы центр по середине
            if (owner.childs().isEmpty() == true) { //если childs.isEmpty то prevArea искать нет смысла

                if (Layout.VERT.equals(owner.layout())) { //сверху вниз
                    double Y2 = (owner.y1() + height > owner.y2()) ? owner.y2() : owner.y1() + height;
                    setDimension(owner.x1(), owner.y1(), owner.x2(), Y2);

                } else if (Layout.HORIZ.equals(owner.layout())) { //слева направо
                    double X2 = (owner.x1() + width > owner.x2()) ? owner.x2() : owner.x1() + width;
                    setDimension(owner.x1(), owner.y1(), X2, owner.y2());
                }

            } else {
                for (int index = owner.childs().size() - 1; index >= 0; --index) { //т.к. this area ёщё не создана начнём с конца
                    if (owner.childs().get(index) instanceof IArea5e) {
                        IArea5e prevArea = (IArea5e) owner.childs().get(index);

                        if (Layout.VERT.equals(owner.layout())) { //сверху вниз                            
                            double Y2 = (prevArea.y2() + height > owner.y2()) ? owner.y2() : prevArea.y2() + height;
                            setDimension(owner.x1(), prevArea.y2(), owner.x2(), Y2);

                        } else if (Layout.HORIZ.equals(owner.layout())) { //слева направо
                            double X2 = (prevArea.x2() + width > owner.x2()) ? owner.x2() : prevArea.x2() + width;
                            setDimension(prevArea.x2(), owner.y1(), X2, owner.y2());
                        }
                        break;
                    }
                }
            }
        }
    }

    /**
     * Параметры системы(технолога) + параметры менеджера В таблице syspar1 для
     * каждой системы лежат параметры по умолчанию от технолога. К параметрам от
     * технолога замешиваем параметры от менеджера см. скрирт, например
     * {"ioknaParam": [-8252]}. При этом в winc.mapPardef будут изменения с
     * учётом менеджера.
     */
    protected void initParametr(JsonObject param) {
        try {
            if (isJson(param)) {
                //Добавим к параметрам системы конструкции параметры конкретной конструкции
                JsonArray ioknaParamArr = param.getAsJsonArray(PKjson.ioknaParam);
                if (ioknaParamArr != null && !ioknaParamArr.isJsonNull() && ioknaParamArr.isJsonArray()) {
                    //Цикл по пааметрам менеджера
                    ioknaParamArr.forEach(ioknaID -> {

                        //Record paramsRec, syspar1Rec;   
                        if (ioknaID.getAsInt() < 0) {
                            Record paramsRec = eParams.find(ioknaID.getAsInt()); //параметр менеджера
                            Record syspar1Rec = winc.mapPardef().get(paramsRec.getInt(eParams.groups_id));
                            if (syspar1Rec != null) { //ситуация если конструкция с nuni = -3, т.е. модели
                                syspar1Rec.setNo(eParams.text, paramsRec.getStr(eParams.text)); //накладываем параметр менеджера
                            }
                        } else {
                            Record paramsRec = eParmap.find(ioknaID.getAsInt()); //параметр менеджера
                            Record syspar1Rec = winc.mapPardef().get(paramsRec.getInt(eParmap.groups_id));
                            if (syspar1Rec != null) { //ситуация если конструкция с nuni = -3, т.е. модели
                                String text = eColor.find(paramsRec.getInt(eParmap.color_id1)).getStr(eColor.name);
                                syspar1Rec.setNo(eParams.text, text); //накладываем параметр менеджера
                            }
                        }
                    });
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка:AreaSimple.initParametr() " + e);
        }
    }

    @Override
    public EnumMap<Layout, IElem5e> frames() {
        return frames;
    }

    @Override
    public LinkedCom<ICom5t> childs() {
        return childs;
    }

    /**
     * Изменение размеров конструкции по оси X
     *
     * @param v - новый размер
     */
    @Override
    public void resizeX(double v) {
        GsonRoot rootGson = winc.rootGson;
        try {
            if (id() == 0) {
                double k = v / gson.width(); //коэффициент
                if (k != 1) {
                    if (UCom.getDbl(rootGson.width2(), 0.0) > UCom.getDbl(rootGson.width1(), 0.0)) {
                        rootGson.width2(v);
                        if (rootGson.width1() != null) {
                            rootGson.width1(k * rootGson.width1());
                        }
                    }
                    if (UCom.getDbl(rootGson.width1(), 0.0) > UCom.getDbl(rootGson.width2(), 0.0)) {
                        rootGson.width1(v);
                        if (rootGson.width2() != null) {
                            rootGson.width2(k * rootGson.width2());
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
                double k = v / lengthX(); //коэффициент 
                if (k != 1) {
                    gson.length(v);
                    if (type() == Type.ARCH) {
                        rootGson.width1(rootGson.width() - v);
                    } else if (type() == Type.TRAPEZE && form == Form.RIGHT) {
                        rootGson.width1(rootGson.width() - v);
                    } else if (type() == Type.TRAPEZE && form == Form.LEFT) {
                        rootGson.width2(rootGson.width() - v);
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

    /**
     * Изменение размеров конструкции по оси Y
     *
     * @param v - новый размер
     */
    @Override
    public void resizeY(double v) {
        GsonRoot rootGson = winc.rootGson;
        try {
            if (id() == 0) {
                double k = v / gson.height(); //коэффициент
                if (k != 1) {
                    if (UCom.getDbl(rootGson.height1(), 0.0) > UCom.getDbl(rootGson.height2(), 0.0)) {
                        rootGson.height1(v);
                        if (rootGson.height2() != null) {
                            rootGson.height2(k * rootGson.height2());
                        }
                    }
                    if (UCom.getDbl(rootGson.height2(), 0.0) > UCom.getDbl(rootGson.height1(), 0.0)) {
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
                double k = v / lengthY(); //коэффициент 
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
     * Т-соед. записываются в map, см. winc.listJoin.put(point, cross). За
     * угловые соединени отвечает конечнй наследник например
     * AreaRectangl.joining(). Прилегающие см. IElem5e.joinFlat()
     */
    @Override
    public void joining() {

        LinkedList<IElem5e> crosList = winc.listElem.filter(Type.IMPOST, Type.SHTULP, Type.STOIKA);
        LinkedList<IElem5e> elemList = winc.listElem.filter(Type.FRAME_SIDE, Type.STVORKA_SIDE, Type.IMPOST, Type.SHTULP, Type.STOIKA);

        //T - соединения
        //Цикл по кросс элементам
        for (IElem5e crosEl : crosList) {
            //Цикл по сторонам рамы и импостам (т.к. в створке Т-обр. соединений нет)
            for (IElem5e elem5e : elemList) {
                if ((elem5e.owner().type() == Type.ARCH && elem5e.layout() == Layout.TOP) == false) { //для арки inside() не работает

                    if (crosEl.owner().layout() == Layout.HORIZ) { //Импосты(штульпы...) расположены вертикально снизу вверх                    
                        if (elem5e.inside(crosEl.x2(), crosEl.y2()) == true && elem5e != crosEl) { //T - соединение нижнее                              
                            winc.listJoin.add(new ElemJoining(winc, TypeJoin.VAR40, LayoutJoin.TEE, crosEl, elem5e));
                        } else if (elem5e.inside(crosEl.x1(), crosEl.y1()) == true && elem5e != crosEl) { //T - соединение верхнее                            
                            winc.listJoin.add(new ElemJoining(winc, TypeJoin.VAR40, LayoutJoin.TEE, crosEl, elem5e));
                        }

                    } else { //Импосты(штульпы...)  расположены горизонтально слева на право 
                        if (elem5e.inside(crosEl.x1(), crosEl.y1()) == true && elem5e != crosEl) { //T - соединение левое                             
                            winc.listJoin.add(new ElemJoining(winc, TypeJoin.VAR40, LayoutJoin.TEE, crosEl, elem5e));
                        } else if (elem5e.inside(crosEl.x2(), crosEl.y2()) == true && elem5e != crosEl) { //T - соединение правое                              
                            winc.listJoin.add(new ElemJoining(winc, TypeJoin.VAR40, LayoutJoin.TEE, crosEl, elem5e));
                        }
                    }
                }
            }
        }
    }

    /**
     * Рисуем всю конструкцию!!!
     */
    @Override
    public void draw() {
        try {
            //Прорисовка стеклопакетов
            LinkedList<IElem5e> elemGlassList = winc.listElem.filter(Type.GLASS);
            elemGlassList.stream().forEach(el -> el.paint());

            //Прорисовка импостов
            LinkedList<IElem5e> elemImpostList = winc.listElem.filter(Type.IMPOST);
            elemImpostList.stream().forEach(el -> el.paint());

            //Прорисовка штульпов
            LinkedList<IElem5e> elemShtulpList = winc.listElem.filter(Type.SHTULP);
            elemShtulpList.stream().forEach(el -> el.paint());

            //Прорисовка стоек
            LinkedList<IElem5e> elemStoikaList = winc.listElem.filter(Type.STOIKA);
            elemStoikaList.stream().forEach(el -> el.paint());

            //Прорисовка рам
            frames.get(Layout.TOP).paint();
            frames.get(Layout.BOTT).paint();
            frames.get(Layout.LEFT).paint();
            frames.get(Layout.RIGHT).paint();
            if(frames.get(Layout.TOPR) != null) {
               frames.get(Layout.TOPR).paint();
            }
            if(frames.get(Layout.TOPL) != null) {
               frames.get(Layout.TOPL).paint();
            }
            

            //Прорисовка створок
            LinkedList<IArea5e> elemStvorkaList = winc.listArea.filter(Type.STVORKA);
            elemStvorkaList.stream().forEach(el -> el.paint());

            //Прорисовка раскладок
            LinkedList<IElem5e> glassList = winc.listElem.filter(Type.GLASS);
            glassList.stream().forEach(el -> el.rascladkaPaint());

            //Прорисовка москиток
            LinkedList<IElem5e> mosqList = winc.listElem.filter(Type.MOSKITKA);
            mosqList.stream().forEach(el -> el.paint());

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

    public EnumMap<Layout, IElem5e> frame() {
        return frames;
    }

    /**
     * Определяет ближайшего соседа в указанном направлении
     * @param side - сторона направления 
     */
    @Override
    public IElem5e joinSide(Layout side) {
        try {
            if (this.equals(root()) || this.type() == Type.STVORKA) {
                return frames().get(side);
            }
            EnumMap<Layout, IElem5e> mapJoin = root().frames();
            ICom5t ret = null;
            LinkedCom<ICom5t> listCom = owner.childs();
            for (int index = 0; index < listCom.size(); ++index) {
                if (listCom.get(index).id() == id()) {

                    if (owner.equals(root()) && owner.layout() == Layout.VERT) {
                        if (side == Layout.TOP) {
                            ret = (index == 0) ? mapJoin.get(side) : listCom.get(index - 1);
                        } else if (side == Layout.BOTT) {
                            ret = (index == listCom.size() - 1) ? mapJoin.get(side) : listCom.get(index + 1);
                        } else {
                            ret = root().frames().get(side);
                        }

                    } else if (owner.equals(root()) && owner.layout() == Layout.HORIZ) {
                        if (side == Layout.LEFT) {
                            ret = (index == 0) ? mapJoin.get(side) : listCom.get(index - 1);
                        } else if (side == Layout.RIGHT) {
                            ret = (index == listCom.size() - 1) ? mapJoin.get(side) : listCom.get(index + 1);
                        } else {
                            return root().frames().get(side);
                        }

                    } else {
                        if (owner.layout() == Layout.VERT) {
                            if (side == Layout.TOP) {
                                ret = (index == 0) ? owner.joinSide(side) : listCom.get(index - 1);
                            } else if (side == Layout.BOTT) {
                                ret = (index == listCom.size() - 1) ? owner.joinSide(side) : listCom.get(index + 1);
                            } else {
                                ret = owner.joinSide(side);
                            }
                        } else {
                            if (side == Layout.LEFT) {
                                ret = (index == 0) ? owner.joinSide(side) : listCom.get(index - 1);
                            } else if (side == Layout.RIGHT) {
                                ret = (index == listCom.size() - 1) ? owner.joinSide(side) : listCom.get(index + 1);
                            } else {
                                ret = owner.joinSide(side);
                            }
                        }
                    }
                }
            }
            return (IElem5e) ret;
        } catch (Exception e) {
            System.err.println("Ошибка:AreaSimple.adjoinedElem() " + e);
            return null;
        }
    }
}
