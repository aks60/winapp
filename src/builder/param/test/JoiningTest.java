package builder.param.test;

import static builder.param.test.ParamTest.param;

public class JoiningTest extends ParamTest {

    public void joiningVar() {

        grup = 1005;
        assert true == joiningVar2.check(frame_left_2.iwin().mapJoin.get(frame_left_2.joinPoint(0)), param("1/1", grup)) : grup;
        assert true == joiningVar2.check(imp_horiz_2.iwin().mapJoin.get(imp_horiz_2.joinPoint(0)), param("3/1", grup)) : grup;
        
        grup = 1008;
        assert true == joiningVar2.check(null, param("30", grup)) : grup;
        assert false == joiningVar2.check(null, param("32", grup)) : grup;    
        
        grup = 1010;
        //assert true == joiningVar2.check(iwin., param("30", grup)) : grup;       
    }

    public void joiningDet() {

    }
}
