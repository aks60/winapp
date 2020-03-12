package common;


/**
 * Профили проекта
 */
public enum eProfile {
    
    
    P02(1, "1.1", "SA-OKNA <АРМ Технолог>", "ROLE1", "ROLE2"),// admin
    P16(9, "9.1", "SA-OKNA <АРМ Менеджер>", "ROLE1", "ROLE2"); // client

    public final static int[] version = {1, 0}; //версия программы
    public final static eProfile profile = eProfile.P16; //профиль пользователя   
    public final static String filename = "v" + String.valueOf(eProfile.profile.id) + ".properties"; //имя файла properties
    public static String role_user = "null"; //права пользователя
    
    public int id;
    public String config;
    public String role_name;
    public String role_filter;
    public String role_key1 = null;
    public String role_key2 = null;
    public String role_name1 = null;
    public String role_name2 = null;   
     
    //Конструктор
    eProfile(int id, String config, String role_name, String role_filter, String... role) {
        this.id = id;
        this.config = config;
        this.role_name = role_name;
        this.role_filter = role_filter;
        this.role_key1 = (role.length > 0) ? role[0] : null;
        this.role_name1 = (role.length > 1) ? role[1] : null;
        this.role_key2 = (role.length > 2) ? role[2] : null;
        this.role_name2 = (role.length > 3) ? role[3] : null;
    }
}
