package estimate.model;

import frames.swing.Draw;
import enums.LayoutArea;
import enums.TypeElem;
import enums.LayoutJoin;
import enums.TypeJoin;
import java.io.File;
import java.util.EnumMap;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import startup.Main;
import estimate.Wincalc;

public class AreaSimple extends Com5t {

    public EnumMap<LayoutArea, ElemFrame> mapFrame = new EnumMap<>(LayoutArea.class); //список рам в окне  

    public AreaSimple(Wincalc iwin, AreaSimple owner, float id, TypeElem typeElem, LayoutArea layout, float width, float height, int color1, int color2, int color3, String param) {
        super(id, iwin, owner);
        this.type = typeElem;
        this.layout = layout;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        parsing(param);
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
            //Поправка на подкладу над импостом
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
        listElem(this, list, Arrays.asList(type));
        return list;
    }

    private <E> void listElem(Com5t com5t, LinkedList<E> list, List<TypeElem> type) {

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
            listElem(com5t2, list, type);
        }
    }

    public void joinFrame() {
    }

    public void joinElem() {

        List<ElemSimple> impList = listElem(TypeElem.IMPOST);
        List<ElemSimple> elemList = listElem(TypeElem.FRAME_SIDE, TypeElem.IMPOST);

        //Цикл по импостам
        for (ElemSimple elemImp : impList) {
            //Цыкл по сторонам рамы и импостам (т.к. в створке Т-обр. соединений нет)
            for (ElemSimple elem5e : elemList) {

                ElemJoining el = new ElemJoining(iwin());
                el.anglProf = 90;
                elemImp.anglCut1 = 90;
                elemImp.anglCut2 = 90;
                
                //Элементы расположены по горизонтали
                if (elemImp.owner().layout() == LayoutArea.HORIZ) {
                    elemImp.anglHoriz = 90;
                    float x3 = elemImp.x1 + (elemImp.x2 - elemImp.x1) / 2;
                    
                    if (elem5e.inside(elemImp.x1, elemImp.y1) == true 
                            && iwin().mapJoin.get(x3 + ":" + elemImp.y1) == null) { //T - соединение верхнее                       
                        el.id = id() + 1f / 100;
                        el.init(TypeJoin.VAR40, LayoutJoin.TTOP, elemImp, elem5e);
                        iwin().mapJoin.put(x3 + ":" + elemImp.y1, el);

                    } else if (elem5e.inside(elemImp.x2, elemImp.y2) == true 
                            && iwin().mapJoin.get(x3 + ":" + elemImp.y2) == null) { //T - соединение нижнее                        
                        el.id = id() + 2f / 100;
                        el.init(TypeJoin.VAR40, LayoutJoin.TBOT, elemImp, elem5e);
                        iwin().mapJoin.put(x3 + ":" + elemImp.y2, el);
                    }
                    
                    //Элементы расположены по вертикали
                } else {
                    elemImp.anglHoriz = 0;
                    float y3 = elemImp.y1 + (elemImp.y2 - elemImp.y1) / 2;
                    
                    if (elem5e.inside(elemImp.x1, elemImp.y1) == true 
                            && iwin().mapJoin.get(elemImp.x1 + ":" + y3) == null) { //T - соединение левое                        
                        el.id = id() + 3f / 100;
                        el.init(TypeJoin.VAR40, LayoutJoin.TLEFT, elemImp, elem5e);
                        iwin().mapJoin.put(elemImp.x1 + ":" + y3, el);

                    } else if (elem5e.inside(elemImp.x2, elemImp.y2) == true 
                            && iwin().mapJoin.get(elemImp.x2 + ":" + y3) == null) { //T - соединение правое                        
                        el.id = id() + 4f / 100;
                        el.init(TypeJoin.VAR40, LayoutJoin.TRIGH, elemImp, elem5e);
                        iwin().mapJoin.put(elemImp.x2 + ":" + y3, el);
                    }
                }
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
            for (ElemSimple impostElem : impostList) { //по импостам определим точки разрыва линии
                if (LayoutArea.VERT == impostElem.owner().layout) {
                    ls2.add(impostElem.y1 + (impostElem.y2 - impostElem.y1) / 2);
                } else {
                    ls1.add(impostElem.x1 + (impostElem.x2 - impostElem.x1) / 2);
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
            Draw.setStroke(6); //толщина линии
            y1 = y1 + dy;
            y2 = y2 + dy;
            Draw.drawLine(x1, y1, x2, y2);
            if (x1 == x2 && y2 - y1 != 0) {
                Draw.drawLine(x1 - 24, y1, x1 + 24, y1);
                Draw.drawLine(x2 - 24, y2, x2 + 24, y2);
                Draw.drawLine(x1, y1, x1 + 12, y1 + 24);
                Draw.drawLine(x1, y1, x1 - 12, y1 + 24);
                Draw.drawLine(x2, y2, x2 + 12, y2 - 24);
                Draw.drawLine(x2, y2, x2 - 12, y2 - 24);
                Draw.rotate(Math.toRadians(270), x1 + 60, y1 + (y2 - y1) / 2);
                Draw.drawString(String.valueOf((int) (y2 - y1)), x1 + 60, y1 + (y2 - y1) / 2);
                Draw.rotate(Math.toRadians(-270), x1 + 60, y1 + (y2 - y1) / 2);
            } else if (y1 == y2 && x2 - x1 != 0) {
                Draw.drawLine(x1, y1 - 24, x1, y1 + 24);
                Draw.drawLine(x2, y2 - 24, x2, y2 + 24);
                Draw.drawLine(x1, y1, x1 + 24, y1 - 12);
                Draw.drawLine(x1, y1, x1 + 24, y1 + 12);
                Draw.drawLine(x2, y2, x2 - 24, y2 - 12);
                Draw.drawLine(x2, y2, x2 - 24, y2 + 12);
                Draw.drawString(String.valueOf((int) (x2 - x1)), x1 + (x2 - x1) / 2, y2 + 60);
            }
        }
    }
}
