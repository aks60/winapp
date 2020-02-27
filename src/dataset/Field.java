package dataset;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;


//Поле таблицы
public interface Field extends Enam{

    public static String conf = "calc";             
    
    public static enum TYPE {

        OBJ(Object.class, 0), INT(Integer.class, 4, 5), NPP(Integer.class), INTsp(Integer.class),
        INTfp(Integer.class), STR(String.class, -9, -1, 1, 12), DBL(Double.class, 8), FLT(Float.class, 6), LONG(Long.class, -5),
        DATE(Date.class, 91, 92, 93), BOOL(Boolean.class, 16), BLOB(String.class, -2, -3, -4, 2004);
        Class type;
        public HashSet<Integer> hsConv = new HashSet<Integer>();

        TYPE(Class type, Integer... v) {
            this.type = type;
            for (Integer i : v) {
                hsConv.add(i);
            }
        }
        
        public static TYPE type(Object index) {
            index = Integer.valueOf(index.toString());
            for (TYPE type : values()) {
                if(type.hsConv.contains(index)) {
                    return type;
                }
            }
            return TYPE.OBJ;
        }
    };

    public static enum EDIT {

        TRUE(true), FALSE(false);
        Boolean edit;

        EDIT(boolean value) {
            this.edit = value;
        }
    };

    default String tname() {

        String str = this.getClass().toString().substring(14);
        StringBuffer str2 = new StringBuffer(str);
        int sep = 0;
        for (int index = 1; index < str.length(); ++index) {
            char ccc = str.charAt(index);
            if (Character.isUpperCase(str.charAt(index))) {
                str2.insert(index + sep++, "_");
            }
        }
        return str2.toString().toUpperCase();
    }
    
    public MetaField meta();
    
    default void virtualRec() throws SQLException {        
    }
    
//    default Query select() {
//        return null;
//    }
    
    default String update(Record record) {
        
        return null;
    }

    default String insert(Record record) {
        return null;
    }

    default String delete(Record record) {
        return null;
    }
    
    public boolean equals(Object other);    
}
