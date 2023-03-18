package builder;

import builder.Wincalc;
import builder.script.GsonElem;
import com.google.gson.JsonObject;
import dataset.Record;
import enums.Layout;
import enums.Type;

public interface ICom5t {

    int TRANSLATE_XY = 2; //сдвиг графика

    default float id() {
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
    //    public boolean inside2(float x, float y) {
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

    default void setDimension(float x1, float y1, float x2, float y2) {
    }

    default Type type() {
        return null;
    }

    //Высота компонента
    default Float height() {
        return 0f;
    }

    //Ширина компонента
    default Float width() {
        return 0f;
    }

    //Длина компонента
    default float length() {
        return 0f;
    }

    default float x1() {
        return 0f;
    }

    default float x2() {
        return 0f;
    }

    default float y1() {
        return 0f;
    }

    default float y2() {
        return 0f;
    }

    //Ширина в gson
    default float lengthX() {
        return 0f;
    }

    //Высота в gson
    default float lengthY() {
        return 0f;
    }
    
    //Точка попадает в контур четырёхугольника
    default boolean inside(float x, float y) {
        return false;
    }

    default boolean isJson(JsonObject jso, String key) {
        return false;
    }

    default boolean isJson(JsonObject jso) {
        return false;
    }
    
}
