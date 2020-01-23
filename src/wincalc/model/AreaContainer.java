package wincalc.model;

import domain.eArtikl;
import domain.eColor;
import enums.LayoutArea;
import enums.TypeElem;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javafx.scene.shape.ArcType;
import javax.imageio.ImageIO;
import main.Main;
import wincalc.Wincalc;

public abstract class AreaContainer extends Com5t {

    private LinkedList<Com5t> listChild = new LinkedList(); //список компонентов в окне

    private LayoutArea layout = LayoutArea.FULL; //порядок расположения компонентов в окне
    public EnumMap<LayoutArea, ElemFrame> mapFrame = new EnumMap<>(LayoutArea.class); //список рам в окне    

    /**
     * Конструктор
     */
    public AreaContainer(String id) {
        super(id);
    }

    /**
     * Конструктор парсинга скрипта
     */
    public AreaContainer(Wincalc iwin, AreaContainer owner, String id, LayoutArea layout, float width, float height) {
        this(owner, id, layout, width, height, 1, 1, 1);
        this.iwin = iwin;
        //Коррекция размера стеклопакета(створки) арки.
        //Уменьшение на величину добавленной подкладки над импостом.
        if (owner != null && TypeElem.ARCH == owner.typeElem()
                && owner.listChild().size() == 2
                && TypeElem.IMPOST == owner.listChild().get(1).typeElem()) {
            float dh = owner.listChild().get(1).articlRec.getFloat(eArtikl.height) / 2;  //.aheig / 2;
            dimension(x1, y1, x2, y2 - dh);
        }
    }

    /**
     * Конструктор
     */
    public AreaContainer(AreaContainer owner, String id, LayoutArea layout, float width, float height, int color1, int color2, int color3) {
        super(id);
        this.owner = owner;
        this.layout = layout;
        this.width = width;
        this.height = height;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        initDimension(owner);
    }

    private void initDimension(AreaContainer owner) {
        if (owner != null) {
            //Заполним по умолчанию
            if (LayoutArea.VERTICAL.equals(owner.layout())) { //сверху вниз
                dimension(owner.x1, owner.y1, owner.x2, owner.y1 + height);

            } else if (LayoutArea.HORIZONTAL.equals(owner.layout())) { //слева направо
                dimension(owner.x1, owner.y1, owner.x1 + width, owner.y2);
            }
            //Проверим есть ещё ареа перед текущей, т.к. this area ущё не создана начнём с конца
            for (int index = owner.listChild().size() - 1; index >= 0; --index) {
                if (owner.listChild().get(index) instanceof AreaContainer) {
                    AreaContainer prevArea = (AreaContainer) owner.listChild().get(index);

                    if (LayoutArea.VERTICAL.equals(owner.layout())) { //сверху вниз
                        dimension(prevArea.x1, prevArea.y2, owner.x2, prevArea.y2 + height);

                    } else if (LayoutArea.HORIZONTAL.equals(owner.layout())) { //слева направо
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

    /**
     * Обход(схлопывание) соединений area
     */
    public void passJoinArea(HashMap<String, ElemJoinig> hmJoin) {

        ElemJoinig elemJoinVal = null;
        String key1 = String.valueOf(x1) + ":" + String.valueOf(y1);
        String key2 = String.valueOf(x2) + ":" + String.valueOf(y1);
        String key3 = String.valueOf(x2) + ":" + String.valueOf(y2);
        String key4 = String.valueOf(x1) + ":" + String.valueOf(y2);

        //if (getAdjoinedElem(LayoutArea.TOP) instanceof AreaSimple) {
        elemJoinVal = hmJoin.get(key1);
        if (elemJoinVal == null) {
            hmJoin.put(key1, new ElemJoinig(iwin));
            elemJoinVal = hmJoin.get(key1);
        }
        if (elemJoinVal.elemJoinRight == null) {
            elemJoinVal.elemJoinRight = adjoinedElem(LayoutArea.TOP);
        }
        if (elemJoinVal.elemJoinBottom == null) {
            elemJoinVal.elemJoinBottom = adjoinedElem(LayoutArea.LEFT);
        }

        elemJoinVal = hmJoin.get(key2);
        if (elemJoinVal == null) {
            hmJoin.put(key2, new ElemJoinig(iwin));
            elemJoinVal = hmJoin.get(key2);
        }
        if (elemJoinVal.elemJoinLeft == null) {
            elemJoinVal.elemJoinLeft = adjoinedElem(LayoutArea.TOP);
        }
        if (elemJoinVal.elemJoinBottom == null) {
            elemJoinVal.elemJoinBottom = adjoinedElem(LayoutArea.RIGHT);
        }
        //}
        elemJoinVal = hmJoin.get(key3);
        if (elemJoinVal == null) {
            hmJoin.put(key3, new ElemJoinig(iwin));
            elemJoinVal = hmJoin.get(key3);
        }
        if (elemJoinVal.elemJoinTop == null) {
            elemJoinVal.elemJoinTop = adjoinedElem(LayoutArea.RIGHT);
        }
        if (elemJoinVal.elemJoinLeft == null) {
            elemJoinVal.elemJoinLeft = adjoinedElem(LayoutArea.BOTTOM);
        }

        elemJoinVal = hmJoin.get(key4);
        if (elemJoinVal == null) {
            hmJoin.put(key4, new ElemJoinig(iwin));
            elemJoinVal = hmJoin.get(key4);
        }
        if (elemJoinVal.elemJoinTop == null) {
            elemJoinVal.elemJoinTop = adjoinedElem(LayoutArea.LEFT);
        }
        if (elemJoinVal.elemJoinRight == null) {
            elemJoinVal.elemJoinRight = adjoinedElem(LayoutArea.BOTTOM);
        }
    }

    /**
     * Получить примыкающий элемент (используется при нахождении элементов
     * соединений)
     */
    protected ElemComp adjoinedElem(LayoutArea layoutSide) {

        LinkedList<Com5t> listElem = areaOrImpostList();
        for (int index = 0; index < listElem.size(); ++index) {

            Com5t elemBase = listElem.get(index);
            if (elemBase.id != id) {
                continue; //пропускаем если другая ареа
            }
            EnumMap<LayoutArea, ElemFrame> mapFrame = root().mapFrame;
            if (index == 0 && owner.equals(root()) && layoutSide == LayoutArea.TOP && owner.layout() == LayoutArea.VERTICAL && root().typeElem() == TypeElem.ARCH) {
                return mapFrame.get(TypeElem.ARCH);
            } else if (owner.equals(root()) && layoutSide == LayoutArea.TOP && owner.layout() == LayoutArea.HORIZONTAL && root().typeElem() == TypeElem.ARCH) {
                return mapFrame.get(TypeElem.ARCH);
            }

            if (owner.equals(root()) && owner.layout() == LayoutArea.VERTICAL) {
                if (layoutSide == LayoutArea.TOP) {

                    return (index == 0) ? mapFrame.get(layoutSide) : (ElemComp) listElem.get(index - 1);
                } else if (layoutSide == LayoutArea.BOTTOM) {
                    return (index == listElem.size() - 1) ? mapFrame.get(layoutSide) : (ElemComp) listElem.get(index + 1);
                } else {
                    return root().mapFrame.get(layoutSide);
                }
            } else if (owner.equals(root()) && owner.layout() == LayoutArea.HORIZONTAL) {
                if (layoutSide == LayoutArea.LEFT) {
                    return (index == 0) ? mapFrame.get(layoutSide) : (ElemComp) listElem.get(index - 1);
                } else if (layoutSide == LayoutArea.RIGHT) {
                    return (index == listElem.size() - 1) ? mapFrame.get(layoutSide) : (ElemComp) listElem.get(index + 1);
                } else {
                    return root().mapFrame.get(layoutSide);
                }

            } else {
                if (owner.layout() == LayoutArea.VERTICAL) {
                    if (layoutSide == LayoutArea.TOP) {
                        return (index == 0) ? owner.adjoinedElem(layoutSide) : (ElemComp) listElem.get(index - 1);
                    } else if (layoutSide == LayoutArea.BOTTOM) {
                        return (index == listElem.size() - 1) ? owner.adjoinedElem(layoutSide) : (ElemComp) listElem.get(index + 1);
                    } else {
                        return owner.adjoinedElem(layoutSide);
                    }
                } else {
                    if (layoutSide == LayoutArea.LEFT) {
                        return (index == 0) ? owner.adjoinedElem(layoutSide) : (ElemComp) listElem.get(index - 1);
                    } else if (layoutSide == LayoutArea.RIGHT) {
                        return (index == listElem.size() - 1) ? owner.adjoinedElem(layoutSide) : (ElemComp) listElem.get(index + 1);
                    } else {
                        return owner.adjoinedElem(layoutSide);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Список элементов окна
     */
    public <E> LinkedList<E> listElem(TypeElem... type) {

        LinkedList<Com5t> arrElem = new LinkedList(); //список элементов
        LinkedList<E> outElem = new LinkedList(); //выходной список
        for (Map.Entry<LayoutArea, ElemFrame> elemFrame : root().mapFrame.entrySet()) {

            arrElem.add(elemFrame.getValue());
        }
        for (Com5t elemBase : root().listChild()) { //первый уровень
            arrElem.add(elemBase);
            if (elemBase instanceof AreaContainer) {
                for (Map.Entry<LayoutArea, ElemFrame> elemFrame : ((AreaContainer) elemBase).mapFrame.entrySet()) {
                    arrElem.add(elemFrame.getValue());
                }
                for (Com5t elemBase2 : elemBase.listChild()) { //второй уровень
                    arrElem.add(elemBase2);
                    if (elemBase2 instanceof AreaContainer) {
                        for (Map.Entry<LayoutArea, ElemFrame> elemFrame : ((AreaContainer) elemBase2).mapFrame.entrySet()) {
                            arrElem.add(elemFrame.getValue());
                        }
                        for (Com5t elemBase3 : elemBase2.listChild()) { //третий уровень
                            arrElem.add(elemBase3);
                            if (elemBase3 instanceof AreaContainer) {
                                for (Map.Entry<LayoutArea, ElemFrame> elemFrame : ((AreaContainer) elemBase3).mapFrame.entrySet()) {
                                    arrElem.add(elemFrame.getValue());
                                }
                                for (Com5t elemBase4 : elemBase3.listChild()) { //четвёртый уровень
                                    arrElem.add(elemBase4);
                                    if (elemBase4 instanceof AreaContainer) {
                                        for (Map.Entry<LayoutArea, ElemFrame> elemFrame : ((AreaContainer) elemBase4).mapFrame.entrySet()) {
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
        for (int index = 0; index < type.length; ++index) {
            TypeElem type2 = type[index];
            for (Com5t elemBase : arrElem) {
                if (elemBase.typeElem() == type2) {
                    E elem = (E) elemBase;
                    outElem.add(elem);
                }
            }
        }
        return outElem;
    }

    public abstract void joinFrame();

    @Override
    public LinkedList<Com5t> listChild() {
        return listChild;
    }

    public void passJoinFrame() {
    }

    public void addElem(Com5t element) {
        listChild.add(element);
    }

    public ElemFrame addFrame(ElemFrame elemFrame) {
        mapFrame.put(elemFrame.layout(), elemFrame);
        return elemFrame;
    }

    public LayoutArea layout() {
        return layout;
    }

    /**
     * Список area и impost
     */
    public LinkedList<Com5t> areaOrImpostList() {

        LinkedList<Com5t> elemList = new LinkedList();
        for (Com5t elemBase : owner.listChild()) {
            if (TypeElem.AREA == elemBase.typeElem() || TypeElem.IMPOST == elemBase.typeElem()) {
                elemList.add(elemBase);
            }
        }
        return elemList;
    }

    public void drawWin(float scale, byte[] buffer, boolean line) {
        try {
            iwin.scale = scale;
            BufferedImage image = iwin.img;
            Graphics2D gc = (Graphics2D) image.getGraphics();
            gc.setColor(java.awt.Color.WHITE);
            gc.fillRect(0, 0, image.getWidth(), image.getHeight());

//            //Прорисовка стеклопакетов
//            LinkedList<ElemGlass> elemGlassList = listElem(TypeElem.GLASS);
//            elemGlassList.stream().forEach(el -> el.drawElem());
//
//            //Прорисовка импостов
//            LinkedList<ElemImpost> elemImpostList = listElem(TypeElem.IMPOST);
//            elemImpostList.stream().forEach(el -> el.drawElem());

            //Прорисовка рам
            if (TypeElem.ARCH == this.typeElem()) {
                //TODO для прорисовки арки добавил один градус, а это не айс!
                //Прорисовка арки
                ElemFrame ef = mapFrame.get(LayoutArea.ARCH);
                float dz = ef.articlRec.getFloat(eArtikl.height);
                double r = ((AreaArch) root()).radiusArch;
                int rgb = eColor.up.find(ef.color3).getInt(eColor.color);
                double ang1 = 90 - Math.toDegrees(Math.asin(width / (r * 2)));
                double ang2 = 90 - Math.toDegrees(Math.asin((width - 2 * dz) / ((r - dz) * 2)));
                strokeArc(width / 2 - r, 0, r * 2, r * 2, ang1, (90 - ang1) * 2 + 1, ArcType.OPEN, 0, 3); //прорисовка на сцену
                strokeArc(width / 2 - r + dz, dz, (r - dz) * 2, (r - dz) * 2, ang2, (90 - ang2) * 2 + 1, ArcType.OPEN, 0, 3); //прорисовка на сцену
                strokeArc(width / 2 - r + dz / 2, dz / 2, (r - dz / 2) * 2, (r - dz / 2) * 2, ang2, (90 - ang2) * 2 + 1, ArcType.OPEN, rgb, dz - 4); //прорисовка на сцену
            } else {
                mapFrame.get(LayoutArea.TOP).paint();
            }
            mapFrame.get(LayoutArea.BOTTOM).paint();
            mapFrame.get(LayoutArea.LEFT).paint();
            mapFrame.get(LayoutArea.RIGHT).paint();
//
//            //Прорисовка створок
//            LinkedList<AreaStvorka> elemStvorkaList = listElem(TypeElem.FULLSTVORKA);
//            elemStvorkaList.stream().forEach(el -> el.drawElem());

//            if (line == true) {
//                //Прорисовка размера
//                this.drawLineLength();
//                LinkedList<AreaContainer> areaList = listElem(TypeElem.AREA);
//                areaList.stream().forEach(el -> el.drawLineLength());
//            }
            //Рисунок в память
            ByteArrayOutputStream bosFill = new ByteArrayOutputStream();
            ImageIO.write(image, "png", bosFill);
            buffer = bosFill.toByteArray();

            if (Main.dev == true) {
                File outputfile = new File("CanvasImage.png");
                ImageIO.write(image, "png", outputfile);
            }

        } catch (Exception s) {
            System.err.println("Ошибка AreaContainer.drawWin() " + s);
        }
    }

    /**
     * Прорисовка размеров окна
     */
    public void lineLength1() {

        float h = iwin.heightAdd - iwin.height;
        if (this == root()) {  //главный контейнер
            float moveV = 180;
            lineLength2(String.format("%.0f", y2 - y1 + h), (int) (x2 + moveV), (int) (y1 - h), (int) (x2 + moveV), (int) y2); //высота окна
            lineLength2(String.format("%.0f", x2 - x1), (int) x1, (int) (y2 + moveV), (int) x2, (int) (y2 + moveV));  //ширина окна

        } else {  //вложенный контейнер
            float moveV = (this.owner == root()) ? 120 : 60;
            if (this.height > 160 && this.width > 160) {
                if (owner.listChild().size() > 1 && owner.layout() == LayoutArea.VERTICAL) {
                    lineLength2(String.format("%.0f", y2 - y1), (int) (x2 + moveV), (int) y1, (int) (x2 + moveV), (int) y2);
                } else if (owner.listChild().size() > 1 && owner.layout() == LayoutArea.HORIZONTAL) {
                    lineLength2(String.format("%.0f", x2 - x1), (int) x1, (int) (y2 + moveV), (int) x2, (int) (y2 + moveV));
                }
            }
        }
    }

    private void lineLength2(String txt, int x1, int y1, int x2, int y2) {
        float h = iwin.heightAdd - iwin.height;
        Graphics2D gc = iwin.img.createGraphics();
        gc.setColor(java.awt.Color.BLACK);
        gc.setFont(new java.awt.Font("Serif", java.awt.Font.BOLD, 40));
        strokeLine(x1, y1, x2, y2, Color.BLACK, 2);
        if (x1 == x2) {
            strokeLine(x1 - 24, y1, x1 + 24, y1, Color.BLACK, 2);
            strokeLine(x2 - 24, y2, x2 + 24, y2, Color.BLACK, 2);
            strokeLine(x1, y1, x1 + 12, y1 + 24, Color.BLACK, 2);
            strokeLine(x1, y1, x1 - 12, y1 + 24, Color.BLACK, 2);
            strokeLine(x2, y2, x2 + 12, y2 - 24, Color.BLACK, 2);
            strokeLine(x2, y2, x2 - 12, y2 - 24, Color.BLACK, 2);
            gc.rotate(Math.toRadians(270), x1 + 28, y1 + (y2 - y1) / 2 + h);
            gc.drawString(txt, x1 + 28, y1 + (y2 - y1) / 2 + h);
        } else {
            strokeLine(x1, y1 - 24, x1, y1 + 24, Color.BLACK, 2);
            strokeLine(x2, y2 - 24, x2, y2 + 24, Color.BLACK, 2);
            strokeLine(x1, y1, x1 + 24, y1 - 12, Color.BLACK, 2);
            strokeLine(x1, y1, x1 + 24, y1 + 12, Color.BLACK, 2);
            strokeLine(x2, y2, x2 - 24, y2 - 12, Color.BLACK, 2);
            strokeLine(x2, y2, x2 - 24, y2 + 12, Color.BLACK, 2);
            gc.rotate(Math.toRadians(0), x1 + (x2 - x1) / 2, y2 + 28 + h);
            gc.drawString(txt, x1 + (x2 - x1) / 2, y2 + 28 + h);
        }
    }

    public void print() {
        System.out.println(TypeElem.AREA + " owner.id=" + owner.id + ", id=" + id + ", x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2=" + y2);
    }
}
