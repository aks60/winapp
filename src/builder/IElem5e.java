package builder;

import builder.making.Specific;
import dataset.Record;
import enums.Layout;

public interface IElem5e extends ICom5t {

    //Главная спецификация
    void setSpecific();

    //Вложенная спецификация
    void addSpecific(Specific spc);

    /**
     * Получить элемент прилегающего профиля. 
     * 
     * Прил. соед. используется для
     * определения координат примыкаемого соединения. (см. )
     * @param layoutSide - сторона прилегания
     * @return - элемент прилегания
     */
    IElem5e joinFlat(Layout layoutSide);

    float[] anglFlat();

    float anglHoriz();

    Specific spcRec();

    void anglHoriz(float angl);

    float[] anglCut();

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

    default int rasclNumber(int num) {
        return 2;
    }

    /**
     * Попадание клика мышки в контур элемента
     */
    boolean mouseClick(int X, int Y);

    default void rascladkaPaint() {
    }

    String toString();
}
