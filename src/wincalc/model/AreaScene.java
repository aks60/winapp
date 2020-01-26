package wincalc.model;

import enums.LayoutArea;
import enums.TypeElem;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import wincalc.Wincalc;

public class AreaScene extends AreaContainer {

    public AreaScene(Wincalc iwin, AreaContainer owner, String id, LayoutArea layout, float width, float height) {
        super(iwin, owner, id, layout, width, height);
    }

    //Обход(схлопывание) соединений area
    public void passJoinArea(HashMap<String, ElemJoinig> mapJoin) {

        ElemJoinig elemJoinVal = null;
        String key1 = String.valueOf(x1) + ":" + String.valueOf(y1);
        String key2 = String.valueOf(x2) + ":" + String.valueOf(y1);
        String key3 = String.valueOf(x2) + ":" + String.valueOf(y2);
        String key4 = String.valueOf(x1) + ":" + String.valueOf(y2);

        elemJoinVal = mapJoin.get(key1);
        if (elemJoinVal == null) {
            mapJoin.put(key1, new ElemJoinig(iwin));
            elemJoinVal = mapJoin.get(key1);
        }
        if (elemJoinVal.elemJoinRight == null) {
            elemJoinVal.elemJoinRight = adjoinedElem(LayoutArea.TOP);
        }
        if (elemJoinVal.elemJoinBottom == null) {
            elemJoinVal.elemJoinBottom = adjoinedElem(LayoutArea.LEFT);
        }

        elemJoinVal = mapJoin.get(key2);
        if (elemJoinVal == null) {
            mapJoin.put(key2, new ElemJoinig(iwin));
            elemJoinVal = mapJoin.get(key2);
        }
        if (elemJoinVal.elemJoinLeft == null) {
            elemJoinVal.elemJoinLeft = adjoinedElem(LayoutArea.TOP);
        }
        if (elemJoinVal.elemJoinBottom == null) {
            elemJoinVal.elemJoinBottom = adjoinedElem(LayoutArea.RIGHT);
        }
        
        elemJoinVal = mapJoin.get(key3);
        if (elemJoinVal == null) {
            mapJoin.put(key3, new ElemJoinig(iwin));
            elemJoinVal = mapJoin.get(key3);
        }
        if (elemJoinVal.elemJoinTop == null) {
            elemJoinVal.elemJoinTop = adjoinedElem(LayoutArea.RIGHT);
        }
        if (elemJoinVal.elemJoinLeft == null) {
            elemJoinVal.elemJoinLeft = adjoinedElem(LayoutArea.BOTTOM);
        }

        elemJoinVal = mapJoin.get(key4);
        if (elemJoinVal == null) {
            mapJoin.put(key4, new ElemJoinig(iwin));
            elemJoinVal = mapJoin.get(key4);
        }
        if (elemJoinVal.elemJoinTop == null) {
            elemJoinVal.elemJoinTop = adjoinedElem(LayoutArea.LEFT);
        }
        if (elemJoinVal.elemJoinRight == null) {
            elemJoinVal.elemJoinRight = adjoinedElem(LayoutArea.BOTTOM);
        }
    }
    
    // Получить примыкающий элемент (используется при нахождении элементов соединений)
   /* protected ElemComp adjoinedElem(LayoutArea layoutSide) {

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

                    return (index == 0) ? mapFrame.get(layoutSide) : (ElemComp) listElem.get(index - 1);
                } else if (layoutSide == LayoutArea.BOTTOM) {
                    return (index == listElem.size() - 1) ? mapFrame.get(layoutSide) : (ElemComp) listElem.get(index + 1);
                } else {
                    return root().mapFrame.get(layoutSide);
                }
            } else if (owner.equals(root()) && owner.layout() == LayoutArea.HORIZ) {
                if (layoutSide == LayoutArea.LEFT) {
                    return (index == 0) ? mapFrame.get(layoutSide) : (ElemComp) listElem.get(index - 1);
                } else if (layoutSide == LayoutArea.RIGHT) {
                    return (index == listElem.size() - 1) ? mapFrame.get(layoutSide) : (ElemComp) listElem.get(index + 1);
                } else {
                    return root().mapFrame.get(layoutSide);
                }

            } else {
                if (owner.layout() == LayoutArea.VERT) {
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
    }*/

    @Override
    public TypeElem typeElem() {
        return TypeElem.AREA;
    }

    @Override
    public void joinFrame() {
        System.out.println("Функция не определена");
    }        
}
