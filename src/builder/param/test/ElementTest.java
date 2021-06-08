package builder.param.test;

import builder.making.Color;
import static builder.param.test.ParamTest.param;

public class ElementTest extends ParamTest {

    public ElementTest() {

        grup = 31000;
        assert true == elementVar2.check(frame_left_2, param("KBE 58;XXX 58;", grup)) : grup;
        assert false == elementVar2.check(frame_left_2, param("KBE58;", grup)) : grup;

        grup = 31001;
        assert true == elementVar2.check(frame_left_2, param("30", grup)) : grup;
        assert false == elementVar2.check(frame_left_2, param("12", grup)) : grup;

        grup = 31002;
        assert true == elementVar2.check(frame_left_2, param("прямой", grup)) : grup;
        assert false == elementVar2.check(frame_left_2, param("арочный", grup)) : grup;

        grup = 31003;
        assert true == elementVar2.check(imp_vert_2, param("807", grup)) : grup;
        assert true == elementVar2.check(imp_vert_2, param("937", grup)) : grup;
        assert true == elementVar2.check(imp_horiz_2, param("807", grup)) : grup;
        assert false == elementVar2.check(imp_horiz_2, param("XXX", grup)) : grup;
        
        grup = 31004;
        assert true == elementVar2.check(stv_right_2, param("937", grup)) : grup;
        assert false == elementVar2.check(stv_right_2, param("XXX", grup)) : grup;
        
        grup = 31005; //33005, 33005, 37005
        assert true == elementVar2.check(frame_left_2, param("0-800;990;1009;1600-2000;", grup)) : grup;
        assert true == elementVar2.check(stv_right_2, param("0-1008;1009", grup)) : grup;
        assert false == elementVar2.check(stv_right_2, param("0-1008;1010", grup)) : grup;
        
        grup = 31006; //33006, 34006, 37006
        assert true == elementVar2.check(frame_left_2, param("0-800;990;1009;1600-2000;", grup)) : grup;
        assert true == elementVar2.check(stv_right_2, param("0-1008;1009", grup)) : grup;
        assert false == elementVar2.check(stv_right_2, param("0-1008;1010", grup)) : grup;
        
        grup = 31007; //33007, 34007, 37007
        assert true == elementVar2.check(frame_left_2, param("0-800;990;1009;1600-2000;", grup)) : grup;
        assert true == elementVar2.check(stv_right_2, param("0-1008;1009", grup)) : grup;
        assert false == elementVar2.check(stv_right_2, param("0-1008;1010", grup)) : grup;
        
        grup = 31008;
        assert true == elementVar2.check(null, param("30", grup)) : grup;
        assert false == elementVar2.check(null, param("32", grup)) : grup;
        
        grup = 31011;
        assert true == elementVar2.check(imp_vert_2, param("30/30", grup)) : grup;
        assert true == elementVar2.check(imp_vert_2, param("*/4-32", grup)) : grup;
        assert false == elementVar2.check(imp_vert_2, param("3;31;12/4-32", grup)) : grup;

        grup = 31017; //37017
        assert true == elementVar2.check(frame_left_2, param("КВЕ 58", grup)) : grup;
        assert false == elementVar2.check(frame_left_2, param("КП-40", grup)) : grup;
        
        grup = 31019;
        elementVar3.check(stv_right_3, param("внутренняя по основной", grup));
        Color.colorFromParam(stv_right_3);
        assert stv_right_3.spcRec.colorID2 == stv_right_3.spcRec.colorID1 : grup;

        grup = 31020;
        assert true == elementVar2.check(frame_left_2, param("90;270", grup)) : grup;
        assert false == elementVar2.check(frame_right_2, param("91;180", grup)) : grup;        
        assert false == elementVar2.check(imp_horiz_2, param("0-89;270,9-359,9", grup)) : grup;        

        grup = 31033;
        assert true == elementVar2.check(imp_vert_2, param("807", grup)) : grup;        

        grup = 31034;
        assert true == elementVar2.check(imp_vert_2, param("937", grup)) : grup;  
        
        
    }
}
