package wincalc;

import enums.eLayoutArea;
import java.util.EnumMap;
import java.util.LinkedList;
import javax.swing.JComponent;

public abstract class AreaBase  extends JComponent implements IBase {

    protected Wincalc iwin = null; //главный класс
    private eLayoutArea layout = eLayoutArea.FULL; //порядок расположения компонентов в окне
    private LinkedList<ElemBase> childList = new LinkedList(); //список компонентов в окне
    protected EnumMap<eLayoutArea, ElemFrame> hmElemFrame = new EnumMap<>(eLayoutArea.class); //список рам в окне
    
    protected String id = "0"; //идентификатор
    protected int width = 0; //ширина
    protected int height = 0; //высота

    public AreaBase(String id) {
        this.id = id;
        
        //setLayout(layout);
        setDoubleBuffered(true);      
    }

    public String getId() {
        return id;
    }

    public int width() {
        return width;
    }

    public int height() {
        return width;
    }
    
    public eLayoutArea getLayoutArea() {
        return layout;
    }     
}
