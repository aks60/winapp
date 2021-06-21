package builder.param.test;

import builder.making.Color;
import static builder.param.test.ParamTest.param;
import dataset.Record;
import domain.eElement;
import java.util.HashMap;

public class ElementTest extends ParamTest {

    public void elementVar() {

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

        grup = 31037;
        assert true == elementVar2.check(stv_right_2, param("ROTO NT Повортные окна/двери", grup)) : grup;
        assert false == elementVar2.check(stv_right_2, param("ROTO NT Повортные", grup)) : grup;
        assert false == elementVar2.check(stv_right_2, param("ACCADO NT Повортные", grup)) : grup;

        grup = 31041;
        assert true == elementVar2.check(stv_right_2, param("400,1-10000", grup)) : grup;
        assert false == elementVar2.check(stv_right_2, param("400,1-800", grup)) : grup;

        grup = 31050;
        assert true == elementVar2.check(stv_right_2, param("2", grup)) : grup;
        assert false == elementVar2.check(stv_right_2, param("1", grup)) : grup;

        grup = 31051;
        assert true == elementVar4.check(stv_right_4, param("ведомая", grup)) : grup;
        assert false == elementVar4.check(stv_right_4, param("ведущая", grup)) : grup;

        grup = 31052;
        assert true == elementVar4.check(stv_right_4, param("600", grup)) : grup;
        assert true == elementVar4.check(stv_right_4, param("60", grup)) : grup;

        grup = 31054;
        assert true == elementVar4.check(stv_right_4, param("1000-1010;", grup)) : grup;
        assert false == elementVar2.check(stv_right_4, param("900-990;", grup)) : grup;

        grup = 31055;
        assert true == elementVar4.check(stv_right_4, param("1000-10010;", grup)) : grup;
        assert false == elementVar3.check(stv_right_3, param("1000-1010;", grup)) : grup;

        grup = 31056; //37056
        assert true == elementVar4.check(stv_right_4, param("1000-10010;", grup)) : grup;
        assert true == elementVar3.check(stv_right_3, param("1000-1010;", grup)) : grup;
        assert false == elementVar3.check(stv_right_3, param("100-310;", grup)) : grup;

        grup = 31060;
        assert true == elementVar4.check(stv_right_4, param("90", grup)) : grup;
        assert false == elementVar4.check(stv_right_4, param("30;", grup)) : grup;

        grup = 31095;
        assert true == elementVar4.check(stv_right_4, param("1;2;", grup)) : grup;
        assert false == elementVar4.check(stv_right_4, param("2;9", grup)) : grup;
        
        grup = 37002;
        assert true == elementVar2.check(frame_left_2, param("807", grup)) : grup;
        assert false == elementVar2.check(frame_left_2, param("800", grup)) : grup;

        grup = 37008;
        assert false == elementVar2.check(stv_right_2, param("глухой", grup)) : grup;
        assert true == elementVar2.check(stv_right_2, param("не глухой", grup)) : grup;

        grup = 37008;
        assert false == elementVar2.check(stv_right_2, param("глухой", grup)) : grup;
        assert true == elementVar2.check(stv_right_2, param("не глухой", grup)) : grup;

        grup = 37009;
        assert true == elementVar2.check(stv_right_2, param("Прямоугольное", grup)) : grup;
        assert false == elementVar2.check(stv_right_2, param("Арочное", grup)) : grup;
        assert false == elementVar2.check(stv_right_2, param("Произвольное", grup)) : grup;

        grup = 37010;
        assert true == elementVar2.check(glass_top_2, param("0-3000/0-334", grup)) : grup;
        assert false == elementVar2.check(glass_left_2, param("0-3000/0-334", grup)) : grup;

        grup = 37030;
        assert true == elementVar2.check(glass_top_2, param("0-6,5", grup)) : grup;
        assert false == elementVar2.check(glass_left_2, param("0-0,4", grup)) : grup;

        grup = 37042;
        assert true == elementVar2.check(glass_top_2, param("5-10", grup)) : grup;
        assert false == elementVar2.check(glass_left_2, param("1-10", grup)) : grup;

        grup = 37054;
        assert false == elementVar2.check(stv_right_2, param("10000-10999;17000-21999;23000-28999", grup)) : grup;
        assert true == elementVar2.check(stv_right_2, param("1;3;35-37;55;70;1009", grup)) : grup;

        grup = 37055;
        assert true == elementVar3.check(stv_right_3, param("1000-10010;", grup)) : grup;
        assert false == elementVar3.check(stv_right_3, param("1000-1010;", grup)) : grup;

        grup = 37056;
        assert true == elementVar4.check(stv_right_4, param("1000-10010;", grup)) : grup;
        assert true == elementVar3.check(stv_right_3, param("1000-1010;", grup)) : grup;

    }

    public void elementDet() {
        HashMap<Integer, String> mapParam = new HashMap();

        grup = 33000; //34000
        assert true == elementDet2.check(frame_left_2, param("KBE 58;XXX 58;", grup)) : grup;
        assert false == elementDet2.check(frame_left_2, param("KBE58;", grup)) : grup;

        grup = 33001; //34001 
        {
            Record rec = eElement.up.newRecord();
            rec.set(eElement.signset, "KBE");
            assert true == elementDet2.check(mapParam, frame_left_2, param("KBE", grup), rec) : grup;
            assert false == elementDet2.check(mapParam, frame_left_2, param("XXX", grup), rec) : grup;
        }

        grup = 33005; //31005, 33005, 37005
        assert true == elementDet2.check(frame_left_2, param("0-800;990;1009;1600-2000;", grup)) : grup;
        assert true == elementDet2.check(stv_right_2, param("0-1008;1009", grup)) : grup;
        assert false == elementDet2.check(stv_right_2, param("0-1008;1010", grup)) : grup;

        grup = 33006; //31006, 34006, 37006
        assert true == elementDet2.check(frame_left_2, param("0-800;990;1009;1600-2000;", grup)) : grup;
        assert true == elementDet2.check(stv_right_2, param("0-1008;1009", grup)) : grup;
        assert false == elementDet2.check(stv_right_2, param("0-1008;1010", grup)) : grup;

        grup = 33007; //31007, 34007, 37007
        assert true == elementDet2.check(frame_left_2, param("0-800;990;1009;1600-2000;", grup)) : grup;
        assert true == elementDet2.check(stv_right_2, param("0-1008;1009", grup)) : grup;
        assert false == elementDet2.check(stv_right_2, param("0-1008;1010", grup)) : grup;

        grup = 33008;
        assert true == elementDet2.check(null, param("30", grup)) : grup;
        assert false == elementDet2.check(null, param("32", grup)) : grup;

        grup = 33011;
        assert true == elementDet2.check(imp_vert_2, param("30/30", grup)) : grup;
        assert true == elementDet2.check(imp_vert_2, param("*/4-32", grup)) : grup;
        assert false == elementDet2.check(imp_vert_2, param("3;31;12/4-32", grup)) : grup;

        grup = 33017; //31017
        assert true == elementDet2.check(frame_left_2, param("КВЕ 58", grup)) : grup;
        assert false == elementDet2.check(frame_left_2, param("КП-40", grup)) : grup;

        grup = 33017; //34017
        assert true == elementDet2.check(frame_left_2, param("КВЕ 58", grup)) : grup;
        assert false == elementDet2.check(frame_left_2, param("КП-40", grup)) : grup;

        grup = 33063; //34064
        assert true == elementDet2.check(stv_right_2, param("3-40", grup)) : grup;
        assert true == elementDet2.check(stv_right_2, param("40", grup)) : grup;
        assert false == elementDet2.check(stv_right_2, param("1-12", grup)) : grup;

        grup = 33071;  //34071
        assert true == elementDet2.check(stv_right_2, param("1;2;3", grup)) : grup;
        assert true == elementDet2.check(stv_right_2, param("1-3", grup)) : grup;
        assert false == elementDet2.check(stv_right_2, param("1", grup)) : grup;

        grup = 33095; //34095, 38095, 39095, 40095
        assert true == elementDet2.check(stv_right_2, param("1;5;4;", grup)) : grup;
        assert false == elementDet2.check(stv_right_2, param("5;4;", grup)) : grup;

        grup = 34008; 
        assert true == elementDet2.check(null, param("30", grup)) : grup;
        assert false == elementDet2.check(null, param("32", grup)) : grup;
        
        grup = 34009; 
        assert true == elementDet3.check(frame_left_3, param("21315-04000;21316-01000", grup)) : grup;
        assert true == elementDet3.check(imp_vert_3, param("21315-04000;21315-04000", grup)) : grup;
        assert true == elementDet3.check(imp_vert_3, param("21316-01000;21315-04000;", grup)) : grup;
        assert true == elementDet3.check(stv_right_3, param("21316-05000;21315-04000;", grup)) : grup;
        assert false == elementDet3.check(stv_right_3, param("21316-X05000;21315-04000;", grup)) : grup;
    }
}
