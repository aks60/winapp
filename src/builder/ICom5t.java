package builder;

import builder.script.GsonElem;
import com.google.gson.JsonObject;
import dataset.Record;
import enums.Layout;
import enums.Type;

public interface ICom5t {

    int TRANSLATE_XY = 2; //сдвиг графика

    default double id() {
        return 0f;
    }

    default IArea5e owner() {
        return null;
    }

    default IArea5e root() {
        return null;
    }

    default GsonElem gson() {
        return null;
    }

    default Wincalc winc() {
        return null;
    }

    default int colorID1() {
        return -1;
    }

    default int colorID2() {
        return -1;
    }

    default int colorID3() {
        return -1;
    }

    default Layout layout() {
        return null;
    }

    default void layout(Layout x) {
    }

    default Record sysprofRec() {
        return null;
    }

    default void sysprofRec(Record record) {
    }

    default Record artiklRec() {
        return null;
    }

    default void artiklRec(Record record) {
    }

    default Record artiklRecAn() {
        return null;
    }

    default void artiklRecAn(Record record) {
    }

    // <editor-fold defaultstate="collapsed" desc="inside2 см.инет Задача о принадлежности точки многоугольнику">
    //    public boolean inside2(double x, double y) {
    //        int X = (int) x, Y = (int) y;
    //        //int X1 = (int) x1, X2 = (int) x2, Y1 = (int) y1, Y2 = (int) y2;
    //        //int xp[] = {X1, X2, X2, X1}; // массив X-координат полигона
    //        //int yp[] = {Y1, Y1, Y2, Y2}; // массив Y-координат полигона
    //
    //        int xp[] = {4, 800, 800, 4}, yp[] = {4, 4, 20, 20}; //test
    //        int j = xp.length - 1;
    //        boolean result = false;
    //        for (int i = 0; i < 4; ++i) {
    //            if ((((yp[i] <= Y) && (Y < yp[j])) || ((yp[j] <= Y) && (Y < yp[i])))
    //                    && ((X > (xp[j] - xp[i]) * (Y - yp[i]) / (yp[j] - yp[i]) + xp[i]))) {
    //                result = !result;
    //            }
    //            j = i;
    //        }
    //        return result;
    //    }
    // </editor-fold>
    default void paint() {
    }

    default void setDimension(double x1, double y1, double x2, double y2) {
    }

    default Type type() {
        return null;
    }

    //Высота компонента
    default Double height() {
        return 0.0;
    }

    //Ширина компонента
    default Double width() {
        return 0.0;
    }

    //Длина компонента
    default double length() {
        return 0f;
    }

    default double x1() {
        return 0f;
    }

    default double x2() {
        return 0f;
    }

    default double y1() {
        return 0f;
    }

    default double y2() {
        return 0f;
    }

    //Ширина в gson
    default double lengthX() {
        return 0f;
    }

    //Высота в gson
    default double lengthY() {
        return 0f;
    }
    
    //Точка попадает в контур четырёхугольника
    default boolean inside(double x, double y) {
        return false;
    }

    default boolean isJson(JsonObject jso, String key) {
        return false;
    }

    default boolean isJson(JsonObject jso) {
        return false;
    }
    
}
