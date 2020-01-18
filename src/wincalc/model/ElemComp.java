package wincalc.model;

import enums.LayoutArea;
import wincalc.constr.Specification;
import enums.TypeElem;
import enums.TypeProfile;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;
import javafx.scene.shape.ArcType;
import static wincalc.model.Comp4t.moveXY;

public abstract class ElemComp extends Comp4t {

    protected float anglHoriz = -1; //угол к горизонту
    protected LayoutArea side = LayoutArea.NONE; //сторона расположения элемента

    public ElemComp(String id) {        
        super(id);
    }

    @Override
    public LinkedList<Comp4t> listChild() {
        return new LinkedList();
    }

    /**
     * Типы профилей
     */
    public abstract TypeProfile typeProfile();

    /**
     * Добавить спецификацию в состав элемента
     */
    public abstract void addSpecifSubelem(Specification specification);

    public void anglCut(int side, float anglCut) {
    }

    /**
     * Получить предыдущий элемент в контейнере
     */
    public ElemComp prevElem() {
        /*
        for (ListIterator<ElemBase> iter = getAreaElemList().listIterator(); iter.hasNext(); ) {
            if (iter.next().id == id) { //находим элемент в списке и от него движемся вверх
                iter.previous();
                if (iter.hasPrevious()) {
                    return iter.previous();
                }
                return iter.next();
            }
        }
        return owner;*/
        return null;
    }

    /**
     * Генерация нового ключа
     */
    public String genId() {
        int maxId = 0;
        LinkedList<ElemComp> elemList = root().elemList(TypeElem.FRAME, TypeElem.IMPOST, TypeElem.GLASS, TypeElem.STVORKA);
        for (ElemComp elemBase : elemList) {
            for (Specification specification : elemBase.specificationRec.specificationList()) {
                if (Integer.valueOf(elemBase.specificationRec.id) > maxId) {
                    maxId = Integer.valueOf(elemBase.specificationRec.id);
                }
                if (Integer.valueOf(specification.id) > maxId) {
                    maxId = Integer.valueOf(specification.id);
                }
            }
        }
        return String.valueOf(++maxId);
    }

    protected void strokePolygon(float x1, float x2, float x3, float x4, float y1,
            float y2, float y3, float y4, int rgbFill, Color rdbStroke, double lineWidth) {

        float scale = iwin.scale;
        Graphics2D gc = iwin.img.createGraphics();
        gc.setStroke(new BasicStroke((float) lineWidth)); //толщина линии
        gc.setColor(java.awt.Color.BLACK);
        float h = iwin.heightAdd - iwin.height;
        gc.drawPolygon(new int[]{(int) ((x1 + moveXY) * scale), (int) ((x2 + moveXY) * scale), (int) ((x3 + moveXY) * scale), (int) ((x4 + moveXY) * scale)},
                new int[]{(int) ((y1 + moveXY + h) * scale), (int) ((y2 + moveXY + h) * scale), (int) ((y3 + moveXY + h) * scale), (int) ((y4 + moveXY + h) * scale)}, 4);
        gc.setColor(new java.awt.Color(rgbFill & 0x000000FF, (rgbFill & 0x0000FF00) >> 8, (rgbFill & 0x00FF0000) >> 16));
        gc.fillPolygon(new int[]{(int) ((x1 + moveXY) * scale), (int) ((x2 + moveXY) * scale), (int) ((x3 + moveXY) * scale), (int) ((x4 + moveXY) * scale)},
                new int[]{(int) ((y1 + moveXY + h) * scale), (int) ((y2 + moveXY + h) * scale), (int) ((y3 + moveXY + h) * scale), (int) ((y4 + moveXY + h) * scale)}, 4);
    }

    protected void strokeArc(double x, double y, double w, double h, double startAngle,
            double arcExtent, ArcType closure, int rdbStroke, double lineWidth) {

        float scale = iwin.scale;
        Graphics2D gc = iwin.img.createGraphics();
        gc.setStroke(new BasicStroke((float) lineWidth * scale)); //толщина линии
        gc.setColor(new java.awt.Color(rdbStroke & 0x000000FF, (rdbStroke & 0x0000FF00) >> 8, (rdbStroke & 0x00FF0000) >> 16));
        gc.drawArc((int) ((x + moveXY) * scale), (int) ((y + moveXY) * scale), (int) (w * scale), (int) (h * scale), (int) startAngle, (int) arcExtent);
    }

    protected void fillArc(double x, double y, double w, double h, double startAngle, double arcExtent) {

        float scale = iwin.scale;
        Graphics2D gc = iwin.img.createGraphics();
        gc.setColor(new java.awt.Color(226, 255, 250));
        gc.fillArc((int) ((x + moveXY) * scale), (int) ((y + moveXY) * scale), (int) (w * scale), (int) (h * scale), (int) startAngle, (int) arcExtent);
    }

    protected void fillPoligon(float x1, float x2, float x3, float x4, float y1, float y2, float y3, float y4) {

        float scale = iwin.scale;
        Graphics2D gc = iwin.img.createGraphics();
        gc.setColor(new java.awt.Color(226, 255, 250));
        float h = iwin.heightAdd - iwin.height;
        gc.fillPolygon(new int[]{(int) ((x1 + moveXY) * scale), (int) ((x2 + moveXY) * scale), (int) ((x3 + moveXY) * scale), (int) ((x4 + moveXY) * scale)},
                new int[]{(int) ((y1 + moveXY + h) * scale), (int) ((y2 + moveXY + h) * scale), (int) ((y3 + moveXY + h) * scale), (int) ((y4 + moveXY + h) * scale)}, 4);
    }
}
