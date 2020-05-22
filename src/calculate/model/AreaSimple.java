package calculate.model;

import enums.LayoutArea;
import enums.TypeElem;
import enums.LayoutJoin;
import enums.TypeJoin;
import java.awt.BasicStroke;
import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import amain.Main;
import calculate.Wincalc;

public class AreaSimple extends Com5t {

    public EnumMap<LayoutArea, ElemFrame> mapFrame = new EnumMap<>(LayoutArea.class); //список рам в окне  

    public AreaSimple(Wincalc iwin, AreaSimple owner, float id, TypeElem typeElem, LayoutArea layout, float width, float height, int color1, int color2, int color3) {
        super(id, iwin, owner);
        this.type = typeElem;
        this.layout = layout;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        initDimension(width, height);
    }

    protected void initDimension(float width, float height) {

        if (owner() == null) { //для root area
            setDimension(0, 0, width, height);

        } else {
            //Первая добавляемая area
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

                        if (LayoutArea.VERT.equals(owner().layout())) { //сверху вниз
                            setDimension(owner().x1, prevArea.y2, owner().x2, prevArea.y2 + height);

                        } else if (LayoutArea.HORIZ.equals(owner().layout())) { //слева направо
                            setDimension(prevArea.x2, owner().y1, prevArea.x2 + width, owner().y2);
                        }
                        break; //как только нашел сразу выход
                    }
                }
            }
            //Поправка на подкладу
            LinkedList<AreaSimple> listArea = root().listElem(TypeElem.AREA);
            if (LayoutArea.VERT.equals(owner().layout())) { //сверху вниз
                AreaSimple prevArea = listArea.stream().filter(el -> el.inside(x1 + (x2 - x1) / 2, y1) == true).findFirst().orElse(null);
                float dy = (prevArea != null && prevArea.height() < 120) ? prevArea.height() : 0; //поправка на величину добавленной подкладки над импостом
                y2 = y2 - dy;
            } else if (LayoutArea.HORIZ.equals(owner().layout())) { //слева направо
                AreaSimple prevArea = listArea.stream().filter(el -> el.inside(x1, y1 + (y2 - y1) / 2) == true).findFirst().orElse(null);
                float dx = (prevArea != null && prevArea.width() < 120) ? prevArea.width() : 0; //поправка на величину добавленной подкладки над импостом
                x2 = x2 - dx;
            }
        }
    }

    //Список элементов окна
    public <E> LinkedList<E> listElem(TypeElem... type) {
        LinkedList<E> list = new LinkedList();
        listCom5t(this, list, Arrays.asList(type));
        return list;
    }

    public <E> void listCom5t(Com5t com5t, LinkedList<E> list, List<TypeElem> type) {

        if (type.contains(com5t.type)) {
            list.add((E) com5t);
        }
        if (com5t instanceof AreaSimple) {
            Collection<ElemFrame> set = ((AreaSimple) com5t).mapFrame.values();
            for (ElemFrame frm : set) {
                if (type.contains(frm.type)) {
                    list.add((E) frm);
                }
            }
        }
        for (Com5t com5t2 : com5t.listChild) {
            listCom5t(com5t2, list, type);
        }
    }

    public void joinFrame() {
    }

    public void joinElem() {

        List<ElemSimple> impList = listElem(TypeElem.IMPOST);
        List<ElemSimple> elemList = listElem(TypeElem.FRAME_SIDE, TypeElem.IMPOST);
        
        for (ElemSimple impElem : impList) {           
            for (ElemSimple elem5e : elemList) {                
                if (impElem.owner().layout() == LayoutArea.HORIZ) {
                    
                    if (elem5e.inside(impElem.x1, impElem.y1) == true) { //T - соединение верхнее                       
                        ElemJoining el = new ElemJoining(iwin());
                        el.id = id() + 1f / 100;
                        el.init(TypeJoin.VAR40, LayoutJoin.TTOP, impElem, elem5e);
                        iwin().mapJoin.put(impElem.x1 + ":" + impElem.y1, el);
                        
                    } else if (impElem.inside(impElem.x2, impElem.y2) == true) { //T - соединение нижнее                        
                        ElemJoining el = new ElemJoining(iwin()); 
                        el.id = id() + 2f / 100;
                        el.init(TypeJoin.VAR40, LayoutJoin.TBOT, impElem, elem5e);
                        iwin().mapJoin.put(impElem.x2 + ":" + impElem.y2, el);
                    }
                } else {
                    if (elem5e.inside(impElem.x1, impElem.y1) == true) { //T - соединение левое                        
                        ElemJoining el = new ElemJoining(iwin()); 
                        el.id = id() + 3f / 100;
                        el.init(TypeJoin.VAR40, LayoutJoin.TLEFT, impElem, elem5e);
                        iwin().mapJoin.put(impElem.x1 + ":" + impElem.y1, el);
                        
                    } else if (impElem.inside(impElem.x2, impElem.y2) == true) { //T - соединение правое                        
                        ElemJoining el = new ElemJoining(iwin()); 
                        el.id = id() + 4f / 100;
                        el.init(TypeJoin.VAR40, LayoutJoin.TRIGH, impElem, elem5e);
                        iwin().mapJoin.put(impElem.x2 + ":" + impElem.y2, el);
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
        for (ElemSimple elem5e : elems) {
            if (elem5e.inside(x, y) == true) {
                map.get(k).add(elem5e);
            }
        }
    }

    //Рисуем конструкцию
    public void draw(int width, int height) {
        try {
            iwin().gc2d.fillRect(0, 0, width, height);

            //Прорисовка стеклопакетов
            LinkedList<ElemGlass> elemGlassList = root().listElem(TypeElem.GLASS);
            elemGlassList.stream().forEach(el -> el.paint());

            //Прорисовка импостов
            LinkedList<ElemImpost> elemImpostList = root().listElem(TypeElem.IMPOST);
            elemImpostList.stream().forEach(el -> el.paint());

            //Прорисовка рам
            if (TypeElem.ARCH == type) {
                mapFrame.get(LayoutArea.ARCH).paint();
            } else {
                mapFrame.get(LayoutArea.TOP).paint();
            }
            mapFrame.get(LayoutArea.BOTTOM).paint();
            mapFrame.get(LayoutArea.LEFT).paint();
            mapFrame.get(LayoutArea.RIGHT).paint();

            //Прорисовка створок
            LinkedList<AreaStvorka> elemStvorkaList = root().listElem(TypeElem.STVORKA);
            elemStvorkaList.stream().forEach(el -> el.paint());

            //Прорисовка размера            
            LinkedList<Float> ls1 = new LinkedList(Arrays.asList(x1, x2)), ls2 = new LinkedList(Arrays.asList(y1, y2));
            LinkedList<ElemImpost> impostList = root().listElem(TypeElem.IMPOST);
            for (ElemSimple el : impostList) { //по импостам определим точки разрыва линии
                if (LayoutArea.VERT == el.owner().layout) {
                    ls2.add(el.y1);
                } else {
                    ls1.add(el.x1 + (el.x2 - el.x1) / 2);
                }
            }
            Collections.sort(ls1);
            Collections.sort(ls2);
            float dy = iwin().heightAdd - iwin().height;
            int mov = 80;
            for (int i = 1; i < ls1.size(); i++) {
                float x1 = ls1.get(i - 1), x2 = ls1.get(i);
                line(x1, (iwin().heightAdd + mov), x2, (iwin().heightAdd + mov), 0);
            }
            for (int i = 1; i < ls2.size(); i++) {
                float y1 = ls2.get(i - 1), y2 = ls2.get(i);
                line((this.x2 + mov), y1, (this.x2 + mov), y2, dy);
            }
            if (ls1.size() > 2) { //линия общей ширины
                line(root().x1, iwin().heightAdd + mov * 2, root().x2, iwin().heightAdd + mov * 2, 0);
            }
            if (ls2.size() > 2) { //линия общей высоты
                line(iwin().width + mov * 2, 0, iwin().width + mov * 2, iwin().heightAdd, 0);
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
            System.err.println("Ошибка AreaSimple.drawWin() " + s);
        }
    }

    private void line(float x1, float y1, float x2, float y2, float dy) {

        if (iwin().scale2 == 1) {
            iwin().gc2d.setColor(java.awt.Color.BLACK);
            iwin().gc2d.setFont(new java.awt.Font("Serif", java.awt.Font.BOLD, 60));
            setStroke(6); //толщина линии
            y1 = y1 + dy;
            y2 = y2 + dy;
            drawLine(x1, y1, x2, y2);
            if (x1 == x2 && y2 - y1 != 0) {
                drawLine(x1 - 24, y1, x1 + 24, y1);
                drawLine(x2 - 24, y2, x2 + 24, y2);
                drawLine(x1, y1, x1 + 12, y1 + 24);
                drawLine(x1, y1, x1 - 12, y1 + 24);
                drawLine(x2, y2, x2 + 12, y2 - 24);
                drawLine(x2, y2, x2 - 12, y2 - 24);
                rotate(Math.toRadians(270), x1 + 60, y1 + (y2 - y1) / 2);
                drawString(String.valueOf((int) (y2 - y1)), x1 + 60, y1 + (y2 - y1) / 2);
                rotate(Math.toRadians(-270), x1 + 60, y1 + (y2 - y1) / 2);
            } else if (y1 == y2 && x2 - x1 != 0) {
                drawLine(x1, y1 - 24, x1, y1 + 24);
                drawLine(x2, y2 - 24, x2, y2 + 24);
                drawLine(x1, y1, x1 + 24, y1 - 12);
                drawLine(x1, y1, x1 + 24, y1 + 12);
                drawLine(x2, y2, x2 - 24, y2 - 12);
                drawLine(x2, y2, x2 - 24, y2 + 12);
                drawString(String.valueOf((int) (x2 - x1)), x1 + (x2 - x1) / 2, y2 + 60);
            }
        }
    }
}
