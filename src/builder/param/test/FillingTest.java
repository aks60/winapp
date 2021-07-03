
package builder.param.test;

public class FillingTest extends ParamTest {
  
    /*
    select distinct e.id, e.name, b.id, b.name, a.text from glaspar1 a
    left join glasgrp b on b.id = a.glasgrp_id left join glasprof c on c.glasgrp_id = b.id
    left join sysprof d on d.artikl_id = c.artikl_id  left join systree e on e.id = d.systree_id
    where a.params_id = 13014   
    */
    public void fillingVar() {
        
        grup = 13001; //Если признак состава 
        //assert true == fillingVar2.check(frame_left_2, param("KBE 58", grup)) : grup;
        //assert false == fillingVar2.check(frame_left_2, param("KBE 5X", grup)) : grup;

        grup = 13003; //Тип проема
        assert false == fillingVar2.check(stv_right_2, param("глухой", grup)) : grup;
        assert true == fillingVar2.check(stv_right_2, param("не глухой", grup)) : grup;        

        grup = 13005; //Заполнение типа
        assert true == fillingVar3.check(glass_top_3, param("Стеклопакет", grup)) : grup;
        assert false == fillingVar3.check(glass_left_3, param("Стекло", grup)) : grup;        
    }
  
    public void fillingDet() {
        
    }
}
