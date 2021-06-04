package builder.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dataset.Record;
import domain.eArtikl;
import domain.eParams;
import domain.eSysprof;
import enums.LayoutArea;
import enums.TypeElem;
import enums.LayoutJoin;
import enums.TypeJoin;
import enums.UseArtiklTo;
import enums.UseSide;
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
import common.Util;
import enums.PKjson;
import frames.Uti4;
import java.awt.BasicStroke;
import java.util.Map;

public class AreaSimple extends Com5t {

    public EnumMap<LayoutArea, ElemFrame> mapFrame = new EnumMap<>(LayoutArea.class); //список рам в окне  

    public AreaSimple(Wincalc iwin, AreaSimple owner, float id, TypeElem typeElem, LayoutArea layout, float width, float height, int color1, int color2, int color3, String param) {
        super(id, iwin, owner);
        this.type = typeElem;
        this.layout = layout;
        this.colorID1 = color1;
        this.colorID2 = color2;
        this.colorID3 = color3;
        initСonstructiv(param);
        initDimension(width, height);
        initParametr(param);
    }

    public void initСonstructiv(String param) {
        if (TypeElem.AREA != type) {
            //Профили коробки или створки
            if (param(param, PKjson.sysprofID) != -1) {
                sysprofRec = eSysprof.find3(param(param, PKjson.sysprofID));
            } else {
                if (this instanceof AreaStvorka) {
                    sysprofRec = eSysprof.find4(iwin().nuni, UseArtiklTo.STVORKA, UseSide.ANY);
                } else {
                    sysprofRec = eSysprof.find4(iwin().nuni, UseArtiklTo.FRAME, UseSide.ANY);
                }
            }
            artiklRec = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), false);
            artiklRecAn = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), true);
        }
    }

    protected void initDimension(float width, float height) {

        if (owner() == null) { //для root area
            setDimension(0, iwin().heightAdd - iwin().height, width, iwin().heightAdd - iwin().height + height);

        } else {
            //Первая area добавляемая в area владельца
            if (owner().listChild.isEmpty() == true) {
                if (LayoutArea.VERT.equals(owner().layout())) { //сверху вниз
                    setDimension(owner().x1, owner().y1, owner().x2, owner().y1 + height);
                } else if (LayoutArea.HORIZ.equals(owner().layout())) { //слева направо
                    setDimension(owner().x1, owner().y1, owner().x1 + width, owner().y2);
                }

            } else { //Aреа перед текущей, т.к. this area ёщё не создана начнём с конца
                for (int index = owner().listChild.size() - 1; index >= 0; --index) {
                    if (owner().listChild.get(index).type == TypeElem.AREA) {
                        AreaSimple prevArea = (AreaSimple) owner().listChild.get(index);
                        //Если последняя доб. area выходит за коорд. root area. Происходит при подкдадке ареа над импостом 
                        if (LayoutArea.VERT.equals(owner().layout())) { //сверху вниз                            
                            float Y2 = (prevArea.y2 + height > root().y2) ? root().y2 : prevArea.y2 + height;
                            setDimension(owner().x1, prevArea.y2, owner().x2, Y2);

                        } else if (LayoutArea.HORIZ.equals(owner().layout())) { //слева направо
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
                        iwin().mapPardef.put(paramRec.getInt(eParams.params_id), paramRec);
                    });
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка:Com5t.parsingParam() " + e);
        }
    }

    //Список элементов окна
    public <E> LinkedList<E> listElem(TypeElem... type) {
        LinkedList<E> list = new LinkedList();
        listElem(this, list, Arrays.asList(type));
        return list;
    }

    private <E> void listElem(Com5t com5t, LinkedList<E> list, List<TypeElem> type) {

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
        com5t.listChild.forEach(comp -> listElem(comp, list, type));
    }

    public void joinFrame() {
    }

    public void joinElem() {

        List<ElemSimple> impList = listElem(TypeElem.IMPOST, TypeElem.SHTULP);
        List<ElemSimple> elemList = listElem(TypeElem.FRAME_SIDE, TypeElem.IMPOST, TypeElem.SHTULP);

        //Цикл по импостам
        for (ElemSimple elemImp : impList) {
            //Цикл по сторонам рамы и импостам (т.к. в створке Т-обр. соединений нет)
            for (ElemSimple elem5e : elemList) {
                if (elem5e.layout != LayoutArea.ARCH) { //для арки inside() не работает

                    elemImp.anglCut[0] = 90;
                    elemImp.anglCut[1] = 90;

                    //Импосты(штульпы...)  расположены по горизонтали слева на право
                    if (elemImp.owner().layout() == LayoutArea.HORIZ) {
                        elemImp.anglHoriz = 90;
                        if (elem5e.inside(elemImp.x2, elemImp.y2) == true
                                && iwin().mapJoin.get(elemImp.joinPoint(0)) == null) { //T - соединение нижнее                              
                            ElemJoining el = new ElemJoining(id() + 1f / 100, TypeJoin.VAR40, LayoutJoin.TBOT, elemImp, elem5e, 90);
                            iwin().mapJoin.put(elemImp.joinPoint(0), el);

                        } else if (elem5e.inside(elemImp.x1, elemImp.y1) == true
                                && iwin().mapJoin.get(elemImp.joinPoint(1)) == null) { //T - соединение верхнее                            
                            ElemJoining el = new ElemJoining(id() + 2f / 100, TypeJoin.VAR40, LayoutJoin.TTOP, elemImp, elem5e, 90);
                            iwin().mapJoin.put(elemImp.joinPoint(1), el);
                        }

                        //Импосты(штульпы...) расположены по вертикали снизу вверх и слева направо
                    } else {
                        elemImp.anglHoriz = 0;
                        if (elem5e.inside(elemImp.x1, elemImp.y1) == true
                                && iwin().mapJoin.get(elemImp.joinPoint(0)) == null) { //T - соединение левое                             
                            ElemJoining el = new ElemJoining(id() + 3f / 100, TypeJoin.VAR40, LayoutJoin.TLEFT, elemImp, elem5e, 90);
                            iwin().mapJoin.put(elemImp.joinPoint(0), el);

                        } else if (elem5e.inside(elemImp.x2, elemImp.y2) == true
                                && iwin().mapJoin.get(elemImp.joinPoint(1)) == null) { //T - соединение правое                              
                            ElemJoining el = new ElemJoining(id() + 4f / 100, TypeJoin.VAR40, LayoutJoin.TRIGH, elemImp, elem5e, 90);
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
            LinkedList<ElemGlass> elemGlassList = root().listElem(TypeElem.GLASS);
            elemGlassList.stream().forEach(el -> el.paint());

            //Прорисовка импостов
            LinkedList<ElemImpost> elemImpostList = root().listElem(TypeElem.IMPOST);
            elemImpostList.stream().forEach(el -> el.paint());

            //Прорисовка штульпов
            LinkedList<ElemShtulp> elemShtulpList = root().listElem(TypeElem.SHTULP);
            elemShtulpList.stream().forEach(el -> el.paint());

            //Прорисовка рам
            if (TypeElem.ARCH == type) {
                mapFrame.get(LayoutArea.ARCH).paint();
            } else {
                mapFrame.get(LayoutArea.TOP).paint();
            }
            mapFrame.get(LayoutArea.BOTT).paint();
            mapFrame.get(LayoutArea.LEFT).paint();
            mapFrame.get(LayoutArea.RIGHT).paint();

            //Прорисовка створок
            LinkedList<AreaStvorka> elemStvorkaList = root().listElem(TypeElem.STVORKA);
            elemStvorkaList.stream().forEach(el -> el.paint());

            //Прорисовка размера  
            if (iwin().scale > 0.1) {
                LinkedList<Float> ls1 = new LinkedList(Arrays.asList(x1, x2)), ls2 = new LinkedList(Arrays.asList(y1, y2));
                LinkedList<ElemImpost> impostList = root().listElem(TypeElem.IMPOST, TypeElem.SHTULP);
                for (ElemSimple impostElem : impostList) { //по импостам определим точки разрыва линии
                    if (LayoutArea.VERT == impostElem.owner().layout) {
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
                    line(x1, (iwin().heightAdd + mov), x2, (iwin().heightAdd + mov), 0);
                }
                for (int i = 1; i < ls2.size(); i++) {
                    float y1 = ls2.get(i - 1), y2 = ls2.get(i);
                    line((this.x2 + mov), y1, (this.x2 + mov), y2, 0);
                }
                if (ls1.size() > 2) { //линия общей ширины
                    line(root().x1, iwin().heightAdd + mov * 2, root().x2, iwin().heightAdd + mov * 2, 0);
                }
                if (ls2.size() > 2) { //линия общей высоты
                    line(iwin().width + mov * 2, 0, iwin().width + mov * 2, iwin().heightAdd, 0);
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
            iwin().gc2d.drawString(Uti4.df.format((float) (y2 - y1)), x1 + 60, y1 + (y2 - y1) / 2);
            iwin().gc2d.rotate(Math.toRadians(-270), x1 + 60, y1 + (y2 - y1) / 2);
        } else if (y1 == y2 && x2 - x1 != 0) {
            iwin().draw.drawLine(x1, y1 - 24, x1, y1 + 24);
            iwin().draw.drawLine(x2, y2 - 24, x2, y2 + 24);
            iwin().draw.drawLine(x1, y1, x1 + 24, y1 - 12);
            iwin().draw.drawLine(x1, y1, x1 + 24, y1 + 12);
            iwin().draw.drawLine(x2, y2, x2 - 24, y2 - 12);
            iwin().draw.drawLine(x2, y2, x2 - 24, y2 + 12);
            iwin().gc2d.drawString(Uti4.df.format((float) (x2 - x1)), x1 + (x2 - x1) / 2, y2 + 60);
        }
    }
}
