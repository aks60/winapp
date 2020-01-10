package wincalc;

import enums.eLayoutArea;
import java.util.EnumMap;
import java.util.LinkedList;
import javax.swing.JComponent;

public abstract class AreaBase implements IBase {

    protected String id = "0"; //идентификатор

    protected Wincalc iwin = null; //главный класс
    protected AreaBase owner = null; //владелец
    private LinkedList<ElemBase> childList = new LinkedList(); //список компонентов в окне    
    private eLayoutArea layout = eLayoutArea.FULL; //порядок расположения компонентов в окне
    protected EnumMap<eLayoutArea, ElemFrame> hmElemFrame = new EnumMap<>(eLayoutArea.class); //список рам в окне

    protected float width = 0; //ширина
    protected float height = 0; //высота
    protected float x1 = 0;
    protected float y1 = 0;
    protected float x2 = 0;
    protected float y2 = 0;

    protected int color1 = -1;
    protected int color2 = -1;
    protected int color3 = -1;

    public AreaBase(String id) {
        this.id = id;
        //setLayout(layout);     
    }

    /**
     * Конструктор root окна
     */
    public AreaBase(AreaBase owner, String id, eLayoutArea layout, float width, float height, int color1, int color2, int color3) {
        this.owner = owner;
        this.layout = layout;
        this.width = width;
        this.height = height;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        //initDimension(owner);
    }

    private void initDimension(AreaBase owner) {
        if (owner != null) {
            //Заполним по умолчанию
            if (eLayoutArea.VERTICAL.equals(owner.getLayout())) { //сверху вниз
                setDimension(owner.x1, owner.y1, owner.x2, owner.y1 + height);

            } else if (eLayoutArea.HORIZONTAL.equals(owner.getLayout())) { //слева направо
                setDimension(owner.x1, owner.y1, owner.x1 + width, owner.y2);
            }
            //Проверим есть ещё ареа перед текущей, т.к. this area ущё не создана начнём с конца
            for (int index = owner.getChildList().size() - 1; index >= 0; --index) {
                if (owner.getChildList().get(index) instanceof AreaBase) {
                    ElemBase prevArea = owner.getChildList().get(index);

                    if (eLayoutArea.VERTICAL.equals(owner.getLayout())) { //сверху вниз
                        setDimension(prevArea.x1, prevArea.y2, owner.x2, prevArea.y2 + height);

                    } else if (eLayoutArea.HORIZONTAL.equals(owner.getLayout())) { //слева направо
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

    public String getId() {
        return id;
    }

    public float width() {
        return width;
    }

    public float height() {
        return width;
    }

    public eLayoutArea getLayout() {
        return layout;
    }

    /**
     * Заполнить главную спецификацию элемента
     */
    public void setDimension(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public LinkedList<ElemBase> getChildList() {
        return childList;
    }
}
