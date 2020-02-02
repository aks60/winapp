package wincalc.model;

import dataset.Record;
import enums.JoinLocate;
import enums.JoinVariant;
import java.util.ArrayList;
import wincalc.Wincalc;
import wincalc.constr.Specification;

public class ElemJoining {

    public static final int FIRST_SIDE = 1; //первая сторона
    public static final int SECOND_SIDE = 2; //вторая сторона

    public String id = "0"; //идентификатор соединения
    private Wincalc iwin;
    protected String name;
    protected JoinLocate typeJoin = JoinLocate.NONE;
    protected JoinVariant varJoin = JoinVariant.EMPTY;    // Вариант соединения

    protected ElemSimple elemJoinTop = null;      //
    protected ElemSimple elemJoinBottom = null;   // Элементы соединения, временно для
    protected ElemSimple elemJoinLeft = null;     // схлопывания повторяющихся обходов
    protected ElemSimple elemJoinRight = null;    //

    protected ElemSimple joinElement1 = null;  // Элемент соединения 1
    protected ElemSimple joinElement2 = null;  // Элемент соединения 2
    
    protected float cutAngl1 = 45;    //Угол реза1
    protected float cutAngl2 = 45;    //Угол реза2
    protected float anglProf = 90;    //Угол между профилями
    public String costsJoin = "";     //Трудозатраты, ч/ч. 

    protected Record foiningRec = null;
    protected Record joinvarRec = null;
    protected ArrayList<Specification> specificationList = new ArrayList();

    public ElemJoining(Wincalc iwin) {
        this.iwin = iwin;
    }

    public float getAnglJoin(int layout) {
        return (layout == 1) ? cutAngl1 : cutAngl2;
    }

    public JoinVariant getVarJoin() {
        return varJoin;
    }

    public ElemSimple getJoinElement(int i) {
        return (i == 1) ? joinElement1 : joinElement2;
    }

    public String toString() {
        return name;
    }
}
