package builder.model;

import enums.Layout;
import builder.Wincalc;
import builder.script.GsonRoot;
import enums.LayoutJoin;
import enums.PKjson;
import enums.Type;
import enums.TypeJoin;

public class AreaTrapeze extends AreaSimple {
    
    public AreaTrapeze(Wincalc iwin, GsonRoot gson, int color1, int color2, int color3) {
        super(iwin, null, gson.id(), Type.TRIANGL, gson.layout(), gson.width(), gson.height(), color1, color2, color3, gson.param());
        setDimension(0, 0, gson.width(), gson.height());
        this.view = gson.view;
    }
    
    @Override
    public void joinFrame() {
        ElemSimple elemBott = mapFrame.get(Layout.BOTT), elemRight = mapFrame.get(Layout.RIGHT),
                elemTop = mapFrame.get(Layout.TOP), elemLeft = mapFrame.get(Layout.LEFT);
        //Цикл по сторонам рамы
        for (int index = 0; index < 4; index++) {
            
            if (index == 0) { //Угловое соединение правое нижнее
                ElemJoining el = new ElemJoining(iwin(), TypeJoin.VAR20, LayoutJoin.RBOT, elemBott, elemRight, 90);
                iwin().mapJoin.put(elemBott.joinPoint(1), el);

            } else if (index == 1) { //Угловое соединение правое верхнее
                ElemJoining el = new ElemJoining(iwin(), TypeJoin.VAR20, LayoutJoin.RTOP, elemRight, elemTop, 90);
                iwin().mapJoin.put(elemRight.joinPoint(1), el);

            } else if (index == 2) { //Угловое соединение левое верхнее    
                ElemJoining el = new ElemJoining(iwin(), TypeJoin.VAR20, LayoutJoin.LTOP, elemTop, elemLeft, 90);
                iwin().mapJoin.put(elemTop.joinPoint(1), el);

            } else if (index == 3) { //Угловое соединение левое нижнее
                ElemJoining el = new ElemJoining(iwin(), TypeJoin.VAR20, LayoutJoin.LBOT, elemLeft, elemRight, 90);
                iwin().mapJoin.put(elemLeft.joinPoint(1), el);
            }
//            if(view == 2) {
//                ElemJoining el = new ElemJoining(iwin(), TypeJoin.VAR20, LayoutJoin.LTOP, elemTop, elemLeft, 90);
//                iwin().mapJoin.put(elemTop.joinPoint(1), el);                
//            }
        }        
    }
}
