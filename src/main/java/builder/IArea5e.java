package builder;

import builder.making.Specific;
import com.google.gson.JsonObject;
import common.LinkedCom;
import enums.Layout;
import enums.Type;
import java.util.EnumMap;

public interface IArea5e extends ICom5t {

    //Рисуем конструкцию
    void draw();

    void initСonstructiv(JsonObject param);

    /**
     * T - соединения area. Все поперечены(cross) в area имеют Т-соединения.
     * Т-соед. записываются в map, см. winc.listJoin.put(point, cross). За
     * угловые соединени отвечает конечнй наследник например
     * AreaRectangl.joining(). Прилегающие см. IElem5e.joinFlat()
     */
    void joining();

    EnumMap<Layout, IElem5e> frames();
            
    LinkedCom<ICom5t> childs();

    void resizeX(double v);

    void resizeY(double v);
    
    IElem5e joinSide(Layout side);

    Type type();
}
