package builder.model;

import enums.Layout;
import builder.Wincalc;
import enums.Type;

public class AreaTrapeze extends AreaSimple {

    public AreaTrapeze(Wincalc iwin, AreaSimple owner, float id, Type type, Layout layout, float width, float height, int color1, int color2, int color3, String param) {
        super(iwin, owner, id, type, layout, width, height, color1, color2, color3, param);
    }

    @Override
    public void joinFrame() {
        System.out.println("Рализация не определена");
    }
}
