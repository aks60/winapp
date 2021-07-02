
package builder.param.test;

public class FillingTest extends ParamTest {
  
    public void fillingVar() {
        
        grup = 13001; //Если признак состава 
        //assert true == fillingVar2.check(frame_left_2, param("KBE 58", grup)) : grup;
        //assert false == fillingVar2.check(frame_left_2, param("KBE 5X", grup)) : grup;

        grup = 13003; //Тип проема
        assert false == fillingVar2.check(stv_right_2, param("глухой", grup)) : grup;
        assert true == fillingVar2.check(stv_right_2, param("не глухой", grup)) : grup;        
    }
  
    public void fillingDet() {
        
    }
}
