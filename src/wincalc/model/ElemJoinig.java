package wincalc.model;

import enums.VariantJoin;
import wincalc.Wincalc;

public class ElemJoinig {

    public static final int FIRST_SIDE = 1; //первая сторона
    public static final int SECOND_SIDE = 2; //вторая сторона

    public String id = "0"; //идентификатор соединения

    protected ElemComponent elemJoinTop = null;      //
    protected ElemComponent elemJoinBottom = null;   // Элементы соединения, временно для
    protected ElemComponent elemJoinLeft = null;     // схлопывания повторяющихся обходов
    protected ElemComponent elemJoinRight = null;    //

    protected VariantJoin varJoin = VariantJoin.EMPTY;    // Вариант соединения
    protected ElemComponent joinElement1 = null;  // Элемент соединения 1
    protected ElemComponent joinElement2 = null;  // Элемент соединения 2

    protected float cutAngl1 = 90;    //Угол реза1
    protected float cutAngl2 = 90;    //Угол реза2
    protected float anglProf = 0;     //Угол между профилями
    public String costsJoin = "";     //Трудозатраты, ч/ч. 
    
    public ElemJoinig(Wincalc iwin) {
    }
       
}
