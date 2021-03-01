package common;

import java.util.Arrays;
import java.util.Set;


//Профили проекта
public enum eProfile {
       
    P02("1.1.4", "technologRO", "technologRW"),  //технолог
    P16("1.1.8", "managerRO", "managerRW");  //менеджер

    public final static int[] version = {1, 0}; //версия программы      
    public static eProfile profile = null; //профиль пользователя 
    public static String role = null;
    
    public String config;    
    public Set<String> roleSet;  
     
    //Конструктор
    eProfile(String config, String... role) {
        this.config = config;
        this.roleSet = Set.of(role);
    }
}
