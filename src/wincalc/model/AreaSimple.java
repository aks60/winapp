package wincalc.model;

import domain.eArtikl;
import enums.LayoutArea;
import enums.TypeElem;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import main.Main;
import wincalc.Wincalc;

public class AreaSimple extends Com5t {

    private LayoutArea layout = LayoutArea.FULL; //порядок расположения компонентов в окне
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
            float dh = owner.listChild().get(1).articlRec.getFloat(eArtikl.height) / 2;  //.aheig / 2;
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

    //Обход(схлопывание) соединений area
    public void passJoinArea(HashMap<String, ElemJoining> mapJoin) {

        if (id.equals("6")) {
            
            LayoutArea side = LayoutArea.BOTTOM;
            LayoutArea sideOwner = LayoutArea.NONE;
            
            float X = 0, Y = 0;
            if (side == LayoutArea.TOP) {
                sideOwner = LayoutArea.VERT;
                X = (x2 - x1) / 2;
                Y = y1;
            } else if (side == LayoutArea.BOTTOM) {
                sideOwner = LayoutArea.VERT;
                X = (x2 - x1) / 2;
                Y = y2;
            } else if (side == LayoutArea.LEFT) {
                sideOwner = LayoutArea.HORIZ;
                X = x1;
                Y = (y2 - y1) / 2;
            } else if (side == LayoutArea.RIGHT) {
                sideOwner = LayoutArea.HORIZ;
                X = x2;
                Y = (y2 - y1) / 2;
            }
            System.out.println("aks = " + X + "  " + Y);
            LinkedList<Com5t> listElem = root().listElem(this, TypeElem.FRAME_BOX, TypeElem.FRAME_STV, TypeElem.IMPOST);
            for (Com5t com5t : listElem) {
                if (com5t.inside(X, Y) == true && owner.layout() == sideOwner) {
                    com5t.print();
                }
            }
        }

//        ElemJoining elemJoinVal = null;
//        String key1 = String.valueOf(x1) + ":" + String.valueOf(y1);
//        String key2 = String.valueOf(x2) + ":" + String.valueOf(y1);
//        String key3 = String.valueOf(x2) + ":" + String.valueOf(y2);
//        String key4 = String.valueOf(x1) + ":" + String.valueOf(y2);
//
//        elemJoinVal = mapJoin.get(key1);
//        if (elemJoinVal == null) {
//            mapJoin.put(key1, new ElemJoining(iwin));
//            elemJoinVal = mapJoin.get(key1);
//        }
//        if (elemJoinVal.elemJoinRight == null) {
//            elemJoinVal.elemJoinRight = adjoinedElem(LayoutArea.TOP);
//        }
//        if (elemJoinVal.elemJoinBottom == null) {
//            elemJoinVal.elemJoinBottom = adjoinedElem(LayoutArea.LEFT);
//        }
//
//        elemJoinVal = mapJoin.get(key2);
//        if (elemJoinVal == null) {
//            mapJoin.put(key2, new ElemJoining(iwin));
//            elemJoinVal = mapJoin.get(key2);
//        }
//        if (elemJoinVal.elemJoinLeft == null) {
//            elemJoinVal.elemJoinLeft = adjoinedElem(LayoutArea.TOP);
//        }
//        if (elemJoinVal.elemJoinBottom == null) {
//            elemJoinVal.elemJoinBottom = adjoinedElem(LayoutArea.RIGHT);
//        }
//        
//        elemJoinVal = mapJoin.get(key3);
//        if (elemJoinVal == null) {
//            mapJoin.put(key3, new ElemJoining(iwin));
//            elemJoinVal = mapJoin.get(key3);
//        }
//        if (elemJoinVal.elemJoinTop == null) {
//            elemJoinVal.elemJoinTop = adjoinedElem(LayoutArea.RIGHT);
//        }
//        if (elemJoinVal.elemJoinLeft == null) {
//            elemJoinVal.elemJoinLeft = adjoinedElem(LayoutArea.BOTTOM);
//        }
//
//        elemJoinVal = mapJoin.get(key4);
//        if (elemJoinVal == null) {
//            mapJoin.put(key4, new ElemJoining(iwin));
//            elemJoinVal = mapJoin.get(key4);
//        }
//        if (elemJoinVal.elemJoinTop == null) {
//            elemJoinVal.elemJoinTop = adjoinedElem(LayoutArea.LEFT);
//        }
//        if (elemJoinVal.elemJoinRight == null) {
//            elemJoinVal.elemJoinRight = adjoinedElem(LayoutArea.BOTTOM);
//        }
    }

    // Получить примыкающий элемент (используется при нахождении элементов соединений)
    protected ElemSimple adjoinedElem(LayoutArea layoutSide) {

        LinkedList<Com5t> listElem = owner.listElem(this, TypeElem.AREA, TypeElem.IMPOST);
        for (int index = 0; index < listElem.size(); ++index) {

            Com5t elemBase = listElem.get(index);
            if (elemBase.id != id) {
                continue; //пропускаем если другая ареа
            }
            EnumMap<LayoutArea, ElemFrame> mapFrame = root().mapFrame;
            if (index == 0 && owner.equals(root()) && layoutSide == LayoutArea.TOP && owner.layout() == LayoutArea.VERT && root().typeElem() == TypeElem.ARCH) {
                return mapFrame.get(TypeElem.ARCH);
            } else if (owner.equals(root()) && layoutSide == LayoutArea.TOP && owner.layout() == LayoutArea.HORIZ && root().typeElem() == TypeElem.ARCH) {
                return mapFrame.get(TypeElem.ARCH);
            }

            if (owner.equals(root()) && owner.layout() == LayoutArea.VERT) {
                if (layoutSide == LayoutArea.TOP) {

                    return (index == 0) ? mapFrame.get(layoutSide) : (ElemSimple) listElem.get(index - 1);
                } else if (layoutSide == LayoutArea.BOTTOM) {
                    return (index == listElem.size() - 1) ? mapFrame.get(layoutSide) : (ElemSimple) listElem.get(index + 1);
                } else {
                    return root().mapFrame.get(layoutSide);
                }
            } else if (owner.equals(root()) && owner.layout() == LayoutArea.HORIZ) {
                if (layoutSide == LayoutArea.LEFT) {
                    return (index == 0) ? mapFrame.get(layoutSide) : (ElemSimple) listElem.get(index - 1);
                } else if (layoutSide == LayoutArea.RIGHT) {
                    return (index == listElem.size() - 1) ? mapFrame.get(layoutSide) : (ElemSimple) listElem.get(index + 1);
                } else {
                    return root().mapFrame.get(layoutSide);
                }

            } else {
                if (owner.layout() == LayoutArea.VERT) {
                    if (layoutSide == LayoutArea.TOP) {
                        return (index == 0) ? owner.adjoinedElem(layoutSide) : (ElemSimple) listElem.get(index - 1);
                    } else if (layoutSide == LayoutArea.BOTTOM) {
                        return (index == listElem.size() - 1) ? owner.adjoinedElem(layoutSide) : (ElemSimple) listElem.get(index + 1);
                    } else {
                        return owner.adjoinedElem(layoutSide);
                    }
                } else {
                    if (layoutSide == LayoutArea.LEFT) {
                        return (index == 0) ? owner.adjoinedElem(layoutSide) : (ElemSimple) listElem.get(index - 1);
                    } else if (layoutSide == LayoutArea.RIGHT) {
                        return (index == listElem.size() - 1) ? owner.adjoinedElem(layoutSide) : (ElemSimple) listElem.get(index + 1);
                    } else {
                        return owner.adjoinedElem(layoutSide);
                    }
                }
            }
        }
        return null;
    }

    public ElemFrame addFrame(ElemFrame elemFrame) {
        mapFrame.put(elemFrame.layout(), elemFrame);
        return elemFrame;
    }

    public LayoutArea layout() {
        return layout;
    }

    @Override
    public TypeElem typeElem() {
        return TypeElem.AREA;
    }

    //Прорисовка окна
    public void drawWin(int width, int height) {
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
            mapFrame.get(LayoutArea.TOP).paint();
            mapFrame.get(LayoutArea.BOTTOM).paint();
            mapFrame.get(LayoutArea.LEFT).paint();
            mapFrame.get(LayoutArea.RIGHT).paint();

            //Прорисовка створок
            LinkedList<AreaStvorka> elemStvorkaList = listElem(root(), TypeElem.FULLSTVORKA);
            elemStvorkaList.stream().forEach(el -> el.paint());

            //if (line == true) {
            //Прорисовка размера
            this.drawLine1();
            LinkedList<AreaSimple> areaList = listElem(root(), TypeElem.AREA);
            areaList.stream().forEach(el -> el.drawLine1());
            //}
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
            System.err.println("Ошибка AreaContainer.drawWin() " + s);
        }
    }

    public void drawLine1() {

        float h = iwin.heightAdd - iwin.height;
        if (this == root()) {  //главный контейнер
            float moveV = 80;
            drawLine2(String.format("%.0f", y2 - y1 + h), (int) (x2 + moveV), (int) (y1 - h), (int) (x2 + moveV), (int) y2); //высота окна
            drawLine2(String.format("%.0f", x2 - x1), (int) x1, (int) (y2 + moveV), (int) x2, (int) (y2 + moveV));  //ширина окна

        } else {  //вложенный контейнер
            float moveV = (this.owner == root()) ? 140 : 60;
            if (this.height > 160 && this.width > 160) {
                if (owner.listChild().size() > 1 && owner.layout() == LayoutArea.VERT) {
                    drawLine2(String.format("%.0f", y2 - y1), (int) (x2 + moveV), (int) y1, (int) (x2 + moveV), (int) y2);
                } else if (owner.listChild().size() > 1 && owner.layout() == LayoutArea.HORIZ) {
                    drawLine2(String.format("%.0f", x2 - x1), (int) x1, (int) (y2 + moveV), (int) x2, (int) (y2 + moveV));
                }
            }
        }
    }

    private void drawLine2(String txt, int x1, int y1, int x2, int y2) {
        float h = iwin.heightAdd - iwin.height;
        Graphics2D gc = iwin.graphics2D;
        float scale = iwin.scale;
        gc.setColor(java.awt.Color.BLACK);
        gc.setFont(new java.awt.Font("Serif", java.awt.Font.BOLD, 12));
        strokeLine(x1, y1, x2, y2, Color.BLACK, 2);
        if (x1 == x2) {
            strokeLine(x1 - 24, y1, x1 + 24, y1, Color.BLACK, 2);
            strokeLine(x2 - 24, y2, x2 + 24, y2, Color.BLACK, 2);
            strokeLine(x1, y1, x1 + 12, y1 + 24, Color.BLACK, 2);
            strokeLine(x1, y1, x1 - 12, y1 + 24, Color.BLACK, 2);
            strokeLine(x2, y2, x2 + 12, y2 - 24, Color.BLACK, 2);
            strokeLine(x2, y2, x2 - 12, y2 - 24, Color.BLACK, 2);
            gc.rotate(Math.toRadians(270), (x1 + 28) * scale, (y1 + (y2 - y1) / 2 + h) * scale);
            gc.drawString(txt, (x1 + 28) * scale, (y1 + (y2 - y1) / 2 + h) * scale);
            gc.rotate(Math.toRadians(-270), (x1 + 28) * scale, (y1 + (y2 - y1) / 2 + h) * scale);
        } else {
            strokeLine(x1, y1 - 24, x1, y1 + 24, Color.BLACK, 2);
            strokeLine(x2, y2 - 24, x2, y2 + 24, Color.BLACK, 2);
            strokeLine(x1, y1, x1 + 24, y1 - 12, Color.BLACK, 2);
            strokeLine(x1, y1, x1 + 24, y1 + 12, Color.BLACK, 2);
            strokeLine(x2, y2, x2 - 24, y2 - 12, Color.BLACK, 2);
            strokeLine(x2, y2, x2 - 24, y2 + 12, Color.BLACK, 2);
            //gc.rotate(Math.toRadians(0), (x1 + (x2 - x1) / 2) * scale, (y2 + 28 + h) * scale);
            gc.drawString(txt, (x1 + (x2 - x1) / 2) * scale, (y2 + 28 + h) * scale);
        }
    }
}
