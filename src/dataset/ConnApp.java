package dataset;

import common.eProperty;
import static dataset.ConnFb.fbserver;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class ConnApp {

    private static ConnApp instanceClass = null;
    protected Connection connection = null;
    protected Statement statement = null;
    protected boolean autoCommit = false;

    public static ConnApp initConnect() {

        instanceClass = new ConnFb();
        return instanceClass;
    }

    public static ConnApp instanc() {
        return instanceClass;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setAutoCommit(boolean autoCommit) {
        try {
            connection.setAutoCommit(autoCommit);
        } catch (SQLException e) {
            System.out.println("dataset.IConnect.setAutoCommit() " + e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public abstract eExcep createConnection(String num_base);

    public abstract eExcep createConnection(String server, String port, String base, String user, char[] password);

    public abstract eExcep createConnection(String server, String port, String base, String user, char[] password, String role);

    public abstract void addUser(String user, String password, String uchId, String role, boolean rw);

    public abstract String getUser();

    public abstract void grantUser(String user, String password, String role, boolean rw);

    public abstract void deleteUser(String user);

    public abstract void modifyPassword(String user, String pass);

    public abstract int genId(Field field);

    public abstract String version();
}
