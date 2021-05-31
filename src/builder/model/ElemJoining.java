package builder.model;

import dataset.Record;
import enums.LayoutJoin;
import enums.TypeJoin;
import java.util.ArrayList;
import builder.Wincalc;
import builder.making.Specific;

public class ElemJoining {

    public float id = -1; //идентификатор соединения
    private Wincalc iwin;
    public LayoutJoin layoutJoin = LayoutJoin.NONE; //расположение соединения 
    public TypeJoin typeJoin = TypeJoin.EMPTY;      //тип соединения (то что пишет )

    public ElemSimple joinElement1 = null;  //элемент соединения 1
    public ElemSimple joinElement2 = null;  //элемент соединения 2

    public float anglProf = 90;    //угол между профилями
    public String costsJoin = "";  //трудозатраты, ч/ч. 

    public ElemJoining(Wincalc iwin) {
        this.iwin = iwin;
    }
    
    public ElemJoining(float id, TypeJoin typeJoin, LayoutJoin layoutJoin, ElemSimple joinElement, float anglProf) {
        this.id = id;
        this.typeJoin = typeJoin;
        this.layoutJoin = layoutJoin;
        this.joinElement1 = joinElement;   
        this.anglProf = anglProf;
    }

    public void init(TypeJoin typeJoin, LayoutJoin layoutJoin, ElemSimple joinElement1, ElemSimple joinElement2) {
        this.typeJoin = typeJoin;
        this.layoutJoin = layoutJoin;
        this.joinElement1 = joinElement1;
        this.joinElement2 = joinElement2;
    }

    public boolean equals(Object obj) {
        return id == ((Com5t) obj).id();
    }

    public String toString() {
        return "ELEM: name=" + layoutJoin.name + ", joinElement1=" + joinElement1.id() + ", joinElement2=" + joinElement2.id()
                + ", cutAngl1=" + joinElement1.anglCut[0] + ", cutAngl2=" + joinElement2.anglCut[1] + ", typeJoin=" + layoutJoin + ", varJoin=" + typeJoin + ", anglProf=" + anglProf;
    }
}
