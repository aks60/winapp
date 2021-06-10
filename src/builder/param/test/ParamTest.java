package builder.param.test;

import builder.model.AreaSimple;
import builder.model.Com5t;
import builder.model.ElemFrame;
import builder.model.ElemSimple;
import builder.param.ElementDet;
import builder.param.ElementVar;
import dataset.Record;
import enums.LayoutArea;
import enums.TypeElem;
import java.util.HashMap;

public class ParamTest {

    protected HashMap<Integer, String> hmParam = new HashMap();
    protected int grup = -1;
    protected Record record = null;

    protected ElemSimple frame_left_2 = null;
    protected ElemSimple frame_right_2 = null;
    protected ElemSimple stv_right_2 = null;
    protected ElemSimple imp_horiz_2 = null;
    protected ElemSimple imp_vert_2 = null;
    protected ElemSimple glass_top_2 = null;
    protected ElemSimple glass_left_2 = null;
    protected ElemSimple glass_right_2 = null;

    protected ElemSimple frame_left_3 = null;
    protected ElemSimple frame_right_3 = null;
    protected ElemSimple stv_right_3 = null;

    protected ElemSimple frame_left_4 = null;
    protected ElemSimple frame_right_4 = null;
    protected ElemSimple stv_left_4 = null;
    protected ElemSimple stv_right_4 = null;

    builder.Wincalc iwin_2 = new builder.Wincalc(); //601004
    protected ElementVar elementVar2 = null;
    protected ElementDet elementDet2 = null;

    builder.Wincalc iwin_3 = new builder.Wincalc(); //604005
    protected ElementVar elementVar3 = null;
    protected ElementDet elementDet3 = null;

    builder.Wincalc iwin_4 = new builder.Wincalc(); //700027
    protected ElementVar elementVar4 = null;
    protected ElementDet elementDet4 = null;

    public ParamTest() {

        iwin_2.build(builder.script.Winscript.test(601004, false));
        iwin_2.constructiv(true);
        elementVar2 = new ElementVar(iwin_2);
        elementDet2 = new ElementDet(iwin_2);
        frame_left_2 = getElem(iwin_2.rootArea, 1.0f);
        frame_right_2 = getElem(iwin_2.rootArea, 1.0f);
        stv_right_2 = getElem(iwin_2.rootArea, 10.2f);
        imp_vert_2 = getElem(iwin_2.rootArea, 12.0f);
        imp_horiz_2 = getElem(iwin_2.rootArea, 7.0f);
        glass_top_2 = getElem(iwin_2.rootArea, 6.0f);
        glass_left_2 = getElem(iwin_2.rootArea, 11.0f);
        glass_right_2 = getElem(iwin_2.rootArea, 15.0f);

        iwin_3.build(builder.script.Winscript.test(604005, false));
        iwin_3.constructiv(true);
        elementVar3 = new ElementVar(iwin_3);
        elementDet3 = new ElementDet(iwin_3);
        frame_left_3 = getElem(iwin_3.rootArea, 1.0f);
        frame_right_3 = getElem(iwin_3.rootArea, 2.0f);
        stv_right_3 = getElem(iwin_3.rootArea, 9.2f);

        iwin_4.build(builder.script.Winscript.test(700027, false));
        iwin_4.constructiv(true);
        elementVar4 = new ElementVar(iwin_4);
        elementDet4 = new ElementDet(iwin_4);
        frame_left_4 = getElem(iwin_4.rootArea, 1.0f);
        frame_right_4 = getElem(iwin_4.rootArea, 2.0f);
        stv_left_4 = getElem(iwin_4.rootArea, 6.4f);
        stv_right_4 = getElem(iwin_4.rootArea, 6.2f);
    }

    //Получить элемент по ключу
    public ElemSimple getElem(AreaSimple rootArea, float id) {
        for (ElemFrame frm : rootArea.mapFrame.values()) {
            if (frm.id() == id) {
                return (ElemSimple) frm;
            }
        }
        for (Com5t it1 : rootArea.listChild) {
            if (it1.id() == id) {
                return (ElemSimple) it1;
            }
            if (it1 instanceof AreaSimple) {
                for (ElemFrame frm : ((AreaSimple) it1).mapFrame.values()) {
                    if (frm.id() == id) {
                        return (ElemSimple) frm;
                    }
                }
            }
            if (it1 instanceof AreaSimple) {
                for (Com5t it2 : ((AreaSimple) it1).listChild) {
                    if (it2.id() == id) {
                        return (ElemSimple) it2;
                    }
                    if (it2 instanceof AreaSimple) {
                        for (ElemFrame frm : ((AreaSimple) it2).mapFrame.values()) {
                            if (frm.id() == id) {
                                return frm;
                            }
                        }
                    }
                    if (it2 instanceof AreaSimple) {
                        for (Com5t it3 : ((AreaSimple) it2).listChild) {
                            if (it3.id() == id) {
                                return (ElemSimple) it3;
                            }
                            if (it3 instanceof AreaSimple) {
                                for (ElemFrame frm : ((AreaSimple) it3).mapFrame.values()) {
                                    if (frm.id() == id) {
                                        return frm;
                                    }
                                }
                            }
                            if (it3 instanceof AreaSimple) {
                                for (Com5t it4 : ((AreaSimple) it3).listChild) {
                                    if (it4.id() == id) {
                                        return (ElemSimple) it4;
                                    }
                                    if (it4 instanceof AreaSimple) {
                                        for (ElemFrame frm : ((AreaSimple) it4).mapFrame.values()) {
                                            if (frm.id() == id) {
                                                return frm;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public static Record param(String txt, int grup) {
        Record record = new Record();
        record.add("SEL");
        record.add(-3);
        record.add(txt);
        record.add(grup);
        record.add(-3);
        return record;
    }
}
