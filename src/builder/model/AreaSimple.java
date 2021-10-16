package builder.model;

import com.google.gson.Gson;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import startup.Main;
import builder.Wincalc;
import common.UCom;
import enums.PKjson;
import enums.Type;
import frames.swing.Draw;
import static java.util.stream.Collectors.toList;

public class AreaSimple extends Com5t {

    public EnumMap<Layout, ElemFrame> mapFrame = new EnumMap<>(Layout.class); //список рам в окне 
    public LinkedList<Com5t> listChild = new LinkedList(); //дети

    public AreaSimple(Wincalc iwin, AreaSimple owner, float id, Type type, Layout layout, float width, float height, int color1, int color2, int color3, String param) {
        super(id, iwin, owner);
        this.type = type;
        this.layout = layout;
        this.colorID1 = color1;
        this.colorID2 = color2;
        this.colorID3 = color3;
        if (owner != null && (owner.type == Type.ARCH || owner.type == Type.TRAPEZE)) {
            if (owner.listChild.isEmpty()) { //пока примитивно, всё нестандартное сверху
                this.type = owner.type;
            }
        }
        initСonstructiv(param);
        setLocation(width, height);
        initParametr(param);
        //System.out.println(this);
    }

    public void initСonstructiv(String param) {
        if (param(param, PKjson.colorID1) != -1) {
            this.colorID1 = param(param, PKjson.colorID1);
        }
        if (param(param, PKjson.sysprofID) != -1) { //профили через параметр
            sysprofRec = eSysprof.find3(param(param, PKjson.sysprofID));
        }
    }

    protected void setLocation(float width, float height) {

        if (owner() != null) {
            //Первая area добавляемая в area владельца
            if (owner().listChild.isEmpty() == true) {
                if (Layout.VERT.equals(owner().layout())) { //сверху вниз
                    setDimension(owner().x1, owner().y1, owner().x2, owner().y1 + height);
                } else if (Layout.HORIZ.equals(owner().layout())) { //слева направо
                    setDimension(owner().x1, owner().y1, owner().x1 + width, owner().y2);
                }

            } else { //Aреа перед текущей, т.к. this area ёщё не создана начнём с конца
                for (int index = owner().listChild.size() - 1; index >= 0; --index) {
                    if (owner().listChild.get(index) instanceof AreaSimple) {
                        AreaSimple prevArea = (AreaSimple) owner().listChild.get(index);
                        //Если последняя доб. area выходит за коорд. root area. Происходит при подкдадке ареа над импостом 
                        if (Layout.VERT.equals(owner().layout())) { //сверху вниз                            
                            float Y2 = (prevArea.y2 + height > root().y2) ? root().y2 : prevArea.y2 + height;
                            setDimension(owner().x1, prevArea.y2, owner().x2, Y2);

                        } else if (Layout.HORIZ.equals(owner().layout())) { //слева направо
                            float X2 = (prevArea.x2 + width > root().x2) ? root().x2 : prevArea.x2 + width;
                            setDimension(prevArea.x2, owner().y1, X2, owner().y2);
                        }
                        break;
                    }
                }
            }
        }
    }

    protected void initParametr(String param) {
        try {
            if (param != null && param.isEmpty() == false && param.equals("null") == false) {
                JsonObject jsonObj = new Gson().fromJson(param, JsonObject.class);
                JsonArray jsonArr = jsonObj.getAsJsonArray(PKjson.ioknaParam);
                if (jsonArr != null && !jsonArr.isJsonNull() && jsonArr.isJsonArray()) {
                    jsonArr.forEach(it -> {
                        Record paramRec = eParams.find(it.getAsInt());
                        iwin.mapPardef.put(paramRec.getInt(eParams.params_id), paramRec);
                    });
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка:Com5t.parsingParam() " + e);
        }
    }

    public void resizeAll(float x1, float y1, float x2, float y2) {
        List<Com5t> areaList = owner.listChild.stream().filter(it -> it.type == Type.AREA).collect(toList());
        if (this.x1 != x1) {
            for (Com5t com5t : areaList) {
                com5t.x1 += (com5t == this) ? this.x1 - x1 : (this.x1 - x1) / areaList.size();
            }
        } else if (this.y1 != y1) {

            for (Com5t com5t : areaList) {
                com5t.y1 += (com5t == this) ? this.y1 - y1 : (this.y1 - y1) / areaList.size();
            }
        } else if (this.x2 != x2) {
            for (Com5t com5t : areaList) {
                com5t.x2 += (com5t == this) ? this.x2 - x2 : (this.x2 - x2) / areaList.size();
            }
        } else if (this.y2 != y2) {
            areaList.forEach(it -> it.y2 += (it == this) ? this.y2 - y1 : (this.y2 - y1) / areaList.size());
        }
    }

    //Список элементов окна
    public <E> LinkedList<E> listElem(Type... type) {
        LinkedList<E> list = new LinkedList();
        UCom.listElem(this, list, Arrays.asList(type));
        return list;
    }

    public void joinFrame() {
    }

    public void joinElem() {

        LinkedList<ElemSimple> impList = listElem(Type.IMPOST, Type.SHTULP, Type.STOIKA);
        LinkedList<ElemSimple> elemList = listElem(Type.FRAME_SIDE, Type.STVORKA_SIDE, Type.IMPOST, Type.SHTULP, Type.STOIKA);
        Collections.sort(elemList, (a, b) -> Float.compare(a.id(), b.id()));

        //Цикл по импостам
        for (ElemSimple elemImp : impList) {
            //Цикл по сторонам рамы и импостам (т.к. в створке Т-обр. соединений нет)
            for (ElemSimple elem5e : elemList) {
                if ((elem5e.owner.type == Type.ARCH && elem5e.layout == Layout.TOP) == false) { //для арки inside() не работает
                    elemImp.anglCut[0] = 90;
                    elemImp.anglCut[1] = 90;

                    if (elemImp.owner().layout() == Layout.HORIZ) { //Импосты(штульпы...)  расположены по горизонтали слева на право                     
                        if (elem5e.inside(elemImp.x2, elemImp.y2) == true && elem5e != elemImp) { //T - соединение нижнее                              
                            ElemJoining.create(elemImp.joinPoint(0), iwin, TypeJoin.VAR40, LayoutJoin.TBOT, elemImp, elem5e, 90);                            
                        } else if (elem5e.inside(elemImp.x1, elemImp.y1) == true && elem5e != elemImp) { //T - соединение верхнее                            
                            ElemJoining.create(elemImp.joinPoint(1), iwin, TypeJoin.VAR40, LayoutJoin.TTOP, elemImp, elem5e, 90);
                        }

                    } else {//Импосты(штульпы...) расположены по вертикали снизу вверх
                        if (elem5e.inside(elemImp.x1, elemImp.y1) == true && elem5e != elemImp) { //T - соединение левое                             
                            ElemJoining.create(elemImp.joinPoint(0), iwin, TypeJoin.VAR40, LayoutJoin.TLEFT, elemImp, elem5e, 90);
                        } else if (elem5e.inside(elemImp.x2, elemImp.y2) == true && elem5e != elemImp) { //T - соединение правое                              
                            ElemJoining.create(elemImp.joinPoint(1), iwin, TypeJoin.VAR40, LayoutJoin.TRIGH, elemImp, elem5e, 90);
                        }
                    }
                }
            }
        }
    }

    //Рисуем конструкцию
    public void draw() {
        try {
            //Прорисовка стеклопакетов
            LinkedList<ElemGlass> elemGlassList = root().listElem(Type.GLASS);
            elemGlassList.stream().forEach(el -> el.paint());

            //Прорисовка импостов
            LinkedList<ElemCross> elemImpostList = root().listElem(Type.IMPOST);
            elemImpostList.stream().forEach(el -> el.paint());

            //Прорисовка штульпов
            LinkedList<ElemCross> elemShtulpList = root().listElem(Type.SHTULP);
            elemShtulpList.stream().forEach(el -> el.paint());

            //Прорисовка стоек
            LinkedList<ElemCross> elemStoikaList = root().listElem(Type.STOIKA);
            elemStoikaList.stream().forEach(el -> el.paint());

            //Прорисовка рам
            mapFrame.get(Layout.TOP).paint();
            mapFrame.get(Layout.BOTT).paint();
            mapFrame.get(Layout.LEFT).paint();
            mapFrame.get(Layout.RIGHT).paint();

            //Прорисовка створок
            LinkedList<AreaStvorka> elemStvorkaList = root().listElem(Type.STVORKA);
            elemStvorkaList.stream().forEach(el -> el.paint());

            //Рисунок в память
            if (iwin.bufferImg != null) {
                ByteArrayOutputStream byteArrOutStream = new ByteArrayOutputStream();
                ImageIO.write(iwin.bufferImg, "png", byteArrOutStream);
                if (Main.dev == true) {
                    File outputfile = new File("CanvasImage.png");
                    ImageIO.write(iwin.bufferImg, "png", outputfile);
                }
            }
        } catch (Exception s) {
            System.err.println("Ошибка:AreaSimple.drawWin() " + s);
        }
    }
}
