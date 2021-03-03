package common;

import java.util.Arrays;
import java.util.Set;


//Профили проекта
public enum eProfile {
       
    P02("SA-OKNA <АРМ Технолог>", "TEXNOLOG_RO", "TEXNOLOG_RW"),  //технолог
    P16("SA-OKNA <АРМ Менеджер>", "MANAGER_RO", "MANAGER_RW");  //менеджер

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
