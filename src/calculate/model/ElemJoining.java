package calculate.model;

import dataset.Record;
import enums.LayoutJoin;
import enums.TypeJoin;
import java.util.ArrayList;
import calculate.Wincalc;
import calculate.constr.Specification;

public class ElemJoining {

    public static final int FIRST_SIDE = 1; //первая сторона
    public static final int SECOND_SIDE = 2; //вторая сторона

    public float id = -1; //идентификатор соединения
    private Wincalc iwin;
    protected String name;
    protected LayoutJoin typeJoin = LayoutJoin.NONE;
    public TypeJoin varJoin = TypeJoin.EMPTY;    // Вариант соединения

    protected ElemSimple elemJoinTop = null;      //
    protected ElemSimple elemJoinBottom = null;   // Элементы соединения, временно для
    protected ElemSimple elemJoinLeft = null;     // схлопывания повторяющихся обходов
    protected ElemSimple elemJoinRight = null;    //

    public ElemSimple joinElement1 = null;  // Элемент соединения 1
    public ElemSimple joinElement2 = null;  // Элемент соединения 2

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

    public float joinAngl(int layout) {
        return (layout == 1) ? cutAngl1 : cutAngl2;
    }

    public boolean equals(Object obj) {
        return id == ((Com5t) obj).id();
    }

    public String toString() {
        return "ELEM: name=" + name + ", joinElement1=" + joinElement1.id() + ", joinElement1=" + joinElement2.id()
                + ", cutAngl1=" + cutAngl1 + ", cutAngl2=" + cutAngl1 + ", typeJoin=" + typeJoin + ", varJoin=" + varJoin + ", anglProf=" + anglProf;
    }
}
