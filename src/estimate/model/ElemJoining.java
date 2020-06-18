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
    public LayoutJoin layoutJoin = LayoutJoin.NONE; //расположение соединения 
    public TypeJoin typeJoin = TypeJoin.EMPTY;  //тип соединения (то что пишет )

    public ElemSimple joinElement1 = null;  //элемент соединения 1
    public ElemSimple joinElement2 = null;  //элемент соединения 2

    protected float anglProf = 90;    //угол между профилями
    public String costsJoin = "";     //трудозатраты, ч/ч. 

    public ElemJoining(Wincalc iwin) {
        this.iwin = iwin;
    }

    public void init(TypeJoin typeJoin, LayoutJoin layoutJoin, ElemSimple joinElement1, ElemSimple joinElement2) {
        this.typeJoin = typeJoin;
        this.layoutJoin = layoutJoin;
        this.joinElement1 = joinElement1;
        this.joinElement2 = joinElement2;
    }

    public float joinAngl(int layout) {
        return (layout == 1) ? joinElement1.anglCut1 : joinElement2.anglCut2;
    }

    public boolean equals(Object obj) {
        return id == ((Com5t) obj).id();
    }

    public String toString() {
        return "ELEM: name=" + layoutJoin.name + ", joinElement1=" + joinElement1.id() + ", joinElement1=" + joinElement2.id()
                + ", cutAngl1=" + joinElement1.anglCut1 + ", cutAngl2=" + joinElement2.anglCut2 + ", typeJoin=" + layoutJoin + ", varJoin=" + typeJoin + ", anglProf=" + anglProf;
    }
}
