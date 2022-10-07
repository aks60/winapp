package builder;

import builder.making.Specific;
import builder.model.ElemGlass;
import com.google.gson.JsonObject;
import enums.Layout;
import enums.Type;
import java.util.EnumMap;
import java.util.LinkedList;

public interface IArea5e extends ICom5t {

    //Рисуем конструкцию
    void draw();

    void initСonstructiv(JsonObject param);
    /**
     * Добавление детализации в заполнение
     * @param glass - элемент заполнения
     * @param spcAdd - строка детализации
     */
    void addFilling(IElem5e glass, Specific spcAdd);

    /**
     * T - соединения area. Все поперечены(cross) в area имеют Т-соединения.
     * Т-соед. записываются в map, см. winc.mapJoin.put(point, cross). За
     * угловые соединени отвечает конечнй наследник например
     * AreaRectangl.joining(). Прилегающие см. IElem5e.joinFlat()
     */
    void joining();

    EnumMap<Layout, IElem5e> frames();
            
    LinkedList<ICom5t> childs();

    void resizeX(float v);

    void resizeY(float v);

    Type type();
}
