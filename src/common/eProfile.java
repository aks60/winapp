package common;

import java.util.Arrays;
import java.util.Set;


//Профили проекта
public enum eProfile {
       
    P02("SA-OKNA <АРМ Технолог>", "technologRO", "technologRW"),  //технолог
    P16("SA-OKNA <АРМ Менеджер>", "managerRO", "managerRW");  //менеджер

    public final static int[] version = {1, 0}; //версия программы      
    public static eProfile profile = null; //профиль пользователя 
    public static String role = null;
    
    public String title; 
    public Set<String> roleSet;  
     
    //Конструктор
    eProfile(String title, String... role) {
        this.title = title;
        this.roleSet = Set.of(role);
    }
}
