package wincalc;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import enums.eLayoutArea;
import enums.eParamJson;
import enums.eTypeElem;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import jdk.nashorn.internal.parser.JSONParser;

public abstract class AreaBase extends Base {

    protected AreaBase owner = null; //владелец
    private LinkedList<Base> listChild = new LinkedList(); //список компонентов в окне

    protected HashMap<eParamJson, Object> mapParam = new HashMap(); //параметры элемента        
    private eLayoutArea layout = eLayoutArea.FULL; //порядок расположения компонентов в окне
    protected EnumMap<eLayoutArea, ElemFrame> mapFrame = new EnumMap<>(eLayoutArea.class); //список рам в окне    

    /**
     * Конструктор
     */
    public AreaBase(String id) {
        this.id = id;
    }

    /**
     * Конструктор парсинга скрипта
     */
    public AreaBase(Wincalc iwin, AreaBase owner, String id, eLayoutArea layout, float width, float height) {
        this(owner, id, layout, width, height, 1, 1, 1);
        this.iwin = iwin;
        //Коррекция размера стеклопакета(створки) арки.
        //Уменьшение на величину добавленной подкладки над импостом.
        //if (owner != null && eTypeElem.ARCH == owner.getTypeArea() && owner.getChildList().size() == 2 && eTypeElem.IMPOST == owner.getChildList().get(1).getTypeElem()) {
        //float dh = owner.getChildList().get(1).getArticlesRec().aheig / 2;
        //setDimension(x1, y1, x2, y2 - dh);
        //}
    }

    /**
     * Конструктор
     */
    public AreaBase(AreaBase owner, String id, eLayoutArea layout, float width, float height, int color1, int color2, int color3) {
        this.owner = owner;
        this.id = id;
        this.layout = layout;
        this.width = width;
        this.height = height;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        initDimension(owner);
    }

    private void initDimension(AreaBase owner) {
        if (owner != null) {
            //Заполним по умолчанию
            if (eLayoutArea.VERTICAL.equals(owner.layout())) { //сверху вниз
                dimension(owner.x1, owner.y1, owner.x2, owner.y1 + height);

            } else if (eLayoutArea.HORIZONTAL.equals(owner.layout())) { //слева направо
                dimension(owner.x1, owner.y1, owner.x1 + width, owner.y2);
            }
            //Проверим есть ещё ареа перед текущей, т.к. this area ущё не создана начнём с конца
            for (int index = owner.listChild().size() - 1; index >= 0; --index) {
                if (owner.listChild().get(index) instanceof AreaBase) {
                    ElemBase prevArea = (ElemBase) owner.listChild().get(index);

                    if (eLayoutArea.VERTICAL.equals(owner.layout())) { //сверху вниз
                        dimension(prevArea.x1, prevArea.y2, owner.x2, prevArea.y2 + height);

                    } else if (eLayoutArea.HORIZONTAL.equals(owner.layout())) { //слева направо
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
     * Инициализация pro4Params
     */
    protected void parsingParam(AreaBase root, String paramJson) {
        try {
            Gson gson = new Gson(); //библиотека json
            if (paramJson != null && paramJson.isEmpty() == false) {
                String str = paramJson.replace("'", "\"");

            JsonElement jsonElem = gson.fromJson(str, JsonElement.class);
            JsonObject jsonObj = jsonElem.getAsJsonObject();
            JsonArray jsonArr = jsonObj.getAsJsonArray(eParamJson.pro4Params.name()); 
            
            if (!jsonArr.isJsonNull() && jsonArr.isJsonArray()) {
                mapParam.put(eParamJson.pro4Params, jsonObj.get(eParamJson.pro4Params.name())); 
                    HashMap<Integer, Object[]> hmValue = new HashMap();
                    for (int index = 0; index < jsonArr.size(); index++) {
                      JsonArray jsonRec = (JsonArray) jsonArr.get(index);
                      int pnumb = jsonRec.getAsInt();
//                        Parlist rec = Parlist.get(root.getConst(), jsonRec.get(0), jsonRec.get(1));
//                        if (pnumb < 0 && rec != null)
//                            hmValue.put(pnumb, new Object[]{rec.pname, rec.znumb, 0});
                    }
                    mapParam.put(eParamJson.pro4Params2, hmValue); //второй вариант                
            }
            }
        } catch (Exception e) {
            System.err.println("Ошибка ElemBase.parsingParamJson() " + e);
        }
    }

    /**
     * Обход(схлопывание) соединений area
     */
    public void joinArea(HashMap<String, ElemJoinig> hmJoin) {

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
            elemJoinVal.elemJoinRight = adjoinElem(eLayoutArea.TOP);
        }
        if (elemJoinVal.elemJoinBottom == null) {
            elemJoinVal.elemJoinBottom = adjoinElem(eLayoutArea.LEFT);
        }

        elemJoinVal = hmJoin.get(key2);
        if (elemJoinVal == null) {
            hmJoin.put(key2, new ElemJoinig(iwin));
            elemJoinVal = hmJoin.get(key2);
        }
        if (elemJoinVal.elemJoinLeft == null) {
            elemJoinVal.elemJoinLeft = adjoinElem(eLayoutArea.TOP);
        }
        if (elemJoinVal.elemJoinBottom == null) {
            elemJoinVal.elemJoinBottom = adjoinElem(eLayoutArea.RIGHT);
        }
        //}
        elemJoinVal = hmJoin.get(key3);
        if (elemJoinVal == null) {
            hmJoin.put(key3, new ElemJoinig(iwin));
            elemJoinVal = hmJoin.get(key3);
        }
        if (elemJoinVal.elemJoinTop == null) {
            elemJoinVal.elemJoinTop = adjoinElem(eLayoutArea.RIGHT);
        }
        if (elemJoinVal.elemJoinLeft == null) {
            elemJoinVal.elemJoinLeft = adjoinElem(eLayoutArea.BOTTOM);
        }

        elemJoinVal = hmJoin.get(key4);
        if (elemJoinVal == null) {
            hmJoin.put(key4, new ElemJoinig(iwin));
            elemJoinVal = hmJoin.get(key4);
        }
        if (elemJoinVal.elemJoinTop == null) {
            elemJoinVal.elemJoinTop = adjoinElem(eLayoutArea.LEFT);
        }
        if (elemJoinVal.elemJoinRight == null) {
            elemJoinVal.elemJoinRight = adjoinElem(eLayoutArea.BOTTOM);
        }
    }

    /**
     * Получить примыкающий элемент (используется при нахождении элементов
     * соединений)
     */
    protected ElemBase adjoinElem(eLayoutArea layoutSide) {

        LinkedList<Base> listElem = areaOrImpostList();
        for (int index = 0; index < listElem.size(); ++index) {

            Base elemBase = listElem.get(index);
            if (elemBase.id != id) {
                continue; //пропускаем если другая ареа
            }
            EnumMap<eLayoutArea, ElemFrame> mapFrame = root().mapFrame;
            if (index == 0 && owner.equals(root()) && layoutSide == eLayoutArea.TOP && owner.layout() == eLayoutArea.VERTICAL && root().typeElem() == eTypeElem.ARCH) {
                return mapFrame.get(eTypeElem.ARCH);
            } else if (owner.equals(root()) && layoutSide == eLayoutArea.TOP && owner.layout() == eLayoutArea.HORIZONTAL && root().typeElem() == eTypeElem.ARCH) {
                return mapFrame.get(eTypeElem.ARCH);
            }

            if (owner.equals(root()) && owner.layout() == eLayoutArea.VERTICAL) {
                if (layoutSide == eLayoutArea.TOP) {

                    return (index == 0) ? mapFrame.get(layoutSide) : (ElemBase) listElem.get(index - 1);
                } else if (layoutSide == eLayoutArea.BOTTOM) {
                    return (index == listElem.size() - 1) ? mapFrame.get(layoutSide) : (ElemBase) listElem.get(index + 1);
                } else {
                    return root().mapFrame.get(layoutSide);
                }
            } else if (owner.equals(root()) && owner.layout() == eLayoutArea.HORIZONTAL) {
                if (layoutSide == eLayoutArea.LEFT) {
                    return (index == 0) ? mapFrame.get(layoutSide) : (ElemBase) listElem.get(index - 1);
                } else if (layoutSide == eLayoutArea.RIGHT) {
                    return (index == listElem.size() - 1) ? mapFrame.get(layoutSide) : (ElemBase) listElem.get(index + 1);
                } else {
                    return root().mapFrame.get(layoutSide);
                }

            } else {
                if (owner.layout() == eLayoutArea.VERTICAL) {
                    if (layoutSide == eLayoutArea.TOP) {
                        return (index == 0) ? owner.adjoinElem(layoutSide) : (ElemBase) listElem.get(index - 1);
                    } else if (layoutSide == eLayoutArea.BOTTOM) {
                        return (index == listElem.size() - 1) ? owner.adjoinElem(layoutSide) : (ElemBase) listElem.get(index + 1);
                    } else {
                        return owner.adjoinElem(layoutSide);
                    }
                } else {
                    if (layoutSide == eLayoutArea.LEFT) {
                        return (index == 0) ? owner.adjoinElem(layoutSide) : (ElemBase) listElem.get(index - 1);
                    } else if (layoutSide == eLayoutArea.RIGHT) {
                        return (index == listElem.size() - 1) ? owner.adjoinElem(layoutSide) : (ElemBase) listElem.get(index + 1);
                    } else {
                        return owner.adjoinElem(layoutSide);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Список элементов окна
     */
    public <E> LinkedList<E> elemList(eTypeElem... type) {
        if (type == null) {
            type = new eTypeElem[]{eTypeElem.FRAME, eTypeElem.IMPOST, eTypeElem.GLASS, eTypeElem.STVORKA};
        }
        LinkedList<Base> arrElem = new LinkedList();
        LinkedList<E> outElem = new LinkedList();
        for (Map.Entry<eLayoutArea, ElemFrame> elemRama : root().mapFrame.entrySet()) {
            arrElem.add(elemRama.getValue());
        }
        for (Base elemBase : root().listChild()) { //первый уровень
            arrElem.add(elemBase);
            if (elemBase instanceof AreaBase) {
                for (Map.Entry<eLayoutArea, ElemFrame> elemRama : ((AreaBase) elemBase).mapFrame.entrySet()) {
                    arrElem.add(elemRama.getValue());
                }
                for (Base elemBase2 : elemBase.listChild()) { //второй уровень
                    arrElem.add(elemBase2);
                    if (elemBase2 instanceof AreaBase) {
                        for (Map.Entry<eLayoutArea, ElemFrame> elemRama : ((AreaBase) elemBase2).mapFrame.entrySet()) {
                            arrElem.add(elemRama.getValue());
                        }
                        for (Base elemBase3 : elemBase2.listChild()) { //третий уровень
                            arrElem.add(elemBase3);
                            if (elemBase3 instanceof AreaBase) {
                                for (Map.Entry<eLayoutArea, ElemFrame> elemRama : ((AreaBase) elemBase3).mapFrame.entrySet()) {
                                    arrElem.add(elemRama.getValue());
                                }
                                for (Base elemBase4 : elemBase3.listChild()) { //четвёртый уровень
                                    arrElem.add(elemBase4);
                                    if (elemBase4 instanceof AreaBase) {
                                        for (Map.Entry<eLayoutArea, ElemFrame> elemRama : ((AreaBase) elemBase4).mapFrame.entrySet()) {
                                            arrElem.add(elemRama.getValue());
                                        }
                                        for (Base elemBase5 : elemBase4.listChild()) { //пятый уровень
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
            eTypeElem type2 = type[index];
            for (Base elemBase : arrElem) {
                if (elemBase.typeElem() == type2) {
                    E elem = (E) elemBase;
                    outElem.add(elem);
                }
            }
        }
        return outElem;
    }

    public abstract void joinRama();

    public HashMap<String, ElemJoinig> mapJoin() {
        return iwin.mapJoin;
    }

    @Override
    public LinkedList<Base> listChild() {
        return listChild;
    }

    public void addElem(Base element) {
        listChild.add(element);
    }

    public ElemFrame addFrame(ElemFrame elemFrame) {
        mapFrame.put(elemFrame.layout(), elemFrame);
        return elemFrame;
    }

    public eLayoutArea layout() {
        return layout;
    }

    /**
     * Список area и impost
     */
    public LinkedList<Base> areaOrImpostList() {

        LinkedList<Base> elemList = new LinkedList();
        for (Base elemBase : owner.listChild()) {
            if (eTypeElem.AREA == elemBase.typeElem() || eTypeElem.IMPOST == elemBase.typeElem()) {
                elemList.add(elemBase);
            }
        }
        return elemList;
    }
}
