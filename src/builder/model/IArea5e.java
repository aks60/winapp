package builder.model;

import com.google.gson.JsonObject;
import enums.Type;

public interface IArea5e extends ICom5t{

    //Рисуем конструкцию
    void draw();

    void initСonstructiv(JsonObject param);

    /**
     * T - соединения area. Все поперечены(cross) в area имеют Т-соединения.
     * Т-соед. записываются в map, см. winc.mapJoin.put(point, cross). За
     * угловые соединени отвечает конечнй наследник например
     * AreaRectangl.joining(). Прилегающие см. ElemSimple.joinFlat()
     */
    void joining();

    void resizeX(float v);

    void resizeX2(float v);

    void resizeY(float v);

    Type type();    
}
