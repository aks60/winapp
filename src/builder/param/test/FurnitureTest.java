
package builder.param.test;

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
    }
    
    public void furnitureDet() {
        
    }
}
