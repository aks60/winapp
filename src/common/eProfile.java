package common;

import com.sun.prism.impl.Disposer.Record;


/**
 * <p>
 * Профили проекта </p>
 */
public enum eProfile {
//TODO необходимо пересмотреть eProfile

    // admin
    P02(30, "30.2", "<АВЕРС: Директор-админ>", "OY_HO", "OY_HO1", "Группа №1", "OY_HO2", "Группа №2"),
    P04(30, "30.4", "<АВЕРС: Директор-админ>", "OY_HO", "OY_HO1", "Группа №1", "OY_HO2", "Группа №2"),
    // client
    P16(30, "30.16", "<АВЕРС: Директор>", "OY_HO", "OY_HO1", "Группа №1", "OY_HO2", "Группа №2", "OY_HO3", "Группа №3", "OY_HO4", "Группа №4");

    //ОЧЕНЬ ВАЖНО! Профайлер проекта
    //устанавливается перед выпуском
    public final static int[] version = {1, 0};
    public final static eProfile profile = eProfile.P16;
    //имя файла properties
    public final static String filename = "v" + String.valueOf(eProfile.profile.id) + ".properties";
    public static String role_user = "null";

    public static int[] getVersionBase() {
            return new int[]{1, 0};
    }
    
    /**
     * Конструктор 2
     */
    eProfile(int id, String config, String role_name, String role_filter, String... role) {
        this.id = id;
        this.config = config;
        this.role_name = role_name;
        this.role_filter = role_filter;
        this.role_key1 = (role.length > 0) ? role[0] : null;
        this.role_name1 = (role.length > 1) ? role[1] : null;
        this.role_key2 = (role.length > 2) ? role[2] : null;
        this.role_name2 = (role.length > 3) ? role[3] : null;
        this.role_key3 = (role.length > 4) ? role[4] : null;
        this.role_name3 = (role.length > 5) ? role[5] : null;
        this.role_key4 = (role.length > 6) ? role[6] : null;
        this.role_name4 = (role.length > 7) ? role[7] : null;
    }


    public int id;
    public String config;
    public String role_name;
    public String role_filter;
    public String paaword;
    //
    public String role_key1 = null;
    public String role_key2 = null;
    public String role_key3 = null;
    public String role_key4 = null;
    //
    public String role_name1 = null;
    public String role_name2 = null;
    public String role_name3 = null;
    public String role_name4 = null;
}
