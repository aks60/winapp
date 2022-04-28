package common.Interface;

import builder.model.ElemSimple;
import enums.Layout;

public interface IElemSimple {

    //Клик мышки попадает в контур элемента
    public boolean mouseClick(int X, int Y);

    //Точки соединения профилей (side 0-нач. вектора, 1-конец вектора, 2-точка прилегающего вектора)
    //В этих точках лежат мапы соединений см. Wincalc.mapJoin    
    public String joinPoint(int side);

    //Прилегающие соединения    
    public ElemSimple joinFlat(Layout layoutSide);
    
    //Элемент соединения 0-пред.артикул, 1-след.артикл, 2-прилег. артикл
    public ElemSimple joinElem(int side);    
}
