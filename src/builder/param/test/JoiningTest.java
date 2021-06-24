package builder.param.test;

import static builder.param.test.ParamTest.param;

public class JoiningTest extends ParamTest {

    public void joiningVar() {

        grup = 31000;
        assert true == elementVar2.check(frame_left_2, param("KBE 58;XXX 58;", grup)) : grup;
        assert false == elementVar2.check(frame_left_2, param("KBE58;", grup)) : grup;
    }

    public void joiningDet() {

    }
}
