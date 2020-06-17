package estimate.model;

import dataset.Record;
import enums.LayoutJoin;
import enums.TypeJoin;
import java.util.ArrayList;
import estimate.Wincalc;
import estimate.constr.Specification;

public class ElemJoining {

    public static final int FIRST_SIDE = 1; //первая сторона
    public static final int SECOND_SIDE = 2; //вторая сторона

    public float id = -1; //идентификатор соединения
    private Wincalc iwin;
    public LayoutJoin layoutJoin = LayoutJoin.NONE; // 
    public TypeJoin typeJoin = TypeJoin.EMPTY;    //тип соединения (то что пишет )

    public ElemSimple joinElement1 = null;  //элемент соединения 1
    public ElemSimple joinElement2 = null;  //элемент соединения 2

    protected float cutAngl1 = 45;    //угол реза1
    protected float cutAngl2 = 45;    //угол реза2
    protected float anglProf = 90;    //угол между профилями
    public String costsJoin = "";     //трудозатраты, ч/ч. 

    protected Record foiningRec = null;
    protected Record joinvarRec = null;
    protected ArrayList<Specification> specificationList = new ArrayList();

    public ElemJoining(Wincalc iwin) {
        this.iwin = iwin;
    }

    public void init(TypeJoin typeJoin, LayoutJoin layoutJoin, ElemSimple joinElement1, ElemSimple joinElement2) {
        this.typeJoin = typeJoin;
        this.layoutJoin = layoutJoin;
        this.joinElement1 = joinElement1;
        this.joinElement2 = joinElement2;
//        if(TypeJoin.VAR40 == typeJoin || TypeJoin.VAR41 == typeJoin) {
//            cutAngl1 = 0;
//            cutAngl2 = 0;
//            anglProf = 0;
//        }
    }

    public float joinAngl(int layout) {
        return (layout == 1) ? cutAngl1 : cutAngl2;
    }

    public boolean equals(Object obj) {
        return id == ((Com5t) obj).id();
    }

    public String toString() {
        return "ELEM: name=" + layoutJoin.name + ", joinElement1=" + joinElement1.id() + ", joinElement1=" + joinElement2.id()
                + ", cutAngl1=" + cutAngl1 + ", cutAngl2=" + cutAngl1 + ", typeJoin=" + layoutJoin + ", varJoin=" + typeJoin + ", anglProf=" + anglProf;
    }
}
