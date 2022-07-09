package builder.model;

import builder.making.Specific;
import com.google.gson.JsonObject;
import enums.Layout;
import enums.Type;
import java.util.EnumMap;
import java.util.LinkedList;

public interface IArea5e extends ICom5t {

    //Рисуем конструкцию
    void draw();

    void initСonstructiv(JsonObject param);
    
    void addFilling(ElemGlass glass, Specific spcAdd);

    /**
     * T - соединения area. Все поперечены(cross) в area имеют Т-соединения.
     * Т-соед. записываются в map, см. winc.mapJoin.put(point, cross). За
     * угловые соединени отвечает конечнй наследник например
     * AreaRectangl.joining(). Прилегающие см. IElem5e.joinFlat()
     */
    void joining();

    EnumMap<Layout, ElemFrame> frames();
            
    LinkedList<ICom5t> childs();

    void resizeX(float v);

    void resizeX2(float v);

    void resizeY(float v);

    Type type();
}
