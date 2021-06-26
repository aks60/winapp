package builder.param.test;

import static builder.param.test.ParamTest.param;

public class JoiningTest extends ParamTest {

    public void joiningVar() {

        grup = 1005; //2005, 3005, 4005
        assert true == joiningVar2.check(iwin_2.mapJoin.get(frame_left_2.joinPoint(0)), param("1/1", grup)) : grup;
        assert true == joiningVar2.check(iwin_2.mapJoin.get(imp_horiz_2.joinPoint(0)), param("3/1", grup)) : grup;
        
        grup = 1008;
        assert true == joiningVar2.check(null, param("30", grup)) : grup;
        assert false == joiningVar2.check(null, param("32", grup)) : grup;    
        
        grup = 1010; //4010
        assert true == joiningVar4.check(iwin_4.mapJoin.get(iwin_4.rootArea.x1 + ":" + iwin_4.rootArea.y1), param("Да", grup)) : grup;       
        assert true == joiningVar4.check(iwin_4.mapJoin.get(frame_left_4.joinPoint(0)), param("Да", grup)) : grup;       
        assert true == joiningVar4.check(iwin_4.mapJoin.get(stv_right_4.joinPoint(0)), param("Нет", grup)) : grup;       
        assert false == joiningVar4.check(iwin_4.mapJoin.get(stv_right_4.joinPoint(0)), param("Да", grup)) : grup; 
        
        grup = 1020;
        assert true == joiningVar4.check(iwin_4.mapJoin.get(stv_right_4.joinPoint(1)), param("30;270", grup)) : grup;
        assert false == joiningVar4.check(iwin_4.mapJoin.get(stv_right_4.joinPoint(1)), param("90", grup)) : grup;

        grup = 1039; 
        assert true == joiningVar2.check(iwin_2.mapJoin.get(stv_right_2.joinPoint(1)), param("поворотное", grup)) : grup;
        assert false == joiningVar2.check(iwin_2.mapJoin.get(stv_right_2.joinPoint(1)), param("поворотно-откидное", grup)) : grup;
        
        grup = 1043; 
        assert true == joiningVar2.check(null, param("2,45", grup)) : grup;
        assert true == joiningVar2.check(null, param("2,0-3,0", grup)) : grup;          
        assert false == joiningVar2.check(null, param("2,0", grup)) : grup; 
        
        grup = 1095; //2095, 3095, 4095;
        assert true == joiningVar4.check(iwin_4.mapJoin.get(stv_right_4.joinPoint(1)), param("1;2;", grup)) : grup;
        assert false == joiningVar4.check(iwin_4.mapJoin.get(stv_right_4.joinPoint(1)), param("2;9", grup)) : grup;  
        
        grup = 2003; //3003;
        assert false == joiningVar4.check(iwin_4.mapJoin.get(stv_right_4.joinPoint(1)), param("левый", grup)) : grup;
        assert true == joiningVar4.check(iwin_4.mapJoin.get(stv_right_4.joinPoint(1)), param("правый", grup)) : grup;  
        
        grup = 4020;
        assert false == joiningVar4.check(iwin_4.mapJoin.get(stv_right_4.joinPoint(1)), param("30-89,98;90,11-179,9;180,1-269,9;270,1-359,9;", grup)) : grup;
        assert true == joiningVar4.check(iwin_4.mapJoin.get(stv_right_4.joinPoint(1)), param("89,98-90,1", grup)) : grup;        
        assert true == joiningVar4.check(iwin_4.mapJoin.get(stv_right_4.joinPoint(1)), param("90", grup)) : grup; 
        
        grup = 2012; //3012;
        assert true == joiningVar4.check(iwin_4.mapJoin.get(frame_right_4.joinPoint(1)), param("Армирование", grup)) : grup;
        assert false == joiningVar4.check(iwin_4.mapJoin.get(stv_right_4.joinPoint(1)), param("_Армирование", grup)) : grup;        
        
        grup = 2015; //3015, 4015
        assert false == joiningVar4.check(iwin_4.mapJoin.get(frame_right_4.joinPoint(1)), param("0;180/*", grup)) : grup;
        assert true == joiningVar4.check(iwin_4.mapJoin.get(stv_right_4.joinPoint(1)), param("90/*", grup)) : grup;  
        
        grup = 2020; //3020, 4030
        assert false == joiningVar4.check(iwin_4.mapJoin.get(frame_right_4.joinPoint(1)), param("29-89,99;90,01-360", grup)) : grup;
        assert true == joiningVar4.check(iwin_4.mapJoin.get(stv_right_4.joinPoint(1)), param("90", grup)) : grup;          
    }

    public void joiningDet() {

    }
}
