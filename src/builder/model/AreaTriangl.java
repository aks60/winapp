package builder.model;

import enums.Layout;
import builder.Wincalc;
import builder.script.GsonRoot;
import enums.Type;

public class AreaTriangl extends AreaSimple {

    public AreaTriangl(Wincalc winc) {
        super(winc);
        setDimension(0, 0, winc.rootGson.width2(), winc.rootGson.height1());
    }

    @Override
    //Угловые соединения
    public void joining() {

        super.joining();

        System.out.println("Реализация не определена");
    }
}
