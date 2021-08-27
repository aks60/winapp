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
import enums.PKjson;
import enums.Type;
import frames.UGui;
import frames.swing.Canvas;
import java.awt.BasicStroke;
import java.util.Map;

public class AreaSimple extends Com5t {

    public int view = 0; //вид конструкции 
    public EnumMap<Layout, ElemFrame> mapFrame = new EnumMap<>(Layout.class); //список рам в окне 
    public LinkedList<Com5t> listChild = new LinkedList(); //дети

    public AreaSimple(Wincalc iwin, AreaSimple owner, float id, Type type, Layout layout, float width, float height, int color1, int color2, int color3, String param) {
        super(id, iwin, owner);
        this.type = type;
        this.layout = layout;
        this.colorID1 = color1;
        this.colorID2 = color2;
        this.colorID3 = color3;
        initСonstructiv(param);
        initDimension(width, height);
        initParametr(param);
    }

    public void initСonstructiv(String param) {
        if (param(param, PKjson.colorID1) != -1) {
            this.colorID1 = param(param, PKjson.colorID1);
        }
        if (param(param, PKjson.sysprofID) != -1) { //профили через параметр
            sysprofRec = eSysprof.find3(param(param, PKjson.sysprofID));
        }
    }

    protected void initDimension(float width, float height) {

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
                    if (owner().listChild.get(index).type == Type.AREA) {
                        AreaSimple prevArea = (AreaSimple) owner().listChild.get(index);
                        //Если последняя доб. area выходит за коорд. root area. Происходит при подкдадке ареа над импостом 
                        if (Layout.VERT.equals(owner().layout())) { //сверху вниз                            
                            float Y2 = (prevArea.y2 + height > rootArea().y2) ? rootArea().y2 : prevArea.y2 + height;
                            setDimension(owner().x1, prevArea.y2, owner().x2, Y2);

                        } else if (Layout.HORIZ.equals(owner().layout())) { //слева направо
                            float X2 = (prevArea.x2 + width > rootArea().x2) ? rootArea().x2 : prevArea.x2 + width;
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
                        iwin().mapPardef.put(paramRec.getInt(eParams.params_id), paramRec);
                    });
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка:Com5t.parsingParam() " + e);
        }
    }

    //Список элементов окна
    public <E> LinkedList<E> listElem(Type... type) {
        LinkedList<E> list = new LinkedList();
        listElem(this, list, Arrays.asList(type));
        return list;
    }

    private <E> void listElem(Com5t com5t, LinkedList<E> list, List<Type> type) {

        if (type.contains(com5t.type)) {
            list.add((E) com5t);
        }
        if (com5t instanceof AreaSimple) {
            for (ElemFrame frm : ((AreaSimple) com5t).mapFrame.values()) {
                if (type.contains(frm.type)) {
                    list.add((E) frm);
                }
            }
        }
        if (com5t instanceof AreaSimple) {
            ((AreaSimple) com5t).listChild.forEach(comp -> listElem(comp, list, type));
        }
    }

    public void joinFrame() {
    }

    public void joinElem() {

        List<ElemSimple> impList = listElem(Type.IMPOST, Type.SHTULP, Type.STOIKA);
        List<ElemSimple> elemList = listElem(Type.FRAME_SIDE, Type.IMPOST, Type.SHTULP, Type.STOIKA);

        //Цикл по импостам
        for (ElemSimple elemImp : impList) {
            //Цикл по сторонам рамы и импостам (т.к. в створке Т-обр. соединений нет)
            for (ElemSimple elem5e : elemList) {
                 if ((elem5e.owner.type == Type.ARCH && elem5e.layout == Layout.TOP) == false) { //для арки inside() не работает

                    elemImp.anglCut[0] = 90;
                    elemImp.anglCut[1] = 90;

                    //Импосты(штульпы...)  расположены по горизонтали слева на право
                    if (elemImp.owner().layout() == Layout.HORIZ) {
                        //elemImp.anglHoriz = 90;
                        if (elem5e.inside(elemImp.x2, elemImp.y2) == true
                                && iwin().mapJoin.get(elemImp.joinPoint(0)) == null) { //T - соединение нижнее                              
                            ElemJoining el = new ElemJoining(iwin(), TypeJoin.VAR40, LayoutJoin.TBOT, elemImp, elem5e, 90);
                            iwin().mapJoin.put(elemImp.joinPoint(0), el);

                        } else if (elem5e.inside(elemImp.x1, elemImp.y1) == true
                                && iwin().mapJoin.get(elemImp.joinPoint(1)) == null) { //T - соединение верхнее                            
                            ElemJoining el = new ElemJoining(iwin(), TypeJoin.VAR40, LayoutJoin.TTOP, elemImp, elem5e, 90);
                            iwin().mapJoin.put(elemImp.joinPoint(1), el);
                        }

                        //Импосты(штульпы...) расположены по вертикали снизу вверх и справо на лево
                    } else {
                        //elemImp.anglHoriz = 270;
                        if (elem5e.inside(elemImp.x1, elemImp.y1) == true
                                && iwin().mapJoin.get(elemImp.joinPoint(0)) == null) { //T - соединение левое                             
                            ElemJoining el = new ElemJoining(iwin(), TypeJoin.VAR40, LayoutJoin.TLEFT, elemImp, elem5e, 90);
                            iwin().mapJoin.put(elemImp.joinPoint(0), el);

                        } else if (elem5e.inside(elemImp.x2, elemImp.y2) == true
                                && iwin().mapJoin.get(elemImp.joinPoint(1)) == null) { //T - соединение правое                              
                            ElemJoining el = new ElemJoining(iwin(), TypeJoin.VAR40, LayoutJoin.TRIGH, elemImp, elem5e, 90);
                            iwin().mapJoin.put(elemImp.joinPoint(1), el);
                        }
                    }
                }
            }
        }
    }

    //Рисуем конструкцию
    public void draw(int width, int height) {
        try {
            //iwin().gc2d.fillRect(0, 0, width, height);

            //Прорисовка стеклопакетов
            LinkedList<ElemGlass> elemGlassList = rootArea().listElem(Type.GLASS);
            elemGlassList.stream().forEach(el -> el.paint());

            //Прорисовка импостов
            LinkedList<ElemCross> elemImpostList = rootArea().listElem(Type.IMPOST);
            elemImpostList.stream().forEach(el -> el.paint());

            //Прорисовка штульпов
            LinkedList<ElemCross> elemShtulpList = rootArea().listElem(Type.SHTULP);
            elemShtulpList.stream().forEach(el -> el.paint());

            //Прорисовка стоек
            LinkedList<ElemCross> elemStoikaList = rootArea().listElem(Type.STOIKA);
            elemStoikaList.stream().forEach(el -> el.paint());

            //Прорисовка рам
            for (Map.Entry<Layout, ElemFrame> entry : mapFrame.entrySet()) {
                ElemFrame frame = entry.getValue();
                frame.paint();
            }
//            if (Type.ARCH == type) {
//                mapFrame.get(Layout.SPEC).paint();
//            } else {
//                mapFrame.get(Layout.TOP).paint();
//            }
//            mapFrame.get(Layout.BOTT).paint();
//            mapFrame.get(Layout.LEFT).paint();
//            mapFrame.get(Layout.RIGHT).paint();

            //Прорисовка створок
            LinkedList<AreaStvorka> elemStvorkaList = rootArea().listElem(Type.STVORKA);
            elemStvorkaList.stream().forEach(el -> el.paint());

            //Прорисовка размера  
            if (iwin().scale > 0.1) {
                LinkedList<Float> ls1 = new LinkedList(Arrays.asList(x1, x2)), ls2 = new LinkedList(Arrays.asList(y1, y2));
                LinkedList<ElemCross> impostList = rootArea().listElem(Type.IMPOST, Type.SHTULP, Type.STOIKA);
                for (ElemSimple impostElem : impostList) { //по импостам определим точки разрыва линии
                    if (Layout.VERT == impostElem.owner().layout) {
                        ls2.add(impostElem.y1 + (impostElem.y2 - impostElem.y1) / 2);
                    } else {
                        ls1.add(impostElem.x1 + (impostElem.x2 - impostElem.x1) / 2);
                    }
                }
                Collections.sort(ls1);
                Collections.sort(ls2);
                int mov = 80;
                for (int i = 1; i < ls1.size(); i++) {
                    float x1 = ls1.get(i - 1), x2 = ls1.get(i);
                    line(x1, Canvas.height(iwin()) + mov, x2, Canvas.height(iwin()) + mov, 0);
                }
                for (int i = 1; i < ls2.size(); i++) {
                    float y1 = ls2.get(i - 1), y2 = ls2.get(i);
                    line((this.x2 + mov), y1, (this.x2 + mov), y2, 0);
                }
                if (ls1.size() > 2) { //линия общей ширины
                    line(rootArea().x1, Canvas.height(iwin()) + mov * 2, rootArea().x2, Canvas.height(iwin()) + mov * 2, 0);
                }
                if (ls2.size() > 2) { //линия общей высоты
                    line(iwin().width + mov * 2, 0, iwin().width + mov * 2, Canvas.height(iwin()), 0);
                }
            }
            //Рисунок в память
            if (iwin().bufferImg != null) {
                ByteArrayOutputStream bosFill = new ByteArrayOutputStream();
                ImageIO.write(iwin().bufferImg, "png", bosFill);
                iwin().bufferByte = bosFill.toByteArray();

                if (Main.dev == true) {
                    File outputfile = new File("CanvasImage.png");
                    ImageIO.write(iwin().bufferImg, "png", outputfile);
                }
            }
        } catch (Exception s) {
            System.err.println("Ошибка:AreaSimple.drawWin() " + s);
        }
    }

    private void line(float x1, float y1, float x2, float y2, float dy) {

        iwin().gc2d.setColor(java.awt.Color.BLACK);
        int size = (iwin().scale > .3) ? 40 : (iwin().scale > .2) ? 55 : 70;
        iwin().gc2d.setFont(new java.awt.Font("Serif", java.awt.Font.BOLD, size));
        iwin().gc2d.setStroke(new BasicStroke(6)); //толщина линии
        y1 = y1 + dy;
        y2 = y2 + dy;
        iwin().draw.drawLine(x1, y1, x2, y2);
        if (x1 == x2 && y2 - y1 != 0) {
            iwin().draw.drawLine(x1 - 24, y1, x1 + 24, y1);
            iwin().draw.drawLine(x2 - 24, y2, x2 + 24, y2);
            iwin().draw.drawLine(x1, y1, x1 + 12, y1 + 24);
            iwin().draw.drawLine(x1, y1, x1 - 12, y1 + 24);
            iwin().draw.drawLine(x2, y2, x2 + 12, y2 - 24);
            iwin().draw.drawLine(x2, y2, x2 - 12, y2 - 24);
            iwin().gc2d.rotate(Math.toRadians(270), x1 + 60, y1 + (y2 - y1) / 2);
            iwin().gc2d.drawString(UGui.df.format((float) (y2 - y1)), x1 + 60, y1 + (y2 - y1) / 2);
            iwin().gc2d.rotate(Math.toRadians(-270), x1 + 60, y1 + (y2 - y1) / 2);
        } else if (y1 == y2 && x2 - x1 != 0) {
            iwin().draw.drawLine(x1, y1 - 24, x1, y1 + 24);
            iwin().draw.drawLine(x2, y2 - 24, x2, y2 + 24);
            iwin().draw.drawLine(x1, y1, x1 + 24, y1 - 12);
            iwin().draw.drawLine(x1, y1, x1 + 24, y1 + 12);
            iwin().draw.drawLine(x2, y2, x2 - 24, y2 - 12);
            iwin().draw.drawLine(x2, y2, x2 - 24, y2 + 12);
            iwin().gc2d.drawString(UGui.df.format((float) (x2 - x1)), x1 + (x2 - x1) / 2, y2 + 60);
        }
    }

    public void setLocation(ElemFrame frame) {
    }

    public void setSpecific(ElemFrame frm) {
    }
}
