package builder;

import builder.making.Specific;
import dataset.Record;
import enums.Layout;

public interface IElem5e extends ICom5t {

    //Установка координат
    void setLocation();

    //Главная спецификация
    void setSpecific();

    //Вложенная спецификация
    void addSpecific(Specific specification);

    /**
     * Получить элемент прилегающие соединения. Прил. соед. используется для
     * определения координат примыкаемого соединения. (см. )
     *
     * @param layoutSide - сторона прилегания
     * @return - элемент прилегания
     */
    IElem5e joinFlat(Layout layoutSide);

    /**
     * Получить элемент соединения.
     *
     * @param side - сторона соединения 0-пред.артикул, 1-след.артикл,
     * 2-прилег.артикл
     * @return - элемент соединения
     */
    IElem5e joinElem(int side);

    /**
     * Точки соединения профилей (side 0-нач. вектора, 1-конец вектора, 2-точка
     * прилегающего вектора) В этих точках лежат мапы соединений см.
     * Wincalc.mapJoin
     */
    String joinPoint(int side);

    float[] anglFlat();

    float anglHoriz();

    Specific spcRec();

    void anglHoriz(float angl);

    float[] anglCut();

    default float[] sideHoriz() {
        return null;
    }

    default float[] gsize() {
        return null;
    }

    default float gzazo() {
        return 0;
    }

    default void gzazo(float zazo) {
    }

    default float radiusGlass() {
        return 0;
    }

    default Record rasclRec() {
        return null;
    }
    
    default int rasclColor() {
        return -3;
    }

    default int rasclHor() {
        return -3;
    }
    
    default int rasclVert() {
        return -3;
    }  

    //Попадание клика мышки в контур элемента
    boolean mouseClick(int X, int Y);

    String toString();
}
