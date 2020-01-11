package wincalc;

import enums.eLayoutArea;
import enums.eParamJson;
import enums.eTypeElem;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public abstract class AreaBase extends Base {

    protected Wincalc iwin = null; //главный класс
    protected AreaBase root = null; //главное окно
    protected AreaBase owner = null; //владелец
    protected HashMap<eParamJson, Object> hmParamJson = new HashMap(); //параметры элемента
    private LinkedList<Base> arrChild = new LinkedList(); //список компонентов в окне    
    private eLayoutArea layout = eLayoutArea.FULL; //порядок расположения компонентов в окне
    protected EnumMap<eLayoutArea, ElemFrame> hmElemFrame = new EnumMap<>(eLayoutArea.class); //список рам в окне    

    /**
     * Конструктор
     */
    public AreaBase(String id) {
        this.id = id;
        //setLayout(layout);     
    }

    /**
     * Конструктор парсинга скрипта
     */
    public AreaBase(Wincalc iwin, AreaBase root, AreaBase owner, String id, eLayoutArea layout, float width, float height) {
        this(owner, id, layout, width, height, 1, 1, 1);
        this.iwin = iwin;
        this.root = root;
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
                setDimension(owner.x1, owner.y1, owner.x2, owner.y1 + height);

            } else if (eLayoutArea.HORIZONTAL.equals(owner.layout())) { //слева направо
                setDimension(owner.x1, owner.y1, owner.x1 + width, owner.y2);
            }
            //Проверим есть ещё ареа перед текущей, т.к. this area ущё не создана начнём с конца
            for (int index = owner.childs().size() - 1; index >= 0; --index) {
                if (owner.childs().get(index) instanceof AreaBase) {
                    ElemBase prevArea = (ElemBase) owner.childs().get(index);

                    if (eLayoutArea.VERTICAL.equals(owner.layout())) { //сверху вниз
                        setDimension(prevArea.x1, prevArea.y2, owner.x2, prevArea.y2 + height);

                    } else if (eLayoutArea.HORIZONTAL.equals(owner.layout())) { //слева направо
                        setDimension(prevArea.x2, prevArea.y1, prevArea.x2 + width, owner.y2);
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
    protected void parsingParamJson(AreaBase root, String paramJson) {
//        try {
//            if (paramJson != null && paramJson.isEmpty() == false) {
//                String str = paramJson.replace("'", "\"");
//                JSONObject jsonObj = (JSONObject) new JSONParser().parse(str);
//                ArrayList<ArrayList<Long>> jsonArr = (JSONArray) jsonObj.get(eParamJson.pro4Params.name());
//                if (jsonArr instanceof ArrayList && jsonArr.isEmpty() == false) {
//
//                    hmParamJson.put(eParamJson.pro4Params, jsonObj.get(eParamJson.pro4Params.name())); //первый вариант
//
//                    HashMap<Integer, Object[]> hmValue = new HashMap();
//                    for (ArrayList<Long> jsonRec : jsonArr) {
//                        int pnumb = Integer.valueOf(String.valueOf(jsonRec.get(0)));
//                        Parlist rec = Parlist.get(root.getConst(), jsonRec.get(0), jsonRec.get(1));
//                        if (pnumb < 0 && rec != null)
//                            hmValue.put(pnumb, new Object[]{rec.pname, rec.znumb, 0});
//                    }
//                    hmParamJson.put(eParamJson.pro4Params2, hmValue); //второй вариант
//                }
//            }
//        } catch (ParseException e) {
//            System.err.println("Ошибка ElemBase.parsingParamJson() " + e);
//        }
    }

    /**
     * Список элементов окна
     *
     * @param type Тип элемента
     * @param <E> Тип возвращаемого элемента
     * @return Список элементов в контейнере
     */
    public <E> LinkedList<E> getElemList(eTypeElem... type) {
        if (type == null) {
            type = new eTypeElem[]{eTypeElem.FRAME, eTypeElem.IMPOST, eTypeElem.GLASS, eTypeElem.STVORKA};
        }
        LinkedList<Base> arrElem = new LinkedList();
        LinkedList<E> outElem = new LinkedList();
        for (Map.Entry<eLayoutArea, ElemFrame> elemRama : root.getHmElemFrame().entrySet()) {
            arrElem.add(elemRama.getValue());
        }
        for (Base elemBase : root.getArrChild()) { //первый уровень
            arrElem.add(elemBase);
            if (elemBase instanceof AreaBase) {
                for (Map.Entry<eLayoutArea, ElemFrame> elemRama : ((AreaBase) elemBase).getHmElemFrame().entrySet()) {
                    arrElem.add(elemRama.getValue());
                }
                for (Base elemBase2 : elemBase.getArrChild()) { //второй уровень
                    arrElem.add(elemBase2);
                    if (elemBase2 instanceof AreaBase) {
                        for (Map.Entry<eLayoutArea, ElemFrame> elemRama : ((AreaBase) elemBase2).getHmElemFrame().entrySet()) {
                            arrElem.add(elemRama.getValue());
                        }
                        for (Base elemBase3 : elemBase2.getArrChild()) { //третий уровень
                            arrElem.add(elemBase3);
                            if (elemBase3 instanceof AreaBase) {
                                for (Map.Entry<eLayoutArea, ElemFrame> elemRama : ((AreaBase) elemBase3).getHmElemFrame().entrySet()) {
                                    arrElem.add(elemRama.getValue());
                                }
                                for (Base elemBase4 : elemBase3.getArrChild()) { //четвёртый уровень
                                    arrElem.add(elemBase4);
                                    if (elemBase4 instanceof AreaBase) {
                                        for (Map.Entry<eLayoutArea, ElemFrame> elemRama : ((AreaBase) elemBase4).getHmElemFrame().entrySet()) {
                                            arrElem.add(elemRama.getValue());
                                        }
                                        for (Base elemBase5 : elemBase4.getArrChild()) { //пятый уровень
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
                if (elemBase.getTypeElem() == type2) {
                    E elem = (E) elemBase;
                    outElem.add(elem);
                }
            }
        }
        return outElem;
    }

    public LinkedList<Base> getArrChild() { 
        return arrChild;
    }
    
    public void addElem(Base element) {
        arrChild.add(element);
    }

    public EnumMap<eLayoutArea, ElemFrame> getHmElemFrame() {
        return hmElemFrame;
    }
    
    public ElemFrame addFrame(ElemFrame elemFrame) {
        hmElemFrame.put(elemFrame.getLayout(), elemFrame);
        return elemFrame;
    }

    public eLayoutArea layout() {
        return layout;
    }

    public LinkedList<Base> childs() {
        return arrChild;
    }
}
