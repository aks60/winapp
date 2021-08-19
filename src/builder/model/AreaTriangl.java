package builder.model;

import enums.Layout;
import builder.Wincalc;
import enums.Type;

public class AreaTriangl extends AreaSimple {

    public AreaTriangl(Wincalc iwin, AreaSimple owner, Type type, Layout layout, float width, float height, int color1, int color2, int color3, String param) {
        super(iwin, owner, type, layout, width, height, color1, color2, color3, param);
    }

    @Override
    public void joinFrame() {
        System.out.println("Ркализация не определена");
    }
}
