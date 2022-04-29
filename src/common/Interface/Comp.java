package common.Interface;

import builder.making.Specific;

public interface Comp {
  
    //Главная спецификация
    public void setSpecific();
    
    //Вложеная спецификация
    public void addSpecific(Specific spcAdd);
    
    public void paint();
}
