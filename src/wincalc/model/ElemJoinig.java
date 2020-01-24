package wincalc.model;

import dataset.Record;
import enums.VariantJoin;
import java.util.ArrayList;
import wincalc.Wincalc;
import wincalc.constr.Specification;

public class ElemJoinig {

    public static final int FIRST_SIDE = 1; //первая сторона
    public static final int SECOND_SIDE = 2; //вторая сторона

    public String id = "0"; //идентификатор соединения
    private Wincalc iwin;
    protected ElemComp elemJoinTop = null;      //
    protected ElemComp elemJoinBottom = null;   // Элементы соединения, временно для
    protected ElemComp elemJoinLeft = null;     // схлопывания повторяющихся обходов
    protected ElemComp elemJoinRight = null;    //

    protected VariantJoin varJoin = VariantJoin.EMPTY;    // Вариант соединения
    protected ElemComp joinElement1 = null;  // Элемент соединения 1
    protected ElemComp joinElement2 = null;  // Элемент соединения 2

    protected float cutAngl1 = 90;    //Угол реза1
    protected float cutAngl2 = 90;    //Угол реза2
    protected float anglProf = 0;     //Угол между профилями
    public String costsJoin = "";     //Трудозатраты, ч/ч. 
    
    protected Record connlstRec = null;
    protected Record connvarRec = null;
    protected ArrayList<Specification> specificationList = new ArrayList();
    
    public ElemJoinig(Wincalc iwin) {
        this.iwin = iwin;
    }
    
    //Инициализация вариантов соединения и первичная углов реза
    public void initJoin() {

        if (elemJoinLeft == null && elemJoinRight != null && elemJoinBottom != null && elemJoinTop == null) { //угловое соединение левое верхнее
            joinElement1 = elemJoinBottom;
            joinElement2 = elemJoinRight;
            varJoin = VariantJoin.VAR2;
            elemJoinBottom.anglCut(ElemComp.SIDE_START, cutAngl1);
            elemJoinRight.anglCut(ElemComp.SIDE_END, cutAngl2);

        } else if (elemJoinLeft == null && elemJoinRight != null && elemJoinBottom == null && elemJoinTop != null) { //угловое соединение левое нижнее
            joinElement1 = elemJoinTop;
            joinElement2 = elemJoinRight;
            varJoin = VariantJoin.VAR2;
            elemJoinTop.anglCut(ElemComp.SIDE_END, cutAngl1);
            elemJoinRight.anglCut(ElemComp.SIDE_START, cutAngl2);

        } else if (elemJoinLeft != null && elemJoinRight == null && elemJoinBottom != null && elemJoinTop == null) { //угловое соединение правое верхнее
            joinElement2 = elemJoinLeft;
            joinElement1 = elemJoinBottom;
            varJoin = VariantJoin.VAR2;
            elemJoinLeft.anglCut(ElemComp.SIDE_START, cutAngl2);
            elemJoinBottom.anglCut(ElemComp.SIDE_END, cutAngl1);

        } else if (elemJoinLeft != null && elemJoinRight == null && elemJoinTop != null && elemJoinBottom == null) { //угловое соединение правое нижнее
            joinElement2 = elemJoinLeft;
            joinElement1 = elemJoinTop;
            varJoin = VariantJoin.VAR2;
            elemJoinLeft.anglCut(ElemComp.SIDE_END, cutAngl2);
            elemJoinTop.anglCut(ElemComp.SIDE_START, cutAngl1);

        } else if ((elemJoinLeft != null && elemJoinBottom != null && elemJoinRight != null && elemJoinTop == null) && elemJoinLeft.equals(elemJoinRight)) { //T - соединение верхнее
            joinElement2 = elemJoinLeft;
            joinElement1 = elemJoinBottom;
            varJoin = VariantJoin.VAR4;
            elemJoinBottom.anglCut(ElemComp.SIDE_START, cutAngl1);

        } else if (elemJoinLeft != null && elemJoinTop != null && elemJoinRight != null && elemJoinBottom == null && elemJoinLeft.equals(elemJoinRight)) { //T - соединение нижнее
            joinElement1 = elemJoinTop;
            joinElement2 = elemJoinLeft;
            varJoin = VariantJoin.VAR4;
            elemJoinTop.anglCut(ElemComp.SIDE_END, cutAngl1);

        } else if (elemJoinLeft == null && elemJoinTop != null && elemJoinRight != null && elemJoinBottom != null && elemJoinTop.equals(elemJoinBottom)) { //T - соединение левое
            joinElement2 = elemJoinTop;
            joinElement1 = elemJoinRight;
            varJoin = VariantJoin.VAR4;
            elemJoinRight.anglCut(ElemComp.SIDE_START, cutAngl2);

        } else if (elemJoinLeft != null && elemJoinTop != null && elemJoinRight == null && elemJoinBottom != null && elemJoinTop.equals(elemJoinBottom)) { //T - соединение правое
            joinElement2 = elemJoinTop;
            joinElement1 = elemJoinLeft;
            varJoin = VariantJoin.VAR4;
            elemJoinLeft.anglCut(ElemComp.SIDE_END, cutAngl2);

        } else {
            System.out.println("Инициализация соединения не выполнена  - LEFT.id=" + elemJoinLeft.id + " - RIGHT.id=" + elemJoinRight.id + " - BOTT.id=" + elemJoinBottom.id + " - TOP.id=" + elemJoinTop.id);
        }
    }

    public float getAnglJoin(int side) {
        return (side == 1) ? cutAngl1 : cutAngl2;
    }

    public VariantJoin getVarJoin() {
        return varJoin;
    }

    public ElemComp getJoinElement(int i) {
        return (i == 1) ? joinElement1 : joinElement2;
    }       
}
