package wincalc.model;

import domain.eArtikl;
import enums.LayoutArea;
import enums.TypeElem;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
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
    public <E> LinkedList<E> elemList(TypeElem... type) {
        
        LinkedList<Com5t> arrElem = new LinkedList();
        LinkedList<E> outElem = new LinkedList();
        for (Map.Entry<LayoutArea, ElemFrame> elemRama : root().mapFrame.entrySet()) {
            arrElem.add(elemRama.getValue());
        }
        Object obj = root().listChild();
        
        for (Com5t elemBase : root().listChild()) { //первый уровень
            arrElem.add(elemBase);
            if (elemBase instanceof AreaContainer) {
                for (Map.Entry<LayoutArea, ElemFrame> elemRama : ((AreaContainer) elemBase).mapFrame.entrySet()) {
                    arrElem.add(elemRama.getValue());
                }
                for (Com5t elemBase2 : elemBase.listChild()) { //второй уровень
                    arrElem.add(elemBase2);
                    if (elemBase2 instanceof AreaContainer) {
                        for (Map.Entry<LayoutArea, ElemFrame> elemRama : ((AreaContainer) elemBase2).mapFrame.entrySet()) {
                            arrElem.add(elemRama.getValue());
                        }
                        for (Com5t elemBase3 : elemBase2.listChild()) { //третий уровень
                            arrElem.add(elemBase3);
                            if (elemBase3 instanceof AreaContainer) {
                                for (Map.Entry<LayoutArea, ElemFrame> elemRama : ((AreaContainer) elemBase3).mapFrame.entrySet()) {
                                    arrElem.add(elemRama.getValue());
                                }
                                for (Com5t elemBase4 : elemBase3.listChild()) { //четвёртый уровень
                                    arrElem.add(elemBase4);
                                    if (elemBase4 instanceof AreaContainer) {
                                        for (Map.Entry<LayoutArea, ElemFrame> elemRama : ((AreaContainer) elemBase4).mapFrame.entrySet()) {
                                            arrElem.add(elemRama.getValue());
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

    public abstract void joinRama();

    @Override
    public LinkedList<Com5t> listChild() {
        return listChild;
    }

    public void passJoinRama() {
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
}
