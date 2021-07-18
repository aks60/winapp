package builder.param.test;

import builder.making.Specific;
import builder.model.ElemFrame;
import common.Util;
import enums.LayoutArea;
import java.util.Map;

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

        grup = 13014; //Углы ориентации стороны, ° 
        fillingVar3.check(glass_left_3, param("0;90;180;270", grup));
        for (Map.Entry<LayoutArea, ElemFrame> it : glass_left_3.owner().mapFrame.entrySet()) {
            assert true == Util.containsNumb(glass_left_3.spcRec.mapParam.get(13014), it.getValue().anglHoriz);
        }
        fillingVar3.check(glass_left_3, param("10;20;30", grup));
        for (Map.Entry<LayoutArea, ElemFrame> it : glass_left_3.owner().mapFrame.entrySet()) {
            assert false == Util.containsNumb(glass_left_3.spcRec.mapParam.get(13014), it.getValue().anglHoriz);
        }
        
        grup = 13015;  //Форма заполнения
        assert true == fillingVar3.check(glass_left_3, param("Прямоугольное", grup)) : grup;
        assert true == fillingVar3.check(glass_top_3, param("Арочное", grup)) : grup;
        assert false == fillingVar3.check(glass_left_3, param("Не прямоугольное", grup)) : grup;  
        
        grup = 13017; //Код системы содержит строку
        assert true == fillingVar4.check(glass_left_4, param("me-1", grup)) : grup;
        assert false == fillingVar4.check(glass_left_3, param("КП-40", grup)) : grup;    
        
        grup = 13095; //Если признак системы конструкции
        assert true == fillingVar4.check(stv_right_4, param("1;2;", grup)) : grup;
        assert false == fillingVar4.check(stv_right_4, param("2;9", grup)) : grup;        
    }

    public void fillingDet() {
        
        grup = 14000; //15000 //Для технологического кода контейнера
        //assert true == fillingDet2.check(stv_right_2, param("KBE 58;XXX 58;", grup)) : grup;
        //assert false == fillingDet2.check(stv_right_2, param("KBE58;", grup)) : grup;
    }
}
