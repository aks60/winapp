package builder;

import builder.ICom5t;
import builder.making.Specific;
import enums.Layout;

public interface IElem5e extends ICom5t {

    //Установка координат
    void setLocation();

    //Главная спецификация
    void setSpecific();

    //Вложенная спецификация
    void addSpecific(Specific specification);

    /**
     * Элемент соединения 0-пред.артикул, 1-след.артикл, 2-прилег. артикл
     */
    IElem5e joinElem(int side);

    /**
     * Прилегающие соединения элемента. Все элементы могут иметь прилегающие
     * соединения. Прил. соед. используется для определения координат
     * примыкаемого соединения. (см. )
     */
    IElem5e joinFlat(Layout layoutSide);

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

    //Клик мышки попадает в контур элемента
    boolean mouseClick(int X, int Y);

    String toString();
}
