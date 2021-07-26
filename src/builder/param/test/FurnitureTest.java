
package builder.param.test;

import enums.LayoutArea;

public class FurnitureTest extends ParamTest {

    public FurnitureTest() {
        super();
    }
    
    public void furnitureVar() {
        
        grup = 21001; //Форма контура
        assert true == furnitureVar2.check(frame_left_2, param("прямоугольная", grup)) : grup;
        assert false == furnitureVar3.check(frame_left_3, param("прямоугольная", grup)) : grup;        
        assert true == furnitureVar3.check(frame_left_3, param("арочная", grup)) : grup;        
        assert false == furnitureVar3.check(frame_left_3, param("не арочная", grup)) : grup;

        grup = 21004;  //Артикул створки 
        assert true == furnitureVar2.check(stv_right_2, param("917", grup)) : grup;
        assert true == furnitureVar3.check(stv_right_3, param("21316-05000", grup)) : grup;         
        assert false == furnitureVar3.check(stv_right_3, param("21316*05000", grup)) : grup;         

        grup = 21005;  //Артикул заполнения по умолчанию  
        assert true == furnitureVar3.check(stv_right_3, param("4x10x4x10x4", grup)) : grup;                
        assert false == furnitureVar3.check(stv_right_3, param("xxx", grup)) : grup;   
        
        grup = 21010; //Ограничение длины стороны, мм 
        assert true == furnitureVar3.check(stv_right_3, param("0-6000", grup)) : grup;                
        assert false == furnitureVar3.check(stv_right_3, param("5000-6000", grup)) : grup;   
        
        grup = 21013; //Ограничение длины ручка по середине, мм
        assert true == furnitureVar3.check(stv_right_3, param("940-1200", grup)) : grup;                
        assert false == furnitureVar3.check(stv_right_3, param("500", grup)) : grup;           
        
        grup = 21016; //Допустимое соотношение габаритов (б/м)
        assert true == furnitureVar2.check(stv_right_2, param("1,1-2,0", grup)) : grup;
        assert false == furnitureVar2.check(stv_right_2, param("1-1,09", grup)) : grup; 
        
        grup = 21040;  //Ограничение угла
        assert true == furnitureVar2.check(stv_right_2, param("74-360", grup)) : grup;
        assert false == furnitureVar2.check(stv_right_2, param("12-55", grup)) : grup;         
                
        //grup = 21050;  //Ориентация стороны 
    }
    
    public void furnitureDet() {
        
    }
}
