package wincalc;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dataset.Record;
import domain.eParams;
import enums.LayoutArea;
import enums.ParamJson;
import enums.TypeElem;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public abstract class AreaBase extends Base {

    protected AreaBase owner = null; //владелец
    private LinkedList<Base> listChild = new LinkedList(); //список компонентов в окне

    protected HashMap<ParamJson, Object> mapParam = new HashMap(); //параметры элемента        
    private LayoutArea layout = LayoutArea.FULL; //порядок расположения компонентов в окне
    protected EnumMap<LayoutArea, ElemFrame> mapFrame = new EnumMap<>(LayoutArea.class); //список рам в окне    

    /**
     * Конструктор
     */
    public AreaBase(String id) {
        this.id = id;
    }

    /**
     * Конструктор парсинга скрипта
     */
    public AreaBase(Wincalc iwin, AreaBase owner, String id, LayoutArea layout, float width, float height) {
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
    public AreaBase(AreaBase owner, String id, LayoutArea layout, float width, float height, int color1, int color2, int color3) {
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
            if (LayoutArea.VERTICAL.equals(owner.layout())) { //сверху вниз
                dimension(owner.x1, owner.y1, owner.x2, owner.y1 + height);

            } else if (LayoutArea.HORIZONTAL.equals(owner.layout())) { //слева направо
                dimension(owner.x1, owner.y1, owner.x1 + width, owner.y2);
            }
            //Проверим есть ещё ареа перед текущей, т.к. this area ущё не создана начнём с конца
            for (int index = owner.listChild().size() - 1; index >= 0; --index) {
                if (owner.listChild().get(index) instanceof AreaBase) {
                    ElemBase prevArea = (ElemBase) owner.listChild().get(index);

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
     * Инициализация pro4Params
     */
    protected void parsingParam(AreaBase root, String paramJson) {
        try {
            Gson gson = new Gson(); //библиотека json
            if (paramJson != null && paramJson.isEmpty() == false) {
                String str = paramJson.replace("'", "\"");

            JsonElement jsonElem = gson.fromJson(str, JsonElement.class);
            JsonObject jsonObj = jsonElem.getAsJsonObject();
            JsonArray jsonArr = jsonObj.getAsJsonArray(ParamJson.pro4Params.name()); 
            
            if (!jsonArr.isJsonNull() && jsonArr.isJsonArray()) {
                mapParam.put(ParamJson.pro4Params, jsonObj.get(ParamJson.pro4Params.name())); //первый вариант    
                    HashMap<Integer, Object[]> mapValue = new HashMap();
                    for (int index = 0; index < jsonArr.size(); index++) {
                      JsonArray jsonRec = (JsonArray) jsonArr.get(index);
                      int pnumb = jsonRec.getAsInt();
                          String p1 = jsonRec.get(0).getAsString();
                          String p2 = jsonRec.get(1).getAsString();
                          Record rec = eParams.query.select(eParams.up, "where", eParams.numb, "=", p1, "and", eParams.mixt, "=", p2).get(0);
                        if (pnumb < 0 && rec != null)
                            mapValue.put(pnumb, new Object[]{rec.get(eParams.name), rec.get(eParams.mixt), 0});
                    }
                    mapParam.put(ParamJson.pro4Params2, mapValue); //второй вариант                
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
            elemJoinVal.elemJoinRight = adjoinElem(LayoutArea.TOP);
        }
        if (elemJoinVal.elemJoinBottom == null) {
            elemJoinVal.elemJoinBottom = adjoinElem(LayoutArea.LEFT);
        }

        elemJoinVal = hmJoin.get(key2);
        if (elemJoinVal == null) {
            hmJoin.put(key2, new ElemJoinig(iwin));
            elemJoinVal = hmJoin.get(key2);
        }
        if (elemJoinVal.elemJoinLeft == null) {
            elemJoinVal.elemJoinLeft = adjoinElem(LayoutArea.TOP);
        }
        if (elemJoinVal.elemJoinBottom == null) {
            elemJoinVal.elemJoinBottom = adjoinElem(LayoutArea.RIGHT);
        }
        //}
        elemJoinVal = hmJoin.get(key3);
        if (elemJoinVal == null) {
            hmJoin.put(key3, new ElemJoinig(iwin));
            elemJoinVal = hmJoin.get(key3);
        }
        if (elemJoinVal.elemJoinTop == null) {
            elemJoinVal.elemJoinTop = adjoinElem(LayoutArea.RIGHT);
        }
        if (elemJoinVal.elemJoinLeft == null) {
            elemJoinVal.elemJoinLeft = adjoinElem(LayoutArea.BOTTOM);
        }

        elemJoinVal = hmJoin.get(key4);
        if (elemJoinVal == null) {
            hmJoin.put(key4, new ElemJoinig(iwin));
            elemJoinVal = hmJoin.get(key4);
        }
        if (elemJoinVal.elemJoinTop == null) {
            elemJoinVal.elemJoinTop = adjoinElem(LayoutArea.LEFT);
        }
        if (elemJoinVal.elemJoinRight == null) {
            elemJoinVal.elemJoinRight = adjoinElem(LayoutArea.BOTTOM);
        }
    }

    /**
     * Получить примыкающий элемент (используется при нахождении элементов
     * соединений)
     */
    protected ElemBase adjoinElem(LayoutArea layoutSide) {

        LinkedList<Base> listElem = areaOrImpostList();
        for (int index = 0; index < listElem.size(); ++index) {

            Base elemBase = listElem.get(index);
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

                    return (index == 0) ? mapFrame.get(layoutSide) : (ElemBase) listElem.get(index - 1);
                } else if (layoutSide == LayoutArea.BOTTOM) {
                    return (index == listElem.size() - 1) ? mapFrame.get(layoutSide) : (ElemBase) listElem.get(index + 1);
                } else {
                    return root().mapFrame.get(layoutSide);
                }
            } else if (owner.equals(root()) && owner.layout() == LayoutArea.HORIZONTAL) {
                if (layoutSide == LayoutArea.LEFT) {
                    return (index == 0) ? mapFrame.get(layoutSide) : (ElemBase) listElem.get(index - 1);
                } else if (layoutSide == LayoutArea.RIGHT) {
                    return (index == listElem.size() - 1) ? mapFrame.get(layoutSide) : (ElemBase) listElem.get(index + 1);
                } else {
                    return root().mapFrame.get(layoutSide);
                }

            } else {
                if (owner.layout() == LayoutArea.VERTICAL) {
                    if (layoutSide == LayoutArea.TOP) {
                        return (index == 0) ? owner.adjoinElem(layoutSide) : (ElemBase) listElem.get(index - 1);
                    } else if (layoutSide == LayoutArea.BOTTOM) {
                        return (index == listElem.size() - 1) ? owner.adjoinElem(layoutSide) : (ElemBase) listElem.get(index + 1);
                    } else {
                        return owner.adjoinElem(layoutSide);
                    }
                } else {
                    if (layoutSide == LayoutArea.LEFT) {
                        return (index == 0) ? owner.adjoinElem(layoutSide) : (ElemBase) listElem.get(index - 1);
                    } else if (layoutSide == LayoutArea.RIGHT) {
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
    public <E> LinkedList<E> elemList(TypeElem... type) {
        if (type == null) {
            type = new TypeElem[]{TypeElem.FRAME, TypeElem.IMPOST, TypeElem.GLASS, TypeElem.STVORKA};
        }
        LinkedList<Base> arrElem = new LinkedList();
        LinkedList<E> outElem = new LinkedList();
        for (Map.Entry<LayoutArea, ElemFrame> elemRama : root().mapFrame.entrySet()) {
            arrElem.add(elemRama.getValue());
        }
        for (Base elemBase : root().listChild()) { //первый уровень
            arrElem.add(elemBase);
            if (elemBase instanceof AreaBase) {
                for (Map.Entry<LayoutArea, ElemFrame> elemRama : ((AreaBase) elemBase).mapFrame.entrySet()) {
                    arrElem.add(elemRama.getValue());
                }
                for (Base elemBase2 : elemBase.listChild()) { //второй уровень
                    arrElem.add(elemBase2);
                    if (elemBase2 instanceof AreaBase) {
                        for (Map.Entry<LayoutArea, ElemFrame> elemRama : ((AreaBase) elemBase2).mapFrame.entrySet()) {
                            arrElem.add(elemRama.getValue());
                        }
                        for (Base elemBase3 : elemBase2.listChild()) { //третий уровень
                            arrElem.add(elemBase3);
                            if (elemBase3 instanceof AreaBase) {
                                for (Map.Entry<LayoutArea, ElemFrame> elemRama : ((AreaBase) elemBase3).mapFrame.entrySet()) {
                                    arrElem.add(elemRama.getValue());
                                }
                                for (Base elemBase4 : elemBase3.listChild()) { //четвёртый уровень
                                    arrElem.add(elemBase4);
                                    if (elemBase4 instanceof AreaBase) {
                                        for (Map.Entry<LayoutArea, ElemFrame> elemRama : ((AreaBase) elemBase4).mapFrame.entrySet()) {
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
            TypeElem type2 = type[index];
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

    public LayoutArea layout() {
        return layout;
    }

    /**
     * Список area и impost
     */
    public LinkedList<Base> areaOrImpostList() {

        LinkedList<Base> elemList = new LinkedList();
        for (Base elemBase : owner.listChild()) {
            if (TypeElem.AREA == elemBase.typeElem() || TypeElem.IMPOST == elemBase.typeElem()) {
                elemList.add(elemBase);
            }
        }
        return elemList;
    }
}
