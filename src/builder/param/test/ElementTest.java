package builder.param.test;

import builder.model.ElemSimple;
import builder.param.ElementVar;
import enums.LayoutArea;
import static builder.param.test.ParamTest.param;
import domain.eArtikl;

public class ElementTest extends ParamTest {

    public ElementTest() {

        grup = 31000;
        assert true == elementVar2.check(frame_side_left_2, param("KBE 58;XXX 58;", grup)) : grup;
        assert false == elementVar2.check(frame_side_left_2, param("KBE58;", grup)) : grup;

        grup = 31001;
        assert true == elementVar2.check(frame_side_left_2, param("30", grup)) : grup;
        assert false == elementVar2.check(frame_side_left_2, param("12", grup)) : grup;

        grup = 31002;
        assert true == elementVar2.check(frame_side_left_2, param("прямой", grup)) : grup;
        assert false == elementVar2.check(frame_side_left_2, param("арочный", grup)) : grup;

        grup = 31003;
        assert true == elementVar2.check(imp_vert_2, param("807", grup)) : grup;
        assert false == elementVar2.check(imp_vert_2, param("777", grup)) : grup;

    }
}
