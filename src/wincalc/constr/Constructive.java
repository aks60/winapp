package wincalc.constr;

import dataset.Query;
import domain.eArtikl;
import java.sql.Connection;
import java.sql.SQLException;

public class Constructive {

    public static boolean fromPS3 = false;  // Признак, что конструктив из ПС-3, а не из PS4.

    public static Query artikls = new Query(eArtikl.values());

    public static Connection connection() {

        String url = (fromPS3 == true) ? "jdbc:firebirdsql:localhost/3050:D:\\Okna\\Database\\Sialbase2\\base2.GDB?encoding=win1251"
                : "jdbc:firebirdsql:localhost/3050:D:\\Okna\\Database\\Profstroy4\\ITEST.FDB?encoding=win1251";
        try {
            Query.connection = java.sql.DriverManager.getConnection(url, "sysdba", "masterkey");
            return Query.connection;
        } catch (SQLException e) {
            System.err.println(e);
        }
        return null;
    }
}
