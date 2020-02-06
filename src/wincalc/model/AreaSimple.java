package wincalc.model;

import domain.eArtikl;
import enums.LayoutArea;
import enums.TypeElem;
import enums.JoinLocate;
import enums.JoinVariant;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import main.Main;
import wincalc.Wincalc;

public class AreaSimple extends Com5t {

    public EnumMap<LayoutArea, ElemFrame> mapFrame = new EnumMap<>(LayoutArea.class); //список рам в окне    

    //Конструктор
    public AreaSimple(String id) {
        super(id);
    }

    //Конструктор парсинга скрипта
    public AreaSimple(Wincalc iwin, AreaSimple owner, String id, LayoutArea layout, float width, float height) {
        this(iwin, owner, id, layout, width, height, 1, 1, 1);
        //Коррекция размера стеклопакета(створки) арки.
        //Уменьшение на величину добавленной подкладки над импостом.
        if (owner != null && TypeElem.ARCH == owner.typeElem()
                && owner.listChild().size() == 2
                && TypeElem.IMPOST == owner.listChild().get(1).typeElem()) {
            float dh = owner.listChild().get(1).artiklRec.getFloat(eArtikl.height) / 2;  //.aheig / 2;
            dimension(x1, y1, x2, y2 - dh);
        }
    }

    //Конструктор
    public AreaSimple(Wincalc iwin, AreaSimple owner, String id, LayoutArea layout, float width, float height, int color1, int color2, int color3) {
        super(id);
        this.iwin = iwin;
        this.owner = owner;
        this.layout = layout;
        this.width = width;
        this.height = height;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        initDimension(owner);
    }

    private void initDimension(AreaSimple owner) {
        if (owner != null) {
            //Заполним по умолчанию
            if (LayoutArea.VERT.equals(owner.layout())) { //сверху вниз
                dimension(owner.x1, owner.y1, owner.x2, owner.y1 + height);

            } else if (LayoutArea.HORIZ.equals(owner.layout())) { //слева направо
                dimension(owner.x1, owner.y1, owner.x1 + width, owner.y2);
            }
            //Проверим есть ещё ареа перед текущей, т.к. this area ущё не создана начнём с конца
            for (int index = owner.listChild().size() - 1; index >= 0; --index) {
                if (owner.listChild().get(index) instanceof AreaSimple) {
                    AreaSimple prevArea = (AreaSimple) owner.listChild().get(index);

                    if (LayoutArea.VERT.equals(owner.layout())) { //сверху вниз
                        dimension(prevArea.x1, prevArea.y2, owner.x2, prevArea.y2 + height);

                    } else if (LayoutArea.HORIZ.equals(owner.layout())) { //слева направо
                        dimension(prevArea.x2, prevArea.y1, prevArea.x2 + width, owner.y2);
                    }
                    break; //как только нашел сразу выход
                }
            }
        } else { //для root area
            x2 = x1 + width;
            y2 = y1 + height;
        }
    }

    //Список элементов окна
    public <E> LinkedList<E> listElem(Com5t com5t, TypeElem... type) {

        LinkedList<Com5t> arrElem = new LinkedList(); //список элементов
        LinkedList<E> outElem = new LinkedList(); //выходной список
        for (Map.Entry<LayoutArea, ElemFrame> elemFrame : root().mapFrame.entrySet()) {

            arrElem.add(elemFrame.getValue());
        }
        for (Com5t elemBase : root().listChild()) { //первый уровень
            arrElem.add(elemBase);
            if (elemBase instanceof AreaSimple) {
                for (Map.Entry<LayoutArea, ElemFrame> elemFrame : ((AreaSimple) elemBase).mapFrame.entrySet()) {
                    arrElem.add(elemFrame.getValue());
                }
                for (Com5t elemBase2 : elemBase.listChild()) { //второй уровень
                    arrElem.add(elemBase2);
                    if (elemBase2 instanceof AreaSimple) {
                        for (Map.Entry<LayoutArea, ElemFrame> elemFrame : ((AreaSimple) elemBase2).mapFrame.entrySet()) {
                            arrElem.add(elemFrame.getValue());
                        }
                        for (Com5t elemBase3 : elemBase2.listChild()) { //третий уровень
                            arrElem.add(elemBase3);
                            if (elemBase3 instanceof AreaSimple) {
                                for (Map.Entry<LayoutArea, ElemFrame> elemFrame : ((AreaSimple) elemBase3).mapFrame.entrySet()) {
                                    arrElem.add(elemFrame.getValue());
                                }
                                for (Com5t elemBase4 : elemBase3.listChild()) { //четвёртый уровень
                                    arrElem.add(elemBase4);
                                    if (elemBase4 instanceof AreaSimple) {
                                        for (Map.Entry<LayoutArea, ElemFrame> elemFrame : ((AreaSimple) elemBase4).mapFrame.entrySet()) {
                                            arrElem.add(elemFrame.getValue());
                                        }
                                        for (Com5t elemBase5 : elemBase4.listChild()) { //пятый уровень
                                            arrElem.add(elemBase5);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        //Цикл по входному списку элементов
        for (Com5t elemBase : arrElem) {
            for (int index = 0; index < type.length; ++index) {

                TypeElem type2 = type[index];
                if (elemBase.typeElem() == type2) {
                    E elem = (E) elemBase;
                    outElem.add(elem);
                }
            }
        }
        return outElem;
    }

    public void joinFrame() {
    }

    public void joinElem(HashMap<String, HashSet<ElemSimple>> mapClap, LinkedList<ElemSimple> listElem) {

        //Обход всех соединений текущей Area        
        insideElem(x1, y1, mapClap, listElem);
        insideElem(x1, y2, mapClap, listElem);
        insideElem(x2, y2, mapClap, listElem);
        insideElem(x2, y1, mapClap, listElem);

        //Обход соединений (xy -> element1, element2)
        for (Map.Entry<String, HashSet<ElemSimple>> it : mapClap.entrySet()) {

            HashSet<ElemSimple> setElem = it.getValue();
            ElemSimple arrElem[] = setElem.stream().toArray(ElemSimple[]::new);
            ElemJoining el = new ElemJoining(iwin);
            String pk = it.getKey();

            //В соединении элемента рамы и импост (T - соединение)
            if (((arrElem[0].typeElem() == TypeElem.FRAME_BOX && arrElem[1].typeElem() == TypeElem.FRAME_BOX)
                    || (arrElem[0].typeElem() == TypeElem.FRAME_STV && arrElem[1].typeElem() == TypeElem.FRAME_STV)) == false) {

                iwin.mapJoin.put(pk, el);
                ElemSimple arrElem2[][] = {{arrElem[0], arrElem[1]}, {arrElem[1], arrElem[0]}}; //варианты общих точек пересечения
                for (ElemSimple[] indexEl : arrElem2) {
                    ElemSimple e1 = indexEl[1];
                    ElemSimple e2 = indexEl[0];

                    //Сторона пересечения одного из элементов, sides[][ном.стороны][коорд.стороны], layout -> 0-LEFT, 1-BOTTOM, 2-RIGHT, 3-TOP 
                    float sides[][][] = {{{e2.x1, e2.y1}, {e2.x1, e2.y2}}, {{e2.x1, e2.y2}, {e2.x2, e2.y2}}, {{e2.x2, e2.y2}, {e2.x2, e2.y1}}, {{e2.x1, e2.y1}, {e2.x2, e2.y1}}};
                    for (int index = 0; index < sides.length; index++) {

                        el.id = id + "." + (index + 1) + "T";
                        float[][] fs = sides[index];
                        if (e1.inside(fs[0][0], fs[0][1]) && e1.inside(fs[1][0], fs[1][1])) {
                            el.varJoin = JoinVariant.VAR4;
                            if (index == 0) {
                                el.name = "T - соединение левое";
                                el.typeJoin = JoinLocate.TLEFT;
                                el.joinElement1 = e2;
                                el.joinElement2 = e1;
                            } else if (index == 1) {
                                el.name = "T - соединение нижнее";
                                el.typeJoin = JoinLocate.TBOT;
                                el.joinElement1 = e2;
                                el.joinElement2 = e1;
                            } else if (index == 2) {
                                el.name = "T - соединение правое";
                                el.typeJoin = JoinLocate.TRIGH;
                                el.joinElement1 = e2;
                                el.joinElement2 = e1;
                            } else if (index == 3) {
                                el.name = "T - соединение верхнее";
                                el.typeJoin = JoinLocate.TTOP;
                                el.joinElement1 = e1;
                                el.joinElement2 = e2;
                            }
                        }
                    }
                }
            }
        }
    }

    private void insideElem(float x, float y, HashMap<String, HashSet<ElemSimple>> map, LinkedList<ElemSimple> elems) {

        String k = x + ":" + y;
        if (map.get(k) == null) {
            map.put(k, new HashSet());
        }
        for (ElemSimple elem : elems) {
            if (elem.inside(x, y) == true) {
                map.get(k).add(elem);
            }
        }
    }

    public ElemFrame addFrame(ElemFrame elemFrame) {
        mapFrame.put(elemFrame.layout(), elemFrame);
        return elemFrame;
    }

    @Override
    public TypeElem typeElem() {
        return TypeElem.AREA;
    }

    //Рисуем конструкцию
    public void draw(int width, int height) {
        try {
            Graphics2D gc = iwin.graphics2D;
            //gc.setColor(new java.awt.Color(212,208,200));
            gc.fillRect(0, 0, width, height);

            //Прорисовка стеклопакетов
            LinkedList<ElemGlass> elemGlassList = listElem(root(), TypeElem.GLASS);
            elemGlassList.stream().forEach(el -> el.paint());

            //Прорисовка импостов
            LinkedList<ElemImpost> elemImpostList = listElem(root(), TypeElem.IMPOST);
            elemImpostList.stream().forEach(el -> el.paint());

            //Прорисовка рам
            if (TypeElem.ARCH == typeElem()) {
                mapFrame.get(LayoutArea.ARCH).paint();
            } else {
                mapFrame.get(LayoutArea.TOP).paint();
            }
            mapFrame.get(LayoutArea.BOTTOM).paint();
            mapFrame.get(LayoutArea.LEFT).paint();
            mapFrame.get(LayoutArea.RIGHT).paint();

            //Прорисовка створок
            LinkedList<AreaStvorka> elemStvorkaList = listElem(root(), TypeElem.FULLSTVORKA);
            elemStvorkaList.stream().forEach(el -> el.paint());

            //Прорисовка размера
            float h = iwin.heightAdd - iwin.height;
            drawLine2(String.format("%.0f", y2 - y1 + h), (int) (x2 + 60), (int) (y1 - h), (int) (x2 + 60), (int) y2); //высота окна
            drawLine2(String.format("%.0f", x2 - x1), (int) x1, (int) (y2 + 60), (int) x2, (int) (y2 + 60));  //ширина окна
            LinkedList<AreaSimple> areaList = listElem(root(), TypeElem.AREA);
            int dx = 80;
            areaList.stream().forEach(el -> el.drawLine1(dx + 60));

            //Рисунок в память
            if (iwin.bufferImg != null) {
                ByteArrayOutputStream bosFill = new ByteArrayOutputStream();
                ImageIO.write(iwin.bufferImg, "png", bosFill);
                iwin.bufferByte = bosFill.toByteArray();

                if (Main.dev == true) {
                    File outputfile = new File("CanvasImage.png");
                    ImageIO.write(iwin.bufferImg, "png", outputfile);
                }
            }
        } catch (Exception s) {
            System.err.println("Ошибка AreaSimple.drawWin() " + s);
        }
    }

    private void drawLine1(float moveV) {

        float h = iwin.heightAdd - iwin.height;
        Graphics2D gc = iwin.graphics2D;
        gc.setColor(java.awt.Color.BLACK);
        gc.setFont(new java.awt.Font("Serif", java.awt.Font.BOLD, 50));

        //вложенный контейнер
        if (this.height > 160 && this.width > 160) {
            if (owner.layout() == LayoutArea.VERT) {

                int x1a = (int) (x2 + moveV), y1a = (int) y1, x2a = (int) (x2 + moveV), y2a = (int) y2;
                strokeLine(x1a, y1a, x2a, y2a, Color.BLACK);
                strokeLine(x1a - 24, y1a, x1a + 24, y1a, Color.BLACK);
                strokeLine(x2a - 24, y2a, x2a + 24, y2a, Color.BLACK);
                strokeLine(x1a, y1a, x1a + 12, y1a + 24, Color.BLACK);
                strokeLine(x1a, y1a, x1a - 12, y1a + 24, Color.BLACK);
                strokeLine(x2a, y2a, x2a + 12, y2a - 24, Color.BLACK);
                strokeLine(x2a, y2a, x2a - 12, y2a - 24, Color.BLACK);
                gc.rotate(Math.toRadians(270), x1a + 60, y1a + (y2a - y1a) / 2 + h);
                gc.drawString(String.format("%.0f", y2 - y1), x1a + 60, y1a + (y2a - y1a) / 2 + h);
                gc.rotate(Math.toRadians(-270), x1a + 60, y1a + (y2a - y1a) / 2 + h);

            } else if (owner.layout() == LayoutArea.HORIZ) {              
                int x1b = (int) x1, y1b = (int) (y2 + moveV), x2b = (int) x2, y2b = (int) (y2 + moveV);
                strokeLine(x1b, y1b, x2b, y2b, Color.BLACK);
                strokeLine(x1b, y1b - 24, x1b, y1b + 24, Color.BLACK);
                strokeLine(x2b, y2b - 24, x2b, y2b + 24, Color.BLACK);
                strokeLine(x1b, y1b, x1b + 24, y1b - 12, Color.BLACK);
                strokeLine(x1b, y1b, x1b + 24, y1b + 12, Color.BLACK);
                strokeLine(x2b, y2b, x2b - 24, y2b - 12, Color.BLACK);
                strokeLine(x2b, y2b, x2b - 24, y2b + 12, Color.BLACK);
                gc.drawString(String.format("%.0f", x2 - x1), x1b + (x2b - x1b) / 2, y2b + 60 + h);
            }
        }
    }

    private void drawLine2(String txt, int x1b, int y1b, int x2b, int y2b) {
        float h = iwin.heightAdd - iwin.height;
        Graphics2D gc = iwin.graphics2D;
        gc.setColor(java.awt.Color.BLACK);
        gc.setFont(new java.awt.Font("Serif", java.awt.Font.BOLD, 50));
        strokeLine(x1b, y1b, x2b, y2b, Color.BLACK);
        if (x1b == x2b) {
            strokeLine(x1b - 24, y1b, x1b + 24, y1b, Color.BLACK);
            strokeLine(x2b - 24, y2b, x2b + 24, y2b, Color.BLACK);
            strokeLine(x1b, y1b, x1b + 12, y1b + 24, Color.BLACK);
            strokeLine(x1b, y1b, x1b - 12, y1b + 24, Color.BLACK);
            strokeLine(x2b, y2b, x2b + 12, y2b - 24, Color.BLACK);
            strokeLine(x2b, y2b, x2b - 12, y2b - 24, Color.BLACK);
            gc.rotate(Math.toRadians(270), x1b + 60, y1b + (y2b - y1b) / 2 + h);
            gc.drawString(txt, x1b + 60, y1b + (y2b - y1b) / 2 + h);
            gc.rotate(Math.toRadians(-270), x1b + 60, y1b + (y2b - y1b) / 2 + h);
        } else {
            strokeLine(x1b, y1b - 24, x1b, y1b + 24, Color.BLACK);
            strokeLine(x2b, y2b - 24, x2b, y2b + 24, Color.BLACK);
            strokeLine(x1b, y1b, x1b + 24, y1b - 12, Color.BLACK);
            strokeLine(x1b, y1b, x1b + 24, y1b + 12, Color.BLACK);
            strokeLine(x2b, y2b, x2b - 24, y2b - 12, Color.BLACK);
            strokeLine(x2b, y2b, x2b - 24, y2b + 12, Color.BLACK);
            gc.drawString(txt, x1b + (x2b - x1b) / 2, y2b + 60 + h);
        }
    }
}
